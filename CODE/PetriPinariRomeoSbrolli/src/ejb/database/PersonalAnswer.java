package ejb.database;

import java.io.Serializable;

import javax.persistence.*;

/**
 * The persistent class for the personalanswer database table.
 * @author Cristian Sbrolli
 */


@Entity
@Table(name = "personalanswer", schema = "db2_project")
public class PersonalAnswer implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int age;
	private char sex;
	private int expertise;

	@ManyToOne
	private Submission submissionId;
	
	public PersonalAnswer(User user,Questionnaire questionnaire,int age,char sex,int expertise) {
		this.age=age;
		this.sex=sex;
		this.expertise=expertise;
	}
	
	public PersonalAnswer() {}
	
	
	/* ******************
	 * 		SETTERS		*
	 ********************/
	public void setAge(int age) {
		this.age = age;
	}

	public void setSex(char sex) {
		this.sex = sex;
	}
	
	public void setExpertise(int expertise) {
		this.expertise = expertise;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setSubmissionId(Submission submissionId) {
		this.submissionId = submissionId;
	}

	
	/* ******************
	 * 		GETTERS		*
	 ********************/
	
	public int getAge() {
		return age;
	}


	public char getSex() {
		return sex;
	}


	public int getExpertise() {
		return expertise;
	}


	
	public int getId() {
		return id;
	}
	
	public Submission getSubmissionId() {
		return submissionId;
	}

	
}
