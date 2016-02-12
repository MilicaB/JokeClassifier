package com.jokes.classifier.common;

import java.util.List;
import java.util.Map;

public class BaesyanUtil {
	private static String DELIMITERS = "[\\s,?!:;.]+";
	private static double DEFAULT_PROBABILITY = 0.001;

	private final Vocabulary vocabulary;
	private Map<String, Double> wordsProbabilityPos;
	private Map<String, Double> wordsProbabilityNeg;
	private Map<String, Integer> wordsCountPos;
	private Map<String, Integer> wordsCountNeg;
	private int totalPositiveWords = 0;
	private int totalNegativeWords = 0;
	private double positiveProbability;
	private double negativeProbability;

	public BaesyanUtil(List<ClassifiedJoke> jokes, Vocabulary vocab) {
		vocabulary = vocab;
		processWordProbabilities(jokes);
		getProbabilities(jokes);
	}

	private void processWordProbabilities(List<ClassifiedJoke> jokes) {
		countPositiveAndNegativeWords(jokes);
		calculateWordProbabilities();
	}

	/**
	 * Finds how many times a given word has been used in a positive/negative joke.
	 * TODO(yasen) maybe separate in two - countPositive and countNegative.
	 *
	 * @param jokes List of jokes.
	 * Also calculates:
	 *   1) positiveWordsCount -- Total amount of words found in positive jokes.
	 *   2) negativeWordsCount -- Total amount of words found in negative jokes.
	 */
	private void countPositiveAndNegativeWords(List<ClassifiedJoke> jokes) {
		for (ClassifiedJoke joke : jokes) {
			for (String word : joke.getWordPartition().keySet()) {
				int wordCount = joke.getWordPartition().get(word);
				if (joke.getRating() < 0) {
					totalNegativeWords += wordCount;
					if (wordsCountNeg.keySet().contains(word)) {
						wordCount += wordsCountNeg.get(word);
					}
					wordsCountNeg.put(word, wordCount);
				} else {
					totalPositiveWords += wordCount;
					if (wordsCountPos.keySet().contains(word)) {
						wordCount += wordsCountPos.get(word);
					}
					wordsCountPos.put(word, wordCount);
				}
			}
		}
	}
	
	/**
	 * Calculates the positive and negative probabilities for all words in the vocabulary.
	 * More formally: P(word | +) and P(word | -).
	 *
	 * The positive probabilities are set in wordsProbabilityPos,
	 * while the negative ones in wordsProbabilityNeg.
	 */
	private void calculateWordProbabilities() {
		int vocabularyCount = vocabulary.getAlphabet().size();

		for (String word : vocabulary.getAlphabet()) {
			double wordProbPos = (wordsCountPos.get(word) + 1) * 1.0
					/ (vocabularyCount + totalPositiveWords);
			double wordProbNeg = (wordsCountNeg.get(word) + 1) * 1.0
					/ (vocabularyCount + totalNegativeWords);
			wordsProbabilityPos.put(word, wordProbPos);
			wordsProbabilityNeg.put(word, wordProbNeg);
		}
	}
	
	private void getProbabilities(List<ClassifiedJoke> jokes) {
		int jokesCount = jokes.size();
		int goodJokes = 0;
		int badJokes = 0;
		for (ClassifiedJoke joke : jokes) {
			if (joke.getRating() < 0) {
				badJokes++;
			} else {
				goodJokes++;
			}
		}
		positiveProbability = goodJokes * 1.0 / jokesCount;
		negativeProbability = badJokes * 1.0 / jokesCount;
	}

	public boolean isTheJokeGood(String jokeText) {
		boolean isGoodJoke = true;
		if (predictJokeRating(jokeText) < 0) {
			isGoodJoke = false;
		}
		return isGoodJoke;
	}

	// TODO(yasen): Use logarithm addition istead of simple multiplication (loses precision).
	public double predictJokeRating(String jokeText) {
		String[] jokeWords = jokeText.split(DELIMITERS);
		double positiveJokeProbability = positiveProbability;
		double negativeJokeProbability = negativeProbability;
		for (String word : jokeWords) {
			if (vocabulary.withStemming()) {
				word = vocabulary.stemWord(word);
			}

			if (wordsProbabilityPos.containsKey(word)) {
				positiveJokeProbability *= wordsProbabilityPos.get(word);
			} else {
				positiveJokeProbability *= DEFAULT_PROBABILITY;
			}
			if (wordsProbabilityNeg.containsKey(word)) {
				negativeJokeProbability *= wordsProbabilityNeg.get(word);
			} else {
				positiveJokeProbability *= DEFAULT_PROBABILITY;
			}
		}
		return positiveJokeProbability - negativeJokeProbability;
	}
}
