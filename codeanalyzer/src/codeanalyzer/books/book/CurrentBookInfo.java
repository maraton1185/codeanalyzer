package codeanalyzer.books.book;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

import codeanalyzer.books.section.SectionsService;
import codeanalyzer.core.models.ModelObject;
import codeanalyzer.utils.PreferenceSupplier;
import codeanalyzer.utils.Utils;

public class CurrentBookInfo extends ModelObject {

	SectionsService bookSections;

	public SectionsService sections() {

		bookSections = bookSections == null ? new SectionsService()
				: bookSections;

		return bookSections;
	}

	public boolean isGroup;

	private String name;

	private String description = "";

	private boolean opened = false;
	private boolean editMode = false;
	private boolean viewMode = false;

	public boolean isViewMode() {
		return viewMode;
	}

	public void setViewMode(boolean viewMode) {
		this.viewMode = viewMode;
		editMode = !viewMode;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
		viewMode = !editMode;
	}

	private IPath path;

	public boolean isOpened() {
		return opened;
	}

	public void setOpened(boolean opened) {
		fireIndexedPropertyChange("opened", this.opened, this.opened = opened);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {

		fireIndexedPropertyChange("description", this.description,
				this.description = description);
	}

	public void setName(String name) {
		this.name = name;
		path = new Path(
				PreferenceSupplier
						.get(PreferenceSupplier.DEFAULT_BOOK_DIRECTORY));
	}

	public void setPath(IPath path) {
		this.path = path.removeLastSegments(1);
		this.name = path.removeFileExtension().removeFileExtension()
				.lastSegment();
	}

	public IPath getPath() {
		return Utils.getAbsolute(path);
	}

	public String getFullName() {
		return Utils.getAbsolute(path).append(name).toString();
	}

	public Image getImage() {
		File folder = getPath().toFile();
		File[] files = folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {

				String ext = Utils.getExtension(file);
				String file_name = file.getName();
				return file_name.contains(name)
						&& (ext.equalsIgnoreCase("bmp") || ext
								.equalsIgnoreCase("png"));
			}
		});
		for (File f : files) {
			ImageData data = new ImageData(f.getAbsolutePath());
			return ImageDescriptor.createFromImageData(data).createImage();
		}
		return null;
	}

	public void setImage(URL path) {

		// ImageDescriptor image = ImageDescriptor.createFromURL(path);
		// this.image = image.createImage();
	}

	public String getName() {
		return name;
	}

	public String getWindowTitle() {
		return name + (editMode ? " (Редактор)" : "");
	}

	// CONNECTION
	// *****************************************************************
	private Connection con;

	public Connection makeConnection(boolean exist)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {

		Class.forName("org.h2.Driver").newInstance();
		String ifExist = exist ? ";IFEXISTS=TRUE" : "";

		IPath path = getPath().append(name);

		return DriverManager.getConnection("jdbc:h2:" + path.toString()
				+ ifExist, "sa", "");

	}

	public Connection getConnection() throws IllegalAccessException {

		if (con == null)
			throw new IllegalAccessException();
		else
			return con;

	}

	public void openConnection() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		if (con != null)
			return;

		Class.forName("org.h2.Driver").newInstance();
		boolean exist = true;
		String ifExist = exist ? ";IFEXISTS=TRUE" : "";

		String mode = !editMode ? ";FILE_LOCK=SERIALIZED" : "";

		IPath path = getPath().append(name);

		con = DriverManager.getConnection("jdbc:h2:" + path.toString()
				+ ifExist + mode, "sa", "");
	}

	public void closeConnection() {
		if (con == null)
			return;
		try {

			con.close();
			con = null;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void finalize() {
		closeConnection();
	}

}
