package view;

import java.net.URL;
import java.sql.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import controller.CurrentUser;
import controller.IController;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Course;
import model.Student;

/**
 * Class for handling AbsenceAddNewView functions
 * 
 * @author Ville
 */
public class AbsenceAddViewController implements Initializable {
	@FXML
	public ChoiceBox<Course> courses;
	@FXML
	public DatePicker dateOfAbsence;
	@FXML
	public ListView<Student> students;
	@FXML
	public HBox selectionBox;
	@FXML
	public TextArea reasonField;

	private IController controller;
	private AbsencesViewController viewController;
	private ResourceBundle localization;
	private Stage absenceAddStage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		localization = LocalizationHelper.getLocalization();
	}

	/**
	 * Method for setting absenceAddStage
	 * 
	 * @param absenceAddStage
	 */
	public void setAbsenceAddStage(Stage absenceAddStage) {
		this.absenceAddStage = absenceAddStage;
	}

	/**
	 * Method for setting local controller
	 * 
	 * @param controller
	 */
	public void setController(IController controller) {
		this.controller = controller;
		setupFields();
	}

	/**
	 * Method for setting local viewController
	 * 
	 * @param viewController
	 */
	public void setViewController(AbsencesViewController viewController) {
		this.viewController = viewController;
	}

	/**
	 * Method for loading data to FXML-lists
	 */
	private void setupFields() {
		courses.setItems(FXCollections.observableArrayList(controller.getCourses()));

		ChangeListener<Course> changeListener = ((observable, oldValue, newValue) -> {
			students.setItems(FXCollections.observableArrayList(newValue.getGroup().getStudents()));
			selectionBox.setDisable(false);
		});

		courses.getSelectionModel().selectedItemProperty().addListener(changeListener);
		students.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}

	@FXML
	/**
	 * Method for adding new absence
	 */
	public void addNewAbsence() {
		// If absence was given a name and student was selected, else display alert
		if (dateOfAbsence.getValue() != null && !students.getSelectionModel().getSelectedItems().isEmpty()) {
			for (Student student : students.getSelectionModel().getSelectedItems()) {
				controller.createAbsence(reasonField.getText(), Date.valueOf(dateOfAbsence.getValue()),
						courses.getValue().getCourseID(), student.getStudentID());
			}
			viewController.updateAbsences();
			absenceAddStage.close();
		} else {
			Alert info = new Alert(AlertType.WARNING);
			info.setHeaderText(localization.getString("absenceAddNewErrorHeader"));
			info.setContentText(localization.getString("absenceAddNewErrorContent"));
			info.show();
		}
	}

	@FXML
	/**
	 * Method for closing stage
	 */
	public void cancelButton() {
		absenceAddStage.close();
	}
}
