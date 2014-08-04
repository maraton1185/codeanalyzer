package ebook.module.bookList;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;

import ebook.core.App;
import ebook.core.pico;
import ebook.core.interfaces.IDbConnection;
import ebook.core.models.DbOptions;
import ebook.module.book.BookConnection;
import ebook.module.bookList.tree.ListBookInfo;
import ebook.module.bookList.tree.ListBookInfoOptions;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.TreeService;
import ebook.utils.Events;
import ebook.utils.PreferenceSupplier;

public class BookListService extends TreeService {

	final static String tableName = "BOOKS";
	final static String updateEvent = Events.EVENT_UPDATE_BOOK_LIST;

	public BookListService() {
		super(tableName, updateEvent, pico.get(IDbConnection.class));
	}

	// ******************************************************************

	public void addImage(Integer id, IPath p) {

		try {
			Connection con = db.getConnection();
			String SQL;
			PreparedStatement prep;

			SQL = "UPDATE " + tableName + " SET IMAGE=? WHERE ID=?;";
			prep = con.prepareStatement(SQL, Statement.CLOSE_CURRENT_RESULT);

			prep.setInt(2, id);

			File f = p.toFile();
			FileInputStream fis = new FileInputStream(f);

			BufferedInputStream inputStreamReader = new BufferedInputStream(fis);
			ImageData imageData = new ImageData(inputStreamReader);

			int mWidth = PreferenceSupplier
					.getInt(PreferenceSupplier.IMAGE_WIDTH);// options.scaledImageWidth;
			int width = imageData.width;
			int height = imageData.height;
			if (width > mWidth)
				imageData = imageData.scaledTo((mWidth), (int) ((float) height
						/ width * mWidth));

			ByteArrayOutputStream os = new ByteArrayOutputStream();

			ImageLoader loader = new ImageLoader();
			loader.data = new ImageData[] { imageData };
			int type;
			switch (p.getFileExtension()) {
			case "png":
				type = SWT.IMAGE_PNG;
				break;
			case "bmp":
				type = SWT.IMAGE_BMP;
			default:
				type = SWT.IMAGE_BMP;
				break;
			}
			loader.save(os, type);

			ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
			prep.setBinaryStream(1, is);

			int affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			App.br.post(Events.EVENT_UPDATE_BOOK_INFO, null);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void deleteImage(Integer id) {
		try {
			Connection con = db.getConnection();
			String SQL;
			PreparedStatement prep;

			SQL = "UPDATE " + tableName + " SET IMAGE=? WHERE ID=?;";
			prep = con.prepareStatement(SQL, Statement.CLOSE_CURRENT_RESULT);

			prep.setNull(1, java.sql.Types.BINARY);
			prep.setInt(2, id);

			int affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			App.br.post(Events.EVENT_UPDATE_BOOK_INFO, null);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public BufferedInputStream getImage(String book_id) {
		Integer id;
		try {
			id = Integer.parseInt(book_id);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		BufferedInputStream inputStreamReader = null;

		try {
			Connection con = db.getConnection();
			String SQL = "Select T.IMAGE FROM " + tableName
					+ " AS T WHERE T.ID=?";

			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setInt(1, id);
			ResultSet rs = prep.executeQuery();

			try {
				if (rs.next()) {

					InputStream is = rs.getBinaryStream(1);
					if (is == null)
						return null;
					inputStreamReader = new BufferedInputStream(is);
				}
			} finally {
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inputStreamReader;
	}

	// SERVICE
	// *****************************************************************
	@Override
	protected String getItemString(String table) {
		String s = "$Table.TITLE, $Table.ID, $Table.PARENT, $Table.ISGROUP, $Table.OPTIONS, $Table.PATH, $Table.IMAGE ";
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

		prep.setString(6, data.isGroup() ? "" : ((ListBookInfo) data).getPath()
				.toString());
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
		// info.role = (UserInfo) App.srv.us().get(rs.getInt(6));
		info.setPath(rs.getString(6));

		InputStream is = rs.getBinaryStream(7);
		if (is != null) {
			BufferedInputStream inputStreamReader = new BufferedInputStream(is);
			ImageData imageData = new ImageData(inputStreamReader);
			info.setImage(new Image(Display.getCurrent(), imageData));
		}

		info.setACL();

		return info;
	}

	// @Override
	// public void saveOptions(ITreeItemInfo _data)
	// throws InvocationTargetException {
	// ListBookInfo data = (ListBookInfo) _data;
	// try {
	// Connection con = db.getConnection();
	// String SQL;
	// PreparedStatement prep;
	//
	// SQL = "UPDATE " + tableName + " SET OPTIONS=? WHERE ID=?;";
	//
	// prep = con.prepareStatement(SQL, Statement.CLOSE_CURRENT_RESULT);
	//
	// prep.setString(1, DbOptions.save(data.getOptions()));
	// // if (data.role == null)
	// // prep.setNull(2, java.sql.Types.INTEGER);
	// // // prep.setInt(2, 0);
	// // else
	// // prep.setInt(2, data.role.getId());
	// prep.setInt(3, data.getId());
	// int affectedRows = prep.executeUpdate();
	// if (affectedRows == 0)
	// throw new SQLException();
	//
	// App.br.post(updateEvent,
	// new EVENT_UPDATE_TREE_DATA(get(data.getParent()), data));
	//
	// } catch (Exception e) {
	// throw new InvocationTargetException(e);
	//
	// }
	//
	// }

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

}
