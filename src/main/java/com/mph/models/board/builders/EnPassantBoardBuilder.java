package com.mph.models.board.builders;

import com.mph.exceptions.TypeNotFoundException;
import com.mph.exceptions.ColorNotFoundException;

import com.mph.models.piece.factories.PieceFactory;

public class EnPassantBoardBuilder extends BoardBuilder {

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
	public void addWhitePawns() {

		try {

			board.addPiece(6, 0, PieceFactory.getInstance().createPiece("white", "pawn"));

		} catch (TypeNotFoundException | ColorNotFoundException e) { }

	}

	@Override
	public void addBlackKing() {

		try {

			board.addPiece(0, 4, PieceFactory.getInstance().createPiece("black", "king"));

		} catch (TypeNotFoundException | ColorNotFoundException e) { }

	}

	@Override
	public void addBlackQueen() {}

	@Override
	public void addBlackKnights() { }

	@Override
	public void addBlackBishops() { }

	@Override
	public void addBlackRooks() { }

	@Override
	public void addBlackPawns() {

		try {

			board.addPiece(4, 1, PieceFactory.getInstance().createPiece("black", "pawn"));

		} catch (TypeNotFoundException | ColorNotFoundException e) { }

	}

}