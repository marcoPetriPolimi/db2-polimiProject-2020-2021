package exceptions;

import utils.Const;

public class ProductException extends Exception {
	private static final long serialVersionUID = Const.ExceptionVersion;
	
	public ProductException() {
		super();
	}
	public ProductException(String message) {
		super(message);
	}

}