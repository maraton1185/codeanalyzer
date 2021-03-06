package ebook.module.conf.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import ebook.module.conf.ConfConnection;
import ebook.module.conf.tree.ListInfo;
import ebook.module.conf.tree.ListInfoOptions;
import ebook.module.db.DbOptions;
import ebook.module.tree.item.ITreeItemInfo;
import ebook.module.tree.service.ITreeService;
import ebook.module.tree.service.TreeService;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_VIEW_DATA;

public class ListService extends TreeService {

	final static String tableName = "LISTS";
	final static String updateEvent = Events.EVENT_UPDATE_LIST_VIEW;

	public ListService(ConfConnection con) {
		super(tableName, updateEvent, con);

	}

	@Override
	public List<ITreeItemInfo> getRoot() {

		return getRootCondition(false);

	}

	@Override
	protected String getItemString(String table) {
		String s = "$Table.TITLE, $Table.ID, $Table.PARENT, $Table.ISGROUP, $Table.OPTIONS, $Table.SORT ";
		s = s.replaceAll("\\$Table", "T");
		return s;
	}

	@Override
	protected Object getUpdateEventData(ITreeItemInfo parent, ITreeItemInfo item) {

		return new EVENT_UPDATE_VIEW_DATA(db, parent, item);
	}

	@Override
	protected ITreeItemInfo getItem(ResultSet rs) throws SQLException {

		ListInfo info = new ListInfo();
		info.setTitle(rs.getString(1));
		info.setId(rs.getInt(2));
		info.setParent(rs.getInt(3));
		info.setGroup(rs.getBoolean(4));
		info.setOptions(DbOptions.load(ListInfoOptions.class, rs.getString(5)));
		info.setSort(rs.getInt(6));
		return info;
	}

	@Override
	public ITreeItemInfo getSelected() {

		return get(ITreeService.rootId);

	}

}
