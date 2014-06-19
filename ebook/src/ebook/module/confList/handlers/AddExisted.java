package ebook.module.confList.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.confList.tree.ListConfInfo;

public class AddExisted {
	@Execute
	public void execute(final Shell shell, @Optional final ListConfInfo parent) {

		App.mng.clm().addToList(parent, shell);

	}

}