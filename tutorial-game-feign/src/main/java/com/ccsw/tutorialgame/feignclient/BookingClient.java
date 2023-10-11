package com.ccsw.tutorialgame.feignclient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ccsw.tutorialgame.model.BookingDto;

@FeignClient(value = "SPRING-CLOUD-EUREKA-CLIENT-BOOKING", url = "http://localhost:8080")
public interface BookingClient {

    @GetMapping(value = "/booking")
    List<BookingDto> findBookingbyIdGamesOrIdCustomer(@RequestParam(value = "idgames", required = false) String idgames,
            @RequestParam(value = "idCustomer", required = false) Long idCustomer);
}