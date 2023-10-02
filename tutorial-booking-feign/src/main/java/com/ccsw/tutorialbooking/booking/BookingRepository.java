package com.ccsw.tutorialbooking.booking;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ccsw.tutorialbooking.booking.model.Booking;

/**
 * @author ccsw
 *
 */
public interface BookingRepository
        extends CrudRepository<Booking, Long>, JpaSpecificationExecutor<Booking>, BookingRepositoryDynamic {

    // @EntityGraph(attributePaths = { "category", "author" }) // unica consulta
    // join, no multiples querys (quitar porque booking no incorpora objetos)
    List<Booking> findAll();

    List<Booking> findAll(Specification<Booking> spec);

    Page<Booking> findAll(Specification<Booking> spec, Pageable pageable);

    List<Booking> findByidCustomer(Long idCustomer);

    List<Booking> findByidGame(Long idGame);

    List<Booking> findByidCustomerAndIdGame(Long idCustomer, Long idGame);

    @Query("SELECT b FROM Booking b WHERE b.idGame BETWEEN ?1 AND ?2")
    List<Booking> findAllBookingQueryBetweentwoIdsGames(long idGame1, long idGame2);

    List<Booking> findAllBookingDynamicByIdsGames(List<Long> idGames);

}
