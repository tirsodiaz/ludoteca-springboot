package com.ccsw.tutorialbooking.booking;

import org.springframework.data.jpa.domain.Specification;

import com.ccsw.tutorialbooking.booking.model.Booking;
import com.ccsw.tutorialbooking.common.criteria.SearchCriteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class BookingSpecification implements Specification<Booking> {

    private static final long serialVersionUID = 1L;

    private final SearchCriteria criteria;

    public BookingSpecification(SearchCriteria criteria) {

        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Booking> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (criteria.getOperation().equalsIgnoreCase(":") && criteria.getValue() != null) {
            Path<String> path = getPath(root);
            if (path.getJavaType() == String.class) {
                return cb.like(path, "%" + criteria.getValue() + "%");
            } else {
                return cb.equal(path, criteria.getValue());
            }
        } else if (criteria.getOperation().equalsIgnoreCase("in") && criteria.getValue() != null) {
            return cb.in(root.get(criteria.getKey())).value(criteria.getValue());
        } else if (criteria.getOperation().equalsIgnoreCase(">=") && criteria.getValue() != null) {
            return cb.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase("<=") && criteria.getValue() != null) {
            return cb.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
        }
        return null;
    }

    private Path<String> getPath(Root<Booking> root) {
        String key = criteria.getKey();
        String[] split = key.split("[.]", 0);

        Path<String> expression = root.get(split[0]);
        for (int i = 1; i < split.length; i++) {
            expression = expression.get(split[i]);
        }

        return expression;
    }

}
