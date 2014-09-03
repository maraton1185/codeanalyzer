package ebook.module.text.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import ebook.core.interfaces.IDbConnection;
import ebook.module.text.interfaces.IBookmarkService;
import ebook.module.text.tree.BookmarkInfo;
import ebook.module.text.tree.BookmarkInfoSelection;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.TreeService;

public abstract class AbstractBookmarkService extends TreeService implements
		IBookmarkService {

	protected AbstractBookmarkService(String tableName,
			String EVENT_UPDATE_TREE_NAME, IDbConnection db) {
		super(tableName, EVENT_UPDATE_TREE_NAME, db);
	}

	@Override
	public List<ITreeItemInfo> getBookmarks(String path) {
		List<ITreeItemInfo> result = new ArrayList<ITreeItemInfo>();
		// Connection con = null;
		try {
			Connection con = db.getConnection();
			String SQL = "SELECT " + getItemString("T") + "FROM " + tableName
					+ " AS T WHERE T.PATH=? ORDER BY T.SORT, T.ID";

			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setString(1, path);
			ResultSet rs = prep.executeQuery();

			try {
				while (rs.next()) {

					result.add(getItem(rs));
				}
			} finally {
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public BookmarkInfo getBookmark(BookmarkInfo data) {

		try {
			Connection con = db.getConnection();
			String SQL = "SELECT " + getItemString("T") + "FROM " + tableName
					+ " AS T WHERE T.PATH=? AND T.PROC=? AND T.OFFSET=?";
			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setString(1, data._path);
			prep.setString(2, data._proc);
			prep.setInt(3, data._offset);
			ResultSet rs = prep.executeQuery();

			try {
				if (rs.next())
					return (BookmarkInfo) getItem(rs);
				else
					return null;
			} finally {
				rs.close();
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;

	}

	@Override
	public void removeBookmark(BookmarkInfo item) {
		BookmarkInfoSelection sel = new BookmarkInfoSelection();
		sel.add(item);
		delete(sel);

	}

}
