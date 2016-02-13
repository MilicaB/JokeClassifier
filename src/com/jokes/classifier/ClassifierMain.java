package com.jokes.classifier;

import java.util.List;

import com.jokes.classifier.common.Joke;

import db.JokeReader;

public class ClassifierMain {
	private static int FIRST_TEST_SET_SIZE = 0;
	private static int SECOND_TEST_SET_SIZE = 0;
	private static int TOTAL_SIZE = 0;

	public static void main(String args[]) {
		FIRST_TEST_SET_SIZE = 1000;
		SECOND_TEST_SET_SIZE = 1000;
		TOTAL_SIZE = JokeReader.getDatabaseSize();
		System.out.println(TOTAL_SIZE);
		List<Joke> firstTestSet = JokeReader.getJokes(1, FIRST_TEST_SET_SIZE);
		List<Joke> secondTestSet = JokeReader.getJokes(FIRST_TEST_SET_SIZE + 1,
				FIRST_TEST_SET_SIZE + SECOND_TEST_SET_SIZE);

		// All the other jokes will be in the training set.
		List<Joke> trainingSet = JokeReader.getJokes(
				FIRST_TEST_SET_SIZE + SECOND_TEST_SET_SIZE + 1, TOTAL_SIZE);
		Classifier classifier = new Classifier(trainingSet);

		classifier.classifySet(firstTestSet);
		classifier.classifySet(secondTestSet);
	}
}
