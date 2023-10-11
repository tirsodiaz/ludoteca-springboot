package com.ccsw.tutorialbooking.feignclient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.ccsw.tutorialbooking.model.CustomerDto;

@FeignClient(value = "SPRING-CLOUD-EUREKA-CLIENT-CUSTOMER", url = "http://localhost:8080")
public interface CustomerClient {

    @GetMapping(value = "/customer")
    List<CustomerDto> findAll();
}
