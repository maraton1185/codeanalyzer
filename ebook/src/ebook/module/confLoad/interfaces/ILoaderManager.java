package ebook.module.confLoad.interfaces;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Shell;

import ebook.module.confList.tree.ListConfInfo;
import ebook.module.tree.item.ITreeItemInfo;

public interface ILoaderManager {

	public enum operationType {
		// fromDb,
		fromDirectory, update, fromSQL// , fillProcLinkTable
	}

	void loadFromDirectory(ListConfInfo db, IProgressMonitor monitor)
			throws InvocationTargetException, InterruptedException;

	// void loadFromDb(ListConfInfo db) throws InvocationTargetException;

	// void fillProcLinkTable(ListConfInfo db, IProgressMonitor monitor)
	// throws InvocationTargetException;

	void update(ListConfInfo db, IProgressMonitor monitor)
			throws InvocationTargetException;

	void loadFromSQL(ListConfInfo db, IProgressMonitor monitor)
			throws InvocationTargetException;

	String getOperationName(operationType key);

	void execute(ITreeItemInfo conf, Shell shell);

}
