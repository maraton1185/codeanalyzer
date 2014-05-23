package codeanalyzer.module.books;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.eclipse.swt.graphics.Image;

import codeanalyzer.core.AppManager;
import codeanalyzer.core.Events;
import codeanalyzer.core.Events.EVENT_UPDATE_TREE_DATA;
import codeanalyzer.core.pico;
import codeanalyzer.core.interfaces.IDbService;
import codeanalyzer.core.models.DbOptions;
import codeanalyzer.module.books.list.ListBookInfo;
import codeanalyzer.module.books.list.ListBookInfoOptions;
import codeanalyzer.module.tree.ITreeItemInfo;
import codeanalyzer.module.tree.ITreeService;
import codeanalyzer.module.tree.TreeService;
import codeanalyzer.module.users.UserInfo;
import codeanalyzer.module.users.UserService;
import codeanalyzer.utils.PreferenceSupplier;

public class BookListService extends TreeService {

	final static String tableName = "BOOKS";
	final static String updateEvent = Events.EVENT_UPDATE_BOOK_LIST;

	UserService us = new UserService();

	public BookListService() {
		super(tableName, updateEvent, pico.get(IDbService.class));
	}

	// ******************************************************************
	public Image getImage(Connection con) {

		// ImageData data = new ImageData(stream);
		// ImageDescriptor image = ImageDescriptor.createFromImageData(data);
		// return image.createImage();

		return null;
	}

	// SERVICE
	// *****************************************************************
	@Override
	protected String getItemString(String table) {
		String s = "$Table.TITLE, $Table.ID, $Table.PARENT, $Table.ISGROUP, $Table.OPTIONS, $Table.ROLE ";
		s = s.replaceAll("\\$Table", "T");
		return s;
	}

	@Override
	protected ITreeItemInfo getItem(ResultSet rs) throws SQLException {

		ListBookInfo info = new ListBookInfo();
		info.title = rs.getString(1);
		info.id = rs.getInt(2);
		info.parent = rs.getInt(3);
		info.isGroup = rs.getBoolean(4);
		info.options = DbOptions.load(ListBookInfoOptions.class,
				rs.getString(5));
		// info.path = rs.getString(5);
		info.role = (UserInfo) us.get(rs.getInt(6));
		return info;
	}

	@Override
	public void saveOptions(ITreeItemInfo _data)
			throws InvocationTargetException {
		ListBookInfo data = (ListBookInfo) _data;
		try {
			Connection con = db.getConnection();
			String SQL;
			PreparedStatement prep;

			SQL = "UPDATE " + tableName + " SET OPTIONS=?, ROLE=?  WHERE ID=?;";

			prep = con.prepareStatement(SQL, Statement.CLOSE_CURRENT_RESULT);

			prep.setString(1, DbOptions.save(data.getOptions()));
			if (data.role == null)
				prep.setNull(2, java.sql.Types.INTEGER);
			// prep.setInt(2, 0);
			else
				prep.setInt(2, data.role.getId());
			prep.setInt(3, data.getId());
			int affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			AppManager.br.post(updateEvent,
					new EVENT_UPDATE_TREE_DATA(get(data.getParent()), data));

		} catch (Exception e) {
			throw new InvocationTargetException(e);

		}

	}

	@Override
	public void add(ITreeItemInfo item, ITreeItemInfo parent_item, boolean sub)
			throws InvocationTargetException {

		ListBookInfo parent = (ListBookInfo) parent_item;
		ListBookInfo data = (ListBookInfo) item;

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

			SQL = "INSERT INTO "
					+ tableName
					+ " (TITLE, PARENT, ISGROUP, SORT, OPTIONS) VALUES (?,?,?,?,?);";
			prep = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);

			prep.setString(1, data.title);
			if (data.parent == 0)
				prep.setNull(2, java.sql.Types.INTEGER);
			else
				prep.setInt(2, data.parent);
			prep.setBoolean(3, data.isGroup);
			prep.setInt(4, sort);
			prep.setString(5, DbOptions.save(data.options));

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

	public List<UserInfo> getBookRoles() {

		return us.getBookRoles();
	}

	@Override
	public ITreeItemInfo getSelected() {
		int id = PreferenceSupplier.getInt(PreferenceSupplier.SELECTED_BOOK);

		return get(id);
	}
}
