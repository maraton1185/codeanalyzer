 
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

public class ConfigListSetActive {
	@Execute
	public void execute(@Named(Const.CONTEXT_SELECTED_DB) IDb db, IEventBroker br, IDbManager dbm) {
		dbm.setActive(db);
		
		PreferenceSupplier.set(PreferenceSupplier.BASE_ACTIVE, db.getId());
		PreferenceSupplier.save();
		
//		Preferences pref = ConfigurationScope.INSTANCE.getNode(Strings.get("P_NODE"));
//		pref.put(Strings.get("P_BASE_ACTIVE"), db.getId());
//		try {
//			pref.flush();
//		} catch (BackingStoreException e) {
//			e.printStackTrace();
//		}
		
		br.post(Const.EVENT_UPDATE_CONFIG_LIST, null);
	}
		
	@CanExecute
	public boolean canExecute(@Optional @Named(Const.CONTEXT_SELECTED_DB) IDb db) {
		return db!=null;
	}
		
}