package com.mph.models.piece.strategies;

import java.util.List;
import java.util.ArrayList;

import com.mph.exceptions.InvalidMoveException;

import com.mph.models.board.Board;
import com.mph.models.board.Square;
import com.mph.models.board.Position;
import com.mph.models.piece.Piece;
import com.mph.models.piece.Color;
import com.mph.models.move.Move;

public class KingMovementStrategy implements MovementStrategy {

	@Override
	public boolean move(Board board, Move move) throws InvalidMoveException {

		if(!isValidMove(board, move) && !isValidSpecialMove(board, move))
			throw new InvalidMoveException();

		Position src = move.getSrc().getPosition();
		Position dst = move.getDst().getPosition();

		if(isValidMove(board, move)) {

			boolean didCapture = move.getDst().getPiece() != null;

			board.addPiece(dst.getX(), dst.getY(), move.getSrc().getPiece());	
			board.removePiece(src.getX(), src.getY());

			return didCapture;

		}

		// castling

		if(isValidSpecialMove(board, move)) {

			// left

			if(dst.getY() == 2) {

				board.addPiece(src.getX(), 2, move.getSrc().getPiece());	
				board.addPiece(src.getX(), 3, board.getSquare(src.getX(), 0).getPiece());		
				board.removePiece(src.getX(), 0);

			}

			// right

			if(dst.getY() == 6) {

				board.addPiece(src.getX(), 6, move.getSrc().getPiece());	
				board.addPiece(src.getX(), 5, board.getSquare(src.getX(), 7).getPiece());
				board.removePiece(src.getX(), 7);

			}

			board.removePiece(src.getX(), 4);

			return false;

		}

		return false;

	}

	@Override
	public List<Square> getAvailableDestinations(Board board, Square src) {

		List<Square> destinations = new ArrayList<>();

		destinations.addAll(getAvailableDestinationsTopLeft(board, src));
		destinations.addAll(getAvailableDestinationsTop(board, src));
		destinations.addAll(getAvailableDestinationsTopRight(board, src));
		destinations.addAll(getAvailableDestinationsLeft(board, src));
		destinations.addAll(getAvailableDestinationsRight(board, src));
		destinations.addAll(getAvailableDestinationsBottomLeft(board, src));
		destinations.addAll(getAvailableDestinationsBottom(board, src));
		destinations.addAll(getAvailableDestinationsBottomRight(board, src));
		destinations.addAll(getAvailableDestinationsCastlingLeft(board, src));
		destinations.addAll(getAvailableDestinationsCastlingRight(board, src));

		return destinations;

	}

	private boolean isValidMove(Board board, Move move) {

		Position src = move.getSrc().getPosition();
		Position dst = move.getDst().getPosition();

		return ((dst.getX() == src.getX() - 1 && (dst.getY() == src.getY() - 1 || dst.getY() == src.getY() || dst.getY() == src.getY() + 1))
			|| (dst.getX() == src.getX() && (dst.getY() == src.getY() - 1 || dst.getY() == src.getY() + 1))
			|| (dst.getX() == src.getX() + 1 && (dst.getY() == src.getY() - 1 || dst.getY() == src.getY() || dst.getY() == src.getY() + 1)));

	}

	private boolean isValidSpecialMove(Board board, Move move) {

		Position src = move.getSrc().getPosition();
		Position dst = move.getDst().getPosition();

		Color playerColor = move.getSrc().getPiece().getColor();

		if(dst.getX() == src.getX()
			&& src.getX() == (playerColor == Color.WHITE ? 7 : 0)
			&& src.getY() == 4
			&& (dst.getY() == 2 || dst.getY() == 6)) {

			// left

			if(dst.getY() == 2)
				return board.getSquare(dst.getX(), 3).getPiece() == null
					&& board.getSquare(dst.getX(), 2).getPiece() == null
					&& board.getSquare(dst.getX(), 1).getPiece() == null
					&& board.getSquare(dst.getX(), 0).getPiece() != null
					&& board.getSquare(dst.getX(), 0).getPiece().isRook()
					&& board.getSquare(dst.getX(), 0).getPiece().getColor() == playerColor
					&& !board.hasAlreadyMoved(move.getSrc().getPiece())
					&& !board.hasAlreadyMoved(board.getSquare(dst.getX(), 0).getPiece());

			// right

			if(dst.getY() == 6)
				return board.getSquare(dst.getX(), 5).getPiece() == null
					&& board.getSquare(dst.getX(), 6).getPiece() == null
					&& board.getSquare(dst.getX(), 7).getPiece() != null
					&& board.getSquare(dst.getX(), 7).getPiece().isRook()
					&& board.getSquare(dst.getX(), 0).getPiece().getColor() == playerColor
					&& !board.hasAlreadyMoved(move.getSrc().getPiece())
					&& !board.hasAlreadyMoved(board.getSquare(dst.getX(), 7).getPiece());

		}

		return false;

	}

	private List<Square> getAvailableDestinationsTopLeft(Board board, Square src) {

		List<Square> destinations = new ArrayList<>();

		if(src.getPosition().getX() - 1 >= 0 && src.getPosition().getY() - 1 >= 0) {

			Square square = board.getSquare(src.getPosition().getX() - 1, src.getPosition().getY() - 1);
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

	private List<Square> getAvailableDestinationsTop(Board board, Square src) {

		List<Square> destinations = new ArrayList<>();

		if(src.getPosition().getX() - 1 >= 0) {

			Square square = board.getSquare(src.getPosition().getX() - 1, src.getPosition().getY());
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

	private List<Square> getAvailableDestinationsTopRight(Board board, Square src) {

		List<Square> destinations = new ArrayList<>();

		if(src.getPosition().getX() - 1 >= 0 && src.getPosition().getY() + 1 < 8) {

			Square square = board.getSquare(src.getPosition().getX() - 1, src.getPosition().getY() + 1);
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

		if(src.getPosition().getY() - 1 >= 0) {

			Square square = board.getSquare(src.getPosition().getX(), src.getPosition().getY() - 1);
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

		if(src.getPosition().getY() + 1 < 8) {

			Square square = board.getSquare(src.getPosition().getX(), src.getPosition().getY() + 1);
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

	private List<Square> getAvailableDestinationsBottomLeft(Board board, Square src) {

		List<Square> destinations = new ArrayList<>();

		if(src.getPosition().getX() + 1 < 8 && src.getPosition().getY() - 1 >= 0) {

			Square square = board.getSquare(src.getPosition().getX() + 1, src.getPosition().getY() - 1);
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

		if(src.getPosition().getX() + 1 < 8) {

			Square square = board.getSquare(src.getPosition().getX() + 1, src.getPosition().getY());
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

	private List<Square> getAvailableDestinationsBottomRight(Board board, Square src) {

		List<Square> destinations = new ArrayList<>();

		if(src.getPosition().getX() + 1 < 8 && src.getPosition().getY() + 1 < 8) {

			Square square = board.getSquare(src.getPosition().getX() + 1, src.getPosition().getY() + 1);
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

	private List<Square> getAvailableDestinationsCastlingLeft(Board board, Square src) {

		List<Square> destinations = new ArrayList<>();

		Piece pieceToMove1 = src.getPiece();
		Piece pieceToMove2 = board.getSquare(src.getPosition().getX(), 0).getPiece();

		Color playerColor = pieceToMove1.getColor();

		if(src.getPosition().getX() == (playerColor == Color.WHITE ? 7 : 0) && src.getPosition().getY() == 4
			&& board.getSquare(src.getPosition().getX(), 3).getPiece() == null
			&& board.getSquare(src.getPosition().getX(), 2).getPiece() == null
			&& board.getSquare(src.getPosition().getX(), 1).getPiece() == null
			&& pieceToMove2 != null
			&& pieceToMove2.isRook()
			&& pieceToMove2.getColor() == playerColor
			&& !board.hasAlreadyMoved(pieceToMove1)
			&& !board.hasAlreadyMoved(pieceToMove2))
			destinations.add(board.getSquare(src.getPosition().getX(), 2));

		return destinations;

	}

	private List<Square> getAvailableDestinationsCastlingRight(Board board, Square src) {

		List<Square> destinations = new ArrayList<>();

		Piece pieceToMove1 = src.getPiece();
		Piece pieceToMove2 = board.getSquare(src.getPosition().getX(), 7).getPiece();

		Color playerColor = pieceToMove1.getColor();

		if(src.getPosition().getX() == (playerColor == Color.WHITE ? 7 : 0) && src.getPosition().getY() == 4
			&& board.getSquare(src.getPosition().getX(), 5).getPiece() == null
			&& board.getSquare(src.getPosition().getX(), 6).getPiece() == null
			&& pieceToMove2 != null
			&& pieceToMove2.isRook()
			&& pieceToMove2.getColor() == playerColor
			&& !board.hasAlreadyMoved(pieceToMove1)
			&& !board.hasAlreadyMoved(pieceToMove2))
			destinations.add(board.getSquare(src.getPosition().getX(), 6));

		return destinations;

	}

}