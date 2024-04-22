package com.mph.models.game.states;

import com.mph.exceptions.InvalidMoveException;

import com.mph.models.game.Game;
import com.mph.models.board.Board;
import com.mph.models.piece.Piece;
import com.mph.models.move.Move;

public abstract class State {

	public void movePiece(Game game, Move move) throws InvalidMoveException {

		game.preCheckMove(move);

		Piece pieceToMove = move.getSrc().getPiece();
		Piece pieceToCapture = move.getDst().getPiece();

		Board board = game.getBoard();

		Board boardSave = board.clone();
		Move moveSave = move.clone();

		boolean didCapture = board.movePiece(move);

		try {

			game.postCheckMove(move);

			game.addBoardToHistory(boardSave);
			board.addMoveToHistory(moveSave);

			if(pieceToMove.isPawn() || didCapture)
				game.resetDrawCounter();
			else
				game.decrementDrawCounter();

		} catch(InvalidMoveException e) {

			game.rollback(move.getSrc().getPosition(), move.getDst().getPosition(), pieceToMove, pieceToCapture);

			throw e;

		}

		game.updateState();

		if(!game.isOver())
			game.nextTurn();

	}

}