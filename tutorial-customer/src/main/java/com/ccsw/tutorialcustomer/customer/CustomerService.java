package com.ccsw.tutorialcustomer.customer;

import java.util.List;
import java.util.Locale.Category;

import com.ccsw.tutorialcustomer.customer.model.Customer;
import com.ccsw.tutorialcustomer.customer.model.CustomerDto;

/**
 * @author ccsw
 * 
 */
public interface CustomerService {
    /**
     * Recupera una {@link Category} a partir de su ID
     *
     * @param id PK de la entidad
     * @return {@link Customer}
     */
    Customer get(Long id);

    /**
     * Método para recuperar todas las {@link Customer}
     *
     * @return {@link List} de {@link Customer}
     */
    List<Customer> findAll();

    /**
     * Método para crear o actualizar un {@link Customer}
     *
     * @param id  PK de la entidad
     * @param dto datos de la entidad
     * @throws Exception
     */
    void save(Long id, CustomerDto dto) throws Exception;

    /**
     * Método para borrar un {@link Customer}
     *
     * @param id PK de la entidad
     */
    void delete(Long id) throws Exception;

    Customer findById(Long id);

}