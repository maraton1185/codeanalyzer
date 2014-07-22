package ebook.module.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import ebook.core.App;
import ebook.core.pico;
import ebook.core.interfaces.IDbConnection;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.userList.views.RoleViewModel;

public class ACLService {

	protected IDbConnection db;
	private final String tableName;

	// private final String updateEvent;

	public ACLService() {

		this.db = pico.get(IDbConnection.class);
		this.tableName = "ACL";
		// this.updateEvent = "";
	}

	public Set<RoleViewModel> get(Integer book) {

		Set<RoleViewModel> result = new HashSet<RoleViewModel>();

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
					RoleViewModel item = new RoleViewModel(rs.getInt(1));
					result.add(item);
				}
			} finally {
				rs.close();
			}

			if (result.isEmpty()) {

				ITreeItemInfo b = App.srv.bl().get(book);
				if (b == null)
					return result;

				Integer parent = b.getParent();
				if (parent != null)
					result = get(parent);

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

				if (!(item instanceof RoleViewModel))
					continue;

				SQL = "INSERT INTO " + tableName
						+ " (BOOK, ROLE) VALUES (?,?);";
				prep = con
						.prepareStatement(SQL, Statement.CLOSE_CURRENT_RESULT);

				prep.setInt(1, book);
				prep.setInt(2, ((RoleViewModel) item).getId());

				affectedRows = prep.executeUpdate();
				if (affectedRows == 0)
					throw new SQLException();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
