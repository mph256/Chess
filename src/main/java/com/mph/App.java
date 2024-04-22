package com.mph;

import java.util.Scanner;

import java.util.List;
import java.util.ArrayList;

import com.mph.exceptions.InvalidBoardException;
import com.mph.exceptions.InvalidMoveException;

import com.mph.models.board.builders.DefaultBoardBuilder;
import com.mph.models.board.builders.CheckBoardBuilder;
import com.mph.models.board.builders.CheckmateBoardBuilder;
import com.mph.models.board.builders.StalemateBoardBuilder;
import com.mph.models.board.builders.ThreefoldRepetitionBoardBuilder;
import com.mph.models.board.builders.CastlingBoardBuilder;
import com.mph.models.board.builders.EnPassantBoardBuilder;
import com.mph.models.board.builders.PromotionBoardBuilder;

import com.mph.models.game.states.CheckState;
import com.mph.models.game.states.CheckmateState;

import com.mph.models.game.Game;
import com.mph.models.board.Board;
import com.mph.models.move.Move;

public class App {

	public static void main( String[] args ) {

		System.out.print("\u001B[44m");
		System.out.print("\u001B[37m");
		System.out.println("                             ");
        System.out.println("            Chess            ");
		System.out.println("                             ");
        System.out.print("\033[0m");
        System.out.print("\u001B[0m");
 
        try {

			loadMenu();

		} catch (InvalidBoardException e) {

			e.printStackTrace();

		}

	}

	public static void loadMenu() throws InvalidBoardException {

		try(Scanner scanner = new Scanner(System.in)) {

			String input = "";

			do {

		        System.out.println("\n");

				System.out.print("\u001B[43m");
				System.out.println("         New game (g)        ");
		        System.out.print("\u001B[0m");
				System.out.println("                             ");
				System.out.print("\u001B[43m");
				System.out.println("           Quit (q)          ");
		        System.out.print("\u001B[0m");

				System.out.println("\n");

				input = scanner.nextLine().trim();

				if("g".equalsIgnoreCase(input)) {

					Game game = new Game(new DefaultBoardBuilder());
//					Game game = new Game(new CheckBoardBuilder()); // e7 -> f7
//					Game game = new Game(new CheckmateBoardBuilder()); // a1 -> e1
//					Game game = new Game(new StalemateBoardBuilder()); // d1 -> c1
//					Game game = new Game(new ThreefoldRepetitionBoardBuilder()); // a3 -> d0 AND b0 -> a1 AND d0 -> a3 AND a1 -> b0 AND a3 -> d0 AND b0 -> a1 AND d0 -> a3 AND a1 -> b0
//					Game game = new Game(new CastlingBoardBuilder()); // e7 -> c7 OR e7 -> g7
//					Game game = new Game(new EnPassantBoardBuilder()); // a6 -> a4 AND b4 -> a5 
//					Game game = new Game(new PromotionBoardBuilder()); // a1 -> a0

					Board board = game.getBoard();

					while(!game.isOver()) {

						System.out.println("\n");

						System.out.println(board);

						System.out.println("\n");

						System.out.println("Turn: " + (game.getTurn() % 2 != 0 ? "White" : "Black"));

						if(game.getState() instanceof CheckState) {

							System.out.println("\n");
							System.out.println("CHECK");

						}

						System.out.println("\n");

						System.out.print("Which piece do you want to move ? ");

						String src = scanner.nextLine().toLowerCase().trim();

						System.out.print("Where do you want to move it ? ");

						String dst = scanner.nextLine().toLowerCase().trim();

						try {

							game.movePiece(new Move(board.getSquare(getRowFromInput(src), getColumnFromInput(src)), board.getSquare(getRowFromInput(dst), getColumnFromInput(dst))));

						} catch (InvalidMoveException e) {

							System.out.println("\n");

							System.out.print("\033[0;31m");
							System.out.println("Invalid move");
							System.out.print("\033[0m");

						}

					}

					System.out.println("\n");

					System.out.println(board);

					System.out.println("\n");

					if(game.getState() instanceof CheckmateState) {

						if(game.getTurn() % 2 != 0)
							System.out.println("WINNER: White");
						else
							System.out.println("WINNER: Black");

					} else
						System.out.println("DRAW");

				} else if(!"q".equalsIgnoreCase(input)) {

					System.out.println("\n");

					System.out.print("\033[0;31m");
					System.out.println("Invalid command");
					System.out.print("\033[0m");

				}

			} while(!"q".equalsIgnoreCase(input));

		}

	}

	private static List<String> getValidInputs() {

		List<String> validInputs = new ArrayList<>();

		for(String row: new String[]{"0", "1", "2", "3", "4", "5", "6", "7"}) {

			for(String column: new String[]{"a", "b", "c", "d", "e", "f", "g", "h"}) {

				validInputs.add(row + column);
				validInputs.add(column + row);

			}

		}

		return validInputs;

	}

	private static int getRowFromInput(String input) throws InvalidMoveException {

		if(input.length() != 2 || !getValidInputs().contains(input))
			throw new InvalidMoveException();

		if(input.contains("0"))
			return 0;

		if(input.contains("1"))
			return 1;

		if(input.contains("2"))
			return 2;

		if(input.contains("3"))
			return 3;

		if(input.contains("4"))
			return 4;

		if(input.contains("5"))
			return 5;

		if(input.contains("6"))
			return 6;

		return 7;

	}

	private static int getColumnFromInput(String input) throws InvalidMoveException {

		if(input.length() != 2 || !getValidInputs().contains(input))
			throw new InvalidMoveException();

		input = input.toLowerCase();

		if(input.contains("a"))
			return 0;

		if(input.contains("b"))
			return 1;

		if(input.contains("c"))
			return 2;

		if(input.contains("d"))
			return 3;

		if(input.contains("e"))
			return 4;

		if(input.contains("f"))
			return 5;

		if(input.contains("g"))
			return 6;

		return 7;

	}

}