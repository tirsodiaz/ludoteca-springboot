package com.ccsw.tutorialbooking.booking;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ccsw.tutorialbooking.booking.exceptions.MyBadExceptions;
import com.ccsw.tutorialbooking.booking.exceptions.MyConflictExceptions;
import com.ccsw.tutorialbooking.booking.model.Booking;
import com.ccsw.tutorialbooking.booking.model.BookingDto;
import com.ccsw.tutorialbooking.booking.model.BookingSearchDto;
import com.ccsw.tutorialbooking.customer.CustomerClient;
import com.ccsw.tutorialbooking.customer.model.CustomerDto;
import com.ccsw.tutorialbooking.game.GameClient;
import com.ccsw.tutorialbooking.game.model.GameDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author ccsw
 *
 */
@Tag(name = "Booking", description = "API of Booking")
@RequestMapping(value = "/booking")
@RestController
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    BookingService bookingService;

    // @Autowired
    // DozerBeanMapper mapper; sobra, dto y entity son diferentes no puede hacerse
    // mapeo automatico completo

    @Autowired
    CustomerClient customerClient;

    @Autowired
    GameClient gameClient;

    /**
     * Método para recuperar una lista de {@link Booking}
     *
     * @param idCustomer PK customer
     * @param inicio     booking
     * @param fin        booking
     * @return {@link Page} de {@link BookingDto}
     */
    @Operation(summary = "Find", description = "Method that return a filtered list of Booking")
    @RequestMapping(path = "", method = RequestMethod.POST)
    // public List<BookingDto> find(
    public Page<BookingDto> find(@RequestParam(value = "idCustomer", required = false) Long idCustomer,
            @RequestParam(value = "inicio", required = false) String inicio,
            @RequestParam(value = "fin", required = false) String fin,
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestBody(required = true) BookingSearchDto dto) {

        List<CustomerDto> customers = customerClient.findAll();
        List<GameDto> games = gameClient.find(null, null);

        GameDto gameDto = games.stream().filter(g -> g.getTitle().equals(titulo)).findFirst().orElse(null);
        Long idGame = null;
        if (gameDto != null) {
            idGame = gameDto.getId();
        }

//      List<Booking> bookings = bookingService.findAll(idCustomer, inicio, fin, idGame);
        Page<Booking> page = bookingService.findAll(idCustomer, inicio, fin, idGame, dto);
        List<Booking> bookings = page.getContent();

        return new PageImpl<>(bookings.stream().map(booking -> {
            BookingDto bookingDto = new BookingDto();

            bookingDto.setId(booking.getId());
            bookingDto.setInicio(booking.getInicio());
            bookingDto.setFin(booking.getFin());
            bookingDto.setCustomer(customers.stream()
                    .filter(customer -> customer.getId().equals(booking.getIdCustomer())).findFirst().orElse(null));
            bookingDto.setGame(
                    games.stream().filter(game -> game.getId().equals(booking.getIdGame())).findFirst().orElse(null));

            return bookingDto;
        }).collect(Collectors.toList()), page.getPageable(), page.getTotalElements());

    }

    /**
     * Método para crear o actualizar un {@link Booking}
     *
     * @param id  PK de la entidad
     * @param dto datos de la entidad
     * @throws Exception
     */
    @Operation(summary = "Save or Update", description = "Method that saves or updates a Booking")
    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.PUT)
    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody BookingDto dto)
            throws MyBadExceptions, MyConflictExceptions {
        bookingService.save(id, dto);
    }

    /**
     * Método para borrar un booking
     *
     * @param id PK de la entidad
     * @throws Exception
     */
    @Operation(summary = "Delete", description = "Method that deletes a Booking")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) throws Exception {

        this.bookingService.delete(id);
    }

}