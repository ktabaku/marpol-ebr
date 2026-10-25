package al.sybina.ebr.commons.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Database credentials for the bundled Derby database.
 * <p>
 * The values are read from {@code database.properties}, which ships next to
 * this class in the bundle. To use different credentials, point the
 * {@value #CONFIG_FILE_PROPERTY} system property at another properties file.
 *
 * <pre>
 * database.user=root
 * database.password=...
 * database.bootPassword=...
 * </pre>
 */
public final class DatabaseCredentials {

	/** System property naming a properties file that replaces the bundled one. */
	public static final String CONFIG_FILE_PROPERTY = "sybina.database.config";

	/** Name of the bundled properties file, next to this class. */
	public static final String BUNDLED_FILE = "database.properties";

	private static final String USER = "database.user";
	private static final String PASSWORD = "database.password";
	private static final String BOOT_PASSWORD = "database.bootPassword";

	private static Properties properties;

	private DatabaseCredentials() {
	}

	/** Database user name; defaults to {@code root}. */
	public static String user() {
		String user = load().getProperty(USER);
		return user == null || user.trim().isEmpty() ? "root" : user.trim();
	}

	/** Password of the database user. */
	public static String password() {
		return required(PASSWORD);
	}

	/** Boot password (encryption key) of the encrypted database. */
	public static String bootPassword() {
		return required(BOOT_PASSWORD);
	}

	/** Where the credentials are read from, for messages. */
	public static String source() {
		Path override = overrideFile();
		return override != null ? override.toString() : BUNDLED_FILE + " in " + DatabaseCredentials.class.getPackage().getName();
	}

	private static Path overrideFile() {
		String override = System.getProperty(CONFIG_FILE_PROPERTY);
		return override == null || override.trim().isEmpty() ? null : Paths.get(override.trim());
	}

	private static String required(String key) {
		String value = load().getProperty(key);
		if (value == null || value.isEmpty()) {
			throw new IllegalStateException("Missing '" + key + "' in " + source() + ".");
		}
		return value;
	}

	private static synchronized Properties load() {
		if (properties == null) {
			Properties loaded = new Properties();
			Path override = overrideFile();
			try (InputStream in = override != null ? Files.newInputStream(override)
					: DatabaseCredentials.class.getResourceAsStream(BUNDLED_FILE)) {
				if (in != null) {
					loaded.load(in);
				}
			} catch (IOException e) {
				throw new IllegalStateException("Cannot read database credentials from " + source(), e);
			}
			properties = loaded;
		}
		return properties;
	}
}
