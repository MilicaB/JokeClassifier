package com.jokes.classifier.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassifiedJoke {
	private static String DELIMITERS = "[\\s,?!:;.-+]+";
	private Map<String, Integer> wordPartition;
	private int rating;

	// As the data is from an online forum, around half of the jokes have
	// rating thumbs_up - thumbs_down > 27. Take this into account as inflation.
	private int RATING_INFLATION = 27;

	public ClassifiedJoke(Joke joke, Vocabulary vocabulary) {
		List<String> words = vocabulary.getAlphabet();
		wordPartition = new HashMap<String, Integer>();
		String[] jokeWords = joke.getJokeText().split(DELIMITERS);
		for (String jokeWord : jokeWords) {
			if (jokeWord.length() < 3 && words.contains(jokeWord)) {
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
