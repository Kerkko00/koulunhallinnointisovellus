package model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name = "Bulletin")
/**
 * Class for handling Bulletin data
 * 
 * @author Saku
 */
public class Bulletin {

	@Id
	@GeneratedValue
	private long bulletinID;

	@Column(name = "title")
	private String bulletinTitle;

	@Column(name = "content")
	private String bulletinContent;

	@ManyToOne
	@JoinColumn(name = "teacherID", nullable = false)
	private Teacher bulletinSender;

	@Column(name = "date")
	private Date sentDate;

	@PrePersist
	protected void onCreate() {
		sentDate = new Date();
	}

	@ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	@JoinTable(name = "BulletinReceiverGroups", joinColumns = {
			@JoinColumn(name = "bulletinID") }, inverseJoinColumns = { @JoinColumn(name = "groupID") })
	private Set<Group> bulletinGroups = new HashSet<>();

	public Bulletin() {
	}

	public Bulletin(String title, String content, Teacher sender, Set<Group> groups) {
		this.bulletinTitle = title;
		this.bulletinContent = content;
		this.bulletinSender = sender;
		this.bulletinGroups = groups;
	}

	public long getID() {
		return bulletinID;
	}

	public String getTitle() {
		return bulletinTitle;
	}

	public void setTitle(String title) {
		bulletinTitle = title;
	}

	public String getContent() {
		return bulletinContent;
	}

	public void setContent(String content) {
		bulletinContent = content;
	}

	public Teacher getSender() {
		return bulletinSender;
	}

	public String getSenderName() {
		return bulletinSender.getPerson().getFullName();
	}

	public void setSender(Teacher sender) {
		if (bulletinSender != null) {
			bulletinSender.getBulletins().remove(this);
		}
		bulletinSender = sender;
		bulletinSender.getBulletins().add(this);
	}

	public Set<Group> getGroups() {
		return bulletinGroups;
	}

	public void setGroups(Set<Group> groups) {
		bulletinGroups = groups;
	}

	public void addGroup(Group group) {
		if (bulletinGroups == null) {
			bulletinGroups = new HashSet<>();
		}

		bulletinGroups.add(group);

		group.getBulletins().add(this);
	}

	public Date getSentDate() {
		return this.sentDate;
	}

	public String getSentDateString() {
		if (this.sentDate == null)
			return "";
		return this.sentDate.toString();
	}

	public void setSentDate(Date date) {
		this.sentDate = date;
	}
}
