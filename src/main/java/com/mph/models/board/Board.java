package com.mph.models.board;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import com.mph.exceptions.InvalidMoveException;

import com.mph.models.piece.Piece;
import com.mph.models.piece.Color;
import com.mph.models.move.Move;

import com.mph.util.Prototype;

public class Board implements Prototype<Board> {

	private Square[][] tab;

	private List<Move> moveHistory;

	public Board(int rows, int columns) {

		tab = new Square[rows][columns];

		for(int i = 0; i < tab.length; i++) {

			for(int j = 0; j < tab[i].length; j++) {

				tab[i][j] = new Square(i, j);

			}

		}

		moveHistory = new ArrayList<>();

	}

	public boolean movePiece(Move move) throws InvalidMoveException {

		Piece pieceToMove = move.getSrc().getPiece();

		boolean didCapture = pieceToMove.move(this, move);

		return didCapture;

	}

	public void addPiece(int x, int y, Piece piece) {
		tab[x][y].setPiece(piece);
	}

	public void removePiece(int x, int y) {
		tab[x][y].setPiece(null);
	}

	public Move getLastMove() {

		if(moveHistory.size() > 0)
			return moveHistory.get(moveHistory.size() - 1);

		return null;

	}

	public void addMoveToHistory(Move move) {
		moveHistory.add(move);
	}

	public boolean hasAlreadyMoved(Piece piece) {

		return moveHistory
			.stream()
			.filter(move -> piece.equals(move.getSrc().getPiece()))
			.findFirst()
			.isPresent();

	}

	public Square getSquare(int x, int y) {
		return tab[x][y];
	}

	public Position getKingPosition(Color color) {

		for(int i = 0; i < tab.length; i++) {

			for(int j = 0; j < tab[i].length; j++) {

				Piece piece = tab[i][j].getPiece();

				if(piece != null && piece.getColor() == color && piece.isKing())
					return tab[i][j].getPosition();

			}

		}

		return null;

	}

	@Override
	public Board clone() {

		Board board = new Board(tab.length, tab[0].length);

		for(int i = 0; i < tab.length; i++) {

			for(int j = 0; j < tab[i].length; j++) {

				Piece piece = tab[i][j].getPiece();

				if(piece != null)
					board.addPiece(i, j, piece.clone());

			}

		}

		return board;

	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;

		result = prime * result + Arrays.deepHashCode(tab);

		return result;

	}

	@Override
	public boolean equals(Object obj) {

		if(this == obj)
			return true;

		if(obj == null)
			return false;

		if(getClass() != obj.getClass())
			return false;

		Board other = (Board) obj;

		return Arrays.deepEquals(tab, other.tab);

	}

	@Override
	public String toString() {

		StringBuilder stringBuilder = new StringBuilder();

		for(int i = 0; i < tab.length; i++) {

			for(int j = 0; j < tab[i].length; j++) {

				if((i % 2 == 0 && j % 2 == 0) || (i % 2 != 0 && j % 2 != 0))
					stringBuilder.append("\033[46m");
				else
					stringBuilder.append("\u001B[47m");

				stringBuilder.append(tab[i][j]);

				stringBuilder.append("\u001B[0m");

			}
			
			stringBuilder.append("\u200A");

			stringBuilder.append("\033[0;31m");
			stringBuilder.append(i);
			stringBuilder.append("\033[0m");

			stringBuilder.append("\n");

		}

		stringBuilder.append("\033[0;31m");

		char[] columns = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};

		for(char column: columns) {

			if(column == 'a')
				stringBuilder.append("\u200A\u3000" + column + "\u200A\u3000");
			else
				stringBuilder.append("\u2009\u3000" + column + "\u3000");

		}

		stringBuilder.append("\033[0m");

		return stringBuilder.toString();

	}

}