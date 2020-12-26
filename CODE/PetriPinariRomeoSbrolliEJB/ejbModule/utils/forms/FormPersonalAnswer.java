package utils.forms;

/**
 * This is a class used to represent an answer to the personal answers. This class contains all the information regarding the answer to a general question.
 * @author Marco Petri
 */
public class FormPersonalAnswer {
	private char sex;
	private int age;
	private int expertise;
	
	public FormPersonalAnswer() {
		sex = 'X';
		age = -1;
		expertise = -1;
	}

	/* ******************
	 * 		SETTERS		*
	 ********************/
	public void setExpertise(int expertise) throws IllegalArgumentException {
		if (expertise <= 0 || expertise >= 3) {
			throw new IllegalArgumentException();
		}
		
		this.expertise = expertise;
	}
	public void setAge(int age) throws IllegalArgumentException {
		if (age < 0 || age > 130) {
			throw new IllegalArgumentException();
		}
		
		this.age = age;
	}
	public void setSex(char sex) throws IllegalArgumentException {
		if (sex != 'M' && sex != 'F' && sex != 'U') {
			throw new IllegalArgumentException();
		}
		
		this.sex = sex;
	}
	
	/* ******************
	 * 		GETTERS		*
	 ********************/
	public int getExpertise() {
		return expertise;
	}
	public int getAge() {
		return age;
	}
	public char getSex() {
		return sex;
	}
}
