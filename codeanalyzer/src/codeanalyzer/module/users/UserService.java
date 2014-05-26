package codeanalyzer.module.users;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import codeanalyzer.core.App;
import codeanalyzer.core.pico;
import codeanalyzer.core.interfaces.IDbConnection;
import codeanalyzer.core.models.DbOptions;
import codeanalyzer.module.tree.ITreeItemInfo;
import codeanalyzer.module.tree.ITreeService;
import codeanalyzer.module.tree.TreeService;
import codeanalyzer.module.users.tree.UserInfo;
import codeanalyzer.module.users.tree.UserInfoOptions;
import codeanalyzer.utils.Events;
import codeanalyzer.utils.PreferenceSupplier;

public class UserService extends TreeService {

	// IDbService db = pico.get(IDbService.class);

	final static String tableName = "USERS";
	final static String updateEvent = Events.EVENT_UPDATE_USERS;

	public UserService() {
		super(tableName, updateEvent, pico.get(IDbConnection.class));
	}

	@Override
	protected ITreeItemInfo getItem(ResultSet rs) throws SQLException {

		UserInfo info = new UserInfo();
		info.setTitle(rs.getString(1));
		info.setId(rs.getInt(2));
		info.setParent(rs.getInt(3));
		info.setGroup(rs.getBoolean(4));
		info.options = DbOptions.load(UserInfoOptions.class, rs.getString(5));
		return info;
	}

	@Override
	public void saveTitle(ITreeItemInfo item) {

		super.saveTitle(item);

		App.br.post(Events.EVENT_UPDATE_USER_INFO, null);
	}

	@Override
	public void add(ITreeItemInfo item, ITreeItemInfo parent_item, boolean sub)
			throws InvocationTargetException {

		try {
			Connection con = db.getConnection();
			String SQL;
			PreparedStatement prep;
			ResultSet rs;

			SQL = "SELECT Top 1 T.ID FROM " + tableName
					+ " AS T WHERE T.TITLE=?";
			prep = con.prepareStatement(SQL);

			prep.setString(1, item.getTitle());
			rs = prep.executeQuery();

			try {
				if (rs.next())
					throw new SQLException();

			} finally {
				rs.close();
			}

			super.add(item, parent_item, sub);

			App.br.post(Events.EVENT_UPDATE_USER_ROLES, null);

		} catch (Exception e) {
			throw new InvocationTargetException(e);

		}

	}

	public List<UserInfo> getBookRoles() {
		List<UserInfo> result = new ArrayList<UserInfo>();

		try {
			Connection con = db.getConnection();
			String SQL = "SELECT "
					+ getItemString("T")
					+ "FROM "
					+ tableName
					+ " AS T WHERE T.PARENT=? AND T.ISGROUP  ORDER BY T.SORT, T.ID";
			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setInt(1, ITreeService.rootId);
			ResultSet rs = prep.executeQuery();

			try {
				while (rs.next()) {

					result.add((UserInfo) getItem(rs));
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
	public ITreeItemInfo getSelected() {
		int id = PreferenceSupplier.getInt(PreferenceSupplier.SELECTED_USER);

		return get(id);
	}
}
