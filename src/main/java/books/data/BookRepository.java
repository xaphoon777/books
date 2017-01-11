package books.data;

import books.model.Book;
import books.model.Book_;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by Alex Volobuev on 06.01.2017.
 */
@Stateless
public class BookRepository {
    @Inject
    private EntityManager em;

    public Book findById(Long id) {
        return em.find(Book.class, id);
    }

    public Book saveOrUpdate (Book book) {
        return em.merge(book);
    }

    public List<Book> findAllOrderedByTitle() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> criteria = cb.createQuery(Book.class);
        Root<Book> book = criteria.from(Book.class);
         criteria.select(book).orderBy(cb.asc(book.get(Book_.title)));
//        criteria.select(member).orderBy(cb.asc(member.get("name")));
        return em.createQuery(criteria).getResultList();
    }

    public Book deleteById (Long id) {
        Book book = findById(id);
        if (book != null) {
            em.remove(book);
        }
        return book;
    }
}
