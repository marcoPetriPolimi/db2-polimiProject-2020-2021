package exceptions;

import javax.ejb.ApplicationException;

import utils.Const;

@ApplicationException(rollback=true)
public class QuestionnaireException extends Exception {
	private static final long serialVersionUID = Const.ExceptionVersion;
	
	public QuestionnaireException() {
		super();
	}
	public QuestionnaireException(String message) {
		super(message);
	}
}
