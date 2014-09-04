package ebook.module.acl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ebook.core.App;
import ebook.core.pico;
import ebook.core.interfaces.IDbConnection;
import ebook.module.book.BookConnection;
import ebook.module.tree.item.ITreeItemInfo;

public class ACLService {

	public static class ACLResult {
		public boolean inherited = false;
	}

	protected IDbConnection db;
	private final String tableName;

	// private final String updateEvent;

	public ACLService() {

		this.db = pico.get(IDbConnection.class);
		this.tableName = "ACL";
		// this.updateEvent = "";
	}

	public boolean hasExplicit(Integer book) {

		try {
			Connection con = db.getConnection();

			String SQL;
			PreparedStatement prep;

			SQL = "SELECT TOP 1 T.ROLE FROM " + tableName
					+ " AS T WHERE T.BOOK=? AND T.SECTION IS NULL";
			prep = con.prepareStatement(SQL);
			prep.setInt(1, book);
			ResultSet rs = prep.executeQuery();

			try {
				if (rs.next()) {
					return true;
				}
			} finally {
				rs.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean hasExplicit(Integer book, Integer section) {

		try {
			Connection con = db.getConnection();

			String SQL;
			PreparedStatement prep;

			SQL = "SELECT TOP 1 T.ROLE FROM " + tableName
					+ " AS T WHERE T.BOOK=? AND T.SECTION=?";
			prep = con.prepareStatement(SQL);
			prep.setInt(1, book);
			prep.setInt(2, section);
			ResultSet rs = prep.executeQuery();

			try {
				if (rs.next()) {
					return true;
				}
			} finally {
				rs.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public List<AclViewModel> get(Integer book, ACLResult out) {

		List<AclViewModel> result = new ArrayList<AclViewModel>();

		try {
			Connection con = db.getConnection();

			String SQL;
			PreparedStatement prep;

			SQL = "SELECT T.ROLE FROM " + tableName
					+ " AS T WHERE T.BOOK=? AND T.SECTION IS NULL";
			prep = con.prepareStatement(SQL);
			prep.setInt(1, book);
			ResultSet rs = prep.executeQuery();

			try {
				while (rs.next()) {
					AclViewModel item = new AclViewModel(rs.getInt(1));
					result.add(item);
				}
			} finally {
				rs.close();
			}

			if (result.isEmpty()) {

				out.inherited = true;

				ITreeItemInfo b = App.srv.bl().get(book);
				if (b == null)
					return result;

				Integer parent = b.getParent();
				if (parent != null)
					result = get(parent, out);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public void set(Integer book, Object[] objects) {
		try {
			Connection con = db.getConnection();

			String SQL;
			PreparedStatement prep;

			SQL = "DELETE FROM " + tableName
					+ " WHERE BOOK=? AND SECTION IS NULL;";

			prep = con.prepareStatement(SQL);

			prep.setInt(1, book);

			int affectedRows = prep.executeUpdate();
			// if (affectedRows == 0)
			// throw new SQLException();

			for (Object item : objects) {

				if (!(item instanceof AclViewModel))
					continue;

				SQL = "INSERT INTO " + tableName
						+ " (BOOK, ROLE) VALUES (?,?);";
				prep = con
						.prepareStatement(SQL, Statement.CLOSE_CURRENT_RESULT);

				prep.setInt(1, book);
				prep.setInt(2, ((AclViewModel) item).getId());

				affectedRows = prep.executeUpdate();
				if (affectedRows == 0)
					throw new SQLException();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<AclViewModel> get(Integer b_id, Integer s_id, ACLResult out) {
		List<AclViewModel> result = new ArrayList<AclViewModel>();

		try {
			Connection con = db.getConnection();

			String SQL;
			PreparedStatement prep;

			SQL = "SELECT T.ROLE FROM " + tableName
					+ " AS T WHERE T.BOOK=? AND T.SECTION=?";
			prep = con.prepareStatement(SQL);
			prep.setInt(1, b_id);
			prep.setInt(2, s_id);
			ResultSet rs = prep.executeQuery();

			try {
				while (rs.next()) {
					AclViewModel item = new AclViewModel(rs.getInt(1));
					result.add(item);
				}
			} finally {
				rs.close();
			}

			if (result.isEmpty()) {

				out.inherited = true;

				BookConnection book = App.srv.bl().getBook(b_id.toString());

				ITreeItemInfo b = book.srv().get(s_id);
				if (b == null)
					return result;

				Integer parent = b.getParent();
				if (parent != null)
					result = get(b_id, parent, out);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public void set(Integer book, Integer s_id, Object[] objects) {
		try {
			Connection con = db.getConnection();

			String SQL;
			PreparedStatement prep;

			SQL = "DELETE FROM " + tableName + " WHERE BOOK=? AND SECTION=?;";

			prep = con.prepareStatement(SQL);

			prep.setInt(1, book);
			prep.setInt(2, s_id);

			int affectedRows = prep.executeUpdate();
			// if (affectedRows == 0)
			// throw new SQLException();

			for (Object item : objects) {

				if (!(item instanceof AclViewModel))
					continue;

				SQL = "INSERT INTO " + tableName
						+ " (BOOK, SECTION, ROLE) VALUES (?, ?,?);";
				prep = con
						.prepareStatement(SQL, Statement.CLOSE_CURRENT_RESULT);

				prep.setInt(1, book);
				prep.setInt(2, s_id);
				prep.setInt(3, ((AclViewModel) item).getId());

				affectedRows = prep.executeUpdate();
				if (affectedRows == 0)
					throw new SQLException();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
