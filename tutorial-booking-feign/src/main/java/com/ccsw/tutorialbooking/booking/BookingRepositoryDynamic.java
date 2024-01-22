package com.ccsw.tutorialbooking.booking;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ccsw.tutorialbooking.booking.model.Booking;

public interface BookingRepositoryDynamic {
    List<Booking> findAllBookingDynamic(Long idCustomer, String inicio, String fin, List<Long> idsGames);

    Page<Booking> findPageAllBookingDynamic(Long idCustomer, String inicio, String fin, List<Long> idsGames,
            Pageable pageable);

}
