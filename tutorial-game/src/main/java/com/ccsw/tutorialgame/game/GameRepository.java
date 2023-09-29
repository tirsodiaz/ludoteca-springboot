package com.ccsw.tutorialgame.game;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.ccsw.tutorialgame.game.model.Game;

/**
 * @author ccsw
 *
 */
public interface GameRepository extends CrudRepository<Game, Long>, JpaSpecificationExecutor<Game> {

    @Override

    // @EntityGraph(attributePaths = { "category", "author" }) // unica consulta
    // join, no multiples querys (quitar porque game no incorpora objetos)
    List<Game> findAll(Specification<Game> spec);

}
