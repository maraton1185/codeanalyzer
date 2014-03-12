package codeanalyzer.book;

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

import codeanalyzer.utils.PreferenceSupplier;
import codeanalyzer.utils.Utils;

public class BookInfo {

	private String name;

	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	private IPath path;

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

	// public void setImage(Image image) {
	// this.image = image;
	// }

	public String getFullName() {
		return Utils.getAbsolute(path).append(name).toString();
	}

	public String getName() {
		return name;
	}

	public Connection getConnection(boolean exist)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {

		Class.forName("org.h2.Driver").newInstance();
		String ifExist = exist ? ";IFEXISTS=TRUE" : "";

		IPath path = getPath().append(name);

		return DriverManager.getConnection("jdbc:h2:" + path.toString()
				+ ifExist, "sa", "");
	}

}
