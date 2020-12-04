package exceptions;

import utils.Const;

public class QuestionnaireException extends Exception {
	private static final long serialVersionUID = Const.ExceptionVersion;
	
	public QuestionnaireException() {
		super();
	}
	public QuestionnaireException(String message) {
		super(message);
	}
}
