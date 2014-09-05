package ebook.module.confList.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.confList.tree.ListConfInfo;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_TREE_DATA;
import ebook.utils.PreferenceSupplier;

public class Comparison {
	@Execute
	public void execute(ListConfInfo item, Shell shell) {

		String name = PreferenceSupplier
				.get(PreferenceSupplier.CONF_LIST_VIEW_COMPARISON);
		if (name == null)
			return;

		if (name.equals(item.getDbName()))

			PreferenceSupplier.set(
					PreferenceSupplier.CONF_LIST_VIEW_COMPARISON, 0);
		else

			PreferenceSupplier.set(
					PreferenceSupplier.CONF_LIST_VIEW_COMPARISON,
					item.getDbName());

		PreferenceSupplier.save();

		App.br.post(Events.EVENT_UPDATE_CONF_LIST, new EVENT_UPDATE_TREE_DATA(
				App.srv.cl().get(item.getParent()), item));

	}

	@CanExecute
	public boolean canExecute(@Optional @Active ListConfInfo item) {
		return item != null && !item.isGroup();
	}

}