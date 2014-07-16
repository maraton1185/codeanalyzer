package ebook.module.bookList;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.graphics.Image;

import ebook.core.App;
import ebook.core.pico;
import ebook.core.interfaces.IDbConnection;
import ebook.core.models.DbOptions;
import ebook.module.book.BookConnection;
import ebook.module.bookList.servlets.ListServletModel;
import ebook.module.bookList.tree.ListBookInfo;
import ebook.module.bookList.tree.ListBookInfoOptions;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.TreeService;
import ebook.module.userList.tree.UserInfo;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_TREE_DATA;
import ebook.utils.PreferenceSupplier;

public class BookListService extends TreeService {

	final static String tableName = "BOOKS";
	final static String updateEvent = Events.EVENT_UPDATE_BOOK_LIST;

	public BookListService() {
		super(tableName, updateEvent, pico.get(IDbConnection.class));
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
		String s = "$Table.TITLE, $Table.ID, $Table.PARENT, $Table.ISGROUP, $Table.OPTIONS, $Table.ROLE, $Table.PATH ";
		s = s.replaceAll("\\$Table", "T");
		return s;
	}

	@Override
	protected String additionKeysString() {
		return ", PATH";
	}

	@Override
	protected String additionValuesString() {
		return ", ?";
	}

	@Override
	protected void setAdditions(PreparedStatement prep, ITreeItemInfo data)
			throws SQLException {
		prep.setString(6, ((ListBookInfo) data).getPath().toString());
	}

	@Override
	protected ITreeItemInfo getItem(ResultSet rs) throws SQLException {

		ListBookInfo info = new ListBookInfo();
		info.setTitle(rs.getString(1));
		info.setId(rs.getInt(2));
		info.setParent(rs.getInt(3));
		info.setGroup(rs.getBoolean(4));
		info.setOptions(DbOptions.load(ListBookInfoOptions.class,
				rs.getString(5)));
		info.role = (UserInfo) App.srv.us().get(rs.getInt(6));
		info.setPath(rs.getString(7));

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

			App.br.post(updateEvent,
					new EVENT_UPDATE_TREE_DATA(get(data.getParent()), data));

		} catch (Exception e) {
			throw new InvocationTargetException(e);

		}

	}

	@Override
	public ITreeItemInfo getSelected() {
		int id = PreferenceSupplier.getInt(PreferenceSupplier.SELECTED_BOOK);

		return get(id);
	}

	public BookConnection getBook(String book_id) {

		Integer id;
		try {
			id = Integer.parseInt(book_id);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		ListBookInfo bookInfo = (ListBookInfo) get(id);
		if (bookInfo == null)
			return null;
		// ListBookInfoOptions opt = (ListBookInfoOptions)
		// bookInfo.getOptions();

		BookConnection book;
		try {
			book = new BookConnection(bookInfo.getPath(), false);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
		return book;
	}

	public ITreeItemInfo getBookTreeItem(IPath connectionPath) {
		try {
			Connection con = db.getConnection();
			String SQL = "SELECT " + getItemString("T") + " FROM " + tableName
					+ " AS T " + "WHERE T.PATH=?";

			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setString(1, connectionPath.toString());
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

	public ListServletModel getModel() {
		ListServletModel model = new ListServletModel();

		model.host = App.getJetty().host();

		return model;
	}
}
