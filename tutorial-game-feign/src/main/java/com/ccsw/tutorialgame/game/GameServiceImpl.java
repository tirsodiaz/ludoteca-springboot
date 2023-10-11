package com.ccsw.tutorialgame.game;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ccsw.tutorialgame.common.criteria.SearchCriteria;
import com.ccsw.tutorialgame.feignclient.BookingClient;
import com.ccsw.tutorialgame.game.exception.MyConflictAdviceException;
import com.ccsw.tutorialgame.game.model.Game;
import com.ccsw.tutorialgame.game.model.GameDto;
import com.ccsw.tutorialgame.model.BookingDto;

import jakarta.transaction.Transactional;

/**
 * @author ccsw
 *
 */
@Service
@Transactional
public class GameServiceImpl implements GameService {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    BookingClient bookingClient;

    /*
     * @Autowired AuthorService authorService;
     * 
     * @Autowired CategoryService categoryService;
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Game> find(String title, Long idCategory, Long idAuthor) {

        GameSpecification titleSpec, categorySpec, authorSpec;
        titleSpec = new GameSpecification(new SearchCriteria("title", ":", title));
        categorySpec = new GameSpecification(new SearchCriteria("category.id", ":", idCategory));

        // si no objetos
        categorySpec = new GameSpecification(new SearchCriteria("idCategory", ":", idCategory));
        authorSpec = new GameSpecification(new SearchCriteria("idAuthor", ":", idAuthor));

        Specification<Game> spec = Specification.where(titleSpec).and(categorySpec).and(authorSpec);

        return this.gameRepository.findAll(spec);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Long id, GameDto dto) {

        Game game;

        if (id == null) {
            game = new Game();
        } else {
            game = this.gameRepository.findById(id).orElse(null);
        }

        BeanUtils.copyProperties(dto, game, "id", "idCategory", "idAuthor");

        game.setIdCategory(dto.getCategory().getId());
        game.setIdAuthor(dto.getAuthor().getId());

        this.gameRepository.save(game);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws Exception {

        if (this.get(id) == null) {
            throw new Exception("Not exists");
        }

        List<BookingDto> bookings = bookingClient.findBookingbyIdGamesOrIdCustomer(String.valueOf(id), null);
        if (bookings.isEmpty() || bookings.size() == 0)
            this.gameRepository.deleteById(id);
        else
            throw new MyConflictAdviceException("Game already used in booking!");
    }

    private Game get(Long id) {

        return this.gameRepository.findById(id).orElse(null);
    }

}