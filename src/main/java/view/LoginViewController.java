package view;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import controller.CurrentUser;
import controller.IController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;

/**
 * Class for handling LoginView functions
 * 
 * @author Ville
 */
public class LoginViewController implements Initializable {
	@FXML
	private TextField username;
	@FXML
	private PasswordField password;
	@FXML
	private Button logIn;
	@FXML
	private Button register;
	@FXML
	private Text error;

	private GUI gui;
	private ResourceBundle localization;
	private IController controller;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (CurrentUser.getUser() != null) {
			localization = LocalizationHelper.getLocalization();
		}
		
		username.setOnKeyPressed((event) -> {
			if (event.getCode() == KeyCode.ENTER) {
				handleLogin();
			}
		});
		password.setOnKeyPressed((event) -> {
			if (event.getCode() == KeyCode.ENTER) {
				handleLogin();
			}
		});
	}

	/**
	 * Method for setting local GUI
	 * 
	 * @param gui
	 */
	public void setGUI(GUI gui) {
		this.gui = gui;
	}

	/**
	 * Method for setting local controller
	 * 
	 * @param controller
	 */
	public void setController(IController controller) {
		this.controller = controller;
	}

	@FXML
	/**
	 * Method for handling login
	 */
	public void handleLogin() {
		if (controller.login(username.getText(), password.getText())) {
			gui.openMainView();
		} else {
			error.setText(localization.getString("loginError"));
		}
	}
}
