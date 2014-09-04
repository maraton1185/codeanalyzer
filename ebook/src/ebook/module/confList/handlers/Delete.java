package ebook.module.confList.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.tree.item.ITreeItemSelection;

public class Delete {
	@Execute
	public void execute(Shell shell,
			@Named("confListSelection") ITreeItemSelection selection) {

		App.mng.clm().delete(selection, shell);
	}

	@CanExecute
	public boolean canExecute(
			@Optional @Active @Named("confListSelection") ITreeItemSelection selection) {
		return selection != null && !selection.isEmpty();
	}

}