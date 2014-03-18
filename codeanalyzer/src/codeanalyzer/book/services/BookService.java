package codeanalyzer.book.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.eclipse.swt.graphics.Image;

import codeanalyzer.book.BookInfo;

public class BookService {

	// ******************************************************************
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

}
