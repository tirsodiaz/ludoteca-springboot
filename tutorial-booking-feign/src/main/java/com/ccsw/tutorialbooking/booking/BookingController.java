package com.ccsw.tutorialbooking.booking;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;
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

import com.ccsw.tutorialbooking.booking.exception.MyBadException;
import com.ccsw.tutorialbooking.booking.exception.MyConflictException;
import com.ccsw.tutorialbooking.booking.model.Booking;
import com.ccsw.tutorialbooking.booking.model.BookingDto;
import com.ccsw.tutorialbooking.feignclient.CustomerClient;
import com.ccsw.tutorialbooking.feignclient.GameClient;
import com.ccsw.tutorialbooking.model.CustomerDto;
import com.ccsw.tutorialbooking.model.GameDto;
import com.ccsw.tutorialbooking.pagination.WrapperPageableRequest;

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
    // DozerBeanMapper mapper; sobra, dto y entity son diferentes y no mapea

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
            @RequestParam(value = "idgames", required = false) String idgames,
            @RequestBody(required = true) WrapperPageableRequest dto) {

        TimeZone timeZone = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Madrid"));

        ZoneId zoneId = ZoneId.of("Europe/Madrid");
        ZoneOffset zoneOffset = ZonedDateTime.now(zoneId).getOffset(); // ZoneOffset.of("+02:00");
        zoneOffset = ZonedDateTime.now(ZoneId.of("Europe/Madrid")).getOffset();
        zoneOffset = OffsetDateTime.now(zoneId).getOffset();

        if (inicio != null) {
            inicio = inicio.replace(' ', '+'); // '+' se pierde en @RequestParam, aunque no en body
            OffsetDateTime odt = OffsetDateTime.parse(inicio);
            zoneOffset = odt.getOffset();
        }
        if (fin != null) {
            fin = fin.replace(' ', '+'); // '+' se pierde en @RequestParam, aunque no en body
            OffsetDateTime odt = OffsetDateTime.parse(fin);
            zoneOffset = odt.getOffset();
        }

        List<CustomerDto> customers = customerClient.findAll();
        List<GameDto> games = gameClient.find(null, null, null);
        List<Long> idGames = null;
        if (titulo != null) {
            idGames = games.stream().filter(g -> g.getTitle().contains(titulo)).map(g -> g.getId())
                    .collect(Collectors.toList());
        } else if (idgames != null) {
            idGames = Arrays.asList(idgames.split(",")).stream().map(i -> Long.valueOf(i)).toList();
        }

//      List<Booking> bookings = bookingService.findAll(idCustomer, inicio, fin, idGames);
        Page<Booking> page = bookingService.findAll(idCustomer, inicio, fin, idGames, dto);
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

    @Operation(summary = "FindBooking by game id list", description = "Method that return a filtered list of Booking by game id list")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<BookingDto> findBookingbyIdGamesOrIdCustomer(
            @RequestParam(value = "idgames", required = false) String idgames,
            @RequestParam(value = "idCustomer", required = false) Long idCustomer) {

        List<CustomerDto> customers = customerClient.findAll();
        List<GameDto> games = gameClient.find(null, null, null);

        List<Booking> bookings = null;
        if (idgames != null) {
            List<Long> idGames = Arrays.asList(idgames.split(",")).stream().map(i -> Long.valueOf(i)).toList();
            bookings = bookingService.findAllBookingbyIdGames(idGames);
        } else if (idCustomer != null) {
            bookings = bookingService.findAllBookingbyIdCustomer(idCustomer);
        }

        return bookings.stream().map(booking -> {
            BookingDto bookingDto = new BookingDto();

            bookingDto.setId(booking.getId());
            bookingDto.setInicio(booking.getInicio());
            bookingDto.setFin(booking.getFin());
            bookingDto.setCustomer(customers.stream()
                    .filter(customer -> customer.getId().equals(booking.getIdCustomer())).findFirst().orElse(null));
            bookingDto.setGame(
                    games.stream().filter(game -> game.getId().equals(booking.getIdGame())).findFirst().orElse(null));

            return bookingDto;
        }).collect(Collectors.toList());
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
            throws MyBadException, MyConflictException {
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