package com.mph.models.game;

import java.util.List;
import java.util.ArrayList;

import com.mph.exceptions.InvalidBoardException;
import com.mph.exceptions.InvalidMoveException;

import com.mph.models.board.Board;
import com.mph.models.board.Square;
import com.mph.models.board.Position;
import com.mph.models.piece.Piece;
import com.mph.models.piece.Color;
import com.mph.models.move.Move;

import com.mph.models.board.builders.BoardBuilder;

import com.mph.models.game.states.State;
import com.mph.models.game.states.DefaultState;
import com.mph.models.game.states.CheckState;
import com.mph.models.game.states.CheckmateState;
import com.mph.models.game.states.DrawState;

public class Game {

	private State state;

	private int turn;

	private int drawCounter;

	private List<Board> boardHistory;

	private BoardBuilder boardBuilder;

	public Game(BoardBuilder boardBuilder) throws InvalidBoardException {

		state = new DefaultState();
		drawCounter = 50;
		boardHistory = new ArrayList<>();

		this.boardBuilder = boardBuilder;

		buildBoard();

	}

	public void movePiece(Move move) throws InvalidMoveException {
		state.movePiece(this, move);
	}

	public void preCheckMove(Move move) throws InvalidMoveException {

		Piece pieceToMove = move.getSrc().getPiece();
		Piece pieceToCapture = move.getDst().getPiece();

		if(pieceToMove == null
			|| pieceToMove.isWhite() ? (turn % 2 == 0) : (turn % 2 != 0)
			|| (pieceToCapture != null && pieceToCapture.getColor() == pieceToMove.getColor()))
			throw new InvalidMoveException();

	}

	public void postCheckMove(Move move) throws InvalidMoveException {

		Color playerColor = move.getDst().getPiece().getColor();

		if(isCheck(getBoard().getKingPosition(playerColor), playerColor))
			throw new InvalidMoveException();

	}

	public void rollback(Position src, Position dst, Piece pieceToMove, Piece pieceToCapture) {

		Board board = getBoard();

		board.addPiece(src.getX(), src.getY(), pieceToMove);
		board.addPiece(dst.getX(), dst.getY(), pieceToCapture);

	}

	public void updateState() {

		Board board = getBoard();

		Color enemyColor = (turn % 2 != 0) ? Color.BLACK : Color.WHITE;

		boolean isCheck = isCheck(board.getKingPosition(enemyColor), enemyColor);
		boolean isCheckmate = isCheck && isBlockedKing(enemyColor);

		if(isCheckmate) {

			state = new CheckmateState();

			return;

		}

		if(isStalemate() || is50thMoveWithoutCapturesOrPawnMoves() || isThreefoldRepetition()) {

			state = new DrawState();

			return;

		}

		if(isCheck)
			state = new CheckState();
		else if(state instanceof CheckState)
			state = new DefaultState();

	}

	public boolean isOver() {
		return state instanceof CheckmateState || state instanceof DrawState;
	}

	public void decrementDrawCounter() {
		drawCounter--;
	}

	public void resetDrawCounter() {
		drawCounter = 50;
	}

	public void addBoardToHistory(Board board) {
		boardHistory.add(board);
	}

	public void nextTurn() {
		turn++;
	}

	public State getState() {
		return state;
	}

	public int getTurn() {
		return turn;
	}

	public Board getBoard() {

		try {

			return boardBuilder.getBoard();

		} catch (InvalidBoardException e) {

			return null;

		}

	}

	private void buildBoard() throws InvalidBoardException {

		boardBuilder.createBoard();

		boardBuilder.addWhiteKing();
		boardBuilder.addWhiteQueen();
		boardBuilder.addWhiteBishops();
		boardBuilder.addWhiteKnights();
		boardBuilder.addWhiteRooks();
		boardBuilder.addWhitePawns();

		boardBuilder.addBlackKing();
		boardBuilder.addBlackQueen();
		boardBuilder.addBlackBishops();
		boardBuilder.addBlackKnights();
		boardBuilder.addBlackRooks();
		boardBuilder.addBlackPawns();

		checkBoard();

		updateState();

		turn++;

	}

	private void checkBoard() throws InvalidBoardException {
		boardBuilder.getBoard();
	}

	private boolean isStalemate() {

		Board board = getBoard();

		Color enemyColor = (turn % 2 != 0) ? Color.BLACK : Color.WHITE;

		if(!isCheck(board.getKingPosition(enemyColor), enemyColor)) {

			for(int i = 0; i < 8; i++) {

				for(int j = 0; j < 8; j++) {

					Square square = board.getSquare(i, j);
					Piece piece = board.getSquare(i, j).getPiece();

					if(piece != null && piece.getColor() == enemyColor) {

						if(!piece.getAvailableDestinations(board, square).isEmpty()) {

							if(piece.isKing()) {

								if(!isBlockedKing(enemyColor))
									return false;

							} else
								return false;

						}

					}

				}

			}

			return true;

		}

		return false;

	}

	private boolean is50thMoveWithoutCapturesOrPawnMoves() {
		return drawCounter == 0;
	}

	private boolean isThreefoldRepetition() {

		Board board = getBoard();

		boolean isDraw = (
			boardHistory
				.stream()
				.reduce(1, (accumulator, element) -> board.equals(element) ? accumulator + 1 : accumulator, Integer::sum)
		) >= 3;

		return isDraw;

	}

	private boolean isCheck(Position position, Color color) {

		Board board = getBoard();

		// knights

		if((position.getX() - 2 >= 0 && position.getY() - 1 >= 0 && board.getSquare(position.getX() - 2, position.getY() - 1).getPiece() != null && board.getSquare(position.getX() - 2, position.getY() - 1).getPiece().getColor() != color && board.getSquare(position.getX() - 2, position.getY() - 1).getPiece().isKnight())
			|| (position.getX() - 2 >= 0 && position.getY() + 1 < 8 && board.getSquare(position.getX() - 2, position.getY() + 1).getPiece() != null && board.getSquare(position.getX() - 2, position.getY() + 1).getPiece().getColor() != color && board.getSquare(position.getX() - 2, position.getY() + 1).getPiece().isKnight())
			|| (position.getX() - 1 >= 0 && position.getY() - 2 >= 0 && board.getSquare(position.getX() - 1, position.getY() - 2).getPiece() != null && board.getSquare(position.getX() - 1, position.getY() - 2).getPiece().getColor() != color && board.getSquare(position.getX() - 1, position.getY() - 2).getPiece().isKnight())
			|| (position.getX() - 1 >= 0 && position.getY() + 2 < 8 && board.getSquare(position.getX() - 1, position.getY() + 2).getPiece() != null && board.getSquare(position.getX() - 1, position.getY() + 2).getPiece().getColor() != color && board.getSquare(position.getX() - 1, position.getY() + 2).getPiece().isKnight())			
			|| (position.getX() + 1 < 8 && position.getY() - 2 >= 0 && board.getSquare(position.getX() + 1, position.getY() - 2).getPiece() != null && board.getSquare(position.getX() + 1, position.getY() - 2).getPiece().getColor() != color && board.getSquare(position.getX() + 1, position.getY() - 2).getPiece().isKnight())
			|| (position.getX() + 1 < 8 && position.getY() + 2 < 8 && board.getSquare(position.getX() + 1, position.getY() + 2).getPiece() != null && board.getSquare(position.getX() + 1, position.getY() + 2).getPiece().getColor() != color && board.getSquare(position.getX() + 1, position.getY() + 2).getPiece().isKnight())
			|| (position.getX() + 2 < 8 && position.getY() - 1 >= 0 && board.getSquare(position.getX() + 2, position.getY() - 1).getPiece() != null && board.getSquare(position.getX() + 2, position.getY() - 1).getPiece().getColor() != color && board.getSquare(position.getX() + 2, position.getY() - 1).getPiece().isKnight())
			|| (position.getX() + 2 < 8 && position.getY() + 1 < 8 && board.getSquare(position.getX() + 2, position.getY() + 1).getPiece() != null && board.getSquare(position.getX() + 2, position.getY() + 1).getPiece().getColor() != color && board.getSquare(position.getX() + 2, position.getY() + 1).getPiece().isKnight()))
			return true;

		// rooks and queen

		// up

		for(int i = position.getX() - 1; i >= 0; i--) {

			Piece piece = board.getSquare(i, position.getY()).getPiece();

			if(piece == null)
				continue;
			else {

				if(piece.getColor() != color && (piece.isRook() || piece.isQueen()))
					return true;

				break;

			}

		}

		// left

		for(int j = position.getY() - 1; j >= 0; j--) {

			Piece piece = board.getSquare(position.getX(), j).getPiece();

			if(piece == null)
				continue;
			else {

				if(piece.getColor() != color && (piece.isRook() || piece.isQueen()))
					return true;

				break;

			}

		}

		// right

		for(int j = position.getY() + 1; j < 8; j++) {

			Piece piece = board.getSquare(position.getX(), j).getPiece();

			if(piece == null)
				continue;
			else {

				if(piece.getColor() != color && (piece.isRook() || piece.isQueen()))
					return true;

				break;

			}

		}

		// down

		for(int i = position.getX() + 1; i < 8; i++) {

			Piece piece = board.getSquare(i, position.getY()).getPiece();

			if(piece == null)
				continue;
			else {

				if(piece.getColor() != color && (piece.isRook() || piece.isQueen()))
					return true;

				break;

			}

		}

		// bishops and queen

		// up left

		if(position.getY() > 0) {

			int j = position.getY() - 1;

			for(int i = position.getX() - 1; i >= 0; i--) {

				Piece piece = board.getSquare(i, j).getPiece();

				if(piece == null) {

					if(j == 0)
						break;

					j--;

				} else {

					if(piece.getColor() != color && (piece.isBishop() || piece.isQueen()))
						return true;

					break;

				}

			}

		}

		// up right

		if(position.getY() < 6) {

			int j = position.getY() + 1;

			for(int i = position.getX() - 1; i >= 0; i--) {

				Piece piece = board.getSquare(i, j).getPiece();

				if(piece == null) {

					if(j == 7)
						break;

					j++;

				} else {

					if(piece.getColor() != color && (piece.isBishop() || piece.isQueen()))
						return true;

					break;

				}

			}

		}

		// down left
	
		if(position.getY() > 0) {

			int j = position.getY() - 1;

			for(int i = position.getX() + 1; i < 8; i++) {

				Piece piece = board.getSquare(i, j).getPiece();

				if(piece == null) {

					if(j == 0)
						break;

					j--;

				} else {

					if(piece.getColor() != color && (piece.isBishop() || piece.isQueen()))
						return true;

					break;

				}

			}
		
		}

		// down right

		if(position.getY() < 6) {

			int j = position.getY() + 1;

			for(int i = position.getX() + 1; i < 8; i++) {

				Piece piece = board.getSquare(i, j).getPiece();

				if(piece == null) {

					if(j == 7)
						break;

					j++;

				} else {

					if(piece.getColor() != color && (piece.isBishop() || piece.isQueen()))
						return true;

					break;

				}

			}
	
		}

		// king

		if((position.getX() - 1 >= 0 && position.getY() - 1 >= 0 && board.getSquare(position.getX() - 1, position.getY() - 1).getPiece() != null && board.getSquare(position.getX() - 1, position.getY() - 1).getPiece().getColor() != color && board.getSquare(position.getX() - 1, position.getY() - 1).getPiece().isKing())
			|| (position.getX() - 1 >= 0 && board.getSquare(position.getX() - 1, position.getY()).getPiece() != null && board.getSquare(position.getX() - 1, position.getY()).getPiece().getColor() != color && board.getSquare(position.getX() - 1, position.getY()).getPiece().isKing())
			|| (position.getX() - 1 >= 0 && position.getY() + 1 < 8 && board.getSquare(position.getX() - 1, position.getY() + 1).getPiece() != null && board.getSquare(position.getX() - 1, position.getY() + 1).getPiece().getColor() != color && board.getSquare(position.getX() - 1, position.getY() + 1).getPiece().isKing())
			|| (position.getY() - 1 >= 0 && board.getSquare(position.getX(), position.getY() - 1).getPiece() != null && board.getSquare(position.getX(), position.getY() - 1).getPiece().getColor() != color && board.getSquare(position.getX(), position.getY() - 1).getPiece().isKing())
			|| (position.getY() + 1 < 8 && board.getSquare(position.getX(), position.getY() + 1).getPiece() != null && board.getSquare(position.getX(), position.getY() + 1).getPiece().getColor() != color && board.getSquare(position.getX(), position.getY() + 1).getPiece().isKing())
			|| (position.getX() + 1 < 8 && position.getY() - 1 >= 0 && board.getSquare(position.getX() + 1, position.getY() - 1).getPiece() != null && board.getSquare(position.getX() + 1, position.getY() - 1).getPiece().getColor() != color && board.getSquare(position.getX() + 1, position.getY() - 1).getPiece().isKing())
			|| (position.getX() + 1 < 8 && board.getSquare(position.getX() + 1, position.getY()).getPiece() != null && board.getSquare(position.getX() + 1, position.getY()).getPiece().getColor() != color && board.getSquare(position.getX() + 1, position.getY()).getPiece().isKing())
			|| (position.getX() + 1 < 8 && position.getY() + 1 < 8 && board.getSquare(position.getX() + 1, position.getY() + 1).getPiece() != null && board.getSquare(position.getX() + 1, position.getY() + 1).getPiece().getColor() != color && board.getSquare(position.getX() + 1, position.getY() + 1).getPiece().isKing()))
			return true;

		// pawns

		if(color == Color.WHITE)
			if((position.getX() + 1 < 8 && position.getY() - 1 >= 0 && board.getSquare(position.getX() + 1, position.getY() - 1).getPiece() != null && board.getSquare(position.getX() + 1, position.getY() - 1).getPiece().getColor() != color && board.getSquare(position.getX() + 1, position.getY() - 1).getPiece().isPawn())
				|| (position.getX() + 1 < 8 && position.getY() + 1 < 8 && board.getSquare(position.getX() + 1, position.getY() + 1).getPiece() != null && board.getSquare(position.getX() + 1, position.getY() + 1).getPiece().getColor() != color && board.getSquare(position.getX() + 1, position.getY() + 1).getPiece().isPawn()))
				return true;
		else
			if((position.getX() - 1 >= 0 && position.getY() - 1 >= 0 && board.getSquare(position.getX() - 1, position.getY() - 1).getPiece() != null && board.getSquare(position.getX() - 1, position.getY() - 1).getPiece().getColor() != color && board.getSquare(position.getX() - 1, position.getY() - 1).getPiece().isPawn())
				|| (position.getX() - 1 >= 0 && position.getY() + 1 < 8 && board.getSquare(position.getX() - 1, position.getY() + 1).getPiece() != null && board.getSquare(position.getX() - 1, position.getY() + 1).getPiece().getColor() != color && board.getSquare(position.getX() - 1, position.getY() + 1).getPiece().isPawn()))
				return true;

		return false;

	}

	private List<Square> getAvailableDestinationsForKing(Color color) {

		Board board = getBoard();

		Position kingPosition = board.getKingPosition(color);
		Square src = board.getSquare(kingPosition.getX(), kingPosition.getY());

		List<Square> destinations = new ArrayList<>();

		for(Square dst: src.getPiece().getAvailableDestinations(board, src)) {

			Piece pieceToMove = src.getPiece();
			Piece pieceToCapture = dst.getPiece();

			Move move = new Move(src, dst);

			try {

				board.movePiece(move);

				postCheckMove(move);

				destinations.add(dst);

			} catch(InvalidMoveException e) { 
			} finally {
				rollback(src.getPosition(), dst.getPosition(), pieceToMove, pieceToCapture);
			}

		}

		return destinations;

	}

	private boolean isBlockedKing(Color color) {
		return getAvailableDestinationsForKing(color).isEmpty();
	}

}