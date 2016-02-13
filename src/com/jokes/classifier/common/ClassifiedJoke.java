package com.jokes.classifier.common;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class ClassifiedJoke {
	public static String DELIMITERS = "[A-Za-z0-9–=„\\s,?!:;._()\"”“*…'’#@/-]+";
	private Map<String, Integer> wordPartition;
	private int rating;

	// As the data is from an online forum, around half of the jokes have
	// rating thumbs_up - thumbs_down > 27. Take this into account as inflation.
	public static final int RATING_INFLATION = 10;

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
