package codeanalyzer.db;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import codeanalyzer.core.AppManager;
import codeanalyzer.core.components.ITreeItemInfo;
import codeanalyzer.core.components.ITreeService;
import codeanalyzer.core.model.BookInfo;
import codeanalyzer.db.interfaces.IDbService;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Const.EVENT_UPDATE_BOOK_LIST_DATA;

public class DbService implements IDbService, ITreeService {

	DbStructure dbStructure = new DbStructure();
	private boolean exist;

	@Override
	public void init(boolean createNew) throws InvocationTargetException {

		try {
			openConnection();
			con = getConnection();
			if (createNew)
				dbStructure.createStructure(con);
			else if (exist)
				dbStructure.checkSructure(con);
			else
				dbStructure.createStructure(con);

		} catch (Exception e) {
			throw new InvocationTargetException(e, e.getMessage());
		}
		// finally {
		// try {
		// con.close();
		// } catch (Exception e) {
		// throw new InvocationTargetException(e,
		// Const.ERROR_CONFIG_OPEN_DATABASE);
		// }
		// }

	}

	// CONNECTION
	// *****************************************************************
	private Connection con;

	private void openConnection() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException,
			URISyntaxException {

		String root = System.getProperty("user.dir");
		File f = new File(root + "\\" + Const.SYSTEM_DB_NAME
				+ Const.DEFAULT_DB_EXTENSION);
		exist = f.exists();

		String ifExist = "";
		// String ifExist = exist ? ";IFEXISTS=TRUE" : "";

		boolean editMode = false;
		String mode = !editMode ? ";FILE_LOCK=SERIALIZED" : "";

		Class.forName("org.h2.Driver").newInstance();

		con = DriverManager.getConnection("jdbc:h2:" + Const.SYSTEM_DB_NAME
				+ ifExist + mode, "sa", "");
	}

	private Connection getConnection() throws IllegalAccessException {

		if (con == null)
			throw new IllegalAccessException();
		else
			return con;

	}

	public void closeConnection() {
		if (con == null)
			return;
		try {

			con.close();
			con = null;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void finalize() {
		closeConnection();
	}

	// SERVICE
	// *****************************************************************

	private String getItemString(String table) {
		String s = "$Table.TITLE, $Table.ID, $Table.PARENT, $Table.ISGROUP, $Table.PATH ";
		s = s.replaceAll("\\$Table", "T");
		return s;
	}

	private ITreeItemInfo getItem(ResultSet rs) throws SQLException {

		BookInfo info = new BookInfo();
		info.title = rs.getString(1);
		info.id = rs.getInt(2);
		info.parent = rs.getInt(3);
		info.isGroup = rs.getBoolean(4);
		info.path = rs.getString(5);
		return info;
	}

	@Override
	public List<ITreeItemInfo> getRoot() {

		List<ITreeItemInfo> result = new ArrayList<ITreeItemInfo>();
		// Connection con = null;
		try {
			con = getConnection();
			String SQL = "SELECT "
					+ getItemString("T")
					+ "FROM BOOKS AS T WHERE T.PARENT IS NULL ORDER BY T.SORT, T.ID";
			PreparedStatement prep = con.prepareStatement(SQL);

			ResultSet rs = prep.executeQuery();
			try {
				if (rs.next()) {

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
	public List<ITreeItemInfo> getChildren(int parent) {
		List<ITreeItemInfo> result = new ArrayList<ITreeItemInfo>();
		// Connection con = null;
		try {
			con = getConnection();
			String SQL = "SELECT " + getItemString("T")
					+ "FROM BOOKS AS T WHERE T.PARENT=? ORDER BY T.SORT, T.ID";

			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setInt(1, parent);
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
	public boolean hasChildren(int parent) {
		try {
			con = getConnection();
			String SQL = "SELECT COUNT(ID) from BOOKS WHERE PARENT=?";
			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setInt(1, parent);
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

	@Override
	public ITreeItemInfo getParent(int item) {
		try {
			Connection con = getConnection();
			String SQL = "SELECT " + getItemString("T1") + "FROM BOOKS AS T "
					+ "INNER JOIN BOOKS AS T1 ON T.PARENT = T1.ID "
					+ "WHERE T.ID=?";

			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setInt(1, item);
			ResultSet rs = prep.executeQuery();

			try {
				if (rs.next()) {

					return getItem(rs);
				}
			} finally {
				rs.close();
			}

			return null;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ITreeItemInfo getLast(int parent) {
		try {
			con = getConnection();
			String SQL = "SELECT TOP 1 T.ID FROM BOOKS AS T WHERE T.PARENT=? ORDER BY T.SORT DESC, T.ID DESC";

			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setInt(1, parent);
			ResultSet rs = prep.executeQuery();

			try {
				if (rs.next()) {

					BookInfo sec = new BookInfo();
					sec.id = rs.getInt(1);
					return sec;
				}
			} finally {
				rs.close();
			}

			return null;

		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}

	@Override
	public void add(ITreeItemInfo item) throws InvocationTargetException {

		BookInfo data = (BookInfo) item;
		BookInfo added = null;
		// Connection con = null;
		try {
			con = getConnection();
			String SQL;
			PreparedStatement prep;

			SQL = "SELECT Top 1 T.SORT FROM BOOKS AS T WHERE T.PARENT=? ORDER BY T.SORT DESC";
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

			SQL = "INSERT INTO BOOKS (TITLE, PARENT, ISGROUP, SORT, PATH) VALUES (?,?,?,?,?);";
			prep = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);

			prep.setString(1, data.title);
			if (data.parent == 0)
				prep.setNull(2, java.sql.Types.INTEGER);
			else
				prep.setInt(2, data.parent);
			prep.setBoolean(3, data.isGroup);
			prep.setInt(4, sort);
			prep.setString(5, data.path);

			ResultSet generatedKeys = null;
			try {
				int affectedRows = prep.executeUpdate();
				if (affectedRows == 0)
					throw new SQLException();

				generatedKeys = prep.getGeneratedKeys();
				if (generatedKeys.next()) {
					added = new BookInfo();
					added.id = generatedKeys.getInt(1);
				} else
					throw new SQLException();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				generatedKeys.close();
			}

			AppManager.br.post(Const.EVENT_UPDATE_BOOK_LIST,
					new EVENT_UPDATE_BOOK_LIST_DATA(getParent(data.parent),
							added));

		} catch (Exception e) {
			throw new InvocationTargetException(e);

		}

	}

	@Override
	public void delete(ITreeItemInfo item) {

		ITreeItemInfo parent = getParent(item.getId());
		if (parent == null)
			return;

		try {
			Connection con = getConnection();

			String SQL = "DELETE FROM BOOKS WHERE ID=?;";
			PreparedStatement prep;

			prep = con.prepareStatement(SQL);

			prep.setInt(1, item.getId());

			int affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

		} catch (Exception e) {
			e.printStackTrace();
		}

		ITreeItemInfo selected = getLast(parent.getId());
		if (selected == null)
			selected = parent;

		AppManager.br.post(Const.EVENT_UPDATE_BOOK_LIST,
				new EVENT_UPDATE_BOOK_LIST_DATA(parent, selected));

	}

	@Override
	public Boolean setParent(ITreeItemInfo item, ITreeItemInfo target) {
		try {

			// ITreeItemInfo parent = getParent(item.getId());

			Connection con = getConnection();

			String SQL = "UPDATE BOOKS SET PARENT=? WHERE ID=?;";
			PreparedStatement prep = con.prepareStatement(SQL,
					Statement.CLOSE_CURRENT_RESULT);

			prep.setInt(1, target.getId());
			prep.setInt(2, item.getId());
			int affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			List<ITreeItemInfo> items = getChildren(target.getId());
			items.remove(item);
			items.add(item);

			updateOrder(items);

			AppManager.br.post(Const.EVENT_UPDATE_BOOK_LIST,
					new EVENT_UPDATE_BOOK_LIST_DATA(target, item));

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public Boolean setAfter(ITreeItemInfo item, ITreeItemInfo target) {

		ITreeItemInfo parent = getParent(target.getId());

		if (parent == null)
			return false;

		boolean notify = true;
		if (item.getParent() != parent.getId()) {
			setParent(item, parent);
			notify = false;
		}

		List<ITreeItemInfo> items = getChildren(parent.getId());

		items.remove(item);
		int i = items.indexOf(target);
		items.add(i + 1, item);

		updateOrder(items);

		if (notify)
			AppManager.br.post(Const.EVENT_UPDATE_BOOK_LIST,
					new EVENT_UPDATE_BOOK_LIST_DATA(parent, item));
		// AppManager.br.post(Const.EVENT_UPDATE_CONTENT_VIEW,
		// new EVENT_UPDATE_VIEW_DATA(book, parent, section));

		return true;
	}

	@Override
	public Boolean setBefore(ITreeItemInfo item, ITreeItemInfo target) {
		ITreeItemInfo parent = getParent(target.getId());

		if (parent == null)
			return false;

		boolean notify = true;
		if (item.getParent() != parent.getId()) {
			setParent(item, parent);
			notify = false;
		}

		List<ITreeItemInfo> items = getChildren(parent.getId());

		items.remove(item);
		int i = items.indexOf(target);
		if (i < 0)
			return false;
		items.add(i, item);
		// set(t, section);

		updateOrder(items);

		if (notify)
			AppManager.br.post(Const.EVENT_UPDATE_BOOK_LIST,
					new EVENT_UPDATE_BOOK_LIST_DATA(parent, item));

		return true;
	}

	@Override
	public void updateOrder(List<ITreeItemInfo> items) {
		try {
			Connection con = getConnection();
			int order = 0;
			for (ITreeItemInfo item : items) {

				String SQL = "UPDATE BOOKS SET SORT=? WHERE ID=?;";
				PreparedStatement prep = con.prepareStatement(SQL,
						Statement.CLOSE_CURRENT_RESULT);

				prep.setInt(1, order);
				prep.setInt(2, item.getId());
				int affectedRows = prep.executeUpdate();
				if (affectedRows == 0)
					throw new SQLException();

				order++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void saveTitle(ITreeItemInfo item) {
		try {
			// SectionInfo parent = getParent(section);

			con = getConnection();
			String SQL = "UPDATE BOOKS SET TITLE=? WHERE ID=?;";
			PreparedStatement prep = con.prepareStatement(SQL,
					Statement.CLOSE_CURRENT_RESULT);

			prep.setString(1, item.getTitle());
			prep.setInt(2, item.getId());
			int affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			// AppManager.br.post(Const.EVENT_UPDATE_BOOK_LIST,
			// new EVENT_UPDATE_BOOK_LIST_DATA(book.parent, book));

			// AppManager.br.post(Const.EVENT_UPDATE_CONTENT_VIEW,
			// new EVENT_UPDATE_VIEW_DATA(book, section, true));

		} catch (Exception e) {
			e.printStackTrace();
			// } finally {
			// try {
			// con.close();
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
		}

	}

	@Override
	public ITreeItemInfo get(Integer id) {
		BookInfo result = new BookInfo();
		result.id = id;
		// Connection con = null;
		try {
			con = getConnection();
			String SQL = "SELECT " + getItemString("T")
					+ "FROM BOOKS AS T WHERE T.ID=? ORDER BY T.SORT, T.ID";

			PreparedStatement prep = con.prepareStatement(SQL);

			prep.setInt(1, id);
			ResultSet rs = prep.executeQuery();

			try {
				if (rs.next()) {
					return getItem(rs);
				}
			} finally {
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
