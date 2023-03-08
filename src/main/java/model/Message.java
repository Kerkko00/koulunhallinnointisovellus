package model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Message")
/**
 * Class for handling message data
 * 
 * @author Olli
 */
public class Message {

	@Id
	@GeneratedValue
	private long messageID;
	@Column(name = "content")
	private String content;
	@Column(name = "sendDate")
	private Date sendDate;
	@Column(name = "isNew")
	private boolean isNew;
	@Column(name = "sender")
	private String sender;
	@Column(name = "reciver")
	private String reciver;

	public Message() {
	}

	public Message(String content, Date date, boolean isNew, String reciver, String sender) {
		this.content = content;
		this.sendDate = date;
		this.isNew = isNew;
		this.reciver = reciver;
		this.sender = sender;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public String getSender() {
		return sender;
	}

	public String getReciver() {
		return reciver;
	}

	public Date getDate() {
		return sendDate;
	}
}
