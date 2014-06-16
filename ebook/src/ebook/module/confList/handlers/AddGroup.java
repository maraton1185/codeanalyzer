package ebook.module.confList.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.confList.tree.ListConfInfo;

public class AddGroup {
	@Execute
	public void execute(Shell shell, @Optional ListConfInfo parent) {

		App.mng.cm().addGroup(parent, shell);

	}

}