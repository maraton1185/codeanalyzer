package codeanalyzer.core.interfaces;

import org.eclipse.core.runtime.IPath;

import codeanalyzer.core.interfaces.ILoaderService.operationType;
import codeanalyzer.db.DbInfo.SQLConnection;



public interface IDb {
	
	public static enum DbState {
		notLoaded, Loaded
//		, LoadedWithLinkTable
	}
	
	//—“¿“”— ****************************************************** 
	
	public abstract String status();

	//—≈–»¿À»«¿÷»ﬂ ****************************************************** 
	
	public abstract void load(String key);

	public abstract void save();

	//”—“¿ÕŒ¬ ¿/œŒÀ”◊≈Õ»≈ —¬Œ…—“¬ ******************************************************
	
	public abstract String getId();
	
	public abstract void setType(operationType type);

	public abstract operationType getType();

	public abstract void setPath(String path);

	public abstract IPath getPath();

	public abstract void setName(String name);

	public abstract String getName();

	public abstract void setDbPath(String path);

	public abstract IPath getDbPath();

	public abstract void setState(DbState status);

	public abstract DbState getState();
	
	public abstract void setSQL(String path, String user, String password);

	public abstract SQLConnection getSQL();
	
	public abstract SQLConnection getDefaultSQL();

//	void initDbPath();

	boolean isLoaded();

	boolean getAutoName();

	void setAutoName(boolean value);

	//–¿¡Œ“¿ — ƒ¿ÕÕ€Ã»  ŒÕ‘»√”–¿÷»» ****************************************************** 
	
//	Connection getSQLConnection() throws InstantiationException,
//			IllegalAccessException, ClassNotFoundException, SQLException;
//	
//	Connection getConnection(boolean exist) throws InstantiationException,
//			IllegalAccessException, ClassNotFoundException, SQLException;
//

//	List<BuildInfo> search(searchType type, String text, IProgressMonitor monitor);

	//BUILD ****************************************************** 
	
//	public abstract boolean buildObject(List<BuildInfo>list, String topicText);
//
//	public abstract boolean buildObject(List<BuildInfo> list, String parentText, String topicText);
//
//	public abstract void buildObject(List<BuildInfo> list);
//
//	public abstract void buildModule(List<BuildInfo> list, BuildInfo data);
//
//	public abstract void buildParamsList(List<String> paramsList, BuildInfo data);
//	
	//GET TEXT ****************************************************** 
	
//	public abstract String getModuleText(BuildInfo data);
//
//	public abstract String getProcText(BuildInfo data);
	
	//HIERARCHY ****************************************************** 
	
//	void getCalls(List<BuildInfo> list, BuildInfo data, boolean callsInObject, IProgressMonitor monitor);
//
//	void getCalled(List<BuildInfo> list, BuildInfo data);
//
//	void getProcsInLine(List<BuildInfo> list, String line, BuildInfo context);

//	public abstract boolean checkVersion(String version);

	//COMPARE ****************************************************** 
	
//	public abstract void compareModules(IDb nonActive, CompareResults compareResults,
//			BuildInfo data, IProgressMonitor monitor);
//
//	public abstract void compareObjects(IDb nonActive,
//			CompareResults compareResults, String parentText, String topicText, IProgressMonitor monitor);
//
//	public abstract void compareObjects(IDb nonActive,
//			CompareResults compareResults, String topicText, IProgressMonitor monitor);
//	
//	public abstract void compareObjects(IDb nonActive,
//			CompareResults compareResults, IProgressMonitor monitor);



































}
