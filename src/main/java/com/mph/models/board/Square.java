package com.mph.models.board;

import java.util.Objects;

import com.mph.models.piece.Piece;

import com.mph.util.Prototype;

public class Square implements Prototype<Square> {

	private Position position;

	private Piece piece;

	public Square(int x, int y) {
		position = new Position(x, y);
	}

	@Override
	public Square clone() {

		Square square = new Square(position.getX(), position.getY());

		if(piece != null)
			square.setPiece(piece.clone());

		return square;

	}

	@Override
	public int hashCode() {
		return Objects.hash(position);
	}

	@Override
	public boolean equals(Object obj) {

		if(this == obj)
			return true;

		if(obj == null)
			return false;

		if(getClass() != obj.getClass())
			return false;

		Square other = (Square) obj;

		return Objects.equals(position, other.position)
			&& Objects.equals(piece, other.getPiece());

	}

	@Override
	public String toString() {

		if(piece != null) {

			if(piece.isPawn() && piece.isBlack())
				return "\u200A" + piece + "\u3000";

			return "\u3000" + piece + "\u3000";

		}

		return "\u200A\u3000\u3000\u3000";

	}

	public Position getPosition() {
		return position;
	}

	public Piece getPiece() {
		return piece;
	}

	void setPiece(Piece piece) {
		this.piece = piece;
	}

}