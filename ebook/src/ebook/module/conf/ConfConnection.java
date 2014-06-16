package ebook.module.conf;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IPath;

import ebook.core.models.BaseDbPathConnection;

public class ConfConnection extends BaseDbPathConnection {

	public ConfConnection(IPath path) throws InvocationTargetException {
		super(path);
	}

	public ConfConnection(String name) throws InvocationTargetException {
		super(name);
	}

}
