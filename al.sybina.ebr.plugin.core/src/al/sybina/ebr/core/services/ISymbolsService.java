package al.sybina.ebr.core.services;


public interface ISymbolsService extends IIbrCoreService {

	/**
	 * @param symbolName
	 * @return the image filename associated to the symbol name
	 */
	String getImageFileName(String symbolName);

	/**
	 * @return the default symbol fileName for route points
	 */
	String getdefaultSymbolForRoute();

	/**
	 * get the swt image of the symbol given it's name.
	 * @param key the symbol's name
	 * @return an org.eclipse.swt.graphics.Image 
	 */
	Object getSWTImage(String symbolName);
	
	/**
	 * get the javafx image of the symbol given it's name.
	 * @param key the symbol's name
	 * @return an org.eclipse.swt.graphics.Image 
	 */
	Object getJavaFxImage(String symbolName);

}
