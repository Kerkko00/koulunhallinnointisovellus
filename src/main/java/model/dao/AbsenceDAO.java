package model.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import model.Absence;
import model.HibernateUtil;

/**
 * DAO class for handling CRUD database operations for absences.
 * 
 * @author Ville
 */
public class AbsenceDAO implements IAbsenceDAO {

	private SessionFactory sessionFactory = null;
	private Transaction transaction = null;

	/**
	 * Initializes a new AbsenceDAO instance.
	 */
	public AbsenceDAO() {
		try {
			sessionFactory = HibernateUtil.getSessionFactory();
		} catch (Exception e) {
			System.err.println("SessionFactory creation was unsuccessful: " + e.getMessage());
			System.exit(-1);
		}
	}

	@Override
	public boolean createAbsence(Absence absence) {
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			session.saveOrUpdate(absence);
			transaction.commit();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public List<Absence> getAbsences() {
		try (Session session = sessionFactory.openSession()) {
			String q = "FROM Absence";
			@SuppressWarnings("unchecked")
			List<Absence> absenceList = session.createQuery(q).getResultList();
			return absenceList;
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			System.err.println("Database search was unsuccessful: " + e.getMessage());
		}
		return null;
	}

	@Override
	public List<Absence> getAbsencesByStudent(long studentID) {
		try (Session session = sessionFactory.openSession()) {

			String q = "FROM Absence WHERE student_studentID = :studentID";
			@SuppressWarnings("unchecked")
			List<Absence> absenceList = session.createQuery(q).setParameter("studentID", studentID).getResultList();
			return absenceList;
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			System.err.println("Database search was unsuccessful: " + e.getMessage());
		}
		
		return null;
	}

	@Override
	public List<Absence> getAbsencesByCourse(long courseID) {
		try (Session session = sessionFactory.openSession()) {

			String q = "FROM Absence WHERE course_courseID  = :courseID";
			@SuppressWarnings("unchecked")
			List<Absence> absenceList = session.createQuery(q).setParameter("courseID", courseID).getResultList();
			return absenceList;
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			System.err.println("Database search was unsuccessful: " + e.getMessage());
		}
		
		return null;
	}

	@Override
	public boolean deleteAbsence(long id) {
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			session.delete(session.load(Absence.class, id));
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
