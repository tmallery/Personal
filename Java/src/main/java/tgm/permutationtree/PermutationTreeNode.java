/**
 *  PermutationTreeNode.java, Copyright 2018 Thomas Mallery
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
import java.util.List;

/**
 *
 * @athor Thomas Mallery
 * @param <T>
 */
class PermutationTreeNode<T> {
	protected T m_value;
	private T[] m_valueTypes;
	private List<PermutationTreeNode<T>> m_children;
	protected int m_accessIndex;
	private int m_treeDepth;

	PermutationTreeNode(T val, T[] valueTypes, int treeDepth) {
		m_value = val;
		m_valueTypes= valueTypes;
		m_children = null;
		m_accessIndex = 0;
		m_treeDepth = treeDepth;

	}

	/**
	 * Gets the child at the current index, a child node is created
	 * if one doesn't already exist.
	 *
	 * @return a child node.
	 */
	private PermutationTreeNode<T> getChild() {
		PermutationTreeNode<T> child;

			if( m_children == null ) {
				m_children = new ArrayList<>(m_valueTypes.length);
				m_accessIndex = 0;
			}

			if( m_accessIndex < m_children.size() ) {
				child = m_children.get(m_accessIndex);
			} else {
				if( m_treeDepth == 1 ) {
					child = new PermutationTreeLeafNode<>(m_valueTypes[m_accessIndex], m_valueTypes);
				} else {
					child = new PermutationTreeNode<>(m_valueTypes[m_accessIndex], m_valueTypes, m_treeDepth - 1);
				}
				m_children.add(child);
			}

		return child;
	}

	/**
	 * Returns the current permutation, else null is all permutations have been returned
	 * @return current permutation else null
	 */
	ArrayList<T> getNext() {

		if( m_accessIndex < m_valueTypes.length ) {
			ArrayList<T> result;
			result = (getChild()).getNext();
			if (result == null) {
				// wipe out previous references to allow the GC to do some clean up
				m_children.set(m_accessIndex, null);
				m_accessIndex++;

				return getNext();
			}
			if(m_value != null) result.add(m_value);
			return result;
		}

		return null;
	}
}
