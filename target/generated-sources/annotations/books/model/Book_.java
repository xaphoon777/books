package books.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Book.class)
public abstract class Book_ {

	public static volatile SingularAttribute<Book, Date> releaseDate;
	public static volatile SingularAttribute<Book, String> author;
	public static volatile SingularAttribute<Book, Long> id;
	public static volatile SingularAttribute<Book, Boolean> isPrivate;
	public static volatile SingularAttribute<Book, String> title;

}

