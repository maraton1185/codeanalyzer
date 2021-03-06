package ebook.module.book.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLConnection;
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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;

import ebook.core.App;
import ebook.module.book.BookConnection;
import ebook.module.book.model.BookOptions;
import ebook.module.book.tree.SectionImage;
import ebook.module.book.tree.SectionInfo;
import ebook.module.book.tree.SectionInfoOptions;
import ebook.module.book.xml.ImageXML;
import ebook.module.book.xml.SectionXML;
import ebook.module.bookList.tree.ListBookInfoOptions;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.xml.ContextXML;
import ebook.module.db.DbOptions;
import ebook.module.tree.item.ITreeItemInfo;
import ebook.module.tree.item.ITreeItemSelection;
import ebook.module.tree.item.ITreeItemXML;
import ebook.module.tree.service.IDownloadService;
import ebook.module.tree.service.ITreeService;
import ebook.module.tree.service.TreeService;
import ebook.utils.Const;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_VIEW_DATA;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;
import ebook.utils.ZipHelper;

public class BookService extends TreeService implements IDownloadService {

	final static String tableName = "SECTIONS";
	final static String updateEvent = Events.EVENT_UPDATE_CONTENT_VIEW;

	public BookService(BookConnection book) {

		super(tableName, updateEvent, book);
	}

	@Override
	protected String getItemString(String table) {
		String s = "$Table.TITLE, $Table.ID, $Table.PARENT, $Table.ISGROUP, $Table.OPTIONS, $Table.SORT, $Table.ROOT ";
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
		info.setRoot(rs.getBoolean(7));

		return info;
	}

	@Override
	protected Object getUpdateEventData(ITreeItemInfo parent, ITreeItemInfo item) {

		return new EVENT_UPDATE_VIEW_DATA(db, parent, item);
	}

	@Override
	protected String getTextQUERY() {
		return "SELECT TEXT FROM S_TEXT WHERE SECTION=?";
	}

	@Override
	protected String getInitText() {
		return PreferenceSupplier.get(PreferenceSupplier.INIT_SECTION_HTML);
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

	// public void saveBlock(SectionInfo section, SectionSaveData data) {
	// saveText(section, data.text);
	// SectionInfoOptions opt = section.getOptions();
	// opt.setBigImageCSS(data.options.getBigImageCSS());
	// saveOptions(section, opt);
	// }

	public void saveOptions(SectionInfo section, SectionInfoOptions options) {
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

			// App.br.post(Events.EVENT_UPDATE_SECTION_BLOCK_VIEW,
			// new EVENT_UPDATE_VIEW_DATA(db, section, null));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveText(SectionInfo section, String text) {
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

	public SectionImage getImage(int id) {

		List<SectionImage> result = new ArrayList<SectionImage>();
		try {
			Connection con = db.getConnection();
			String SQL = "Select T.DATA, T.TITLE, T.SORT, T.MIME, T.ID FROM S_IMAGES AS T WHERE T.ID=? ORDER BY T.SORT, T.ID";

			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setInt(1, id);
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
		return result.isEmpty() ? null : result.get(0);
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

	public int add_image(SectionInfo section, IPath p, String title) {

		int result = 0;
		// BookSection sec = null;
		FileInputStream fis = null;
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
			prep = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);

			if (title == null || title.isEmpty()) {
				title = PreferenceSupplier.get(PreferenceSupplier.IMAGE_TITLE);
				title = title + "" + sort;
			}
			prep.setString(1, title);
			prep.setInt(3, sort);
			prep.setString(4, p.getFileExtension());
			prep.setInt(5, section.getId());

			File f = p.toFile();
			fis = new FileInputStream(f);
			prep.setBinaryStream(2, fis, (int) f.length());

			ResultSet generatedKeys = null;
			try {
				int affectedRows = prep.executeUpdate();
				if (affectedRows == 0)
					throw new SQLException();

				generatedKeys = prep.getGeneratedKeys();
				if (generatedKeys.next()) {
					result = generatedKeys.getInt(1);
				} else
					throw new SQLException();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				generatedKeys.close();
			}

			// int affectedRows = prep.executeUpdate();
			// if (affectedRows == 0)
			// throw new SQLException();

			// App.br.post(Events.EVENT_UPDATE_SECTION_BLOCK_VIEW,
			// new EVENT_UPDATE_VIEW_DATA(db, section, null));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;
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

			// App.br.post(Events.EVENT_UPDATE_SECTION_BLOCK_VIEW,
			// new EVENT_UPDATE_VIEW_DATA(db, section, null));

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

			image.image = getImage(image.getId()).image;

			// App.br.post(Events.EVENT_UPDATE_SECTION_BLOCK_VIEW,
			// new EVENT_UPDATE_VIEW_DATA(db, section, null));

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

			image.setTitle(title);

			// App.br.post(Events.EVENT_UPDATE_SECTION_BLOCK_VIEW,
			// new EVENT_UPDATE_VIEW_DATA(db, section, null));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// public void move_image(SectionInfo section, SectionImage item,
	// boolean direction) {
	//
	// try {
	//
	// List<SectionImage> items = getImages(section.getId());
	//
	// int i = items.indexOf(item);
	//
	// if (direction == true) {
	// // up
	// i = i == 0 ? items.size() : i - 1;
	// } else {
	// // down
	// i = i == items.size() - 1 ? 0 : i + 1;
	// }
	//
	// items.remove(item);
	// items.add(i, item);
	//
	// updateImagesOrder(items);
	//
	// App.br.post(Events.EVENT_UPDATE_SECTION_BLOCK_VIEW,
	// new EVENT_UPDATE_VIEW_DATA(db, section, null));
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	public void updateImagesOrder(List<SectionImage> items) {
		try {
			Connection con = db.getConnection();
			int order = 1;
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
			e.printStackTrace();
		}

	}

	boolean clear;

	public String SaveToHtml(IPath zipFolder, ITreeItemSelection selection)
			throws IOException {

		// TODO save to html

		int section = 0;
		Iterator<ITreeItemInfo> iterator = selection.iterator();
		while (iterator.hasNext()) {
			ITreeItemInfo item = iterator.next();
			section = item.getId();
		}

		int book = ((BookConnection) db).getTreeItem().getId();
		String url = App.getJetty().host()
				.concat(App.getJetty().section(book, section));
		URL hp;
		URLConnection hpCon;
		// String url =
		// "http://localhost/MISApp/servlet/TestServlet?msg=MAHAANNA";
		StringBuffer contHTML = new StringBuffer(); // to hold the contents of
													// html file
		String readLine = "";
		hp = new URL(url);
		hpCon = hp.openConnection();
		// int len = hpCon.getContentLength();
		// if (len>0){
		BufferedReader br = new BufferedReader(new InputStreamReader(
				hpCon.getInputStream()));
		while ((readLine = br.readLine()) != null) {
			System.out.println(readLine);
			contHTML.append(readLine);
		}

		IPath p = zipFolder.append(
				((BookConnection) db).getWindowTitle() + " ("
						+ selection.getTitle() + ")").addFileExtension("html");
		String zipName = p.toString();

		BufferedWriter out = new BufferedWriter(new FileWriter(zipName));
		out.write(contHTML.toString());
		out.flush();
		out.close();

		return zipName;
	}

	@Override
	public String download(IPath zipFolder, ITreeItemSelection selection,
			String zipName, boolean clear) throws InvocationTargetException {

		try {
			this.clear = clear;
			File temp = File.createTempFile("download", "");
			temp.delete();
			temp.mkdir();

			IPath t = new Path(temp.getAbsolutePath());

			SectionXML root = new SectionXML();
			Iterator<ITreeItemInfo> iterator = selection.iterator();
			while (iterator.hasNext()) {
				ITreeItemInfo item = iterator.next();
				SectionXML child = new SectionXML(item);
				root.children.add(child);
				writeXml(child, t, item);
			}

			// create JAXB context and instantiate marshaller
			JAXBContext context = JAXBContext.newInstance(SectionXML.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			// Write to System.out
			// m.marshal(root, System.out);

			// Write to File
			File f = t.append(ITreeItemXML.filename).addFileExtension("xml")
					.toFile();
			m.marshal(root, f);

			if (zipName == null || zipName.isEmpty()) {
				IPath p = zipFolder.append(
						((BookConnection) db).getWindowTitle() + " ("
								+ selection.getTitle() + ")").addFileExtension(
						"zip");
				zipName = p.toString();
			}
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

	private void writeXml(SectionXML root, IPath p, ITreeItemInfo section) {

		ContextService srv = ((BookConnection) db)
				.ctxsrv((SectionInfo) section);

		List<ITreeItemInfo> c_root_item = srv.getRoot();
		if (!c_root_item.isEmpty()) {
			ContextXML c_root = new ContextXML(c_root_item.get(0), true);
			srv.writeXml(c_root);
			root.context = c_root;
		}

		List<SectionImage> _images = getImages(root.id);

		ArrayList<ImageXML> images = new ArrayList<ImageXML>();

		for (SectionImage image : _images) {
			ImageXML item = new ImageXML(image);
			images.add(item);

			ImageLoader saver = new ImageLoader();
			saver.data = new ImageData[] { image.image.getImageData() };

			File f = p.append(ImageXML.filename + image.getId())
					.addFileExtension(image.getMime()).toFile();

			saver.save(f.getAbsolutePath(), image.getFormat());

		}

		root.images = images;

		if (!root.group)
			root.text = getText(root.id);

		List<ITreeItemInfo> list = getChildren(root.id);

		ArrayList<SectionXML> children = new ArrayList<SectionXML>();

		for (ITreeItemInfo item : list) {

			SectionXML child = new SectionXML(item);
			writeXml(child, p, item);

			children.add(child);

		}

		root.children = children;

	}

	@Override
	public ITreeItemInfo upload(String path, ITreeItemInfo section,
			boolean clear, boolean relative) throws InvocationTargetException {

		try {
			File temp = File.createTempFile("upload", "");
			temp.delete();
			temp.mkdir();
			IPath t = new Path(temp.getAbsolutePath());

			if (clear)
				new File(path).deleteOnExit();

			ZipHelper.unzip(path, t.toString());

			JAXBContext context = JAXBContext.newInstance(SectionXML.class);

			InputStream inputStream = new FileInputStream(t
					.append(SectionXML.filename).addFileExtension("xml")
					.toString());
			Reader reader = new InputStreamReader(inputStream, "UTF-8");

			Unmarshaller um = context.createUnmarshaller();
			// um.setProperty(Unmarshaller.JAXB_ENCODING, "UTF-8");
			SectionXML root = (SectionXML) um.unmarshal(reader);

			stopUpdate();
			SectionInfo res = null;
			ITreeItemInfo parent = section.isGroup() ? section : get(section
					.getParent());
			for (SectionXML child : root.children) {
				res = readXML(child, parent, t);
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

	private SectionInfo readXML(SectionXML element, ITreeItemInfo parent,
			IPath p) throws InvocationTargetException {

		SectionInfo root = (SectionInfo) SectionInfo.fromXML(element);
		add(root, parent, true);

		for (ImageXML image : element.images) {

			IPath image_path = p.append(ImageXML.filename + image.id)
					.addFileExtension(image.mime);
			if (!image_path.toFile().exists())
				continue;

			image.new_id = add_image(root, image_path, image.title);

		}

		if (!root.isGroup()) {
			String text = element.text;
			text = text.replace("picture-link image", "_picture-link image");
			for (ImageXML image : element.images) {
				text = text.replace("_picture-link image" + image.id,
						"picture-link image" + image.new_id);
			}
			saveText(root, text);
		}

		if (element.context != null) {
			ContextService srv = ((BookConnection) db).ctxsrv(root);
			// srv.setSection(root);
			ContextInfo info = new ContextInfo();
			info.setRoot(true);
			info.setId(0);
			srv.readXML(element.context, info);
		}

		for (SectionXML child : element.children) {

			readXML(child, root, p);
		}

		return root;
	}

	public List<ITreeItemInfo> findSections(String search) {
		List<ITreeItemInfo> result = new ArrayList<ITreeItemInfo>();
		// Connection con = null;
		try {
			Connection con = db.getConnection();
			String SQL = "SELECT "
					+ getItemString("T")
					+ "FROM "
					+ tableName
					+ " AS T WHERE UPPER(T.TITLE) REGEXP UPPER(?) ORDER BY T.SORT, T.ID";

			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setString(1, search);
			ResultSet rs = prep.executeQuery();

			try {
				while (rs.next()) {

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
	public boolean check() {
		try {
			Connection con = db.getConnection();
			String SQL = "SELECT COUNT(T.ID) FROM " + tableName + " AS T ";

			PreparedStatement prep = con.prepareStatement(SQL);
			// prep.setBoolean(1, false);
			ResultSet rs = prep.executeQuery();

			try {
				if (rs.next()) {

					int i = rs.getInt(1);
					return i < Const.FREE_BOOK_ITEMS_COUNT;
				}
			} finally {
				rs.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

}
