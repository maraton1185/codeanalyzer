package codeanalyzer.module.cf.interfaces;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;

public interface ILoaderManager {

	public enum operationType {
		fromDb, fromDirectory, update, fromSQL, fillProcLinkTable
	}

	void loadFromDirectory(ICf db, IProgressMonitor monitor)
			throws InvocationTargetException, InterruptedException;

	void loadFromDb(ICf db) throws InvocationTargetException;

	void fillProcLinkTable(ICf db, IProgressMonitor monitor)
			throws InvocationTargetException;

	void update(ICf db, IProgressMonitor monitor)
			throws InvocationTargetException;

	void loadFromSQL(ICf db, IProgressMonitor monitor)
			throws InvocationTargetException;

}
