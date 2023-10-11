package com.ccsw.tutorialauthor.author;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ccsw.tutorialauthor.author.model.Author;
import com.ccsw.tutorialauthor.author.model.AuthorDto;
import com.ccsw.tutorialauthor.pagination.WrapperPageableRequest;

/**
 * @author ccsw
 *
 */
public interface AuthorService {
    /**
     * Recupera un {@link Author} a través de su ID
     *
     * @param id PK de la entidad
     * @return {@link Author}
     */
    Author get(Long id);

    /**
     * Método para recuperar un listado paginado de {@link Author}
     *
     * @param dto dto de búsqueda
     * @return {@link Page} de {@link Author}
     */
    Page<Author> findPage(WrapperPageableRequest dto);

    /**
     * Método para crear o actualizar un {@link Author}
     *
     * @param id  PK de la entidad
     * @param dto datos de la entidad
     */
    void save(Long id, AuthorDto dto);

    /**
     * Método para crear o actualizar un {@link Author}
     *
     * @param id PK de la entidad
     */
    void delete(Long id) throws Exception;

    /**
     * Recupera un listado de autores {@link Author}
     *
     * @return {@link List} de {@link Author}
     */
    List<Author> findAll();
}
