package ejb.database;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * This class is the EJB for the submission database class.
 * @author Marco Petri
 */
@Entity
@Table(name = "submission", schema = "db2_project")
public class Submission implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@OneToOne
	@JoinColumn(name="questionnaireId")
	private Questionnaire questionnaire;
	@ManyToOne
	@JoinColumn(name="userId")
	private User senderUser;
	private boolean submitted;
	/**
	 * The points column of the database can be null, so an object is needed because a primitive type cannot be null.
	 */
	private Integer points;
	/**
	 * Usage of the annotation Temporal is needed to translate a java.util.Date object type (https://javaee.github.io/javaee-spec/javadocs/javax/persistence/Temporal.html).
	 */
	@Temporal(TemporalType.DATE)
	private Date date;
	
	public Submission() {}
	public Submission(Questionnaire questionnaire, User senderUser, boolean submitted, Integer points, Date date) {
		this.questionnaire		= questionnaire;
		this.senderUser			= senderUser;
		this.submitted			= submitted;
		this.points				= points;
		this.date				= date;
	}
	
	/* ******************
	 * 		SETTERS		*
	 ********************/
	public void setQuestionnaire(Questionnaire questionnaire) {
		this.questionnaire		= questionnaire;
	}
	public void setUser(User senderUser) {
		this.senderUser			= senderUser;
	}
	public void setSubmitted(boolean submitted) {
		this.submitted			= submitted;
	}
	public void setPoints(Integer points) {
		this.points				= points;
	}
	public void setDate(Date date) {
		this.date				= date;
	}
	
	/* ******************
	 * 		GETTERS		*
	 ********************/
	public Questionnaire getQuestionnaire() {
		return questionnaire;
	}
	public User getUser() {
		return senderUser;
	}
	public boolean getSubmitted() {
		return submitted;
	}
	public Integer getPoints() {
		return points;
	}
	public Date getDate() {
		return date;
	}
}