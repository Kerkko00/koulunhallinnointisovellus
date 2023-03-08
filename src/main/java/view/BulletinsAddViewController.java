package view;

import java.net.URL;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import controller.CurrentUser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import model.Bulletin;
import model.Group;
import model.Teacher;
import model.dao.BulletinDAO;
import model.dao.GroupDAO;
import model.dao.IBulletinDAO;
import model.dao.IGroupDAO;

/**
 * Class for handling BulletinsAddView functions
 * 
 * @author Saku
 */
public class BulletinsAddViewController implements Initializable {
	@FXML
	private TextField bulletinTitle;
	@FXML
	private TextArea bulletinContent;
	@FXML
	private ListView<Group> groupsList;

	private GUI gui;
	private ResourceBundle localization;
	private IBulletinDAO bulletinDao = new BulletinDAO();
	private IGroupDAO groupDao = new GroupDAO();

	/**
	 * Method for setting local GUI
	 * 
	 * @param gui
	 */
	public void setGUI(GUI gui) {
		this.gui = gui;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		groupsList.getItems().addAll(groupDao.getGroups());
		groupsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		localization = LocalizationHelper.getLocalization();
	}

	@FXML
	/**
	 * Method for handling cancelButton
	 */
	public void handleCancelButton() {
		// Confirm that the user wants to discard the bulletin
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, localization.getString("bulletinCancelContent"),
				ButtonType.YES, ButtonType.NO);
		alert.setTitle(localization.getString("bulletinCancelTitle"));
		alert.setHeaderText(null);

		((Button) alert.getDialogPane().lookupButton(ButtonType.YES)).setText(localization.getString("yes"));
		((Button) alert.getDialogPane().lookupButton(ButtonType.NO)).setText(localization.getString("no"));

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.YES) {
			gui.openView(Views.Bulletins);
		}
	}

	@FXML
	/**
	 * Method for handling sendButton
	 */
	public void handleSendButton() {
		String title = bulletinTitle.getText();
		String content = bulletinContent.getText();
		Teacher teacher = CurrentUser.getUser().getTeacher();
		Set<Group> receivingGroups = new HashSet<>(groupsList.getSelectionModel().getSelectedItems());

		// Create a new bulletin if its data is valid
		if (validate(title, content, receivingGroups)) {
			Bulletin bulletin = new Bulletin(title, content, teacher, receivingGroups);
			boolean success = bulletinDao.createBulletin(bulletin);

			if (success) {
				gui.openView(Views.Bulletins);

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(localization.getString("bulletinSentSuccessTitle"));
				alert.setHeaderText(null);
				alert.setContentText(localization.getString("bulletinSentSuccessContent"));
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle(localization.getString("bulletinSentErrorTitle"));
				alert.setHeaderText(null);
				alert.setContentText(localization.getString("bulletinSentErrorContent"));
				alert.showAndWait();
			}
		}
	}

	/**
	 * Method for validating new bulletin
	 * 
	 * @param title
	 * @param content
	 * @param receivingGroups
	 * @return true if valid
	 */
	private boolean validate(String title, String content, Set<Group> receivingGroups) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(localization.getString("bulletinValidateTitle"));
		alert.setHeaderText(null);

		// Display alert if title/content/selectedGroup(s) are invalid
		if (title.length() == 0) {
			alert.setContentText(localization.getString("bulletinValidateTitleEmpty"));
			alert.showAndWait();
			bulletinTitle.requestFocus();
			return false;
		} else if (content.length() == 0) {
			alert.setContentText(localization.getString("bulletinValidateContentEmpty"));
			alert.showAndWait();
			bulletinContent.requestFocus();
			return false;
		} else if (receivingGroups.isEmpty()) {
			alert.setContentText(localization.getString("bulletinValidateReceiversEmpty"));
			alert.showAndWait();
			return false;
		}
		return true;
	}
}
