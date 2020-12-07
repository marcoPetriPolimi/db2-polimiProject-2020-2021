package exceptions;

import utils.Const;

public class UserCredentialsException extends UserException {
	private static final long serialVersionUID = Const.ExceptionVersion;
	
	public UserCredentialsException() {
		super();
	}
	public UserCredentialsException(String message) {
		super(message);
	}
}
