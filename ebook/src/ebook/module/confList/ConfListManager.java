package ebook.module.confList;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.core.exceptions.DbLicenseException;
import ebook.module.book.BookConnection;
import ebook.module.book.ContextService;
import ebook.module.conf.ConfConnection;
import ebook.module.conf.ConfOptions;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ListInfo;
import ebook.module.confList.tree.ListConfInfo;
import ebook.module.confList.tree.ListConfInfoOptions;
import ebook.module.confLoad.LoadDialog;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.ITreeItemSelection;
import ebook.module.tree.TreeManager;
import ebook.utils.Events;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class ConfListManager extends TreeManager {

	public ConfListManager() {
		super(App.srv.cl());

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
				ListConfInfo data = new ListConfInfo(opt);
				data.setTitle(con.getTitle());
				data.setGroup(false);
				data.setPath(con.getName());
				// data.setDbFullPath(con.getName());
				// data.options = opt;
				srv.add(data, parent, true);

				new LoadDialog(shell, data).open();

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
						.get(PreferenceSupplier.DEFAULT_CONF_DIRECTORY)),
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

						ConfOptions _opt = con.srv(null).getRootOptions(
								ConfOptions.class);
						if (_opt != null) {
							opt.status = _opt.status;
							opt.status_date = _opt.status_date;
							opt.link_status = _opt.link_status;
							opt.link_status_date = _opt.link_status_date;
						}

						ListConfInfo data = new ListConfInfo(opt);
						data.setTitle(con.getTitle());
						data.setGroup(false);
						data.setPath(con.getName());
						// data.setDbFullPath(con.getName());

						srv.add(data, parent, true);

					} catch (Exception e) {

						if (files.size() > 1
								&& !MessageDialog.openConfirm(
										shell,
										Strings.get("appTitle"),
										"Ошибка открытия конфигурации: "
												+ path
												+ "\nВозможно, структура конфигурации не соответствует ожидаемой."
												+ "\nПродолжить?"))

							return;
						else
							MessageDialog.openError(
									shell,
									Strings.get("appTitle"),
									"Ошибка открытия конфигурации: "
											+ path
											+ "\nВозможно, структура конфигурации не соответствует ожидаемой.");
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
		try {

			srv.saveOptions(data);

		} catch (InvocationTargetException e) {
			MessageDialog.openError(shell, Strings.get("appTitle"),
					"Ошибка сохранения данных конфигурации.");

			return false;
		}

		return true;
	}

	@Override
	public void delete(ITreeItemSelection selection, Shell shell) {
		Iterator<ITreeItemInfo> iterator = selection.iterator();
		while (iterator.hasNext()) {
			try {
				ConfConnection con = new ConfConnection(
						((ListConfInfo) iterator.next()).getPath(), false);
				con.closeConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		super.delete(selection, shell);
	}

	public void open(IPath path, Shell shell) {

		if (path == null)
			return;

		try {

			ConfConnection con = new ConfConnection(path, true, true);
			// con.openConnection();
			App.ctx.set(ConfConnection.class, con);
			App.br.post(Events.EVENT_SHOW_CONF, null);

		} catch (InvocationTargetException e) {

			App.ctx.set(BookConnection.class, null);

			if (shell != null)
				if (e.getTargetException() instanceof DbLicenseException)
					MessageDialog.openError(shell, Strings.get("appTitle"),
							"Ошибка открытия конфигурации. (Лицензия)");
				else
					MessageDialog.openError(shell, Strings.get("appTitle"),
							"Ошибка открытия конфигурации.");
		}
	}

	public void openWithContext(ContextService contextService, IPath path,
			ContextInfo item, Shell shell) {
		if (path == null)
			return;

		try {

			ConfConnection con = new ConfConnection(path, true, true);

			ListInfo newList = App.mng.clm(con).openInNewList(contextService,
					item, shell);

			App.ctx.set(ConfConnection.class, con);
			App.ctx.set(ListInfo.class, newList);
			App.br.post(Events.EVENT_SHOW_CONF, null);

		} catch (InvocationTargetException e) {

			App.ctx.set(BookConnection.class, null);
			App.ctx.set(ListInfo.class, null);

			if (shell != null)
				if (e.getTargetException() instanceof DbLicenseException)
					MessageDialog.openError(shell, Strings.get("appTitle"),
							"Ошибка открытия конфигурации. (Лицензия)");
				else
					MessageDialog.openError(shell, Strings.get("appTitle"),
							"Ошибка открытия конфигурации.");
		}

	}

}
