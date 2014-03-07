package codeanalyzer.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.core.AppManager;
import codeanalyzer.core.interfaces.IDb;
import codeanalyzer.core.interfaces.IDbManager;
import codeanalyzer.utils.Const;

public class ConfigListExecute {

	@Execute
	public void execute(@Named(Const.CONTEXT_SELECTED_DB) IDb db,
			IDbManager dbManager, Shell shell) {

		dbManager.execute(db, shell);
		AppManager.br.post(Const.EVENT_UPDATE_CONFIG_LIST, null);

	}

	@CanExecute
	public boolean canExecute(@Optional @Named(Const.CONTEXT_SELECTED_DB) IDb db) {
		return db != null;
	}

}
