package codeanalyzer.core.db;

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
import codeanalyzer.core.db.interfaces.IDbManager;
import codeanalyzer.core.db.model.BookInfo;
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
	public List<BookInfo> getBooks() {

		List<BookInfo> result = new ArrayList<BookInfo>();
		BookInfo e = new BookInfo();
		e.id = 1;
		e.parent = 0;
		e.title = "test";
		e.isGroup = false;
		e.path = "d://temp//book.h2.db";
		result.add(e);

		e = new BookInfo();
		e.id = 2;
		e.parent = 0;
		e.title = "test2";
		e.isGroup = true;
		e.path = "";
		result.add(e);

		return result;
	}

	@Override
	public void addBook(CurrentBookInfo book, BookInfo current) {

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

			prep.setString(1, book.getName());
			if (current == null)
				prep.setInt(2, 0);
			else if (current.isGroup)
				prep.setInt(2, current.id);
			else
				prep.setInt(2, current.parent);

			prep.setBoolean(3, false);
			prep.setInt(4, sort);
			prep.setString(5, book.getFullName());

			ResultSet generatedKeys = null;
			try {
				int affectedRows = prep.executeUpdate();
				if (affectedRows == 0)
					throw new SQLException();

				generatedKeys = prep.getGeneratedKeys();
				if (generatedKeys.next()) {
					added = new BookInfo();
					// sec.title = book.getName();
					added.id = generatedKeys.getInt(1);
					// sec.parent = parent == null ? 0 : parent.id;
					// sec.path
				} else
					throw new SQLException();
			} finally {
				generatedKeys.close();
			}

			// int affectedRows = prep.executeUpdate();
			// if (affectedRows == 0)
			// throw new SQLException();

		} catch (Exception e) {
			e.printStackTrace();
		}

		AppManager.br.post(Const.EVENT_UPDATE_BOOK_LIST,
				new EVENT_UPDATE_BOOK_LIST_DATA(book, current, added));
	}

	private BookInfo getParent(BookInfo current) {
		try {
			Connection con = getConnection();
			String SQL = "Select T1.TITLE, T1.ID, T1.PARENT, T1.BLOCK, T1.OPTIONS FROM SECTIONS AS T "
					+ "INNER JOIN SECTIONS AS T1 ON T.PARENT = T1.ID "
					+ "WHERE T.ID=?";

			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setInt(1, current.id);
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
}
