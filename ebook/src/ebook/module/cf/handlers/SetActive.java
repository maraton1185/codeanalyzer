package ebook.module.cf.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;

import ebook.module.cf.interfaces.ICf;
import ebook.module.cf.interfaces.ICfManager;
import ebook.utils.Events;
import ebook.utils.PreferenceSupplier;

public class SetActive {
	@Execute
	public void execute(ICf db, IEventBroker br, ICfManager dbm) {
		dbm.setActive(db);

		PreferenceSupplier.set(PreferenceSupplier.BASE_ACTIVE, db.getId());
		PreferenceSupplier.save();

		// Preferences pref =
		// ConfigurationScope.INSTANCE.getNode(Strings.get("P_NODE"));
		// pref.put(Strings.get("P_BASE_ACTIVE"), db.getId());
		// try {
		// pref.flush();
		// } catch (BackingStoreException e) {
		// e.printStackTrace();
		// }

		br.post(Events.EVENT_UPDATE_CONFIG_LIST, null);
	}

	@CanExecute
	public boolean canExecute(@Optional ICf db) {
		return db != null;
	}

}