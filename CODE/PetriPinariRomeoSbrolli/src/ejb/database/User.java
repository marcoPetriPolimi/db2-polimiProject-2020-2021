package ejb.database;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.*;


/**
 * This class is the EJB for the user database class.
 * @author Marco Petri
 */
@Entity
@Table(name = "user", schema = "db2_project")
public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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
	
	@OneToMany(mappedBy = "creatorId")
	private List<Questionnaire> questionnaires;
	
	/**
	 * Reverse relationship for the 1:N relationship with the submissions.
	 */
	@OneToMany(mappedBy="userId")
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
	
	/* ******************
	 * 		SETTERS		*
	 ********************/
	
	public void setId(int id) {
		this.id = id;
	}

	public void setquestionnaires(List<Questionnaire> questionnaires) {
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
	
	public List<Questionnaire> getquestionnaires() {
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
