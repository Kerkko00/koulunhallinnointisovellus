package view;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import controller.CurrentUser;
import controller.StudentChangedListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import model.Grade;
import model.Student;
import model.dao.PersonDAO;

/**
 * Class for handling GradesTeacherView functions
 * 
 * @author Olli
 */
public class GradesParentViewController implements Initializable {
	@FXML
	private Text studentsName;
	@FXML
	private Text averageGrade;
	@FXML
	private TableView<Grade> gradeTV;

	private ObservableList<Grade> data = FXCollections.observableArrayList();
	private PersonDAO personDAO = new PersonDAO();
	private ResourceBundle localization;

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

		gradeTV.getColumns().addAll(name, date, grade);

		name.setMinWidth(300);
		date.setMinWidth(150);
		grade.setMinWidth(125);

		setStudent(CurrentUser.getStudent());

		CurrentUser.setStudentChangedListener(new StudentChangedListener() {
			@Override
			public void studentChanged(Student student) {
				setStudent(student);
			}
		});

	}

	/**
	 * Method for setting student for grade management
	 * 
	 * @param student
	 */
	private void setStudent(Student student) {
		data.clear();

		String studentsNameText = localization.getString("gradesParentViewStudent") + " "
				+ student.getPerson().getFullName();
		studentsName.setText(studentsNameText);

		Set<Grade> grades = personDAO.getStudentById(student.getStudentID()).getGrades();
		if (grades != null && grades.size() > 0) {
			int gradesScore = 0;
			int gradesCount = grades.size();
			for (Grade gradeObject : grades) {
				gradesScore += gradeObject.getGrade();
			}
			data.addAll(grades);
			gradeTV.setItems(data);
			double gpa = (double) gradesScore / gradesCount;

			averageGrade.setText(localization.getString("gradesParentViewAverage") + " " + formattedDouble(gpa));
		} else {
			averageGrade.setText(localization.getString("gradesParentViewNoGrades"));
		}
	}

	/**
	 * Method for formatting Double
	 * 
	 * @param value to be formatted
	 * @return formatted double
	 */
	private String formattedDouble(Double value) {
		DecimalFormat df = new DecimalFormat("#.##");
		return df.format(value);
	}
}