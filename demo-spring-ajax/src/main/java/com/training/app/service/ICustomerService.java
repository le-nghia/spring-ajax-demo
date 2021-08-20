package com.training.app.service;

import com.training.app.entity.Customers;
import com.training.app.model.ResponseDataModel;

public interface ICustomerService {

    Customers findByCustomerName(String name);

    ResponseDataModel findAll();
    ResponseDataModel findBrandByIdApi(Long id);

    ResponseDataModel addApi(Customers customers);
    ResponseDataModel updateApi(Customers customers);
    ResponseDataModel delApi(Long id);

    ResponseDataModel searchBrand(String brandName);

}
