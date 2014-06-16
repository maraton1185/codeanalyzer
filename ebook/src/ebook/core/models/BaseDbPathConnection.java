package ebook.core.models;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import ebook.core.interfaces.IDbStructure;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Utils;

public class BaseDbPathConnection extends BaseDbConnection {

	IPath path;
	String name;

	public BaseDbPathConnection(IPath path, IDbStructure dbStructure)
			throws InvocationTargetException {

		super(dbStructure);

		if (path == null)
			throw new InvocationTargetException(null);

		if (!path.isValidPath(path.toString()))
			throw new InvocationTargetException(null);

		setPath(path);

		check();
	}

	public BaseDbPathConnection(String name, IDbStructure dbStructure)
			throws InvocationTargetException {

		super(dbStructure);

		this.name = name;
		this.path = new Path(
				PreferenceSupplier
						.get(PreferenceSupplier.DEFAULT_CONF_DIRECTORY));
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

}
