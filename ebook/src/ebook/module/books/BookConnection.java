package ebook.module.books;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import ebook.core.App;
import ebook.core.models.BaseDbConnection;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Utils;

public class BookConnection extends BaseDbConnection {

	IPath path;
	String name;

	public BookConnection(IPath path) throws InvocationTargetException {

		super(new BookStructure());

		if (path == null)
			throw new InvocationTargetException(null);

		if (!path.isValidPath(path.toString()))
			throw new InvocationTargetException(null);

		setPath(path);

		check();
	}

	public BookConnection(String name) throws InvocationTargetException {

		super(new BookStructure());

		this.name = name;
		this.path = new Path(
				PreferenceSupplier
						.get(PreferenceSupplier.DEFAULT_BOOK_DIRECTORY));
		create();
	}

	@Override
	protected IPath getConnectionPath() {
		return getPath().append(name);
	}

	// *****************************************************************

	private void setPath(IPath path) {
		if (path == null)
			return;
		this.path = path.removeLastSegments(1);
		this.name = path.removeFileExtension().removeFileExtension()
				.lastSegment();
	}

	public IPath getPath() {
		return Utils.getAbsolute(path);
	}

	// *****************************************************************

	public String getName() {
		return name;
	}

	public String getFullName() {
		return Utils.getAbsolute(path).append(name).toString();
	}

	public String getWindowTitle() {
		// return name + (editMode ? " (Редактор)" : "");
		return getFullName();
	}

	BookService service;

	public BookService srv() {

		service = service == null ? App.srv.bs(this) : service;

		return service;
	}

	// *****************************************************************

}
//
// public boolean isGroup;
//
// private String name;this
//
// private String description = "";
//
// private boolean opened = false;
// private boolean editMode = false;
// private boolean viewMode = false;
//
// public boolean isViewMode() {
// return viewMode;
// }
//
// public void setViewMode(boolean viewMode) {
// this.viewMode = viewMode;
// editMode = !viewMode;
// }
//
// public boolean isEditMode() {
// return editMode;
// }
//
// public void setEditMode(boolean editMode) {
// this.editMode = editMode;
// viewMode = !editMode;
// }
//
// private IPath path;
//
// public boolean isOpened() {
// return opened;
// }
//
// public void setOpened(boolean opened) {
// fireIndexedPropertyChange("opened", this.opened, this.opened = opened);
// }
//
// public String getDescription() {
// return description;
// }
//
// public void setDescription(String description) {
//
// fireIndexedPropertyChange("description", this.description,
// this.description = description);
// }
//
// public void setName(String name) {
// this.name = name;
// path = new Path(
// PreferenceSupplier
// .get(PreferenceSupplier.DEFAULT_BOOK_DIRECTORY));
// }
//
// public void setPath(IPath path) {
// if (path == null)
// return;
// this.path = path.removeLastSegments(1);
// this.name = path.removeFileExtension().removeFileExtension()
// .lastSegment();
// }
//
// public IPath getPath() {
// return Utils.getAbsolute(path);
// }
//

//
// public Image getImage() {
// File folder = getPath().toFile();
// File[] files = folder.listFiles(new FileFilter() {
// @Override
// public boolean accept(File file) {
//
// String ext = Utils.getExtension(file);
// String file_name = file.getName();
// return file_name.contains(name)
// && (ext.equalsIgnoreCase("bmp") || ext
// .equalsIgnoreCase("png"));
// }
// });
// for (File f : files) {
// ImageData data = new ImageData(f.getAbsolutePath());
// return ImageDescriptor.createFromImageData(data).createImage();
// }
// return null;
// }
//
// public void setImage(URL path) {
//
// // ImageDescriptor image = ImageDescriptor.createFromURL(path);
// // this.image = image.createImage();
// }
//

//
// public String getWindowTitle() {
// return name + (editMode ? " (Редактор)" : "");
// }
