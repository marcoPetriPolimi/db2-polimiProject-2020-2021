package exceptions;

import javax.ejb.ApplicationException;

import utils.Const;

@ApplicationException(rollback=true)
public class UserCredentialsException extends UserException {
	private static final long serialVersionUID = Const.ExceptionVersion;
	
	public UserCredentialsException() {
		super();
	}
	public UserCredentialsException(String message) {
		super(message);
	}
}
