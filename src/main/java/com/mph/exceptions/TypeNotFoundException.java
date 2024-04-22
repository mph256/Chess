package com.mph.exceptions;

public class TypeNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public TypeNotFoundException() {
		System.out.println("Type not found.");
	}

}