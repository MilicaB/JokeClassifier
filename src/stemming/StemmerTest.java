package stemming;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StemmerTest {

	@Test
	public void test() {
		String text = "ученик учениците ученика учените";
		String[] words = text.split(" ");
		Stemmer stemmer = new Stemmer();
		stemmer.loadAllRules();

		String stemmedText = "";
		for (String word : words) {
		    String stemmedWord = stemmer.stem(word);
		    stemmedText = stemmedText + stemmedWord + " ";
		}

		String expectedText = "учени учени учени учени ";
		assertEquals(expectedText, stemmedText);
	}
}
