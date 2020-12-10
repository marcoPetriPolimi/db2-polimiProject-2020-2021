package utils.forms;

import java.util.ArrayList;
import java.util.List;

public class FormQuestion {
	private int question;
	private short type;
	private String questionText;
	private List<String> possibleAnswers;
	
	public FormQuestion() {
		question= 0;
		type= 0;
		possibleAnswers= new ArrayList<>();
	}
	
	public void addPossibleAnswer(String possibleAnswer) {
		if (type >= 3) {
			throw new IllegalArgumentException();
		}
		possibleAnswers.add(possibleAnswer);
		
	}
	
	/* ******************
	 * 		GETTERS		*
	 ********************/
	
	public int getQuestion() {
		return question;
	}


	public short getType() {
		return type;
	}

	public List<String> getPossibleAnswers() {
		return possibleAnswers;
	}
	
	public String getQuestionText() {
		return questionText;
	}

	
	/* ******************
	 * 		SETTERS		*
	 ********************/
	
	
	public void setPossibleAnswers(List<String> possibleAnswers) {
		if (type >= 3) {
			throw new IllegalArgumentException();
		}
		this.possibleAnswers = possibleAnswers;
	}

	public void setType(short type) {
		if (type <= 0 || type >= 5) {
			throw new IllegalArgumentException();
		}
		
		this.type = type;
	}
	
	public void setQuestion(int question) {
		this.question = question;
	}
	
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
}
