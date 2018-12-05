/**
 *  PermutationTreeLeafNode.java, Copyright 2018 Thomas Mallery
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
 * Represents a leaf node of the permutation tree. This node behaves differently
 * in that it has no children and once it returns its value the first time from
 * getNext(), it will always return null
 * @param <T> Generic Type
 */
public class PermutationTreeLeafNode<T> extends PermutationTreeNode<T> {
	PermutationTreeLeafNode(T val, T[] valueTypes) {
		super(val, valueTypes, 0);
	}

	@Override
	ArrayList<T> getNext() {
		ArrayList<T> result = null;
		if( m_accessIndex == 0 ) {
			result = new ArrayList<>();
			result.add(m_value);
			m_accessIndex++;
		}

		return result;
	}
}
