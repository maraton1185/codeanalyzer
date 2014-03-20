package codeanalyzer.book.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import codeanalyzer.book.BookInfo;
import codeanalyzer.book.BookSection;
import codeanalyzer.core.AppManager;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Const.EVENT_UPDATE_CONTENT_VIEW_DATA;

public class BookSectionsService {

	private BookInfo book = new BookInfo();

	// public BookSectionsService(BookInfo book) {
	// this.book = book;
	// try {
	// book.openConnection();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	public void setBook(BookInfo book) throws IllegalAccessException {
		this.book = book;
		try {
			book.openConnection();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalAccessException();
		}
	}

	public void add(BookSection section) {
		BookSection parent = getParent(section);
		if (parent == null) {
			add_sub(section);
			return;
		}
		add_sub(parent);

	}

	public void add_sub(BookSection section) {

		BookSection sec = null;
		try {
			Connection con = book.getConnection();

			String SQL = "INSERT INTO SECTIONS (TITLE, PARENT) VALUES (?,?);";
			PreparedStatement prep;

			prep = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);

			String title = "Новый раздел";
			prep.setString(1, title);
			if (section.id == 0)
				prep.setNull(2, java.sql.Types.INTEGER);
			else
				prep.setInt(2, section.id);

			ResultSet generatedKeys = null;
			try {
				int affectedRows = prep.executeUpdate();
				if (affectedRows == 0)
					throw new SQLException();

				generatedKeys = prep.getGeneratedKeys();
				if (generatedKeys.next()) {
					sec = new BookSection();
					sec.title = title;
					sec.id = generatedKeys.getInt(1);
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

		// AppManager.br.post(Const.EVENT_UPDATE_CONTENT_VIEW, book);

		AppManager.br.post(Const.EVENT_UPDATE_CONTENT_VIEW,
				new EVENT_UPDATE_CONTENT_VIEW_DATA(section, sec));
	}

	public void delete(BookSection section) {
		BookSection parent = getParent(section);
		if (parent == null)
			return;

		try {
			Connection con = book.getConnection();

			String SQL = "DELETE FROM SECTIONS WHERE ID=?;";
			PreparedStatement prep;

			prep = con.prepareStatement(SQL);

			prep.setInt(1, section.id);
			// if (section.id == 0)
			// prep.setNull(2, java.sql.Types.INTEGER);
			// else
			// prep.setInt(2, section.id);

			int affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

		} catch (Exception e) {
			e.printStackTrace();
		}

		BookSection selected = getLast(parent);
		if (selected == null)
			selected = parent;

		// AppManager.br.post(Const.EVENT_UPDATE_CONTENT_VIEW, book);
		AppManager.br.post(Const.EVENT_UPDATE_CONTENT_VIEW,
				new EVENT_UPDATE_CONTENT_VIEW_DATA(parent, selected));
	}

	public List<BookSection> get() {
		List<BookSection> result = new ArrayList<BookSection>();
		try {
			Connection con = book.getConnection();
			String SQL = "Select T.TITLE, T.ID FROM SECTIONS AS T WHERE T.PARENT IS NULL ORDER BY T.SORT, T.ID";
			PreparedStatement prep = con.prepareStatement(SQL);
			// prep.setNull(1, java.sql.Types.INTEGER);
			ResultSet rs = prep.executeQuery();
			try {
				while (rs.next()) {

					BookSection sec = new BookSection();
					sec.title = rs.getString(1);
					sec.id = rs.getInt(2);
					result.add(sec);
				}
			} finally {
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public List<BookSection> getChildren(BookSection parent) {

		List<BookSection> result = new ArrayList<BookSection>();
		try {
			Connection con = book.getConnection();
			String SQL = "Select T.TITLE, T.ID FROM SECTIONS AS T WHERE T.PARENT=? ORDER BY T.SORT, T.ID";

			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setInt(1, parent.id);
			ResultSet rs = prep.executeQuery();

			try {
				while (rs.next()) {

					BookSection sec = new BookSection();
					sec.title = rs.getString(1);
					sec.id = rs.getInt(2);
					result.add(sec);
				}
			} finally {
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public BookSection getParent(BookSection section) {
		try {
			Connection con = book.getConnection();
			String SQL = "Select T1.TITLE, T1.ID FROM SECTIONS AS T "
					+ "INNER JOIN SECTIONS AS T1 ON T.PARENT = T1.ID "
					+ "WHERE T.ID=?";

			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setInt(1, section.id);
			ResultSet rs = prep.executeQuery();

			try {
				if (rs.next()) {

					BookSection sec = new BookSection();
					sec.title = rs.getString(1);
					sec.id = rs.getInt(2);
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

	private BookSection getLast(BookSection parent) {
		try {
			Connection con = book.getConnection();
			String SQL = "Select TOP 1 T.TITLE, T.ID FROM SECTIONS AS T WHERE T.PARENT=? ORDER BY T.SORT, T.ID DESC";
			// String SQL = "Select T1.TITLE, T1.ID FROM SECTIONS AS T "
			// + "INNER JOIN SECTIONS AS T1 ON T.PARENT = T1.ID "
			// + "WHERE T.ID=?";

			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setInt(1, parent.id);
			ResultSet rs = prep.executeQuery();

			try {
				if (rs.next()) {

					BookSection sec = new BookSection();
					sec.title = rs.getString(1);
					sec.id = rs.getInt(2);
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

	public boolean hasChildren(BookSection section) {
		try {
			Connection con = book.getConnection();
			String SQL = "Select COUNT(ID) from SECTIONS WHERE PARENT=?";
			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setInt(1, section.id);
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
