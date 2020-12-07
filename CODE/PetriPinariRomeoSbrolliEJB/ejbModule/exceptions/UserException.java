package exceptions;

import utils.Const;

public class UserException extends Exception {
	private static final long serialVersionUID = Const.ExceptionVersion;
	
	public UserException() {
		super();
	}
	public UserException(String message) {
		super(message);
	}
}
