package books.service;

import books.data.BookRepository;
import books.model.Book;

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
@ApplicationScoped
public class BookService {
    @Inject
    private Logger log;
    @Inject
    private Validator validator;
    @Inject
    private BookRepository bookRepository;

    public List<Book> getAll() {
        return bookRepository.findAllOrderedByTitle();
    }
    public Book getById(long id) {
        return bookRepository.findById(id);
    }
    public void saveOrUpdate (Book book) {
        validate(book);
        bookRepository.saveOrUpdate(book);
    }

    public Book deleteById (Long id) {
        return bookRepository.deleteById(id);
    }

    /**
     * <p>
     * Validates the given Book variable and throws validation exceptions based on the type of error. If the error is standard
     * bean validation errors then it will throw a ConstraintValidationException with the set of the constraints violated.
     * </p>
     * <p>
     * If the error is caused because an existing book with the same email is registered it throws a regular validation
     * exception so that it can be interpreted separately.
     * </p>
     *
     * @param book Book to be validated
     * @throws ConstraintViolationException If Bean Validation errors exist
     */
    private void validate(Book book) throws ConstraintViolationException {
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }
    }

}
