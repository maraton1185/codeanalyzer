package ebook.module.conf;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ListInfo;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.TreeManager;
import ebook.utils.Strings;

public class ConfManager extends TreeManager {

	public ConfManager(ConfConnection con, ListInfo list) {
		super(con.srv(list));

	}

	@Override
	public void addGroup(ITreeItemInfo parent, Shell shell) {
		try {

			ContextInfo data = new ContextInfo();
			data.setTitle(Strings.get("s.newsection.title"));
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

			ContextInfo data = new ContextInfo();
			data.setTitle(Strings.get("s.newsection.title"));
			data.setGroup(true);
			srv.add(data, parent, true);

		} catch (InvocationTargetException e) {
			MessageDialog.openError(shell, Strings.get("appTitle"),
					"Ошибка создания подраздела.");
		}

	}

	public void build(ContextInfo item, Shell shell) {
		// TODO Auto-generated method stub

	}

}
