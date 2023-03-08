package view;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import controller.CurrentUser;
import controller.IController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.Group;
import model.Person;
import model.Student;

/**
 * Class for handling GroupView functions
 * 
 * @author Ville, Kerkko
 */
public class GroupViewController implements Initializable {
	@FXML
	private TextField groupName;
	@FXML
	private TextField studentNameTextField;
	@FXML
	private TextField number;
	@FXML
	private Button createGroup;
	@FXML
	private TableView<Person> studentTV;
	@FXML
	private TableColumn<Person, String> studentFNameColumn;
	@FXML
	private TableColumn<Person, String> studentLNameColumn;
	@FXML
	private TableColumn<Person, Button> studentActionsColumn;
	@FXML
	private TableView<Group> groupTV;
	@FXML
	private TableColumn<Group, String> groupNameColumn;
	@FXML
	private TableColumn<Group, Button> groupActionsColumn;

	private Set<Student> studentsInGroup = new HashSet<>();
	private ResourceBundle localization;
	private IController controller;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		localization = LocalizationHelper.getLocalization();
	}

	/**
	 * Method for setting local controller and loading groups into tableView
	 * 
	 * @param controller
	 */
	public void setController(IController controller) {
		this.controller = controller;

		groupNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
		groupActionsColumn
				.setCellFactory(TableViewHelper.<Group>forTableColumn(localization.getString("delete"), (Group g) -> {
					groupTV.getItems().remove(g);

					controller.deleteGroup(g);
					return g;
				}));
		Group[] groupArray = controller.getGroups();
		if (groupArray != null) {
			ObservableList<Group> groupOL = FXCollections.observableArrayList(controller.getGroups());
			groupTV.setItems(groupOL);
		}
	}

	@FXML
	/**
	 * Method for adding student to group
	 */
	public void addStudent() {
		List<Student> studentData = controller.getStudent(studentNameTextField.getText());

		/*
		 * If student is found from DB add table row containing students data, else
		 * display error message
		 */
		if (!studentData.isEmpty()) {
			studentFNameColumn
					.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
			studentLNameColumn
					.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
			// Use TableViewHelper-class to add delete button next to added student
			studentActionsColumn.setCellFactory(
					TableViewHelper.<Person>forTableColumn(localization.getString("delete"), (Person p) -> {
						studentTV.getItems().remove(p);
						studentsInGroup.remove(studentData.get(0));
						return p;
					}));

			studentTV.getItems().add(studentData.get(0).getPerson());
			studentsInGroup.add(studentData.get(0));
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle(localization.getString("groupViewAddErrorTitle"));
			alert.setHeaderText(localization.getString("groupViewAddErrorHeader"));
			alert.setContentText(localization.getString("groupViewAddErrorContent"));
			alert.showAndWait();
		}

	}

	@FXML
	/**
	 * Method for creating group using controller
	 */
	public void createGroup() {
		// If groupNameTextField is empty display error
		if (groupName.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle(localization.getString("groupViewAddErrorTitle"));
			alert.setHeaderText(localization.getString("groupViewCreateErrorNoNameHeader"));
			alert.setContentText(localization.getString("groupViewCreateErrorNoNameContent"));
			groupName.setStyle("-fx-border-color: red; -fx-border-width: 2px");
		} else {
			// If creating group with given data succeeds, else displays error
			if (controller.createGroup(groupName.getText(), studentsInGroup)) {
				groupTV.getItems().add(controller.getGroup(groupName.getText()));
				clearFields();
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(localization.getString("groupViewCreateSuccessTitle"));
				alert.setHeaderText(localization.getString("groupViewCreateSuccessHeader"));
				alert.setContentText(null);
				alert.showAndWait();
				groupName.setStyle("-fx-border-width: 0px");
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle(localization.getString("groupViewAddErrorTitle"));
				alert.setHeaderText(localization.getString("groupViewCreateErrorHeader"));
				alert.setContentText(localization.getString("groupViewCreateErrorContent"));
				alert.showAndWait();
			}
		}
	}

	/**
	 * Method for clearing FXML-elements
	 */
	private void clearFields() {
		studentTV.getItems().clear();
		groupName.setText("");
		studentNameTextField.setText("");
	}
}
