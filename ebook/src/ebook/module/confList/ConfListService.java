package ebook.module.confList;

import java.sql.ResultSet;
import java.sql.SQLException;

import ebook.core.pico;
import ebook.core.interfaces.IDbConnection;
import ebook.core.models.DbOptions;
import ebook.module.confList.tree.ListConfInfo;
import ebook.module.confList.tree.ListConfInfoOptions;
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
	protected ITreeItemInfo getItem(ResultSet rs) throws SQLException {

		ListConfInfo info = new ListConfInfo();
		info.setTitle(rs.getString(1));
		info.setId(rs.getInt(2));
		info.setParent(rs.getInt(3));
		info.setGroup(rs.getBoolean(4));
		info.options = DbOptions.load(ListConfInfoOptions.class,
				rs.getString(5));
		return info;
	}

	@Override
	public ITreeItemInfo getSelected() {
		int id = PreferenceSupplier.getInt(PreferenceSupplier.SELECTED_CONF);

		return get(id);
	}

}
