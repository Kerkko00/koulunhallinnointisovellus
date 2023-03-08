package model.dao;

import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import model.Conversation;
import model.HibernateUtil;

/**
 * DAO class for handling CRUD database operations for conversations.
 * 
 * @author Olli
 */
public class ConversationDAO implements IConversationDAO {

	private SessionFactory sessionFactory = null;
	private Transaction transaction = null;

	/**
	 * Initializes a new ConversationDAO instance.
	 */
	public ConversationDAO() {
		try {
			sessionFactory = HibernateUtil.getSessionFactory();
		} catch (Exception e) {
			System.err.println("SessionFactory creation failed: " + e.getMessage());
			System.exit(-1);
		}
	}

	@Override
	public boolean createConversation(Conversation conversation) {
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			session.saveOrUpdate(conversation);
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
	public Conversation getConversation(long id) {
		try (Session session = sessionFactory.openSession()) {
			Conversation conversation = (Conversation) session.get(Conversation.class, id);
			session.close();
			return conversation;
		} catch (ObjectNotFoundException exp) {
			return null;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	@Override
	public Conversation getConversation(String username, String reciver) {
		try (Session session = sessionFactory.openSession()) {
			String q = "FROM Conversation c WHERE c.username = :username AND c.reciver = :reciver";

			Conversation conversation = (Conversation) session.createQuery(q).setParameter("username", username)
					.setParameter("reciver", reciver).getSingleResult();

			session.close();

			return conversation;
		} catch (NoResultException exp) {
			System.out.println("!!!!");
			return null;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	@Override
	public Conversation[] getConversations() {
		try (Session session = sessionFactory.openSession()) {
			String q = "FROM Conversation";

			@SuppressWarnings("unchecked")
			List<Conversation> results = session.createQuery(q).getResultList();

			Conversation[] conversations = results.toArray(new Conversation[0]);

			session.close();

			return conversations;
		} catch (NoResultException exp) {
			return null;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
				System.out.println(e.getMessage());
			}
		}
		return null;
	}

	@Override
	public Conversation[] getConversationsByUsername(String username) {

		try (Session session = sessionFactory.openSession()) {
			String q = "FROM Conversation WHERE username = :username";

			@SuppressWarnings("unchecked")
			List<Conversation> results = session.createQuery(q).setParameter("username", username).getResultList();
			Conversation[] conversations = results.toArray(new Conversation[0]);
			session.close();
			return conversations;
		} catch (NoResultException exp) {
			return null;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	@Override
	public boolean updateConversation(Conversation conversation) {
		if (getConversation(conversation.getID()) != null) {
			try {
				createConversation(conversation);
				return true;
			} catch (Exception e) {
				System.err.println("db entry update failed: " + e.getMessage());
			}
		} else {
			System.err.println("db entry not found");
		}
		return false;
	}

	@Override
	public boolean deleteConversation(long id) {
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			session.delete(session.get(Conversation.class, id));
			transaction.commit();
			session.close();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		}
	}
}
