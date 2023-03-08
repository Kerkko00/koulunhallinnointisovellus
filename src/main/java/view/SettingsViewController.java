package view;

import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import controller.CurrentUser;
import controller.IController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

/**
 * Class for handling SettingsView functions
 * 
 * @author Kerkko, Ville
 */
public class SettingsViewController implements Initializable {
	@FXML
	public TextField firstName;
	@FXML
	public TextField lastName;
	@FXML
	public TextField phonenumber;
	@FXML
	public TextField email;
	@FXML
	public PasswordField oldPassword;
	@FXML
	public PasswordField newPassword;
	@FXML
	public PasswordField newPasswordConfirmation;
	@FXML
	public Label errorTextPassword;
	@FXML
	public Label errorTextOldPassword;
	@FXML
	public Label errorText;

	private IController controller;
	private ResourceBundle localization;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		localization = LocalizationHelper.getLocalization();

		firstName.setText(CurrentUser.getPerson().getFirstName());
		lastName.setText(CurrentUser.getPerson().getLastName());
		phonenumber.setText(CurrentUser.getPerson().getPhoneNumber());
		email.setText(CurrentUser.getPerson().getEmail());
	}

	/**
	 * Method for defining controller
	 * 
	 * @param controller
	 */
	public void setController(IController controller) {
		this.controller = controller;
	}

	@FXML
	/**
	 * Method for saving person data
	 */
	public void savePerson() {
		if (firstName.getText().isEmpty() || lastName.getText().isEmpty() || phonenumber.getText().isEmpty()
				|| email.getText().isEmpty()) {
			errorText.setText(localization.getString("settingsValidateContactInfoEmpty"));
			return;
		} else {
			errorText.setText("");
		}

		Alert confirmation = new Alert(AlertType.CONFIRMATION, localization.getString("settingsSaveContactInfoContent"),
				ButtonType.YES, ButtonType.NO);
		confirmation.setTitle(localization.getString("settingsSaveContactInfoTitle"));
		confirmation.setHeaderText(null);

		((Button) confirmation.getDialogPane().lookupButton(ButtonType.YES)).setText(localization.getString("yes"));
		((Button) confirmation.getDialogPane().lookupButton(ButtonType.NO)).setText(localization.getString("no"));

		Optional<ButtonType> result = confirmation.showAndWait();
		if (result.get() == ButtonType.YES) {
			CurrentUser.getPerson().setFirstName(firstName.getText());
			CurrentUser.getPerson().setLastName(lastName.getText());
			CurrentUser.getPerson().setPhoneNumber(phonenumber.getText());
			CurrentUser.getPerson().setEmail(email.getText());
			if (controller.updatePerson(CurrentUser.getPerson())) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(localization.getString("settingsSaveContactInfoSuccessTitle"));
				alert.setHeaderText(null);
				alert.setContentText(localization.getString("settingsSaveContactInfoSuccessContent"));

				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle(localization.getString("settingsSaveContactInfoErrorTitle"));
				alert.setHeaderText(null);
				alert.setContentText(localization.getString("settingsSaveContactInfoErrorContent"));

				alert.showAndWait();
			}
		}
	}

	@FXML
	/**
	 * Method for saving user data
	 */
	public void saveUser() {
		if (!validPassword()) {
			return;
		}

		Alert confirmation = new Alert(AlertType.CONFIRMATION, localization.getString("settingsSaveLoginInfoContent"),
				ButtonType.YES, ButtonType.NO);
		confirmation.setTitle(localization.getString("settingsSaveLoginInfoTitle"));
		confirmation.setHeaderText(null);

		((Button) confirmation.getDialogPane().lookupButton(ButtonType.YES)).setText(localization.getString("yes"));
		((Button) confirmation.getDialogPane().lookupButton(ButtonType.NO)).setText(localization.getString("no"));

		Optional<ButtonType> result = confirmation.showAndWait();
		if (result.get() == ButtonType.YES) {

			CurrentUser.getUser().setPassword(newPassword.getText());

			if (controller.updateUser(CurrentUser.getUser(), oldPassword.getText())) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(localization.getString("settingsSaveLoginInfoSuccessTitle"));
				alert.setHeaderText(null);
				alert.setContentText(localization.getString("settingsSaveLoginInfoSuccessContent"));

				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle(localization.getString("settingsSaveLoginInfoErrorTitle"));
				alert.setHeaderText(null);
				alert.setContentText(localization.getString("settingsSaveLoginInfoErrorContent"));

				alert.showAndWait();
			}
		}
	}

	/**
	 * Method for checking if a password is valid
	 * 
	 * @return boolean true if valid, else false
	 */
	private boolean validPassword() {
		boolean validationError = true;

		errorTextOldPassword.setText("");
		errorTextPassword.setText("");

		if (!oldPassword.getText().equals(CurrentUser.getUser().getPassword())) {
			errorTextOldPassword.setText(localization.getString("settingsValidateOldPasswordWrong"));
			validationError = false;
		}

		if (!newPassword.getText().equals(newPasswordConfirmation.getText())) {
			errorTextPassword.setText(localization.getString("settingsValidatePasswordMismatch"));
			validationError = false;
		}

		return validationError;
	}
}
