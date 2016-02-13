package com.jokes.classifier.common;

import java.util.Locale;

/**
 * Class representing a single joke.
 */
public class Joke {
	private String jokeText;
	private int thumbsUp;
	private int thumbsDown;

	private static final String REPEATED_TEXT_PREFIX = "първоначално публикувано от ";

	public String getJokeText() {
		return jokeText;
	}

	public void setJokeText(String joke) {
		this.jokeText = joke;
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
	 * Removes uppercase letters and an uneccessary preffix.
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
