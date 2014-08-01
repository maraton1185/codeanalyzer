package ebook.module.book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ebook.core.models.DbOptions;
import ebook.module.book.tree.SectionInfo;
import ebook.module.book.tree.SectionInfoOptions;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ContextInfoOptions;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.ITreeService;
import ebook.module.tree.TreeService;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_VIEW_DATA;

public class SectionContextService extends TreeService {

	final static String tableName = "CONTEXT";
	final static String updateEvent = Events.EVENT_UPDATE_SECTION_CONTEXT_VIEW;
	private SectionInfo section;

	public SectionContextService(BookConnection con, SectionInfo section) {
		super(tableName, updateEvent, con);

		this.section = section;
	}

	public void setSection(SectionInfo section) {
		this.section = section;

	}

	@Override
	protected String getItemString(String table) {
		String s = "$Table.TITLE, $Table.ID, $Table.PARENT, $Table.ISGROUP, $Table.OPTIONS, $Table.SORT, $Table.SECTION ";
		s = s.replaceAll("\\$Table", "T");
		return s;
	}

	@Override
	public List<ITreeItemInfo> getRoot() {
		List<ITreeItemInfo> result = new ArrayList<ITreeItemInfo>();

		try {
			Connection con = db.getConnection();
			String SQL = "SELECT "
					+ getItemString("T")
					+ "FROM "
					+ tableName
					+ " AS T WHERE T.PARENT IS NULL AND T.SECTION=? ORDER BY T.SORT, T.ID";
			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setInt(1, section.getId());

			ResultSet rs = prep.executeQuery();
			try {
				if (rs.next()) {

					result.add(getItem(rs));
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
	protected Object getUpdateEventData(ITreeItemInfo parent, ITreeItemInfo item) {

		return new EVENT_UPDATE_VIEW_DATA(db, section, parent, item);
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
		info.setSection(rs.getInt(7));

		return info;
	}

	@Override
	public ITreeItemInfo getSelected() {
		SectionInfoOptions opt = section.getOptions();
		if (opt == null)
			return get(ITreeService.rootId);

		return get(opt.selectedContext);
	}

}
