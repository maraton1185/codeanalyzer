package ebook.module.confLoad.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

		if (con == null)
			return null;

		if (proposals != null)
			proposals.clear();

		Integer index = null;

		String SQL;
		PreparedStatement prep;
		ResultSet rs;

		SQL = "Select TITLE, ID from OBJECTS WHERE "
				+ (parent == null ? "PARENT IS NULL" : "PARENT = ?");

		if (level != null)
			SQL = SQL.concat(" AND LEVEL=?");

		if (title != null)
			if (proposals == null)
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

		if (level != null) {
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
				if (proposals != null) {
					BuildInfo info = new BuildInfo();
					info.title = rs.getString(1);
					proposals.add(info);
				}
				index = rs.getInt(2);
				count++;
			}

		} finally {
			rs.close();
		}

		return count > 1 ? null : index;

	}

	private void getProcs(Integer object, List<BuildInfo> proposals)
			throws SQLException {

		if (con == null)
			return;

		proposals.clear();

		String SQL;
		PreparedStatement prep;
		ResultSet rs;

		SQL = "Select TITLE, ID from PROCS WHERE OBJECT = ? ORDER BY ID";
		prep = con.prepareStatement(SQL);
		prep.setInt(1, object);

		rs = prep.executeQuery();

		try {
			while (rs.next()) {

				BuildInfo info = new BuildInfo();
				info.title = rs.getString(1);
				proposals.add(info);
			}

		} finally {
			rs.close();
		}

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

						info.children.add(info1);
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

	public List<BuildInfo> buildWithRootPath(List<ITreeItemInfo> path,
			ITreeItemInfo item, AdditionalInfo info) throws SQLException {

		List<BuildInfo> proposals = new ArrayList<BuildInfo>();
		Integer gr = null;

		List<ELevel> lv = new ArrayList<ELevel>();
		lv.add(ELevel.group1);
		lv.add(ELevel.group2);
		lv.add(ELevel.module);
		lv.add(null);

		List<String> tl = new ArrayList<String>();

		for (ITreeItemInfo p : path)
			tl.add(p.getTitle());

		tl.add(item.getTitle());
		tl.add(null);

		gr = get(lv.get(0), tl.get(0), null, proposals);
		for (int i = 1; i < tl.size(); i++) {

			if (lv.get(i) == null && tl.get(i) == null && gr != null) {
				getProcs(gr, proposals);
				break;
			}
			if (gr != null)
				gr = get(lv.get(i), tl.get(i), gr, proposals);
			else {
				info.type = BuildType.object;
				break;
			}

		}

		return proposals;
	}
}
