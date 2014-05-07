package codeanalyzer.handlers.config;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;

import codeanalyzer.core.interfaces.IDb;
import codeanalyzer.core.interfaces.IDbManager;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.PreferenceSupplier;

public class SetNonActive {
	@Execute
	public void execute(IDb db, IEventBroker br, IDbManager dbm) {
		dbm.setNonActive(db);

		PreferenceSupplier.set(PreferenceSupplier.BASE_COMPARE, db.getId());
		PreferenceSupplier.save();

		// Preferences pref =
		// ConfigurationScope.INSTANCE.getNode(Strings.get("P_NODE"));
		// pref.put(Strings.get("P_BASE_COMPARE"), db.getId());
		// try {
		// pref.flush();
		// } catch (BackingStoreException e) {
		// e.printStackTrace();
		// }

		br.post(Const.EVENT_UPDATE_CONFIG_LIST, null);
	}

	@CanExecute
	public boolean canExecute(@Optional IDb db) {
		return db != null;
	}

}