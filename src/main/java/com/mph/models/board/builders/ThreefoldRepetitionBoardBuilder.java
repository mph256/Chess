package com.mph.models.board.builders;

import com.mph.exceptions.TypeNotFoundException;
import com.mph.exceptions.ColorNotFoundException;

import com.mph.models.piece.factories.PieceFactory;

public class ThreefoldRepetitionBoardBuilder extends BoardBuilder {

	@Override
	public void addWhiteKing() {

		try {

			board.addPiece(7, 4, PieceFactory.getInstance().createPiece("white", "king"));

		} catch (TypeNotFoundException | ColorNotFoundException e) { }

	}

	@Override
	public void addWhiteQueen() {

		try {

			board.addPiece(3, 0, PieceFactory.getInstance().createPiece("white", "queen"));

		} catch (TypeNotFoundException | ColorNotFoundException e) { }

	}

	@Override
	public void addWhiteKnights() { }

	@Override
	public void addWhiteBishops() { }

	@Override
	public void addWhiteRooks() {

		try {

			board.addPiece(2, 3, PieceFactory.getInstance().createPiece("white", "rook"));

		} catch (TypeNotFoundException | ColorNotFoundException e) { }

	}

	@Override
	public void addWhitePawns() { }

	@Override
	public void addBlackKing() {

		try {

			board.addPiece(0, 1, PieceFactory.getInstance().createPiece("black", "king"));

		} catch (TypeNotFoundException | ColorNotFoundException e) { }

	}

	@Override
	public void addBlackQueen() { }

	@Override
	public void addBlackKnights() {

		try {

			board.addPiece(1, 3, PieceFactory.getInstance().createPiece("black", "knight"));

		} catch (TypeNotFoundException | ColorNotFoundException e) { }

	}

	@Override
	public void addBlackBishops() { }

	@Override
	public void addBlackRooks() { }

	@Override
	public void addBlackPawns() { 

		try {

			board.addPiece(1, 1, PieceFactory.getInstance().createPiece("black", "pawn"));

		} catch (TypeNotFoundException | ColorNotFoundException e) { }

	}

}