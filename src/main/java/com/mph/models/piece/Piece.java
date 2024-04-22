package com.mph.models.piece;

import java.util.List;

import java.util.Objects;

import java.util.concurrent.atomic.AtomicLong;

import com.mph.exceptions.InvalidMoveException;

import com.mph.models.piece.strategies.MovementStrategy;
import com.mph.models.piece.strategies.KingMovementStrategy;
import com.mph.models.piece.strategies.QueenMovementStrategy;
import com.mph.models.piece.strategies.BishopMovementStrategy;
import com.mph.models.piece.strategies.KnightMovementStrategy;
import com.mph.models.piece.strategies.RookMovementStrategy;
import com.mph.models.piece.strategies.PawnMovementStrategy;

import com.mph.models.board.Board;
import com.mph.models.board.Square;
import com.mph.models.move.Move;

import com.mph.util.Prototype;

public class Piece implements Prototype<Piece> {

	private static final AtomicLong count = new AtomicLong();

	private long id;

	private Color color;

	private MovementStrategy movementStrategy;

	public Piece(Color color, MovementStrategy moveStrategy) {

		id = count.incrementAndGet();

		this.color = color;
		this.movementStrategy = moveStrategy;

	}

	private Piece(long id, Color color, MovementStrategy moveStrategy) {

		this.id = id;
		this.color = color;
		this.movementStrategy = moveStrategy;

	}

	public boolean move(Board board, Move move) throws InvalidMoveException {
		return movementStrategy.move(board, move);
	}

	public List<Square> getAvailableDestinations(Board board, Square src) {
		return movementStrategy.getAvailableDestinations(board, src);
	}

	public boolean isWhite() {
		return color == Color.WHITE;
	}

	public boolean isBlack() {
		return color == Color.BLACK;
	}

	public boolean isKing() {
		return movementStrategy instanceof KingMovementStrategy;
	}

	public boolean isQueen() {
		return movementStrategy instanceof QueenMovementStrategy;
	}

	public boolean isBishop() {
		return movementStrategy instanceof BishopMovementStrategy;
	}

	public boolean isKnight() {
		return movementStrategy instanceof KnightMovementStrategy;
	}

	public boolean isRook() {
		return movementStrategy instanceof RookMovementStrategy;
	}

	public boolean isPawn() {
		return movementStrategy instanceof PawnMovementStrategy;
	}

	public Piece clone() {
		return new Piece(id, color, movementStrategy);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {

		if(this == obj)
			return true;

		if(obj == null)
			return false;

		if(getClass() != obj.getClass())
			return false;

		Piece other = (Piece) obj;

		return id == other.id;

	}

	@Override
	public String toString() {

		if(color == Color.WHITE) {

			if(movementStrategy instanceof KingMovementStrategy)
				return "♔";

			if(movementStrategy instanceof QueenMovementStrategy)
				return "♕";

			if(movementStrategy instanceof BishopMovementStrategy)
				return "♗";

			if(movementStrategy instanceof KnightMovementStrategy)
				return "♘";

			if(movementStrategy instanceof RookMovementStrategy)
				return "♖";

			return "♙";

		}

		if(movementStrategy instanceof KingMovementStrategy)
			return "♚";

		if(movementStrategy instanceof QueenMovementStrategy)
			return "♛";

		if(movementStrategy instanceof BishopMovementStrategy)
			return "♝";

		if(movementStrategy instanceof KnightMovementStrategy)
			return "♞";

		if(movementStrategy instanceof RookMovementStrategy)
			return "♜";

		return "♟︎";

	}

	public long getId() {
		return id;
	}

	public Color getColor() {
		return color;
	}

	public void setMovementStrategy(MovementStrategy movementStrategy) {
		this.movementStrategy = movementStrategy;
	}

}