package view;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import controller.CurrentUser;
import controller.IController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.Course;
import model.Group;

/**
 * Class for handling CourseViewPane functions
 * 
 * @author Kerkko
 */
public class CourseViewPaneController implements Initializable {
	@FXML
	private javafx.scene.control.Button okButton;
	@FXML
	private javafx.scene.control.Button cancelButton;
	@FXML
	private TextField courseName;
	@FXML
	private ChoiceBox<String> courseGroup;
	@FXML
	private DatePicker courseStart;
	@FXML
	private DatePicker courseEnd;

	private ResourceBundle localization;
	private IController controller;
	private CourseViewController cvc;
	private Course course = new Course();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		localization = LocalizationHelper.getLocalization();
	}

	/**
	 * Method for setting local controller
	 * 
	 * @param controller
	 */
	public void setController(IController controller) {
		this.controller = controller;
	}

	/**
	 * Method for setting CourseViewController
	 */
	public void setCourseViewController(CourseViewController cvc) {
		this.cvc = cvc;
	}

	/**
	 * Method for getting group data
	 */
	public void updateGroups() {
		if (controller.getGroups() != null) {
			Group[] groupArray = controller.getGroups();
			ObservableList<String> groupOL = FXCollections.observableArrayList();
			for (int i = 0; i < groupArray.length; i++) {
				groupOL.add(groupArray[i].getName());
			}
			courseGroup.setItems(groupOL);
		}
	}

	/**
	 * Method for getting selected Course data from CourseViewController
	 */
	public void getCourseData(Course course) {
		this.course = course;
		courseName.setText(course.getCourseName());
		courseGroup.setValue(course.getGroup().getName());
		courseStart.setValue(course.getStartDate());
		courseEnd.setValue(course.getEndDate());
	}

	/**
	 * Method for handling okButton in modal
	 */
	@FXML
	public void handleOkButton() {
		course.setCourseName(courseName.getText());

		Group selectedGroup = controller.getGroup(courseGroup.getValue());
		course.setGroup(selectedGroup);

		course.setStartDate(courseStart.getValue());
		course.setEndDate(courseEnd.getValue());

		if (courseName.getText().isBlank()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle(localization.getString("errorwindowTitle"));
			alert.setHeaderText(localization.getString("courseviewpanealertHeader"));
			alert.setContentText(localization.getString("courseviewpanealertContent"));
			courseName.setStyle("-fx-border-color: red; -fx-border-width: 2px");
			alert.showAndWait();
		} else {
			controller.createCourse(course);
			cvc.updateCourses();
			cvc.closePane();
		}
	}

	/**
	 * Method for handling cancelButton in modal
	 */
	@FXML
	public void handleCancelButton() {
		cvc.closePane();
	}
}
