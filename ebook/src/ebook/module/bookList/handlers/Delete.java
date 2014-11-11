package ebook.module.bookList.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.confList.tree.ListConfInfo;
import ebook.module.tree.item.ITreeItemSelection;
import ebook.utils.Strings;

public class Delete {
	@Execute
	public void execute(@Optional ListConfInfo db, Shell shell,
			@Named("bookListSelection") ITreeItemSelection selection) {

		if (!MessageDialog.openConfirm(shell, Strings.title("appTitle"),
				"Удалить элемент из списка?"))
			return;

		App.mng.blm().delete(selection, shell);
	}

	@CanExecute
	public boolean canExecute(
			@Optional @Active @Named("bookListSelection") ITreeItemSelection selection) {
		return selection != null && !selection.isEmpty();
	}
}