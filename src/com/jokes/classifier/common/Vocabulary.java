package com.jokes.classifier.common;

import java.util.LinkedHashSet;
import java.util.List;

import stemming.Stemmer;

/**
 * Represents a vocabulary of all the words
 * contained in a joke from the training set.
 *
 * @author Milica Borisova
 */
public class Vocabulary {
	private final LinkedHashSet<String> alphabet;

	private final boolean withStemming;
	private final boolean ignoreSmallWords;
	private Stemmer stemmer = new Stemmer();

	public Vocabulary(List<Joke> jokes, boolean useStemming, boolean shouldIgnoreSmall) {
		withStemming = useStemming;
		ignoreSmallWords = shouldIgnoreSmall;
		if (useStemming) {
			stemmer.loadAllRules();
		}

		alphabet = new LinkedHashSet<String>();
		createAlphabet(jokes);
	}

	public LinkedHashSet<String> getAlphabet() {
		return alphabet;
	}

	private void createAlphabet(List<Joke> jokes) {
		for (Joke joke : jokes) {
			addJokeWords(joke);
		}
	}

	private void addJokeWords(Joke joke) {
		String[] jokeWords = joke.getJokeText().split(ClassifiedJoke.DELIMITERS);
		for (String jokeWord : jokeWords) {
			if (ignoreSmallWords && jokeWord.length() < 3) {
				continue;
			}
            if (!alphabet.contains(jokeWord)) {
				if (withStemming) {
					jokeWord = stemmer.stem(jokeWord);
				}
				alphabet.add(jokeWord);
				//System.out.println(jokeWord);
			}
		}
		//System.out.println("Total vocabulary size: " + alphabet.size());
	}

	public String stemWord(String word) {
		return stemmer.stem(word);
	}

	public boolean ignoreSmall() {
		return ignoreSmallWords;
	}

	public boolean withStemming() {
		return withStemming;
	}
}
