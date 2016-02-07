package com.jokes.classifier;

import java.util.List;

import com.jokes.classifier.common.Joke;

import db.JokeReader;

public class ClassifierMain {

	public static void main(String args[]) {
		List<Joke> jokes = JokeReader.getJokes();
		Classifier classifier = new Classifier(jokes);
		String joke = "������ ������������ �����. "
				+ "��� ���������� �� ���� �� ������� �� ��������� ������. "
				+ "������� ������, �� ��� ����� �� ������� �� ����, "
				+ "������ ��� ������� �� ���������� ����� ��� ��������� ����. "
				+ "����������� ���� ����, �� �� �� ���������, ���� �� �����, "
				+ "���� ������� ����.�� ������ �� ������ ���������� ����� � ���� �����."
				+ "- ��������� �� ������� ������ �������� - ����� �������."
				+ "- ���� �� �� �� ��������� � �� ������ ������ �� �� �� �� �������... ";
		System.out.println(classifier.isJokeGood(joke));
	}
}
