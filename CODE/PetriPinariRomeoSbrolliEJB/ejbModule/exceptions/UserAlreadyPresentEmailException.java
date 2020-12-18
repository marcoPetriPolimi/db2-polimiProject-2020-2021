package exceptions;

import javax.ejb.ApplicationException;

import utils.Const;

@ApplicationException(rollback=true)
public class UserAlreadyPresentEmailException extends UserException {
	private static final long serialVersionUID = Const.ExceptionVersion;
	
	public UserAlreadyPresentEmailException() {
		super();
	}
	public UserAlreadyPresentEmailException(String message) {
		super(message);
	}
}
