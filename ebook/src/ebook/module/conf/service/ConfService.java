package ebook.module.conf.service;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import ebook.core.pico;
import ebook.module.conf.ConfConnection;
import ebook.module.conf.model.AdditionalInfo;
import ebook.module.conf.model.BuildType;
import ebook.module.conf.model.ConfOptions;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ContextInfoOptions;
import ebook.module.conf.tree.ListInfo;
import ebook.module.conf.tree.ListInfoOptions;
import ebook.module.conf.xml.ContextXML;
import ebook.module.confLoad.interfaces.ICfServices;
import ebook.module.confLoad.model.DbState;
import ebook.module.confLoad.services.CfBuildService;
import ebook.module.db.BaseDbPathConnection;
import ebook.module.db.DbOptions;
import ebook.module.tree.item.ITreeItemInfo;
import ebook.module.tree.item.ITreeItemSelection;
import ebook.module.tree.item.ITreeItemXML;
import ebook.module.tree.service.IDownloadService;
import ebook.module.tree.service.ITreeService;
import ebook.module.tree.service.TreeService;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_VIEW_DATA;
import ebook.utils.Strings;
import ebook.utils.ZipHelper;

public class ConfService extends TreeService implements IDownloadService {

	final static String tableName = "CONTEXT";
	final static String updateEvent = Events.EVENT_UPDATE_CONF_VIEW;
	private ListInfo list;
	ICfServices cf = pico.get(ICfServices.class);

	public ConfService(ConfConnection con, ListInfo list) {
		super(tableName, updateEvent, con);
		this.list = list;

	}

	@Override
	protected String getItemString(String table) {
		String s = "$Table.TITLE, $Table.ID, $Table.PARENT, $Table.ISGROUP, $Table.OPTIONS, $Table.SORT, $Table.LIST ";
		s = s.replaceAll("\\$Table", "T");
		return s;
	}

	@Override
	protected Object getUpdateEventData(ITreeItemInfo parent, ITreeItemInfo item) {

		return new EVENT_UPDATE_VIEW_DATA(db, list, parent, item);
		// return new EVENT_UPDATE_VIEW_DATA(db, parent, item);
	}

	@Override
	protected String additionKeysString() {
		return ", LIST";
	}

	@Override
	protected String additionValuesString() {
		return ", ?";
	}

	@Override
	protected String getTextQUERY() {
		return "SELECT TEXT FROM PROCS_TEXT WHERE PROC=?";
	}

	@Override
	public List<ITreeItemInfo> getRoot() {
		List<ITreeItemInfo> result = super.getRoot();
		if (result.isEmpty())
			return createRoot();

		return result;
	}

	private List<ITreeItemInfo> createRoot() {

		try {
			Connection con = db.getConnection();

			String SQL = "INSERT INTO CONTEXT (TITLE, ISGROUP, LIST, OPTIONS) VALUES (?,?,?,?);";
			PreparedStatement prep = con.prepareStatement(SQL,
					Statement.CLOSE_CURRENT_RESULT);

			prep.setString(1, Strings.value("contextRoot"));
			prep.setBoolean(2, true);
			if (list == null)
				prep.setNull(3, java.sql.Types.INTEGER);
			else
				prep.setInt(3, list.getId());

			ContextInfoOptions opt = new ContextInfoOptions();
			opt.type = BuildType.root;
			prep.setString(4, DbOptions.save(opt));

			int affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			List<ITreeItemInfo> input = getRoot();
			if (input.isEmpty())
				return null;

			if (list != null) {
				list.getOptions().selectedContext = input.get(0).getId();

				((ConfConnection) db).lsrv().saveOptions(list);
			}

			return input;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected void setAdditions(PreparedStatement prep, ITreeItemInfo data)
			throws SQLException {

		if (list == null)
			prep.setNull(6, java.sql.Types.INTEGER);
		else
			prep.setInt(6, list.getId());
	}

	@Override
	protected void setAdditionRoot(PreparedStatement prep) throws SQLException {
		if (list != null)
			prep.setInt(1, list.getId());
	}

	@Override
	protected String additionRootWHEREString() {
		if (list != null)
			return "AND T.LIST=?";
		else
			return "AND T.LIST IS NULL";
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
		info.setConfId(db.getTreeItem().getId());
		info.setList(rs.getInt(7));
		return info;
	}

	@Override
	public ITreeItemInfo getSelected() {

		if (list != null) {
			ListInfoOptions opt = list.getOptions();
			if (opt == null)
				return get(ITreeService.rootId);
			return get(opt.selectedContext);
		}

		DbOptions _opt = getRootOptions(ConfOptions.class);
		if (_opt == null)
			return get(ITreeService.rootId);

		ConfOptions opt = (ConfOptions) _opt;
		return get(opt.selectedSection);
	}

	public void setState(DbState status) {

		ConfOptions opt = getRootOptions(ConfOptions.class);
		if (opt == null)
			opt = new ConfOptions();

		opt.status = status;
		// opt.link_status = DbState.notLoaded;
		opt.status_date = new Date();
		try {
			saveRootOptions(opt);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// public void setLinkState(DbState status) {
	//
	// ConfOptions opt = getRootOptions(ConfOptions.class);
	// if (opt == null)
	// opt = new ConfOptions();
	//
	// opt.link_status = status;
	// opt.link_status_date = new Date();
	// try {
	// saveRootOptions(opt);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }

	@Override
	public String download(IPath zipFolder, ITreeItemSelection selection,
			String zipName, boolean clear) throws InvocationTargetException {

		try {
			File temp = File.createTempFile("downloadConf", "");
			temp.delete();
			temp.mkdir();

			IPath t = new Path(temp.getAbsolutePath());

			CfBuildService build = cf.build(getConnection());
			ContextXML root = new ContextXML();

			Iterator<ITreeItemInfo> iterator = selection.iterator();
			while (iterator.hasNext()) {
				ITreeItemInfo item = iterator.next();
				ContextXML child = new ContextXML(item);

				List<String> path = new ArrayList<String>();
				child.proc = build.getId(this, (ContextInfo) item, null, path);
				child.path = path;

				root.children.add(child);
				writeXml(child, t);
			}

			// create JAXB context and instantiate marshaller
			JAXBContext context = JAXBContext.newInstance(ContextXML.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			// Write to System.out
			// m.marshal(root, System.out);

			// Write to File
			File f = t.append(ITreeItemXML.filename).addFileExtension("xml")
					.toFile();
			m.marshal(root, f);

			if (zipName == null || zipName.isEmpty())
				zipName = zipFolder
						.append(((BaseDbPathConnection) db).getWindowTitle()
								+ " (" + selection.getTitle() + ")")
						.addFileExtension("zip").toString();

			ZipHelper.zip(t.toString(), zipName);
			if (clear)
				new File(zipName).deleteOnExit();
			return zipName;
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvocationTargetException(e,
					Strings.msg("SaveToFile.error") + ":\n" + zipName + "");
		}
	}

	private void writeXml(ContextXML root, IPath p) throws SQLException,
			IllegalAccessException {

		if (root.proc != null)
			root.text = getText(root.proc);

		List<ITreeItemInfo> list = getChildren(root.id);

		ArrayList<ContextXML> children = new ArrayList<ContextXML>();

		List<String> path = null;
		if (root.path != null) {
			path = new ArrayList<String>(root.path);
			path.add(root.title);
		}
		for (ITreeItemInfo item : list) {

			ContextXML child = new ContextXML(item);

			if (path != null) {

				AdditionalInfo info = new AdditionalInfo();
				info.itemTitle = item.getTitle();
				child.proc = cf.build(getConnection())
						.getItemByPath(info, path);
				child.path = path;
			}
			writeXml(child, p);

			children.add(child);

		}

		root.children = children;
	}

	@Override
	public ITreeItemInfo upload(String path, ITreeItemInfo item, boolean clear,
			boolean relative) throws InvocationTargetException {

		try {
			File temp = File.createTempFile("uploadConf", "");
			temp.delete();
			temp.mkdir();
			if (clear)
				new File(path).deleteOnExit();
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

			stopUpdate();

			ContextInfo res = null;
			ITreeItemInfo parent = item.isGroup() ? item
					: get(item.getParent());
			for (ContextXML child : root.children) {

				ITreeItemInfo r_parent = null;
				if (relative && child.path != null)
					r_parent = makeUploadPath(child.path, parent);

				res = readXML(child, r_parent == null ? parent : r_parent, t);
			}

			startUpdate();
			if (res != null)
				selectLast(res.getParent());
			return res;

		} catch (Exception e) {
			e.printStackTrace();
			throw new InvocationTargetException(e,
					Strings.msg("loadFromFile.error"));
		}

	}

	private ContextInfo readXML(ContextXML element, ITreeItemInfo parent,
			IPath p) throws InvocationTargetException {

		ContextInfo root = (ContextInfo) ContextInfo.fromXML(element);
		add(root, parent, true);

		for (ContextXML child : element.children) {

			readXML(child, root, p);
		}

		return root;
	}

	public void setPassword(String value) {
		try {

			Connection con = db.getConnection();

			String SQL = "ALTER USER SA SET PASSWORD ?;";
			PreparedStatement prep1 = con.prepareStatement(SQL,
					Statement.CLOSE_CURRENT_RESULT);
			prep1.setString(1, value);
			prep1.execute();

			SQL = "SELECT TOP 1 T.ID FROM INFO AS T;";
			Statement stat = con.createStatement();
			ResultSet rs = stat.executeQuery(SQL);

			try {

				PreparedStatement prep;

				if (rs.next()) {

					SQL = "UPDATE INFO SET CANLOAD=? WHERE ID=?;";
					prep = con.prepareStatement(SQL,
							Statement.CLOSE_CURRENT_RESULT);

					prep.setBoolean(1, false);
					prep.setInt(2, rs.getInt(1));
					int affectedRows = prep.executeUpdate();
					if (affectedRows == 0)
						throw new SQLException();

				} else {
					SQL = "INSERT INTO INFO (CANLOAD) VALUES (?);";
					prep = con.prepareStatement(SQL,
							Statement.CLOSE_CURRENT_RESULT);
					prep.setBoolean(1, false);

					int affectedRows = prep.executeUpdate();
					if (affectedRows == 0)
						throw new SQLException();

				}

			} finally {
				rs.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
