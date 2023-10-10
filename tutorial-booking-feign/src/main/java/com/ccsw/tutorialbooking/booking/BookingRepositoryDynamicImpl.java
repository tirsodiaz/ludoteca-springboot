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
        List<Predicate> predicates = new ArrayList<>();

        In<String> inClause = cb.in(bookingRoot.get("idGame"));
        for (Long idGame : idsGames) {
            inClause.value(String.valueOf(idGame));
        }
        // predicates.add(inClause);
        // query.select(bookingRoot).where(predicates.get(0));
        query.select(bookingRoot).where(inClause);
        List<Booking> resultList = entityManager.createQuery(query).getResultList();

        Path<String> idgamePath = bookingRoot.get("idGame");
        Predicate in = idgamePath.in(idsGames);
        // predicates.add(in);
        // predicates.add(inClause);
        // predicates.add(cb.like(idgamePath, "%2%"));
        // predicates.add(cb.equal(idgamePath, 2));
        // predicates.add(cb.greaterThanOrEqualTo(idgamePath, "2"));
        // predicates.add(cb.lessThanOrEqualTo(idgamePath, "2"));
        // predicates.add(cb.equal(bookingRoot.get("idCustomer"), 2));
        query.select(bookingRoot).where(in);
        resultList = entityManager.createQuery(query).getResultList();
        Predicate[] arrayPredicates = new Predicate[predicates.size()];
        query.select(bookingRoot).where(cb.and(predicates.toArray(arrayPredicates)));
        return entityManager.createQuery(query).getResultList();
    }
}