package exceptions;

import javax.ejb.ApplicationException;

import utils.Const;

@ApplicationException(rollback=true)
public class QuestionnaireWrongResponseException extends QuestionnaireException {
	private static final long serialVersionUID = Const.ExceptionVersion;
	
	public QuestionnaireWrongResponseException() {
		super();
	}
	public QuestionnaireWrongResponseException(String message) {
		super(message);
	}
}
