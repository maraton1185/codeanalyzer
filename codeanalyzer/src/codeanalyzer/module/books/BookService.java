package codeanalyzer.module.books;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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

import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

import codeanalyzer.core.App;
import codeanalyzer.core.models.DbOptions;
import codeanalyzer.module.books.tree.SectionImage;
import codeanalyzer.module.books.tree.SectionInfo;
import codeanalyzer.module.books.tree.SectionInfoOptions;
import codeanalyzer.module.books.tree.SectionSaveData;
import codeanalyzer.module.tree.ITreeItemInfo;
import codeanalyzer.module.tree.TreeService;
import codeanalyzer.utils.Events;
import codeanalyzer.utils.Events.EVENT_UPDATE_VIEW_DATA;
import codeanalyzer.utils.Strings;

public class BookService extends TreeService {

	final static String tableName = "SECTIONS";
	final static String updateEvent = Events.EVENT_UPDATE_CONTENT_VIEW;

	public BookService(BookConnection book) {

		super(tableName, updateEvent, book);
	}

	@Override
	protected ITreeItemInfo getItem(ResultSet rs) throws SQLException {

		SectionInfo info = new SectionInfo();
		info.setTitle(rs.getString(1));
		info.setId(rs.getInt(2));
		info.setParent(rs.getInt(3));
		info.setGroup(rs.getBoolean(4));
		info.options = DbOptions
				.load(SectionInfoOptions.class, rs.getString(5));
		return info;
	}

	@Override
	public void add(ITreeItemInfo item, ITreeItemInfo parent_item, boolean sub)
			throws InvocationTargetException {

		add(item, parent_item, sub);
	}

	// ************************************************************************************

	// public BookInfo getBookInfo() {
	//
	// BookInfo result = new BookInfo();
	//
	// try {
	// Connection con = db.getConnection();
	// String SQL = "Select TOP 1 T.DESCRIPTION, T.OPTIONS FROM INFO AS T";
	// PreparedStatement prep = con.prepareStatement(SQL);
	//
	// ResultSet rs = prep.executeQuery();
	// try {
	// if (rs.next()) {
	//
	// result.description = rs.getString(1);
	// result.options = DbOptions.load(BookInfoOptions.class,
	// rs.getString(2));
	// } else {
	// result.description = "";
	// result.options = new BookInfoOptions();
	// // result.options
	// }
	// } finally {
	// rs.close();
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// return result;
	//
	// }
	//
	// public void setBookInfo(BookInfo info) {
	//
	// try {
	//
	// Connection con = db.getConnection();
	// String SQL = "SELECT TOP 1 T.ID FROM INFO AS T;";
	// Statement stat = con.createStatement();
	// ResultSet rs = stat.executeQuery(SQL);
	//
	// try {
	//
	// PreparedStatement prep;
	// if (rs.next()) {
	//
	// SQL = "UPDATE INFO SET DESCRIPTION=?, OPTIONS=? WHERE ID=?;";
	// prep = con.prepareStatement(SQL,
	// Statement.CLOSE_CURRENT_RESULT);
	//
	// prep.setString(1, info.description);
	// prep.setString(2, DbOptions.save(info.options));
	// prep.setInt(3, rs.getInt(1));
	// int affectedRows = prep.executeUpdate();
	// if (affectedRows == 0)
	// throw new SQLException();
	//
	// } else {
	// SQL = "INSERT INTO INFO (DESCRIPTION, EDIT_MODE) VALUES (?,?);";
	// prep = con.prepareStatement(SQL,
	// Statement.CLOSE_CURRENT_RESULT);
	// prep.setString(1, info.description);
	// prep.setString(2, DbOptions.save(info.options));
	//
	// int affectedRows = prep.executeUpdate();
	// if (affectedRows == 0)
	// throw new SQLException();
	//
	// }
	//
	// } finally {
	// rs.close();
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }

	// public void setBook(WindowBookInfo book) throws IllegalAccessException {
	// this.book = book;
	// try {
	// book.openConnection();
	// Connection con = book.getConnection();
	//
	// SectionInfo sec = new SectionInfo();
	// sec.id = 1;
	//
	// String SQL =
	// "Select TOP 1 T1.TITLE, T.SELECTED_SECTION, T1.PARENT, T1.BLOCK, T1.OPTIONS FROM INFO AS T INNER JOIN SECTIONS AS T1 ON T.SELECTED_SECTION=T1.ID";
	// Statement stat = con.createStatement();
	// ResultSet rs = stat.executeQuery(SQL);
	// try {
	// if (rs.next()) {
	// sec = getSection(rs);
	// }
	// } finally {
	// rs.close();
	// }
	//
	// AppManager.br.post(Events.EVENT_UPDATE_CONTENT_VIEW,
	// new EVENT_UPDATE_VIEW_DATA(book, null, sec, true));
	// } catch (Exception e) {
	// e.printStackTrace();
	// throw new IllegalAccessException();
	// }
	// }

	public void saveSelectedSelection(SectionInfo section) {
		try {
			Connection con = db.getConnection();
			String SQL = "SELECT TOP 1 T.ID FROM INFO AS T;";
			Statement stat = con.createStatement();
			ResultSet rs = stat.executeQuery(SQL);
			try {

				if (rs.next()) {
					SQL = "UPDATE INFO SET SELECTED_SECTION=? WHERE ID=?;";
					PreparedStatement prep = con.prepareStatement(SQL,
							Statement.CLOSE_CURRENT_RESULT);

					prep.setInt(1, section.getId());
					prep.setInt(2, rs.getInt(1));
					int affectedRows = prep.executeUpdate();
					if (affectedRows == 0)
						throw new SQLException();
				} else {
					SQL = "INSERT INTO INFO (SELECTED_SECTION) VALUES (?);";
					PreparedStatement prep = con.prepareStatement(SQL,
							Statement.CLOSE_CURRENT_RESULT);

					prep.setInt(1, section.getId());
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

	// public void saveSection(SectionInfo section) {
	// try {
	// SectionInfo parent = getParent(section);
	//
	// Connection con = book.getConnection();
	// String SQL = "UPDATE SECTIONS SET TITLE=? WHERE ID=?;";
	// PreparedStatement prep = con.prepareStatement(SQL,
	// Statement.CLOSE_CURRENT_RESULT);
	//
	// prep.setString(1, section.title);
	// prep.setInt(2, section.id);
	// int affectedRows = prep.executeUpdate();
	// if (affectedRows == 0)
	// throw new SQLException();
	//
	// AppManager.br.post(Events.EVENT_UPDATE_CONTENT_VIEW,
	// new EVENT_UPDATE_VIEW_DATA(book, section, true));
	//
	// if (parent != null)
	// AppManager.br.post(Events.EVENT_UPDATE_CONTENT_VIEW,
	// new EVENT_UPDATE_VIEW_DATA(book, parent, true));
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }

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

			section.options = options;

			App.br.post(Events.EVENT_UPDATE_SECTION_BLOCK_VIEW,
					new EVENT_UPDATE_VIEW_DATA((BookConnection) db, section,
							null));

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
					App.br.post(Events.EVENT_UPDATE_CONTENT_VIEW,
							new EVENT_UPDATE_VIEW_DATA((BookConnection) db,
									parent, true));
			} finally {
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getText(SectionInfo section) {

		StringBuilder result = new StringBuilder();

		try {
			Connection con = db.getConnection();
			String SQL = "SELECT TEXT FROM S_TEXT WHERE SECTION=?";
			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setInt(1, section.getId());
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

	public List<SectionImage> getImages(Device display, SectionInfo section) {

		// List<BookSectionImage> result = new ArrayList<BookSectionImage>();
		//
		// result.add(new BookSectionImage(Utils.getImage("_start.png"),
		// "картинка 1", true));
		// result.add(new BookSectionImage(Utils.getImage("add.png"),
		// "картинка 2", false));
		//
		// return result;

		List<SectionImage> result = new ArrayList<SectionImage>();
		try {
			Connection con = db.getConnection();
			String SQL = "Select T.DATA, T.TITLE, T.SORT, T.EXPANDED, T.ID FROM S_IMAGES AS T WHERE T.SECTION=? ORDER BY T.SORT, T.ID";

			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setInt(1, section.getId());
			ResultSet rs = prep.executeQuery();

			try {
				while (rs.next()) {

					SectionImage sec = new SectionImage();
					// BookSection sec = new BookSection();
					InputStream is = rs.getBinaryStream(1);
					sec.title = rs.getString(2);
					sec.sort = rs.getInt(3);
					sec.expanded = rs.getBoolean(4);
					sec.id = rs.getInt(5);

					BufferedInputStream inputStreamReader = new BufferedInputStream(
							is);
					// new ByteArrayInputStream(imageByte));
					ImageData imageData = new ImageData(inputStreamReader);

					sec.image = new Image(display, imageData);

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

	public void add_image(SectionInfo section, IPath p) {

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

			SQL = "INSERT INTO S_IMAGES (TITLE, DATA, SORT, EXPANDED, SECTION) VALUES (?,?,?,?,?);";
			prep = con.prepareStatement(SQL, Statement.CLOSE_CURRENT_RESULT);

			String title = Strings.get("s.new_image.title");
			title = title + " " + sort;
			prep.setString(1, title);
			prep.setInt(3, sort);
			prep.setBoolean(4, true);
			prep.setInt(5, section.getId());

			File f = p.toFile();
			FileInputStream fis = new FileInputStream(f);
			prep.setBinaryStream(2, fis, (int) f.length());

			// ResultSet generatedKeys = null;
			// try {
			int affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			// generatedKeys = prep.getGeneratedKeys();
			// if (generatedKeys.next()) {
			// sec = new BookSection();
			// sec.title = title;
			// sec.id = generatedKeys.getInt(1);
			// sec.parent = section.id;
			// } else
			// throw new SQLException();
			// } finally {
			// generatedKeys.close();
			// }

			// int affectedRows = prep.executeUpdate();
			// if (affectedRows == 0)
			// throw new SQLException();

			App.br.post(Events.EVENT_UPDATE_SECTION_BLOCK_VIEW,
					new EVENT_UPDATE_VIEW_DATA((BookConnection) db, section,
							null));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public ITreeItemInfo getSelected() {
//		int id = PreferenceSupplier.getInt(PreferenceSupplier.SELECTED_USER);

		return null;
	}
}
