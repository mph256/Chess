package com.mph.models.piece.factories;

import com.mph.exceptions.TypeNotFoundException;
import com.mph.exceptions.ColorNotFoundException;

import com.mph.models.piece.Piece;
import com.mph.models.piece.Color;

import com.mph.models.piece.strategies.MovementStrategy;
import com.mph.models.piece.strategies.KingMovementStrategy;
import com.mph.models.piece.strategies.QueenMovementStrategy;
import com.mph.models.piece.strategies.BishopMovementStrategy;
import com.mph.models.piece.strategies.KnightMovementStrategy;
import com.mph.models.piece.strategies.RookMovementStrategy;
import com.mph.models.piece.strategies.PawnMovementStrategy;

public final class PieceFactory {

	private static PieceFactory instance;

	private PieceFactory() { }

	public static PieceFactory getInstance() {

		if(instance == null)
			instance = new PieceFactory();

		return instance;

	}

	public Piece createPiece(String colorStr, String movementStrategyStr) throws TypeNotFoundException, ColorNotFoundException {

		Color color = null;

		switch(colorStr) {

			case "white" -> color = Color.WHITE;
			case "black" -> color = Color.BLACK;
			default -> throw new ColorNotFoundException();

		}

		MovementStrategy movementStrategy = null;

		switch(movementStrategyStr) {

			case "king" -> movementStrategy = new KingMovementStrategy();
			case "queen" -> movementStrategy = new QueenMovementStrategy();
			case "bishop" -> movementStrategy = new BishopMovementStrategy();
			case "knight" -> movementStrategy = new KnightMovementStrategy();
			case "rook" -> movementStrategy = new RookMovementStrategy();
			case "pawn" -> movementStrategy = new PawnMovementStrategy();
			default -> throw new TypeNotFoundException();

		}

		return new Piece(color, movementStrategy);

	}

}