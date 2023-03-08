package model.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import model.Group;
import model.HibernateUtil;

/**
 * DAO class for handling CRUD database operations for groups.
 * 
 * @author Kerkko, Ville
 */
public class GroupDAO implements IGroupDAO {

	private SessionFactory sessionFactory = null;
	private Transaction transaction = null;

	/**
	 * Initializes a new GroupDAO instance.
	 */
	public GroupDAO() {
		try {
			sessionFactory = HibernateUtil.getSessionFactory();
		} catch (Exception e) {
			System.err.println("SessionFactory creation was unsuccessful: " + e.getMessage());
			System.exit(-1);
		}
	}

	@Override
	public boolean createGroup(Group group) {
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			session.saveOrUpdate(group);
			transaction.commit();

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
	public Group getGroup(long id) {
		try (Session session = sessionFactory.openSession()) {
			Group group = new Group();
			session.load(group, id);

			return group;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	@Override
	public Group getGroup(String name) {
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();

			@SuppressWarnings("unchecked")
			List<Group> groupList = session.createQuery("FROM Group WHERE name = :name").setParameter("name", name)
					.list();
			Group[] groupArray = new Group[groupList.size()];
			groupList.toArray(groupArray);

			if (groupArray.length == 0) {
				return null;
			}

			Group group = groupArray[0];
			session.getTransaction().commit();

			return group;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	@Override
	public Group[] getGroups() {
		Group[] groupArray = new Group[0];

		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			@SuppressWarnings("unchecked")
			List<Group> groupList = session.createQuery("FROM Group").list();
			groupArray = new Group[groupList.size()];
			groupList.toArray(groupArray);
			transaction.commit();
		} catch (Exception e) {
			System.err.println("Database search was unsuccessful: " + e.getMessage());
		}

		return groupArray;
	}

	@Override
	public boolean deleteGroup(long id) {
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			session.delete(session.load(Group.class, id));
			transaction.commit();

			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		}
	}
}
