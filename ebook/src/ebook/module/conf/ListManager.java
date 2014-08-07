package ebook.module.conf;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import ebook.core.exceptions.GetRootException;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ListInfo;
import ebook.module.conf.tree.ListInfoOptions;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.ITreeItemSelection;
import ebook.module.tree.ITreeService;
import ebook.module.tree.TreeManager;
import ebook.utils.Strings;

public class ListManager extends TreeManager {

	private ConfConnection con;

	public ListManager(ConfConnection con) {
		super(con.lsrv());
		this.con = con;

	}

	public ListInfo openInNewList(ITreeService downloadService,
			ContextInfo source, Shell shell) {
		List<ITreeItemInfo> result = srv.getRoot();

		try {
			if (result.isEmpty())
				throw new GetRootException();

			ListInfo lroot = (ListInfo) result.get(0);

			ListInfoOptions opt = new ListInfoOptions();
			ListInfo new_list = new ListInfo(opt);
			new_list.setTitle(Strings.get("s.newlist.title"));
			new_list.setGroup(true);
			srv.add(new_list, lroot, false);

			final File zipFile = File.createTempFile("copycontext", ".zip");

			// ConfService csrv = con.srv(new_list);
			// ListInfo source_list = (ListInfo) srv.get(source.getList());

			downloadService.download(null, source, zipFile.getAbsolutePath());

			result = con.srv(new_list).getRoot();
			if (result.isEmpty())
				throw new GetRootException();

			ContextInfo dest = (ContextInfo) result.get(0);

			con.srv(new_list).upload(zipFile.getAbsolutePath(), dest);

			return new_list;

		} catch (Exception e) {
			MessageDialog.openError(shell, Strings.get("appTitle"),
					"Error open new list.");
		}
		return null;

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
					"������ �������� �������.");
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
					"������ �������� ����������.");
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
