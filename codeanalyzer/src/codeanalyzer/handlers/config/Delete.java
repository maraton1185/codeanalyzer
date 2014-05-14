package codeanalyzer.handlers.config;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;

import codeanalyzer.cf.interfaces.ICf;
import codeanalyzer.cf.interfaces.ICfManager;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.PreferenceSupplier;

public class Delete {
	@Execute
	public void execute(@Optional ICf db, IEventBroker br, ICfManager dbm) {
		if (db == null)
			return;

		PreferenceSupplier.remove(db.getId());
		PreferenceSupplier.save();

		dbm.remove(db);
		br.post(Const.EVENT_UPDATE_CONFIG_LIST, null);
	}

	@CanExecute
	public boolean canExecute(@Optional ICf db) {
		return db != null;
	}
}