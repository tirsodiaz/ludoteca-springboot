package com.ccsw.tutorialbooking.booking;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ccsw.tutorialbooking.booking.exception.MyBadAdviceException;
import com.ccsw.tutorialbooking.booking.exception.MyBadException;
import com.ccsw.tutorialbooking.booking.exception.MyConflictAdviceException;
import com.ccsw.tutorialbooking.booking.exception.MyConflictException;
import com.ccsw.tutorialbooking.booking.model.Booking;
import com.ccsw.tutorialbooking.booking.model.BookingDto;
import com.ccsw.tutorialbooking.common.criteria.SearchCriteria;
import com.ccsw.tutorialbooking.pagination.WrapperPageableRequest;

import jakarta.transaction.Transactional;

/**
 * @author ccsw
 *
 */
@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    @Autowired
    BookingRepository bookingRepository;

    // @Autowired AuthorService authorService;

    // @Autowired CategoryService categoryService;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Booking> findAll(Long idCustomer, String inicio, String fin, List<Long> idGames) {

        BookingSpecification customerSpec = new BookingSpecification(new SearchCriteria("idCustomer", ":", idCustomer));
        BookingSpecification inicioSpec = new BookingSpecification(new SearchCriteria("inicio", ">=", inicio));
        BookingSpecification finSpec = new BookingSpecification(new SearchCriteria("fin", "<=", fin));
        BookingSpecification tituloSpec = new BookingSpecification(new SearchCriteria("idGame", "in", idGames));

        Specification<Booking> spec = Specification.where(customerSpec).and(inicioSpec).and(finSpec).and(tituloSpec);

        return this.bookingRepository.findAll(spec);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Booking> findAll(Long idCustomer, String inicio, String fin, List<Long> idGames,
            WrapperPageableRequest dto) {

        // consultas por nomenclatura
        // List<Booking> findByidCustomer =
        // bookingRepository.findByidCustomer(idCustomer);
        // List<Booking> findByidCustomerAndIdGame =
        // bookingRepository.findByidCustomerAndIdGame(idCustomer, idGame);

        BookingSpecification customerSpec = new BookingSpecification(new SearchCriteria("idCustomer", ":", idCustomer));
        BookingSpecification inicioSpec = new BookingSpecification(new SearchCriteria("inicio", ">=", inicio));
        BookingSpecification finSpec = new BookingSpecification(new SearchCriteria("fin", "<=", fin));
        BookingSpecification tituloSpec = new BookingSpecification(new SearchCriteria("idGame", "in", idGames));

        Specification<Booking> spec = Specification.where(customerSpec).and(inicioSpec).and(finSpec).and(tituloSpec);

        return this.bookingRepository.findAll(spec, dto.getPageableRequest().getPageable());
    }

    @Override
    public List<Booking> findAllBookingbyIdGames(List<Long> idGames) {
        if (idGames.size() == 2)
            return this.bookingRepository.findAllBookingQueryBetweentwoIdsGames(idGames.get(0), idGames.get(1));
        else
            return this.bookingRepository.findAllBookingDynamicByIdsGames(idGames);

    }

    @Override
    public List<Booking> findAllBookingbyIdCustomer(Long idCustomer) {
        return this.bookingRepository.findByidCustomer(idCustomer);
    }

    /**
     * {@inheritDoc}
     * 
     * @throws Exception
     */
    @Override
    public ResponseEntity<Void> save(Long id, BookingDto dto) throws MyBadException, MyConflictException {

        Booking booking;
        // reglas de negocio
        TimeZone timeZone = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Madrid"));

        ZoneId zoneId = ZoneId.of("Europe/Madrid");
        ZoneOffset zoneOffset = ZonedDateTime.now(zoneId).getOffset(); // ZoneOffset.of("+02:00");
        zoneOffset = ZonedDateTime.now(ZoneId.of("Europe/Madrid")).getOffset();
        zoneOffset = OffsetDateTime.now(zoneId).getOffset();

        OffsetDateTime finodt = OffsetDateTime.parse(dto.getFin().toString());
        Instant finInstant = dto.getFin().withOffsetSameInstant(zoneOffset).toInstant();
        Instant inicioInstant = dto.getInicio().toZonedDateTime().toInstant();

        if (finInstant.isBefore(inicioInstant) || dto.getFin().isBefore(dto.getInicio())
                || Duration.between(inicioInstant, finInstant).isNegative()) {
            // throw new MyBadException("Fecha Inicio > Fecha Fin");
            throw new MyBadAdviceException("Fecha Inicio > Fecha Fin");
        }

        ZonedDateTime finzdt = dto.getFin().withOffsetSameInstant(zoneOffset).toZonedDateTime();
        ZonedDateTime iniciozdt = dto.getInicio().withOffsetSameInstant(zoneOffset).toZonedDateTime();
        // Calendar.getInstance().toInstant().atZone(ZoneId.of("Europe/Madrid"))
        if (finzdt.isBefore(iniciozdt)) {
            // throw new MyBadException("Fecha Inicio > Fecha Fin");
            throw new MyBadAdviceException("Fecha Inicio > Fecha Fin");
        }

        if (Duration.between(inicioInstant, finInstant).toDays() > 15) {
            // throw new MyBadException("Periodo Prestamo > 15 dias");
            throw new MyBadAdviceException("Periodo Prestamo > 15 dias");
        }

        // Instant instant = Calendar.getInstance(timeZone).toInstant();
        OffsetDateTime nowOffsetDateTime = OffsetDateTime.now(zoneId);
        // nowOffsetDateTime.toString(); "2023-09-29T09:36:57.455809600+02:00"
        List<Booking> bookingListByCustomer = findAll(dto.getCustomer().getId(), null, null, null);
        if (id != null)
            bookingListByCustomer.removeIf(b -> b.getId() == id);

        if (bookingListByCustomer.stream().filter(b -> b.getFin().isAfter(nowOffsetDateTime)).count() >= 2) {
            StringBuffer sb = new StringBuffer("Solo se permite 2 reservas por customer >= 2, (");
            sb.append(bookingListByCustomer.size()).append(")");
            // throw new MyBadException(sb.toString());
            throw new MyBadAdviceException(sb.toString());
        }

        List<Booking> bookingListByGame = findAll(null, null, null, List.of(dto.getGame().getId()));
        // bookingListByGame = bookingRepository.findByidGame(dto.getGame().getId());
        List<Booking> lista = bookingListByGame.stream().filter(b -> b.getFin().isAfter(dto.getFin()))
                .filter(b -> b.getInicio().isBefore(dto.getInicio())).toList();
        if (lista.size() > 0) {
            StringBuffer sb = new StringBuffer(
                    "Game reservado solicitud todos los días ya reservados en bbdd fecha inicio y fecha fin [");
            sb.append(lista.get(0).getInicio()).append("-").append(lista.get(0).getFin()).append("]");
            // throw new MyConflictException(sb.toString());
            throw new MyConflictAdviceException(sb.toString());
        }

        lista = bookingListByGame.stream().filter(b -> odtToZdt(b.getInicio()).isAfter(odtToZdt(dto.getInicio())))
                .filter(b -> odtToZdt(b.getInicio()).isBefore(odtToZdt(dto.getFin()))).toList();
        if (lista.size() > 0) {
            StringBuffer sb = new StringBuffer("Game reservado en fecha inicio solicitud [");
            sb.append(lista.get(0).getInicio()).append("-").append(lista.get(0).getFin()).append("]");
            // throw new MyConflictException(sb.toString());
            throw new MyConflictAdviceException(sb.toString());
        }

        lista = bookingListByGame.stream().filter(b -> b.getFin().isAfter(dto.getInicio()))
                .filter(b -> b.getFin().isBefore(dto.getFin())).toList();
        if (lista.size() > 0) {
            StringBuffer sb = new StringBuffer("Game reservado en fecha fin solicitud [");
            sb.append(lista.get(0).getInicio()).append("-").append(lista.get(0).getFin()).append("]");
            // throw new MyConflictException(sb.toString());
            throw new MyConflictAdviceException(sb.toString());
        }
        // fin reglas de negocio

        if (id == null) {
            booking = new Booking();
        } else {
            booking = this.get(id);
        }

        BeanUtils.copyProperties(dto, booking, "id", "idCustomer", "idGame");

        booking.setIdCustomer(dto.getCustomer().getId());
        booking.setIdGame(dto.getGame().getId());

        this.bookingRepository.save(booking);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws Exception {

        if (this.get(id) == null) {
            throw new Exception("Not exists");
        }

        this.bookingRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Booking get(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    private ZonedDateTime odtToZdt(OffsetDateTime odt) {
        return odt.toZonedDateTime();
    }

}