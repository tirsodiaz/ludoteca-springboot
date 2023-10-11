package com.ccsw.tutorialcustomer.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.tutorialcustomer.customer.exception.MyConflictAdviceException;
import com.ccsw.tutorialcustomer.customer.exception.MyConflictException;
import com.ccsw.tutorialcustomer.customer.model.Customer;
import com.ccsw.tutorialcustomer.customer.model.CustomerDto;
import com.ccsw.tutorialcustomer.feignclient.BookingClient;
import com.ccsw.tutorialcustomer.model.BookingDto;

import jakarta.transaction.Transactional;

/**
 * @author ccsw
 *
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BookingClient bookingClient;

    /**
     * {@inheritDoc}
     */
    @Override
    public Customer get(Long id) {

        return this.customerRepository.findById(id).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Customer> findAll() {

        return (List<Customer>) this.customerRepository.findAll();
    }

    /**
     * {@inheritDoc}
     * 
     * @throws Exception
     */
    @Override
    public void save(Long id, CustomerDto dto) throws Exception {

        Customer customer;
        // regla de negocio, no existe cliente con el mismo nombre
        boolean exists = findAll().stream().filter(c -> c.getName().equalsIgnoreCase(dto.getName())).findFirst()
                .isPresent();
        if (exists) {
            throw new MyConflictException("Cliente nombre duplicadoooo");
        }

        if (id == null) {
            customer = new Customer();
        } else {
            customer = this.get(id);
            if (customer == null) {
                throw new MyConflictException("Cliente id no existe");
            }
        }

        customer.setName(dto.getName()); // BeanUtils.copyProperties(dto, customer, "id");

        this.customerRepository.save(customer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws Exception {

        if (this.get(id) == null) {
            throw new Exception("Not exists");
        }

        List<BookingDto> bookings = bookingClient.findBookingbyIdGamesOrIdCustomer(null, id);
        if (bookings.isEmpty() || bookings.size() == 0)
            this.customerRepository.deleteById(id);
        else
            throw new MyConflictAdviceException("Customer already used in booking!");
    }

    @Override
    public Customer findById(Long id) {
        return this.get(id);
    }

}