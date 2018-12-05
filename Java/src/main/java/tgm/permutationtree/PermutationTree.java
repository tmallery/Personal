/**
 *  PermutationTree.java, Copyright 2018 Thomas Mallery
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package tgm.permutationtree;

import java.util.ArrayList;

/**
 * This PermutationTree exists as a way to handle extremely large
 * input sets of data, where an application wants to test all
 * possible combinations of a type of data.
 *
 * The tree is stateful, every call to getNextPermutation() will return the next
 * permutation until no more exist. At this point, the object has completed its
 * purpose and if more permutations are required, a new PermutationTree must be
 * created.
 *
 * For the game of life, a usage would look like:<br/>
 * PermutationTree< Byte > states = new PermutationTree<>(6*6, new Byte[]{ 0, 1 });
 * This will generate a Byte array list with all possible combinations of [0,1] with
 * total lengths of 36, which is a 6x6 Game of Life grid.
 *
 * @author Thomas Mallery
 *
 * @param <T> the value of the nodes
 */
public class PermutationTree<T> extends PermutationTreeNode {

	/**
	 * Create a new permutation tree. The tree depth indicates
	 * how long each permutation is.
	 * @param depth length of permutations
	 * @param valueArr possible values used in the permutation
	 */
	public PermutationTree(int depth, T[] valueArr) {
		super(null, valueArr, depth);
	}

	/**
	 * @return
	 * Returns the next permutation until all permutations have been returned and then
	 * null is returned.
	 */
	public synchronized ArrayList<T> getNextPermutation() {
		return getNext();
	}
}
