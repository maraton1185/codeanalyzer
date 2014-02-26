 
package codeanalyzer.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;

import codeanalyzer.core.interfaces.IDb;
import codeanalyzer.core.interfaces.IDbManager;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.PreferenceSupplier;

public class ConfigListDelete {
	@Execute
	public void execute(@Optional @Named(Const.CONTEXT_SELECTED_DB) IDb db, IEventBroker br, IDbManager dbm) {
		if(db==null)
			return;
		
		PreferenceSupplier.remove(db.getId());
		PreferenceSupplier.save();
		
//		Preferences pref = ConfigurationScope.INSTANCE.getNode(Strings.get("P_BASE_NODE"));
//		pref.remove(db.getId());
//		try {
//			pref.flush();
//		} catch (BackingStoreException e) {
//			e.printStackTrace();
//		}
		
		dbm.remove(db);
		br.post(Const.EVENT_UPDATE_CONFIG_LIST, null);
	}
		
	@CanExecute
	public boolean canExecute(@Optional @Named(Const.CONTEXT_SELECTED_DB) IDb db) {
		return db!=null;
	}
}