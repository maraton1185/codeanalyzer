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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import ebook.core.App;
import ebook.core.pico;
import ebook.module.book.tree.SectionInfo;
import ebook.module.book.tree.SectionInfoOptions;
import ebook.module.conf.model.AdditionalInfo;
import ebook.module.conf.model.BuildType;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ContextInfoOptions;
import ebook.module.conf.xml.ContextXML;
import ebook.module.confLoad.interfaces.ICfServices;
import ebook.module.confLoad.services.CfBuildService;
import ebook.module.db.BaseDbPathConnection;
import ebook.module.db.DbOptions;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.ITreeItemSelection;
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
	ICfServices cf = pico.get(ICfServices.class);

	public ContextService(BookConnection con, SectionInfo section) {
		super(tableName, updateEvent, con);

		this.section = section;
	}

	public void setSection(SectionInfo section) {
		this.section = section;
	}

	public SectionInfo getSection() {
		return section;
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
	protected String getTextQUERY() {
		return "SELECT TEXT FROM PROCS_TEXT WHERE PROC=?";
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

	@Override
	protected boolean canDeleteRoot() {

		return true;
	}

	@Override
	public void delete(ITreeItemSelection selection) {
		int parent = selection.getParent();

		Iterator<ITreeItemInfo> iterator = selection.iterator();
		while (iterator.hasNext()) {
			ITreeItemInfo item = iterator.next();
			if (item.isRoot()) {
				try {
					section.getOptions().resetContext();
					((BookConnection) db).srv().saveOptions(section);
					App.br.post(Events.EVENT_UPDATE_LABELS,
							new EVENT_UPDATE_VIEW_DATA(db, section));
					App.br.post(Events.EVENT_UPDATE_SECTION_INFO, null);
					delete(item);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			delete(item);
		}

		if (parent != 0)
			selectLast(parent);
	}

	String conf = "";

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
				ContextXML child = new ContextXML(item, false);

				ContextInfoOptions opt = (ContextInfoOptions) item.getOptions();
				List<String> path = new ArrayList<String>();
				AdditionalInfo info = new AdditionalInfo();
				info.itemTitle = item.getTitle();
				build.getPath(this, (ContextInfo) item, info, opt, path);

				if (path.size() > 1)
					path.remove(0);
				if (!path.isEmpty())
					child.path = path;

				root.children.add(child);
				writeXml(child);
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

	public void writeXml(ContextXML root) {

		root.text = getText(root.id);

		List<ITreeItemInfo> list = getChildren(root.id);

		ArrayList<ContextXML> children = new ArrayList<ContextXML>();

		for (ITreeItemInfo item : list) {

			ContextXML child = new ContextXML(item);
			writeXml(child);

			children.add(child);

		}

		root.children = children;
	}

	public void upload(String path, String conf, boolean clear)
			throws InvocationTargetException {

		try {

			this.conf = conf;
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
			ITreeItemInfo parent = getUploadRoot();
			for (ContextXML child : root.children) {
				ITreeItemInfo r_parent = null;
				if (child.path != null)
					r_parent = makeUploadPath(child.path, parent);

				res = readXML(child, r_parent == null ? parent : r_parent);
			}
			startUpdate();
			if (res != null)
				selectLast(res.getParent());

		} catch (Exception e) {
			e.printStackTrace();
			throw new InvocationTargetException(e,
					Strings.msg("loadFromFile.error"));
		}

	}

	public ContextInfo readXML(ContextXML element, ITreeItemInfo parent)
			throws InvocationTargetException {

		ContextInfo root = (ContextInfo) ContextInfo.fromXML(element);
		// root.getOptions().conf = conf;

		add(root, parent, true);

		if (element.text != null && !element.text.isEmpty()) {
			saveText(root.getId(), element.text);
			// root.getOptions().hasText = true;
			// saveOptions(root);
		}
		if (element.root) {
			List<ITreeItemInfo> input = getRoot();
			if (!input.isEmpty()) {
				section.getOptions().selectedContext = input.get(0).getId();
				ContextInfoOptions opt = (ContextInfoOptions) input.get(0)
						.getOptions();
				section.getOptions().setContextName(opt.conf);
				((BookConnection) db).srv().saveOptions(section);
				App.br.post(Events.EVENT_UPDATE_LABELS,
						new EVENT_UPDATE_VIEW_DATA(db, section));

			}
		}

		for (ContextXML child : element.children) {

			readXML(child, root);
		}

		return root;

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

			prep.setString(1, Strings.value("contextRoot"));
			prep.setBoolean(2, true);
			prep.setInt(3, section.getId());

			ContextInfoOptions opt = new ContextInfoOptions();
			opt.conf = conf;
			opt.type = BuildType.object;
			prep.setString(4, DbOptions.save(opt));

			int affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			List<ITreeItemInfo> input = getRoot();
			if (input.isEmpty())
				return null;

			section.getOptions().selectedContext = input.get(0).getId();
			section.getOptions().setContextName(conf);
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

}
