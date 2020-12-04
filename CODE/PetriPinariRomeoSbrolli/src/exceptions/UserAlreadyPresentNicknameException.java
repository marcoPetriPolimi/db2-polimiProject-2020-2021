package exceptions;

import utils.Const;

public class UserAlreadyPresentNicknameException extends UserException {
	private static final long serialVersionUID = Const.ExceptionVersion;
	
	public UserAlreadyPresentNicknameException() {
		super();
	}
	public UserAlreadyPresentNicknameException(String message) {
		super(message);
	}
}
