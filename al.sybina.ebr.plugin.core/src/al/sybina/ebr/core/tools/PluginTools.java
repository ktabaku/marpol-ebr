package al.sybina.ebr.core.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.osgi.internal.debug.Debug;
import org.eclipse.osgi.service.datalocation.Location;
import org.osgi.framework.Bundle;

import al.sybina.ebr.core.Activator;

@SuppressWarnings("restriction")
public class PluginTools {

	/**
	 * copy the path local to the plugin into the installation location.
	 * @param bundle the plugin in consideration
	 * @param path the directory to copy (include sub dirs and may contains also hidden sub dirs)
	 * @throws IOException 
	 */
	public static void installPluginDataInRuntimeLocation(final Bundle bundle, final String path) {
		Location dataLocation = Platform.getInstanceLocation();
		if (dataLocation != null){
			try {
				if (dataLocation.isSet() && !dataLocation.isLocked() && !dataLocation.isReadOnly()){
					URL url = new URL(dataLocation.getURL().toString()+path);
					final URI uriOutData = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
					if (uriOutData != null){
						Log.info(bundle, uriOutData.getPath());
						File filePathOutData = new File(uriOutData);
						if (!filePathOutData.exists() || !filePathOutData.isDirectory()){
							final String absoluteFilePath = FileLocator.getBundleFile(bundle).getAbsolutePath();
							ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
								@Override
								public void run(IProgressMonitor monitor)
										throws CoreException {
									try {
										if (isJar(absoluteFilePath)){
											copyFromJar(absoluteFilePath, path, uriOutData, monitor);
										}else{
											String src = path;
											if (!src.startsWith(File.separator) && !absoluteFilePath.endsWith(File.separator)){
												src = absoluteFilePath + File.separator + src;
												copyDirectory(bundle, src, uriOutData, monitor);
											}
										}
									} catch (Exception e) {
										Log.severe(bundle, "error installing " + path, e);
									}
								}

							}, null);
						}
					}
				}
			} catch (Exception e) {
				Log.severe(bundle, "error installing "+path, e);
			}			
		}else{
			Log.severe(bundle, "datalocation is not set");
		}
	}

	/**
	 * create the path in the workspace.
	 * if the installation is : c:/ibr then the call to 
	 * 
	 * @param bundle
	 * @param path
	 */
	public static void createPluginDataInRuntimeLocation(final String path) {
		Location dataLocation = Platform.getInstanceLocation();
		if (dataLocation != null){
			try {
				if (dataLocation.isSet() && dataLocation.isLocked() && !dataLocation.isReadOnly()){
					URL url = new URL(dataLocation.getURL().toString()+path);
					final URL urlOutData = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null).toURL();
					if (urlOutData != null){
						File filePathOutData = new File(urlOutData.toURI());
						if (!filePathOutData.exists() || !filePathOutData.isDirectory()){
							ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
								@Override
								public void run(IProgressMonitor monitor)
										throws CoreException {
									try {
										File targetLocationFile = new File(urlOutData.getFile());
										if (!targetLocationFile.exists()) {
											targetLocationFile.mkdir();
										}
									} catch (Exception e) {
										Log.severe(Activator.getBundle(), "error creating path " + path, e);
									}
								}

							}, null);
						}
					}
				}
			} catch (Exception e) {
				Log.severe(Activator.getBundle(), "error creating path "+path, e);
			}			
		}else{
			Log.severe(Activator.getBundle(), "datalocation is not set");
		}
	}

	private static boolean isJar(String absoluteFilePath) {
		return absoluteFilePath.endsWith(".jar");
	}

	public static void copyDirectory(Bundle bundle, String source, URI target, IProgressMonitor monitor)
			throws IOException, URISyntaxException {
		String log = "copying "+source+" into "+target.getPath();
		Debug.println(log);
		if (monitor != null){
			monitor.beginTask(log, IProgressMonitor.UNKNOWN);
		}
		File sourceLocationFile = new File(source);
		File targetLocationFile = new File(target);
		if (sourceLocationFile.isDirectory()) {
			if (!targetLocationFile.exists()) {
				targetLocationFile.mkdir();
			}

			String[] children = sourceLocationFile.list();
			for (String element : children) {
				URI uri = new URI(target.getScheme(), target.getHost(), target.getPath() + File.separator + element, target.getQuery(), null);
				copyDirectory(bundle, new File(source, element).getPath(),
						uri, monitor);
			}
		} else {
			copyStreams(new FileInputStream(source), new FileOutputStream(new File(target)));
		}
		if (monitor != null){
			monitor.worked(1);
		}

	}

	public static void copyFromJar(String jarFile, String source, URI target, IProgressMonitor monitor) throws IOException, URISyntaxException{
		String log = "copying "+source+" into "+target.getPath();
		Debug.println(log);
		if (monitor != null){
			monitor.beginTask(log, IProgressMonitor.UNKNOWN);
		}
		JarFile jar = new JarFile(jarFile);
		Enumeration<JarEntry> enumeration = jar.entries();
		while (enumeration.hasMoreElements()) {
			JarEntry entry = (JarEntry) enumeration.nextElement();
			String entryName = entry.getName();
			if (entryName.startsWith(source)){
				entryName = entryName.replaceFirst(source, "");
				URI uri = new URI(target.getScheme(), target.getHost(), target.getPath() + File.separator + entryName, target.getQuery(), null);
				File file = new File(uri);
				Log.info(Activator.getBundle(), file.getAbsolutePath());
				if (entry.isDirectory()) { 
					file.mkdirs();
					Log.info(Activator.getBundle(), "creating dir " + file.getAbsolutePath());
				}else{
					copyStreams(jar.getInputStream(entry), new FileOutputStream(file));				
				}				
			}
		}		
		if (monitor != null){
			monitor.worked(1);
		}
	}

	private static void copyStreams(InputStream is, FileOutputStream fos) throws IOException {
		byte[] buffer = new byte[4096];
		int len =  0;
		while ((len = is.read(buffer)) > 0) {
			fos.write(buffer, 0, len);
		}
		fos.close();
		is.close();
	}

	public static String getFileLocalToRuntime(String fileName){
		URL url = Platform.getInstanceLocation().getURL();
		File fileLocation = null;
		try {
			URL urlLocation = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null).toURL();
			fileLocation = new File(urlLocation.toURI());
		} catch (URISyntaxException | MalformedURLException e) {
			Log.severe(Activator.getBundle(), "error getting the installation path", e);
		}
		String platformLocation = fileLocation.getPath();
		if (!fileName.startsWith("/") && !platformLocation.endsWith("/")){
			fileName = "/"+fileName;
		}
		return platformLocation + fileName;
	}

}
