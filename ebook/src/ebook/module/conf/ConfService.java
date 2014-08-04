package ebook.module.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import ebook.core.models.BaseDbPathConnection;
import ebook.core.models.DbOptions;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ContextInfoOptions;
import ebook.module.conf.xml.ContextXML;
import ebook.module.confLoad.model.DbState;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.ITreeItemXML;
import ebook.module.tree.ITreeService;
import ebook.module.tree.TreeService;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_VIEW_DATA;
import ebook.utils.Strings;
import ebook.utils.ZipHelper;

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
		info.setConfId(db.getTreeItem().getId());
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

	public void download(IPath zipFolder, ContextInfo item, String zipName)
			throws InvocationTargetException {

		try {
			File temp = File.createTempFile("downloadConf", "");
			temp.delete();
			temp.mkdir();
			IPath t = new Path(temp.getAbsolutePath());

			ContextXML root = new ContextXML(item);

			writeXml(root, t);

			// create JAXB context and instantiate marshaller
			JAXBContext context = JAXBContext.newInstance(ContextXML.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			// Write to System.out
			// m.marshal(root, System.out);

			// Write to File
			m.marshal(root,
					t.append(ITreeItemXML.filename).addFileExtension("xml")
							.toFile());

			if (zipName == null || zipName.isEmpty())
				zipName = zipFolder
						.append(((BaseDbPathConnection) db).getWindowTitle()
								+ " (" + item.getTitle() + ")")
						.addFileExtension("zip").toString();

			ZipHelper.zip(t.toString(), zipName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvocationTargetException(e,
					Strings.get("error.saveToFile") + ":\n" + zipName + "");
		}
	}

	private void writeXml(ContextXML root, IPath p) {

		// List<SectionImage> _images = getImages(root.id);

		// ArrayList<ImageXML> images = new ArrayList<ImageXML>();
		//
		// for (SectionImage image : _images) {
		// ImageXML item = new ImageXML(image);
		// images.add(item);
		//
		// ImageLoader saver = new ImageLoader();
		// saver.data = new ImageData[] { image.image.getImageData() };
		//
		// saver.save(p.append(ImageXML.filename + image.getId())
		// .addFileExtension(image.getMime()).toString(),
		// image.getFormat());
		//
		// }
		//
		// root.images = images;

		List<ITreeItemInfo> list = getChildren(root.id);

		ArrayList<ContextXML> children = new ArrayList<ContextXML>();

		for (ITreeItemInfo item : list) {

			ContextXML child = new ContextXML(item);
			writeXml(child, p);

			children.add(child);

		}

		// if (!root.group)
		// root.text = getText(root.id);

		root.children = children;
	}

	public void upload(String path, ContextInfo item)
			throws InvocationTargetException {

		try {
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

			if (item.isGroup())
				readXML(root, item, t);
			else
				readXML(root, get(item.getParent()), t);

		} catch (Exception e) {
			e.printStackTrace();
			throw new InvocationTargetException(e,
					Strings.get("error.loadFromFile"));
		}

	}

	private void readXML(ContextXML element, ITreeItemInfo parent, IPath p)
			throws InvocationTargetException {

		ContextInfo root = (ContextInfo) ContextInfo.fromXML(element);
		add(root, parent, true);
		// if (!root.isGroup())
		// saveText(root, element.text);

		// for (ImageXML image : element.images) {
		//
		// IPath image_path = p.append(ImageXML.filename + image.id)
		// .addFileExtension(image.mime);
		// if (!image_path.toFile().exists())
		// continue;
		// add_image(root, image_path, image.title);
		//
		// }

		for (ContextXML child : element.children) {

			readXML(child, root, p);
		}

	}
}
