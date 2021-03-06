package com.jokes.classifier.common;

import java.util.Locale;

/**
 * Class representing a single joke.
 *
 * @author Vasil Todorov
 */
public class Joke {
	private String jokeText;
	private int thumbsUp;
	private int thumbsDown;

	/**
	 * This is something specific for the online forum from which we got the data.
	 * Some posts start with this prefix to indicate that it was originally
	 * posted by someone else. We remove this prefix so that it doesn't affect the
	 * accuracy of the classifier.
	 */
	private static final String REPEATED_TEXT_PREFIX = "първоначално публикувано от ";

	public String getJokeText() {
		return jokeText;
	}

	public void setJokeText(String joke) {
		this.jokeText = preprocessJokeText(joke);
	}

	public int getThumbsUp() {
		return thumbsUp;
	}

	public void setThumbsUp(int thumbsUp) {
		this.thumbsUp = thumbsUp;
	}

	public int getThumbsDown() {
		return thumbsDown;
	}

	public void setThumbsDown(int thumbsDown) {
		this.thumbsDown = thumbsDown;
	}

	@Override
	public String toString() {
		return String.format("+%d -%d:\n%s\n", thumbsUp, thumbsDown, jokeText);
	}

	/**
	 * Removes upper case letters and an unnecessary prefix.
	 * @param text The joke text.
	 * @return The processed text of the joke.
	 */
	public static String preprocessJokeText(String text) {
		text = text.toLowerCase(new Locale("bg"));
		if (text.startsWith(REPEATED_TEXT_PREFIX)) {
			text = text.substring(REPEATED_TEXT_PREFIX.length());
		}
		return text;
	}
}
