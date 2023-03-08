package model.dao;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import model.Grade;
import model.Student;
import model.dao.GradeDAO;
import model.dao.PersonDAO;

public class GradeDAOTest {

	static GradeDAO gradeDAO;
	static PersonDAO personDAO;
	static Student student;
	static Student student1;
	static Grade grade;
	
	@BeforeAll
	static void setUp() {
		gradeDAO = new GradeDAO();
		personDAO = new PersonDAO();
		student = new Student();
		student1 = new Student();
		personDAO.createStudent(student);
		personDAO.createStudent(student1);
		grade = new Grade("Liikunta", 9 ,new Date(System.currentTimeMillis()), student1);
		gradeDAO.createGrade(grade);
	
	}
	
	@AfterAll
	static void cleanUp() {
		Grade[] grades = gradeDAO.getGrades(student);
		if(grades == null)
			return;
		for(Grade g : grades) {
			gradeDAO.deleteGrade(g);
		}
		
		personDAO.deleteStudent(student.getStudentID());
		personDAO.deleteStudent(student1.getStudentID());
	}
	
	@Test
	@DisplayName("Test createGrade method")
	void testCreateGrade() {
		Grade grade = new Grade("Matikka", 6,new Date(System.currentTimeMillis()), student);
		assertTrue(gradeDAO.createGrade(grade));
	}
	
	@Test
	@DisplayName("Test getGradesByStudent method")
	void testGetGrades() {
		//Student student = new Student();
		Grade grade = new Grade("Liikunta", 9 ,new Date(System.currentTimeMillis()), student);
		gradeDAO.createGrade(grade);
		assertTrue(gradeDAO.getGrades(student).length >0);
	}
	
	@Test
	@DisplayName("Test updateGrade method")
	void testUpdateGrade() {
		Grade grade = new Grade("Liikunta", 9 ,new Date(System.currentTimeMillis()), student);
		gradeDAO.createGrade(grade);
		grade.setName("Fysiikka");
		grade.setGrade(5);
		
		assertTrue(gradeDAO.updateGrade(grade));
	}
	/*
	@Test
	@DisplayName("Test deleteGrade method")
	void testDeleteGrade() {
		assertTrue(dao.deleteGrade(grade));
	}
	*/
}
