package com.mph.exceptions;

public class InvalidBoardException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidBoardException() {
		System.out.println("Invalid board.");
	}

}