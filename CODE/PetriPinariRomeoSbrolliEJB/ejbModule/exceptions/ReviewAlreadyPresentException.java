package exceptions;

import javax.ejb.ApplicationException;

import utils.Const;

@ApplicationException(rollback=true)
public class ReviewAlreadyPresentException extends Exception {
	private static final long serialVersionUID = Const.ExceptionVersion;
	
	public ReviewAlreadyPresentException() {
		super();
	}
	public ReviewAlreadyPresentException(String message) {
		super(message);
	}
}

