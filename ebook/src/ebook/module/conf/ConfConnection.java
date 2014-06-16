package ebook.module.conf;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IPath;

import ebook.core.models.BaseDbPathConnection;

public class ConfConnection extends BaseDbPathConnection {

	public ConfConnection(IPath path) throws InvocationTargetException {
		super(path, new ConfStructure());
	}

	public ConfConnection(String name) throws InvocationTargetException {
		super(name, new ConfStructure());
	}

}
