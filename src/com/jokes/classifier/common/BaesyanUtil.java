package com.jokes.classifier.common;

import java.util.List;
import java.util.Map;

public class BaesyanUtil {
	private static String DELIMITERS = "[\\s,?!:;.]+";
	private static double DEFAULT_PROBABILITY = 0.001;

	private Map<String, Double> wordsProbabilityPos;
	private Map<String, Double> wordsProbabilityNeg;
	private Map<String, Integer> wordsCountPos;
	private Map<String, Integer> wordsCountNeg;
	private double positiveProbability;
	private double negativeProbability;

	public BaesyanUtil(List<ClassifiedJoke> jokes) {
		getWordProbabilities(jokes);
		getProbabilities(jokes);
	}

	private void getWordProbabilities(List<ClassifiedJoke> jokes) {
		int positiveWordsCount = 0;
		int negativeWordsCount = 0;
		int vocabularyCount = jokes.get(0).getWordPartition().keySet().size();
		countPositiveAndNegativeWords(positiveWordsCount, negativeWordsCount,
				jokes);
		for (String word : jokes.get(0).getWordPartition().keySet()) {
			double wordProbPos = (wordsCountPos.get(word) + 1)
					/ (vocabularyCount + positiveWordsCount);
			double wordProbNeg = (wordsCountNeg.get(word) + 1)
					/ (vocabularyCount + negativeWordsCount);
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
		positiveProbability = goodJokes / jokesCount;
		negativeProbability = badJokes / jokesCount;
	}

	private void countPositiveAndNegativeWords(int positiveWordsCount,
			int negativeWordsCount, List<ClassifiedJoke> jokes) {
		for (ClassifiedJoke joke : jokes) {
			for (String word : joke.getWordPartition().keySet()) {
				int wordCount = 0;
				if (joke.getRating() < 0) {
					negativeWordsCount += joke.getWordPartition().get(word);
					if (wordsCountNeg.keySet().contains(word)) {
						wordCount = wordsCountNeg.get(word);
					}
					wordsCountPos.put(word, wordCount + 1);
				} else {
					positiveWordsCount += joke.getWordPartition().get(word);
					if (wordsCountPos.keySet().contains(word)) {
						wordCount = wordsCountPos.get(word);
					}
					wordsCountPos.put(word, wordCount + 1);
				}
			}
		}

	}

	public Map<String, Double> getWordsProbabilityPos() {
		return wordsProbabilityPos;
	}

	public Map<String, Double> getWordsProbabilityNeg() {
		return wordsProbabilityNeg;
	}

	public double getPositiveProbability() {
		return positiveProbability;
	}

	public double getNegativeProbability() {
		return negativeProbability;
	}

	public boolean isTheJokeGood(String jokeText) {
		boolean isGoodJoke = true;
		if (getJokeRating(jokeText) < 0) {
			isGoodJoke = false;
		}
		return isGoodJoke;
	}

	public double getJokeRating(String jokeText) {
		String[] jokeWords = jokeText.split(DELIMITERS);
		double positiveJokeProbability = positiveProbability;
		double negativeJokeProbability = negativeProbability;
		for (String word : jokeWords) {
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
