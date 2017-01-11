package books.service;

import books.data.BookRepository;
import books.model.Book;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by Alex Volobuev on 07.01.2017.
 */
@Stateless
public class BookService {
    @Inject
    private Logger log;

    @Inject
    private BookRepository bookRepository;

    public List<Book> getAll() {
        return bookRepository.findAllOrderedByTitle();
    }
    public Book getById(long id) {
        return bookRepository.findById(id);
    }
    public Book saveOrUpdate (Book book) {
        return bookRepository.saveOrUpdate(book);
    }

    public Book deleteById (Long id) {
        return bookRepository.deleteById(id);
    }
    public Book switchCatalogue (long id) {
        Book book = bookRepository.findById(id);
        if (book == null) {
            return null;
        } else {
            boolean isPrivate = !(book.getPrivate() == null || !book.getPrivate());
            book.setPrivate(!isPrivate);
            bookRepository.saveOrUpdate(book);
            return book;
        }
    }



}
