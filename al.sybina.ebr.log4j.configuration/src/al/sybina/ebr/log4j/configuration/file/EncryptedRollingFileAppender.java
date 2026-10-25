package al.sybina.ebr.log4j.configuration.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;

import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.helpers.CountingQuietWriter;
import org.apache.log4j.helpers.LogLog;

public class EncryptedRollingFileAppender extends RollingFileAppender {
	
    private static byte[] keyBytes = new byte[] {
            // Change these numbers, lest other StackOverflow readers can decrypt your files.
    		101, 110, 99, 114, 121, 112, 116, 101, 100, 77, 97, 114, 112, 111, 108, 65, 117, 100, 105, 116, 76, 111, 103, 80, 97, 115, 115, 119, 111, 114, 100, 10
    };
    private static SecretKeySpec key = new SecretKeySpec(keyBytes,"AES");

	public synchronized void setFile(String fileName, boolean append, boolean bufferedIO, int bufferSize)
			throws IOException {

		
		
		
		
		
		
		
		
		
	    LogLog.debug("setFile called: "+fileName+", "+append);

	    // It does not make sense to have immediate flush and bufferedIO.
	    if(bufferedIO) {
	      setImmediateFlush(false);
	    }

	    reset();
	    FlushableCipherOutputStream ostream = null;
	    try {
	          //
	          //   attempt to create file
	          //
	          ostream = new FlushableCipherOutputStream(fileName, key, append, true);
	    } catch(FileNotFoundException ex) {
	          //
	          //   if parent directory does not exist then
	          //      attempt to create it and try to create file
	          //      see bug 9150
	          //
	          String parentName = new File(fileName).getParent();
	          if (parentName != null) {
	             File parentDir = new File(parentName);
	             if(!parentDir.exists() && parentDir.mkdirs()) {
	                ostream = new FlushableCipherOutputStream(fileName, key, append, true);
	             } else {
	                throw ex;
	             }
	          } else {
	             throw ex;
	          }
	    }
	    Writer fw = createWriter(ostream);
	    if(bufferedIO) {
	      fw = new BufferedWriter(fw, bufferSize);
	    }
	    this.setQWForFiles(fw);
	    this.fileName = fileName;
	    this.fileAppend = append;
	    this.bufferedIO = bufferedIO;
	    this.bufferSize = bufferSize;
	    writeHeader();
	    LogLog.debug("setFile ended");
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		if (append) {
			File f = new File(fileName);
			((CountingQuietWriter) qw).setCount(f.length());
		}
	}
}
