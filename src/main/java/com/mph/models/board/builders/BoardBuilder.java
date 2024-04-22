package com.mph.models.board.builders;

import com.mph.exceptions.InvalidBoardException;

import com.mph.models.board.Board;
import com.mph.models.piece.Color;

public abstract class BoardBuilder {

	Board board;

	public abstract void addWhiteKing();

	public abstract void addWhiteQueen();

	public abstract void addWhiteKnights();

	public abstract void addWhiteBishops();

	public abstract void addWhiteRooks();

	public abstract void addWhitePawns();

	public abstract void addBlackKing();

	public abstract void addBlackQueen();

	public abstract void addBlackKnights();

	public abstract void addBlackBishops();

	public abstract void addBlackRooks();

	public abstract void addBlackPawns();

	public final void createBoard() {
		board = new Board(8, 8);
	}

	public final Board getBoard() throws InvalidBoardException {

		if(!isValidBoard())
			throw new InvalidBoardException();

		return board;

	}

	private boolean isValidBoard() {

		return board != null
			&& board.getKingPosition(Color.WHITE) != null 
			&& board.getKingPosition(Color.BLACK) != null;

	}

}