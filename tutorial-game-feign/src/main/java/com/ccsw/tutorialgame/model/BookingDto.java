package com.ccsw.tutorialgame.model;

import java.time.OffsetDateTime;

import com.ccsw.tutorialgame.game.model.GameDto;

/**
 * @author ccsw
 *
 */
public class BookingDto {

    private Long id;

    private CustomerDto customer;

    private GameDto game;

    private OffsetDateTime inicio;

    private OffsetDateTime fin;

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

    public OffsetDateTime getInicio() {
        return inicio;
    }

    public void setInicio(OffsetDateTime inicio) {
        this.inicio = inicio;
    }

    public OffsetDateTime getFin() {
        return fin;
    }

    public void setFin(OffsetDateTime fin) {
        this.fin = fin;
    }

}