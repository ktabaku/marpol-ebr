package al.sybina.ebr.dist;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

/**
 * Writes a .tar.gz download for Linux or macOS. The build may run on Windows, whose file system has
 * neither executable bits nor symbolic links, so:
 * <ul>
 * <li>files of a directory get mode 644, or 755 when listed as executable;</li>
 * <li>entries of another .tar.gz (the Java runtime) are copied with their own modes and links.</li>
 * </ul>
 * Arguments: the output file followed by any number of
 * <pre>
 * --dir  &lt;directory&gt; &lt;prefix&gt; &lt;executables&gt;   executables: comma separated paths, a path ending in / covers a folder, - for none
 * --tar  &lt;archive.tar.gz&gt; &lt;strip&gt; &lt;prefix&gt;      entries starting with strip are copied below prefix, the others are skipped
 * --file &lt;file&gt; &lt;entry name&gt;
 * </pre>
 */
public class DistArchive {

	private static final int FILE = 0100644;
	private static final int EXECUTABLE = 0100755;
	private static final int FOLDER = 040755;

	public static void main(String[] args) throws IOException {
		File output = new File(args[0]);
		output.getParentFile().mkdirs();
		try (TarArchiveOutputStream tar = new TarArchiveOutputStream(
				new GzipCompressorOutputStream(new BufferedOutputStream(new FileOutputStream(output))))) {
			tar.setLongFileMode(TarArchiveOutputStream.LONGFILE_POSIX);
			tar.setBigNumberMode(TarArchiveOutputStream.BIGNUMBER_POSIX);
			for (int i = 1; i < args.length;) {
				switch (args[i]) {
				case "--dir":
					addDirectory(tar, new File(args[i + 1]), args[i + 2], executables(args[i + 3]));
					i += 4;
					break;
				case "--tar":
					addArchive(tar, new File(args[i + 1]), args[i + 2], args[i + 3]);
					i += 4;
					break;
				case "--file":
					addFile(tar, new File(args[i + 1]), args[i + 2], FILE);
					i += 3;
					break;
				default:
					throw new IllegalArgumentException("Unknown option " + args[i]);
				}
			}
		}
		System.out.println("Wrote " + output);
	}

	private static List<String> executables(String list) {
		return list.equals("-") ? new ArrayList<String>() : Arrays.asList(list.split(","));
	}

	private static void addDirectory(TarArchiveOutputStream tar, File directory, String prefix,
			List<String> executables) throws IOException {
		addFolderEntry(tar, prefix);
		addChildren(tar, directory, "", prefix, executables);
	}

	private static void addChildren(TarArchiveOutputStream tar, File directory, String path, String prefix,
			List<String> executables) throws IOException {
		File[] children = directory.listFiles();
		if (children == null) {
			throw new IOException("Cannot read " + directory);
		}
		Arrays.sort(children);
		for (File child : children) {
			String childPath = path + child.getName();
			if (child.isDirectory()) {
				addFolderEntry(tar, prefix + "/" + childPath);
				addChildren(tar, child, childPath + "/", prefix, executables);
			} else {
				addFile(tar, child, prefix + "/" + childPath, isExecutable(childPath, executables) ? EXECUTABLE : FILE);
			}
		}
	}

	private static boolean isExecutable(String path, List<String> executables) {
		for (String executable : executables) {
			if (executable.endsWith("/") ? path.startsWith(executable) : path.equals(executable)) {
				return true;
			}
		}
		return false;
	}

	private static void addFolderEntry(TarArchiveOutputStream tar, String name) throws IOException {
		TarArchiveEntry entry = new TarArchiveEntry(name + "/");
		entry.setMode(FOLDER);
		tar.putArchiveEntry(entry);
		tar.closeArchiveEntry();
	}

	private static void addFile(TarArchiveOutputStream tar, File file, String name, int mode) throws IOException {
		TarArchiveEntry entry = new TarArchiveEntry(file, name);
		entry.setMode(mode);
		tar.putArchiveEntry(entry);
		Files.copy(file.toPath(), tar);
		tar.closeArchiveEntry();
	}

	private static void addArchive(TarArchiveOutputStream tar, File archive, String strip, String prefix)
			throws IOException {
		try (TarArchiveInputStream in = new TarArchiveInputStream(
				new GzipCompressorInputStream(new BufferedInputStream(new FileInputStream(archive))))) {
			TarArchiveEntry source;
			while ((source = in.getNextTarEntry()) != null) {
				String name = source.getName();
				if (!name.startsWith(strip)) {
					continue;
				}
				String target = prefix + name.substring(strip.length());
				if (source.isDirectory() && !target.endsWith("/")) {
					target += "/";
				}
				TarArchiveEntry entry;
				if (source.isSymbolicLink()) {
					entry = new TarArchiveEntry(target, TarArchiveEntry.LF_SYMLINK);
					entry.setLinkName(source.getLinkName());
				} else {
					entry = new TarArchiveEntry(target);
					entry.setSize(source.isDirectory() ? 0 : source.getSize());
				}
				entry.setMode(source.getMode());
				entry.setModTime(source.getModTime());
				tar.putArchiveEntry(entry);
				if (source.isFile()) {
					copy(in, tar);
				}
				tar.closeArchiveEntry();
			}
		}
	}

	private static void copy(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[65536];
		int read;
		while ((read = in.read(buffer)) > 0) {
			out.write(buffer, 0, read);
		}
	}
}
