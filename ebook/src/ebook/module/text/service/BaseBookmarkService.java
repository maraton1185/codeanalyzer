package ebook.module.text.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import ebook.core.interfaces.IDbConnection;
import ebook.module.text.interfaces.IBookmarkService;
import ebook.module.text.tree.BookmarkInfo;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.TreeService;

public abstract class BaseBookmarkService extends TreeService implements
		IBookmarkService {

	protected BaseBookmarkService(String tableName,
			String EVENT_UPDATE_TREE_NAME, IDbConnection db) {
		super(tableName, EVENT_UPDATE_TREE_NAME, db);
	}

	@Override
	public List<ITreeItemInfo> getBookmarks(int item) {
		List<ITreeItemInfo> result = new ArrayList<ITreeItemInfo>();
		// Connection con = null;
		try {
			Connection con = db.getConnection();
			String SQL = "SELECT " + getItemString("T") + "FROM " + tableName
					+ " AS T WHERE T.ITEM=? ORDER BY T.SORT, T.ID";

			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setInt(1, item);
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
	public boolean haveBookmark(BookmarkInfo data) {

		try {
			Connection con = db.getConnection();
			String SQL = "SELECT COUNT(ID) FROM " + tableName
					+ "  WHERE ITEM=? AND PROC=? AND OFFSET=?";
			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setInt(1, data._id);
			prep.setString(2, data._proc);
			prep.setInt(3, data._offset);
			ResultSet rs = prep.executeQuery();

			try {
				if (rs.next())
					return rs.getInt(1) != 0;
				else
					return false;
			} finally {
				rs.close();
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return false;

	}
}
