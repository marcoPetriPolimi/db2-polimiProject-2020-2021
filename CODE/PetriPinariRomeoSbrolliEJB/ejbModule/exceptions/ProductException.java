package exceptions;

import javax.ejb.ApplicationException;

import utils.Const;

@ApplicationException(rollback=true)
public class ProductException extends Exception {
	private static final long serialVersionUID = Const.ExceptionVersion;
	
	public ProductException() {
		super();
	}
	public ProductException(String message) {
		super(message);
	}

}