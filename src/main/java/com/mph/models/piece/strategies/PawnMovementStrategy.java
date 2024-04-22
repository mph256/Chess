package com.mph.models.piece.strategies;

import java.util.Scanner;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import com.mph.exceptions.InvalidMoveException;

import com.mph.models.board.Board;
import com.mph.models.board.Square;
import com.mph.models.board.Position;
import com.mph.models.piece.Piece;
import com.mph.models.piece.Color;
import com.mph.models.move.Move;

public class PawnMovementStrategy implements MovementStrategy {

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

			if(isValidPromotion(move))
				promotePiece(move.getDst().getPiece());

			return didCapture;

		}

		// en passant

		if(isValidSpecialMove(board, move)) {

			board.addPiece(dst.getX(), dst.getY(), move.getSrc().getPiece());	
			board.removePiece(src.getX(), src.getY());
			board.removePiece(src.getX(), dst.getY());

			return true;

		}

		return false;

	}

	@Override
	public List<Square> getAvailableDestinations(Board board, Square src) {

		List<Square> destinations = new ArrayList<>();	

		destinations.addAll(getAvailableDestinationsTopLeft(board, src));
		destinations.addAll(getAvailableDestinationsTop(board, src));
		destinations.addAll(getAvailableDestinationsTopRight(board, src));
		destinations.addAll(getAvailableDestinationsBottomLeft(board, src));
		destinations.addAll(getAvailableDestinationsBottom(board, src));
		destinations.addAll(getAvailableDestinationsBottomRight(board, src));

		return destinations;

	}

	private boolean isValidMove(Board board, Move move) {

		Position src = move.getSrc().getPosition();
		Position dst = move.getDst().getPosition();

		if(move.getDst().getPiece() == null) {

			if(dst.getY() != src.getY())
				return false;

			if(dst.getX() == (move.getSrc().getPiece().isWhite() ? src.getX() - 1 : src.getX() + 1))
				return true;

			return isValidFirstMove(board, move);

		}

		return isValidCapture(move);

	}

	private boolean isValidSpecialMove(Board board, Move move) {
		return isValidEnPassant(board, move);
	}

	private boolean isValidFirstMove(Board board, Move move) {

		Position src = move.getSrc().getPosition();
		Position dst = move.getDst().getPosition();

		Color playerColor = move.getSrc().getPiece().getColor();

		return (playerColor == Color.WHITE && src.getX() == 6 && (dst.getX() == 5 || (dst.getX() == 4 && board.getSquare(5, src.getY()).getPiece() == null)))
			|| (playerColor == Color.BLACK && src.getX() == 1 && (dst.getX() == 2 || (dst.getX() == 3) && board.getSquare(2, src.getY()).getPiece() == null));

	}

	private boolean isValidCapture(Move move) {

		Position src = move.getSrc().getPosition();
		Position dst = move.getDst().getPosition();

		return move.getDst().getPiece() != null
			&& dst.getX() == (move.getSrc().getPiece().isWhite() ? src.getX() - 1 : src.getX() + 1)
			&& (dst.getY() == src.getY() - 1 || dst.getY() == src.getY() + 1);

	}

	private boolean isValidPromotion(Move move) {
		return move.getDst().getPosition().getX() == (move.getDst().getPiece().isWhite() ? 0 : 7);
	}

	private boolean isValidEnPassant(Board board, Move move) {

		Position src = move.getSrc().getPosition();
		Position dst = move.getDst().getPosition();

		return move.getDst().getPiece() == null
			&& dst.getX() == (move.getSrc().getPiece().isWhite() ? src.getX() - 1 : src.getX() + 1)
			&& (dst.getY() == src.getY() - 1 || dst.getY() == src.getY() + 1)
			&& checkPreviousMove(board, move);

	}

	private boolean checkPreviousMove(Board board, Move move) {

		Position src = move.getSrc().getPosition();
		Position dst = move.getDst().getPosition();

		Move previousMove = board.getLastMove();

		if(previousMove == null)
			return false;

		Piece previousPiece = previousMove.getSrc().getPiece();

		Position previousSrc = previousMove.getSrc().getPosition();
		Position previousDst = previousMove.getDst().getPosition();

		return previousPiece.equals(board.getSquare(src.getX(), dst.getY()).getPiece())
			&& previousPiece.isPawn()
			&& previousDst.getX() == (previousPiece.isWhite() ? previousSrc.getX() - 2 : previousSrc.getX() + 2);

	}

	private void promotePiece(Piece piece) {

		Scanner scanner = new Scanner(System.in);

		List<String> validInputs = Arrays.asList("1", "2", "3", "4");

		String input = "";

		do {

			System.out.println("\n");

			System.out.println("Which piece do you want to get ?");

			System.out.println("");

			System.out.println("1: Queen");
			System.out.println("2: Bishop");
			System.out.println("3: Knight");
			System.out.println("4: Rook");

			System.out.println("");

			input = scanner.nextLine();

		} while(!validInputs.contains(input));

		switch(input) {

			case "1" -> piece.setMovementStrategy(new QueenMovementStrategy());
			case "2" -> piece.setMovementStrategy(new BishopMovementStrategy());
			case "3" -> piece.setMovementStrategy(new KnightMovementStrategy());
			case "4" -> piece.setMovementStrategy(new RookMovementStrategy());

		}

	}

	private List<Square> getAvailableDestinationsTopLeft(Board board, Square src) {

		List<Square> destinations = new ArrayList<>();

		if(src.getPosition().getX() - 1 >= 0 && src.getPosition().getY() - 1 >= 0) {

			Square square = board.getSquare(src.getPosition().getX() - 1, src.getPosition().getY() - 1);
			Piece pieceToCapture = square.getPiece();

			if(pieceToCapture != null && pieceToCapture.getColor() != src.getPiece().getColor())
				destinations.add(square);

			// en passant

			pieceToCapture = board.getSquare(src.getPosition().getX(), src.getPosition().getY() - 1).getPiece();

			Move previousMove = board.getLastMove();

			if(board.getSquare(src.getPosition().getX() - 1, src.getPosition().getY() - 1).getPiece() == null
				&& pieceToCapture != null
				&& pieceToCapture.isPawn()
				&& pieceToCapture.getColor() != src.getPiece().getColor()
				&& previousMove != null
				&& previousMove.getSrc().getPiece().equals(pieceToCapture)
				&& previousMove.getDst().getPosition().getX() == previousMove.getSrc().getPosition().getX() + 2)
			 	destinations.add(square);

		}

		return destinations;

	}

	private List<Square> getAvailableDestinationsTop(Board board, Square src) {

		List<Square> destinations = new ArrayList<>();

		if(src.getPosition().getX() - 1 >= 0) {

			Square square = board.getSquare(src.getPosition().getX() - 1, src.getPosition().getY());
			Piece piece = square.getPiece();

			if(piece == null) {

				destinations.add(square);

				if(src.getPosition().getX() == 6) {

					square = board.getSquare(4, src.getPosition().getY());

					if(square.getPiece() == null)
						destinations.add(square);

				}

			}

		}

		return destinations;

	}

	private List<Square> getAvailableDestinationsTopRight(Board board, Square src) {

		List<Square> destinations = new ArrayList<>();

		if(src.getPosition().getX() - 1 >= 0 && src.getPosition().getY() + 1 < 8) {

			Square square = board.getSquare(src.getPosition().getX() - 1, src.getPosition().getY() + 1);
			Piece pieceToCapture = square.getPiece();

			if(pieceToCapture != null && pieceToCapture.getColor() != src.getPiece().getColor())
				destinations.add(square);

			// en passant

			pieceToCapture = board.getSquare(src.getPosition().getX(), src.getPosition().getY() + 1).getPiece();

			Move previousMove = board.getLastMove();

			if(board.getSquare(src.getPosition().getX() - 1, src.getPosition().getY() + 1).getPiece() == null
				&& pieceToCapture != null
				&& pieceToCapture.isPawn()
				&& pieceToCapture.getColor() != src.getPiece().getColor()
				&& previousMove != null
				&& previousMove.getSrc().getPiece().equals(pieceToCapture)
				&& previousMove.getDst().getPosition().getX() == previousMove.getSrc().getPosition().getX() + 2)
			 	destinations.add(square);

		}

		return destinations;

	}

	private List<Square> getAvailableDestinationsBottomLeft(Board board, Square src) {

		List<Square> destinations = new ArrayList<>();

		if(src.getPosition().getX() + 1 < 8 && src.getPosition().getY() - 1 >= 0) {

			Square square = board.getSquare(src.getPosition().getX() + 1, src.getPosition().getY() - 1);
			Piece pieceToCapture = square.getPiece();

			if(pieceToCapture != null && pieceToCapture.getColor() != src.getPiece().getColor())
				destinations.add(square);

			// en passant

			pieceToCapture = board.getSquare(src.getPosition().getX(), src.getPosition().getY() - 1).getPiece();

			Move previousMove = board.getLastMove();

			if(board.getSquare(src.getPosition().getX() + 1, src.getPosition().getY() - 1).getPiece() == null
				&& pieceToCapture != null
				&& pieceToCapture.isPawn()
				&& pieceToCapture.getColor() != src.getPiece().getColor()
				&& previousMove != null
				&& previousMove.getSrc().getPiece().equals(pieceToCapture)
				&& previousMove.getDst().getPosition().getX() == previousMove.getSrc().getPosition().getX() - 2)
			 	destinations.add(square);

		}

		return destinations;

	}

	private List<Square> getAvailableDestinationsBottom(Board board, Square src) {

		List<Square> destinations = new ArrayList<>();

		if(src.getPosition().getX() + 1 < 8) {

			Square square = board.getSquare(src.getPosition().getX() + 1, src.getPosition().getY());
			Piece piece = square.getPiece();

			if(piece == null) {

				destinations.add(square);

				if(src.getPosition().getX() == 1) {

					square = board.getSquare(3, src.getPosition().getY());

					if(square.getPiece() == null)
						destinations.add(square);

				}

			}

			destinations.add(square);

		}

		return destinations;

	}

	private List<Square> getAvailableDestinationsBottomRight(Board board, Square src) {

		List<Square> destinations = new ArrayList<>();

		if(src.getPosition().getX() + 1 < 8 && src.getPosition().getY() + 1 < 8) {

			Square square = board.getSquare(src.getPosition().getX() + 1, src.getPosition().getY() + 1);
			Piece pieceToCapture = square.getPiece();

			if(pieceToCapture != null && pieceToCapture.getColor() != src.getPiece().getColor())
				destinations.add(square);

			// en passant

			pieceToCapture = board.getSquare(src.getPosition().getX(), src.getPosition().getY() + 1).getPiece();

			Move previousMove = board.getLastMove();

			if(board.getSquare(src.getPosition().getX() + 1, src.getPosition().getY() + 1).getPiece() == null
				&& pieceToCapture != null
				&& pieceToCapture.isPawn()
				&& pieceToCapture.getColor() != src.getPiece().getColor()
				&& previousMove != null
				&& previousMove.getSrc().getPiece().equals(pieceToCapture)
				&& previousMove.getDst().getPosition().getX() == previousMove.getSrc().getPosition().getX() - 2)
			 	destinations.add(square);

		}

		return destinations;

	}

}