package com.oceanpro.core.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.IOUtils;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class FileUtils {

	private static final Log log = LogFactory.getLog(FileUtils.class);
	
	/**
	 * 
	 * @param filePathToMove
	 * @param dirName
	 * @param newName If name same name
	 * @throws IOException
	 */
	public static String moveFileTo(String filePathToMove, String targetDir, String newName) throws IOException {
		log.info("moveFile filePathToMove:" +filePathToMove + " to:" + targetDir + " newName:"+newName);
		// File (or directory) to be moved
		File file = new File(filePathToMove);
	
		// Destination directory
		File dir = new File(targetDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	
		String newFileName = (newName==null?file.getName():newName);
		// Move file to new directory
		if (file.renameTo(new File(dir, newFileName))) return targetDir + File.separator + newFileName;
		else return null;
	}
	
	
	/**
	 * Calls writeToFile with createDir flag false.
	 * 
	 * @see writeToFile(String fileName, InputStream iStream, boolean createDir)
	 * 
	 * @created 2002-05-02 by Alexander Feldman
	 * 
	 */
	public static void writeToFile(String fileName, InputStream iStream)
			throws IOException {
		writeToFile(fileName, iStream, false);
	}

	/**
	 * Writes InputStream to a given <code>fileName<code>.
	 * And, if directory for this file does not exists,
	 * if createDir is true, creates it, otherwice throws OMDIOexception.
	 * 
	 * @param fileName
	 *            - filename save to.
	 * @param iStream
	 *            - InputStream with data to read from.
	 * @param createDir
	 *            (false by default)
	 * @throws IOException
	 *             in case of any error.
	 * 
	 * @refactored 2002-05-02 by Alexander Feldman - moved from OMDBlob.
	 * 
	 */
	public static void writeToFile(String fileName, InputStream iStream,
			boolean createDir) throws IOException {
		
		String me = "FileUtils.WriteToFile";
		if (fileName == null) {
			throw new IOException(me + ": filename is null!");
		}
		if (iStream == null) {
			throw new IOException(me + ": InputStream is null!");
		}

		File theFile = new File(fileName);

		// Check if a file exists.
		if (theFile.exists()) {
			String msg = theFile.isDirectory() ? "directory" : (!theFile
					.canWrite() ? "not writable" : null);
			if (msg != null) {
				throw new IOException(me + ": file '" + fileName + "' is "
						+ msg);
			}
		}

		// Create directory for the file, if requested.
		if (createDir && theFile.getParentFile() != null) {
			theFile.getParentFile().mkdirs();
		}

		// Save InputStream to the file.
		BufferedOutputStream fOut = null;
		try {
			fOut = new BufferedOutputStream(new FileOutputStream(theFile));
			byte[] buffer = new byte[32 * 1024];
			int bytesRead = 0;
			while ((bytesRead = iStream.read(buffer)) != -1) {
				fOut.write(buffer, 0, bytesRead);
			}
		} catch (Exception e) {
			throw new IOException(me + " failed, got: " + e.toString());
		} finally {
			close(iStream, fOut);
		}
	}

	/**
	 * Closes InputStream and/or OutputStream. It makes sure that both streams
	 * tried to be closed, even first throws an exception.
	 * 
	 * @throw IOException if stream (is not null and) cannot be closed.
	 * 
	 */
	protected static void close(InputStream iStream, OutputStream oStream)
			throws IOException {
		try {
			if (iStream != null) {
				iStream.close();
			}
		} finally {
			if (oStream != null) {
				oStream.close();
			}
		}
	}
	
	
	/**
	 *  Returns the contents of the file in a byte array.
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static byte[] getBytesFromFile(String file) throws IOException {
	    InputStream is = new FileInputStream(file);

	    return IOUtils.toByteArray(is);
	}
	
	
	
	public static String getContentType(InputStream is, String fileName) {
		
        ContentHandler contenthandler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        Parser parser = new AutoDetectParser();
        ParseContext pc = new ParseContext();
        try {
              parser.parse(is, contenthandler, metadata, pc);

        } catch (IOException e) {
              e.printStackTrace();
        } catch (SAXException e) {
              e.printStackTrace();
        } catch (TikaException e) {
              e.printStackTrace();
        }           
        log.info("Mime: " + metadata.get(Metadata.CONTENT_TYPE));
        return metadata.get(Metadata.CONTENT_TYPE);
	}

	public static String storeFile(String locationPropertyName, String fileName) {
		String newDir = PropertiesUtils.getInstance().readProperty(locationPropertyName);
		
		log.info("storeFile " + fileName + " in " + newDir );
		
		if (newDir==null) return null;
		
		 try {
			 File file = new File(fileName);
			 if (!file.exists()){
				 log.warn("il file " + fileName + " non esiste");
				 return null;
			 }
			 
			 if (fileName.startsWith(newDir)){
				 //si trova nella stessa cartella -> non lo sposto
				 return fileName;
			 }
			 
			 int index = fileName.lastIndexOf(".");
			 
			 String extension = "";	
			 String nome = fileName;
			 
			 if(index>-1){
				 extension = fileName.substring(index);
				 nome = fileName.substring(0, index);
			 } 
			 int index2 = nome.lastIndexOf(File.separator) +1;
			 if (index2>0){
				 nome = nome.substring(index2);
			 }
			 
			 nome += "_" + System.currentTimeMillis() + extension;
			 
			 //String filePath = PropertiesUtils.getInstance().readProperty(locationPropertyName) + File.separator + nome;
			 
			 moveFileTo(fileName, newDir, nome);
			 
			
			 return nome;
		 } catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
