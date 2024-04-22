package com.mph.models.board.builders;

import java.util.Arrays;

import com.mph.exceptions.TypeNotFoundException;
import com.mph.exceptions.ColorNotFoundException;

import com.mph.models.piece.factories.PieceFactory;

public class DefaultBoardBuilder extends BoardBuilder {

	@Override
	public void addWhiteKing() {

		try {

			board.addPiece(7, 4, PieceFactory.getInstance().createPiece("white", "king"));

		} catch (TypeNotFoundException | ColorNotFoundException e) { }

	}

	@Override
	public void addWhiteQueen() {

		try {

			board.addPiece(7, 3, PieceFactory.getInstance().createPiece("white", "queen"));

		} catch (TypeNotFoundException | ColorNotFoundException e) { }

	}

	@Override
	public void addWhiteKnights() {

		try {

			board.addPiece(7, 1, PieceFactory.getInstance().createPiece("white", "knight"));
			board.addPiece(7, 6, PieceFactory.getInstance().createPiece("white", "knight"));

		} catch (TypeNotFoundException | ColorNotFoundException e) { }

	}

	@Override
	public void addWhiteBishops() {

		try {

			board.addPiece(7, 2, PieceFactory.getInstance().createPiece("white", "bishop"));
			board.addPiece(7, 5, PieceFactory.getInstance().createPiece("white", "bishop"));

		} catch (TypeNotFoundException | ColorNotFoundException e) { }

	}

	@Override
	public void addWhiteRooks() {

		try {

			board.addPiece(7, 0, PieceFactory.getInstance().createPiece("white", "rook"));
			board.addPiece(7, 7, PieceFactory.getInstance().createPiece("white", "rook"));

		} catch (TypeNotFoundException | ColorNotFoundException e) { }

	}

	@Override
	public void addWhitePawns() {

		Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7).forEach(j -> {

			try {

				board.addPiece(6, j, PieceFactory.getInstance().createPiece("white", "pawn"));

			} catch (TypeNotFoundException | ColorNotFoundException e) { }

		});

	}

	@Override
	public void addBlackKing() {

		try {

			board.addPiece(0, 4, PieceFactory.getInstance().createPiece("black", "king"));

		} catch (TypeNotFoundException | ColorNotFoundException e) { }

	}

	@Override
	public void addBlackQueen() {

		try {

			board.addPiece(0, 3, PieceFactory.getInstance().createPiece("black", "queen"));

		} catch (TypeNotFoundException | ColorNotFoundException e) { }

	}

	@Override
	public void addBlackKnights() {

		try {

			board.addPiece(0, 1, PieceFactory.getInstance().createPiece("black", "knight"));
			board.addPiece(0, 6, PieceFactory.getInstance().createPiece("black", "knight"));

		} catch (TypeNotFoundException | ColorNotFoundException e) { }

	}

	@Override
	public void addBlackBishops() {

		try {

			board.addPiece(0, 2, PieceFactory.getInstance().createPiece("black", "bishop"));
			board.addPiece(0, 5, PieceFactory.getInstance().createPiece("black", "bishop"));

		} catch (TypeNotFoundException | ColorNotFoundException e) { }

	}

	@Override
	public void addBlackRooks() {

		try {

			board.addPiece(0, 0, PieceFactory.getInstance().createPiece("black", "rook"));
			board.addPiece(0, 7, PieceFactory.getInstance().createPiece("black", "rook"));

		} catch (TypeNotFoundException | ColorNotFoundException e) { }

	}

	@Override
	public void addBlackPawns() {

		Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7).forEach(j -> {

			try {

				board.addPiece(1, j, PieceFactory.getInstance().createPiece("black", "pawn"));

			} catch (TypeNotFoundException | ColorNotFoundException e) { }

		});

	}

}