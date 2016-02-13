package com.jokes.classifier.common;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * Represents a joke that is already classified.
 *
 * @author Milica Borisova
 */
public class ClassifiedJoke {
	/**
	 * What are the word delimiters.
	 */
	public static String DELIMITERS = "[A-Za-z0-9–=„\\s,?!:;._()\"”“*…'’#@/-]+";
	private Map<String, Integer> wordPartition;
	private int rating;

	/**
	 *  As the data is from an online forum, around half of the jokes have
	 *  rating thumbs_up - thumbs_down > 27. Take this into account as inflation.
	 */
	public static final int RATING_INFLATION = 27;

	/**
	 * Processes a joke with known rating. In addition, it extracts all its words along with
	 * their frequencies in the wordPartition Map. It may use stemming and it may ingore the
	 * words with length < 3 depending on the booleans ignoreSmall and withStemming from the
	 * vocabulary.
	 *
	 * @param joke The current classified joke.
	 * @param vocabulary Vocabulary containing all the words contained in a
	 * joke from the training set.
	 */
	public ClassifiedJoke(Joke joke, Vocabulary vocabulary) {
		LinkedHashSet<String> words = vocabulary.getAlphabet();
		wordPartition = new HashMap<String, Integer>();
		String[] jokeWords = joke.getJokeText().split(DELIMITERS);
		for (String jokeWord : jokeWords) {
			if (vocabulary.ignoreSmall() && jokeWord.length() < 3) {
				continue;
			}
			if (vocabulary.withStemming()) {
				jokeWord = vocabulary.stemWord(jokeWord);
			}

			if (words.contains(jokeWord)) {
				int numberOccurences = 0;
				if (wordPartition.keySet().contains(jokeWord)) {
					numberOccurences = wordPartition.get(jokeWord);
				}
				wordPartition.put(jokeWord, numberOccurences + 1);
			}
		}
		rating = joke.getThumbsUp() - joke.getThumbsDown() - RATING_INFLATION;
	}

	public Map<String, Integer> getWordPartition() {
		return wordPartition;
	}

	public int getRating() {
		return rating;
	}
}
