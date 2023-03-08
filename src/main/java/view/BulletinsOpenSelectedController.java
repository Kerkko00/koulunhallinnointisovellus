package view;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import controller.CurrentUser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import model.Bulletin;
import model.dao.BulletinDAO;

/**
 * Class for handling BulletinsOpenSelectedView functions
 * 
 * @author Saku
 */
public class BulletinsOpenSelectedController implements Initializable {
	@FXML
	private Label bulletinTitle;
	@FXML
	private Label bulletinSender;
	@FXML
	private Label bulletinReceivers;
	@FXML
	private TextArea bulletinContent;

	private GUI gui;
	private ResourceBundle localization;

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
		localization = LocalizationHelper.getLocalization();
	}

	/**
	 * Method for setting bulletin
	 * 
	 * @param bulletin
	 */
	public void setBulletin(Bulletin bulletin) {
		Bulletin actualBulletin = (new BulletinDAO()).getBulletin(bulletin.getID());

		// Display the title
		bulletinTitle.setText(actualBulletin.getTitle());

		// Display the receiver group names
		String receiverGroups = actualBulletin.getGroups().stream().map(b -> b.toString())
				.collect(Collectors.joining(", "));
		bulletinReceivers.setText(String.format(localization.getString("bulletinViewReceivingGroups"), receiverGroups));

		// Display the sender + sent date
		String formattedDate;
		try {
			// Get the stored date as a Date and turn it into a formatted string in the
			// correct locale
			Date bulletinSentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S")
					.parse(actualBulletin.getSentDateString());
			formattedDate = DateFormat
					.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, localization.getLocale())
					.format(bulletinSentDate);
		} catch (ParseException e) {
			formattedDate = "-";
		}
		bulletinSender.setText(String.format(localization.getString("bulletinViewSender"),
				actualBulletin.getSenderName(), formattedDate));

		// Display the content
		bulletinContent.setText(actualBulletin.getContent());
	}

	@FXML
	/**
	 * Method for handling backButton
	 */
	public void handleBackButton() {
		gui.openView(Views.Bulletins);
	}
}
