package codeanalyzer.utils;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import codeanalyzer.book.BookInfo;
import codeanalyzer.core.interfaces.IBookManager;
import codeanalyzer.views.ConfigsView;

public abstract class Utils {

	public static String getExtension(File pathname) {
		String extension = "";
		String fileName = pathname.getName();

		int i = fileName.lastIndexOf('.');
		if (i > 0)
			extension = fileName.substring(i + 1);

		return extension;
	}

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
		IPath path = Utils.browseFile(getPath(field), shell,
				"Выберите файл базы данных", "*.db");
		if (path == null)
			return;
		IPath rootLoc = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		if (rootLoc.isPrefixOf(path))
			path = path.setDevice(null).removeFirstSegments(
					rootLoc.segmentCount());
		field.setText(path.toString());
	}

	// *********************************************************************

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

	public static IPath browseFile(IPath path, Shell shell, String title,
			String filter_name) {
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

	// *********************************************************************

	public static void fillBooks(final IBookManager bm,
			Composite sectionClient, FormToolkit toolkit, final Shell shell,
			HyperlinkAdapter handler) {
		List<BookInfo> bl = bm.getBooks();
		for (BookInfo book : bl) {
			ImageHyperlink link = toolkit.createImageHyperlink(sectionClient,
					SWT.WRAP);
			link.setUnderlined(false);
			link.setImage(book.getImage());
			link.setText(book.getName());
			link.setHref(book);
			link.addHyperlinkListener(handler);

		}

	}
}
