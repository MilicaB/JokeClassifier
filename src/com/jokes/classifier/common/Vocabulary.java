package com.jokes.classifier.common;

import java.util.ArrayList;
import java.util.List;

import stemming.Stemmer;

public class Vocabulary {
	private static String DELIMITERS = "[\\s,?!:;.]+";
	private List<String> alphabet;

	private final boolean withStemming;
	private Stemmer stemmer = new Stemmer();

	public Vocabulary(List<Joke> jokes, boolean useStemming) {
		withStemming = useStemming;
		if (useStemming) {
			stemmer.loadAllRules();
		}
		//TODO(yasen): Maybe change this to a HashMap or something. We call contains on it.
		alphabet = new ArrayList<>();
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
			if (jokeWord.length() < 3) {
				continue;
			}
            if (!alphabet.contains(jokeWord)) {
				if (withStemming) {
					jokeWord = stemmer.stem(jokeWord);
				}
				alphabet.add(jokeWord);
			}
		}
	}

	public String stemWord(String word) {
		return stemmer.stem(word);
	}

	public boolean withStemming() {
		return withStemming;
	}
}
