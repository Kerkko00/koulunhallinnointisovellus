package view;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import controller.CurrentUser;
import controller.IController;
import controller.StudentChangedListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Absence;
import model.PersonType;
import model.Student;

/**
 * Class for handling AbsenceView functions
 * 
 * @author Ville
 */
public class AbsencesViewController implements Initializable {
	@FXML
	private TableView<Absence> absenceTV;
	@FXML
	private TableColumn<Absence, String> dateColumn;
	@FXML
	private TableColumn<Absence, String> courseColumn;
	@FXML
	private TableColumn<Absence, String> teacherColumn;
	@FXML
	private TableColumn<Absence, String> studentColumn;
	@FXML
	private TableColumn<Absence, String> reasonColumn;
	@FXML
	private TableColumn<Absence, Button> addReasonColumn;
	@FXML
	private Button addAbsence;

	private IController controller;
	private Stage primaryStage;
	private ResourceBundle localization;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		localization = LocalizationHelper.getLocalization();
	}

	/**
	 * Method for setting primaryStage
	 * 
	 * @param primaryStage
	 */
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	/**
	 * Method for setting controller and loading data into tableView
	 * 
	 * @param controller
	 */
	public void setController(IController controller) {
		this.controller = controller;

		dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
		courseColumn.setCellValueFactory(
				cellvalue -> new SimpleStringProperty(cellvalue.getValue().getCourse().getCourseName()));
		teacherColumn.setCellValueFactory(cellvalue -> new SimpleStringProperty(
				cellvalue.getValue().getCourse().getGroup().getTeacher().getPerson().getFullName()));
		studentColumn.setCellValueFactory(
				cellvalue -> new SimpleStringProperty(cellvalue.getValue().getStudent().getPerson().getFullName()));
		reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));

		updateAbsences();
		
		// Update the absences based on the selected student
		CurrentUser.setStudentChangedListener(new StudentChangedListener() {
			@Override
			public void studentChanged(Student student) {
				updateAbsences();
			}
		});
	}

	/**
	 * Method for updating absenceTableView
	 */
	public void updateAbsences() {
		absenceTV.getItems().clear();
		if (CurrentUser.getUserType() == PersonType.PARENT) {
			teacherColumn.setVisible(true);
			studentColumn.setVisible(false);
			addReasonColumn.setVisible(true);
			addAbsence.setVisible(false);

			ObservableList<Absence> absences = FXCollections
					.observableArrayList(controller.getAbsencesByStudent(CurrentUser.getStudent().getStudentID()));
			absenceTV.setItems(absences);

			addReasonColumn.setCellFactory(TableViewHelper
					.<Absence>forTableColumn(localization.getString("absenceViewAddReason"), (Absence a) -> {
						openAddView(a);
						return a;
					}));
		} else if (CurrentUser.getUserType() == PersonType.TEACHER) {
			teacherColumn.setVisible(false);
			studentColumn.setVisible(true);
			addReasonColumn.setVisible(false);
			addAbsence.setVisible(true);

			if (CurrentUser.getUser().getTeacher().getTeacherGroup() != null) {
				Set<Student> students = CurrentUser.getUser().getTeacher().getTeacherGroup().getStudents();

				for (Student student : students) {
					ObservableList<Absence> absences = FXCollections
							.observableArrayList(controller.getAbsencesByStudent(student.getStudentID()));

					absenceTV.getItems().addAll(absences);
				}
			}
		}
	}

	/**
	 * Method for opening addAbsence view
	 * 
	 * @param selected Absence
	 */
	private void openAddView(Absence selected) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/view/AbsenceAddReasonView.fxml"));
			loader.setResources(localization);
			BorderPane absenceReasonAddView = (BorderPane) loader.load();

			Stage absenceReasonAddStage = new Stage();
			absenceReasonAddStage.setTitle(localization.getString("absenceViewAddReason"));
			absenceReasonAddStage.initModality(Modality.WINDOW_MODAL);
			absenceReasonAddStage.initOwner(primaryStage);
			Scene scene = new Scene(absenceReasonAddView);
			absenceReasonAddStage.setScene(scene);
			absenceReasonAddStage.setResizable(false);

			AbsenceAddReasonViewController addReasonController = loader.getController();
			addReasonController.setController(controller);
			addReasonController.setViewController(this);
			addReasonController.setReasonStage(absenceReasonAddStage);
			addReasonController.setSelectedAbsence(selected);

			absenceReasonAddStage.show();

		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("AbsenceAddReasonView loading failed." + e.getMessage());
		}
	}
	
	@FXML
	/**
	 * Method for opening AbsenceAddView from FXML
	 */
	public void handleNewButton() {
		openAbsenceAddView();
	}

	/**
	 * Method for opening AbsenceAddView
	 */
	private void openAbsenceAddView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/view/AbsenceAddNewView.fxml"));
			loader.setResources(localization);
			BorderPane absenceAddNewView = (BorderPane) loader.load();

			Stage absenceAddNewStage = new Stage();
			absenceAddNewStage.setTitle(localization.getString("absenceViewAddNew"));
			absenceAddNewStage.initModality(Modality.WINDOW_MODAL);
			absenceAddNewStage.initOwner(primaryStage);
			Scene scene = new Scene(absenceAddNewView);
			absenceAddNewStage.setScene(scene);
			absenceAddNewStage.setResizable(false);

			AbsenceAddViewController addNewAbsenceController = loader.getController();
			addNewAbsenceController.setController(controller);
			addNewAbsenceController.setAbsenceAddStage(absenceAddNewStage);
			addNewAbsenceController.setViewController(this);

			absenceAddNewStage.show();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("AbsenceAddView loading failed." + e.getMessage());
		}
	}
}
