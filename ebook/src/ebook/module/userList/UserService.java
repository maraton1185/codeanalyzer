package ebook.module.userList;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ebook.core.App;
import ebook.core.pico;
import ebook.core.interfaces.IDbConnection;
import ebook.core.models.DbOptions;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.ITreeService;
import ebook.module.tree.TreeItemInfoSelection;
import ebook.module.tree.TreeService;
import ebook.module.userList.tree.UserInfo;
import ebook.module.userList.tree.UserInfoOptions;
import ebook.utils.Events;
import ebook.utils.PreferenceSupplier;

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
		info.setOptions(DbOptions.load(UserInfoOptions.class, rs.getString(5)));
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
					+ " AS T WHERE T.TITLE=? AND NOT T.ISGROUP";
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

	@Override
	public void delete(ITreeItemInfo item) {
		super.delete(item);
		App.br.post(Events.EVENT_UPDATE_USER_ROLES, null);
	}

	@Override
	public void delete(TreeItemInfoSelection selection) {
		super.delete(selection);
		App.br.post(Events.EVENT_UPDATE_USER_ROLES, null);
	}

	@Override
	public void updateOrder(List<ITreeItemInfo> items) {
		super.updateOrder(items);
		App.br.post(Events.EVENT_UPDATE_USER_ROLES, null);
	}

	public List<UserInfo> getRoles() {
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

	public List<UserInfo> find(String user) {
		List<UserInfo> result = new ArrayList<UserInfo>();

		try {
			Connection con = db.getConnection();
			String SQL = "SELECT "
					+ getItemString("T")
					+ "FROM "
					+ tableName
					+ " AS T WHERE T.TITLE=? AND NOT T.ISGROUP  ORDER BY T.SORT, T.ID";
			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setString(1, user);
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

	public UserInfo getRole(UserInfo user) {

		ITreeItemInfo parent = get(user.getParent());

		while (parent == null ? false : parent.getId() != ITreeService.rootId) {

			parent = get(parent.getParent());

		}
		return (UserInfo) parent;
	}
}
