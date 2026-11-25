package al.sybina.ebr.rcp;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * PATCH (2026-11): JVM-wide handler for exceptions that escape every catch block.
 *
 * Two failures were silent in this application because nothing was listening:
 *  - the ORB save failure (generated setNull(Types.VARBINARY) against a BLOB column ->
 *    SQLException swallowed by Mithra, RuntimeException thrown on the JavaFX thread);
 *  - History Preview (getResourceAsStream("/version.properties") returned null ->
 *    NPE from prop.load(null), not covered by the surrounding catch(IOException)).
 * Both died as uncaught RuntimeExceptions from QuantumToolkit.runWithoutRenderLock with
 * only a stderr trace and no user-visible feedback.
 *
 * This handler always logs the full stack (stderr + JUL) and shows an error dialog on
 * the JavaFX thread, so a failed operation can never again look like a successful no-op.
 * Installed from Activator.start().
 */
public final class GlobalFxExceptionHandler
{
	private static final Logger LOG = Logger.getLogger(Activator.PLUGIN_ID);

	/** at most one error dialog at a time */
	private static final AtomicBoolean DIALOG_OPEN = new AtomicBoolean();
	/** spam guard: never show two dialogs closer than this (log is never suppressed) */
	private static final AtomicLong LAST_DIALOG_AT = new AtomicLong();
	private static final long MIN_MS_BETWEEN_DIALOGS = 2000;

	private GlobalFxExceptionHandler()
	{
	}

	public static void install()
	{
		Thread.setDefaultUncaughtExceptionHandler(GlobalFxExceptionHandler::handle);
		LOG.info("Global uncaught-exception handler installed (" + Activator.PLUGIN_ID + ").");
	}

	static void handle(Thread thread, Throwable error)
	{
		try
		{
			// 1) always log, regardless of anything else
			System.err.println("[UNCAUGHT] on thread '" + thread.getName() + "': " + error);
			error.printStackTrace();
			LOG.log(Level.SEVERE, "Uncaught exception on thread " + thread.getName(), error);

			// 2) some framework noise is not worth a dialog
			if (isBenign(error))
			{
				return;
			}

			// 3) rate/stack limiting so a broken loop cannot open a new window per event
			long now = System.currentTimeMillis();
			if (now - LAST_DIALOG_AT.get() < MIN_MS_BETWEEN_DIALOGS)
			{
				return;
			}
			if (!DIALOG_OPEN.compareAndSet(false, true))
			{
				return;
			}
			LAST_DIALOG_AT.set(now);
			try
			{
				Platform.runLater(() -> {
					try
					{
						showDialog(error);
					}
					finally
					{
						DIALOG_OPEN.set(false);
					}
				});
			}
			catch (IllegalStateException toolkitNotAvailable)
			{
				// FX toolkit not running (startup / shutdown / headless): console logging already done
				DIALOG_OPEN.set(false);
			}
		}
		catch (Throwable handlerFailure)
		{
			// never let the error reporter itself propagate
			DIALOG_OPEN.set(false);
			System.err.println("[UNCAUGHT-HANDLER] failed to report the error:");
			handlerFailure.printStackTrace();
		}
	}

	private static boolean isBenign(Throwable error)
	{
		if (error instanceof InterruptedException)
		{
			return true;
		}
		String msg = error.getMessage();
		if (error instanceof IllegalStateException && msg != null
				&& (msg.contains("Toolkit") || msg.contains("already exited")))
		{
			// FX shutdown races
			return true;
		}
		return false;
	}

	private static void showDialog(Throwable error)
	{
		Alert alert = new Alert(AlertType.ERROR,
				"The application hit an unexpected error while executing an action.\n"
				+ "The operation may not have completed, and the data shown may be out of date.\n"
				+ "The full stack trace was printed to the console log.");
		alert.setTitle("Unexpected application error");
		alert.setHeaderText(describe(error));

		TextArea stackTrace = new TextArea(stackTraceToString(error));
		stackTrace.setEditable(false);
		stackTrace.setWrapText(true);
		stackTrace.setPrefRowCount(14);

		GridPane details = new GridPane();
		details.add(new Label("Details:"), 1, 0);
		GridPane.setHgrow(stackTrace, Priority.ALWAYS);
		GridPane.setVgrow(stackTrace, Priority.ALWAYS);
		details.add(stackTrace, 1, 1);
		alert.getDialogPane().setExpandableContent(details);

		alert.showAndWait();
	}

	/** deepest cause first, e.g. "SQLException: ... (thrown as RuntimeException)" */
	private static String describe(Throwable error)
	{
		Throwable root = error;
		while (root.getCause() != null && root.getCause() != root)
		{
			root = root.getCause();
		}
		String text = root.getClass().getName();
		if (root.getMessage() != null)
		{
			text += ": " + root.getMessage();
		}
		if (root != error)
		{
			text += "  (thrown as " + error.getClass().getName() + ")";
		}
		return text;
	}

	private static String stackTraceToString(Throwable error)
	{
		StringWriter buffer = new StringWriter();
		try (PrintWriter out = new PrintWriter(buffer))
		{
			error.printStackTrace(out);
		}
		return buffer.toString();
	}
}
