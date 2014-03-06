package codeanalyzer.core.interfaces;

import java.util.List;

import org.eclipse.swt.widgets.Shell;

import codeanalyzer.core.interfaces.ILoaderManager.operationType;

public interface IDbManager {

	public void init();

	public List<IDb> getList();

	public IDb getActive();

	public IDb getNonActive();

	public void setActive(IDb db);

	public void setNonActive(IDb db);

	public String getOperationName(operationType key);

	// void execute(final IDb db, IProgressMonitor widget);

	public void add(IDb db);

	public void remove(IDb db);

	public void executeInit(IDb db);

	void execute(IDb db, Shell shell);

}
