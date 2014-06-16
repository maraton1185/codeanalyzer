package ebook.module.confList.handlers;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import ebook.module.confList.IConfManager;
import ebook.module.confList.tree.ListConfInfo;
import ebook.utils.Strings;

public class AddGroup {
	@Execute
	public void execute(Shell shell, IConfManager mng,
			@Optional ListConfInfo selected) {
		InputDialog dlg = new InputDialog(shell,
				ebook.utils.Strings.get("appTitle"),
				"¬ведите название группы:", "", null);
		if (dlg.open() == Window.OK)
			try {

				ListConfInfo data = new ListConfInfo();
				data.setTitle(dlg.getValue());
				data.setGroup(true);
				mng.addGroup(data, selected, false);

				// bm.add((ITreeItemInfo) data);

			} catch (InvocationTargetException e) {
				MessageDialog.openError(shell, Strings.get("appTitle"),
						"ќшибка создании группы.");
			}

	}

}