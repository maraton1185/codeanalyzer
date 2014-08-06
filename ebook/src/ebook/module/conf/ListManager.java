package ebook.module.conf;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import ebook.module.conf.tree.ListInfo;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.ITreeItemSelection;
import ebook.module.tree.TreeManager;
import ebook.utils.Strings;

public class ListManager extends TreeManager {

	private ConfConnection con;

	public ListManager(ConfConnection con) {
		super(con.lsrv());
		this.con = con;

	}

	@Override
	public void addGroup(ITreeItemInfo parent, Shell shell) {
		try {

			ListInfo data = new ListInfo();
			data.setTitle(Strings.get("s.newlist.title"));
			data.setGroup(true);
			srv.add(data, parent, false);

		} catch (InvocationTargetException e) {
			MessageDialog.openError(shell, Strings.get("appTitle"),
					"Ошибка создания раздела.");
		}

	}

	@Override
	public void addSubGroup(ITreeItemInfo parent, Shell shell) {
		try {

			ListInfo data = new ListInfo();
			data.setTitle(Strings.get("s.newlist.title"));
			data.setGroup(true);
			srv.add(data, parent, true);

		} catch (InvocationTargetException e) {
			MessageDialog.openError(shell, Strings.get("appTitle"),
					"Ошибка создания подраздела.");
		}

	}

	@Override
	public void delete(ITreeItemSelection selection, Shell shell) {

		Iterator<ITreeItemInfo> iterator = selection.iterator();
		while (iterator.hasNext())
			con.remove((ListInfo) iterator.next());

		super.delete(selection, shell);
	}

}
