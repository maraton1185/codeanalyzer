package ebook.module.conf;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import ebook.module.conf.build.BuildService;
import ebook.module.conf.model.AdditionalInfo;
import ebook.module.conf.model.BuildInfo;
import ebook.module.conf.model.BuildType;
import ebook.module.conf.service.ConfService;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ContextInfoOptions;
import ebook.module.conf.tree.ListInfo;
import ebook.module.tree.item.ITreeItemInfo;
import ebook.module.tree.manager.TreeManager;
import ebook.utils.Strings;

public class ConfManager extends TreeManager {

	// public ICfServices cf = pico.get(ICfServices.class);
	ConfService srv;

	public ConfManager(ConfConnection con, ListInfo list) {
		super(con.srv(list));
		srv = con.srv(list);
	}

	@Override
	public void addGroup(ITreeItemInfo parent, Shell shell) {
		try {

			ContextInfo data = new ContextInfo();
			data.setTitle("");// Strings.value("section"));
			data.setGroup(true);
			srv.add(data, parent, false);
			srv.edit(data);

		} catch (InvocationTargetException e) {
			MessageDialog.openError(shell, Strings.title("appTitle"),
					"Ошибка создания раздела.");
		}

	}

	@Override
	public void addSubGroup(ITreeItemInfo parent, Shell shell) {
		try {

			ContextInfo data = new ContextInfo();
			data.setTitle("");// Strings.value("section"));
			data.setGroup(true);
			srv.add(data, parent, true);
			srv.edit(data);

		} catch (InvocationTargetException e) {
			MessageDialog.openError(shell, Strings.title("appTitle"),
					"Ошибка создания подраздела.");
		}

	}

	public void build(ContextInfo item, AdditionalInfo build_options) {

		try {
			ContextInfoOptions opt = item.getOptions();

			// delete children
			srv.deleteChildren(item);

			List<BuildInfo> list = new ArrayList<BuildInfo>();

			// root
			if (opt.type == BuildType.root)
				list = srv.build().buildRoot();
			else
				buildPath(list, item, build_options);

			// **************************************

			srv.stopUpdate();
			addBuild(list, item);
			srv.startUpdate();
			srv.expand(item);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void buildPath(List<BuildInfo> list, ContextInfo item,
			AdditionalInfo build_options) throws SQLException,
			InvocationTargetException, IllegalAccessException {

		BuildService build = srv.build();

		ContextInfoOptions opt = item.getOptions();
		List<String> path = new ArrayList<String>();
		AdditionalInfo info = new AdditionalInfo();
		info.itemTitle = item.getTitle();
		ITreeItemInfo root = build.getPath(srv, item, info, opt, path);

		info.type = BuildType.object;

		info.textWithoutLines = build_options.textWithoutLines;
		info.setText(opt.type == BuildType.text);
		info.setProc(opt.type == BuildType.proc);
		info.setComparison(opt.type == BuildType.comparison);

		if (root != null) {
			// get root without type between
			info.type = null;
			build.buildWithPath(list, path, info);
		}

		if ((info.text || info.proc || info.comparison) && root == null) {
			// build root
			info.type = null;
			path.clear();
			build.buildWithPath(list, path, info);
		}

		if (info.type != null) {
			// set type object
			opt.type = info.type;
			srv.saveOptions(item);
		}

		if (info.type == BuildType.object) {
			buildPath(list, item, build_options);
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

	public void buildText(ContextInfo item, AdditionalInfo build_options) {
		try {
			item.getOptions().type = BuildType.text;
			srv.saveOptions(item);
			build(item, build_options);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void buildDelete(ContextInfo item) {
		try {
			srv.deleteChildren(item);

			ContextInfoOptions opt = item.getOptions();
			if (opt.type == BuildType.proposal) {
				opt.type = null;
				srv.saveOptions(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void buildComparison(ContextInfo item, AdditionalInfo build_options) {
		try {
			item.getOptions().type = BuildType.comparison;
			srv.saveOptions(item);
			build(item, build_options);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void buildModule(ContextInfo item, AdditionalInfo build_options) {
		try {
			item.getOptions().type = BuildType.module;
			srv.saveOptions(item);
			build(item, build_options);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void buildRoot(ContextInfo item, AdditionalInfo build_options) {
		try {
			item.getOptions().type = BuildType.root;
			srv.saveOptions(item);
			build(item, build_options);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void buildObject(ContextInfo item, AdditionalInfo build_options) {
		try {
			item.getOptions().type = BuildType.object;
			srv.saveOptions(item);
			build(item, build_options);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
