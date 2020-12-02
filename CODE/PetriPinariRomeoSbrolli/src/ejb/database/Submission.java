package ejb.database;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

/**
 * This class is the EJB for the submission database class.
 * @author Marco Petri
 */
@Entity
@Table(name = "submission", schema = "db2_project",
	uniqueConstraints = @UniqueConstraint(columnNames={"questionnaireId","userId"}))
public class Submission implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	private Questionnaire questionnaireId;
	
	@ManyToOne
	private User userId;
	
	@OneToMany (mappedBy = "submissionId")
	private List<ProductAnswer> productAnswers;
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
	public Submission(Questionnaire questionnaireId, User userId, boolean submitted, Integer points, Date date) {
		this.questionnaireId		= questionnaireId;
		this.userId			= userId;
		this.submitted			= submitted;
		this.points				= points;
		this.date				= date;
	}
	
	/* ******************
	 * 		SETTERS		*
	 ********************/
	
	public void setId(int id) {
		this.id = id;
	}

	public void setProductAnswers(List<ProductAnswer> productAnswers) {
		this.productAnswers = productAnswers;
	}
	public void setQuestionnaireId(Questionnaire questionnaireId) {
		this.questionnaireId = questionnaireId;
	}
	public void setUserId(User userId) {
		this.userId = userId;
	}
	
	public void setQuestionnaire(Questionnaire questionnaireId) {
		this.questionnaireId		= questionnaireId;
	}
	public void setUser(User userId) {
		this.userId			= userId;
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
	public Questionnaire getQuestionnaireId() {
		return questionnaireId;
	}
	public User getUserId() {
		return userId;
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
	
	public int getId() {
		return id;
	}
	
	public List<ProductAnswer> getProductAnswers() {
		return productAnswers;
	}
	
}