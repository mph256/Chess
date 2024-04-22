package com.mph.models.piece.strategies;

import java.util.List;
import java.util.ArrayList;

import com.mph.exceptions.InvalidMoveException;

import com.mph.models.board.Board;
import com.mph.models.board.Square;
import com.mph.models.board.Position;
import com.mph.models.piece.Piece;
import com.mph.models.move.Move;

public class RookMovementStrategy implements MovementStrategy {

	@Override
	public boolean move(Board board, Move move) throws InvalidMoveException {

		if(!isValidMove(board, move))
			throw new InvalidMoveException();

		boolean didCapture = move.getDst().getPiece() != null;

		Position src = move.getSrc().getPosition();
		Position dst = move.getDst().getPosition();

		board.addPiece(dst.getX(), dst.getY(), move.getSrc().getPiece());
		board.removePiece(src.getX(), src.getY());

		return didCapture;

	}

	@Override
	public List<Square> getAvailableDestinations(Board board, Square src) {

		List<Square> destinations = new ArrayList<>();

		destinations.addAll(getAvailableDestinationsTop(board, src));
		destinations.addAll(getAvailableDestinationsLeft(board, src));
		destinations.addAll(getAvailableDestinationsRight(board, src));
		destinations.addAll(getAvailableDestinationsBottom(board, src));

		return destinations;

	}

	private boolean isValidMove(Board board, Move move) {

		Position src = move.getSrc().getPosition();
		Position dst = move.getDst().getPosition();

		if(dst.getX() < src.getX() && dst.getY() == src.getY())
			return checkTop(board, move);

		if(dst.getX() == src.getX() && dst.getY() < src.getY())
			return checkLeft(board, move);

		if(dst.getX() == src.getX() && dst.getY() > src.getY())
			return checkRight(board, move);

		if(dst.getX() > src.getX() && dst.getY() == src.getY())
			return checkBottom(board, move);

		return false;

	}

	private boolean checkTop(Board board, Move move) {

		Position src = move.getSrc().getPosition();
		Position dst = move.getDst().getPosition();

		for(int i = src.getX() - 1; i > dst.getX(); i--) {

			if(board.getSquare(i, src.getY()).getPiece() != null)
				return false;

		}

		return true;

	}

	private boolean checkLeft(Board board, Move move) {

		Position src = move.getSrc().getPosition();
		Position dst = move.getDst().getPosition();

		for(int j = src.getY() - 1; j > dst.getY(); j--) {

			if(board.getSquare(src.getX(), j).getPiece() != null)
				return false;

		}

		return true;

	}

	private boolean checkRight(Board board, Move move) {

		Position src = move.getSrc().getPosition();
		Position dst = move.getDst().getPosition();

		for(int j = src.getY() + 1; j < dst.getY(); j++) {

			if(board.getSquare(src.getX(), j).getPiece() != null)
				return false;

		}

		return true;

	}

	private boolean checkBottom(Board board, Move move) {

		Position src = move.getSrc().getPosition();
		Position dst = move.getDst().getPosition();

		for(int i = src.getX() + 1; i < dst.getX(); i++) {

			if(board.getSquare(i, src.getY()).getPiece() != null)
				return false;

		}

		return true;

	}

	private List<Square> getAvailableDestinationsTop(Board board, Square src) {

		List<Square> destinations = new ArrayList<>();

		for(int i = src.getPosition().getX() - 1; i >= 0; i--) {

			Square square = board.getSquare(i, src.getPosition().getY());
			Piece piece = square.getPiece();

			if(piece != null) {

				if(piece.getColor() != src.getPiece().getColor())
					destinations.add(square);

				return destinations;

			}

			destinations.add(square);

		}

		return destinations;

	}

	private List<Square> getAvailableDestinationsLeft(Board board, Square src) {

		List<Square> destinations = new ArrayList<>();

		for(int j = src.getPosition().getY() - 1; j >= 0; j--) {

			Square square = board.getSquare(src.getPosition().getX(), j);
			Piece piece = square.getPiece();

			if(piece != null) {

				if(piece.getColor() != src.getPiece().getColor())
					destinations.add(square);

				return destinations;

			}

			destinations.add(square);

		}

		return destinations;

	}

	private List<Square> getAvailableDestinationsRight(Board board, Square src) {

		List<Square> destinations = new ArrayList<>();

		for(int j = src.getPosition().getY() + 1; j < 8; j++) {

			Square square = board.getSquare(src.getPosition().getX(), j);
			Piece piece = square.getPiece();

			if(piece != null) {

				if(piece.getColor() != src.getPiece().getColor())
					destinations.add(square);

				return destinations;

			}

			destinations.add(square);

		}

		return destinations;

	}

	private List<Square> getAvailableDestinationsBottom(Board board, Square src) {

		List<Square> destinations = new ArrayList<>();

		for(int i = src.getPosition().getX() + 1; i < 8; i++) {

			Square square = board.getSquare(i, src.getPosition().getY());
			Piece piece = square.getPiece();

			if(piece != null) {

				if(piece.getColor() != src.getPiece().getColor())
					destinations.add(square);

				return destinations;

			}

			destinations.add(square);

		}

		return destinations;

	}

}