package com.ccsw.tutorialauthor.author;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ccsw.tutorialauthor.author.exception.MyBadAdviceException;
import com.ccsw.tutorialauthor.author.exception.MyConflictAdviceException;
import com.ccsw.tutorialauthor.author.model.Author;
import com.ccsw.tutorialauthor.author.model.AuthorDto;
import com.ccsw.tutorialauthor.feignclient.GameClient;
import com.ccsw.tutorialauthor.model.GameDto;
import com.ccsw.tutorialauthor.pagination.WrapperPageableRequest;

import jakarta.transaction.Transactional;

/**
 * @author ccsw
 *
 */
@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    GameClient gameClient;

    /**
     * {@inheritDoc}
     */
    @Override
    public Author get(Long id) {

        return this.authorRepository.findById(id).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Author> findPage(WrapperPageableRequest dto) {

        return this.authorRepository.findAll(dto.getPageableRequest().buidPageable());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Author> findAll() {

        return (List<Author>) this.authorRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Long id, AuthorDto data) {

        Author author;

        if (id == null) {
            author = new Author();
        } else {
            author = this.get(id);
        }
        if (author == null)
            throw new MyBadAdviceException("Author id doesn't exit");

        BeanUtils.copyProperties(data, author, "id");

        this.authorRepository.save(author);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws Exception {

        if (this.get(id) == null) {
            throw new Exception("Not exists");
        }

        List<GameDto> games = gameClient.find(null, null, id);
        if (games.size() == 0 || games.isEmpty())
            this.authorRepository.deleteById(id);
        else
            throw new MyConflictAdviceException("Author already used in game!");
    }

}