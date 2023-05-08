package com.ccsw.tutorialbooking.booking.model;

import java.util.Date;

import com.ccsw.tutorialbooking.customer.model.CustomerDto;
import com.ccsw.tutorialbooking.game.model.GameDto;

/**
 * @author ccsw
 *
 */
public class BookingDto {

    private Long id;

    private CustomerDto customer;

    private GameDto game;

    private Date inicio;

    private Date fin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomerDto getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDto customer) {
        this.customer = customer;
    }

    public GameDto getGame() {
        return game;
    }

    public void setGame(GameDto game) {
        this.game = game;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

}