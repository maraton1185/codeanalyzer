package codeanalyzer.books.book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.eclipse.swt.graphics.Image;

public class BookService {

	// ******************************************************************
	public Image getImage(Connection con) {

		// ImageData data = new ImageData(stream);
		// ImageDescriptor image = ImageDescriptor.createFromImageData(data);
		// return image.createImage();

		return null;
	}

	public void getData(Connection con, BookInfo info) throws SQLException {
		String SQL = "Select TOP 1 T.DESCRIPTION, T.EDIT_MODE FROM INFO AS T";
		Statement stat = con.createStatement();
		ResultSet rs = stat.executeQuery(SQL);
		try {
			if (rs.next()) {
				info.setDescription(rs.getString(1));
				info.setEditMode(rs.getBoolean(2));
			} else {
				info.setDescription("Новая книга");
				info.setEditMode(true);
			}
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
				SQL = "UPDATE INFO SET DESCRIPTION=?, EDIT_MODE=? WHERE ID=?;";
				PreparedStatement prep = con.prepareStatement(SQL,
						Statement.CLOSE_CURRENT_RESULT);

				prep.setString(1, info.getDescription());
				prep.setBoolean(2, info.isEditMode());
				prep.setInt(3, rs.getInt(1));
				int affectedRows = prep.executeUpdate();
				if (affectedRows == 0)
					throw new SQLException();
			} else {
				SQL = "INSERT INTO INFO (DESCRIPTION, EDIT_MODE) VALUES (?,?);";
				PreparedStatement prep = con.prepareStatement(SQL,
						Statement.CLOSE_CURRENT_RESULT);

				prep.setString(1, info.getDescription());
				prep.setBoolean(2, info.isEditMode());
				int affectedRows = prep.executeUpdate();
				if (affectedRows == 0)
					throw new SQLException();
			}
		} finally {
			rs.close();
		}
	}

}
