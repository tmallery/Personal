package tgm.gameoflife.state;

import java.util.ArrayList;

public class StateTreeNode {

	byte m_value;
	byte[] m_valueTypes;
	StateTreeNode m_parent;
	StateTreeNode[] m_children;
	int m_accessIndex;
	int m_treeDepth;
	public StateTreeNode(byte val, byte[] valueTypes, int treeDepth) {
		m_value = val;
		m_valueTypes= valueTypes;
		m_parent = null;
		m_children = null;
		m_accessIndex = -1;
		m_treeDepth = treeDepth;
	}

	private void generateChild() {

		if( m_treeDepth > 0 ) {
			if (m_children == null) {
				m_children = new StateTreeNode[m_valueTypes.length];
			}
			new StateTreeNode(m_valueTypes[m_accessIndex], m_valueTypes, m_treeDepth - 1);
		}
	}

	ArrayList<Byte> getNext() {
		ArrayList<Byte> result = null;

		m_accessIndex++;
		generateChild();

		if( m_treeDepth == 0 ) {
			// leaf node

			// first access, return this node
			if( m_accessIndex == 0 ) {
				result = new ArrayList<Byte>();
				result.add(m_value);
			} else {
				// we'll return null
			}
		} else {
			// node that will have children
			if( m_accessIndex < m_valueTypes.length ) {
				result = m_children[m_accessIndex].getNext();
			} else {
				// we'll return null
			}
		}

		return result;
	}
}
