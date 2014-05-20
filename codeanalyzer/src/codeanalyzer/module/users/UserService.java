package codeanalyzer.module.users;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import codeanalyzer.core.AppManager;
import codeanalyzer.core.Events;
import codeanalyzer.core.Events.EVENT_UPDATE_TREE_DATA;
import codeanalyzer.core.pico;
import codeanalyzer.module.books.list.BookInfo;
import codeanalyzer.module.db.interfaces.IDbService;
import codeanalyzer.module.tree.ITreeItemInfo;
import codeanalyzer.module.tree.ITreeService;
import codeanalyzer.module.tree.TreeService;

public class UserService extends TreeService {

	IDbService db = pico.get(IDbService.class);

	final static String tableName = "USERS";
	final static String updateEvent = Events.EVENT_UPDATE_USERS;

	public UserService() {
		super(tableName, updateEvent);
	}

	@Override
	protected String getItemString(String table) {
		String s = "$Table.TITLE, $Table.ID, $Table.PARENT, $Table.ISGROUP ";
		s = s.replaceAll("\\$Table", "T");
		return s;
	}

	@Override
	protected ITreeItemInfo getItem(ResultSet rs) throws SQLException {

		BookInfo info = new BookInfo();
		info.title = rs.getString(1);
		info.id = rs.getInt(2);
		info.parent = rs.getInt(3);
		info.isGroup = rs.getBoolean(4);
		// info.path = rs.getString(5);
		return info;
	}

	@Override
	public void add(ITreeItemInfo item, ITreeItemInfo parent_item, boolean sub)
			throws InvocationTargetException {

		BookInfo parent = (BookInfo) parent_item;
		BookInfo data = (BookInfo) item;

		if (parent == null)
			data.parent = ITreeService.rootId;
		else if (parent.id == ITreeService.rootId)
			data.parent = ITreeService.rootId;
		else if (parent.isGroup)
			data.parent = sub ? parent.id : parent.parent;
		else
			data.parent = parent.parent;

		// BookInfo added = null;
		// Connection con = null;
		try {
			Connection con = db.getConnection();
			String SQL;
			PreparedStatement prep;

			SQL = "SELECT Top 1 T.SORT FROM " + tableName
					+ " AS T WHERE T.PARENT=? ORDER BY T.SORT DESC";
			prep = con.prepareStatement(SQL);

			prep.setInt(1, data.parent);
			ResultSet rs = prep.executeQuery();

			int sort = 0;
			try {
				if (rs.next())
					sort = rs.getInt(1);
				sort++;
			} finally {
				rs.close();
			}

			SQL = "INSERT INTO " + tableName
					+ " (TITLE, PARENT, ISGROUP, SORT) VALUES (?,?,?,?,?);";
			prep = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);

			prep.setString(1, data.title);
			if (data.parent == 0)
				prep.setNull(2, java.sql.Types.INTEGER);
			else
				prep.setInt(2, data.parent);
			prep.setBoolean(3, data.isGroup);
			prep.setInt(4, sort);
			// prep.setString(5, data.path);

			ResultSet generatedKeys = null;
			try {
				int affectedRows = prep.executeUpdate();
				if (affectedRows == 0)
					throw new SQLException();

				generatedKeys = prep.getGeneratedKeys();
				if (generatedKeys.next()) {
					// added = new BookInfo();
					data.id = generatedKeys.getInt(1);
					// added.parent = data
				} else
					throw new SQLException();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				generatedKeys.close();
			}

			AppManager.br.post(updateEvent, new EVENT_UPDATE_TREE_DATA(
					get(data.parent), data));

		} catch (Exception e) {
			throw new InvocationTargetException(e);

		}

	}
}
