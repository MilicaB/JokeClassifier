package db;

/**
 * Database-specific constants.
 *
 * @author Yasen Trifonov
 */
public class Constants {
	public static final String JDBC_URL = "jdbc:mysql://localhost:3306/jokes";
	public static final String USER = "java";
	public static final String PASS = "password";

	// SQL queries.
	public static final String GET_JOKES_BETWEEN_IDS_QUERY =
			"SELECT * FROM jokes WHERE id BETWEEN %d AND %d;";
	public static final String GET_DATABASE_SIZE_QUERY =
			"SELECT COUNT(*) AS cnt FROM jokes;";
}
