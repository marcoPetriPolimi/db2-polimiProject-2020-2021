package exceptions;

import javax.ejb.ApplicationException;

import utils.Const;

@ApplicationException(rollback=true)
public class UserException extends Exception {
	private static final long serialVersionUID = Const.ExceptionVersion;
	
	public UserException() {
		super();
	}
	public UserException(String message) {
		super(message);
	}
}
