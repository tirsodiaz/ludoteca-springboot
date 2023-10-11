package com.ccsw.tutorialgame.game;

import java.util.List;

import com.ccsw.tutorialgame.game.model.Game;
import com.ccsw.tutorialgame.game.model.GameDto;

/**
 * @author ccsw
 *
 */
public interface GameService {

    /**
     * Recupera los juegos filtrando opcionalmente por título y/o categoría
     *
     * @param title      título del juego
     * @param idCategory PK de la categoría
     * @param idCategory PK del autor
     * @return {@link List} de {@link Game}
     */
    List<Game> find(String title, Long idCategory, Long idAuthor);

    /**
     * Guarda o modifica un juego, dependiendo de si el identificador está o no
     * informado
     *
     * @param id  PK de la entidad
     * @param dto datos de la entidad
     */
    void save(Long id, GameDto dto);

    /**
     * {@inheritDoc}
     */
    void delete(Long id) throws Exception;

}
