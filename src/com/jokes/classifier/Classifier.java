package com.jokes.classifier;

import java.util.LinkedList;
import java.util.List;

import com.jokes.classifier.common.BaesyanUtil;
import com.jokes.classifier.common.ClassifiedJoke;
import com.jokes.classifier.common.Joke;
import com.jokes.classifier.common.Vocabulary;

public class Classifier {
	private BaesyanUtil baesyanUtil;

	public Classifier(List<Joke> jokes) {
		Vocabulary vocabulary = new Vocabulary(jokes);
		List<ClassifiedJoke> classifiedJokes = new LinkedList<ClassifiedJoke>();
		for (Joke joke : jokes) {
			classifiedJokes.add(new ClassifiedJoke(joke, vocabulary));
		}
		baesyanUtil = new BaesyanUtil(classifiedJokes);
	}

	public double getJokeRating(String jokeText) {
		return baesyanUtil.getJokeRating(jokeText);
	}

	public boolean isJokeGood(String jokeText) {
		return baesyanUtil.isTheJokeGood(jokeText);
	}
}
