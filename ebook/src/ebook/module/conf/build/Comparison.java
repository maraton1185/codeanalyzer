package ebook.module.conf.build;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import ebook.core.App;
import ebook.core.pico;
import ebook.core.exceptions.DbLicenseException;
import ebook.module.conf.ConfConnection;
import ebook.module.conf.model.AdditionalInfo;
import ebook.module.conf.model.BuildInfo;
import ebook.module.conf.model.BuildType;
import ebook.module.conf.model.CompareResults;
import ebook.module.conf.service.ConfService;
import ebook.module.conf.service.ConfTreeService;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.confList.tree.ListConfInfo;
import ebook.module.confLoad.interfaces.ICfServices;
import ebook.module.confLoad.services.TextBuffer;
import ebook.module.confLoad.services.TextParser;
import ebook.module.text.TextConnection;
import ebook.module.text.model.LineInfo;
import ebook.module.tree.item.ITreeItemInfo;
import ebook.utils.Const;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_TEXT_DATA;
import ebook.utils.PreferenceSupplier;

public class Comparison {

	private ConfService srv;
	TextParser parser = pico.get(ICfServices.class).parse();
	TextBuffer buffer = pico.get(ICfServices.class).buffer();

	public Comparison(ConfService srv) {
		this.srv = srv;
	}

	public void build(List<BuildInfo> proposals, Integer gr, String title,
			AdditionalInfo info, boolean root) {

		if (proposals != null)
			proposals.clear();

		BuildInfo error = new BuildInfo();

		if (gr == null && !root) {
			error.title = "Не найден контекст сравнения";
			proposals.add(error);
			return;
		}

		ConfConnection con = getConnection(proposals);

		if (con == null)
			return;

		ConfTreeService db1 = srv.conf();
		ConfTreeService db2 = con.conf();

		if (root) {
			listRoot(db1, db2, proposals, info.comparisonWithEquals);
			return;
		}

		if (!info.getProc)
			db1.setObjectsTable();
		ContextInfo item1 = (ContextInfo) db1.get(gr);

		db1.setProcTable();

		if (item1 == null) {
			error.title = "Не найден контекст сравнения";
			proposals.add(error);
			return;
		}

		item1.setProc(info.getProc);
		List<String> _path = new ArrayList<String>();
		String path = db1.getPath(item1, _path, true);
		ContextInfo item2 = db2.getByPath(path);

		if (item2 == null) {
			error.title = "В конфигурации для сравнения не найден контекст сравнения";
			proposals.add(error);
			return;
		}

		if (item1.isProc())
			listProcs(db1, item1, db2, item2, proposals, _path);
		else
			listObjects(db1, item1, db2, item2, proposals,
					info.comparisonWithEquals);

	}

	private void listRoot(ConfTreeService db1, ConfTreeService db2,
			List<BuildInfo> proposals, boolean comparisonWithEquals) {

		db1.setObjectsTable();
		List<ITreeItemInfo> root = db1.getRoot();
		db1.setProcTable();

		for (ITreeItemInfo item1 : root) {

			List<String> _path = new ArrayList<String>();
			String path = db1.getPath((ContextInfo) item1, _path, true);
			ContextInfo item2 = db2.getByPath(path);

			if (item2 == null) {
				continue;
			}

			List<BuildInfo> _proposals = new ArrayList<BuildInfo>();
			boolean diffs = objectsDiffs(db1, item1, db2, item2, _proposals,
					comparisonWithEquals);

			if (diffs) {
				BuildInfo category = new BuildInfo();
				category.title = item1.getTitle();
				proposals.add(category);
				category.children.addAll(_proposals);
			}

		}

		db2.setObjectsTable();
		root = db2.getRoot();
		db2.setProcTable();

		for (ITreeItemInfo item2 : root) {
			List<String> _path = new ArrayList<String>();
			String path = db2.getPath((ContextInfo) item2, _path, true);
			ContextInfo item1 = db1.getByPath(path);

			if (item1 != null)
				continue;

			BuildInfo category = new BuildInfo();
			category.title = item2.getTitle();
			proposals.add(category);

		}

	}

	private void listProcs(ConfTreeService db1, ContextInfo item1,
			ConfTreeService db2, ContextInfo item2, List<BuildInfo> proposals,
			List<String> path) {

		BuildInfo info = new BuildInfo();

		String t1 = db1.getHash(item1);
		String t2 = db2.getHash(item2);

		if (t1.equalsIgnoreCase(t2)) {
			info.title = "процедуры идентичны";
		} else {

			String t = parser.compare(db1.getText(item1.getId()),
					db2.getText(item2.getId()));
			buffer.setText(t);
			item1.getOptions().compare = true;
			db1.adaptProc(item1, item1.getTitle(), item1.getParent(), path);
			TextConnection text_con = srv.textConnection(item1);

			LineInfo line = new LineInfo(item1.getOptions());
			text_con.setLine(line);
			App.br.post(Events.EVENT_OPEN_TEXT, text_con);
			App.br.post(Events.EVENT_TEXT_VIEW_UPDATE, new EVENT_TEXT_DATA(
					item1));
			info.title = "есть различия: открыть версию конфигурации для сравнения";
			info.openInComparison = true;
		}

		proposals.add(info);
	}

	private void listObjects(ConfTreeService db1, ContextInfo item1,
			ConfTreeService db2, ContextInfo item2, List<BuildInfo> proposals,
			boolean comparisonWithEquals) {

		BuildInfo info = new BuildInfo();

		String t1 = db1.getHash(item1);
		String t2 = db2.getHash(item2);

		if (t1.equalsIgnoreCase(t2)) {
			info.title = "объекты идентичны";
			proposals.add(info);
		} else {

			objectsDiffs(db1, item1, db2, item2, proposals,
					comparisonWithEquals);
		}

	}

	private boolean objectsDiffs(ConfTreeService db1, ITreeItemInfo _item1,
			ConfTreeService db2, ITreeItemInfo _item2,
			List<BuildInfo> proposals, boolean comparisonWithEquals) {

		CompareResults compareResults = new CompareResults();

		List<ITreeItemInfo> list2 = db2.getChildren(_item2.getId());
		List<ITreeItemInfo> list1 = db1.getChildren(_item1.getId());

		for (ITreeItemInfo item2 : list2) {

			String text2 = db2.getHash((ContextInfo) item2);
			boolean equals = false;
			boolean added = true;
			boolean changed = false;

			for (ITreeItemInfo item1 : list1) {

				if (item1.getTitle().equalsIgnoreCase(item2.getTitle())) {

					added = false;

					String text1 = db1.getHash((ContextInfo) item1);

					if (text1.equalsIgnoreCase(text2))
						equals = true;
					else
						changed = true;

					list1.remove(item1);
					break;
				}
			}

			if (equals)
				compareResults.equals.add(item2);
			if (changed)
				compareResults.changed.add(item2);
			if (added)
				compareResults.added.add(item2);

		}

		for (ITreeItemInfo item1 : list1) {

			boolean removed = true;
			for (ITreeItemInfo item2 : list2) {
				if (item1.getTitle().equalsIgnoreCase(item2.getTitle())) {
					removed = false;
					list2.remove(item2);
					break;
				}
			}

			if (removed)
				compareResults.removed.add(item1);

		}

		makeProposals(Const.COMPARE_REMOVED, compareResults.added, proposals);
		makeProposals(Const.COMPARE_ADDED, compareResults.removed, proposals);
		makeProposals(Const.COMPARE_CHANGED, compareResults.changed, proposals);
		if (comparisonWithEquals)
			makeProposals(Const.COMPARE_EQUALS, compareResults.equals,
					proposals);

		return !compareResults.isEmpty();

	}

	private void makeProposals(String title, List<ITreeItemInfo> list,
			List<BuildInfo> proposals) {

		BuildInfo category = new BuildInfo();
		category.title = title;
		category.type = BuildType.proposal;
		for (ITreeItemInfo item : list) {
			BuildInfo info = new BuildInfo();
			info.title = item.getTitle();
			category.children.add(info);
		}
		if (!list.isEmpty())
			proposals.add(category);

	}

	public ConfConnection getConnection(List<BuildInfo> proposals) {

		BuildInfo error = new BuildInfo();

		String name = PreferenceSupplier
				.get(PreferenceSupplier.CONF_LIST_VIEW_COMPARISON);
		if (name == null)
			return null;

		ListConfInfo item = (ListConfInfo) App.srv.cl().getTreeItem(name, "");
		if (item == null) {
			error.title = "Не установлена конфигурация для сравнения";
			if (proposals != null)
				proposals.add(error);
			return null;
		}

		ConfConnection con = null;
		try {

			con = new ConfConnection(item.getPath(), true, true);

		} catch (InvocationTargetException e) {

			if (e.getTargetException() instanceof DbLicenseException)
				error.title = "Ошибка открытия конфигурации для сравнения. (Лицензия)";
			else
				error.title = "Ошибка открытия конфигурации для сравнения.";
			if (proposals != null)
				proposals.add(error);
			return null;
		}

		return con;
	}
}
