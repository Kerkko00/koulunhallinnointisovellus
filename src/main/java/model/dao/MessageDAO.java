package model.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import model.HibernateUtil;
import model.Message;

/**
 * DAO class for handling CRUD database operations for messages.
 * 
 * @author Olli
 */
public class MessageDAO implements IMessageDAO {
	private SessionFactory sessionFactory = null;
	private Transaction transaction = null;

	/**
	 * Initializes a new MessageDAO instance.
	 */
	public MessageDAO() {
		try {
			sessionFactory = HibernateUtil.getSessionFactory();
		} catch (Exception e) {
			System.err.println("SessionFactory creation failed: " + e.getMessage());
			System.exit(-1);
		}
	}

	@Override
	public boolean createMessage(Message message) {
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			session.saveOrUpdate(message);
			transaction.commit();
			session.close();

			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
				throw e;
			}
		}

		return false;
	}

	@Override
	public Message[] getMessages() {
		try (Session session = sessionFactory.openSession()) {
			@SuppressWarnings("unchecked")
			List<Message> messages = session.createQuery("FROM Message").getResultList();
			Message[] messageArray = (Message[]) messages.toArray();
			session.close();

			return messageArray;
		} catch (Exception e) {
			System.err.println("Database search failed: " + e.getMessage());
		}

		return null;
	}
}
