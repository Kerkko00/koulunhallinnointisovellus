package view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import controller.CurrentUser;
import controller.IController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import model.PersonType;

/**
 * Class for handling RegistrationView functions
 * 
 * @author Ville
 */
public class RegistrationViewController implements Initializable {
	@FXML
	private TextField firstName;
	@FXML
	private TextField lastName;
	@FXML
	private TextField number;
	@FXML
	private TextField email;
	@FXML
	private Button register;
	@FXML
	private ListView<String> studentListView;
	@FXML
	private Label errorText;
	@FXML
	private RadioButton parentButton;
	@FXML
	private RadioButton teacherButton;
	@FXML
	private Button addStudent;
	@FXML
	private List<Pair<String, String>> students = new ArrayList<>();

	private IController controller;
	private ResourceBundle localization;

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
	 * Method for opening addStudent dialog
	 */
	public void openStudents() {
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle(localization.getString("registerviewAddstudentTitle"));
		dialog.setHeaderText(localization.getString("registerviewAddstudentHeader"));

		ButtonType addButtonType = new ButtonType(localization.getString("add"), ButtonData.OK_DONE);
		ButtonType cancelButtonType = new ButtonType(localization.getString("cancel"), ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(addButtonType, cancelButtonType);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField firstname = new TextField();
		firstname.setPromptText(localization.getString("firstname"));
		TextField lastname = new TextField();
		lastname.setPromptText(localization.getString("lastname"));

		grid.add(new Label(localization.getString("firstname")), 0, 0);
		grid.add(firstname, 1, 0);
		grid.add(new Label(localization.getString("lastname")), 0, 1);
		grid.add(lastname, 1, 1);

		Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
		addButton.setDisable(true);

		firstname.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.trim().isEmpty() && !lastname.textProperty().isEmpty().get())
				addButton.setDisable(false);
			else
				addButton.setDisable(true);
		});
		lastname.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.trim().isEmpty() && !firstname.textProperty().isEmpty().get())
				addButton.setDisable(false);
			else
				addButton.setDisable(true);
		});
		dialog.getDialogPane().setContent(grid);

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == addButtonType) {
				return new Pair<>(firstname.getText(), lastname.getText());
			}
			return null;
		});

		Optional<Pair<String, String>> result = dialog.showAndWait();

		result.ifPresent(firstnameLastname -> {
			students.add(firstnameLastname);
			studentListView.getItems().add(firstnameLastname.getKey() + " " + firstnameLastname.getValue());
		});
	}

	/**
	 * Method for registering user
	 */
	public void registerUser() {
		if (firstName.getText().isEmpty() || lastName.getText().isEmpty() || email.getText().isEmpty()
				|| number.getText().isEmpty()) {
			errorText.setText("Täytä kaikki kentät!");
			return;
		}

		if (teacherButton.isSelected())
			registerTeacher();

		else if (parentButton.isSelected())
			registerParent();
	}

	/**
	 * Method for registering a new teacher
	 */
	public void registerTeacher() {
		if (controller.createUser(PersonType.TEACHER, firstName.getText(), lastName.getText(), number.getText(),
				email.getText())) {
			clearFields();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(localization.getString("registerviewAlertSuccessTitle"));
			alert.setHeaderText(null);
			alert.setContentText(localization.getString("registerviewAlertSuccessContent"));

			alert.showAndWait();
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(localization.getString("registerviewAlertErrorHeader"));
			alert.setContentText(localization.getString("registerviewAlertErrorContent"));

			alert.showAndWait();
		}
	}

	/**
	 * Method for registering a new parent
	 */
	public void registerParent() {
		if (controller.createUser(PersonType.PARENT, firstName.getText(), lastName.getText(), number.getText(),
				email.getText())) {
			controller.createStudent(students, email.getText());

			Alert alert = new Alert(AlertType.INFORMATION);
			clearFields();
			alert.setTitle(localization.getString("registerviewAlertSuccessTitle"));
			alert.setHeaderText(null);
			alert.setContentText(localization.getString("registerviewAlertSuccessContent"));

			alert.showAndWait();
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(localization.getString("registerviewAlertErrorHeader"));
			alert.setContentText(localization.getString("registerviewAlertErrorContent"));

			alert.showAndWait();
		}
	}

	/**
	 * Method for handling typeChange of teacher/parent radio button
	 */
	public void onTypeChange() {
		if (teacherButton.isSelected()) {
			addStudent.setOpacity(0);
			studentListView.setOpacity(0);
		} else if (parentButton.isSelected()) {
			addStudent.setOpacity(1);
			studentListView.setOpacity(1);
		}
	}

	/**
	 * Method for clearing all the TextFields and student ListView
	 */
	private void clearFields() {
		firstName.clear();
		lastName.clear();
		number.clear();
		email.clear();
		studentListView.getItems().clear();
		students.clear();
	}
}
