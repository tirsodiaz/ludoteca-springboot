package com.ccsw.tutorialbooking.feignclient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ccsw.tutorialbooking.model.GameDto;

@FeignClient(value = "SPRING-CLOUD-EUREKA-CLIENT-GAME", url = "http://localhost:8080")
public interface GameClient {

    @GetMapping(value = "/game")
    List<GameDto> find(@RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "idCategory", required = false) Long idCategory,
            @RequestParam(value = "idAuthor", required = false) Long idAuthor);
}
