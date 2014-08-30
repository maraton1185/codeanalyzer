package ebook.module.conf;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ebook.module.conf.model.BuildType;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ContextInfoOptions;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.TreeService;

public class ConfTreeService extends TreeService {

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

		if (!item.isProc())
			setObjectsTable();

		ContextInfo parent = (ContextInfo) get(item.getParent());

		if (item.isProc())
			setObjectsTable();

		// if (parent != null)
		// result.add(0, parent);

		do {
			if (parent != null)
				result.add(0, parent);
			parent = (ContextInfo) get(parent.getParent());

		} while (parent != null);

		// while (parent != null) {
		// parent = (ContextInfo) get(parent.getParent());
		// result.add(0, parent);
		// }
		setProcTable();

		return result;
	}

}
