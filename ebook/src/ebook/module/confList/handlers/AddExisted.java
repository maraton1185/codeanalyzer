package ebook.module.confList.handlers;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Shell;

import ebook.module.confList.IConfManager;
import ebook.module.confList.tree.ListConfInfo;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class AddExisted {
	@Execute
	public void execute(final Shell shell, final IConfManager mng,
			@Optional final ListConfInfo parent) {

		final List<IPath> files = Utils.browseFileMulti(
				new Path(PreferenceSupplier
						.get(PreferenceSupplier.DEFAULT_BOOK_DIRECTORY)),
				shell, Strings.get("appTitle"), "*.db");
		if (files == null)
			return;

		BusyIndicator.showWhile(shell.getDisplay(), new Runnable() {
			@Override
			public void run() {
				for (IPath f : files) {
					try {

						mng.addToList(f, parent);

					} catch (InvocationTargetException e) {

						if (files.size() > 1
								&& !MessageDialog.openConfirm(
										shell,
										Strings.get("appTitle"),
										"Ошибка открытия конфигурации: "
												+ f
												+ "\nВозможно, структура конфигурации не соответствует ожидаемой."
												+ "\nПродолжить?"))

							return;
					}
				}
			}
		});
	}

}