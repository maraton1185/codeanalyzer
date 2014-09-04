package ebook.module.book.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import ebook.core.App;
import ebook.module.book.BookConnection;
import ebook.module.book.tree.SectionInfo;
import ebook.module.db.DbOptions;
import ebook.module.text.service.AbstractBookmarkService;
import ebook.module.text.tree.BookmarkInfo;
import ebook.module.text.tree.BookmarkInfoOptions;
import ebook.module.tree.item.ITreeItemInfo;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_VIEW_DATA;
import ebook.utils.Strings;

public class BookmarkService extends AbstractBookmarkService {

	final static String tableName = "BOOKMARKS";
	final static String updateEvent = Events.EVENT_UPDATE_BOOKMARK_VIEW;
	private SectionInfo section;

	public BookmarkService(BookConnection con, SectionInfo section) {
		super(tableName, updateEvent, con);

		this.section = section;
	}

	public void setSection(SectionInfo section) {
		this.section = section;
	}

	public SectionInfo getSection() {
		return section;
	}

	@Override
	protected String getItemString(String table) {
		String s = "$Table.TITLE, $Table.ID, $Table.PARENT, $Table.ISGROUP, $Table.OPTIONS, $Table.SORT, $Table.PATH, $Table.PROC, $Table.OFFSET, $Table.SECTION ";
		s = s.replaceAll("\\$Table", "T");
		return s;
	}

	@Override
	protected String additionKeysString() {
		return ", PATH, PROC, OFFSET, SECTION";
	}

	@Override
	protected String additionValuesString() {
		return ", ?, ?, ?, ?";
	}

	@Override
	protected void setAdditions(PreparedStatement prep, ITreeItemInfo data)
			throws SQLException {

		prep.setString(6, ((BookmarkInfo) data)._path);
		prep.setString(7, ((BookmarkInfo) data)._proc);
		prep.setInt(8, ((BookmarkInfo) data)._offset);
		prep.setInt(9, section.getId());
	}

	@Override
	protected void setAdditionRoot(PreparedStatement prep) throws SQLException {
		prep.setInt(1, section.getId());
	}

	@Override
	protected String additionRootWHEREString() {
		return "AND T.SECTION=?";
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
		info._path = rs.getString(7);
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

			String SQL = "INSERT INTO BOOKMARKS (TITLE, ISGROUP, SECTION) VALUES (?,?,?);";
			PreparedStatement prep = con.prepareStatement(SQL,
					Statement.CLOSE_CURRENT_RESULT);

			prep.setString(1, Strings.value("bookmarkRoot"));
			prep.setBoolean(2, true);
			prep.setInt(3, section.getId());
			int affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			List<ITreeItemInfo> input = getRoot();
			if (input.isEmpty())
				return null;

			App.br.post(Events.EVENT_UPDATE_BOOKMARK_VIEW_FULL_UPDATE, db);

			return input.get(0);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// @Override
	// public ITreeItemInfo getSelected() {
	// SectionInfoOptions opt = section.getOptions();
	// if (opt == null)
	// return get(ITreeService.rootId);
	//
	// return get(opt.selectedBookmark);
	// }

}
