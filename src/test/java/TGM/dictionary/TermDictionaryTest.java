package tgm.dictionary;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the TermDictionary
 */
class TermDictionaryTest {

	/**
	 * Test adding and reading values from the dictionary.
	 * This tests the simple case that everything we added
	 * is in fact in the dictionary. Tests that several
	 * obviously out of place strings are not in. Lastly,
	 * tests that sub strings of valid entries are not
	 * treated as a match.
	 */
	@Test
	void putGetTest() {
		TermDictionary d = new TermDictionary();

		String[] args = new String[] {
			"hello john day",
			"a fox jumps",
			"a blue bird sings",
			"hi there",
			"a",
			"a blue bird"
		};
		HashSet<String> entries = new HashSet<>(args.length);

		for(String sen : args ) {
			entries.add(sen);
			d.put(sen);
		}

		for(String sen : args) {
			assertTrue(d.isIn(sen));
		}

		args = new String[] {
			"hello",
			"a fox",
			"a blue bird sings",
			"hi",
			"a",
			"a blue",
		};

		for(String sen : args ) {
			assertEquals(entries.contains(sen), d.isIn(sen));
		}
	}

	@Test
	void cornerCaseTests() {
		TermDictionary d = new TermDictionary();
		d.put("");
		d.put(null);
	}

	@Test
	void goalStateTest() {
		TermDictionary d = new TermDictionary();

		HashMap<String, Integer> entries = new HashMap<>();

		entries.put("a b c", 1);
		entries.put("ab be cd", 2);
		entries.put("a b", 3);
		entries.put("a", 4);

		for( Map.Entry<String, Integer> entry : entries.entrySet()) {
			d.put(entry.getKey(), entry.getValue());
		}


		for( Map.Entry<String, Integer> entry : entries.entrySet()) {
			assertEquals(entry.getValue(), d.get(entry.getKey()));
		}
		
		assertNull(d.get("Not in dictionary"));
	}
}
