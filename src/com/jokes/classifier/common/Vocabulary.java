package com.jokes.classifier.common;

import java.util.List;

public class Vocabulary {
	private static String DELIMITERS = "[\\s,?!:;.]+";
	private List<String> alphabet;

	public Vocabulary(List<Joke> jokes) {
		createAlphabet(jokes);
	}

	public List<String> getAlphabet() {
		return alphabet;
	}

	private void createAlphabet(List<Joke> jokes) {
		for (Joke joke : jokes) {
			addJokeWords(joke);
		}
	}

	private void addJokeWords(Joke joke) {
		String[] jokeWords = joke.getJokeText().split(DELIMITERS);
		for (String jokeWord : jokeWords) {
			if (jokeWord.length() < 3 && !alphabet.contains(jokeWord)) {
				alphabet.add(jokeWord);
			}
		}
	}
}
