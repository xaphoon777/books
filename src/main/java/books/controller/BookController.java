package books.controller;

import books.model.Book;
import books.service.BookService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by Alex Volobuev on 06.01.2017.
 */
@Stateless
@Path("/books")
public class BookController {
    @Inject
    private Logger log;
    @Inject
    private BookService bookService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> listAll() {
        return bookService.getAll();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Book lookupById(@PathParam("id") long id) {
        Book book = bookService.getById(id);
        if (book == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return book;
    }
    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Book deleteById(@PathParam("id") long id) {
        Book book = bookService.deleteById(id);
        if (book == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return book;
    }



    /**
     * Creates a new book from the values provided. Performs validation, and will return a JAX-RS response with either 200 ok,
     * or with a map of fields, and related errors.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveOrUpdate(Book book) {
        Response.ResponseBuilder builder;
        try {
            bookService.saveOrUpdate(book);
            // Create an "ok" response
            builder = Response.ok().entity(book);
        } catch (ConstraintViolationException ce) {
            // Handle bean validation issues
            builder = createViolationResponse(ce.getConstraintViolations());
        } catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }
        return builder.build();
    }

    /**
     * Creates a JAX-RS "Bad Request" response including a map of all violation fields, and their message. This can then be used
     * by clients to show violations.
     *
     * @param violations A set of violations that needs to be reported
     * @return JAX-RS response containing all violations
     */
    private Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations) {
        log.fine("Validation completed. violations found: " + violations.size());
        Map<String, String> responseObj = new HashMap<>();
        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
    }
}
