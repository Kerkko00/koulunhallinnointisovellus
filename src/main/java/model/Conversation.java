package model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Conversation")
/**
 * Class for conversation data
 * 
 * @author Olli
 */
public class Conversation {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long conversationID;

	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "reciver", nullable = false)
	private String reciver;

	@Column(name = "date", nullable = false)
	private Date date;

	@Column(name = "state", nullable = false)
	private int state;			//1 new, 2 sent, 3 received, 4 no messages

	@ManyToMany(targetEntity = Message.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "ConversationMessageGroups", joinColumns = {
			@JoinColumn(name = "conversationID") }, inverseJoinColumns = { @JoinColumn(name = "messageID") })
	private Set<Message> messages = new HashSet<>();

	public Conversation() {
	}

	public Conversation(String username, String reciver, Date date, int state, Set<Message> messages) {
		super();
		this.username = username;
		this.reciver = reciver;
		this.date = date;
		this.state = state;
		this.messages = messages;
	}

	public String getUsername() {
		return username;
	}

	public String getReciver() {
		return reciver;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void setMessages(Set<Message> messages) {
		this.messages = messages;
	}

	public Set<Message> getMessages() {
		return messages;
	}

	public long getID() {
		return conversationID;
	}
}