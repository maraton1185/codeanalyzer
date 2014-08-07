package ebook.module.book;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;

import ebook.core.App;
import ebook.core.models.DbOptions;
import ebook.module.book.tree.SectionImage;
import ebook.module.book.tree.SectionInfo;
import ebook.module.book.tree.SectionInfoOptions;
import ebook.module.book.tree.SectionSaveData;
import ebook.module.book.xml.ImageXML;
import ebook.module.book.xml.SectionXML;
import ebook.module.bookList.tree.ListBookInfoOptions;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.ITreeItemXML;
import ebook.module.tree.ITreeService;
import ebook.module.tree.TreeService;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_VIEW_DATA;
import ebook.utils.Strings;
import ebook.utils.ZipHelper;

public class BookService extends TreeService {

	final static String tableName = "SECTIONS";
	final static String updateEvent = Events.EVENT_UPDATE_CONTENT_VIEW;

	public BookService(BookConnection book) {

		super(tableName, updateEvent, book);
	}

	@Override
	protected String getItemString(String table) {
		String s = "$Table.TITLE, $Table.ID, $Table.PARENT, $Table.ISGROUP, $Table.OPTIONS, $Table.SORT ";
		s = s.replaceAll("\\$Table", "T");
		return s;
	}

	@Override
	protected ITreeItemInfo getItem(ResultSet rs) throws SQLException {

		SectionInfo info = new SectionInfo();
		info.setTitle(rs.getString(1));
		info.setId(rs.getInt(2));
		info.setParent(rs.getInt(3));
		info.setGroup(rs.getBoolean(4));
		info.setOptions(DbOptions.load(SectionInfoOptions.class,
				rs.getString(5)));
		info.setBookId(db.getTreeItem().getId());

		ListBookInfoOptions opt = (ListBookInfoOptions) ((BookConnection) db)
				.getTreeItem().getOptions();
		if (opt == null)
			info.setACL();
		else if (opt.ACL)
			info.setACL();

		info.setSort(rs.getInt(6));

		return info;
	}

	@Override
	protected Object getUpdateEventData(ITreeItemInfo parent, ITreeItemInfo item) {

		return new EVENT_UPDATE_VIEW_DATA(db, parent, item);
	}

	@Override
	public ITreeItemInfo getSelected() {
		DbOptions _opt = getRootOptions(BookOptions.class);
		if (_opt == null)
			return get(ITreeService.rootId);

		BookOptions opt = (BookOptions) _opt;
		return get(opt.selectedSection);
	}

	// ************************************************************************************

	// ************************************************************************************

	public void saveBlock(SectionInfo section, SectionSaveData data) {
		saveText(section, data.text);
		saveOptions(section, data.options);
	}

	private void saveOptions(SectionInfo section, SectionInfoOptions options) {
		try {
			Connection con = db.getConnection();
			String SQL = "UPDATE SECTIONS SET OPTIONS=? WHERE ID=?;";
			PreparedStatement prep = con.prepareStatement(SQL,
					Statement.CLOSE_CURRENT_RESULT);

			prep.setString(1, SectionInfoOptions.save(options));
			prep.setInt(2, section.getId());
			int affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			section.setOptions(options);

			App.br.post(Events.EVENT_UPDATE_SECTION_BLOCK_VIEW,
					new EVENT_UPDATE_VIEW_DATA(db, section, null));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void saveText(SectionInfo section, String text) {
		try {
			SectionInfo parent = (SectionInfo) get(section.getParent());

			Connection con = db.getConnection();
			String SQL = "SELECT TOP 1 ID FROM S_TEXT WHERE SECTION=?;";
			PreparedStatement prep = con.prepareStatement(SQL,
					Statement.CLOSE_CURRENT_RESULT);
			prep.setInt(1, section.getId());

			ResultSet rs = prep.executeQuery();
			try {

				if (rs.next()) {
					SQL = "UPDATE S_TEXT SET TEXT=? WHERE ID=?;";
					PreparedStatement prep1 = con.prepareStatement(SQL,
							Statement.CLOSE_CURRENT_RESULT);

					prep1.setCharacterStream(1, new BufferedReader(
							new StringReader(text.toString())));
					prep1.setInt(2, rs.getInt(1));
					int affectedRows = prep1.executeUpdate();
					if (affectedRows == 0)
						throw new SQLException();
				} else {
					SQL = "INSERT INTO S_TEXT (TEXT, SECTION) VALUES (?,?);";
					PreparedStatement prep2 = con.prepareStatement(SQL,
							Statement.CLOSE_CURRENT_RESULT);

					prep2.setCharacterStream(1, new BufferedReader(
							new StringReader(text.toString())));
					prep2.setInt(2, section.getId());
					int affectedRows = prep2.executeUpdate();
					if (affectedRows == 0)
						throw new SQLException();
				}

				if (parent != null)
					App.br.post(Events.EVENT_UPDATE_SECTION_VIEW,
							new EVENT_UPDATE_VIEW_DATA(db, parent));
			} finally {
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getText(int section) {

		StringBuilder result = new StringBuilder();

		try {
			Connection con = db.getConnection();
			String SQL = "SELECT TEXT FROM S_TEXT WHERE SECTION=?";
			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setInt(1, section);
			ResultSet rs = prep.executeQuery();
			BufferedReader bufferedReader = null;

			try {
				if (rs.next()) {

					Reader in = rs.getCharacterStream(1);
					bufferedReader = new BufferedReader(in);
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						result.append(line + "\n");
					}
				} else
					result.append(Strings.get("s.newblocktext"));
			} finally {
				rs.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result.toString();
	}

	public List<SectionImage> getImages(int section) {

		List<SectionImage> result = new ArrayList<SectionImage>();
		try {
			Connection con = db.getConnection();
			String SQL = "Select T.DATA, T.TITLE, T.SORT, T.MIME, T.ID FROM S_IMAGES AS T WHERE T.SECTION=? ORDER BY T.SORT, T.ID";

			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setInt(1, section);
			ResultSet rs = prep.executeQuery();

			try {
				while (rs.next()) {

					SectionImage sec = new SectionImage();
					// BookSection sec = new BookSection();
					InputStream is = rs.getBinaryStream(1);
					sec.setTitle(rs.getString(2));
					sec.sort = rs.getInt(3);
					sec.setMime(rs.getString(4));
					sec.id = rs.getInt(5);
					sec.book = ((BookConnection) db).getTreeItem().getId();

					BufferedInputStream inputStreamReader = new BufferedInputStream(
							is);
					// new ByteArrayInputStream(imageByte));
					ImageData imageData = new ImageData(inputStreamReader);

					sec.image = new Image(Display.getCurrent(), imageData);

					result.add(sec);
				}
			} finally {
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public BufferedInputStream getImage(String image_id) {
		Integer id;
		try {
			id = Integer.parseInt(image_id);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		BufferedInputStream inputStreamReader = null;

		try {
			Connection con = db.getConnection();
			String SQL = "Select T.DATA FROM S_IMAGES AS T WHERE T.ID=?";

			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setInt(1, id);
			ResultSet rs = prep.executeQuery();

			try {
				if (rs.next()) {

					InputStream is = rs.getBinaryStream(1);
					if (is == null)
						return null;
					inputStreamReader = new BufferedInputStream(is);
				}
			} finally {
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inputStreamReader;
	}

	public void add_image(SectionInfo section, IPath p, String title) {

		// BookSection sec = null;
		try {
			Connection con = db.getConnection();
			String SQL;
			PreparedStatement prep;

			SQL = "Select Top 1 T.SORT FROM S_IMAGES AS T WHERE T.SECTION=? ORDER BY T.SORT DESC";
			prep = con.prepareStatement(SQL);

			prep.setInt(1, section.getId());
			ResultSet rs = prep.executeQuery();

			int sort = 0;
			try {
				if (rs.next())
					sort = rs.getInt(1);
				sort++;
			} finally {
				rs.close();
			}

			SQL = "INSERT INTO S_IMAGES (TITLE, DATA, SORT, MIME, SECTION) VALUES (?,?,?,?,?);";
			prep = con.prepareStatement(SQL, Statement.CLOSE_CURRENT_RESULT);

			if (title == null || title.isEmpty()) {
				title = Strings.get("s.new_image.title");
				title = title + " " + sort;
			}
			prep.setString(1, title);
			prep.setInt(3, sort);
			prep.setString(4, p.getFileExtension());
			prep.setInt(5, section.getId());

			File f = p.toFile();
			FileInputStream fis = new FileInputStream(f);
			prep.setBinaryStream(2, fis, (int) f.length());

			int affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			App.br.post(Events.EVENT_UPDATE_SECTION_BLOCK_VIEW,
					new EVENT_UPDATE_VIEW_DATA(db, section, null));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete_image(SectionInfo section, SectionImage item) {

		try {
			Connection con = db.getConnection();

			String SQL = "DELETE FROM S_IMAGES WHERE ID=?;";
			PreparedStatement prep;

			prep = con.prepareStatement(SQL);

			prep.setInt(1, item.getId());

			int affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			App.br.post(Events.EVENT_UPDATE_SECTION_BLOCK_VIEW,
					new EVENT_UPDATE_VIEW_DATA(db, section, null));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void edit_image(SectionInfo section, SectionImage image, IPath p) {
		try {
			Connection con = db.getConnection();
			String SQL;
			PreparedStatement prep;

			SQL = "UPDATE S_IMAGES SET DATA=?, MIME=?  WHERE ID=?; ";
			prep = con.prepareStatement(SQL, Statement.CLOSE_CURRENT_RESULT);

			prep.setInt(3, image.getId());

			File f = p.toFile();
			FileInputStream fis = new FileInputStream(f);
			prep.setBinaryStream(1, fis, (int) f.length());
			prep.setString(2, p.getFileExtension());

			// image.setMime(p.getFileExtension());

			int affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			App.br.post(Events.EVENT_UPDATE_SECTION_BLOCK_VIEW,
					new EVENT_UPDATE_VIEW_DATA(db, section, null));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void save_image_title(SectionInfo section, SectionImage image,
			String title) {
		try {
			Connection con = db.getConnection();
			String SQL;
			PreparedStatement prep;

			SQL = "UPDATE S_IMAGES SET TITLE=? WHERE ID=?; ";
			prep = con.prepareStatement(SQL, Statement.CLOSE_CURRENT_RESULT);

			prep.setInt(2, image.getId());

			prep.setString(1, title);

			int affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			App.br.post(Events.EVENT_UPDATE_SECTION_BLOCK_VIEW,
					new EVENT_UPDATE_VIEW_DATA(db, section, null));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void move_image(SectionInfo section, SectionImage item,
			boolean direction) {

		try {

			List<SectionImage> items = getImages(section.getId());

			int i = items.indexOf(item);

			if (direction == true) {
				// up
				i = i == 0 ? items.size() : i - 1;
			} else {
				// down
				i = i == items.size() - 1 ? 0 : i + 1;
			}

			items.remove(item);
			items.add(i, item);

			updateImagesOrder(items);

			App.br.post(Events.EVENT_UPDATE_SECTION_BLOCK_VIEW,
					new EVENT_UPDATE_VIEW_DATA(db, section, null));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateImagesOrder(List<SectionImage> items) throws Exception {
		try {
			Connection con = db.getConnection();
			int order = 0;
			for (SectionImage item : items) {

				String SQL = "UPDATE S_IMAGES SET SORT=? WHERE ID=?;";
				PreparedStatement prep = con.prepareStatement(SQL,
						Statement.CLOSE_CURRENT_RESULT);

				prep.setInt(1, order);
				prep.setInt(2, item.getId());
				int affectedRows = prep.executeUpdate();
				if (affectedRows == 0)
					throw new SQLException();

				order++;
			}

		} catch (Exception e) {
			throw new Exception();
		}

	}

	private void writeXml(SectionXML root, IPath p) {

		List<SectionImage> _images = getImages(root.id);

		ArrayList<ImageXML> images = new ArrayList<ImageXML>();

		for (SectionImage image : _images) {
			ImageXML item = new ImageXML(image);
			images.add(item);

			ImageLoader saver = new ImageLoader();
			saver.data = new ImageData[] { image.image.getImageData() };

			saver.save(p.append(ImageXML.filename + image.getId())
					.addFileExtension(image.getMime()).toString(),
					image.getFormat());

		}

		root.images = images;

		List<ITreeItemInfo> list = getChildren(root.id);

		ArrayList<SectionXML> children = new ArrayList<SectionXML>();

		for (ITreeItemInfo item : list) {

			SectionXML child = new SectionXML(item);
			writeXml(child, p);

			children.add(child);

		}

		if (!root.group)
			root.text = getText(root.id);

		root.children = children;
	}

	@Override
	public void download(IPath zipFolder, ITreeItemInfo section, String zipName)
			throws InvocationTargetException {

		try {
			File temp = File.createTempFile("download", "");
			temp.delete();
			temp.mkdir();
			IPath t = new Path(temp.getAbsolutePath());

			SectionXML root = new SectionXML(section);

			writeXml(root, t);

			// create JAXB context and instantiate marshaller
			JAXBContext context = JAXBContext.newInstance(SectionXML.class);
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
						.append(((BookConnection) db).getWindowTitle() + " ("
								+ section.getTitle() + ")")
						.addFileExtension("zip").toString();

			ZipHelper.zip(t.toString(), zipName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvocationTargetException(e,
					Strings.get("error.saveToFile") + ":\n" + zipName + "");
		}
	}

	private void readXML(SectionXML element, ITreeItemInfo parent, IPath p)
			throws InvocationTargetException {

		SectionInfo root = (SectionInfo) SectionInfo.fromXML(element);
		add(root, parent, true);
		if (!root.isGroup())
			saveText(root, element.text);

		for (ImageXML image : element.images) {

			IPath image_path = p.append(ImageXML.filename + image.id)
					.addFileExtension(image.mime);
			if (!image_path.toFile().exists())
				continue;
			add_image(root, image_path, image.title);

		}

		for (SectionXML child : element.children) {

			readXML(child, root, p);
		}

	}

	@Override
	public void upload(String path, ITreeItemInfo section)
			throws InvocationTargetException {

		try {
			File temp = File.createTempFile("upload", "");
			temp.delete();
			temp.mkdir();
			IPath t = new Path(temp.getAbsolutePath());

			ZipHelper.unzip(path, t.toString());

			JAXBContext context = JAXBContext.newInstance(SectionXML.class);

			InputStream inputStream = new FileInputStream(t
					.append(SectionXML.filename).addFileExtension("xml")
					.toString());
			Reader reader = new InputStreamReader(inputStream, "UTF-8");

			Unmarshaller um = context.createUnmarshaller();
			// um.setProperty(Unmarshaller.JAXB_ENCODING, "UTF-8");
			SectionXML root = (SectionXML) um.unmarshal(reader);

			if (section.isGroup())
				readXML(root, section, t);
			else
				readXML(root, get(section.getParent()), t);

		} catch (Exception e) {
			e.printStackTrace();
			throw new InvocationTargetException(e,
					Strings.get("error.loadFromFile"));
		}

	}
}
