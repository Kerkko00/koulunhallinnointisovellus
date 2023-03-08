package view;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import controller.CurrentUser;
import controller.StudentChangedListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Bulletin;
import model.Student;
import model.dao.BulletinDAO;

/**
 * Class for handling BulletinsView functions
 * 
 * @author Saku
 */
public class BulletinsViewController implements Initializable {
	@FXML
	private Label bulletinsTitle;
	@FXML
	private TableView<Bulletin> bulletinsTable;
	@FXML
	private TableColumn<Bulletin, String> titleColumn;
	@FXML
	private TableColumn<Bulletin, String> senderColumn;
	@FXML
	private TableColumn<Bulletin, String> dateColumn;
	@FXML
	private Button btnNewBulletin;

	private GUI gui;
	private ResourceBundle localization;
	private BulletinDAO bulletinDAO = new BulletinDAO();

	/**
	 * Method for setting private GUI
	 * 
	 * @param gui
	 */
	public void setGUI(GUI gui) {
		this.gui = gui;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		localization = LocalizationHelper.getLocalization();

		// Show the new bulletin button only for teachers
		if (CurrentUser.getUser().getTeacher() == null) {
			btnNewBulletin.setManaged(false);
		}

		titleColumn.setCellValueFactory(new PropertyValueFactory<Bulletin, String>("title"));
		senderColumn.setCellValueFactory(new PropertyValueFactory<Bulletin, String>("senderName"));
		dateColumn.setCellValueFactory(new PropertyValueFactory<Bulletin, String>("sentDateString"));

		// Formatting of the date column
		dateColumn.setCellFactory(dateTableCell -> {
			return new TableCell<Bulletin, String>() {
				@Override
				protected void updateItem(String date, boolean dateIsEmpty) {
					super.updateItem(date, dateIsEmpty);
					if (date == null || dateIsEmpty) {
						setText(null);
					} else {
						Date bulletinSentDate;
						try {
							bulletinSentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(date);
						} catch (ParseException e) {
							bulletinSentDate = new Date();
						}
						String formattedDate = DateFormat
								.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, localization.getLocale())
								.format(bulletinSentDate);
						setText(formattedDate);
					}
				}
			};
		});

		// Update the title and visible bulletins based on the selected student
		updateView();
		CurrentUser.setStudentChangedListener(new StudentChangedListener() {
			@Override
			public void studentChanged(Student student) {
				updateView();
			}
		});

		// Open a selected bulletin's view
		bulletinsTable.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (bulletinsTable.getSelectionModel().getSelectedItem() == null)
					return;

				Bulletin selectedBulletin = bulletinsTable.getSelectionModel().getSelectedItem();
				gui.openView(Views.BulletinsOpenSelected, selectedBulletin);
			}
		});
	}

	/**
	 * Method for showing BulletinsAddView
	 */
	public void addNewBulletin() {
		gui.openView(Views.BulletinsAdd);
	}

	/**
	 * Method for refreshing View
	 */
	private void updateView() {
		// Update the title
		if (CurrentUser.getUser().getTeacher() != null) {
			bulletinsTitle.setText(localization.getString("bulletinsViewTeacherTitle"));
		} else {
			bulletinsTitle.setText(
					String.format(localization.getString("bulletinsViewParentTitle"), CurrentUser.getStudent()));
		}

		// Update the bulletins table
		bulletinsTable.getItems().clear();
		List<Bulletin> bulletins = bulletinDAO.getBulletins(CurrentUser.getUser());
		for (Bulletin bulletin : bulletins) {
			bulletinsTable.getItems().add(bulletin);
		}
	}
}
