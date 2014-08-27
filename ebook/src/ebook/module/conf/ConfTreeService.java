package ebook.module.conf;

import java.sql.ResultSet;
import java.sql.SQLException;

import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ContextInfoOptions;
import ebook.module.db.DbOptions;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.TreeService;

public class ConfTreeService extends TreeService {

	final static String tableName = "OBJECTS";
	final static String updateEvent = "";

	public ConfTreeService(ConfConnection con) {
		super(tableName, updateEvent, con);

	}

	@Override
	protected String getItemString(String table) {
		String s = "$Table.TITLE, $Table.ID, $Table.PARENT, $Table.OPTIONS, $Table.SORT ";
		s = s.replaceAll("\\$Table", "T");
		return s;
	}

	@Override
	protected ITreeItemInfo getItem(ResultSet rs) throws SQLException {

		ContextInfo info = new ContextInfo();
		info.setTitle(rs.getString(1));
		info.setId(rs.getInt(2));
		info.setParent(rs.getInt(3));

		info.setOptions(DbOptions.load(ContextInfoOptions.class,
				rs.getString(4)));
		info.setSort(rs.getInt(5));

		return info;
	}

}
