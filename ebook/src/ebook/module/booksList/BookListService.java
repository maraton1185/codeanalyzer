package ebook.module.booksList;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.eclipse.swt.graphics.Image;

import ebook.core.App;
import ebook.core.pico;
import ebook.core.interfaces.IDbConnection;
import ebook.core.models.DbOptions;
import ebook.module.booksList.tree.ListBookInfo;
import ebook.module.booksList.tree.ListBookInfoOptions;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.TreeService;
import ebook.module.users.tree.UserInfo;
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
		String s = "$Table.TITLE, $Table.ID, $Table.PARENT, $Table.ISGROUP, $Table.OPTIONS, $Table.ROLE ";
		s = s.replaceAll("\\$Table", "T");
		return s;
	}

	@Override
	protected ITreeItemInfo getItem(ResultSet rs) throws SQLException {

		ListBookInfo info = new ListBookInfo();
		info.setTitle(rs.getString(1));
		info.setId(rs.getInt(2));
		info.setParent(rs.getInt(3));
		info.setGroup(rs.getBoolean(4));
		info.options = DbOptions.load(ListBookInfoOptions.class,
				rs.getString(5));
		// info.path = rs.getString(5);
		info.role = (UserInfo) App.srv.us().get(rs.getInt(6));
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
}
