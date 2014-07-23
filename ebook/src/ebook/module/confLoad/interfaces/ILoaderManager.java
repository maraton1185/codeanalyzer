package ebook.module.confLoad.interfaces;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;

import ebook.module.confList.tree.ListConfInfo;

public interface ILoaderManager {

	public enum operationType {
		fromDb, fromDirectory, update, fromSQL, fillProcLinkTable
	}

	void loadFromDirectory(ListConfInfo db, IProgressMonitor monitor)
			throws InvocationTargetException, InterruptedException;

	void loadFromDb(ListConfInfo db) throws InvocationTargetException;

	void fillProcLinkTable(ListConfInfo db, IProgressMonitor monitor)
			throws InvocationTargetException;

	void update(ListConfInfo db, IProgressMonitor monitor)
			throws InvocationTargetException;

	void loadFromSQL(ListConfInfo db, IProgressMonitor monitor)
			throws InvocationTargetException;

}
