package ebook.module.confList;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ebook.core.pico;
import ebook.core.interfaces.IDbConnection;
import ebook.module.confList.tree.ListConfInfo;
import ebook.module.confList.tree.ListConfInfoOptions;
import ebook.module.db.DbOptions;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.TreeService;
import ebook.utils.Events;
import ebook.utils.PreferenceSupplier;

public class ConfListService extends TreeService {

	final static String tableName = "CONFS";
	final static String updateEvent = Events.EVENT_UPDATE_CONF_LIST;

	public ConfListService() {
		super(tableName, updateEvent, pico.get(IDbConnection.class));
	}

	// SERVICE
	// *****************************************************************

	@Override
	protected String getItemString(String table) {
		String s = "$Table.TITLE, $Table.ID, $Table.PARENT, $Table.ISGROUP, $Table.OPTIONS, $Table.PATH ";
		s = s.replaceAll("\\$Table", "T");
		return s;
	}

	@Override
	protected String additionKeysString() {
		return ", PATH";
	}

	@Override
	protected String additionValuesString() {
		return ", ?";
	}

	@Override
	protected void setAdditions(PreparedStatement prep, ITreeItemInfo data)
			throws SQLException {

		prep.setString(6, data.isGroup() ? "" : ((ListConfInfo) data).getPath()
				.toString());
	}

	@Override
	protected ITreeItemInfo getItem(ResultSet rs) throws SQLException {

		ListConfInfo info = new ListConfInfo();
		info.setTitle(rs.getString(1));
		info.setId(rs.getInt(2));
		info.setParent(rs.getInt(3));
		info.setGroup(rs.getBoolean(4));
		info.setOptions(DbOptions.load(ListConfInfoOptions.class,
				rs.getString(5)));
		info.setPath(rs.getString(6));
		return info;
	}

	@Override
	public ITreeItemInfo getSelected() {
		int id = PreferenceSupplier.getInt(PreferenceSupplier.SELECTED_CONF);

		return get(id);
	}

	@Override
	public void saveTitle(ITreeItemInfo item) {
		if (item.isGroup())
			super.saveTitle(item);
		else
			try {
				saveOptions(item);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
	}

}
