package model;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Class for handling SessionFactory singleton
 * 
 * @author Kerkko
 */
public class HibernateUtil {
	private static SessionFactory sessionFactory;

	private HibernateUtil() {
	}

	/**
	 * Method for getting instance of SessionFactory, new instance is created if
	 * none exist on method call
	 * 
	 * @return sessionFactory singleton
	 */
	public static synchronized SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
		}
		return sessionFactory;
	}
}
