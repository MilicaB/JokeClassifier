package db;

import static db.Constants.JDBC_URL;
import static db.Constants.PASS;
import static db.Constants.USER;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import com.jokes.classifier.common.Joke;

public class JokeReader {
	public static List<Joke> getJokes() {
		List<Joke> jokes = new LinkedList<Joke>();
		Connection connection = null;
		Statement statement = null;

		try {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection(JDBC_URL, USER, PASS);
			} catch (ClassNotFoundException e) {
				System.out.println("No JDBC driver found.");
				e.printStackTrace();
			}
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM jokes;");

			while (resultSet.next()) {
				Joke currentJoke = new Joke();
				currentJoke.setThumbsUp(resultSet.getInt("thumbs_up"));
				currentJoke.setThumbsDown(resultSet.getInt("thumbs_down"));
				currentJoke.setJokeText(resultSet.getString("text"));
				jokes.add(currentJoke);
			}

			statement.close();
			connection.close();
		} catch (SQLException e) {
		    throw new IllegalStateException("Cannot connect the database!", e);
		}

		return jokes;
	}
}
