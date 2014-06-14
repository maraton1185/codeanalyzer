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

public class Add {
	@Execute
	public void execute(Shell shell, IConfManager mng,
			@Optional ListConfInfo parent) {
		InputDialog dlg = new InputDialog(shell,
				ebook.utils.Strings.get("appTitle"),
				"Введите имя файла конфигурации:",
				ebook.utils.Strings.get("confFileName"), null);
		if (dlg.open() == Window.OK) {
			try {
				mng.Add(dlg.getValue(), parent);
			} catch (InvocationTargetException e) {
				MessageDialog
						.openError(
								shell,
								Strings.get("appTitle"),
								"Ошибка создания конфигурации. \nВозможно, в каталоге конфигураций уже существует файл с таким именем.");
			}
		}
	}

}