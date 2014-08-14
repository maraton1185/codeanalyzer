package ebook.module.conf;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import ebook.core.pico;
import ebook.module.conf.model.AdditionalInfo;
import ebook.module.conf.model.BuildInfo;
import ebook.module.conf.model.BuildType;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ContextInfoOptions;
import ebook.module.conf.tree.ListInfo;
import ebook.module.confLoad.interfaces.ICfServices;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.TreeManager;
import ebook.utils.Strings;

public class ConfManager extends TreeManager {

	public ICfServices cf = pico.get(ICfServices.class);

	public ConfManager(ConfConnection con, ListInfo list) {
		super(con.srv(list));

	}

	@Override
	public void addGroup(ITreeItemInfo parent, Shell shell) {
		try {

			ContextInfo data = new ContextInfo();
			data.setTitle(Strings.value("section"));
			data.setGroup(true);
			srv.add(data, parent, false);

		} catch (InvocationTargetException e) {
			MessageDialog.openError(shell, Strings.title("appTitle"),
					"Ошибка создания раздела.");
		}

	}

	@Override
	public void addSubGroup(ITreeItemInfo parent, Shell shell) {
		try {

			ContextInfo data = new ContextInfo();
			data.setTitle(Strings.value("section"));
			data.setGroup(true);
			srv.add(data, parent, true);

		} catch (InvocationTargetException e) {
			MessageDialog.openError(shell, Strings.title("appTitle"),
					"Ошибка создания подраздела.");
		}

	}

	public void build(ContextInfo item, Shell shell) {

		try {
			ContextInfoOptions opt = item.getOptions();

			// delete children
			srv.deleteChildren(item);

			// set connection to build
			cf.build().setConnection(srv.getConnection());

			List<BuildInfo> list = new ArrayList<BuildInfo>();

			// root
			if (opt.type == BuildType.root)
				list = cf.build().buildRoot();

			// type is null
			if (opt.type == null) {

				ITreeItemInfo parent = item;
				List<ITreeItemInfo> path = new ArrayList<ITreeItemInfo>();

				while (parent == null ? false : !parent.isRoot()) {

					parent = srv.get(parent.getParent());

					ContextInfoOptions opt1 = (ContextInfoOptions) parent
							.getOptions();

					// have root
					if (opt1.type == BuildType.root)
						break;

					if (opt1.type != BuildType.object)
						path.add(0, parent);

					// have type before root
					// if (opt1.type != null) {
					// parent = null;
					// break;
					// }
				}

				AdditionalInfo info = new AdditionalInfo();
				info.type = BuildType.object;
				if (parent != null) {
					// get root without type between
					info.type = null;
					list = cf.build().buildWithRootPath(path, item, info);
				}

				if (info.type != null) {
					// set type object
					opt.type = info.type;
					srv.saveOptions(item);
				}
			}

			// **************************************

			srv.stopUpdate();
			addBuild(list, item);
			srv.startUpdate();
			srv.expand(item);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addBuild(List<BuildInfo> list, ITreeItemInfo parent) {

		for (BuildInfo buildInfo : list) {

			ITreeItemInfo data = ContextInfo.fromBuild(buildInfo);

			try {
				srv.add(data, parent, true);

				addBuild(buildInfo.children, data);

			} catch (InvocationTargetException e) {

			}
		}

	}

	public void buildText(ContextInfo item, Shell shell) {
		try {
			item.getOptions().type = BuildType.text;
			srv.saveOptions(item);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void buildDelete(ContextInfo item, Shell shell) {

		srv.deleteChildren(item);

	}

}
