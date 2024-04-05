package org.elnar.crudapp.util;

import org.elnar.crudapp.model.Label;
import org.elnar.crudapp.model.Post;
import org.elnar.crudapp.model.Writer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static final SessionFactory sessionFactory;
	
	static {
		try {
			Configuration configuration = new Configuration()
					.addAnnotatedClass(Writer.class)
					.addAnnotatedClass(Post.class)
					.addAnnotatedClass(Label.class);
			
			sessionFactory = configuration.buildSessionFactory();
		}catch (Exception e){
			System.err.println("Не удалось создать экземпляр SessionFactory." + e);
			throw new ExceptionInInitializerError(e);
		}
	}
	
	public static Session openSession() {
		return sessionFactory.openSession();
	}
}
