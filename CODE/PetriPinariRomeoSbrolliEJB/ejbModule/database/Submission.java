package database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import utils.Const;

/**
 * This class is the EJB for the submission database class.
 * @author Marco Petri
 */
@Entity
@Table(name = "submission", schema = "db2_project",
	uniqueConstraints = @UniqueConstraint(columnNames={"questionnaireId","userId"}))
@NamedQueries(value = {
		@NamedQuery(name = "Submission.findByNameAndQuestionnaire", query = "SELECT s FROM Submission s WHERE s.userSender.id = ?1 AND s.submissionQuestionnaire.id = ?2")
})
public class Submission implements Serializable {
	private static final long serialVersionUID = Const.EJBVersion;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "questionnaireId")
	private Questionnaire submissionQuestionnaire;
	
	@ManyToOne
	@JoinColumn(name = "userId")
	private User userSender;
	
	@OneToMany (fetch = FetchType.EAGER, mappedBy = "submission",  cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductAnswer> productAnswers;
	
	@OneToOne (fetch = FetchType.EAGER, mappedBy = "submission", cascade = CascadeType.ALL, orphanRemoval = true)
	private PersonalAnswer personalAnswer;
	private int submitted;
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
	public Submission(Questionnaire submissionQuestionnaire, User userSender, int submitted, Integer points, Date date) {
		this.submissionQuestionnaire = submissionQuestionnaire;
		this.userSender = userSender;
		this.submitted = submitted;
		this.points = points;
		this.date = date;
		this.productAnswers = new ArrayList<>();
	}
	
	public void addProductAnswer(ProductAnswer productAnswer) {
		getProductAnswers().add(productAnswer);
		productAnswer.setSubmission(this);
	}
	
	/* ******************
	 * 		SETTERS		*
	 ********************/
	

	public void setSubmissionQuestionnaire(Questionnaire submissionQuestionnaire) {
		this.submissionQuestionnaire = submissionQuestionnaire;
	}

	public void setUserSender(User userSender) {
		this.userSender = userSender;
	}

	public void setPersonalAnswers(PersonalAnswer personalAnswers) {
		this.personalAnswer = personalAnswers;
		personalAnswers.setSubmission(this);
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setProductAnswers(List<ProductAnswer> productAnswers) {
		this.productAnswers = productAnswers;
		for (ProductAnswer p : productAnswers) {
			p.setSubmission(this);
		}
	}
	public void setQuestionnaire(Questionnaire submissionQuestionnaire) {
		this.submissionQuestionnaire = submissionQuestionnaire;
	}
	public void setUser(User userSender) {
		this.userSender = userSender;
	}
	public void setSubmitted(int submitted) {
		this.submitted = submitted;
	}
	public void setPoints(Integer points) {
		this.points = points;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	/* ******************
	 * 		GETTERS		*
	 ********************/
	public Questionnaire getQuestionnaire() {
		return submissionQuestionnaire;
	}
	public User getUser() {
		return userSender;
	}
	public int getSubmitted() {
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
	
	public Questionnaire getSubmissionQuestionnaire() {
		return submissionQuestionnaire;
	}
	
	public User getUserSender() {
		return userSender;
	}
	
	public PersonalAnswer getPersonalAnswers() {
		return personalAnswer;
	}
	
}