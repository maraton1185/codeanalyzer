package codeanalyzer.book.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;

import codeanalyzer.book.BookInfo;
import codeanalyzer.book.BookSection;

public class BookService {

	public Image getImage(Connection con) {

		// ImageData data = new ImageData(stream);
		// ImageDescriptor image = ImageDescriptor.createFromImageData(data);
		// return image.createImage();

		return null;
	}

	public void getData(Connection con, BookInfo info) throws SQLException {
		String SQL = "Select TOP 1 T.DESCRIPTION FROM INFO AS T";
		Statement stat = con.createStatement();
		ResultSet rs = stat.executeQuery(SQL);
		try {
			if (rs.next())
				info.setDescription(rs.getString(1));
			else
				info.setDescription("Новая книга");
		} finally {
			rs.close();
		}
	}

	public void setData(Connection con, BookInfo info) throws SQLException {

		String SQL = "SELECT TOP 1 T.ID FROM INFO AS T;";
		Statement stat = con.createStatement();
		ResultSet rs = stat.executeQuery(SQL);
		try {
			if (rs.next()) {
				SQL = "UPDATE INFO SET DESCRIPTION=? WHERE ID=?;";
				PreparedStatement prep = con.prepareStatement(SQL,
						Statement.CLOSE_CURRENT_RESULT);

				prep.setString(1, info.getDescription());
				prep.setInt(2, rs.getInt(1));
				int affectedRows = prep.executeUpdate();
				if (affectedRows == 0)
					throw new SQLException();
			} else {
				SQL = "INSERT INTO INFO (DESCRIPTION) VALUES (?);";
				PreparedStatement prep = con.prepareStatement(SQL,
						Statement.CLOSE_CURRENT_RESULT);

				prep.setString(1, info.getDescription());
				int affectedRows = prep.executeUpdate();
				if (affectedRows == 0)
					throw new SQLException();
			}
		} finally {
			rs.close();
		}

		// int affectedRows = prep.executeUpdate();
		// if (affectedRows == 0)
		// throw new SQLException();
		//
		// String

		// Integer index = 0;
		// ResultSet generatedKeys = null;
		// try {
		// generatedKeys = prep.getGeneratedKeys();
		// if (generatedKeys.next())
		// index = generatedKeys.getInt(1);
		// else
		// throw new SQLException();
		// } finally {
		// generatedKeys.close();
		// }
	}

	public List<BookSection> getSections(Connection con) throws SQLException {

		List<BookSection> result = new ArrayList<BookSection>();

		String SQL = "Select T.TITLE FROM SECTIONS AS T ORDER BY T.SORT";
		Statement stat = con.createStatement();
		ResultSet rs = stat.executeQuery(SQL);
		try {
			while (rs.next()) {

				BookSection sec = new BookSection();
				sec.title = rs.getString(1);
				result.add(sec);
			}
		} finally {
			rs.close();
		}

		return result;
	}

	public List<BookSection> getChildren(Connection con, int parent)
			throws SQLException {
		List<BookSection> result = new ArrayList<BookSection>();

		String SQL = "Select T.TITLE FROM SECTIONS AS T ORDER BY T.SORT WHERE T.PARENT=?";

		PreparedStatement prep = con.prepareStatement(SQL);
		prep.setInt(1, parent);
		ResultSet rs = prep.executeQuery();

		try {
			while (rs.next()) {

				BookSection sec = new BookSection();
				sec.title = rs.getString(1);
				result.add(sec);
			}
		} finally {
			rs.close();
		}

		return result;
	}

	public BookSection getParent(Connection con, int id) throws SQLException {

		String SQL = "Select T1.TITLE FROM SECTIONS AS T ORDER BY T.SORT WHERE T.ID=?"
				+ "LEFT JOIN SECTIONS AS T1 ON T.PARENT = T1.ID";

		PreparedStatement prep = con.prepareStatement(SQL);
		prep.setInt(1, id);
		ResultSet rs = prep.executeQuery();

		try {
			if (rs.next()) {

				BookSection sec = new BookSection();
				sec.title = rs.getString(1);
				return sec;
			}
		} finally {
			rs.close();
		}

		return null;
	}

	public boolean hasChildren(Connection con, int id) throws SQLException {

		String SQL = "Select COUNT(ID) from SECTIONS WHERE PARENT=?";
		PreparedStatement prep = con.prepareStatement(SQL);
		prep.setInt(1, id);
		ResultSet rs = prep.executeQuery();

		try {
			if (rs.next())
				return true;
			else
				return false;
		} finally {
			rs.close();
		}

	}

	public void addSection(Connection con, int id) throws SQLException {

		String SQL = "INSERT INTO SECTIONS (TITLE, PARENT) VALUES (?,?);";
		PreparedStatement prep = con.prepareStatement(SQL);
		prep.setString(1, "Новый раздел");
		if (id == 0)
			prep.setNull(2, java.sql.Types.INTEGER);
		else
			prep.setInt(2, id);

		int affectedRows = prep.executeUpdate();
		if (affectedRows == 0)
			throw new SQLException();

	}
}
