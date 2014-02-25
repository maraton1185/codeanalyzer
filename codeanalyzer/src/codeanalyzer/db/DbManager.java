package codeanalyzer.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import codeanalyzer.core.pico;
import codeanalyzer.core.interfaces.IDb;
import codeanalyzer.core.interfaces.IDbManager;
import codeanalyzer.core.interfaces.ILoaderService.operationType;
import codeanalyzer.utils.Strings;


public class DbManager implements IDbManager{

//	ILoaderService loaderService = pico.get(ILoaderService.class);
//	HashMap<String,IDb> bases = new HashMap<String,IDb>();
	
	List<IDb> dbs = new ArrayList<IDb>();
	IDb active;
	IDb nonActive;
	
	HashMap <operationType, String> operationNames = new HashMap <operationType, String>();
	
	public void init() {
		
		operationNames.put(operationType.fromDirectory, Strings.get("operationType.fromDirectory"));
		operationNames.put(operationType.update, Strings.get("operationType.update"));
		operationNames.put(operationType.fromDb, Strings.get("operationType.fromDb"));
		operationNames.put(operationType.fromSQL, Strings.get("operationType.fromSQL"));
//		operationNames.put(operationType.fillProcLinkTable, "������������ ������� �������� �������");
		
		Preferences pref1 = ConfigurationScope.INSTANCE.getNode(Strings.get("P_NODE"));
		String activeKey = pref1.get(Strings.get("P_BASE_ACTIVE"), "");
		String compareKey = pref1.get(Strings.get("P_BASE_COMPARE"), "");
		
		Preferences pref = ConfigurationScope.INSTANCE.getNode(Strings.get("P_BASE_NODE"));
		try {
			for (String key : pref.keys()) {
				IDb db = pico.get(IDb.class);
				db.load(key);
				dbs.add(db);
				
				if(activeKey.equalsIgnoreCase(key))				
					setActive(db);

				if(compareKey.equalsIgnoreCase(key))				
					setNonActive(db);

			}
				
		} catch (BackingStoreException e) {
			e.printStackTrace();
		} 
		
		if(dbs.size()!=0)
		{
			if(active==null)
				setActive(dbs.get(0));
			
			if(nonActive==null)
				setNonActive(dbs.get(0));
		}
//		IDb db1 = pico.get(IDb.class);
//		db1.load(PreferenceConstants.DB1_STATE, 1, Const.DB1);
//		if(db1.getType()==operationType.fromDb)
//			execute(db1);
//		bases.put(Const.DB1, db1);
//		
//		IDb db2 = pico.get(IDb.class);
//		db2.load(PreferenceConstants.DB2_STATE, 2, Const.DB2);
//		if(db2.getType()==operationType.fromDb)
//			execute(db2);
//		bases.put(Const.DB2, db2);	
		
//		active = db1;
	}
	
	@Override
	public List<IDb> getList() {
		return dbs;
	}
	
	@Override
	public void add(IDb db) {
		dbs.add(db);
		
	}
	@Override
	public void remove(IDb db) {
		dbs.remove(db);		
	}

	@Override
	public IDb getActive() {
		return active;
	}
	
	@Override
	public IDb getNonActive() {
		return nonActive;
	}
	
	@Override
	public void setActive(IDb db) {
		active = db;
	}
	
	@Override
	public void setNonActive(IDb db) {
		nonActive = db;
	}

	@Override
	public String getOperationName(operationType key) {
		String name = operationNames.get(key); 
		return name == null ? "DBManager.getOperationName" : name;
	}
	
	public void execute(final IDb db) {
		
//		IRunnableWithProgress runnable = new IRunnableWithProgress() {
//			public void run(IProgressMonitor monitor)
//					throws InvocationTargetException, InterruptedException {
//								
//				switch (db.getType()) {
//				case fromDirectory:
//					loaderService.loadFromDirectory(db, monitor);
//					break;
//				case fromDb:
//					loaderService.loadFromDb(db, monitor);
//					break;
////				case fillProcLinkTable:
////					loaderService.fillProcLinkTable(db, monitor);
////					break;	
//				case update:
//					loaderService.update(db, monitor);
//					break;
//				case fromSQL:
//					loaderService.loadFromSQL(db, monitor);
//					break;
//				default:
//					throw new InterruptedException();
//				}	
//			}
//		};
//		
//		try {
//			
//			PlatformUI.getWorkbench().getProgressService().run(true, true, runnable);
//			
//		} catch (InvocationTargetException e) {
//			
//			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "������ �������� ������������", e.getMessage());
//			
//			db.setState(DbState.notLoaded);
//			
//		} catch (Exception e) {	
//			
//			db.setState(DbState.notLoaded);
//		}
	}

	
}
