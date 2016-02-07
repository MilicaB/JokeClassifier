package com.jokes.classifier;

import java.util.List;

import com.jokes.classifier.common.Joke;

import db.JokeReader;

public class ClassifierMain {

	public static void main(String args[]) {
		List<Joke> jokes = JokeReader.getJokes();
		Classifier classifier = new Classifier(jokes);
		String joke = "Потъва презокеански кораб. "
				+ "Наш сънародник по чудо се спасява на необитаем остров. "
				+ "Минават години, но той някак си оцелява до деня, "
				+ "когато към острова се приближава кораб под български флаг. "
				+ "Нещастникът пали огън, за да го забележат, тича по брега, "
				+ "маха отчаяно ръце.От кораба се отделя спасителна лодка с един моряк."
				+ "- Капитанът ви изпраща новите вестници - казва морякът."
				+ "- Моли ви да ги прочетете и да решите струва ли си да се връщате... ";
		System.out.println(classifier.isJokeGood(joke));
	}
}
