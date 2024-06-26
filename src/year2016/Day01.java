/*
 * https://adventofcode.com/2016/day/1
 *
 * --- Day 1: No Time for a Taxicab ---
 *
 * Santa's sleigh uses a very high-precision clock to guide its movements, and the clock's oscillator is
 * regulated by stars. Unfortunately, the stars have been stolen... by the Easter Bunny. To save Christmas,
 * Santa needs you to retrieve all fifty stars by December 25th.
 *
 * Collect stars by solving puzzles. Two puzzles will be made available on each day in the Advent calendar;
 * the second puzzle is unlocked when you complete the first. Each puzzle grants one star. Good luck!
 *
 * You're airdropped near Easter Bunny Headquarters in a city somewhere. "Near", unfortunately, is as close
 * as you can get - the instructions on the Easter Bunny Recruiting Document the Elves intercepted start
 * here, and nobody had time to work them out further.
 *
 * The Document indicates that you should start at the given coordinates (where you just landed) and face
 * North. Then, follow the provided sequence: either turn left (L) or right (R) 90 degrees, then walk forward
 * the given number of blocks, ending at a new intersection.
 *
 * There's no time to follow such ridiculous instructions on foot, though, so you take a moment and work
 * out the destination. Given that you can only walk on the street grid of the city, how far is the shortest
 * path to the destination?
 *
 * For example:
 *     Following R2, L3 leaves you 2 blocks East and 3 blocks North, or 5 blocks away.
 *     R2, R2, R2 leaves you 2 blocks due South of your starting position, which is 2 blocks away.
 *     R5, L5, R5, R3 leaves you 12 blocks away.
 *
 * How many blocks away is Easter Bunny HQ?
 *
 * Your puzzle answer was 236.
 *
 *
 * --- Part Two ---
 *
 * Then, you notice the instructions continue on the back of the Recruiting Document. Easter Bunny HQ is
 * actually at the first location you visit twice.
 *
 * For example, if your instructions are R8, R4, R4, R8, the first location you visit twice is 4 blocks
 * away, due East.
 *
 * How many blocks away is the first location you visit twice?
 *
 * Your puzzle answer was 182.
*/

package year2016;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import aoc.Day00;
import aoc.Position;



@SuppressWarnings("javadoc")
public class Day01 extends Day00 {
	private static enum Turn {
		RIGHT,
		LEFT;

		static Turn fromChar(char c) {
			return switch (c) {
			case 'R' -> RIGHT;
			case 'L' -> LEFT;
			default -> throw new IllegalArgumentException("Unexpected value: " + c);
			};
		}
	}

	private static record Instruction(Turn turn, int length) {
		Instruction(String in) {
			this(Turn.fromChar(in.charAt(0)), Integer.parseInt(in.substring(1)));
			if (length < 1) { throw new IllegalArgumentException("Length ahould be >0 (" + length + ")"); }
		}

		static List<Instruction> createList(List<String> list) { // CHECK how to make generic factory function
			var buffer = new LinkedList<Instruction>();
			list.forEach(str -> buffer.add(new Instruction(str)));
			return Collections.unmodifiableList(buffer);
		}
	}

	private static class Route {
		private static enum Direction {
			NORTH,
			EAST,
			SOUTH,
			WEST;

			Position getVector() {
				return switch (this) {
				case NORTH -> new Position(1, 0);
				case EAST -> new Position(0, 1);
				case SOUTH -> new Position(-1, 0);
				case WEST -> new Position(0, -1);
				default -> throw new IllegalArgumentException("Unexpected value: " + name());
				};
			}

			Direction turn(Turn turn) {
				return switch (this) {
				case NORTH -> (turn == Turn.RIGHT) ? EAST : WEST;
				case EAST -> (turn == Turn.RIGHT) ? SOUTH : NORTH;
				case SOUTH -> (turn == Turn.RIGHT) ? WEST : EAST;
				case WEST -> (turn == Turn.RIGHT) ? NORTH : SOUTH;
				default -> throw new IllegalArgumentException("Unexpected value: " + name());
				};
			}
		}

		private static final Direction START_DIRECTION = Direction.NORTH;
		private static final Position START_POS = new Position(0, 0);

		private final List<Instruction> instructions;

		Route(List<String> input) {
			instructions = Instruction.createList(input);
		}

		private static int calculateDistance(Position pos) {
			return Math.abs(pos.x) + Math.abs(pos.y);
		}

		int getShortestRoutePart1() {
			class Way {
				private Position pos = new Position(START_POS);
				private Direction direction = START_DIRECTION;

				private void walkOnce(Instruction i) {
					direction = direction.turn(i.turn);
					var newPos = direction.getVector().scale(i.length);
					pos.add(newPos);
				}

				public Position walk() {
					instructions.forEach(i -> walkOnce(i));
					return new Position(pos);
				}
			}

			return calculateDistance(new Way().walk());
		}

		int getShortestRoutePart2() {
			class Way {
				private static final Position INVALID_POS = new Position(Integer.MIN_VALUE);

				List<Position> positions = new LinkedList<>(Arrays.asList(START_POS));
				private Direction direction = START_DIRECTION;

				private Position walkOnce(Instruction instruction) {
					direction = direction.turn(instruction.turn);

					for (int i = 0; i < instruction.length; ++i) {
						var newPos = new Position(positions.getLast());
						newPos.add(direction.getVector());

						if (positions.contains(newPos)) { return newPos; }
						positions.add(newPos);
					}

					return new Position(INVALID_POS);
				}

				public Position walk() {
					for (Instruction i : instructions) {
						var pos = walkOnce(i);
						if (!pos.equals(INVALID_POS)) { return pos; }
					}

					throw new RuntimeException("Headquarter not found!");
				}

			}

			return calculateDistance(new Way().walk());
		}
	}



	public Day01() {
		super(2016, 1);
	}

	@Override
	protected void testPuzzle() {
		var c = new Route(Arrays.asList("R2", "L3"));
		io.printTest(c.getShortestRoutePart1(), 5);

		c = new Route(Arrays.asList("R2", "R2", "R2"));
		io.printTest(c.getShortestRoutePart1(), 2);

		c = new Route(Arrays.asList("R5", "L5", "R5", "R3"));
		io.printTest(c.getShortestRoutePart1(), 12);

		c = new Route(Arrays.asList("R8", "R4", "R4", "R8"));
		io.printTest(c.getShortestRoutePart2(), 4);
	}

	@Override
	public void solvePuzzle() {
		var c = new Route(io.readAllLines(", "));
		io.printResult(c.getShortestRoutePart1(), 236);
		io.printResult(c.getShortestRoutePart2(), 182);
	}
}