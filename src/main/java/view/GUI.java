package view;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import controller.Controller;
import controller.CurrentUser;
import controller.IController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Bulletin;

/**
 * Class for creating GUI and related components
 * 
 * @author Kerkko, Ville, Saku
 */

public class GUI extends Application {

	/**
	 * Private string for program title
	 */
	private static final String TITLE = "Kouluhallinnointisovellus";

	/**
	 * Private instance for mainStage
	 */
	private Stage primaryStage;

	/**
	 * Private instance for rootLayout view
	 */
	private BorderPane rootLayout;

	/**
	 * Private instance for controller
	 */
	private IController controller;

	/**
	 * Private instance for localization
	 */
	private ResourceBundle localization;

	/**
	 * The currently visible view
	 */
	private Views openView;

	/**
	 * The data that was most recently passed to the opened view's controller
	 */
	private Object viewPayload;

	/**
	 * Method for launching the program
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Method, which initializes primaryStage
	 * 
	 * @param primaryStage
	 */
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle(TITLE);
		initRootLayout();
		
		openView(Views.Login, null);
	}

	/**
	 * Method for linking RootLayout to MainView and loading localization resources
	 */
	private void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/view/RootLayout.fxml"));
			localization = ResourceBundle.getBundle("TextResources", Locale.getDefault());
			loader.setResources(localization);

			rootLayout = (BorderPane) loader.load();
		} catch (IOException e) {
			System.err.println("RootLayout loading failed.");
		}

		controller = new Controller(this);
		Scene scene = new Scene(rootLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setMinHeight(700);
		primaryStage.setMinWidth(1200);
	}

	/**
	 * Method for opening MainView after login
	 */
	public void openMainView() {
		showSideView();
		openView(Views.Conversation, null);
	}

	/**
	 * Method for navigating to different view of the application.
	 * 
	 * @param view The view to open
	 */
	public void openView(Views view) {
		openView(view, null);
	}

	/**
	 * Method for navigating to different view of the application.
	 * 
	 * @param view    The view to open
	 * @param payload Data to pass to the view's controller
	 */
	public void openView(Views view, Object payload) {
		viewPayload = payload;

		switch (view) {
		case Login:
			showLoginView();
			break;
		case Main:
			showMainView();
			break;
		case Conversation:
			showConversationView();
			break;
		case Messages:
			showMessagesView();
			break;
		case Bulletins:
			showBulletinsView();
			break;
		case BulletinsAdd:
			showBulletinsAddView();
			break;
		case BulletinsOpenSelected:
			showBulletinsOpenSelectedView((Bulletin) payload);
			break;
		case Absences:
			showAbsenceView();
			break;
		case GradesTeacher:
			showGradesTeacherView();
			break;
		case GradesParent:
			showGradesParentView();
			break;
		case Course:
			showCourseView();
			break;
		case Group:
			showGroupView();
			break;
		case Registration:
			showRegistrationView();
			break;
		case Settings:
			showSettingsView();
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + view);
		}
	}

	/**
	 * Method for updating user data in DB
	 */
	public void updateUser() {
		controller.updateUser(CurrentUser.getUser(), CurrentUser.getUser().getPassword());
	}

	/**
	 * Method for updating locale when changed
	 * 
	 * @param locale
	 */
	public void updateLocale(Locale locale) {
		localization = ResourceBundle.getBundle("TextResources", locale);
		showSideView();
		openView(openView, viewPayload);
	}

	/**
	 * Method for showing SideView and setting locale according to User preference
	 */
	private void showSideView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/view/SideView.fxml"));
			// Backup check for user
			if (CurrentUser.getUser() != null) {
				localization = LocalizationHelper.getLocalization();
			}
			loader.setResources(localization);
			BorderPane sideView = (BorderPane) loader.load();

			SideViewController svc = loader.getController();
			svc.setGUI(this);
			rootLayout.setLeft(sideView);
		} catch (IOException e) {
			System.err.println("SideView loading failed." + e.getMessage());
		}
	}

	/**
	 * Method for showing Login view
	 * 
	 */
	private void showLoginView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/view/LoginView.fxml"));
			loader.setResources(localization);
			BorderPane loginView = (BorderPane) loader.load();

			rootLayout.setCenter(loginView);
			rootLayout.setLeft(null);
			LoginViewController loginController = loader.getController();

			loginController.setController(controller);
			loginController.setGUI(this);

			controller.logout();

			openView = Views.Login;
		} catch (IOException e) {
			System.err.println("LoginView loading failed." + e.getMessage());
		}
	}

	/**
	 * Method for showing MainView
	 * 
	 */
	private void showMainView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/view/MainView.fxml"));
			loader.setResources(localization);
			BorderPane mainView = (BorderPane) loader.load();

			rootLayout.setCenter(mainView);

			openView = Views.Main;
		} catch (IOException e) {
			System.err.println("MainView loading failed." + e.getMessage());
		}
	}

	/**
	 * Method for showing ConversationsView
	 */
	private void showConversationView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/view/ConversationsView.fxml"));
			loader.setResources(localization);
			SplitPane conversationsView = (SplitPane) loader.load();

			ConversationViewController conversationsController = loader.getController();
			conversationsController.setGUI(this);

			rootLayout.setCenter(conversationsView);

			openView = Views.Conversation;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("ConversationsView loading failed." + e.getMessage());
		}
	}

	/**
	 * Method for showing MessagesView
	 */
	private void showMessagesView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/view/MessagesView.fxml"));
			loader.setResources(localization);
			SplitPane messagesView = (SplitPane) loader.load();

			MessagesViewController messagesController = loader.getController();
			messagesController.setGUI(this);

			rootLayout.setCenter(messagesView);

			openView = Views.Messages;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("MessagesView loading failed." + e.getMessage());
		}
	}

	/**
	 * Method for showing BulletinsView
	 */
	private void showBulletinsView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/view/BulletinsView.fxml"));
			loader.setResources(localization);
			BorderPane bulletinsView = (BorderPane) loader.load();

			BulletinsViewController bulletinsController = loader.getController();
			bulletinsController.setGUI(this);

			rootLayout.setCenter(bulletinsView);

			openView = Views.Bulletins;
		} catch (IOException e) {
			System.err.println("BulletinsView loading failed." + e.getMessage());
		}
	}

	/**
	 * Method for showing NewBulletinView
	 */
	private void showBulletinsAddView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/view/BulletinsAddView.fxml"));
			loader.setResources(localization);
			BorderPane newBulletinView = (BorderPane) loader.load();

			BulletinsAddViewController newBulletinController = loader.getController();
			newBulletinController.setGUI(this);

			rootLayout.setCenter(newBulletinView);

			openView = Views.BulletinsAdd;
		} catch (IOException e) {
			System.err.println("BulletinsAddView loading failed." + e.getMessage());
		}
	}

	/**
	 * Method for showing BulletinView
	 */
	private void showBulletinsOpenSelectedView(Bulletin bulletin) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/view/BulletinsOpenSelectedView.fxml"));
			loader.setResources(localization);
			BorderPane bulletinView = (BorderPane) loader.load();

			BulletinsOpenSelectedController bulletinController = loader.getController();
			bulletinController.setGUI(this);
			bulletinController.setBulletin(bulletin);

			rootLayout.setCenter(bulletinView);

			openView = Views.BulletinsOpenSelected;
		} catch (IOException e) {
			System.err.println("BulletinsOpenSelectedView loading failed." + e.getMessage());
		}
	}

	/**
	 * Method for showing AbsenceView
	 */
	private void showAbsenceView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/view/AbsenceView.fxml"));
			loader.setResources(localization);
			BorderPane absenceView = (BorderPane) loader.load();

			AbsencesViewController absenceController = loader.getController();
			absenceController.setController(controller);
			absenceController.setPrimaryStage(primaryStage);

			rootLayout.setCenter(absenceView);

			openView = Views.Absences;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Groupview loading failed." + e.getMessage());
		}
	}

	/**
	 * Method for showing GradesView for teachers
	 */
	private void showGradesTeacherView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/view/GradesTeacherView.fxml"));
			loader.setResources(localization);
			BorderPane gradesView = (BorderPane) loader.load();
			rootLayout.setCenter(gradesView);

			openView = Views.GradesTeacher;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("GradesViewTeacher loading failed.");
		}
	}

	/**
	 * Method for showing GradesView for parents
	 */
	private void showGradesParentView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/view/GradesParentView.fxml"));
			localization = ResourceBundle.getBundle("TextResources", CurrentUser.getUser().getLanguage());
			loader.setResources(localization);
			BorderPane gradesView = (BorderPane) loader.load();
			rootLayout.setCenter(gradesView);

			openView = Views.GradesParent;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("GradesViewParent loading failed.");
		}
	}

	/**
	 * Method for showing CourseView
	 */
	private void showCourseView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/view/CourseView.fxml"));
			loader.setResources(localization);
			BorderPane courseView = (BorderPane) loader.load();

			CourseViewController cvc = loader.getController();
			cvc.setController(controller);
			cvc.updateCourses();
			rootLayout.setCenter(courseView);

			openView = Views.Course;
		} catch (IOException e) {
			System.err.println("CourseView loading failed." + e.getMessage());
		}
	}

	/**
	 * Method for showing GroupView
	 */
	private void showGroupView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/view/GroupView.fxml"));
			loader.setResources(localization);
			BorderPane conversationsView = (BorderPane) loader.load();

			GroupViewController groupController = loader.getController();
			groupController.setController(controller);

			rootLayout.setCenter(conversationsView);

			openView = Views.Group;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Groupview loading failed." + e.getMessage());
		}
	}

	/**
	 * Method for showing RegistrationView
	 */
	private void showRegistrationView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/view/RegisterView.fxml"));
			loader.setResources(localization);
			BorderPane conversationsView = (BorderPane) loader.load();

			RegistrationViewController registrationController = loader.getController();
			registrationController.setController(controller);

			rootLayout.setCenter(conversationsView);

			openView = Views.Registration;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("RegistrationView loading failed." + e.getMessage());
		}
	}

	/**
	 * Method for showing SettingsView
	 */
	private void showSettingsView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/view/SettingsView.fxml"));
			loader.setResources(localization);
			BorderPane settingsView = (BorderPane) loader.load();

			SettingsViewController svc = loader.getController();
			svc.setController(controller);

			rootLayout.setCenter(settingsView);

			openView = Views.Settings;
		} catch (IOException e) {
			System.err.println("SettingsView loading failed. " + e.getMessage());
		}
	}
}
