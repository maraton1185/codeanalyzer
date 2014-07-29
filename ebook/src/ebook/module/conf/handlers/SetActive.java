package ebook.module.conf.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;

import ebook.module.confList.tree.ListConfInfo;

public class SetActive {
	@Execute
	public void execute() {
		// dbm.setActive(db);
		//
		// PreferenceSupplier.set(PreferenceSupplier.BASE_ACTIVE, db.getId());
		// PreferenceSupplier.save();

		// br.post(Events.EVENT_UPDATE_CONFIG_LIST, null);
	}

	@CanExecute
	public boolean canExecute(@Optional @Active ListConfInfo item) {
		return item != null;
	}

}