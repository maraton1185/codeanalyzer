package codeanalyzer.book.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import codeanalyzer.book.BookInfo;
import codeanalyzer.book.BookSection;
import codeanalyzer.core.AppManager;
import codeanalyzer.utils.Const;

public class BookSectionsService {

	private BookInfo book;

	public void setBook(BookInfo book) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		this.book = book;
		book.openConnection();

	}

	public void add(BookSection section) {

		try {
			Connection con = book.getConnection();

			String SQL = "INSERT INTO SECTIONS (TITLE, PARENT) VALUES (?,?);";
			PreparedStatement prep;

			prep = con.prepareStatement(SQL);

			prep.setString(1, "Новый раздел");
			if (section.id == 0)
				prep.setNull(2, java.sql.Types.INTEGER);
			else
				prep.setInt(2, section.id);

			int affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

		} catch (Exception e) {
			e.printStackTrace();
		}

		AppManager.br.post(Const.EVENT_UPDATE_CONTENT_VIEW, null);
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
			String SQL = "Select T1.TITLE, T1.ID FROM SECTIONS AS T WHERE T.ID=? "
					+ "LEFT JOIN SECTIONS AS T1 ON T.PARENT = T1.ID";

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
