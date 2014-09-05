package ebook.module.conf.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ebook.core.pico;
import ebook.module.conf.ConfConnection;
import ebook.module.conf.model.BuildType;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ContextInfoOptions;
import ebook.module.confLoad.interfaces.ICfServices;
import ebook.module.confLoad.services.TextParser;
import ebook.module.text.interfaces.ITextTreeService;
import ebook.module.text.model.GotoDefinitionData;
import ebook.module.tree.item.ITreeItemInfo;
import ebook.module.tree.service.TreeService;

public class ConfTreeService extends TreeService implements ITextTreeService {

	TextParser parser = pico.get(ICfServices.class).parse();

	final static String objectsTable = "OBJECTS";
	final static String tableName = "PROCS";
	final static String updateEvent = "";

	public ConfTreeService(ConfConnection con) {
		super(tableName, updateEvent, con);

	}

	@Override
	protected String getItemString(String table) {
		String s = "$Table.TITLE, $Table.ID, $Table.PARENT, $Table.SORT ";
		s = s.replaceAll("\\$Table", "T");
		return s;
	}

	@Override
	protected ITreeItemInfo getItem(ResultSet rs) throws SQLException {

		ContextInfoOptions opt = new ContextInfoOptions();
		ContextInfo info = new ContextInfo(opt);
		info.setTitle(rs.getString(1));
		info.setId(rs.getInt(2));
		info.setParent(rs.getInt(3));
		info.setSort(rs.getInt(4));
		info.setProc(true);
		info.setGroup(true);

		return info;
	}

	@Override
	protected String getTextQUERY() {
		return "SELECT TEXT FROM PROCS_TEXT WHERE PROC=?";
	}

	public void setProcTable() {
		setTableName(tableName);
	}

	public void setObjectsTable() {
		setTableName(objectsTable);
	}

	@Override
	public ITreeItemInfo getModule(ITreeItemInfo _item) {

		ContextInfo item = (ContextInfo) _item;
		if (!item.isProc())
			return null;

		setObjectsTable();
		item = (ContextInfo) get(_item.getParent());
		if (item != null) {
			ContextInfo parent = (ContextInfo) get(item.getParent());
			if (parent != null)
				item.setTitle(parent.getTitle() + "." + item.getTitle());
			item.getOptions().type = BuildType.module;
			item.setProc(false);

		}
		setProcTable();
		return item;
	}

	@Override
	public List<ITreeItemInfo> getParents(ITreeItemInfo _item) {
		List<ITreeItemInfo> result = new ArrayList<ITreeItemInfo>();
		ContextInfo item = (ContextInfo) _item;

		setObjectsTable();

		ContextInfo parent = (ContextInfo) get(item.getParent());

		while (parent != null) {
			// if (parent != null)
			result.add(0, parent);
			parent = (ContextInfo) get(parent.getParent());
		}

		setProcTable();

		return result;
	}

	@Override
	public String getPath(ContextInfo item) {
		return getPath(item, null);
	}

	@Override
	public String getPath(ContextInfo item, List<String> path) {

		return getPath(item, path, false);
	}

	public String getPath(ContextInfo item, List<String> path, boolean full) {
		String result = "";
		List<ITreeItemInfo> parents = getParents(item);
		String last = null;
		if (!parents.isEmpty() && !full) {
			last = parents.get(parents.size() - 1).getTitle();
			parents.remove(parents.size() - 1);
		}
		for (ITreeItemInfo p : parents) {
			result += p.getTitle() + ".";
			if (path != null)
				path.add(p.getTitle());
		}

		if (path != null) {
			if (last != null)
				path.add(last);
			path.add(item.getTitle());
		}
		return result.concat(item.getTitle());
	}

	@Override
	public ContextInfo getByPath(String path) {

		setObjectsTable();

		Integer parent = null;
		Boolean proc = false;
		String[] data = path.replace("...", "###").split("\\.");
		ITreeItemInfo item = null;
		for (String s : data) {
			s = s.replace("###", "...");
			if (s.contains("...")) {
				setProcTable();
				proc = true;
			}
			item = findInParent(s, parent);

			if (item == null)
				break;
			parent = item.getId();
		}

		setProcTable();

		if (item != null)
			((ContextInfo) item).setProc(proc);
		return (ContextInfo) item;
	}

	@Override
	public List<ITreeItemInfo> getDefinitions(GotoDefinitionData data) {

		if (data.isEmpty())
			return null;

		List<ITreeItemInfo> result = new ArrayList<ITreeItemInfo>();
		try {
			Connection con = db.getConnection();
			String SQL = "SELECT " + getItemString("T") + "FROM " + tableName
					+ " AS T WHERE T.NAME=? ORDER BY T.SORT, T.ID";

			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setString(1, data.getProcName());
			ResultSet rs = prep.executeQuery();

			try {
				while (rs.next()) {

					ITreeItemInfo item = getItem(rs);
					List<String> path = new ArrayList<String>();
					getPath((ContextInfo) item, path);
					adaptProc(item, item.getTitle(), item.getParent(), path);

					result.add(item);
				}
			} finally {
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void adaptProc(ITreeItemInfo _item, String proc_name, int id,
			List<String> path) {
		ContextInfo item = (ContextInfo) _item;
		setObjectsTable();
		ContextInfo module = (ContextInfo) get(id);
		setProcTable();
		item.setParent(-1);
		if (module != null) {
			Integer i = module.getParent();
			item.setParent(i);
			item.setModule(null);
			item.setTitle(module.getTitle());

			int s = path.size();
			for (int j = 2; j < s; j++)
				path.remove(path.size() - 1);

			item.getOptions().type = BuildType.module;
			item.getOptions().proc = proc_name;
			item.setProc(false);
		}

		item.setId(id);
		item.setTitle(path.get(path.size() - 1).concat("." + item.getTitle()));

	}

	public String getProcHash(int id) {
		StringBuilder result = new StringBuilder();
		try {
			Connection con = db.getConnection();

			String SQL = "Select HASH from PROCS_TEXT WHERE PROC=?";
			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setInt(1, id);
			ResultSet rs = prep.executeQuery();

			try {
				if (rs.next()) {

					String line = rs.getString(1);

					result.append(line + "\n");
				}
			} finally {
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	public String getObjectHash(int id) {
		StringBuilder result = new StringBuilder();
		try {
			Connection con = db.getConnection();

			String SQL = "SELECT T1.HASH FROM PROCS AS T INNER JOIN PROCS_TEXT AS T1 ON T1.PROC = T.ID"
					+ " WHERE (T.MODULE = ? OR T.GROUP2 = ? OR T.GROUP1 = ?)";

			SQL = SQL.concat(" ORDER BY TITLE");

			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setInt(1, id);
			prep.setInt(2, id);
			prep.setInt(3, id);
			ResultSet rs = prep.executeQuery();

			try {
				while (rs.next()) {

					String line = rs.getString(1);

					result.append(line + "\n");
				}
			} finally {
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

}
