package view;

import java.net.URL;
import java.sql.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import controller.CurrentUser;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import model.Grade;
import model.Student;
import model.dao.GradeDAO;
import model.dao.PersonDAO;

/**
 * Class for handling GradesTeacherView functions
 * 
 * @author Olli
 */
public class GradesTeacherViewController implements Initializable {
	@FXML
	private ChoiceBox<Student> cb;
	@FXML
	private TableView<Grade> table;
	@FXML
	private Text errorText;
	@FXML
	private TextField nameTextField;
	@FXML
	private TextField gradeTextField;

	private PersonDAO personDAO = new PersonDAO();
	private GradeDAO gradeDAO = new GradeDAO();
	private ObservableList<Grade> data = FXCollections.observableArrayList();
	private ResourceBundle localization;
	private int[] grades = { 4, 5, 6, 7, 8, 9, 10 };

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		localization = LocalizationHelper.getLocalization();

		String nameColumn = localization.getString("gradesParentViewName");
		String dateColumn = localization.getString("gradesParentViewDate");
		String gradeColumn = localization.getString("gradesParentViewGrade");

		TableColumn<Grade, String> name = new TableColumn<>(nameColumn);
		TableColumn<Grade, String> date = new TableColumn<>(dateColumn);
		TableColumn<Grade, String> grade = new TableColumn<>(gradeColumn);

		name.setCellValueFactory(new PropertyValueFactory<>("name"));
		date.setCellValueFactory(new PropertyValueFactory<>("date"));
		grade.setCellValueFactory(new PropertyValueFactory<>("grade"));

		name.setMinWidth(300);
		date.setMinWidth(150);
		grade.setMinWidth(125);

		table.getColumns().addAll(name, date, grade);

		Student[] students = personDAO.getStudents();
		if (students.length > 0) {

			cb.getItems().addAll(students);
			cb.setValue(students[0]);

			data.addAll(students[0].getGrades());

			table.setItems(data);
		}

		cb.valueProperty().addListener(new ChangeListener<Student>() {
			@Override
			public void changed(ObservableValue<? extends Student> observable, Student oldValue, Student newValue) {
				clickChoiceBoxItem(newValue);
			}
		});

	}

	@FXML
	/**
	 * Method for adding a new grade
	 */
	private void addNewGrade() {
		errorText.setVisible(false);

		String gradeName = nameTextField.getText();
		String gradeNumber = gradeTextField.getText();

		if (gradeName.isBlank() || gradeNumber.isBlank()) {
			errorText.setVisible(true);
			errorText.setText(localization.getString("gradesTeacherViewEmptyFields"));
			return;
		}

		boolean isNumber = false;
		if (isInteger(gradeNumber)) {
			for (int i : grades) {
				if (Integer.parseInt(gradeNumber) == i) {
					isNumber = true;
					break;
				}
			}
		}

		if (!isNumber) {
			errorText.setVisible(true);
			errorText.setText(localization.getString("gradesTeacherViewInvalidGrade"));
			gradeTextField.setText("");
			return;
		}

		long millis = System.currentTimeMillis();
		Date date = new Date(millis);

		Student student = cb.getSelectionModel().getSelectedItem();
		Grade grade = new Grade(gradeName, Integer.parseInt(gradeNumber), date, student);
		boolean isSuccess = gradeDAO.createGrade(grade);

		errorText.setVisible(!isSuccess);

		if (!isSuccess) {
			errorText.setText(localization.getString("gradesTeacherViewCreatingError"));
		} else {
			data.add(grade);
			table.setItems(data);
		}

		this.gradeTextField.setText("");
		nameTextField.setText("");
	}

	@FXML
	/**
	 * Method for editing a grade
	 */
	private void editGrade() {
		errorText.setVisible(false);

		Grade grade = table.getSelectionModel().getSelectedItem();
		if (grade == null) {
			errorText.setVisible(true);
			errorText.setText(localization.getString("gradesTeacherViewChooseGrade"));
			return;
		}
		long millis = System.currentTimeMillis();
		Date date = new Date(millis);

		grade.setDate(date);

		if (!nameTextField.getText().isBlank())
			grade.setName(nameTextField.getText());

		if (!this.gradeTextField.getText().isBlank()) {

			if (!isInteger(this.gradeTextField.getText())) {
				errorText.setVisible(true);
				errorText.setText(localization.getString("gradesTeacherViewInvalidGrade"));
				return;
			}
			grade.setGrade(Integer.parseInt(this.gradeTextField.getText()));
		}

		boolean isSuccess = gradeDAO.updateGrade(grade);
		this.gradeTextField.setText("");
		nameTextField.setText("");

		errorText.setVisible(!isSuccess);

		if (!isSuccess)
			errorText.setText(localization.getString("gradesTeacherViewUpdatingError"));
		else {
			Student student = cb.getSelectionModel().getSelectedItem();
			Set<Grade> grades = personDAO.getStudentById(student.getStudentID()).getGrades();
			data.clear();
			data.addAll(grades);
			table.setItems(data);
		}
	}

	@FXML
	/**
	 * Method for deleting a grade
	 */
	private void deleteGrade() {
		errorText.setVisible(false);

		Grade grade = table.getSelectionModel().getSelectedItem();
		if (grade == null) {

			errorText.setVisible(true);
			errorText.setText(localization.getString("gradesTeacherViewChooseGrade"));
			return;
		}

		boolean isSuccess = gradeDAO.deleteGrade(grade);
		errorText.setVisible(!isSuccess);

		if (!isSuccess)
			errorText.setText(localization.getString("gradesTeacherViewDeletingError"));
		else {
			data.remove(grade);
			table.setItems(data);
		}
		this.gradeTextField.setText("");
		nameTextField.setText("");
	}

	@FXML
	/**
	 * Method for setting grade of a clicked item
	 * 
	 * @param event
	 */
	private void clickItem(MouseEvent event) {
		errorText.setVisible(false);

		Grade grade = table.getSelectionModel().getSelectedItem();
		if (grade != null) {
			this.gradeTextField.setText(String.valueOf(grade.getGrade()));
			nameTextField.setText(grade.getName());
		}
	}

	/**
	 * Method for handling choicebox selection
	 * 
	 * @param activeStudent selected student
	 */
	private void clickChoiceBoxItem(Student activeStudent) {
		data.clear();
		Student student = personDAO.getStudentById(activeStudent.getStudentID());
		data.addAll(student.getGrades());
		table.setItems(data);
	}

	/**
	 * Method for checking if a value is Integer
	 * 
	 * @param text String for Integer parsing
	 * @return true if parsing succeeded, false if failed
	 */
	private boolean isInteger(String text) {
		try {
			Integer.parseInt(text);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
