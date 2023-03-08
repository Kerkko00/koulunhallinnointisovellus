package view;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import controller.CurrentUser;
import controller.IController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.Absence;

/**
 * Class for handling AbsenceAddReasonView functions
 * 
 * @author Ville
 */
public class AbsenceAddReasonViewController implements Initializable {
	@FXML
	public TextArea reasonField;

	private IController controller;
	private AbsencesViewController viewController;
	private Stage reasonStage;
	private ResourceBundle localization;
	private Absence selectedAbsence;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		localization = LocalizationHelper.getLocalization();
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
	 * Method for setting selected absence
	 * 
	 * @param selectedAbsence
	 */
	public void setSelectedAbsence(Absence selectedAbsence) {
		this.selectedAbsence = selectedAbsence;
	}

	/**
	 * Method for setting local reason stage
	 * 
	 * @param reasonStage
	 */
	public void setReasonStage(Stage reasonStage) {
		this.reasonStage = reasonStage;
	}

	/**
	 * Method for setting controller
	 * 
	 * @param controller
	 */
	public void setController(IController controller) {
		this.controller = controller;
	}

	@FXML
	/**
	 * Method for adding reason to absence 
	 */
	public void handleAddButton() {
		if (reasonField.getText().length() > 0) {
			selectedAbsence.setReason(reasonField.getText());
			controller.updateAbsence(selectedAbsence);
			viewController.updateAbsences();
			reasonStage.close();
		} else {
			Alert info = new Alert(AlertType.WARNING);
			info.setHeaderText(localization.getString("absenceAddNewErrorHeader"));
			info.setContentText(localization.getString("absenceReasonAddErrorContent"));
			info.show();
		}
	}

	@FXML
	/**
	 * Method for closing stage
	 */
	public void handleCancelButton() {
		reasonStage.close();
	}

}
