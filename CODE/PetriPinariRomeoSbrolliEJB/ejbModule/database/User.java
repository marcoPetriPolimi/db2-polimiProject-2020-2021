package database;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import utils.Const;


/**
 * This class is the EJB for the user database class.
 * @author Marco Petri
 */
@Entity
@Table(name = "user", schema = "db2_project")
@NamedQueries(value = {
	@NamedQuery(name = "User.findByName", query = "SELECT u FROM User u WHERE u.nickname = ?1"),
	@NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = ?1"),
	@NamedQuery(name= "User.getAllTimeLeaderBoard", query= "SELECT u.nickname, u.points FROM User u WHERE u.role=1 ORDER BY u.points DESC")
})
public class User implements Serializable {
	private static final long serialVersionUID = Const.EJBVersion;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(unique=true)
	private String nickname;
	private String password;
	private String email;
	/**
	 * Usage of the annotation Temporal is needed to translate a java.util.Date object type (https://javaee.github.io/javaee-spec/javadocs/javax/persistence/Temporal.html).
	 */
	@Temporal(TemporalType.DATE)
	private Date registration;

	/**
	 * The points column of the database can be null, so an object is needed because a primitive type cannot be null.
	 */
	private Integer points;
	private boolean blocked;
	private int role;
	
	@OneToMany(mappedBy = "creator",  cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Questionnaire> questionnaires;
	
	/**
	 * Reverse relationship for the 1:N relationship with the submissions.
	 */
	@OneToMany(mappedBy="userSender")
	private Collection<Submission> submissions;
	
	public User() {
	}
	public User(String nickname, String password, String email, Date registration, Integer points, boolean blocked, int role) {
		this.nickname		= nickname;
		this.password		= password;
		this.email			= email;
		this.registration	= registration;
		this.points			= points;
		this.blocked		= blocked;
		this.role			= role;
	}
	
	
	public void addQuestionnaire(Questionnaire questionnaire) {
		getQuestionnaires().add(questionnaire);
		questionnaire.setCreator(this);
	}
	
	/* ******************
	 * 		SETTERS		*
	 ********************/
	
	public void setId(int id) {
		this.id = id;
	}

	public void setQuestionnaires(List<Questionnaire> questionnaires) {
		this.questionnaires = questionnaires;
	}

	public void setSubmissions(Collection<Submission> submissions) {
		this.submissions = submissions;
	}
	
	public void setNickname(String nickname) {
		this.nickname		= nickname;
	}
	public void setPassword(String password) {
		this.password		= password;
	}
	public void setEmail(String email) {
		this.email			= email;
	}
	public void setRegistration(Date registration) {
		this.registration	= registration;
	}
	public void setPoints(Integer points) {
		this.points			= points;
	}
	public void setBlocked(boolean blocked) {
		this.blocked		= blocked;
	}
	public void setRole(int role) {
		this.role			= role;
	}
	
	
	
	/* ******************
	 * 		GETTERS		*
	 ********************/
	
	public int getId() {
		return id;
	}
	
	public List<Questionnaire> getQuestionnaires() {
		return questionnaires;
	}
	
	public Collection<Submission> getSubmissions() {
		return submissions;
	}
	
	public String getNickname() {
		return nickname;
	}
	public String getPassword() {
		return password;
	}
	public String getEmail() {
		return email;
	}
	public Date getRegistration() {
		return registration;
	}
	public Integer getPoints() {
		return points;
	}
	public boolean getBlocked() {
		return blocked;
	}
	public int getRole() {
		return role;
	}
}
