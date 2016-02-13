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

/**
 * Establishes a connection with the jokes database and
 * executes queries in it.
 *
 * @author Yasen Trifonov
 */
public class JokeReader {
	/**
	 * Establishes a connection with the database and fetches jokes.
	 * @param idFrom The id of the first joke entity to fetch.
	 * @param idTo The id of the last (inclusive) entity to fetch.
	 * @return jokes An array of jokes.
	 */
	public static List<Joke> getJokes(int idFrom, int idTo) {
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
			ResultSet resultSet = statement.executeQuery(
					String.format(Constants.GET_JOKES_BETWEEN_IDS_QUERY, idFrom, idTo));

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

	/**
	 * @return The size of the database.
	 */
	public static int getDatabaseSize() {
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
			ResultSet resultSet = statement.executeQuery(Constants.GET_DATABASE_SIZE_QUERY);
			int size = 0;
			if (resultSet.next()) {
			    size = resultSet.getInt("cnt");
			}
			statement.close();
			connection.close();
			return size;
		} catch (SQLException e) {
		    throw new IllegalStateException("Cannot connect the database!", e);
		}
	}
}
