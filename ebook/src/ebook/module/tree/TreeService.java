package ebook.module.tree;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IPath;

import ebook.core.App;
import ebook.core.interfaces.IDbConnection;
import ebook.module.db.DbOptions;
import ebook.utils.Const;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_TREE_DATA;
import ebook.utils.Events.EVENT_UPDATE_VIEW_DATA;

public abstract class TreeService implements ITreeService {

	protected IDbConnection db;
	private final String tableName;
	private final String updateEvent;
	private boolean stopUpdate = false;

	protected TreeService(String tableName, String EVENT_UPDATE_TREE_NAME,
			IDbConnection db) {
		this.tableName = tableName;
		this.updateEvent = EVENT_UPDATE_TREE_NAME;
		this.db = db;
	}

	protected String getItemString(String table) {
		String s = "$Table.TITLE, $Table.ID, $Table.PARENT, $Table.ISGROUP, $Table.OPTIONS ";
		s = s.replaceAll("\\$Table", "T");
		return s;
	}

	protected abstract ITreeItemInfo getItem(ResultSet rs) throws SQLException;

	protected Object getUpdateEventData(ITreeItemInfo parent, ITreeItemInfo item) {

		return new EVENT_UPDATE_TREE_DATA(parent, item);
	}

	@Override
	public void add(ITreeItemInfo data, ITreeItemInfo parent, boolean sub)
			throws InvocationTargetException {

		// ListBookInfo parent = (ListBookInfo) parent_item;
		// ListBookInfo data = (ListBookInfo) item;

		if (parent == null)
			data.setParent(ITreeService.rootId);
		else if (parent.isRoot())
			// data.setParent(ITreeService.rootId);
			data.setParent(parent.getId());
		else if (parent.isGroup())
			data.setParent(sub ? parent.getId() : parent.getParent());
		else
			data.setParent(parent.getParent());

		try {
			Connection con = db.getConnection();
			String SQL;
			PreparedStatement prep;

			SQL = "SELECT Top 1 T.SORT FROM " + tableName
					+ " AS T WHERE T.PARENT=? ORDER BY T.SORT DESC";
			prep = con.prepareStatement(SQL);

			prep.setInt(1, data.getParent());
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
					+ " (TITLE, PARENT, ISGROUP, SORT, OPTIONS"
					+ additionKeysString() + ") VALUES (?,?,?,?,?"
					+ additionValuesString() + ");";
			prep = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);

			String title = data.isTitleIncrement() ? data.getTitle() + " "
					+ Integer.toString(sort) : data.getTitle();
			prep.setString(1, title);
			if (data.getParent() == 0)
				prep.setNull(2, java.sql.Types.INTEGER);
			else
				prep.setInt(2, data.getParent());
			prep.setBoolean(3, data.isGroup());
			prep.setInt(4, sort);
			prep.setString(5, DbOptions.save(data.getOptions()));

			setAdditions(prep, data);

			ResultSet generatedKeys = null;
			try {
				int affectedRows = prep.executeUpdate();
				if (affectedRows == 0)
					throw new SQLException();

				generatedKeys = prep.getGeneratedKeys();
				if (generatedKeys.next()) {
					// added = new BookInfo();
					data.setId(generatedKeys.getInt(1));
					data.setTitle(title);
					// added.parent = data
				} else
					throw new SQLException();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				generatedKeys.close();
			}

			if (!stopUpdate) {
				ITreeItemInfo _parent = get(data.getParent());
				if (_parent != null && parent.isRoot())
					_parent.setRoot();
				App.br.post(updateEvent, getUpdateEventData(_parent, data));
				// getUpdateEventData(get(data.getParent()), data));
			}
		} catch (Exception e) {
			throw new InvocationTargetException(e);

		}

	}

	protected void setAdditions(PreparedStatement prep, ITreeItemInfo data)
			throws SQLException {

	}

	protected String additionValuesString() {
		return "";
	}

	protected String additionKeysString() {
		return "";
	}

	@Override
	public List<ITreeItemInfo> getRoot() {
		List<ITreeItemInfo> result = new ArrayList<ITreeItemInfo>();

		try {
			Connection con = db.getConnection();
			String SQL = "SELECT " + getItemString("T") + "FROM " + tableName
					+ " AS T WHERE T.PARENT IS NULL "
					+ additionRootWHEREString() + " ORDER BY T.SORT, T.ID";
			PreparedStatement prep = con.prepareStatement(SQL);
			setAdditionRoot(prep);
			ResultSet rs = prep.executeQuery();
			try {
				if (rs.next()) {

					ITreeItemInfo root = getItem(rs);
					root.setRoot();
					result.add(root);
				}
			} finally {
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	protected void setAdditionRoot(PreparedStatement prep) throws SQLException {

	}

	protected String additionRootWHEREString() {
		return "";
	}

	@Override
	public List<ITreeItemInfo> getChildren(int parent) {
		List<ITreeItemInfo> result = new ArrayList<ITreeItemInfo>();
		// Connection con = null;
		try {
			Connection con = db.getConnection();
			String SQL = "SELECT " + getItemString("T") + "FROM " + tableName
					+ " AS T WHERE T.PARENT=? ORDER BY T.SORT, T.ID";

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
			String SQL = "SELECT COUNT(ID) FROM " + tableName
					+ "  WHERE PARENT=?";
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

	public void deleteChildren(ITreeItemInfo item) {

		try {
			Connection con = db.getConnection();

			String SQL = "DELETE FROM " + tableName + " WHERE PARENT=?;";
			PreparedStatement prep;

			prep = con.prepareStatement(SQL);

			prep.setInt(1, item.getId());

			prep.executeUpdate();
			// int affectedRows = prep.executeUpdate();
			// if (affectedRows == 0)
			// throw new SQLException();
			App.br.post(updateEvent, getUpdateEventData(item, item));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public ITreeItemInfo get(int item) {
		try {
			Connection con = db.getConnection();
			String SQL = "SELECT " + getItemString("T") + "FROM " + tableName
					+ " AS T " + "WHERE T.ID=?";

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
			String SQL = "SELECT TOP 1 " + getItemString("T") + "FROM "
					+ tableName
					+ " AS T WHERE T.PARENT=? ORDER BY T.SORT DESC, T.ID DESC";

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
	public void delete(ITreeItemInfo item) {
		ITreeItemInfo parent = get(item.getParent());

		if (parent == null && !canDeleteRoot())
			return;

		try {
			Connection con = db.getConnection();

			String SQL = "DELETE FROM " + tableName + " WHERE ID=?;";
			PreparedStatement prep;

			prep = con.prepareStatement(SQL);

			prep.setInt(1, item.getId());

			prep.executeUpdate();
			// int affectedRows = prep.executeUpdate();
			// if (affectedRows == 0)
			// throw new SQLException();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected boolean canDeleteRoot() {
		return false;
	}

	@Override
	public void delete(ITreeItemSelection selection) {
		int parent = selection.getParent();

		Iterator<ITreeItemInfo> iterator = selection.iterator();
		while (iterator.hasNext()) {
			ITreeItemInfo item = iterator.next();
			delete(item);
			if (item.isRoot() && canDeleteRoot())
				break;
		}
		if (parent != 0)
			selectLast(parent);
	}

	public void selectLast(int index) {
		ITreeItemInfo parent = get(index);

		ITreeItemInfo selected = getLast(parent.getId());
		if (selected == null)
			selected = parent;

		App.br.post(updateEvent, getUpdateEventData(parent, selected));
	}

	public void expand(ITreeItemInfo item) {

		App.br.post(updateEvent + "_EXPAND", getUpdateEventData(item, item));
	}

	@Override
	public Boolean setParent(ITreeItemInfo item, ITreeItemInfo target) {
		try {

			// ITreeItemInfo parent = getParent(item.getId());

			Connection con = db.getConnection();

			String SQL = "UPDATE " + tableName + " SET PARENT=? WHERE ID=?;";
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

			App.br.post(updateEvent, getUpdateEventData(target, item));

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
			App.br.post(updateEvent, getUpdateEventData(parent, item));
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
			App.br.post(updateEvent, getUpdateEventData(parent, item));

		return true;
	}

	@Override
	public void updateOrder(List<ITreeItemInfo> items) {
		try {
			Connection con = db.getConnection();
			int order = 0;
			for (ITreeItemInfo item : items) {

				String SQL = "UPDATE " + tableName + " SET SORT=? WHERE ID=?;";
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
			String SQL = "UPDATE " + tableName + " SET TITLE=? WHERE ID=?;";
			PreparedStatement prep = con.prepareStatement(SQL,
					Statement.CLOSE_CURRENT_RESULT);

			prep.setString(1, item.getTitle());
			prep.setInt(2, item.getId());
			int affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public void saveOptions(ITreeItemInfo data)
			throws InvocationTargetException {

		try {
			Connection con = db.getConnection();
			String SQL;
			PreparedStatement prep;

			SQL = "UPDATE " + tableName + " SET OPTIONS=?  WHERE ID=?;";

			prep = con.prepareStatement(SQL, Statement.CLOSE_CURRENT_RESULT);

			prep.setString(1, DbOptions.save(data.getOptions()));
			prep.setInt(2, data.getId());
			int affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			// App.br.post(updateEvent,
			// getUpdateEventData(get(data.getParent()), data));
			App.br.post(Events.EVENT_UPDATE_LABELS, new EVENT_UPDATE_VIEW_DATA(
					db, data));
			// getUpdateEventData(get(data.getParent()), data));

		} catch (Exception e) {
			throw new InvocationTargetException(e);

		}

	}

	public <T> T getRootOptions(Class<T> clazz) {

		T result = null;

		try {
			Connection con = db.getConnection();
			String SQL = "SELECT TOP 1 T.OPTIONS FROM INFO AS T";
			PreparedStatement prep = con.prepareStatement(SQL);

			ResultSet rs = prep.executeQuery();
			try {
				if (rs.next()) {

					result = DbOptions.load(clazz, rs.getString(1));
				}
			} finally {
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;

	}

	public void saveRootOptions(DbOptions opt) {

		try {

			Connection con = db.getConnection();
			String SQL = "SELECT TOP 1 T.ID FROM INFO AS T;";
			Statement stat = con.createStatement();
			ResultSet rs = stat.executeQuery(SQL);

			try {

				PreparedStatement prep;
				if (rs.next()) {

					SQL = "UPDATE INFO SET OPTIONS=? WHERE ID=?;";
					prep = con.prepareStatement(SQL,
							Statement.CLOSE_CURRENT_RESULT);

					prep.setString(1, DbOptions.save(opt));
					prep.setInt(2, rs.getInt(1));
					int affectedRows = prep.executeUpdate();
					if (affectedRows == 0)
						throw new SQLException();

				} else {
					SQL = "INSERT INTO INFO (OPTIONS) VALUES (?);";
					prep = con.prepareStatement(SQL,
							Statement.CLOSE_CURRENT_RESULT);
					prep.setString(1, DbOptions.save(opt));

					int affectedRows = prep.executeUpdate();
					if (affectedRows == 0)
						throw new SQLException();

				}

			} finally {
				rs.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public ITreeItemInfo getTreeItem(String name, String fullName) {
		try {
			Connection con = db.getConnection();
			String SQL = "SELECT " + getItemString("T") + " FROM " + tableName
					+ " AS T " + "WHERE T.PATH=? OR T.PATH=?";

			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setString(1, name);
			prep.setString(2, fullName);
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
	public void download(IPath zipFolder, ITreeItemInfo item, String zipName)
			throws InvocationTargetException {

	}

	@Override
	public void upload(String path, ITreeItemInfo item)
			throws InvocationTargetException {

	}

	@Override
	public boolean check() {
		try {
			Connection con = db.getConnection();
			String SQL = "SELECT COUNT(T.ID) FROM " + tableName + " AS T "
					+ "WHERE T.ISGROUP=?";

			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setBoolean(1, false);
			ResultSet rs = prep.executeQuery();

			try {
				if (rs.next()) {

					int i = rs.getInt(1);
					return i < Const.FREE_TREE_ITEMS_COUNT;
				}
			} finally {
				rs.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public Connection getConnection() throws IllegalAccessException {
		try {
			return db.getConnection();
		} catch (Exception e) {
			throw new IllegalAccessException();
		}
	}

	public void stopUpdate() {
		this.stopUpdate = true;

	}

	public void startUpdate() {
		this.stopUpdate = false;

	}
}
