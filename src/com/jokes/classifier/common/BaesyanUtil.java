package com.jokes.classifier.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaesyanUtil {
	private final double defaultPositiveWordProbability;
	private final double defaultNegativeWordProbability;

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
		wordsProbabilityPos = new HashMap<>();
		wordsProbabilityNeg = new HashMap<>();
		wordsCountNeg = new HashMap<>();
		wordsCountPos = new HashMap<>();

		processWordProbabilities(jokes);
		defaultPositiveWordProbability = 1.0
				/ (vocabulary.getAlphabet().size() + totalPositiveWords);
		defaultNegativeWordProbability = 1.0
				/ (vocabulary.getAlphabet().size() + totalNegativeWords);

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
			double wordProbPos = (wordsCountPos.getOrDefault(word, 0) + 1) * 1.0
					/ (vocabularyCount + totalPositiveWords);
			double wordProbNeg = (wordsCountNeg.getOrDefault(word, 0) + 1) * 1.0
					/ (vocabularyCount + totalNegativeWords);

			// HACK(yasen): Take natural logarithm in order to avoid multiplication of
			// many small numbers which will tremendously influence the precision.
			wordsProbabilityPos.put(word, Math.log(wordProbPos));
			wordsProbabilityNeg.put(word, Math.log(wordProbNeg));
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

		System.out.println("+ probability: " + positiveProbability);
		System.out.println("- probability: " + negativeProbability);
	}

	public boolean isTheJokeGood(String jokeText) {
		boolean isGoodJoke = true;
		if (predictJokeRating(jokeText) < 0) {
			isGoodJoke = false;
		}
		return isGoodJoke;
	}

	/**
	 * Predicts a joke rating with a naive Bayes classification.
	 * The calculation of the positive/negative rating uses is done with
	 * summation of logarithms instead of product of small numbers in
	 * order to increase precision.
	 *
	 * @param jokeText The text of the joke we want to classify.
	 * @return negative if the joke is classified as bad, non-negative otherwise.
	 */
	public double predictJokeRating(String jokeText) {
		String[] jokeWords = jokeText.split(ClassifiedJoke.DELIMITERS);
		double positiveJokeProbability = Math.log(positiveProbability);
		double negativeJokeProbability = Math.log(negativeProbability);
		for (String word : jokeWords) {
			if (vocabulary.ignoreSmall() && word.length() < 3) {
				continue;
			}
			if (vocabulary.withStemming()) {
				word = vocabulary.stemWord(word);
			}

			if (wordsProbabilityPos.containsKey(word)) {
				positiveJokeProbability += wordsProbabilityPos.get(word);
			} else {
				positiveJokeProbability += Math.log(defaultPositiveWordProbability);
			}
			if (wordsProbabilityNeg.containsKey(word)) {
				negativeJokeProbability += wordsProbabilityNeg.get(word);
			} else {
				negativeJokeProbability += Math.log(defaultNegativeWordProbability);
			}
		}
		//System.out.println("+ " + positiveJokeProbability);
		//System.out.println("- " + negativeJokeProbability);
		return positiveJokeProbability - negativeJokeProbability;
	}
}
