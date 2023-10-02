package com.ccsw.tutorialbooking.booking;

import java.util.List;

import com.ccsw.tutorialbooking.booking.model.Booking;

public interface BookingRepositoryDynamic {
    List<Booking> findAllBookingDynamicByIdsGames(List<Long> idGames);
}
