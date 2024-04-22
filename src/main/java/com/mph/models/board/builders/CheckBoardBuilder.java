package com.mph.models.board.builders;

import com.mph.exceptions.TypeNotFoundException;
import com.mph.exceptions.ColorNotFoundException;

import com.mph.models.piece.factories.PieceFactory;

public class CheckBoardBuilder extends BoardBuilder {

	@Override
	public void addWhiteKing() {

		try {

			board.addPiece(7, 4, PieceFactory.getInstance().createPiece("white", "king"));

		} catch (TypeNotFoundException | ColorNotFoundException e) { }

	}

	@Override
	public void addWhiteQueen() { }

	@Override
	public void addWhiteKnights() { }

	@Override
	public void addWhiteBishops() { }

	@Override
	public void addWhiteRooks() { }

	@Override
	public void addWhitePawns() { }

	@Override
	public void addBlackKing() {

		try {

			board.addPiece(0, 4, PieceFactory.getInstance().createPiece("black", "king"));

		} catch (TypeNotFoundException | ColorNotFoundException e) { }

	}

	@Override
	public void addBlackQueen() {

		try {

			board.addPiece(5, 4, PieceFactory.getInstance().createPiece("black", "queen"));

		} catch (TypeNotFoundException | ColorNotFoundException e) { }

	}

	@Override
	public void addBlackKnights() { }

	@Override
	public void addBlackBishops() { }

	@Override
	public void addBlackRooks() { }

	@Override
	public void addBlackPawns() { }

}