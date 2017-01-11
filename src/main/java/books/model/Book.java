package books.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by Alex Volobuev on 05.01.2017.
 */
@Entity
public class Book {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(min = 1, max = 45, message = "1-45 letters")
    @Pattern(regexp = "[\\w а-яА-Я']*", message = "Некорректные символы")
    private String title;

    @Size(min = 1, max = 25, message = "1-25 letters")
    @Pattern(regexp = "[\\w а-яА-Я']*", message = "Некорректные символы")
    private String author;

    @NotNull
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd.MM.yyyy")
    private Date releaseDate;
    private Boolean isPrivate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }
}
