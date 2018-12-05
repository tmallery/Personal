package tgm.gameoflife.state;

/**
 * Generates cell state strings for the Game of Life. This solutions
 * works for grids up to 5. A 6x6 grid will take up about 68 GB of
 * memory.
 */
public class StateGenerator {


	public String [] generate(int length) {
		return generate(new String [] { "0", "1" }, length);
	}

	private String[] generate(String[] elems, int length) {
		if( length <= 1 ) return elems;

		String [] subElems = generate(elems, length-1);
		String[] result = new String[subElems.length*elems.length];

		int resultIdx = 0;
		for( String elm : elems ) {
			for( String subElm : subElems ) {
				result[resultIdx++] = (elm + subElm);
			}
		}

		return result;
	}
}
