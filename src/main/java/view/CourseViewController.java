package view;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import controller.CurrentUser;
import controller.IController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import model.Course;

/**
 * Class for handling CourseView functions
 * 
 * @author Kerkko
 */
public class CourseViewController implements Initializable {
	@FXML
	private TableView<Course> coursesTV;
	@FXML
	private TableColumn<Course, String> nameColumn;
	@FXML
	private TableColumn<Course, String> groupColumn;
	@FXML
	private TableColumn<Course, String> startDateColumn;
	@FXML
	private TableColumn<Course, String> endDateColumn;
	@FXML
	private Pane courseDataPane = new Pane();

	private ResourceBundle localization;
	private IController controller;

	/**
	 * Method for updating courseTV
	 */
	public void updateCourses() {
		// DateTimeFormatter for showing dates according to localization
		DateTimeFormatter dtf;
		if (CurrentUser.getUser().getLanguage() != null) {
			dtf = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(CurrentUser.getUser().getLanguage());
		} else {
			dtf = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(Locale.getDefault());
		}

		nameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
		groupColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGroup().getName()));
		startDateColumn.setCellValueFactory(
				cellData -> new SimpleStringProperty(cellData.getValue().getStartDate().format(dtf)));
		endDateColumn.setCellValueFactory(
				cellData -> new SimpleStringProperty(cellData.getValue().getEndDate().format(dtf)));

		Course[] courseArray = controller.getCourses();
		if (courseArray != null) {
			ObservableList<Course> coursesOL = FXCollections.observableArrayList(courseArray);
			coursesTV.setItems(coursesOL);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		localization = LocalizationHelper.getLocalization();
	}

	/**
	 * Method for defining controller
	 * 
	 * @param controller
	 */
	public void setController(IController controller) {
		this.controller = controller;
	}

	/**
	 * Method for handling addButton
	 */
	@FXML
	public void handleAddButton() {
		courseDataPane.getChildren().clear();
		loadPane(null);
	}

	/**
	 * Method for handling editButton
	 */
	@FXML
	public void handleEditButton() {
		courseDataPane.getChildren().clear();
		Course selectedCourse = coursesTV.getSelectionModel().getSelectedItem();
		if (selectedCourse != null) {
			loadPane(selectedCourse);
		}
	}

	/**
	 * Method for handling deleteButton
	 */
	@FXML
	public void handleDeleteButton() {
		Course selectedCourse = coursesTV.getSelectionModel().getSelectedItem();
		Alert deleteAlert = new Alert(AlertType.CONFIRMATION);
		deleteAlert.setTitle(localization.getString("confirmationwindowTitle"));
		deleteAlert.setHeaderText(localization.getString("courseviewalertHeader"));
		deleteAlert.setContentText(localization.getString("courseviewalertContent"));
		Optional<ButtonType> alertAction = deleteAlert.showAndWait();

		if (selectedCourse != null && alertAction.get() == ButtonType.OK) {
			coursesTV.getItems().remove(selectedCourse);
			controller.deleteCourse(selectedCourse.getCourseID());
		}
	}

	/**
	 * Method for loading CourseViewPane
	 * 
	 * @param selectedCourse for handling edit
	 */
	private void loadPane(Course selectedCourse) {
		Pane tPane = null;
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(CourseViewController.class.getResource("/view/CourseViewPane.fxml"));
		loader.setResources(localization);

		try {
			tPane = loader.load();
			CourseViewPaneController cvpc = loader.getController();
			cvpc.setController(controller);
			cvpc.setCourseViewController(this);
			cvpc.updateGroups();
			if (selectedCourse != null) {
				cvpc.getCourseData(selectedCourse);
			}
		} catch (IOException e) {
			System.err.println("CourseViewPane loading failed.");
		}

		courseDataPane.getChildren().add(tPane);
	}

	/**
	 * Method for closing CourseViewControllerPane
	 */
	public void closePane() {
		courseDataPane.getChildren().clear();
	}
}
