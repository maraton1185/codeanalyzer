package codeanalyzer.module.books.list;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;

import codeanalyzer.core.AppManager;
import codeanalyzer.core.Events;
import codeanalyzer.core.Events.EVENT_UPDATE_TREE_DATA;
import codeanalyzer.core.pico;
import codeanalyzer.module.db.interfaces.IDbService;
import codeanalyzer.module.tree.ITreeItemInfo;
import codeanalyzer.module.tree.ITreeService;

public class BookService implements ITreeService {

	IDbService db = pico.get(IDbService.class);

	// ******************************************************************
	public Image getImage(Connection con) {

		// ImageData data = new ImageData(stream);
		// ImageDescriptor image = ImageDescriptor.createFromImageData(data);
		// return image.createImage();

		return null;
	}

	public void getData(Connection con, CurrentBookInfo info)
			throws SQLException {
		String SQL = "Select TOP 1 T.DESCRIPTION, T.EDIT_MODE FROM INFO AS T";
		Statement stat = con.createStatement();
		ResultSet rs = stat.executeQuery(SQL);
		try {
			if (rs.next()) {
				info.setDescription(rs.getString(1));
				info.setEditMode(rs.getBoolean(2));
			} else {
				info.setDescription("Новая книга");
				info.setEditMode(true);
			}
		} finally {
			rs.close();
		}
	}

	public void setData(Connection con, CurrentBookInfo info)
			throws SQLException {

		String SQL = "SELECT TOP 1 T.ID FROM INFO AS T;";
		Statement stat = con.createStatement();
		ResultSet rs = stat.executeQuery(SQL);
		try {
			if (rs.next()) {
				SQL = "UPDATE INFO SET DESCRIPTION=?, EDIT_MODE=? WHERE ID=?;";
				PreparedStatement prep = con.prepareStatement(SQL,
						Statement.CLOSE_CURRENT_RESULT);

				prep.setString(1, info.getDescription());
				prep.setBoolean(2, info.isEditMode());
				prep.setInt(3, rs.getInt(1));
				int affectedRows = prep.executeUpdate();
				if (affectedRows == 0)
					throw new SQLException();
			} else {
				SQL = "INSERT INTO INFO (DESCRIPTION, EDIT_MODE) VALUES (?,?);";
				PreparedStatement prep = con.prepareStatement(SQL,
						Statement.CLOSE_CURRENT_RESULT);

				prep.setString(1, info.getDescription());
				prep.setBoolean(2, info.isEditMode());
				int affectedRows = prep.executeUpdate();
				if (affectedRows == 0)
					throw new SQLException();
			}
		} finally {
			rs.close();
		}
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
			Connection con = db.getConnection();
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
			Connection con = db.getConnection();
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
			Connection con = db.getConnection();
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
	public ITreeItemInfo get(int item) {
		try {
			Connection con = db.getConnection();
			String SQL = "SELECT " + getItemString("T") + "FROM BOOKS AS T "
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
			Connection con = db.getConnection();
			String SQL = "SELECT TOP 1 "
					+ getItemString("T")
					+ "FROM BOOKS AS T WHERE T.PARENT=? ORDER BY T.SORT DESC, T.ID DESC";

			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setInt(1, parent);
			ResultSet rs = prep.executeQuery();

			try {
				if (rs.next()) {

					// BookInfo sec = new BookInfo();
					// sec.id = rs.getInt(1);
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

			AppManager.br.post(Events.EVENT_UPDATE_BOOK_LIST,
					new EVENT_UPDATE_TREE_DATA(get(data.parent), data));

		} catch (Exception e) {
			throw new InvocationTargetException(e);

		}

	}

	@Override
	public void delete(ITreeItemInfo item) {

		ITreeItemInfo parent = get(item.getParent());
		if (parent == null)
			return;

		try {
			Connection con = db.getConnection();

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

		// ITreeItemInfo selected = getLast(parent.getId());
		// if (selected == null)
		// selected = parent;
		//
		// AppManager.br.post(Const.EVENT_UPDATE_BOOK_LIST,
		// new EVENT_UPDATE_BOOK_LIST_DATA(parent, selected));

	}

	public void selectLast(int index) {
		ITreeItemInfo parent = get(index);

		ITreeItemInfo selected = getLast(parent.getId());
		if (selected == null)
			selected = parent;

		AppManager.br.post(Events.EVENT_UPDATE_BOOK_LIST,
				new EVENT_UPDATE_TREE_DATA(parent, selected));
	}

	@Override
	public Boolean setParent(ITreeItemInfo item, ITreeItemInfo target) {
		try {

			// ITreeItemInfo parent = getParent(item.getId());

			Connection con = db.getConnection();

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

			AppManager.br.post(Events.EVENT_UPDATE_BOOK_LIST,
					new EVENT_UPDATE_TREE_DATA(target, item));

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public Boolean setAfter(ITreeItemInfo item, ITreeItemInfo target) {

		ITreeItemInfo parent = get(target.getParent());

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
			AppManager.br.post(Events.EVENT_UPDATE_BOOK_LIST,
					new EVENT_UPDATE_TREE_DATA(parent, item));
		// AppManager.br.post(Const.EVENT_UPDATE_CONTENT_VIEW,
		// new EVENT_UPDATE_VIEW_DATA(book, parent, section));

		return true;
	}

	@Override
	public Boolean setBefore(ITreeItemInfo item, ITreeItemInfo target) {
		ITreeItemInfo parent = get(target.getParent());

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
			AppManager.br.post(Events.EVENT_UPDATE_BOOK_LIST,
					new EVENT_UPDATE_TREE_DATA(parent, item));

		return true;
	}

	@Override
	public void updateOrder(List<ITreeItemInfo> items) {
		try {
			Connection con = db.getConnection();
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

			Connection con = db.getConnection();
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
			Connection con = db.getConnection();
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
