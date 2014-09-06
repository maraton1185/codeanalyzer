package ebook.module.conf.build;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

import ebook.module.conf.model.BuildInfo;
import ebook.module.conf.model.BuildType;
import ebook.module.confLoad.model.ELevel;

public class Get {

	Connection con;

	public Get(Connection con) {
		this.con = con;
	}

	// private List<ELevel> getLevels() {
	// List<ELevel> levels = new ArrayList<ELevel>();
	// levels.add(ELevel.group1);
	// levels.add(ELevel.group2);
	// levels.add(ELevel.module);
	// levels.add(ELevel.proc);
	// return levels;
	// }

	public Integer get(ELevel level, String title, Integer parent,
			List<BuildInfo> proposals) throws SQLException {
		Integer gr = get(level, title, parent, proposals, true);
		if (gr == null)
			get(level, title, parent, proposals, false);
		return gr;
	}

	private Integer get(ELevel level, String title, Integer parent,
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

	private Integer getProcs(String title, Integer object,
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

		return (!exact || count > 1) ? null : index;
	}

	public BuildInfo getObject(Integer id) throws SQLException {

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
