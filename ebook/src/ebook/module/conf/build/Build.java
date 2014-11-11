package ebook.module.conf.build;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import ebook.module.conf.model.AdditionalInfo;
import ebook.module.conf.model.BuildInfo;
import ebook.module.conf.model.BuildType;

public class Build {

	Connection con;
	Get get;

	public Build(Connection con, Get get) {
		this.con = con;
		this.get = get;
	}

	public void buildProcText(List<BuildInfo> proposals, Integer gr,
			String title, AdditionalInfo build_opt) throws SQLException {
		if (con == null)
			return;

		if (proposals != null)
			proposals.clear();

		if (gr == null)
			return;
		// title = Pattern.quote(title);

		String SQL;
		PreparedStatement prep;
		ResultSet rs;

		SQL = "Select T.TITLE, T.PARENT, T1.TEXT from PROCS AS T INNER JOIN PROCS_TEXT AS T1 ON T1.PROC = T.ID";
		SQL = SQL.concat(" WHERE ");

		SQL = SQL.concat(" T.ID = ? ");
		SQL = SQL.concat(" ORDER BY TITLE");

		prep = con.prepareStatement(SQL);

		prep.setInt(1, gr);

		BufferedReader bufferedReader = null;
		rs = prep.executeQuery();
		try {
			if (rs.next()) {

				Reader in = rs.getCharacterStream(3);
				bufferedReader = new BufferedReader(in);
				String line;
				int index = 0;
				while ((line = bufferedReader.readLine()) != null) {

					BuildInfo ch = new BuildInfo();
					ch.title = line.trim();
					ch.start_offset = index;
					ch.proc = rs.getString(1);
					proposals.add(ch);
					index = index + (line + "\n").length();
				}
			}

		} catch (Exception e) {
			throw new SQLException();
		} finally {
			rs.close();
		}

	}

	public void buildProc(List<BuildInfo> proposals, Integer gr, String title,
			AdditionalInfo build_opt) throws SQLException {

		if (con == null)
			return;

		if (proposals != null)
			proposals.clear();

		// title = Pattern.quote(title);

		String SQL;
		PreparedStatement prep;
		ResultSet rs;

		SQL = "Select T.TITLE, T.PARENT from PROCS AS T";
		SQL = SQL.concat(" WHERE ");

		if (gr != null)
			SQL = SQL
					.concat("(T.MODULE = ? OR T.GROUP2 = ? OR T.GROUP1 = ?) AND");

		SQL = SQL.concat(" UPPER(T.NAME) REGEXP UPPER(?)");
		SQL = SQL.concat(" ORDER BY TITLE");

		prep = con.prepareStatement(SQL);

		if (gr != null) {
			prep.setInt(1, gr);
			prep.setInt(2, gr);
			prep.setInt(3, gr);
			prep.setString(4, Pattern.quote(title));
		} else {
			prep.setString(1, Pattern.quote(title));
		}

		rs = prep.executeQuery();
		try {
			while (rs.next()) {

				BuildInfo info = new BuildInfo();
				info.title = rs.getString(1);
				info.parent = rs.getInt(2);

				proposals.add(info);

			}

		} catch (Exception e) {
			throw new SQLException();
		} finally {
			rs.close();
		}

	}

	public void buildText(List<BuildInfo> proposals, Integer gr, String title,
			AdditionalInfo build_opt) throws SQLException {

		if (con == null)
			return;

		if (proposals != null)
			proposals.clear();

		if (gr != null) {
			buildTextWithContext(proposals, gr, title, build_opt, "MODULE");
			if (proposals.isEmpty())
				buildTextWithContext(proposals, gr, title, build_opt, "GROUP1");
			if (proposals.isEmpty())
				buildTextWithContext(proposals, gr, title, build_opt, "GROUP2");
		} else
			buildTextWithoutContext(proposals, title, build_opt);

	}

	private void buildTextWithoutContext(List<BuildInfo> proposals,
			String title, AdditionalInfo build_opt) throws SQLException {
		String SQL;
		PreparedStatement prep;
		ResultSet rs;

		if (build_opt.textRegex) {
			SQL = "Select T.TITLE, T.PARENT, T1.TEXT from PROCS AS T INNER JOIN PROCS_TEXT AS T1 ON T1.PROC = T.ID";
			SQL = SQL.concat(" WHERE UPPER(T1.TEXT) REGEXP UPPER(?)");
			SQL = SQL.concat(" ORDER BY TITLE");
		} else {
			SQL = "Select T.TITLE, T.PARENT, T1.TEXT from PROCS AS T INNER JOIN ("
					+ "SELECT T.* FROM FT_SEARCH_DATA(?, 0, 0) FT, PROCS_TEXT T "
					+ "WHERE FT.TABLE='PROCS_TEXT' AND T.ID=FT.KEYS[0]) AS T1 ON T1.PROC = T.ID ORDER BY TITLE";
		}
		prep = con.prepareStatement(SQL);

		prep.setString(1, build_opt.textRegex ? Pattern.quote(title) : title);

		rs = prep.executeQuery();
		try {
			while (rs.next()) {

				BuildInfo info = new BuildInfo();
				info.title = rs.getString(1);
				info.parent = rs.getInt(2);

				makeTextLines(rs.getCharacterStream(3), title, info);

				proposals.add(info);

			}

		} catch (Exception e) {
			throw new SQLException();
		} finally {
			rs.close();
		}

	}

	private void buildTextWithContext(List<BuildInfo> proposals, Integer gr,
			String title, AdditionalInfo build_opt, String WHERE)
			throws SQLException {
		String SQL;
		PreparedStatement prep;
		ResultSet rs;

		SQL = "Select T.TITLE, T.PARENT, T1.TEXT from PROCS AS T INNER JOIN PROCS_TEXT AS T1 ON T1.PROC = T.ID";
		SQL = SQL.concat(" WHERE T." + WHERE + "=? AND");

		SQL = SQL.concat(" UPPER(T1.TEXT) REGEXP UPPER(?)");
		SQL = SQL.concat(" ORDER BY TITLE");

		prep = con.prepareStatement(SQL);

		prep.setInt(1, gr);
		prep.setString(2, Pattern.quote(title));

		rs = prep.executeQuery();
		try {
			while (rs.next()) {

				BuildInfo info = new BuildInfo();
				info.title = rs.getString(1);
				info.parent = rs.getInt(2);

				// if (!build_opt.textRegex)
				makeTextLines(rs.getCharacterStream(3), title, info);

				proposals.add(info);

			}

		} catch (Exception e) {
			throw new SQLException();
		} finally {
			rs.close();
		}

	}

	private void makeTextLines(Reader in, String title, BuildInfo info)
			throws IOException {

		BufferedReader bufferedReader = new BufferedReader(in);
		String line;
		int index = 0;
		while ((line = bufferedReader.readLine()) != null) {

			// System.out.println(line);

			if (line.toLowerCase().contains(title.toLowerCase())) {
				BuildInfo ch = new BuildInfo();
				ch.title = line.trim();
				ch.start_offset = index;
				ch.proc = info.title;
				info.children.add(ch);
				// System.out.println(index);
			}
			// result.append(line + "\n");
			index = index + (line + "\n").length();
		}

	}

	public void fillParents(List<BuildInfo> proposals, List<String> context)
			throws SQLException {
		if (proposals.isEmpty())
			return;

		if (context == null)
			context = new ArrayList<String>();

		HashMap<BuildInfo, List<BuildInfo>> parents = new HashMap<BuildInfo, List<BuildInfo>>();
		for (BuildInfo buildInfo : proposals) {

			List<BuildInfo> path = new ArrayList<BuildInfo>();

			BuildInfo root = get.getObject(buildInfo.parent);
			while (root != null) {

				if (!context.contains(root.title))
					path.add(0, root);
				root = get.getObject(root.parent);
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
}
