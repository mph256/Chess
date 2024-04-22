package com.mph.exceptions;

public class ColorNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public ColorNotFoundException() {
		System.out.println("Color not found.");
	}

}