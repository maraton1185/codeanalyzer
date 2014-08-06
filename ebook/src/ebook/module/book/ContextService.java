package ebook.module.book;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import ebook.core.App;
import ebook.core.models.DbOptions;
import ebook.module.book.tree.SectionInfo;
import ebook.module.book.tree.SectionInfoOptions;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ContextInfoOptions;
import ebook.module.conf.xml.ContextXML;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.ITreeItemXML;
import ebook.module.tree.ITreeService;
import ebook.module.tree.TreeService;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_VIEW_DATA;
import ebook.utils.Strings;
import ebook.utils.ZipHelper;

public class ContextService extends TreeService {

	final static String tableName = "CONTEXT";
	final static String updateEvent = Events.EVENT_UPDATE_CONTEXT_VIEW;
	private SectionInfo section;

	public ContextService(BookConnection con, SectionInfo section) {
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
	protected String additionKeysString() {
		return ", SECTION";
	}

	@Override
	protected String additionValuesString() {
		return ", ?";
	}

	@Override
	protected void setAdditions(PreparedStatement prep, ITreeItemInfo data)
			throws SQLException {

		prep.setInt(6, section.getId());
	}

	@Override
	protected void setAdditionRoot(PreparedStatement prep) throws SQLException {
		prep.setInt(1, section.getId());
	}

	@Override
	protected String additionRootWHEREString() {
		return "AND T.SECTION=?";
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

	String conf = "";

	public void upload(String path, String conf)
			throws InvocationTargetException {

		try {

			this.conf = conf;
			File temp = File.createTempFile("uploadConf", "");
			temp.delete();
			temp.mkdir();
			IPath t = new Path(temp.getAbsolutePath());

			ZipHelper.unzip(path, t.toString());

			JAXBContext context = JAXBContext.newInstance(ContextXML.class);

			InputStream inputStream = new FileInputStream(t
					.append(ITreeItemXML.filename).addFileExtension("xml")
					.toString());
			Reader reader = new InputStreamReader(inputStream, "UTF-8");

			Unmarshaller um = context.createUnmarshaller();
			// um.setProperty(Unmarshaller.JAXB_ENCODING, "UTF-8");
			ContextXML root = (ContextXML) um.unmarshal(reader);

			readXML(root, getUploadRoot(), t);

		} catch (Exception e) {
			e.printStackTrace();
			throw new InvocationTargetException(e,
					Strings.get("error.loadFromFile"));
		}

	}

	private ITreeItemInfo getUploadRoot() {
		List<ITreeItemInfo> input = getRoot();
		if (input.isEmpty())
			return createRoot();

		return input.get(0);

	}

	private ITreeItemInfo createRoot() {

		try {
			Connection con = db.getConnection();

			String SQL = "INSERT INTO CONTEXT (TITLE, ISGROUP, SECTION, OPTIONS) VALUES (?,?,?,?);";
			PreparedStatement prep = con.prepareStatement(SQL,
					Statement.CLOSE_CURRENT_RESULT);

			prep.setString(1, Strings.get("initContextTitle"));
			prep.setBoolean(2, true);
			prep.setInt(3, section.getId());

			ContextInfoOptions opt = new ContextInfoOptions();
			opt.conf = conf;
			prep.setString(4, DbOptions.save(opt));

			int affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			List<ITreeItemInfo> input = getRoot();
			if (input.isEmpty())
				return null;

			section.getOptions().selectedContext = input.get(0).getId();
			section.getOptions().hasContext = true;
			((BookConnection) db).srv().saveOptions(section);
			App.br.post(Events.EVENT_UPDATE_LABELS, new EVENT_UPDATE_VIEW_DATA(
					db, section));

			App.br.post(Events.EVENT_UPDATE_SECTION_INFO, null);

			return input.get(0);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private void readXML(ContextXML element, ITreeItemInfo parent, IPath p)
			throws InvocationTargetException {

		ContextInfo root = (ContextInfo) ContextInfo.fromXML(element);
		root.getOptions().conf = conf;

		add(root, parent, true);

		for (ContextXML child : element.children) {

			readXML(child, root, p);
		}

	}

}
