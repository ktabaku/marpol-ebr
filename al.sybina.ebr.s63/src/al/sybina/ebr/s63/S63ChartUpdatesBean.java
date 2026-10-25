package al.sybina.ebr.s63;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;


public class S63ChartUpdatesBean {
	private File file;
	private String cellpermit;
	private String cellbase;
	private TreeSet<String> cellupdates;
	
	public S63ChartUpdatesBean(String filename) throws FileNotFoundException {
		this(new File(filename));
	}
	
	public S63ChartUpdatesBean(File installedPermitFile) throws FileNotFoundException {
		cellupdates = new TreeSet<String>(new IssueDateComparator<String>());
		this.file = installedPermitFile;
		FileInputStream fis = new FileInputStream(installedPermitFile);
		Scanner scanner = new Scanner(fis);
		while(scanner.hasNextLine()){
		    String line = scanner.nextLine();
		    if(line.startsWith("cellpermit:")) {
		    	cellpermit = line.substring("cellpermit:".length());
		    }
		    if(line.startsWith("cellbase:")) {
		    	cellbase = line.substring("cellbase:".length());
		    }
		    if(line.startsWith("cellupdate:")) {
		    	cellupdates.add(line.substring("cellupdate:".length()));
		    }
		}
		scanner.close();
	}

	public File getBaseFile() throws Exception {
		if(cellbase!=null) {
			return (File) parseCellElement(cellbase)[0];
		} else {
			throw new Exception("No cellbase for this chart.");
		}
	}
	public List<File> getBaseUpdatesFiles() throws Exception {
		List<File> baseUpdatesFiles = new ArrayList<>();
		if(cellbase!=null) {
			File file = (File) parseCellElement(cellbase)[0];
			baseUpdatesFiles.add(file);
		}
		for(String cellupdate : cellupdates) {
			File file = (File) parseCellElement(cellupdate)[0];
			baseUpdatesFiles.add(file);
		}
		return baseUpdatesFiles;
	}

	public String getCellpermit() {
		return cellpermit;
	}

	public void setCellpermit(String cellpermit) {
		this.cellpermit = cellpermit;
	}

	public String getCellbase() {
		return cellbase;
	}

	public void setCellbase(String cellbase) {
		this.cellbase = cellbase;
	}

	public TreeSet<String> getCellupdates() {
		if(cellupdates==null) {
			return new TreeSet<String>(new IssueDateComparator<String>());
		}
		return cellupdates;
	}

	public void setCellupdates(TreeSet<String> cellupdates) {
		this.cellupdates = cellupdates;
	}
	
	private Object[] parseCellElement(String cellElement) throws ParseException {
		String[] arr = cellElement.split(";");
		File file = new File(arr[0]);
		String[] dsidArr = arr[1].split(",");
		Dsid dsid = new Dsid();
		dsid.setVersion(NumberFormat.getInstance().parse(dsidArr[0].substring("VERSION=".length())).doubleValue());
		dsid.setEdition(Integer.parseInt(dsidArr[1].substring("EDTN=".length())));
		dsid.setUpdateNumber(Integer.parseInt(dsidArr[2].substring("UPDN=".length())));
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		if(dsidArr.length==4) {
			dsid.setIssueDate(dateFormat.parse(dsidArr[3].substring("ISDT=".length())));
		} else {
			dsid.setUpdateApplicationDate(dateFormat.parse(dsidArr[3].substring("UADT=".length())));
			dsid.setIssueDate(dateFormat.parse(dsidArr[4].substring("ISDT=".length())));
		}
		return new Object[]{file, dsid};
	}
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("cellpermit:" + cellpermit + "\n");
		sb.append("cellbase:" + cellbase + "\n");
		for(String cellUpdate : cellupdates) {
			sb.append("cellupdate:" + cellUpdate + "\n");
		}
		return sb.toString();
	}
	public void save() throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		fos.write((toString()).getBytes());
		fos.close();
	}
	
	public class Dsid {
		private double version;
		private int edition;
		private int updateNumber;
		private Date updateApplicationDate;
		private Date issueDate;
		
		public double getVersion() {
			return version;
		}
		public void setVersion(double version) {
			this.version = version;
		}
		public int getEdition() {
			return edition;
		}
		public void setEdition(int edition) {
			this.edition = edition;
		}
		public int getUpdateNumber() {
			return updateNumber;
		}
		public void setUpdateNumber(int updateNumber) {
			this.updateNumber = updateNumber;
		}
		public Date getUpdateApplicationDate() {
			return updateApplicationDate;
		}
		public void setUpdateApplicationDate(Date updateApplicationDate) {
			this.updateApplicationDate = updateApplicationDate;
		}
		public Date getIssueDate() {
			return issueDate;
		}
		public void setIssueDate(Date issueDate) {
			this.issueDate = issueDate;
		}
	}

	public class IssueDateComparator<T> implements Comparator<String> {
		@Override
		public int compare(String o1, String o2) {
			try {
				Dsid dsid1 = (Dsid) parseCellElement(o1)[1];
				Dsid dsid2 = (Dsid) parseCellElement(o2)[1];
				return dsid1.getIssueDate().compareTo(dsid2.getIssueDate());
			} catch (ParseException e) {
				return 0;
			}
		}
	}

	public File getThisFile() {
		return file;
	}
	
}
