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
	boolean isProc = true;

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
		info.setProc(isProc);
		info.setGroup(true);

		return info;
	}

	@Override
	protected String getTextQUERY() {
		return "SELECT TEXT FROM PROCS_TEXT WHERE PROC=?";
	}

	public void setProcTable() {
		setTableName(tableName);
		isProc = true;
	}

	public void setObjectsTable() {
		setTableName(objectsTable);
		isProc = false;
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
			String SQL = "SELECT "
					+ getItemString("T")
					+ "FROM "
					+ tableName
					+ " AS T WHERE T.NAME=? AND (T.EXPORT OR T.PARENT=?) ORDER BY T.SORT, T.ID";

			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setString(1, data.getProcName());
			prep.setInt(2, data.getItem().getId());
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

	public String getHash(ContextInfo item) {
		StringBuilder result = new StringBuilder();

		if (item.isProc())
			return getProcHash(item);
		else {
			boolean getIt = getObjectHash(result, item, "MODULE");
			if (!getIt)
				getIt = getObjectHash(result, item, "GROUP1");
			if (!getIt)
				getIt = getObjectHash(result, item, "GROUP2");

		}

		return result.toString();
	}

	private boolean getObjectHash(StringBuilder result, ContextInfo item,
			String where) {

		try {
			Connection con = db.getConnection();

			String SQL = "SELECT T1.HASH FROM PROCS AS T INNER JOIN PROCS_TEXT AS T1 ON T1.PROC = T.ID WHERE T."
					+ where + "=?";

			SQL = SQL.concat(" ORDER BY TITLE");

			int id = item.getId();
			PreparedStatement prep = con.prepareStatement(SQL);

			prep.setInt(1, id);

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
		return result.length() != 0;
	}

	private String getProcHash(ContextInfo item) {
		StringBuilder result = new StringBuilder();
		try {
			Connection con = db.getConnection();

			String SQL = "SELECT T1.HASH FROM PROCS_TEXT AS T1 WHERE T1.PROC = ?";

			// SQL = SQL.concat(" ORDER BY TITLE");

			int id = item.getId();
			PreparedStatement prep = con.prepareStatement(SQL);

			prep.setInt(1, id);

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

	@Override
	public List<ITreeItemInfo> getChildren(int parent) {

		setObjectsTable();
		List<ITreeItemInfo> result = super.getChildren(parent);
		setProcTable();
		if (result.isEmpty())
			return super.getChildren(parent);

		return result;
	}

}
