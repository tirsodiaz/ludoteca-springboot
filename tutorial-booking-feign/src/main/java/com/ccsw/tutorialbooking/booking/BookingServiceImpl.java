package com.ccsw.tutorialbooking.booking;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ccsw.tutorialbooking.booking.exceptions.MyExceptions;
import com.ccsw.tutorialbooking.booking.model.Booking;
import com.ccsw.tutorialbooking.booking.model.BookingDto;
import com.ccsw.tutorialbooking.booking.model.BookingSearchDto;
import com.ccsw.tutorialbooking.common.criteria.SearchCriteria;

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
    public List<Booking> findAll(Long idCustomer, String inicio, String fin, Long idGame) {

        BookingSpecification customerSpec = new BookingSpecification(new SearchCriteria("idCustomer", ":", idCustomer));
        BookingSpecification inicioSpec = new BookingSpecification(new SearchCriteria("inicio", ">=", inicio));
        BookingSpecification finSpec = new BookingSpecification(new SearchCriteria("fin", "<=", fin));
        BookingSpecification tituloSpec = new BookingSpecification(new SearchCriteria("idGame", ":", idGame));

        Specification<Booking> spec = Specification.where(customerSpec).and(inicioSpec).and(finSpec).and(tituloSpec);

        return this.bookingRepository.findAll(spec);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Booking> findAll(Long idCustomer, String inicio, String fin, Long idGame, BookingSearchDto dto) {

        // consultas por nomenclatura
        // List<Booking> findByidCustomer =
        // bookingRepository.findByidCustomer(idCustomer);
        // List<Booking> findByidCustomerAndIdGame =
        // bookingRepository.findByidCustomerAndIdGame(idCustomer, idGame);

        BookingSpecification customerSpec = new BookingSpecification(new SearchCriteria("idCustomer", ":", idCustomer));
        BookingSpecification inicioSpec = new BookingSpecification(new SearchCriteria("inicio", ">=", inicio));
        BookingSpecification finSpec = new BookingSpecification(new SearchCriteria("fin", "<=", fin));
        BookingSpecification tituloSpec = new BookingSpecification(new SearchCriteria("idGame", ":", idGame));

        Specification<Booking> spec = Specification.where(customerSpec).and(inicioSpec).and(finSpec).and(tituloSpec);

        return this.bookingRepository.findAll(spec, dto.getPageable().getPageable());
    }

    /**
     * {@inheritDoc}
     * 
     * @throws Exception
     */
    @Override
    public ResponseEntity<Void> save(Long id, BookingDto dto) throws Exception {

        Booking booking;

        // reglas de negocio
        if (dto.getFin().before(dto.getInicio())) {
            throw new Exception("Fecha Inicio > Fecha Fin");
        }
        Instant instant1 = dto.getInicio().toInstant();
        Instant instant2 = dto.getFin().toInstant();
        if (Duration.between(instant1, instant2).toDays() > 15) {
            // throw new Exception("Periodo Prestamo > 15 dias");
            throw new MyExceptions("Periodo Prestamo > 15 dias");
        }

        List<Booking> bookingListByCustomer = findAll(dto.getCustomer().getId(), null, null, null);
        if (bookingListByCustomer.size() >= 2) {
            StringBuffer sb = new StringBuffer("Prestamos por customer >= 2, (");
            sb.append(bookingListByCustomer.size()).append(")");
            // throw new Exception(sb.toString());
            throw new MyExceptions(sb.toString());
        }

        List<Booking> bookingListByGame = findAll(null, null, null, dto.getGame().getId());
        // bookingListByGame = bookingRepository.findByidGame(dto.getGame().getId());

        List<Booking> lista = bookingListByGame.stream().filter(b -> b.getInicio().after(dto.getInicio()))
                .filter(b -> b.getInicio().before(dto.getFin())).toList();
        if (lista.size() > 0) {
            StringBuffer sb = new StringBuffer("Game reservado por interseccion fecha inicio [");
            sb.append(lista.get(0).getInicio()).append("-").append(lista.get(0).getFin()).append("]");
            // throw new Exception(sb.toString());
            throw new MyExceptions(sb.toString());
        }

        lista = bookingListByGame.stream().filter(b -> b.getFin().after(dto.getInicio()))
                .filter(b -> b.getFin().before(dto.getFin())).toList();
        if (lista.size() > 0) {
            StringBuffer sb = new StringBuffer("Game reservado por interseccion fecha fin [");
            sb.append(lista.get(0).getInicio()).append("-").append(lista.get(0).getFin()).append("]");
            // throw new Exception(sb.toString());
            throw new MyExceptions(sb.toString());
        }

        lista = bookingListByGame.stream().filter(b -> b.getFin().after(dto.getFin()))
                .filter(b -> b.getInicio().before(dto.getInicio())).toList();
        if (lista.size() > 0) {
            StringBuffer sb = new StringBuffer("Game reservado por interseccion fecha inicio y fecha fin [");
            sb.append(lista.get(0).getInicio()).append("-").append(lista.get(0).getFin()).append("]");
            // throw new Exception(sb.toString());
            throw new MyExceptions(sb.toString());
        }

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

}