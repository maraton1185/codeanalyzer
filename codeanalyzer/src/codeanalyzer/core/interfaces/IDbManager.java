package codeanalyzer.core.interfaces;

import java.util.List;

import codeanalyzer.core.interfaces.ILoaderService.operationType;


public interface IDbManager {

	public void init();
		
	public List<IDb> getList();
	
	public IDb getActive();
	
	public IDb getNonActive();
	
	public void setActive(IDb db);
	
	public void setNonActive(IDb db);

	public String getOperationName(operationType key);
	
	void execute(final IDb db);

	public void addDb(IDb db);
	
}
