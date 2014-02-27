package codeanalyzer.core.interfaces;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;

public interface ILoaderManager {

	public enum operationType {
		fromDb, fromDirectory, update, fromSQL
//		, fillProcLinkTable
	}
	
	void loadFromDirectory(IDb db, IProgressMonitor monitor) throws InvocationTargetException, InterruptedException;

	void loadFromDb(IDb db, IProgressMonitor monitor) throws InvocationTargetException;

//	void fillProcLinkTable(IDb db, IProgressMonitor monitor) throws InvocationTargetException;

	void update(IDb db, IProgressMonitor monitor) throws InvocationTargetException;

	void loadFromSQL(IDb db, IProgressMonitor monitor) throws InvocationTargetException;
	
}
