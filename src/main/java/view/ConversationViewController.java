package view;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import controller.CurrentUser;
import controller.DataHolder;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Conversation;
import model.Message;
import model.User;
import model.dao.ConversationDAO;
import model.dao.IConversationDAO;
import model.dao.IPersonDAO;
import model.dao.PersonDAO;

/**
 * Class for ConversationView functions
 * 
 * @author Olli
 */
public class ConversationViewController implements Initializable {
	@FXML
	private TextField name;
	@FXML
	private DatePicker date;
	@FXML
	private Button search;
	@FXML
	private TableView<Conversation> conversationsTV;
	@FXML
	private ChoiceBox<String> searchFilters;
	@FXML
	private ChoiceBox<String> users;
	@FXML
	private TableColumn<Conversation, String> nameColumn;
	@FXML
	private TableColumn<Conversation, String> dateColumn;
	@FXML
	private TableColumn<Conversation, String> stateColumn;
	@FXML
	private TableColumn<Conversation, String> buttonColumn;

	private ResourceBundle localization;
	private ObservableList<Conversation> conversationList;
	private IConversationDAO conversationDAO = new ConversationDAO();
	private String username;
	private GUI gui;

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

		String[] choices = { localization.getString("conversationViewAll"),
				localization.getString("conversationViewNew"), localization.getString("conversationViewRecieved"),
				localization.getString("conversationViewSent"), localization.getString("conversationViewNoMessage") };

		searchFilters.getItems().addAll(choices);
		searchFilters.setValue(choices[0]);

		username = CurrentUser.getUser().getUsername();

		updateConversations();

		Conversation[] conversationArray = conversationDAO.getConversationsByUsername(username);
		if (conversationArray != null) {
			conversationList = FXCollections.observableArrayList(conversationArray);
			conversationsTV.setItems(conversationList);
		}

		IPersonDAO personDAO = new PersonDAO();
		ArrayList<String> nameList = new ArrayList<>();
		nameList.add(localization.getString("conversationNewUserText"));

		User[] userList = personDAO.getUsers();

		if (userList != null) {
			for (User user : userList) {
				if (!user.getUsername().equals(username))
					nameList.add(user.getUsername());
			}
		}

		if (conversationArray != null) {
			for (Conversation c : conversationList) {
				if (nameList.contains((c.getReciver()))) {
					nameList.remove(c.getReciver());
				}
			}
		}

		users.getItems().addAll(nameList);
		users.setValue(nameList.get(0));

		conversationsTV.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {

				if (conversationsTV.getSelectionModel().getSelectedItem() == null)
					return;

				DataHolder holder = DataHolder.getInstance();
				holder.setConversation(conversationsTV.getSelectionModel().getSelectedItem());
				if (holder.getConversation() == null)
					return;

				gui.openView(Views.Messages);
			}
		});
	}

	/**
	 * Method for updating conversation list
	 */
	private void updateConversations() {
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("reciver"));
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
		stateColumn.setCellValueFactory( cellData -> new ReadOnlyStringWrapper(getState(cellData.getValue().getState())));
	}
	
	/**
	 * Method for getting conversation state
	 * @param state of a conversation
	 * @return String of state
	 */
	private String getState(int state) {
		if(state == 1) {
			return localization.getString("conversationViewNewMessage");
		}
		else if(state == 2) {
			return localization.getString("conversationViewSent");
		}
		else if(state == 3) {
			return localization.getString("conversationViewRecieved");
		}
		else 
			return localization.getString("conversationViewNoMessage");
	}
	
	
	/**
	 * Method for filtering conversations
	 */
	@FXML
	public void searchConversations() {
		Conversation[] conversationArray = conversationDAO.getConversationsByUsername(username);
		if (conversationArray != null) {
			conversationList = FXCollections.observableArrayList(conversationArray);
			
			int choice = searchFilters.getSelectionModel().getSelectedIndex();
			
			if(choice == 1) {
				for(int i = 0; i< conversationList.size();i++) {
					if(conversationList.get(i).getState() != 1)
						conversationList.remove(i);
				}
			} else if(choice == 2) {
				for(int i = 0; i< conversationList.size();i++) {
					if(conversationList.get(i).getState() != 3)
						conversationList.remove(i);
				}
			} else if(choice == 3) {
				for(int i = 0; i< conversationList.size();i++) {
					if(conversationList.get(i).getState() != 2)
						conversationList.remove(i);
				}
			} else if(choice == 4) {
				for(int i = 0; i< conversationList.size();i++) {
					if(conversationList.get(i).getState() != 4)
						conversationList.remove(i);
				}
			}
			
			if(isDate()) {
				java.sql.Date datePickerDate = java.sql.Date.valueOf(date.getValue());
				Date date = new java.util.Date(datePickerDate.getTime());
				for(int i = 0; i< conversationList.size();i++) {
					Date conversationDate = new Date(getDateWithoutTime(conversationList.get(i).getDate()).getTime());
					
					if(!conversationDate.equals(date))
						conversationList.remove(i);
				}	
			}
			
			if(!name.getText().isBlank()) {
				for(int i = 0; i< conversationList.size();i++) {
					if(!conversationList.get(i).getReciver().contains(name.getText()))
						conversationList.remove(i);
				}
			}
			
			conversationsTV.setItems(conversationList);
		} else {
			conversationsTV.setItems(null);
		}
	}
	
	
	/**
	 * Method for recognizing dates
	 * @return true if date is String else false
	 */
	private boolean isDate() {
		try {
			java.sql.Date.valueOf(date.getValue());
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	/**
	 * Method for getting date without time
	 * @param date where time will be removed from
	 * @return Date without time
	 */
	private Date getDateWithoutTime(Date date) { 
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		try {
			return formatter.parse(formatter.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}    
	}

	@FXML
	/**
	 * Method for starting a new conversation
	 */
	public void startNewConversation() {
		String reciver = users.getValue();

		if (users.getSelectionModel().getSelectedIndex() != 0) {
			Date date = new Date();

			Set<Message> messages = new HashSet<>();

			Conversation conversation = new Conversation(username, reciver, date,
					4, messages);
			Conversation conversation2 = new Conversation(reciver, username, date,
					4, messages);
			conversationDAO.createConversation(conversation);
			conversationDAO.createConversation(conversation2);
			DataHolder holder = DataHolder.getInstance();
			holder.setConversation(conversation);

			if (holder.getConversation() == null)
				return;

			gui.openView(Views.Messages);
		}

	}
}
