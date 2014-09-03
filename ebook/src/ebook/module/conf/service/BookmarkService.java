package ebook.module.conf.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import ebook.module.conf.ConfConnection;
import ebook.module.db.DbOptions;
import ebook.module.text.service.AbstractBookmarkService;
import ebook.module.text.tree.BookmarkInfo;
import ebook.module.text.tree.BookmarkInfoOptions;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.ITreeService;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_VIEW_DATA;
import ebook.utils.Strings;

public class BookmarkService extends AbstractBookmarkService {

	final static String tableName = "BOOKMARKS";
	final static String updateEvent = Events.EVENT_UPDATE_BOOKMARK_VIEW;

	public BookmarkService(ConfConnection con) {
		super(tableName, updateEvent, con);

	}

	@Override
	protected String getItemString(String table) {
		String s = "$Table.TITLE, $Table.ID, $Table.PARENT, $Table.ISGROUP, $Table.OPTIONS, $Table.SORT, $Table.ITEM, $Table.PROC, $Table.OFFSET ";
		s = s.replaceAll("\\$Table", "T");
		return s;
	}

	@Override
	protected String additionKeysString() {
		return ", ITEM, PROC, OFFSET";
	}

	@Override
	protected String additionValuesString() {
		return ", ?, ?, ?";
	}

	@Override
	protected void setAdditions(PreparedStatement prep, ITreeItemInfo data)
			throws SQLException {

		prep.setInt(6, ((BookmarkInfo) data)._id);
		prep.setString(7, ((BookmarkInfo) data)._proc);
		prep.setInt(8, ((BookmarkInfo) data)._offset);
	}

	@Override
	protected ITreeItemInfo getItem(ResultSet rs) throws SQLException {

		BookmarkInfoOptions opt = new BookmarkInfoOptions();
		BookmarkInfo info = new BookmarkInfo(opt);

		info.setTitle(rs.getString(1));
		info.setId(rs.getInt(2));
		info.setParent(rs.getInt(3));
		info.setGroup(rs.getBoolean(4));
		info.setOptions(DbOptions.load(BookmarkInfoOptions.class,
				rs.getString(5)));
		info.setSort(rs.getInt(6));
		info._id = rs.getInt(7);
		info._proc = rs.getString(8);
		info._offset = rs.getInt(9);
		return info;
	}

	@Override
	protected Object getUpdateEventData(ITreeItemInfo parent, ITreeItemInfo item) {

		return new EVENT_UPDATE_VIEW_DATA(db, parent, item);
	}

	@Override
	public ITreeItemInfo getUploadRoot() {
		List<ITreeItemInfo> input = getRoot();
		if (input.isEmpty())
			return createRoot();

		return input.get(0);

	}

	private ITreeItemInfo createRoot() {

		try {
			Connection con = db.getConnection();

			String SQL = "INSERT INTO BOOKMARKS (TITLE, ISGROUP) VALUES (?,?);";
			PreparedStatement prep = con.prepareStatement(SQL,
					Statement.CLOSE_CURRENT_RESULT);

			prep.setString(1, Strings.value("bookmarkRoot"));
			prep.setBoolean(2, true);
			int affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			List<ITreeItemInfo> input = getRoot();
			if (input.isEmpty())
				return null;

			return input.get(0);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public ITreeItemInfo getSelected() {

		return get(ITreeService.rootId);

	}

}
