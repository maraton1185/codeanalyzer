package ebook.module.conf.build;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ebook.module.conf.ConfConnection;
import ebook.module.conf.model.AdaptData;
import ebook.module.conf.model.AdditionalInfo;
import ebook.module.conf.model.BuildInfo;
import ebook.module.conf.model.BuildType;
import ebook.module.conf.service.ConfService;
import ebook.module.conf.service.ConfTreeService;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ContextInfoOptions;
import ebook.module.confLoad.model.ELevel;
import ebook.module.tree.item.ITreeItemInfo;
import ebook.module.tree.service.ITreeService;

public class BuildService {

	private Connection con;
	private ConfService srv;
	Get get;
	Comparison comparison;
	Build build;

	public BuildService(ConfService srv) {
		this.srv = srv;
		try {
			this.con = srv.getConnection();
		} catch (IllegalAccessException e) {
			con = null;
			e.printStackTrace();
		}

		comparison = new Comparison(srv);
		get = new Get(con);
		build = new Build(con, get);
	}

	public List<BuildInfo> buildRoot() throws SQLException {
		List<BuildInfo> result = new ArrayList<BuildInfo>();

		String SQL;
		PreparedStatement prep;
		ResultSet rs;
		ResultSet rs1;

		SQL = "Select TITLE, ID from OBJECTS WHERE LEVEL=? ORDER BY SORT, TITLE";
		prep = con.prepareStatement(SQL);

		prep.setInt(1, ELevel.group1.toInt());

		rs = prep.executeQuery();

		try {
			while (rs.next()) {

				BuildInfo info = new BuildInfo();
				info.title = rs.getString(1);
				info.group = true;

				int id = rs.getInt(2);
				SQL = "Select TITLE, ID from OBJECTS WHERE LEVEL=? AND PARENT=? ORDER BY TITLE";
				prep = con.prepareStatement(SQL);

				prep.setInt(1, ELevel.group2.toInt());
				prep.setInt(2, id);
				rs1 = prep.executeQuery();
				try {
					while (rs1.next()) {
						BuildInfo info1 = new BuildInfo();
						info1.title = rs1.getString(1);
						info1.group = true;

						info.children.insertSorted(info1);
					}
				} finally {
					rs1.close();
				}
				result.add(info);
			}

		} finally {
			rs.close();
		}

		return result;
	}

	public void buildWithPath(List<BuildInfo> proposals,
			List<String> path_items, AdditionalInfo info) throws SQLException {

		boolean root = path_items.isEmpty();
		// List<BuildInfo> proposals = new ArrayList<BuildInfo>();
		Integer gr = null;

		List<ELevel> levels = new ArrayList<ELevel>();
		if (!info.group2)
			levels.add(ELevel.group1);
		levels.add(ELevel.group2);
		levels.add(ELevel.module);
		levels.add(ELevel.proc);
		levels.add(null);
		// levels.add(ELevel.group2);
		// levels.add(ELevel.module);
		// levels.add(ELevel.proc);
		// levels.add(null);

		List<String> path = new ArrayList<String>();

		for (String p : path_items)
			path.add(p.replace("###", "..."));

		// add 2 items
		path.add(info.itemTitle);
		path.add(null);

		int stop_index = 1000;

		if (info.text || info.proc || info.comparison)
			stop_index = path.indexOf(info.itemTitle);

		if (info.comparison && !info.rootComparison)
			stop_index++;

		if (stop_index != 0)
			gr = get.get(levels.get(0), path.get(0), null, proposals);

		for (int i = 1; i < path.size(); i++) {

			if (i >= stop_index)
				break;

			if (levels.size() <= i)
				break;

			if (levels.get(i) == ELevel.proc && gr != null) {
				gr = get.getProcs(path.get(i), gr, proposals);
				if (gr != null) {
					info.getProc = true;
					// System.out.println("find proc");
					// proposals.clear();
					break;
					// gr = null;
					// proposals.clear();
				}
				continue;
			}

			if (gr != null)
				gr = get.get(levels.get(i), path.get(i), gr, proposals);
			else {

				if (!proposals.isEmpty())
					info.type = BuildType.proposal;
				break;
			}

		}

		if (info.comparison) {

			comparison.build(proposals, gr, info.itemTitle, info,
					info.rootComparison);

			// remove last 2 items
			// path.remove(path.size() - 1);
			// path.remove(path.size() - 1);
			// fillParents(proposals, path);
			return;
		}

		if (info.getProc) {
			build.buildProcText(proposals, gr, info.itemTitle, info);
			return;
		}

		if (info.text) {

			build.buildText(proposals, gr, info.itemTitle, info);

			// remove last 2 items
			path.remove(path.size() - 1);
			path.remove(path.size() - 1);
			build.fillParents(proposals, path);
			return;
		}

		if (info.proc) {

			build.buildProc(proposals, gr, info.itemTitle, info);

			// remove last 2 items
			path.remove(path.size() - 1);
			path.remove(path.size() - 1);
			build.fillParents(proposals, path);
			return;
		}

		if (root && proposals.isEmpty() && !info.group2) {
			info.group2 = true;
			buildWithPath(proposals, path_items, info);

			build.fillParents(proposals, null);
			return;
		}

	}

	public Integer getId(ConfService srv, ContextInfo item, ELevel level,
			List<String> path) throws SQLException {

		ContextInfoOptions opt = item.getOptions();
		AdditionalInfo info = new AdditionalInfo();
		info.itemTitle = item.getTitle();
		info.level = level;

		if (getPath(srv, item, info, opt, path) != null) {
			return getItemByPath(info, path);
		}
		path.clear();
		return null;
	}

	public ITreeItemInfo getPath(ITreeService srv, ContextInfo item,
			AdditionalInfo info, ContextInfoOptions opt, List<String> path) {

		path.clear();

		ITreeItemInfo root = item;

		String[] str;
		List<String> inpath;
		if (opt.type != BuildType.text) {
			str = info.itemTitle.replace("...", "###").split("\\.");
			inpath = Arrays.asList(str);
			if (!inpath.isEmpty()) {
				path.addAll(0, inpath.subList(0, inpath.size() - 1));
				info.itemTitle = inpath.get(inpath.size() - 1);
			}
			if (inpath.size() > 1) {
				info.filter = inpath.get(inpath.size() - 1);
			}
		}
		info.itemTitle = info.itemTitle.replace("###", "...");
		info.filter = info.filter.replace("###", "...");

		boolean getPath = opt.type != BuildType.object;
		while (getPath && root != null) {

			root = srv.get(root.getParent());

			if (root == null)
				break;

			ContextInfoOptions opt1 = (ContextInfoOptions) root.getOptions();

			// have root
			if (opt1.type == BuildType.root)
				break;

			if (opt1.type == BuildType.proposal)
				continue;

			if (opt1.type == BuildType.text)
				continue;

			if (opt1.type == BuildType.proc)
				continue;

			str = root.getTitle().replace("...", "###").split("\\.");
			inpath = Arrays.asList(str);
			// if (inpath.size() > 1)
			path.addAll(0, inpath);

			// path.add(0, root.getTitle());

			if (opt1.type == BuildType.object)
				break;

			// have type before root
			if (opt1.type != null && opt1.type != BuildType.module) {
				root = null;
				break;
			}

		}

		return root;
	}

	public Integer getItemByPath(AdditionalInfo info, List<String> path_items)
			throws SQLException {

		Integer gr = null;

		List<ELevel> levels = new ArrayList<ELevel>();
		levels.add(ELevel.group1);
		levels.add(ELevel.group2);
		levels.add(ELevel.module);
		levels.add(ELevel.proc);

		List<String> path = new ArrayList<String>();

		for (String p : path_items)
			path.add(p.replace("###", "..."));

		// add 2 items
		path.add(info.itemTitle);
		// path.add(null);

		List<BuildInfo> proposals = new ArrayList<BuildInfo>();

		gr = get.get(levels.get(0), path.get(0), null, proposals);

		for (int i = 1; i < path.size(); i++) {

			if (levels.size() <= i)
				break;

			if (info.level == null && path.get(i) == null && gr != null)
				return gr;

			if (levels.get(i) == ELevel.proc && gr != null) {
				gr = get.getProcs(path.get(i), gr, proposals);
				if (gr != null) {
					info.getProc = true;
					return gr;
				}
				continue;
			}

			if (gr != null)
				gr = get.get(levels.get(i), path.get(i), gr, proposals);

			if (info.level == levels.get(i) && gr != null)
				return gr;
		}

		return null;

	}

	public AdaptData adapt(ContextInfo selected) {

		AdaptData result = new AdaptData();

		ConfTreeService conf = srv.conf();

		ContextInfo item = new ContextInfo(selected);
		result.item = item;
		result.text_con = srv.textConnection(item);

		if (!item.getOptions().openInComparison)
			return adaptNormal(conf, result, item);
		else
			return adaptComparison(conf, result, item);

	}

	private AdaptData adaptComparison(ConfTreeService conf, AdaptData result,
			ContextInfo item) {

		Integer id = null;
		try {

			List<String> path = new ArrayList<String>();
			// String proc_name = "";

			id = getId(srv, item, ELevel.proc, path);
			if (id == null)
				return null;

		} catch (Exception e) {

			e.printStackTrace();
		}

		ConfConnection con = comparison.getConnection(null);
		if (con == null)
			return null;

		ConfTreeService db1 = srv.conf();
		ConfTreeService db2 = con.conf();

		ContextInfo item1 = (ContextInfo) db1.get(id);
		if (item1 == null)
			return null;
		List<String> _path = new ArrayList<String>();
		String path = db1.getPath(item1, _path, true);
		ContextInfo item2 = db2.getByPath(path);
		if (item2 == null)
			return null;

		result.item = item2;
		result.text_con = con.srv(null).textConnection(item2);

		con.conf().adaptProc(item2, item2.getTitle(), item2.getParent(), _path);

		return result;
	}

	private AdaptData adaptNormal(ConfTreeService conf, AdaptData result,
			ContextInfo item) {
		Integer id;
		try {

			List<String> path = new ArrayList<String>();
			String proc_name = "";

			id = getId(srv, item, ELevel.proc, path);

			if (id != null) {
				ContextInfo proc = (ContextInfo) conf.get(id);
				if (proc != null)
					proc_name = proc.getTitle();

			}

			id = getId(srv, item, ELevel.module, path);
			if (id != null) {
				conf.adaptProc(item, proc_name, id, path);
				return result;
			}

			item.canOpen = false;

		} catch (Exception e) {

			e.printStackTrace();
		}
		return result;
	}
}
