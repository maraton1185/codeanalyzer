package codeanalyzer.utils;

import java.net.URL;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import codeanalyzer.views.ConfigsView;

public abstract class Utils {

	// helper method to load the images
	// ensure to dispose the images in your @PreDestroy method
	public static Image getImage(String file) {

	    // assume that the current class is called View.java
	  Bundle bundle = FrameworkUtil.getBundle(ConfigsView.class);
	  URL url = FileLocator.find(bundle, new Path("icons/" + file), null);
	  ImageDescriptor image = ImageDescriptor.createFromURL(url);
	  return image.createImage();

	} 
	
	public static IPath getAbsolute(IPath path) {
		if (!path.isAbsolute())
			path = ResourcesPlugin.getWorkspace().getRoot().getLocation()
					.append(path);
		return path;
	}

	public static void browseForPath(Text field, Shell shell) {
		IPath path = Utils.browseDirectory(getPath(field), shell);
		if (path == null)
			return;
		IPath rootLoc = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		if (rootLoc.isPrefixOf(path))
			path = path.setDevice(null).removeFirstSegments(
					rootLoc.segmentCount());
		field.setText(path.toString());
	}
	
	public static void browseForFile(Text field, Shell shell) {
		IPath path = Utils.browseFile(getPath(field), shell, "Выберите файл базы данных", "*.db");
		if (path == null)
			return;
		IPath rootLoc = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		if (rootLoc.isPrefixOf(path))
			path = path.setDevice(null).removeFirstSegments(
					rootLoc.segmentCount());
		field.setText(path.toString());
	}
	
	//*********************************************************************

	static IPath getPath(Text field) {
		String text = field.getText().trim();
		if (text.length() == 0)
			return null;
		IPath path = new Path(text);
		
		return Utils.getAbsolute(path);
	}
	
	static IPath browseDirectory(IPath path, Shell shell) {
		DirectoryDialog dialog = new DirectoryDialog(shell);
		dialog.setText("Выберите каталог");

		if (path != null) {
			dialog.setFilterPath(path.toString());
		}
		String result = dialog.open();
		if (result == null)
			return null;
		return new Path(result);
	}

	static IPath browseFile(IPath path, Shell shell, String title, String filter_name) {
		FileDialog dialog = new FileDialog(shell);
		dialog.setText(title);
		String[] filter = new String[1];
		filter[0] = filter_name;
		dialog.setFilterExtensions(filter);

		if (path != null) {
			dialog.setFilterPath(path.toString());
		}
		String result = dialog.open();
		if (result == null)
			return null;
		return new Path(result);
	}
}


