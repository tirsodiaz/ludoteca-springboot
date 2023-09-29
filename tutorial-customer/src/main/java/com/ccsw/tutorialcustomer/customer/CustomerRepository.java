package com.ccsw.tutorialcustomer.customer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ccsw.tutorialcustomer.customer.model.Customer;

/**
 * @author ccsw
 *
 */
@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

}