package view;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Set;

import controller.DataHolder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import model.Conversation;
import model.Message;
import model.dao.ConversationDAO;
import model.dao.IConversationDAO;

/**
 * Class for handling MessagesView functions
 * 
 * @author Olli
 */
public class MessagesViewController implements Initializable {
	@FXML
	private TextField text;
	@FXML
	private GUI gui;
	@FXML
	private Text name;
	@FXML
	private VBox vbox;
	@FXML
	private Text noMessagesText;
	@FXML
	private ScrollPane scrollPane;

	private Conversation conversation;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		IConversationDAO dao = new ConversationDAO();
		DataHolder holder = DataHolder.getInstance();
		conversation = holder.getConversation(); // get current conversation

		if (conversation == null) { // if no conversation -> return to conversation view.
			gui.openView(Views.Conversation);
			return;
		}
		name.setText(conversation.getReciver()); // set conversation name

		if (conversation.getState() == 1) { // change conversation state
			conversation.setState(3);
			dao.updateConversation(conversation);
		}

		// change scroll pane height when sending message
		vbox.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				scrollPane.setVvalue((double) newValue);
			}
		});
		showMessages(); // show message history
	}

	/**
	 * Method for setting local GUI
	 * 
	 * @param gui
	 */
	public void setGUI(GUI gui) {
		this.gui = gui;
	}

	@FXML
	/**
	 * Method for closing MessagesView
	 */
	public void leaveConversation() {
		gui.openView(Views.Conversation);
		conversation = null;
	}

	@FXML
	/**
	 * Method for sending message
	 */
	public void sendMessage() {
		if (noMessagesText.isVisible())
			noMessagesText.setVisible(false);

		String message = text.getText(); // get text field input
		if (!message.isEmpty()) { // check if input is empty
			text.setText(""); // clear input
			HBox hbox = new HBox();
			hbox.setAlignment(Pos.CENTER_RIGHT);
			hbox.setPadding(new Insets(5, 5, 5, 10));

			Text text = new Text(message);
			TextFlow textFlow = new TextFlow(text);

			textFlow.setStyle("-fx-color: rgb(239, 242, 255);" + " -fx-background-color: rgb(15, 125, 242);"
					+ " -fx-background-radius: 20px;");

			textFlow.setPadding(new Insets(5, 10, 5, 10));
			text.setFill(Color.color(0.934f, 0.945f, 0.996f));
			hbox.getChildren().add(textFlow);
			vbox.getChildren().add(hbox); // style and show message
			vbox.getScene().getWindow().setWidth(vbox.getScene().getWidth() + 0.001);
			Date date = new Date();

			Message newMessage = new Message(message, date, true, conversation.getReciver(),
					conversation.getUsername());

			Set<Message> messages = conversation.getMessages();
			messages.add(newMessage);

			conversation.setMessages(messages);
			conversation.setState(2);
			conversation.setDate(date);
			IConversationDAO dao = new ConversationDAO();

			dao.updateConversation(conversation);

			Conversation otherConversation = dao.getConversation(conversation.getReciver(), conversation.getUsername());

			Set<Message> otherMessages = otherConversation.getMessages();
			otherMessages.add(newMessage);
			otherConversation.setMessages(otherMessages);
			otherConversation.setState(1);
			otherConversation.setDate(date);

			dao.updateConversation(otherConversation);
		}

	}

	/**
	 * Method for showing messages in a list
	 */
	private void showMessages() {
		ArrayList<Message> messages = new ArrayList<Message>(conversation.getMessages());// list conversation messages

		if (messages.size() > 0) {

			Collections.sort(messages, new Comparator<Message>() {
	            @Override
	            public int compare(Message o1, Message o2) {
	                return o1.getDate().compareTo(o2.getDate());
	            }
	        });
			
			
			for (Message message : messages) {
				showMessage(message);
			}
		} else {
			noMessagesText.setVisible(true);
		}
	}

	/**
	 * Method for displaying contents of a message
	 * 
	 * @param message selected in MessagesView
	 */
	private void showMessage(Message message) {
		// if current user has sent a message
		if (message.getSender().equals(conversation.getUsername())) {
			String messageContent = message.getContent();

			HBox hbox = new HBox();
			hbox.setAlignment(Pos.CENTER_RIGHT);
			hbox.setPadding(new Insets(5, 5, 5, 10));

			Text text = new Text(messageContent);
			TextFlow textFlow = new TextFlow(text);

			textFlow.setStyle("-fx-color: rgb(239, 242, 255);" + " -fx-background-color: rgb(15, 125, 242);"
					+ " -fx-background-radius: 20px;");

			textFlow.setPadding(new Insets(5, 10, 5, 10));
			text.setFill(Color.color(0.934f, 0.945f, 0.996f));
			hbox.getChildren().add(textFlow);
			vbox.getChildren().add(hbox); // style and show message
		} else // if user has received a message
		{
			HBox hbox = new HBox();
			hbox.setAlignment(Pos.CENTER_LEFT);
			hbox.setPadding(new Insets(5, 5, 5, 10));

			Text text = new Text(message.getContent());
			TextFlow textFlow = new TextFlow(text);

			textFlow.setStyle(" -fx-background-color: rgb(233, 233, 235);" + " -fx-background-radius: 20px;");

			textFlow.setPadding(new Insets(5, 10, 5, 10));
			hbox.getChildren().add(textFlow);
			vbox.getChildren().add(hbox); // style and show message
		}
	}
}
