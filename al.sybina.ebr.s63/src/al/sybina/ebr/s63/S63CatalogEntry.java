/**
 * 
 */
package al.sybina.ebr.s63;

import java.io.File;
import java.util.Vector;

import al.sybina.ebr.S57Library.fiedsRecords.S57FieldCATD;
import al.sybina.ebr.S57Library.files.S57ModuleReader;
import al.sybina.ebr.core.geo.Sector;


/**
 * @author cyrille
 *
 */
public class S63CatalogEntry  {
	Vector<S57FieldCATD> elements = null;
	
	private class S57ModuleReaderCatalog extends S57ModuleReader{
		private String pathName = "";
		
		public S57ModuleReaderCatalog(String filename) throws Exception {
			super();
			File f = new File(filename);
			pathName = f.getPath().replace(f.getName(), "");
			pathName = pathName.replace('\\', '/');
		}

		@Override
		public void newCatEntryCallBack(S57FieldCATD catd){
			if (catd.northLat != 0 && catd.southLat != 0){
				elements.add(catd);				
			}
		}
	}

	public S63CatalogEntry(String filename) throws Exception {
		elements = new Vector<S57FieldCATD>();
		S57ModuleReaderCatalog module = new S57ModuleReaderCatalog(filename);
		module.loadCatalog(filename);
	}

	public Vector<S57FieldCATD> getElements() {
		return elements;
	}

}
