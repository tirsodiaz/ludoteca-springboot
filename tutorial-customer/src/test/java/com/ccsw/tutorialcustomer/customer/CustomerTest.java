package com.ccsw.tutorialcustomer.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ccsw.tutorialcustomer.customer.model.Customer;
import com.ccsw.tutorialcustomer.customer.model.CustomerDto;

@ExtendWith(MockitoExtension.class) // tests unitarios o de superficie para pruebas de Service
public class CustomerTest {
    public static final String CUSTOMER_NAME = "Customer1";
    public static final Long EXISTS_CUSTOMER_ID = 1L;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    public void findAllShouldReturnAllCustomers() {

        List<Customer> list = new ArrayList<>();
        list.add(mock(Customer.class));

        when(customerRepository.findAll()).thenReturn(list);

        List<Customer> customers = customerService.findAll();

        assertNotNull(customers);
        assertEquals(1, customers.size());
    }

    public static final Long NOT_EXISTS_CUSTOMER_ID = 0L;

    @Test
    public void getExistsCustomerIdShouldReturnCustomer() {

        Customer customer = mock(Customer.class);
        when(customer.getId()).thenReturn(EXISTS_CUSTOMER_ID);
        when(customerRepository.findById(EXISTS_CUSTOMER_ID)).thenReturn(Optional.of(customer));

        Customer customerResponse = customerService.get(EXISTS_CUSTOMER_ID);

        assertNotNull(customerResponse);
        assertEquals(EXISTS_CUSTOMER_ID, customer.getId());
    }

    @Test
    public void getNotExistsCustomerIdShouldReturnNull() {

        when(customerRepository.findById(NOT_EXISTS_CUSTOMER_ID)).thenReturn(Optional.empty());

        Customer customer = customerService.get(NOT_EXISTS_CUSTOMER_ID);

        assertNull(customer);
    }

    @Test
    public void saveNotExistsCustomerIdShouldInsert() throws Exception {

        CustomerDto customerDto = new CustomerDto();
        customerDto.setName(CUSTOMER_NAME);

        customerService.save(null, customerDto);

        ArgumentCaptor<Customer> customer = ArgumentCaptor.forClass(Customer.class);

        verify(customerRepository).save(customer.capture());

        assertEquals(CUSTOMER_NAME, customer.getValue().getName());
    }

    @Test
    public void saveExistsCustomerIdShouldUpdate() throws Exception {

        CustomerDto customerDto = new CustomerDto();
        customerDto.setName(CUSTOMER_NAME);

        Customer customer = mock(Customer.class);
        when(customerRepository.findById(EXISTS_CUSTOMER_ID)).thenReturn(Optional.of(customer));

        customerService.save(EXISTS_CUSTOMER_ID, customerDto);

        verify(customerRepository).save(customer);

    }

    @Test
    public void deleteExistsCustomerIdShouldDelete() throws Exception {

        Customer customer = mock(Customer.class);
        when(customerRepository.findById(EXISTS_CUSTOMER_ID)).thenReturn(Optional.of(customer));

        customerService.delete(EXISTS_CUSTOMER_ID);

        verify(customerRepository).deleteById(EXISTS_CUSTOMER_ID);
    }

}
