package tgm.gameoflife;

import tgm.utils.Log;

/**
 * Java implementation of Conways Game of Life:
 * https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life.
 * Currently this is a single threaded implementation
 *
 * NotThreadSafe
 */
public class GameOfLife {
	static final byte 	DEAD_STATE = 0,
						ALIVE_STATE = 1;

	/**
	 * Parses a cell state string which has the following requirements
	 * 1) Must be a square [width is the square root of the total length]
	 * 2) '1' indicates live state, '0' indicates dead state
	 * 3) Must not contain any other characters
	 *
	 * Boundary cells do not overlap, in that they do not count the cell on
	 * the far side as being connected. Practically this means that Gliders
	 * will only live until they hit the far side.
	 *
	 * @param state [cell state string]
	 * @return cell array
	 * @throws IllegalArgumentException if any of the rules above are not
	 *                                  followed
	 */
	static byte[][] parseSquareState(String state) {
		byte[][] result = null;

		if (state != null && state.length() > 0) {
			double size = Math.sqrt(state.length());
			// require that the length is an integer
			// square root otherwise the board isn't
			// even
			if ((size % 1) == 0) {
				int stateIdx = 0;
				int rounded = (int) Math.round(size);
				result = new byte[rounded][rounded];

				outer:
				for (int y = 0; y < result.length; y++) {
					for (int x = 0; x < result[y].length; x++) {
						String cellState = String.valueOf(state.charAt(stateIdx++));
						Byte val = null;
						try {
							val = Byte.parseByte(cellState);
						} catch (NumberFormatException e) {
							Log.debug("GameOfLife: invalid cell state " + cellState + " at index " + (stateIdx - 1));
						}
						if (val != null && (val == DEAD_STATE || val == ALIVE_STATE)) {
							result[y][x] = val;
						} else {
							result = null;
							break outer;
						}
					}
				}
			}
		}


		return result;
	}

	private byte[][] m_cells;

	/**
	 * Creates the Game of Life with the provided starting state.
	 * The state must represent dead cells with 0, and live cells with 1,
	 * and be a square with no other characters.
	 *
	 * @param state [starting state]
	 */
	public GameOfLife(String state) {
		m_cells = parseSquareState(state);

		if (m_cells == null) {
			throw new IllegalArgumentException("GameOfLife: Invalid Input State");
		}
	}

	/**
	 * Counts the number of live cells around the provided array coordinate
	 *
	 * @param x [width position]
	 * @param y [height position]
	 * @return number of live cells surrounding this coordinate
	 */
	private int countLiveNeighbors(int x, int y) {
		int count = 0;
		for (int yOff = -1; yOff <= 1; yOff++) {
			for (int xOff = -1; xOff <= 1; xOff++) {
				// skip over the node that we're counting around
				if (!(xOff == 0 && yOff == 0)) {

					int posX = x + xOff;
					int posY = y + yOff;

					// verify that our new coordinate is within the bounds of the cells array
					if (posX >= 0 && posY >= 0 &&
						posY < this.m_cells.length && posX < this.m_cells[posY].length) {
						if (this.m_cells[posY][posX] == ALIVE_STATE) {
							count++;
						}
					}
				}
			}
		}
		return count;
	}

	/**
	 * Rules: [alive and 2,3 neighbors - stays alive]
	 * [alive and 0,1 neighbors - dies ]
	 * [alive and 4,5,6,7,8 neighbors - dies ]
	 * [dead and 3 neighbors - becomes alive ]
	 *
	 * @param x [width position]
	 * @param y [height position]
	 * @return
	 */
	private byte calculateNewState(int x, int y) {
		int neighborCount = this.countLiveNeighbors(x, y);
		byte curCellState = m_cells[y][x],
			newCellState;

		// if the cell is dead the only way for it to come alive again is if
		// it as exactly three neighbors
		if (curCellState == GameOfLife.DEAD_STATE) {
			if (neighborCount == 3) {
				newCellState = GameOfLife.ALIVE_STATE;
			} else {
				newCellState = GameOfLife.DEAD_STATE;
			}
		// for living cells, they will die unless they have two or three neighbors
		} else {
			if (neighborCount == 2 || neighborCount == 3) {
				newCellState = GameOfLife.ALIVE_STATE;
			} else {
				newCellState = GameOfLife.DEAD_STATE;
			}
		}

		return newCellState;
	}

	/**
	 * Update each cell in the game
	 */
	public void updateState() {
		byte[][] updatedCells = new byte[m_cells.length][m_cells.length];

		for (int y = 0; y < m_cells.length; y++) {
			for (int x = 0; x < m_cells[y].length; x++) {
				updatedCells[y][x] = this.calculateNewState(x, y);
			}
		}

		m_cells = updatedCells;
	}

	/**
	 * Returns the current game state as a string
	 *
	 * @return [current state]
	 */
	public String getStateString() {
		StringBuilder sb = new StringBuilder();

		for (int y = 0; y < m_cells.length; y++) {
			for (int x = 0; x < m_cells[y].length; x++) {
				sb.append(m_cells[y][x]);
			}
		}

		return sb.toString();
	}

	/**
	 * Pretty print the current game state
	 *
	 * @return pretty state
	 */
	public String prettyPrintStateString() {
		StringBuilder sb = new StringBuilder();

		for (int y = 0; y < m_cells.length; y++) {
			sb.append("[");
			for (int x = 0; x < m_cells[y].length; x++) {
				if (x > 0) {
					sb.append(",");
				}
				sb.append(m_cells[y][x]);
			}
			sb.append("]");
		}

		return sb.toString();
	}
}
