package com.mph.models.piece.strategies;

import java.util.List;
import java.util.ArrayList;

import java.util.stream.Collectors;

import com.mph.exceptions.InvalidMoveException;

import com.mph.models.board.Board;
import com.mph.models.board.Square;
import com.mph.models.board.Position;
import com.mph.models.move.Move;

public class KnightMovementStrategy implements MovementStrategy {

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

		if(src.getPosition().getX() - 2 >= 0 && src.getPosition().getY() - 1 >= 0)
			destinations.add(board.getSquare(src.getPosition().getX() - 2, src.getPosition().getY() - 1));

		if(src.getPosition().getX() - 2 >= 0 && src.getPosition().getY() + 1 < 8)
			destinations.add(board.getSquare(src.getPosition().getX() - 2, src.getPosition().getY() + 1));

		if(src.getPosition().getX() - 1 >= 0 && src.getPosition().getY() - 2 >= 0)
			destinations.add(board.getSquare(src.getPosition().getX() - 1, src.getPosition().getY() - 2));

		if(src.getPosition().getX() - 1 >= 0 && src.getPosition().getY() + 2 < 8)
			destinations.add(board.getSquare(src.getPosition().getX() - 1, src.getPosition().getY() + 2));

		if(src.getPosition().getX() + 1 < 8 && src.getPosition().getY() - 2 >= 0)
			destinations.add(board.getSquare(src.getPosition().getX() + 1, src.getPosition().getY() - 2));

		if(src.getPosition().getX() + 1 < 8 && src.getPosition().getY() + 2 < 8)
			destinations.add(board.getSquare(src.getPosition().getX() + 1, src.getPosition().getY() + 2));

		if(src.getPosition().getX() + 2 < 8 && src.getPosition().getY() - 1 >= 0)
			destinations.add(board.getSquare(src.getPosition().getX() + 2, src.getPosition().getY() - 1));

		if(src.getPosition().getX() + 2 < 8 && src.getPosition().getY() + 1 < 8)
			destinations.add(board.getSquare(src.getPosition().getX() + 2, src.getPosition().getY() + 1));

		return destinations
			.stream()
			.filter(destination -> destination.getPiece() != null ? destination.getPiece().getColor() != src.getPiece().getColor() : true)
			.collect(Collectors.toList());

	}

	private boolean isValidMove(Board board, Move move) {

		Position src = move.getSrc().getPosition();
		Position dst = move.getDst().getPosition();

		return (((dst.getX() == src.getX() - 2 || dst.getX() == src.getX() + 2) && (dst.getY() == src.getY() - 1 || dst.getY() == src.getY() + 1))
			|| ((dst.getX() == src.getX() - 1 || dst.getX() == src.getX() + 1) && (dst.getY() == src.getY() - 2 || dst.getY() == src.getY() + 2)));

	}

}