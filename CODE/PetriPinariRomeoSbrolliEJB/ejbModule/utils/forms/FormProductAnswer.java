package utils.forms;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a class used to represent an answer to a product of a questionnaire. This class contains all the information regarding the answer to a general question.
 * @author Marco Petri
 *
 */
public class FormProductAnswer {
	private int question;
	private int type;
	private List<String> responses;
	
	public FormProductAnswer() {
		question = 0;
		type = 0;
		responses = new ArrayList<>();
	}
	
	/* ******************
	 * 		SETTERS		*
	 ********************/
	public void setQuestion(int question) throws IllegalArgumentException {
		if (question <= 0) {
			throw new IllegalArgumentException();
		}
		
		this.question = question;
	}
	public void setType(int type) throws IllegalArgumentException {
		if (type <= 0 || type >= 5) {
			throw new IllegalArgumentException();
		}
		
		responses = new ArrayList<>();
		this.type = type;
	}
	public void setResponse(String response) throws IllegalArgumentException {
		if (type == 1) {
			throw new IllegalArgumentException();
		}
		
		if (!responses.isEmpty()) {
			responses.set(0, response);
		} else {
			responses.add(response);
		}
	}
	public void addResponse(String response) throws IllegalArgumentException {
		if (type != 1) {
			throw new IllegalArgumentException();
		}
		
		responses.add(response);
	}
	
	/* ******************
	 * 		GETTERS		*
	 ********************/
	public int getQuestion() {
		return question;
	}
	public int getType() {
		return type;
	}
	public List<String> getResponses() throws IllegalArgumentException {
		if (type != 1) {
			throw new IllegalArgumentException();
		}
		
		// defensive return
		return new ArrayList<>(responses);
	}
	public String getResponse() throws IllegalArgumentException {
		if (type == 1) {
			throw new IllegalArgumentException();
		}
		
		return responses.get(0);
	}
}
