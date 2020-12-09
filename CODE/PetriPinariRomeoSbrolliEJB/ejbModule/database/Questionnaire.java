package database;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import utils.Const;

/**
 * This class is the EJB for the Questionnaire database table.
 * @author Giorgio Romeo
 */

@Entity
@Table (name = "Questionnaire", schema = "db2_project")
public class Questionnaire implements Serializable{
	private static final long serialVersionUID = Const.EJBVersion;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	/**
	 * Usage of the annotation Temporal is needed to translate a java.util.Date object type (https://javaee.github.io/javaee-spec/javadocs/javax/persistence/Temporal.html).
	 */
	@Temporal(TemporalType.DATE)
	private Date date;
	
	@Column(unique=true)
	private String name;

	@OneToMany (mappedBy = "submissionQuestionnaire")
	private List<Submission> submissions;

	@ManyToOne
	@JoinColumn(name = "creatorId")
	private User creator;
	
	@OneToMany (mappedBy = "inclusionQuestionnaire")
	private List<Inclusion> questionnaireInclusions;
	
	public Questionnaire() {}
	public Questionnaire(int id, Date date, String name) {
		this.id = id;
		this.date = date;
		this.name = name;
	}
	
	/* ******************
	 * 		SETTERS		*
	 ********************/
	public void setId(int id) {
		this.id = id;
	}
	
	public void setCreator(User creator) {
		this.creator = creator;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	public void setSubmissions(List<Submission> submissions) {
		this.submissions = submissions;
	}
	
	/* ******************
	 * 		GETTERS		*
	 ********************/
	public int getId() {
		return id;
	}
	
	public User getCreator() {
		return creator;
	}
	
	public Date getDate() {
		return date;
	}
	
	public String getName() {
		return name;
	}

	public List<Submission> getSubmissions() {
		return submissions;
	}
	
	public List<Inclusion> getInclusions() {
		return questionnaireInclusions;
	}
}
