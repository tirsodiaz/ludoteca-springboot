package com.ccsw.tutorialbooking.booking;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.ccsw.tutorialbooking.booking.exception.MyBadException;
import com.ccsw.tutorialbooking.booking.exception.MyConflictException;
import com.ccsw.tutorialbooking.booking.model.Booking;
import com.ccsw.tutorialbooking.booking.model.BookingDto;
import com.ccsw.tutorialbooking.pagination.WrapperPageableRequest;

/**
 * @author ccsw
 *
 */
public interface BookingService {

    /**
     * Recupera los reservas filtrando opcionalmente por customer y/o fechas
     *
     * @param idCustomer PK Customer
     * @param inicio     inicio reserva
     * @param fin        booking reserva
     * @return {@link List} de {@link Booking}
     */
    List<Booking> findAll(Long idCustomer, String inicio, String fin, List<Long> idGames);

    Page<Booking> findAll(Long idCustomer, String inicio, String fin, List<Long> idGames, WrapperPageableRequest dto);

    List<Booking> findAllBookingbyIdGames(List<Long> gameList);

    /**
     * Guarda o modifica una reserva, dependiendo de si el identificador está o no
     * informado
     *
     * @param id  PK de la entidad
     * @param dto datos de la entidad
     * @return
     * @throws Exception
     */
    ResponseEntity<Void> save(Long id, BookingDto dto) throws MyBadException, MyConflictException;

    /**
     * Borra una reserva por identificador
     * 
     * @param id PK de la entidad
     */
    void delete(Long id) throws Exception;

    /**
     * Recupera una {@link Booking} a partir de su ID
     *
     * @param id PK de la entidad
     * @return {@link Booking}
     */
    Booking get(Long id);

    List<Booking> findAllBookingbyIdCustomer(Long idCustomer);
}
