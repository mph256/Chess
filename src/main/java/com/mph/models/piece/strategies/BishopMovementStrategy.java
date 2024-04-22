package com.mph.models.piece.strategies;

import java.util.List;
import java.util.ArrayList;

import com.mph.exceptions.InvalidMoveException;

import com.mph.models.board.Board;
import com.mph.models.board.Square;
import com.mph.models.board.Position;
import com.mph.models.piece.Piece;
import com.mph.models.move.Move;

public class BishopMovementStrategy implements MovementStrategy {

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

		destinations.addAll(getAvailableDestinationsTopLeft(board, src));
		destinations.addAll(getAvailableDestinationsTopRight(board, src));
		destinations.addAll(getAvailableDestinationsBottomLeft(board, src));
		destinations.addAll(getAvailableDestinationsBottomRight(board, src));

		return destinations;

	}

	private boolean isValidMove(Board board, Move move) {

		Position src = move.getSrc().getPosition();
		Position dst = move.getDst().getPosition();

		if(dst.getX() < src.getX() && dst.getY() < src.getY())
			return checkTopLeft(board, move);

		if(dst.getX() < src.getX() && dst.getY() > src.getY())
			return checkTopRight(board, move);

		if(dst.getX() > src.getX() && dst.getY() < src.getY())
			return checkBottomLeft(board, move);

		if(dst.getX() > src.getX() && dst.getY() > src.getY())
			return checkBottomRight(board, move);

		return false;

	}

	private boolean checkTopLeft(Board board, Move move) {

		Position src = move.getSrc().getPosition();
		Position dst = move.getDst().getPosition();

		if(dst.getX() != src.getX() + (dst.getY() - src.getY())
			|| dst.getY() != src.getY() + (dst.getX() - src.getX()))
			return false;

		int j = src.getY() - 1;

		if(j >= 0) {

			for(int i = src.getX() - 1; i > dst.getX(); i--) {

				if(board.getSquare(i, j).getPiece() != null)
					return false;

				if(j == 0)
					break;

				j--;

			}

		}

		return true;

	}

	private boolean checkTopRight(Board board, Move move) {

		Position src = move.getSrc().getPosition();
		Position dst = move.getDst().getPosition();

		if(dst.getX() != src.getX() - (dst.getY() - src.getY())
			|| dst.getY() != src.getY() - (dst.getX() - src.getX()))
			return false;

		int j = src.getY() + 1;

		if(j < 8) {

			for(int i = src.getX() - 1; i > dst.getX(); i--) {

				if(board.getSquare(i, j).getPiece() != null)
					return false;

				if(j == 7)
					break;

				j++;

			}

		}

		return true;

	}

	private boolean checkBottomLeft(Board board, Move move) {

		Position src = move.getSrc().getPosition();
		Position dst = move.getDst().getPosition();

		if(dst.getX() != src.getX() - (dst.getY() - src.getY())
			|| dst.getY() != src.getY() - (dst.getX() - src.getX()))
			return false;

		int j = src.getY() - 1;

		if(j >= 0) {

			for(int i = src.getX() + 1; i < dst.getX(); i++) {

				if(board.getSquare(i, j).getPiece() != null)
					return false;

				if(j == 0)
					break;

				j--;

			}

		}

		return true;

	}

	private boolean checkBottomRight(Board board, Move move) {

		Position src = move.getSrc().getPosition();
		Position dst = move.getDst().getPosition();

		if(dst.getX() != src.getX() + (dst.getY() - src.getY())
			|| dst.getY() != src.getY() + (dst.getX() - src.getX()))
			return false;

		int j = src.getY() + 1;

		if(j < 8) {

			for(int i = src.getX() + 1; i < dst.getX(); i++) {

				if(board.getSquare(i, j).getPiece() != null)
					return false;

				if(j == 7)
					break;

				j++;

			}

		}

		return true;

	}

	private List<Square> getAvailableDestinationsTopLeft(Board board, Square src) {

		List<Square> destinations = new ArrayList<>();

		int j = src.getPosition().getY() - 1;

		if(j >= 0) {

			for(int i = src.getPosition().getX() - 1; i >= 0; i--) {

				Square square = board.getSquare(i, j);
				Piece piece = square.getPiece();

				if(piece != null) {

					if(piece.getColor() != src.getPiece().getColor())
						destinations.add(square);

					return destinations;

				}

				destinations.add(square);

				if(j == 0)
					break;

				j--;

			}

		}

		return destinations;

	}

	private List<Square> getAvailableDestinationsTopRight(Board board, Square src) {

		List<Square> destinations = new ArrayList<>();

		int j = src.getPosition().getY() + 1;

		if(j < 8) {

			for(int i = src.getPosition().getX() - 1; i >= 0; i--) {

				Square square = board.getSquare(i, j);
				Piece piece = square.getPiece();

				if(piece != null) {

					if(piece.getColor() != src.getPiece().getColor())
						destinations.add(square);

					return destinations;

				}

				destinations.add(square);

				if(j == 7)
					break;

				j++;

			}

		}

		return destinations;

	}

	private List<Square> getAvailableDestinationsBottomLeft(Board board, Square src) {

		List<Square> destinations = new ArrayList<>();

		int j = src.getPosition().getY() - 1;

		if(j >= 0) {

			for(int i = src.getPosition().getX() + 1; i < 8; i++) {

				Square square = board.getSquare(i, j);
				Piece piece = square.getPiece();

				if(square.getPiece() != null) {

					if(piece.getColor() != src.getPiece().getColor())
						destinations.add(square);

					return destinations;

				}

				destinations.add(square);

				if(j == 0)
					break;

				j--;

			}

		}

		return destinations;

	}

	private List<Square> getAvailableDestinationsBottomRight(Board board, Square src) {

		List<Square> destinations = new ArrayList<>();

		int j = src.getPosition().getY() + 1;

		if(j < 8) {

			for(int i = src.getPosition().getX() + 1; i < 8; i++) {

				Square square = board.getSquare(i, j);
				Piece piece = square.getPiece();

				if(piece != null) {

					if(piece.getColor() != src.getPiece().getColor())
						destinations.add(square);

					return destinations;

				}

				destinations.add(square);

				if(j == 7)
					break;

				j++;

			}

		}

		return destinations;

	}

}