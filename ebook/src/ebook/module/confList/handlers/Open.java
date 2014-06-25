package ebook.module.confList.handlers;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class Open {
	@Execute
	public void execute(Shell shell) {
		IPath p = Utils.browseFile(
				new Path(PreferenceSupplier
						.get(PreferenceSupplier.DEFAULT_CONF_DIRECTORY)),
				shell, Strings.get("appTitle"), "*.db");
		if (p == null)
			return;

		App.mng.clm().open(p, shell);
	}

}