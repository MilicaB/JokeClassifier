package com.jokes.classifier;

import java.util.LinkedList;
import java.util.List;

import com.jokes.classifier.common.BaesyanUtil;
import com.jokes.classifier.common.ClassifiedJoke;
import com.jokes.classifier.common.Joke;
import com.jokes.classifier.common.Vocabulary;

/**
 * Classifies each joke from a set of jokes.
 *
 * @author Milica Borisova
 */
public class Classifier {
	private BaesyanUtil baesyanUtil;
	private boolean withStemming = true;
	private boolean ignoreSmallWords = true;
	private StatsCollector stats;

	public Classifier(List<Joke> jokes, boolean useStemming, boolean ignoreSmall,
			StatsCollector collector) {
		withStemming = useStemming;
		ignoreSmallWords = ignoreSmall;
		stats = collector;

		// Extract all words from the current set of jokes and
		// put them in the vocabulary.
		Vocabulary vocabulary = new Vocabulary(jokes, withStemming, ignoreSmallWords);
		List<ClassifiedJoke> classifiedJokes = new LinkedList<ClassifiedJoke>();
		for (Joke joke : jokes) {
			classifiedJokes.add(new ClassifiedJoke(joke, vocabulary));
		}
		System.out.println(String.format("Learning a set of %d jokes.", jokes.size()));
		baesyanUtil = new BaesyanUtil(classifiedJokes, vocabulary);
	}

	public double getJokeRating(String jokeText) {
		return baesyanUtil.predictJokeRating(jokeText);
	}

	public boolean isJokeGood(String jokeText) {
		return baesyanUtil.isTheJokeGood(jokeText);
	}

	public void classifySet(List<Joke> testJokes) {
		int correct = 0;
		int goodJokeAsbad = 0;
		int badJokeAsGood = 0;
		int total = testJokes.size();
		System.out.println(String.format("%d %b %b", total, withStemming, ignoreSmallWords));

		for (Joke joke : testJokes) {
			boolean realRating = (joke.getThumbsUp() - joke.getThumbsDown()
					- ClassifiedJoke.RATING_INFLATION) >= 0;
			boolean predictedRating = isJokeGood(joke.getJokeText());
			if (realRating == predictedRating) {
				correct ++;
			} else {
				if (realRating) {
					goodJokeAsbad ++;
				} else {
					badJokeAsGood ++;
				}
			}
		}
		double accuracy = correct * 1.0 / total;
		System.out.println("Accuracy: " + accuracy);
		stats.addAccuracyEntry(accuracy);
		System.out.println("Total correct: " + correct);
		System.out.println("Bad jokes found as good: " + badJokeAsGood);
		stats.addBadAsGoodEntry(badJokeAsGood * 1.0 / total);
		System.out.println("Good jokes found as bad: " + goodJokeAsbad);
		stats.addGoodAsBadEntry(goodJokeAsbad * 1.0 / total);
	}
}
