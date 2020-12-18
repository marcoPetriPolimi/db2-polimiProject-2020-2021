package exceptions;

import javax.ejb.ApplicationException;

import utils.Const;

@ApplicationException(rollback=true)
public class UserAlreadyPresentNicknameException extends UserException {
	private static final long serialVersionUID = Const.ExceptionVersion;
	
	public UserAlreadyPresentNicknameException() {
		super();
	}
	public UserAlreadyPresentNicknameException(String message) {
		super(message);
	}
}
