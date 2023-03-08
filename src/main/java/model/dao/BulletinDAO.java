package model.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import controller.CurrentUser;
import model.Bulletin;
import model.Group;
import model.HibernateUtil;
import model.Parent;
import model.Teacher;
import model.User;

/**
 * DAO class for handling CRUD database operations for bulletins.
 * 
 * @author Saku
 */
public class BulletinDAO implements IBulletinDAO {

	private SessionFactory sessionFactory = null;
	private Transaction transaction = null;

	/**
	 * Initializes a new BulletinDAO instance.
	 */
	public BulletinDAO() {
		try {
			sessionFactory = HibernateUtil.getSessionFactory();
		} catch (Exception e) {
			System.err.println("SessionFactory creation was unsuccessful: " + e.getMessage());
			System.exit(-1);
		}
	}

	@Override
	public boolean createBulletin(Bulletin bulletin) {
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			session.saveOrUpdate(bulletin);
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
	public Bulletin getBulletin(long id) {
		try (Session session = sessionFactory.openSession()) {
			Bulletin bulletin = new Bulletin();
			session.load(bulletin, id);
			session.close();
			return bulletin;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	@Override
	public List<Bulletin> getBulletins(User user) {
		if (user.getParent() != null) {
			return getBulletinsForParent(user.getParent());
		} else if (user.getTeacher() != null) {
			return getBulletinsForTeacher(user.getTeacher());
		} else {
			return new ArrayList<Bulletin>();
		}
	}

	/**
	 * Returns a list of bulletins related to the given parent. The parent can view
	 * the bulletins received by their selected student's group.
	 * 
	 * @param parent The parent who's bulletins to retrieve
	 * @return A list of bulletins related to the specified parent
	 */
	private List<Bulletin> getBulletinsForParent(Parent parent) {
		try (Session session = sessionFactory.openSession()) {
			String hql = "SELECT b FROM Bulletin b JOIN FETCH b.bulletinGroups g WHERE g.groupID = :groupID";
			Group group = CurrentUser.getStudent().getGroup();

			List<Bulletin> bulletins = session.createQuery(hql, Bulletin.class).setParameter("groupID", group.getID())
					.getResultList();
			session.close();

			return bulletins;
		} catch (Exception e) {
			System.err.println("Database search was not seccessful: " + e.getMessage());
			return new ArrayList<Bulletin>();
		}
	}

	/**
	 * Returns all bulletins sent by the specified teacher.
	 * 
	 * @param teacher The teacher who's bulletins to retrieve
	 * @return A list of bulletins related to the specified teacher
	 */
	private List<Bulletin> getBulletinsForTeacher(Teacher teacher) {
		try (Session session = sessionFactory.openSession()) {
			String hql = "FROM Bulletin WHERE teacherID = :teacherID";

			List<Bulletin> bulletins = session.createQuery(hql, Bulletin.class)
					.setParameter("teacherID", teacher.getTeacherID()).getResultList();
			session.close();

			return bulletins;
		} catch (Exception e) {
			System.err.println("Database search was not seccessful: " + e.getMessage());
			return new ArrayList<Bulletin>();
		}
	}

	@Override
	public boolean deleteBulletin(long id) {
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			session.delete(session.load(Bulletin.class, id));
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
