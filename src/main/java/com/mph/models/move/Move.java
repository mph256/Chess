package com.mph.models.move;

import java.util.Objects;

import com.mph.models.board.Square;

import com.mph.util.Prototype;

public class Move implements Prototype<Move> {

	private Square src;

	private Square dst;

	public Move(Square src, Square dst) {

		this.src = src;
		this.dst = dst;

	}

	@Override
	public Move clone() {
		return new Move(src.clone(), dst.clone());
	}

	@Override
	public int hashCode() {
		return Objects.hash(dst, src);
	}

	@Override
	public boolean equals(Object obj) {

		if(this == obj)
			return true;

		if(obj == null)
			return false;

		if(getClass() != obj.getClass())
			return false;

		Move other = (Move) obj;

		return Objects.equals(src, other.src) 
			&& Objects.equals(dst, other.dst);

	}

	public Square getSrc() {
		return src;
	}

	public Square getDst() {
		return dst;
	}

}