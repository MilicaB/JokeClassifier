package com.jokes.classifier;

import java.util.ArrayList;
import java.util.List;

public class StatsCollector {
	private static List<Double> accuracies = new ArrayList<>();
	private static List<Double> goodAsBad = new ArrayList<>();
	private static List<Double> badAsGood = new ArrayList<>();

	public void addAccuracyEntry(double entry) {
		accuracies.add(entry);
	}

	public void addGoodAsBadEntry(double entry) {
		goodAsBad.add(entry);
	}

	public void addBadAsGoodEntry(double entry) {
		badAsGood.add(entry);
	}

	public void printAllStats() {
		System.out.println(accuracies);
		System.out.println(goodAsBad);
		System.out.println(badAsGood);
	}
}
