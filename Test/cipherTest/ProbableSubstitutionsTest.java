package cipherTest;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

import cipher.Mapping;
import cipher.NGramAnalyser;
import cipher.ProbableSubstitutions;
import cipher.Substitution;
import cipher.Utilities;

public class ProbableSubstitutionsTest {

	private Substitution s = new Substitution();
	private Utilities u = new Utilities();
	private NGramAnalyser n = new NGramAnalyser(u);
	private ProbableSubstitutions tester = new ProbableSubstitutions();
	private final Mapping[] MAPPINGS = SubstitutionTest.initialiseMappings("zyxwvutsrqponmlkjihgfedcba");
	private final String PLAINTEXT = "One stop brought us into the family sitting-room, without any introductory lobby"
			+ " or passage: they call it here ‘the house’ pre-eminently.  It includes kitchen and parlour,"
			+ " generally; but I believe at Wuthering Heights the kitchen is forced to retreat altogether"
			+ " into another quarter: at least I distinguished a chatter of tongues, and a clatter of"
			+ " culinary utensils, deep within; and I observed no signs of roasting, boiling, or baking,"
			+ " about the huge fireplace; nor any glitter of copper saucepans and tin cullenders on the"
			+ " walls.  One end, indeed, reflected splendidly both light and heat from ranks of immense"
			+ " pewter dishes, interspersed with silver jugs and tankards, towering row after row, on a"
			+ " vast oak dresser, to the very roof.  The latter had never been under-drawn: its entire"
			+ " anatomy lay bare to an inquiring eye, except where a frame of wood laden with oatcakes"
			+ " and clusters of legs of beef, mutton, and ham, concealed it.  Above the chimney were"
			+ " sundry villainous old guns, and a couple of horse-pistols: and, by way of ornament,"
			+ " three gaudily-painted canisters disposed along its ledge.  The floor was of smooth, white"
			+ " stone; the chairs, high-backed, primitive structures, painted green: one or two heavy"
			+ " black ones lurking in the shade.  In an arch under the dresser reposed a huge,"
			+ " liver-coloured bitch pointer, surrounded by a swarm of squealing puppies; and other dogs"
			+ " haunted other recesses.";
	// Wuthering Heights - Emily Bronte
	String encrypted = s.encrypt(u.cleanText(PLAINTEXT), MAPPINGS);

	@Test
	public void testProbableSubstitutionGenerator() {
		Mapping[] probable = tester.probableSubstitutionGenerator(n.NgramAnalysis(1, encrypted, true));
		List<Mapping> probableList = Arrays.asList(probable);
		List<Mapping> mappingsList = Arrays.asList(MAPPINGS);
		int counter = 0;
		for (Mapping m : probableList) {
			for (Mapping n : mappingsList) {
				if (m.getCipherChar() == n.getCipherChar() && m.getPlainChar() == n.getPlainChar()) {
					counter++;
				}
			}
		}
		assertTrue(counter > 10);
	}

	@Test
	public void testMaxKey() {
		Map<String, Float> newMap = new TreeMap<String, Float>();
		newMap.put("1", 1.0f);
		newMap.put("2", 2.0f);
		assertEquals(tester.maxKey(newMap), "2");
	}

}
