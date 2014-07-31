package ebook.module.conf;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import ebook.core.models.DbOptions;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ContextInfoOptions;
import ebook.module.confLoad.model.DbState;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.ITreeService;
import ebook.module.tree.TreeService;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_VIEW_DATA;

public class ConfService extends TreeService {

	final static String tableName = "CONTEXT";
	final static String updateEvent = Events.EVENT_UPDATE_CONF_VIEW;

	public ConfService(ConfConnection con) {
		super(tableName, updateEvent, con);
	}

	@Override
	protected String getItemString(String table) {
		String s = "$Table.TITLE, $Table.ID, $Table.PARENT, $Table.ISGROUP, $Table.OPTIONS, $Table.SORT ";
		s = s.replaceAll("\\$Table", "T");
		return s;
	}

	@Override
	protected Object getUpdateEventData(ITreeItemInfo parent, ITreeItemInfo item) {

		return new EVENT_UPDATE_VIEW_DATA(db, parent, item);
	}

	@Override
	protected ITreeItemInfo getItem(ResultSet rs) throws SQLException {

		ContextInfo info = new ContextInfo();
		info.setTitle(rs.getString(1));
		info.setId(rs.getInt(2));
		info.setParent(rs.getInt(3));
		info.setGroup(rs.getBoolean(4));
		info.setOptions(DbOptions.load(ContextInfoOptions.class,
				rs.getString(5)));
		info.setSort(rs.getInt(6));

		return info;
	}

	@Override
	public ITreeItemInfo getSelected() {
		DbOptions _opt = getRootOptions();
		if (_opt == null)
			return get(ITreeService.rootId);

		ConfOptions opt = (ConfOptions) _opt;
		return get(opt.selectedSection);
	}

	public void setState(DbState status) {

		ConfOptions opt = (ConfOptions) getRootOptions();
		if (opt == null)
			opt = new ConfOptions();

		opt.status = status;
		opt.link_status = DbState.notLoaded;
		opt.status_date = new Date();
		try {
			saveRootOptions(opt);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setLinkState(DbState status) {

		ConfOptions opt = (ConfOptions) getRootOptions();
		if (opt == null)
			opt = new ConfOptions();

		opt.link_status = status;
		opt.link_status_date = new Date();
		try {
			saveRootOptions(opt);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
