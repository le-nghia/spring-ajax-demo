package com.training.app.controller;

import com.training.app.entity.Customers;
import com.training.app.model.ResponseDataModel;
import com.training.app.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping( "customers")
public class CustomersController {

    private final ICustomerService customerService;

    @Autowired
    public CustomersController(ICustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public String init(){
        return "index";
    }

    @GetMapping("/api")
    @ResponseBody
    public ResponseDataModel findAll(){
        return customerService.findAll();
    }

    @GetMapping("/api/find")
    @ResponseBody
    public ResponseDataModel findBrandByIdApi(@RequestParam("id") Long id) {
        return customerService.findBrandByIdApi(id);
    }


    @PostMapping("/api/add")
    @ResponseBody
    public ResponseDataModel addApi(@ModelAttribute Customers customers) {
        return customerService.addApi(customers);
    }

    @PostMapping(value = {"/api/update"})
    @ResponseBody
    public ResponseDataModel updateApi(@ModelAttribute Customers customers) {
        return customerService.updateApi(customers);
    }

    @DeleteMapping(value = {"/api/delete/{id}"})
    @ResponseBody
    public ResponseDataModel delApi(@PathVariable("id") Long id) {
        return customerService.delApi(id);
    }

    @GetMapping(value = "/api/search/{customerName}")
    @ResponseBody
    public ResponseDataModel searchBrand(@PathVariable("customerName") String customerName) {
        return customerService.searchBrand(customerName);
    }
}
