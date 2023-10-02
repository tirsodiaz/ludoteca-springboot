package com.ccsw.tutorialbooking.booking;

import java.util.ArrayList;
import java.util.List;

import com.ccsw.tutorialbooking.booking.model.Booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class BookingRepositoryDynamicImpl implements BookingRepositoryDynamic {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Booking> findAllBookingDynamicByIdsGames(List<Long> idsGames) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Booking> query = cb.createQuery(Booking.class);
        Root<Booking> bookingRoot = query.from(Booking.class);

        In<String> inClause = cb.in(bookingRoot.get("idGame"));
        for (Long idGame : idsGames) {
            inClause.value(String.valueOf(idGame));
        }
        query.select(bookingRoot).where(inClause);
        List<Booking> resultList = entityManager.createQuery(query).getResultList();

        List<Predicate> predicates = new ArrayList<>();
        Path<String> idgamePath = bookingRoot.get("idGame");
        Predicate in = idgamePath.in(idsGames);
        predicates.add(in);
        // predicates.add(cb.equal(bookingRoot.get("idCustomer"), 2));
        query.select(bookingRoot).where(in);
        resultList = entityManager.createQuery(query).getResultList();

        query.select(bookingRoot).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        return entityManager.createQuery(query).getResultList();
    }
}