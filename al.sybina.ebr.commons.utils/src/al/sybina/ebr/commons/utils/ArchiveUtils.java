package al.sybina.ebr.commons.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ArchiveUtils {
	
	public static void zipFile(final File fileToZip, final File outputZipFile) throws IOException {
		final FileOutputStream fos = new FileOutputStream(outputZipFile);
		final ZipOutputStream zipOut = new ZipOutputStream(fos);
		zipFile(fileToZip, fileToZip.getName(), zipOut);
		zipOut.close();
		fos.close();
	}
	private static void zipFile(final File fileToZip, final String fileName, final ZipOutputStream zipOut)	throws IOException {
		if (fileToZip.isHidden()) {
			return;
		}
		if (fileToZip.isDirectory()) {
			if (fileName.endsWith("/")) {
				zipOut.putNextEntry(new ZipEntry(fileName));
				zipOut.closeEntry();
			} else {
				zipOut.putNextEntry(new ZipEntry(fileName + "/"));
				zipOut.closeEntry();
			}
			final File[] children = fileToZip.listFiles();
			for (final File childFile : children) {
				zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
			}
			return;
		}
		final FileInputStream fis = new FileInputStream(fileToZip);
		final ZipEntry zipEntry = new ZipEntry(fileName);
		zipOut.putNextEntry(zipEntry);
		final byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zipOut.write(bytes, 0, length);
		}
		fis.close();
	}
	
//	public static void unzipFile(String fileZip, File destDir) throws IOException {
//        final byte[] buffer = new byte[1024];
//        final ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
//        ZipEntry zipEntry = zis.getNextEntry();
//        while (zipEntry != null) {
//            final File newFile = newFile(destDir, zipEntry);
//            
//            final FileOutputStream fos = new FileOutputStream(newFile);
//            int len;
//            while ((len = zis.read(buffer)) > 0) {
//                fos.write(buffer, 0, len);
//            }
//            fos.flush();
//            fos.close();
//            zipEntry = zis.getNextEntry();
//        }
//        zis.closeEntry();
//        zis.close();
//	}

//	public static void unzip(File zipFile, String outputFolder) throws ZipException, IOException {
//		try (ZipFile file = new ZipFile(zipFile)) {
//			Enumeration<? extends ZipEntry> zipEntries = file.entries();
//			while (zipEntries.hasMoreElements()) {
//				ZipEntry zipEntry = zipEntries.nextElement();
//				File newFile = new File(outputFolder, zipEntry.getName());
//
//				// create sub directories
//				newFile.getParentFile().mkdirs();
//
//				if (!zipEntry.isDirectory()) {
//					try (FileOutputStream outputStream = new FileOutputStream(newFile)) {
//						BufferedInputStream inputStream = new BufferedInputStream(file.getInputStream(zipEntry));
//						while (inputStream.available() > 0) {
//							outputStream.write(inputStream.read());
//						}
//						inputStream.close();
//					}
//				}
//
//			}
//		}
//	}
	
	public static void extractFolder(File file, String extractFolder) {
		try {
			int BUFFER = 2048;

			ZipFile zip = new ZipFile(file);
			String newPath = extractFolder;

			new File(newPath).mkdir();
			Enumeration zipFileEntries = zip.entries();

			// Process each entry
			while (zipFileEntries.hasMoreElements()) {
				// grab a zip file entry
				ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
				String currentEntry = entry.getName();

				File destFile = new File(newPath, currentEntry);
				// destFile = new File(newPath, destFile.getName());
				File destinationParent = destFile.getParentFile();

				// create the parent directory structure if needed
				destinationParent.mkdirs();

				if (!entry.isDirectory()) {
					BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry));
					int currentByte;
					// establish buffer for writing file
					byte data[] = new byte[BUFFER];

					// write the current file to disk
					FileOutputStream fos = new FileOutputStream(destFile);
					BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);

					// read and write until last byte is encountered
					while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
						dest.write(data, 0, currentByte);
					}
					dest.flush();
					dest.close();
					is.close();
				}

			}
			zip.close();
		} catch (Exception e) {
			//Log("ERROR: " + e.getMessage());
		}

	}
}
