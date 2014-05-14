package codeanalyzer.cf.interfaces;

import java.util.List;

import org.eclipse.swt.widgets.Shell;

import codeanalyzer.cf.interfaces.ILoaderManager.operationType;

public interface ICfManager {

	public void init();

	public List<ICf> getList();

	public ICf getActive();

	public ICf getNonActive();

	public void setActive(ICf db);

	public void setNonActive(ICf db);

	public String getOperationName(operationType key);

	// void execute(final IDb db, IProgressMonitor widget);

	public void add(ICf db);

	public void remove(ICf db);

	public void executeInit(ICf db);

	void execute(ICf db, Shell shell);

}
