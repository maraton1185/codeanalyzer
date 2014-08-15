package ebook.module.confLoad.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ebook.module.conf.model.AdditionalInfo;
import ebook.module.conf.model.BuildInfo;
import ebook.module.conf.model.BuildType;
import ebook.module.confLoad.model.ELevel;
import ebook.module.tree.ITreeItemInfo;

public class CfBuildService {

	private Connection con;

	public void setConnection(Connection con) {
		this.con = con;
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
			else
				SQL = SQL.concat(" AND UPPER(TITLE) REGEXP UPPER(?)");

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

	private Integer getProcs(String title, Integer object,
			List<BuildInfo> proposals) throws SQLException {

		Integer gr = getProcs(title, object, proposals, true);
		if (gr == null)
			gr = getProcs(title, object, proposals, false);
		return gr;

	}

	private Integer getProcs(String title, Integer object,
			List<BuildInfo> proposals, boolean exact) throws SQLException {

		if (con == null)
			return null;

		proposals.clear();

		Integer index = null;

		String SQL;
		PreparedStatement prep;
		ResultSet rs;

		SQL = "Select TITLE, ID from PROCS WHERE OBJECT = ?";
		if (title != null)
			if (exact)
				SQL = SQL.concat(" AND UPPER(TITLE)=UPPER(?)");
			else
				SQL = SQL.concat(" AND UPPER(TITLE) REGEXP UPPER(?)");

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
				info.id = null;
				proposals.add(info);

				index = rs.getInt(2);
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
			List<ITreeItemInfo> path_items, ITreeItemInfo item,
			AdditionalInfo info) throws SQLException {

		// List<BuildInfo> proposals = new ArrayList<BuildInfo>();
		Integer gr = null;

		List<ELevel> levels = new ArrayList<ELevel>();
		if (!info.searchByGroup2)
			levels.add(ELevel.group1);
		levels.add(ELevel.group2);
		levels.add(ELevel.module);
		levels.add(ELevel.proc);
		levels.add(null);

		List<String> path = new ArrayList<String>();

		for (ITreeItemInfo p : path_items)
			path.add(p.getTitle());

		path.add(item.getTitle());
		path.add(null);

		int index_search_by_text = 1000;

		if (info.searchByText)
			index_search_by_text = path.indexOf(item.getTitle());

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
				}
				continue;
			}

			if (gr != null)
				gr = get(levels.get(i), path.get(i), gr, proposals);
			else {
				info.type = BuildType.proposal;
				break;
			}

		}

		if (info.searchByText && !info.getProc) {

			buildWithTextSearch(proposals, gr, item.getTitle());

			fillParents(proposals);
		}

		if (path_items.isEmpty() && proposals.isEmpty() && !info.searchByGroup2
				&& !info.searchByText) {
			info.searchByGroup2 = true;
			buildWithPath(proposals, path_items, item, info);

			fillParents(proposals);
		}

	}

	private void buildWithTextSearch(List<BuildInfo> proposals, Integer gr,
			String title) throws SQLException {

		if (con == null)
			return;

		if (proposals != null)
			proposals.clear();

		String SQL;
		PreparedStatement prep;
		ResultSet rs;

		SQL = "Select T.TITLE, T.OBJECT from PROCS AS T INNER JOIN PROCS_TEXT AS T1 ON T1.PROC = T.ID";
		SQL = SQL.concat(" WHERE UPPER(T1.TEXT) REGEXP UPPER(?)");

		if (gr != null)
			SQL = SQL
					.concat(" AND (T.GROUP1 = ? OR T.GROUP2 = ? OR T.MODULE = ?)");

		SQL = SQL.concat(" ORDER BY TITLE");

		prep = con.prepareStatement(SQL);
		prep.setString(1, title);
		if (gr != null) {
			prep.setInt(2, gr);
			prep.setInt(3, gr);
			prep.setInt(4, gr);
		}
		rs = prep.executeQuery();
		try {
			while (rs.next()) {

				BuildInfo info = new BuildInfo();
				info.title = rs.getString(1);
				info.parent = rs.getInt(2);

				proposals.add(info);

			}

		} finally {
			rs.close();
		}

	}

	private void fillParents(List<BuildInfo> proposals) throws SQLException {
		if (proposals.isEmpty())
			return;

		HashMap<BuildInfo, List<BuildInfo>> parents = new HashMap<BuildInfo, List<BuildInfo>>();
		for (BuildInfo buildInfo : proposals) {

			List<BuildInfo> path = new ArrayList<BuildInfo>();

			BuildInfo root = getObject(buildInfo.parent);
			while (root != null) {
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

		java.util.Collections.sort(proposals, new Comparator<BuildInfo>() {
			@Override
			public int compare(BuildInfo o1, BuildInfo o2) {
				return o1.sort - o2.sort;
			}
		});

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
}
