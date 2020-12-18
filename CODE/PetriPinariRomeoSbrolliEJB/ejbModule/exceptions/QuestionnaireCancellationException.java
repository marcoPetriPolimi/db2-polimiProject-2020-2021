package exceptions;

import javax.ejb.ApplicationException;

@ApplicationException(rollback=true)
public class QuestionnaireCancellationException extends QuestionnaireException {
	private static final long serialVersionUID = 1L;
	public QuestionnaireCancellationException() {
		super();
	}
	public QuestionnaireCancellationException(String message) {
		super(message);
	}
}
