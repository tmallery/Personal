package tgm.utils;

/**
 * Collection of utilities for Arrays
 */
public class ArrayUtils {

	private static final ArrayUtils s_instance = new ArrayUtils();

	public static ArrayUtils getInstance() {
		return s_instance;
	}

	/**
	 * Returns true if the value at each index matches
	 * @param a1
	 * @param a2
	 * @return
	 */
	public boolean arrayEquals(byte [][] a1, byte [][] a2 ) {
		boolean result = false;

		if( a1 != null && a2 != null &&
			a1.length > 0 && a2.length > 0 )
		{

			boolean mismatch = false;
			outer:
			for (int y = 0; y < a1.length && y < a2.length; y++) {
				for (int x = 0; x < a1[y].length && x < a2[y].length; x++) {
					if( a1[y][x] != a2[y][x] ) {
						mismatch = true;
						break outer;
					}
				}
			}

			if( !mismatch ) {
				result = true;
			}
		}


		return result;
	}

	/**
	 * Returns a new array with the values from the provided array rotated 90 degrees
	 * to the right.
	 * @param arr [double array to rotate]
	 * @return a new array of rotated values
	 */
	public byte[][] rotateArrayRight(byte [][] arr) {

		byte[][] result = new byte[arr[0].length][arr.length];

		for(int y = 0; y < arr.length; y++ ) {
			for(int x = 0; x < arr[y].length; x++ ) {
				int posx = (arr.length-1) - y,
					posy = x;

				result[posy][posx] = arr[y][x];
			}
		}
		return result;
	}

	/**
	 * Returns a new array with the values transposed in a mirror image
	 * @param arr
	 * @return
	 */
	public byte[][] mirrorImage(byte [][] arr) {
		byte[][] result = new byte[arr.length][arr[0].length];

		for(int y = 0; y < arr.length; y++ ) {
			for(int x = 0; x < arr[y].length; x++ ) {
				int posx = (arr[y].length -1) - x,
					posy = y;

				result[posy][posx] = arr[y][x];
			}
		}
		return result;
	}
}
