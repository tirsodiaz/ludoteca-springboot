package com.ccsw.tutorialbooking.booking;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.ccsw.tutorialbooking.booking.model.Booking;

/**
 * @author ccsw
 *
 */
public interface BookingRepository extends CrudRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {

    @Override
    // @EntityGraph(attributePaths = { "category", "author" }) // unica consulta
    // join, no multiples querys (quitar porque booking no incorpora objetos)
    List<Booking> findAll(Specification<Booking> spec);

    @Override
    Page<Booking> findAll(Specification<Booking> spec, Pageable pageable);

    List<Booking> findByidCustomer(Long idCustomer);

    List<Booking> findByidGame(Long idGame);

    List<Booking> findByidCustomerAndIdGame(Long idCustomer, Long idGame);

}
