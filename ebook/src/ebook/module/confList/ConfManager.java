package ebook.module.confList;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.conf.ConfConnection;
import ebook.module.confList.tree.ListConfInfo;
import ebook.module.confList.tree.ListConfInfoOptions;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.TreeManager;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class ConfManager extends TreeManager {

	public ConfManager() {
		super(App.srv.cls());

	}

	@Override
	public void add(ITreeItemInfo parent, Shell shell) {
		InputDialog dlg = new InputDialog(shell,
				ebook.utils.Strings.get("appTitle"),
				"Введите имя файла конфигурации:",
				ebook.utils.Strings.get("confFileName"), null);
		if (dlg.open() == Window.OK) {
			try {
				ConfConnection con = new ConfConnection(dlg.getValue());

				ListConfInfoOptions opt = new ListConfInfoOptions();
				opt.path = con.getFullName();
				ListConfInfo data = new ListConfInfo(opt);
				data.setTitle(con.getName());
				data.setGroup(false);

				// data.options = opt;
				srv.add(data, parent, true);

			} catch (InvocationTargetException e) {
				MessageDialog
						.openError(
								shell,
								Strings.get("appTitle"),
								"Ошибка создания конфигурации. \nВозможно, в каталоге конфигураций уже существует файл с таким именем.");
			}
		}
	}

	@Override
	public void addToList(final ITreeItemInfo parent, final Shell shell) {
		final List<IPath> files = Utils.browseFileMulti(
				new Path(PreferenceSupplier
						.get(PreferenceSupplier.DEFAULT_BOOK_DIRECTORY)),
				shell, Strings.get("appTitle"), "*.db");
		if (files == null)
			return;

		BusyIndicator.showWhile(shell.getDisplay(), new Runnable() {
			@Override
			public void run() {
				for (IPath path : files) {
					try {

						ConfConnection con = new ConfConnection(path);

						ListConfInfoOptions opt = new ListConfInfoOptions();
						opt.path = con.getFullName();
						ListConfInfo data = new ListConfInfo(opt);
						data.setTitle(con.getName());
						data.setGroup(false);

						srv.add(data, parent, true);

					} catch (InvocationTargetException e) {

						if (files.size() > 1
								&& !MessageDialog.openConfirm(
										shell,
										Strings.get("appTitle"),
										"Ошибка открытия конфигурации: "
												+ path
												+ "\nВозможно, структура конфигурации не соответствует ожидаемой."
												+ "\nПродолжить?"))

							return;
					}
				}
			}
		});
	}

	@Override
	public void addGroup(ITreeItemInfo parent, Shell shell) {
		InputDialog dlg = new InputDialog(shell,
				ebook.utils.Strings.get("appTitle"),
				"Введите название группы:", "", null);
		if (dlg.open() == Window.OK)
			try {

				ListConfInfo data = new ListConfInfo();
				data.setTitle(dlg.getValue());
				data.setGroup(true);
				srv.add(data, parent, false);

				// bm.add((ITreeItemInfo) data);

			} catch (InvocationTargetException e) {
				MessageDialog.openError(shell, Strings.get("appTitle"),
						"Ошибка создании группы.");
			}
	}

	@Override
	public void addSubGroup(ITreeItemInfo parent, Shell shell) {
		InputDialog dlg = new InputDialog(shell,
				ebook.utils.Strings.get("appTitle"),
				"Введите название подгруппы:", "", null);
		if (dlg.open() == Window.OK)
			try {

				ListConfInfo data = new ListConfInfo();
				data.setTitle(dlg.getValue());
				data.setGroup(true);
				srv.add(data, parent, true);

			} catch (InvocationTargetException e) {
				MessageDialog.openError(shell, Strings.get("appTitle"),
						"Ошибка создания подгруппы.");
			}
	}

	@Override
	public boolean save(ITreeItemInfo data, Shell shell) {
		// NEXT Auto-generated method stub
		return false;
	}

}
