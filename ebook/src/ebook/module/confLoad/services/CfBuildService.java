package ebook.module.confLoad.services;

import java.io.BufferedReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import ebook.module.conf.ConfService;
import ebook.module.conf.ConfTreeService;
import ebook.module.conf.model.AdditionalInfo;
import ebook.module.conf.model.BuildInfo;
import ebook.module.conf.model.BuildType;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ContextInfoOptions;
import ebook.module.confLoad.model.ELevel;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.ITreeService;

public class CfBuildService {

	private Connection con;

	public void setConnection(Connection con) {
		this.con = con;
	}

	public List<ELevel> getLevels() {
		List<ELevel> levels = new ArrayList<ELevel>();
		levels.add(ELevel.group1);
		levels.add(ELevel.group2);
		levels.add(ELevel.module);
		levels.add(ELevel.proc);
		return levels;
	}

	public Integer get(ELevel level, String title, Integer parent,
			List<BuildInfo> proposals) throws SQLException {
		Integer gr = get(level, title, parent, proposals, true);
		if (gr == null)
			get(level, title, parent, proposals, false);
		return gr;
	}

	public Integer get(ELevel level, String title, Integer parent,
			List<BuildInfo> proposals, boolean exact) throws SQLException {

		if (con == null)
			return null;

		if (proposals != null)
			proposals.clear();

		Integer index = null;

		String SQL;
		PreparedStatement prep;
		ResultSet rs;

		SQL = "Select TITLE, ID, PARENT from OBJECTS WHERE TRUE";

		if (parent != null)
			SQL = SQL.concat(" AND PARENT = ?");

		if (level != null && level != ELevel.proc)
			SQL = SQL.concat(" AND LEVEL=?");

		if (title != null)
			if (proposals == null || exact)

				SQL = SQL.concat(" AND UPPER(TITLE)=UPPER(?)");
			else {
				SQL = SQL.concat(" AND UPPER(TITLE) REGEXP UPPER(?)");
				title = Pattern.quote(title);
			}

		SQL = SQL.concat(" ORDER BY TITLE");

		prep = con.prepareStatement(SQL);

		int prep_index = 0;

		if (parent != null) {
			prep_index++;
			prep.setInt(prep_index, parent);
		}

		if (level != null && level != ELevel.proc) {
			prep_index++;
			prep.setInt(prep_index, level.toInt());
		}

		if (title != null) {
			prep_index++;
			prep.setString(prep_index, title);
		}

		rs = prep.executeQuery();
		int count = 0;
		try {
			while (rs.next()) {
				index = rs.getInt(2);
				count++;

				if (proposals != null) {
					BuildInfo info = new BuildInfo();
					info.title = rs.getString(1);
					info.id = index;
					info.parent = rs.getInt(3);
					if (level == ELevel.module)
						info.type = BuildType.module;

					proposals.add(info);
				}

			}

		} finally {
			rs.close();
		}

		return count > 1 ? null : index;

	}

	public Integer getProcs(String title, Integer object,
			List<BuildInfo> proposals) throws SQLException {

		Integer gr = getProcs(title, object, proposals, true);
		if (gr == null && title != null)
			gr = getProcs(title, object, proposals, false);
		return gr;

	}

	public Integer getProcs(String title, Integer object,
			List<BuildInfo> proposals, boolean exact) throws SQLException {

		if (con == null)
			return null;

		proposals.clear();

		Integer index = null;

		String SQL;
		PreparedStatement prep;
		ResultSet rs;

		SQL = "Select TITLE, ID from PROCS WHERE PARENT = ?";
		if (title != null)
			if (exact)
				SQL = SQL.concat(" AND UPPER(TITLE)=UPPER(?)");
			else {
				SQL = SQL.concat(" AND UPPER(TITLE) REGEXP UPPER(?)");
				title = Pattern.quote(title);
			}

		SQL = SQL.concat(" ORDER BY ID");

		prep = con.prepareStatement(SQL);
		int prep_index = 0;
		prep_index++;
		prep.setInt(prep_index, object);
		if (title != null) {
			prep_index++;
			prep.setString(prep_index, title);
		}

		rs = prep.executeQuery();
		int count = 0;
		try {
			while (rs.next()) {

				BuildInfo info = new BuildInfo();
				info.title = rs.getString(1);
				info.parent = object;
				info.id = rs.getInt(2);
				proposals.add(info);

				index = info.id;
				count++;
			}

		} finally {
			rs.close();
		}

		return count > 1 ? null : index;
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

		// List<BuildInfo> proposals = new ArrayList<BuildInfo>();
		Integer gr = null;

		List<ELevel> levels = new ArrayList<ELevel>();
		if (!info.searchByGroup2)
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

		int index_search_by_text = 1000;

		if (info.searchByText)
			index_search_by_text = path.indexOf(info.itemTitle);

		if (index_search_by_text != 0)
			gr = get(levels.get(0), path.get(0), null, proposals);

		for (int i = 1; i < path.size(); i++) {

			if (i >= index_search_by_text)
				break;

			if (levels.size() <= i)
				break;

			if (levels.get(i) == ELevel.proc && gr != null) {
				gr = getProcs(path.get(i), gr, proposals);
				if (gr != null) {
					info.getProc = true;
					System.out.println("find proc");
					gr = null;
					proposals.clear();
				}
				continue;
			}

			if (gr != null)
				gr = get(levels.get(i), path.get(i), gr, proposals);
			else {

				if (!proposals.isEmpty())
					info.type = BuildType.proposal;
				break;
			}

		}

		if (info.searchByText && !info.getProc) {

			buildWithTextSearch(proposals, gr, info.itemTitle, info);

			// remove last 2 items
			path.remove(path.size() - 1);
			path.remove(path.size() - 1);
			fillParents(proposals, path);
		}

		if (path_items.isEmpty() && proposals.isEmpty() && !info.searchByGroup2
				&& !info.searchByText) {
			info.searchByGroup2 = true;
			buildWithPath(proposals, path_items, info);

			fillParents(proposals, null);
		}

	}

	private void buildWithTextSearch(List<BuildInfo> proposals, Integer gr,
			String title, AdditionalInfo build_opt) throws SQLException {

		if (con == null)
			return;

		if (proposals != null)
			proposals.clear();

		// title = Pattern.quote(title);

		String SQL;
		PreparedStatement prep;
		ResultSet rs;

		SQL = "Select T.TITLE, T.PARENT, T1.TEXT from PROCS AS T INNER JOIN PROCS_TEXT AS T1 ON T1.PROC = T.ID";
		SQL = SQL.concat(" WHERE UPPER(T1.TEXT) REGEXP UPPER(?)");

		if (gr != null)
			SQL = SQL
					.concat(" AND (T.GROUP1 = ? OR T.GROUP2 = ? OR T.MODULE = ?)");

		SQL = SQL.concat(" ORDER BY TITLE");

		prep = con.prepareStatement(SQL);
		prep.setString(1, Pattern.quote(title));
		if (gr != null) {
			prep.setInt(2, gr);
			prep.setInt(3, gr);
			prep.setInt(4, gr);
		}

		BufferedReader bufferedReader = null;
		rs = prep.executeQuery();
		try {
			while (rs.next()) {

				BuildInfo info = new BuildInfo();
				info.title = rs.getString(1);
				info.parent = rs.getInt(2);

				// System.out.println("***************************************");
				// System.out.println(info.title);
				// System.out.println("***************************************");

				// StringBuilder result = new StringBuilder();
				if (!build_opt.textSearchWithoutLines) {
					Reader in = rs.getCharacterStream(3);
					bufferedReader = new BufferedReader(in);
					String line;
					while ((line = bufferedReader.readLine()) != null) {

						// System.out.println(line);

						if (line.toLowerCase().contains(title.toLowerCase())) {
							BuildInfo ch = new BuildInfo();
							ch.title = line.trim();
							info.children.add(ch);
						}
						// result.append(line + "\n");
					}
				}

				proposals.add(info);

			}

		} catch (Exception e) {
			throw new SQLException();
		} finally {
			rs.close();
		}

	}

	private void fillParents(List<BuildInfo> proposals, List<String> context)
			throws SQLException {
		if (proposals.isEmpty())
			return;

		if (context == null)
			context = new ArrayList<String>();

		HashMap<BuildInfo, List<BuildInfo>> parents = new HashMap<BuildInfo, List<BuildInfo>>();
		for (BuildInfo buildInfo : proposals) {

			List<BuildInfo> path = new ArrayList<BuildInfo>();

			BuildInfo root = getObject(buildInfo.parent);
			while (root != null) {

				if (!context.contains(root.title))
					path.add(0, root);
				root = getObject(root.parent);
			}

			parents.put(buildInfo, path);

		}

		// HashSet<BuildInfo> set = new HashSet<BuildInfo>();
		proposals.clear();

		HashMap<Integer, BuildInfo> map = new HashMap<Integer, BuildInfo>();

		for (Map.Entry<BuildInfo, List<BuildInfo>> entry : parents.entrySet()) {

			List<BuildInfo> value = entry.getValue();
			BuildInfo key = entry.getKey();

			if (value.isEmpty()) {
				proposals.add(key);
				continue;
			}

			BuildInfo root = value.get(0);
			BuildInfo fromMap = map.get(root.id);
			if (fromMap == null) {
				map.put(root.id, root);
				proposals.add(root);
				if (root.type == null && context.isEmpty())
					root.type = BuildType.object;
			} else
				root = fromMap;

			for (int i = 1; i < value.size(); i++) {

				BuildInfo item = value.get(i);

				BuildInfo parent = map.get(root.id);
				if (parent == null) {
					map.put(root.id, root);
					root.children.insertSorted(item);
				}

				BuildInfo current = map.get(item.id);
				if (current == null) {
					map.put(item.id, item);
					root.children.insertSorted(item);
				}

				root = map.get(item.id);

			}

			root.children.insertSorted(key);

		}

		java.util.Collections.sort(proposals);

	}

	private BuildInfo getObject(Integer id) throws SQLException {

		if (con == null)
			return null;

		String SQL;
		PreparedStatement prep;
		ResultSet rs;

		SQL = "Select TITLE, ID, PARENT, SORT, LEVEL from OBJECTS WHERE ID = ?";

		prep = con.prepareStatement(SQL);

		prep.setInt(1, id);

		rs = prep.executeQuery();
		BuildInfo info = null;
		try {
			if (rs.next()) {

				info = new BuildInfo();
				info.title = rs.getString(1);
				info.id = rs.getInt(2);
				info.parent = rs.getInt(3);
				info.sort = rs.getInt(4);

				if (rs.getInt(5) == ELevel.module.toInt())
					info.type = BuildType.module;

			}

		} finally {
			rs.close();
		}

		return info;
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

		gr = get(levels.get(0), path.get(0), null, proposals);

		for (int i = 1; i < path.size(); i++) {

			if (levels.size() <= i)
				break;

			if (info.level == null && path.get(i) == null && gr != null)
				return gr;

			if (levels.get(i) == ELevel.proc && gr != null) {
				gr = getProcs(path.get(i), gr, proposals);
				if (gr != null) {
					info.getProc = true;
					return gr;
				}
				continue;
			}

			if (gr != null)
				gr = get(levels.get(i), path.get(i), gr, proposals);

			if (info.level == levels.get(i) && gr != null)
				return gr;
		}

		return null;

	}

	public ContextInfo adapt(ConfTreeService conf, ConfService srv,
			ContextInfo selected) {

		ContextInfo result = new ContextInfo(selected);
		Integer id;
		try {

			List<String> path = new ArrayList<String>();

			id = getId(srv, result, ELevel.proc, path);

			if (id != null) {
				ContextInfo proc = (ContextInfo) conf.get(id);
				result.setParent(-1);
				if (proc != null) {
					Integer i = proc.getParent();
					result.setParent(i);
					result.setModule(i);
				}

				result.setId(id);
				result.setProc(true);
				return result;
			}

			id = getId(srv, result, ELevel.module, path);
			if (id != null) {
				ContextInfo module = (ContextInfo) conf.get(id);
				result.setParent(-1);
				if (module != null) {
					Integer i = module.getParent();
					result.setParent(i);
					result.setModule(null);
				}

				result.setId(id);
				result.setTitle(path.get(path.size() - 1).concat(
						"." + result.getTitle()));
				return result;
			}

			result.canOpen = false;

		} catch (Exception e) {

			e.printStackTrace();
		}
		return result;
	}

}
