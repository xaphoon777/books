package books.service;

import books.data.BookRepository;
import books.model.Book;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
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
}
