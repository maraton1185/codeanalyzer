package ebook.utils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MStackElement;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import ebook.module.conf.views._ConfigsView;

public abstract class Utils {

	public static void popUpInformation(final String message) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				MessageDialog.openInformation(null, Strings.title("appTitle"),
						message);
			}
		});
	}

	public static String getUrl(String host, Integer id) {
		return host + "&id=" + id;
	}

	public static void togglePart(MWindow window, EModelService model,
			String partId, String stackId) {
		List<MPart> parts = model.findElements(window, Strings.model(partId),
				MPart.class, null);
		parts.get(0).setVisible(true);

		List<MPartStack> stacks = model.findElements(window,
				Strings.model(stackId), MPartStack.class, null);

		// String partID = Strings.get("ebook.part.book");

		for (MStackElement item : stacks.get(0).getChildren()) {
			if (!(item instanceof MPart))
				continue;
			MPart part = (MPart) item;
			part.setVisible(part.getElementId().equals(partId));
		}

		PreferenceSupplier.set(PreferenceSupplier.SELECTED_LIST, partId);
	}

	public static void executeHandler(EHandlerService hService,
			ECommandService comService, String id) {
		hService.executeHandler(comService.createCommand(id,
				Collections.EMPTY_MAP));
	}

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
		Bundle bundle = FrameworkUtil.getBundle(_ConfigsView.class);
		URL url = FileLocator.find(bundle, new Path("icons/set/" + file), null);
		ImageDescriptor image = ImageDescriptor.createFromURL(url);
		return image.createImage();

	}

	public static IPath getAbsolute(IPath path) {

		if (!path.isAbsolute())
			return ResourcesPlugin.getWorkspace().getRoot().getLocation()
					.append(path);
		return path;
	}

	public static void browseForPath(Text field, Shell shell) {
		IPath path = Utils.browseDirectory(getPath(field), shell);
		if (path == null)
			return;
		// IPath rootLoc =
		// ResourcesPlugin.getWorkspace().getRoot().getLocation();
		// if (rootLoc.isPrefixOf(path))
		// path = path.setDevice(null).removeFirstSegments(
		// rootLoc.segmentCount());
		field.setText(path.toString());
	}

	public static void browseForFile(Text field, Shell shell) {
		IPath path = Utils.browseFile(getPath(field), shell,
				"Выберите файл базы данных", "*.db");
		if (path == null)
			return;
		// IPath rootLoc =
		// ResourcesPlugin.getWorkspace().getRoot().getLocation();
		// if (rootLoc.isPrefixOf(path))
		// path = path.setDevice(null).removeFirstSegments(
		// rootLoc.segmentCount());
		field.setText(path.removeFileExtension().removeFileExtension()
				.toString());
	}

	// *********************************************************************

	static IPath getPath(Text field) {
		String text = field.getText().trim();
		if (text.length() == 0)
			return null;
		IPath path = new Path(text);

		return Utils.getAbsolute(path);
	}

	public static IPath browseDirectory(IPath path, Shell shell) {
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
		// dialog.

		if (path != null) {
			dialog.setFilterPath(path.toString());
		}
		String result = dialog.open();
		if (result == null)
			return null;
		return new Path(result);
	}

	public static IPath saveFile(IPath path, Shell shell, String title,
			String filter_name) {
		FileDialog dialog = new FileDialog(shell, SWT.SAVE);
		dialog.setText(title);
		String[] filter = new String[1];
		filter[0] = filter_name;
		dialog.setFilterExtensions(filter);
		// dialog.

		if (path != null) {
			dialog.setFilterPath(path.toString());
		}
		String result = dialog.open();
		if (result == null)
			return null;
		return new Path(result);
	}

	public static List<IPath> browseFileMulti(IPath path, Shell shell,
			String title, String filter_name) {
		FileDialog dialog = new FileDialog(shell, SWT.MULTI);
		dialog.setText(title);
		String[] filter = new String[1];
		filter[0] = filter_name;
		dialog.setFilterExtensions(filter);
		// dialog.

		if (path != null) {
			dialog.setFilterPath(path.toString());
		}
		String result = dialog.open();
		if (result == null)
			return null;

		IPath p = new Path(result).removeLastSegments(1);
		List<IPath> value = new ArrayList<IPath>();

		for (String f : dialog.getFileNames()) {
			value.add(p.append(f));
		}
		// p.removeLastSegments(1).append(path)

		return value;
	}

	// *********************************************************************

	public static String getAboutBookPath() {
		// return ResourcesPlugin.getWorkspace().getRoot().getLocation()
		// .toString().concat("/about");
		return "about";
	}

	// public static void fillBooks(Composite sectionClient, FormToolkit
	// toolkit,
	// final Shell shell, HyperlinkAdapter handler) {
	// List<CurrentBookInfo> bl = pico.get(IBookManager.class).getBooks();
	// for (CurrentBookInfo book : bl) {
	// ImageHyperlink link = toolkit.createImageHyperlink(sectionClient,
	// SWT.WRAP);
	// link.setUnderlined(false);
	// link.setImage(book.getImage());
	// link.setText(book.getName());
	// link.setHref(book);
	// link.addHyperlinkListener(handler);
	//
	// }
	//
	// }
}
