package ebook.module.confList.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.tree.item.ITreeItemSelection;
import ebook.utils.Strings;

public class Delete {
	@Execute
	public void execute(Shell shell,
			@Named("confListSelection") ITreeItemSelection selection) {

		if (!MessageDialog.openConfirm(shell, Strings.title("appTitle"),
				"������� ������� �� ������?"))
			return;

		App.mng.clm().delete(selection, shell);
	}

	@CanExecute
	public boolean canExecute(
			@Optional @Active @Named("confListSelection") ITreeItemSelection selection) {
		return selection != null && !selection.isEmpty();
	}

}