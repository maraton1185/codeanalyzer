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

import codeanalyzer.books.book.CurrentBookInfo;
import codeanalyzer.core.AppManager;
import codeanalyzer.core.model.BookInfo;
import codeanalyzer.db.interfaces.IDbManager;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Const.EVENT_UPDATE_BOOK_LIST_DATA;

public class DbManager implements IDbManager {

	DbStructure dbStructure = new DbStructure();
	private boolean exist;

	@Override
	public void init() throws InvocationTargetException {

		Connection con = null;
		try {
			con = getConnection();
			if (exist)
				try {
					dbStructure.checkSructure(con);
				} catch (Exception e) {
					dbStructure.createStructure(con);
				}
			else
				dbStructure.createStructure(con);

		} catch (Exception e) {
			throw new InvocationTargetException(e, e.getMessage());
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				throw new InvocationTargetException(e,
						Const.ERROR_CONFIG_OPEN_DATABASE);
			}
		}

	}

	private Connection getConnection() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException,
			URISyntaxException {

		// Bundle bundle = FrameworkUtil.getBundle(DbManager.class);
		// URL url = FileLocator.find(bundle, new Path("lib/db.h2.h2"), null);
		// String path = url.toURI(). getPath() + Const.SYSTEM_DB_NAME
		// + Const.DEFAULT_DB_EXTENSION;
		// URL url = getClass().getResource("/lib/db.h2.h2");
		String root = System.getProperty("user.dir");
		File f = new File(root + "\\" + Const.SYSTEM_DB_NAME
				+ Const.DEFAULT_DB_EXTENSION);
		exist = f.exists();

		Class.forName("org.h2.Driver").newInstance();
		// String ifExist = exist ? ";IFEXISTS=TRUE" : "";
		//
		// IPath path = exist ? getDbPath().removeFileExtension()
		// .removeFileExtension() : getPath().append(Const.SYSTEM_DB_NAME);

		return DriverManager.getConnection("jdbc:h2:" + Const.SYSTEM_DB_NAME,
				"sa", "");
	}

	@Override
	public List<BookInfo> getBooks(int parent) {

		List<BookInfo> result = new ArrayList<BookInfo>();
		try {
			Connection con = getConnection();
			String SQL = "Select T.TITLE, T.ID, T.PARENT, T.GROUP, T.PATH FROM BOOKS AS T WHERE T.PARENT=? ORDER BY T.SORT, T.ID";

			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setInt(1, parent);
			ResultSet rs = prep.executeQuery();

			try {
				while (rs.next()) {

					BookInfo info = new BookInfo();
					info.title = rs.getString(1);
					info.id = rs.getInt(2);
					info.parent = rs.getInt(3);
					info.isGroup = rs.getBoolean(4);
					info.path = rs.getString(5);

					result.add(info);
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
	public void addBooksGroup(String title, BookInfo current, boolean sub) {

		BookInfo data = new BookInfo();
		data.title = title;
		if (current == null)
			data.parent = 0;
		else if (current.isGroup)
			data.parent = sub ? current.id : current.parent;
		else
			data.parent = current.parent;
		data.isGroup = true;
		data.path = "";
		addBook(data);

	}

	@Override
	public void addBook(CurrentBookInfo book, BookInfo current) {

		BookInfo data = new BookInfo();
		data.title = book.getName();
		if (current == null)
			data.parent = 0;
		else if (current.isGroup)
			data.parent = current.id;
		else
			data.parent = current.parent;
		data.isGroup = false;
		data.path = book.getFullName();
		addBook(data);
	}

	private void addBook(BookInfo data) {

		BookInfo added = null;
		try {
			Connection con = getConnection();
			String SQL;
			PreparedStatement prep;

			SQL = "Select Top 1 T.SORT FROM BOOKS AS T WHERE T.PARENT=? ORDER BY T.SORT DESC";
			prep = con.prepareStatement(SQL);

			prep.setInt(1, 0);
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
			} finally {
				generatedKeys.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		AppManager.br.post(Const.EVENT_UPDATE_BOOK_LIST,
				new EVENT_UPDATE_BOOK_LIST_DATA(data.parent, added));
	}

	@Override
	public void delete(BookInfo book) {

		try {
			Connection con = getConnection();

			String SQL = "DELETE FROM BOOKS WHERE ID=?;";
			PreparedStatement prep;

			prep = con.prepareStatement(SQL);

			prep.setInt(1, book.id);

			int affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

		} catch (Exception e) {
			e.printStackTrace();
		}

		BookInfo selected = getLast(book.parent);

		AppManager.br.post(Const.EVENT_UPDATE_BOOK_LIST,
				new EVENT_UPDATE_BOOK_LIST_DATA(book.parent, selected));

	}

	private BookInfo getLast(int parent) {
		try {
			Connection con = getConnection();
			String SQL = "Select TOP 1 T.ID FROM BOOKS AS T WHERE T.PARENT=? ORDER BY T.SORT DESC, T.ID DESC";

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
	public void saveTitle(BookInfo book) {
		try {
			// SectionInfo parent = getParent(section);

			Connection con = getConnection();
			String SQL = "UPDATE BOOKS SET TITLE=? WHERE ID=?;";
			PreparedStatement prep = con.prepareStatement(SQL,
					Statement.CLOSE_CURRENT_RESULT);

			prep.setString(1, book.title);
			prep.setInt(2, book.id);
			int affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			// AppManager.br.post(Const.EVENT_UPDATE_BOOK_LIST,
			// new EVENT_UPDATE_BOOK_LIST_DATA(book.parent, book));

			// AppManager.br.post(Const.EVENT_UPDATE_CONTENT_VIEW,
			// new EVENT_UPDATE_VIEW_DATA(book, section, true));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean hasChildren(int parent) {
		try {
			Connection con = getConnection();
			String SQL = "Select COUNT(ID) from BOOKS WHERE PARENT=?";
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

}
