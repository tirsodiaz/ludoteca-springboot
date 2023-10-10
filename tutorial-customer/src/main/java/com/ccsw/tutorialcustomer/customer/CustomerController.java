package com.ccsw.tutorialcustomer.customer;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;

import com.ccsw.tutorialcustomer.customer.model.Customer;
import com.ccsw.tutorialcustomer.customer.model.CustomerDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author ccsw
 * 
 */
@Tag(name = "Customer", description = "API of Customer")
@RequestMapping(value = "/customer")
@RestController
@CrossOrigin(origins = "*")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    DozerBeanMapper mapper;

    /**
     * Método para recuperar todos los customers
     *
     * @return {@link List} de {@link CustomerDto}
     */
    @Operation(summary = "Find", description = "Method that return a list of Customers")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<CustomerDto> findAll() {

        List<Customer> customers = this.customerService.findAll();
        return customers.stream().map(c -> mapper.map(c, CustomerDto.class)).collect(Collectors.toList());
    }

    /**
     * Método para recuperar un customer
     *
     * @return un {@link CustomerDto}
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Operation(summary = "Find", description = "Method that return a Customer")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public CustomerDto find(@PathVariable(name = "id", required = true) Long id)
            throws IllegalAccessException, InvocationTargetException {

        Customer customer = this.customerService.findById(id);
//      CustomerDto customerDto = new CustomerDto();
//      BeanUtils.copyProperties(customer, customerDto);
//      return customerDto;
        return mapper.map(customer, CustomerDto.class);
    }

    /**
     * Método para crear o actualizar un customer
     *
     * @param id  PK de la entidad
     * @param dto datos de la entidad
     * @return
     */
    @Operation(summary = "Save or Update", description = "Method that saves or updates a Customer")
    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.PUT)
    public ResponseEntity<Void> save(@PathVariable(name = "id", required = false) Long id,
            @RequestBody CustomerDto dto) {

        try {
            this.customerService.save(id, dto);
        } catch (HttpStatusCodeException exception) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("error", exception.getResponseBodyAsString());
            return new ResponseEntity<Void>(headers, exception.getStatusCode());
        } catch (Exception e) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("error", e.getMessage());
            return new ResponseEntity<Void>(headers, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    /**
     * Método para borrar un customer
     *
     * @param id PK de la entidad
     * @throws Exception
     */
    @Operation(summary = "Delete", description = "Method that deletes a Customer")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) throws Exception {

        this.customerService.delete(id);
    }
}