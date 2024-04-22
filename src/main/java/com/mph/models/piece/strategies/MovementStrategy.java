package com.mph.models.piece.strategies;

import java.util.List;

import com.mph.exceptions.InvalidMoveException;

import com.mph.models.board.Board;
import com.mph.models.board.Square;
import com.mph.models.move.Move;

public interface MovementStrategy {

	boolean move(Board board, Move move) throws InvalidMoveException;

	List<Square> getAvailableDestinations(Board board, Square src);

}