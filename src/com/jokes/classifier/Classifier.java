package com.jokes.classifier;

import java.util.LinkedList;
import java.util.List;

import com.jokes.classifier.common.BaesyanUtil;
import com.jokes.classifier.common.ClassifiedJoke;
import com.jokes.classifier.common.Joke;
import com.jokes.classifier.common.Vocabulary;

public class Classifier {
	private BaesyanUtil baesyanUtil;
	private final static boolean WITH_STEMMING = true;

	public Classifier(List<Joke> jokes) {
		Vocabulary vocabulary = new Vocabulary(jokes, WITH_STEMMING);
		List<ClassifiedJoke> classifiedJokes = new LinkedList<ClassifiedJoke>();
		for (Joke joke : jokes) {
			classifiedJokes.add(new ClassifiedJoke(joke, vocabulary));
		}
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
		int total = testJokes.size();
		System.out.println(String.format("Classifying a set of %d jokes.", total));

		for (Joke joke : testJokes) {
			boolean realRating = (joke.getThumbsUp() - joke.getThumbsDown()
					- ClassifiedJoke.RATING_INFLATION) >= 0;
			boolean predictedRating = isJokeGood(joke.getJokeText());
			if (realRating == predictedRating) {
				correct ++;
			}
		}
		double accuracy = correct * 1.0 / total;
		System.out.println("Accuracy: " + accuracy);
	}
}
