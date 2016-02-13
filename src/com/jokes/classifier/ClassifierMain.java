package com.jokes.classifier;

import java.util.Arrays;
import java.util.List;

import com.jokes.classifier.common.Joke;

import db.JokeReader;

public class ClassifierMain {
	private static int TOTAL_SIZE = 0;

	public static void main(String args[]) {
		TOTAL_SIZE = JokeReader.getDatabaseSize();
		System.out.println(TOTAL_SIZE);
		List<Integer> testSetSizes = Arrays
				.asList(50, 100, 200, 300, 400, 500, 750, 1000, 1500, 2000, 2500);
		for (Integer size : testSetSizes) {
			tryCurrentSet(size, size);
		}
	}

	public static void tryCurrentSet(int firstTestSetSize, int secondTestSetSize) {
		List<Joke> firstTestSet = JokeReader.getJokes(1, firstTestSetSize);
		List<Joke> secondTestSet = JokeReader.getJokes(firstTestSetSize + 1,
				firstTestSetSize + secondTestSetSize);

		// All the other jokes will be in the training set.
		List<Joke> trainingSet = JokeReader.getJokes(
				firstTestSetSize + secondTestSetSize + 1, TOTAL_SIZE);

		Classifier classifier00 = new Classifier(trainingSet, false, false);
		classifier00.classifySet(firstTestSet);
		classifier00.classifySet(secondTestSet);

		Classifier classifier01 = new Classifier(trainingSet, false, true);
		classifier01.classifySet(firstTestSet);
		classifier01.classifySet(secondTestSet);

		Classifier classifier10 = new Classifier(trainingSet, true, false);
		classifier10.classifySet(firstTestSet);
		classifier10.classifySet(secondTestSet);

		Classifier classifier11 = new Classifier(trainingSet, true, true);
		classifier11.classifySet(firstTestSet);
		classifier11.classifySet(secondTestSet);
	}
}
