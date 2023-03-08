package view;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import controller.CurrentUser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.PersonType;
import model.Student;

/**
 * Class for handling SideView functions
 * 
 * @author Kerkko
 */
public class SideViewController implements Initializable {
	@FXML
	public Button coursesBtn;
	@FXML
	public Button groupBtn;
	@FXML
	public Button addUserBtn;
	@FXML
	public Label viewTitle;
	@FXML
	public VBox titleColumn;
	@FXML
	public HBox studentSelectionRow;
	@FXML
	public ChoiceBox<Student> studentChoiceBox;

	private GUI gui;
	private ResourceBundle localization;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		localization = LocalizationHelper.getLocalization();
		
		switch (CurrentUser.getUserType()) {
		case PARENT:
			showParentTabs();
			initStudentDropdown();
			break;
		case TEACHER:
		default:
			showTeacherTabs();
			break;
		}
	}
	
	@FXML
	/**
	 * Method for setting language to en_UK
	 */
	public void setLanguageUK() {
		Locale localeEN = new Locale("en_UK");
		CurrentUser.getUser().setLanguage(localeEN);
		gui.updateUser();
		gui.updateLocale(localeEN);
	}
	
	@FXML
	/**
	 * Method for setting language to fi_FI
	 */
	public void setLanguageFI() {
		Locale localeFI = new Locale("fi_FI");
		CurrentUser.getUser().setLanguage(localeFI);
		gui.updateUser();
		gui.updateLocale(localeFI);
	}

	/**
	 * Method for setting local GUI
	 * 
	 * @param gui
	 */
	public void setGUI(GUI gui) {
		this.gui = gui;
	}

	@FXML
	/**
	 * Method for opening messages view
	 */
	public void handleMessagesButton() {
		gui.openView(Views.Conversation);
	}

	@FXML
	/**
	 * Method for opening bulletins view
	 */
	public void handleBulletinsButton() {
		gui.openView(Views.Bulletins);
	}

	@FXML
	/**
	 * Method for opening accomplishments view
	 */
	public void handleAccomplishmentsButton() {
		if (CurrentUser.getUserType() == PersonType.PARENT)
			gui.openView(Views.GradesParent);
		else if (CurrentUser.getUserType() == PersonType.TEACHER)
			gui.openView(Views.GradesTeacher);
	}

	@FXML
	/**
	 * Method for opening courses view
	 */
	public void handleCoursesButton() {
		gui.openView(Views.Course);
	}

	@FXML
	/**
	 * Method for opening registration view
	 */
	public void handleRegisterButton() {
		gui.openView(Views.Registration);
	}

	@FXML
	/**
	 * Method for opening settings view
	 */
	public void handleSettingsButton() {
		gui.openView(Views.Settings);
	}

	@FXML
	/**
	 * Method for opening login view
	 */
	public void handleLogoutButton() {
		gui.openView(Views.Login);
	}

	@FXML
	/**
	 * Method for opening group view
	 */
	public void handleGroupButton() {
		gui.openView(Views.Group);
	}

	@FXML
	/**
	 * Method for opening absence view
	 */
	public void handleAbsenceButton() {
		gui.openView(Views.Absences);
	}

	/**
	 * Method for showing parent related buttons
	 */
	private void showParentTabs() {
		viewTitle.setText(localization.getString("sideviewParentTitleText"));
		coursesBtn.setDisable(true);
		coursesBtn.setOpacity(0);
		addUserBtn.setDisable(true);
		addUserBtn.setOpacity(0);
		groupBtn.setDisable(true);
		groupBtn.setOpacity(0);
	}

	/**
	 * Method for showing teacher related buttons
	 */
	private void showTeacherTabs() {
		viewTitle.setText(localization.getString("sideviewTeacherTitleText"));
		coursesBtn.setDisable(false);
		coursesBtn.setOpacity(1);
		addUserBtn.setDisable(false);
		addUserBtn.setOpacity(1);
		groupBtn.setDisable(false);
		groupBtn.setOpacity(1);

		studentSelectionRow.setManaged(false);
	}

	/**
	 * Method for showing students in dropdown for parents
	 */
	private void initStudentDropdown() {
		Set<Student> students = CurrentUser.getUser().getParent().getDependants();

		for (Student student : students) {
			studentChoiceBox.getItems().add(student);
		}

		studentChoiceBox.setOnAction(event -> {
			CurrentUser.setStudent(studentChoiceBox.getValue());
		});

		if (students.size() > 0) {
			studentChoiceBox.setValue(students.iterator().next());
		}
	}
}
