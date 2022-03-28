package io.moeah.hicartapi.exception;

public class SellerHasProductsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private static final String ERROR_MSG = "Seller couldn't be deleted. Cause: Products exists under the respected seller.";

	public SellerHasProductsException() {
		super(ERROR_MSG);
	}
}
