package com.training.app.service.impl;

import com.training.app.entity.Customers;
import com.training.app.model.ResponseDataModel;
import com.training.app.repository.CustomerRepository;
import com.training.app.service.ICustomerService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerServiceImpl implements ICustomerService {

    private final Logger LOG = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customers findByCustomerName(String name) {
        return customerRepository.findByCustomerName(name);
    }

    @Override
    public ResponseDataModel findAll() {
        int responseCode = 0;
        String responseMsg = StringUtils.EMPTY;
        Map<String, Object> responseMap = new HashMap<>();
        try {
            Sort sortInfo = Sort.by(Sort.Direction.DESC, "customerid");
            List<Customers> brandEntitiesPage = customerRepository.findAll(sortInfo);
            responseMap.put("customerList", brandEntitiesPage);
            responseCode = 100;
        } catch (Exception e) {
            responseMsg = e.getMessage();
        }
        return new ResponseDataModel(responseCode, responseMsg, responseMap);
    }

    @Override
    public ResponseDataModel addApi(Customers customers) {

        int responseCode = 0;
        String responseMsg;

        try {
            if (findByCustomerName(customers.getCustomername()) != null) {
                responseMsg = "Customer Name is duplicated";
                responseCode = 1;
            } else {
                customerRepository.saveAndFlush(customers);
                responseMsg = "Customer is added successfully";
                responseCode = 100;
            }
        } catch (Exception e) {
            responseMsg = "Error when adding Customer";
            LOG.error("Error when adding Customer: ", e);
        }
        return new ResponseDataModel(responseCode, responseMsg);
    }

    @Override
    public ResponseDataModel findBrandByIdApi(Long id) {
        int responseCode = 0;
        String responseMsg = StringUtils.EMPTY;
        Customers customers = null;
        try {
            customers = customerRepository.findByCustomerId(id);
            if (customers != null) {
                responseCode = 100;
            }
        } catch (Exception e) {
            responseMsg = "Error when finding Customer by ID";
            LOG.error("Error when finding Customer by ID: ", e);
        }
        return new ResponseDataModel(responseCode, responseMsg, customers);
    }

    @Override
    public ResponseDataModel updateApi(Customers customers) {
        int responseCode = 0;
        String responseMsg;
        try {
            Customers duplicatedBrand = customerRepository.findByCustomerNameAndBrandIdNot(customers.getCustomername(), customers.getCustomerid());
            // Check if brand name existed
            if (duplicatedBrand != null) {
                responseMsg = "Customer Name is duplicated";
                responseCode = 1;
            } else {
                customerRepository.saveAndFlush(customers);
                responseMsg = "Customer is updated successfully";
                responseCode = 100;
            }
        } catch (Exception e) {
            responseMsg = "Error when updating Customer";
            LOG.error("Errorr when updating Customer: ", e);
        }
        return new ResponseDataModel(responseCode, responseMsg);
    }

    @Override
    public ResponseDataModel delApi(Long id) {
        int responseCode = 0;
        String responseMsg = StringUtils.EMPTY;
        Customers customers = customerRepository.findByCustomerId(id);
        try {
            if (customers != null) {
                customerRepository.deleteById(id);
                customerRepository.flush();

                // Remove logo of brand from store folder
//                FileHelper.deleteFile(brandEntity.getLogo());
                responseMsg = "Brand is deleted successfully";
                responseCode = 100;
            }
        } catch(Exception e) {
            responseMsg = "Error when deleting brand";
        }
        return new ResponseDataModel(responseCode, responseMsg);
    }

    @Override
    public ResponseDataModel searchBrand(String customerName) {
        int responseCode = 0;
        String responseMsg;
        Map<String, Object> responseMap = new HashMap<>();
        try {
            List<Customers> customersPage = customerRepository.findByCustomerNameLike("%" + customerName + "%");
            responseMap.put("customerList", customersPage);
            responseCode = 100;
            if ( (long) customersPage.size() > 0) {
                responseMsg = "The number of brand found is " + customersPage.size() + " customers";
            } else {
                responseMsg = "The " + customerName + " is not exist!";
            }
        } catch (Exception e) {
            responseMsg = e.getMessage();
            LOG.error("Search brand name failed:",e);
        }
        return new ResponseDataModel(responseCode, responseMsg, responseMap);
    }
}
