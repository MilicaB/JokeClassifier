package com.jokes.classifier.common;

/**
 * Class representing a single joke
 */
public class Joke {
	private String jokeText;
	private int thumbsUp;
	private int thumbsDown;

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
}
