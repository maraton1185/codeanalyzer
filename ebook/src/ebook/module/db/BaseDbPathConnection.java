package ebook.module.db;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import ebook.core.interfaces.IDbStructure;

public abstract class BaseDbPathConnection extends BaseDbConnection {

	IPath path;
	String name;

	public BaseDbPathConnection(IPath path, IDbStructure dbStructure,
			boolean check, boolean license) throws InvocationTargetException {

		super(dbStructure);

		if (path == null)
			throw new InvocationTargetException(null);

		if (!path.isValidPath(path.toString()))
			throw new InvocationTargetException(null);

		this.license = license;

		setPath(path);

		if (check)
			check();
	}

	public BaseDbPathConnection(String name, IDbStructure dbStructure)
			throws InvocationTargetException {

		super(dbStructure);

		this.name = name;
		this.path = new Path("");
		// PreferenceSupplier
		// .get(PreferenceSupplier.DEFAULT_CONF_DIRECTORY));
		create();
	}

	@Override
	protected IPath getConnectionPath() {
		return getFullPath().append(name);
	}

	// *****************************************************************

	protected IPath getBasePath() {

		return ResourcesPlugin.getWorkspace().getRoot().getLocation();

	}

	private void setPath(IPath path) {
		if (path == null)
			return;

		IPath rootLoc = getBasePath();
		if (rootLoc.isPrefixOf(path))
			path = path.setDevice(null).removeFirstSegments(
					rootLoc.segmentCount());
		this.path = path.removeLastSegments(1);
		this.name = path.removeFileExtension().removeFileExtension()
				.lastSegment();
	}

	public IPath getFullPath() {

		if (!path.isAbsolute())
			return getBasePath().append(path);
		return path;

	}

	public IPath getPath() {
		return path;
	}

	// *****************************************************************

	@Override
	public String getName() {
		return getPath().append(name).toString();
	}

	public String getTitle() {
		return name;
	}

	@Override
	public String getFullName() {
		return getFullPath().append(name).toString();
	}

	// public String getWindowTitle() {
	// // return name + (editMode ? " (��������)" : "");
	// return getFullName();
	// }
	// @Override
	@Override
	public String getWindowTitle() {
		return getTreeItem().getTitle() + " (" + getName() + ")";
	}

}
