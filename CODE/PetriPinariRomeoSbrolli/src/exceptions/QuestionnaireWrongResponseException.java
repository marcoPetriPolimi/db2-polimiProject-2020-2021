package exceptions;

import utils.Const;

public class QuestionnaireWrongResponseException extends QuestionnaireException {
	private static final long serialVersionUID = Const.ExceptionVersion;
	
	public QuestionnaireWrongResponseException() {
		super();
	}
	public QuestionnaireWrongResponseException(String message) {
		super(message);
	}
}
