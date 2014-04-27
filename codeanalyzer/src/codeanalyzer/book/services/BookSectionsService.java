package codeanalyzer.book.services;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import codeanalyzer.book.BookInfo;
import codeanalyzer.book.BookSection;
import codeanalyzer.core.AppManager;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Const.EVENT_UPDATE_CONTENT_VIEW_DATA;
import codeanalyzer.utils.Strings;

public class BookSectionsService {

	private BookInfo book = new BookInfo();

	private BookSection getSection(ResultSet rs) throws SQLException {

		BookSection sec = new BookSection();
		sec.title = rs.getString(1);
		sec.id = rs.getInt(2);
		sec.parent = rs.getInt(3);
		sec.block = rs.getBoolean(4);
		return sec;
	}

	public void setBook(BookInfo book) throws IllegalAccessException {
		this.book = book;
		try {
			book.openConnection();
			Connection con = book.getConnection();

			BookSection sec = new BookSection();
			sec.id = 1;

			String SQL = "Select TOP 1 T1.TITLE, T.SELECTED_SECTION, T1.PARENT, T1.BLOCK FROM INFO AS T INNER JOIN SECTIONS AS T1 ON T.SELECTED_SECTION=T1.ID";
			Statement stat = con.createStatement();
			ResultSet rs = stat.executeQuery(SQL);
			try {
				if (rs.next()) {
					sec = getSection(rs);
				}
			} finally {
				rs.close();
			}

			AppManager.br.post(Const.EVENT_UPDATE_CONTENT_VIEW,
					new EVENT_UPDATE_CONTENT_VIEW_DATA(book, null, sec, true));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalAccessException();
		}
	}

	public void add(BookSection section) {
		BookSection parent = getParent(section);
		if (parent == null) {
			add_sub(section);
			return;
		}
		add_sub(parent);

	}

	public void add_sub(BookSection section, boolean block) {
		if (section.block) {
			BookSection parent = getParent(section);
			if (parent != null) {
				section = parent;
			}
		}
		BookSection sec = null;
		try {
			Connection con = book.getConnection();
			String SQL;
			PreparedStatement prep;

			SQL = "Select Top 1 T.SORT FROM SECTIONS AS T WHERE T.PARENT=? ORDER BY T.SORT DESC";
			prep = con.prepareStatement(SQL);

			prep.setInt(1, section.id);
			ResultSet rs = prep.executeQuery();

			int sort = 0;
			try {
				if (rs.next())
					sort = rs.getInt(1);
				sort++;
			} finally {
				rs.close();
			}

			SQL = "INSERT INTO SECTIONS (TITLE, PARENT, BLOCK, SORT) VALUES (?,?,?,?);";
			prep = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);

			String title = block ? Strings.get("s.newblock.title") : Strings
					.get("s.newsection.title");
			title = title + " " + sort;
			prep.setString(1, title);
			if (section.id == 0)
				prep.setNull(2, java.sql.Types.INTEGER);
			else
				prep.setInt(2, section.id);
			prep.setBoolean(3, block);
			prep.setInt(4, sort);

			ResultSet generatedKeys = null;
			try {
				int affectedRows = prep.executeUpdate();
				if (affectedRows == 0)
					throw new SQLException();

				generatedKeys = prep.getGeneratedKeys();
				if (generatedKeys.next()) {
					sec = new BookSection();
					sec.title = title;
					sec.id = generatedKeys.getInt(1);
					sec.parent = section.id;
				} else
					throw new SQLException();
			} finally {
				generatedKeys.close();
			}

			// int affectedRows = prep.executeUpdate();
			// if (affectedRows == 0)
			// throw new SQLException();

		} catch (Exception e) {
			e.printStackTrace();
		}

		AppManager.br.post(Const.EVENT_UPDATE_CONTENT_VIEW,
				new EVENT_UPDATE_CONTENT_VIEW_DATA(book, section, sec));
	}

	public void add_sub(BookSection section) {

		add_sub(section, false);

	}

	public void delete(BookSection section) {
		BookSection parent = getParent(section);
		if (parent == null)
			return;

		try {
			Connection con = book.getConnection();

			String SQL = "DELETE FROM SECTIONS WHERE ID=?;";
			PreparedStatement prep;

			prep = con.prepareStatement(SQL);

			prep.setInt(1, section.id);
			// if (section.id == 0)
			// prep.setNull(2, java.sql.Types.INTEGER);
			// else
			// prep.setInt(2, section.id);

			int affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

		} catch (Exception e) {
			e.printStackTrace();
		}

		BookSection selected = getLast(parent);
		if (selected == null)
			selected = parent;

		// AppManager.br.post(Const.EVENT_UPDATE_CONTENT_VIEW, book);
		AppManager.br.post(Const.EVENT_UPDATE_CONTENT_VIEW,
				new EVENT_UPDATE_CONTENT_VIEW_DATA(book, parent, selected));
		// AppManager.br.post(Const.EVENT_DELETE_SECTION,
		// new EVENT_DELETE_SECTION_DATA(book, section));
	}

	public List<BookSection> getRoot() {
		List<BookSection> result = new ArrayList<BookSection>();
		// BookSection result;
		try {
			Connection con = book.getConnection();
			String SQL = "Select T.TITLE, T.ID, T.PARENT, T.BLOCK FROM SECTIONS AS T WHERE T.PARENT IS NULL ORDER BY T.SORT, T.ID";
			PreparedStatement prep = con.prepareStatement(SQL);
			// prep.setNull(1, java.sql.Types.INTEGER);
			ResultSet rs = prep.executeQuery();
			try {
				if (rs.next()) {

					BookSection sec = getSection(rs);
					// BookSection sec = new BookSection();
					// sec.title = rs.getString(1);
					// sec.id = rs.getInt(2);
					// sec.root = true;
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

	public List<BookSection> getChildren(BookSection parent) {

		List<BookSection> result = new ArrayList<BookSection>();
		try {
			Connection con = book.getConnection();
			String SQL = "Select T.TITLE, T.ID, T.PARENT, T.BLOCK FROM SECTIONS AS T WHERE T.PARENT=? ORDER BY T.SORT, T.ID";

			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setInt(1, parent.id);
			ResultSet rs = prep.executeQuery();

			try {
				while (rs.next()) {

					BookSection sec = getSection(rs);
					// BookSection sec = new BookSection();
					// sec.title = rs.getString(1);
					// sec.id = rs.getInt(2);
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

	public BookSection getParent(BookSection section) {
		try {
			Connection con = book.getConnection();
			String SQL = "Select T1.TITLE, T1.ID, T1.PARENT, T1.BLOCK FROM SECTIONS AS T "
					+ "INNER JOIN SECTIONS AS T1 ON T.PARENT = T1.ID "
					+ "WHERE T.ID=?";

			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setInt(1, section.id);
			ResultSet rs = prep.executeQuery();

			try {
				if (rs.next()) {

					BookSection sec = getSection(rs);
					// BookSection sec = new BookSection();
					// sec.title = rs.getString(1);
					// sec.id = rs.getInt(2);
					return sec;
				}
			} finally {
				rs.close();
			}

			return null;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private BookSection getLast(BookSection parent) {
		try {
			Connection con = book.getConnection();
			String SQL = "Select TOP 1 T.TITLE, T.ID, T.PARENT, T.BLOCK FROM SECTIONS AS T WHERE T.PARENT=? ORDER BY T.SORT DESC, T.ID DESC";
			// String SQL = "Select T1.TITLE, T1.ID FROM SECTIONS AS T "
			// + "INNER JOIN SECTIONS AS T1 ON T.PARENT = T1.ID "
			// + "WHERE T.ID=?";

			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setInt(1, parent.id);
			ResultSet rs = prep.executeQuery();

			try {
				if (rs.next()) {

					BookSection sec = getSection(rs);
					// BookSection sec = new BookSection();
					// sec.title = rs.getString(1);
					// sec.id = rs.getInt(2);
					return sec;
				}
			} finally {
				rs.close();
			}

			return null;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean hasChildren(BookSection section) {
		try {
			Connection con = book.getConnection();
			String SQL = "Select COUNT(ID) from SECTIONS WHERE PARENT=?";
			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setInt(1, section.id);
			ResultSet rs = prep.executeQuery();

			try {
				if (rs.next())
					return rs.getInt(1) != 0;
				else
					return false;
			} finally {
				rs.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void saveSelectedSelection(BookSection section) {
		try {
			Connection con = book.getConnection();
			String SQL = "SELECT TOP 1 T.ID FROM INFO AS T;";
			Statement stat = con.createStatement();
			ResultSet rs = stat.executeQuery(SQL);
			try {

				if (rs.next()) {
					SQL = "UPDATE INFO SET SELECTED_SECTION=? WHERE ID=?;";
					PreparedStatement prep = con.prepareStatement(SQL,
							Statement.CLOSE_CURRENT_RESULT);

					prep.setInt(1, section.id);
					prep.setInt(2, rs.getInt(1));
					int affectedRows = prep.executeUpdate();
					if (affectedRows == 0)
						throw new SQLException();
				} else {
					SQL = "INSERT INTO INFO (SELECTED_SECTION) VALUES (?);";
					PreparedStatement prep = con.prepareStatement(SQL,
							Statement.CLOSE_CURRENT_RESULT);

					prep.setInt(1, section.id);
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

	public void saveSection(BookSection section) {
		try {
			// BookSection parent = getParent(section);

			Connection con = book.getConnection();
			String SQL = "UPDATE SECTIONS SET TITLE=? WHERE ID=?;";
			PreparedStatement prep = con.prepareStatement(SQL,
					Statement.CLOSE_CURRENT_RESULT);

			prep.setString(1, section.title);
			prep.setInt(2, section.id);
			int affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			// if (parent != null)
			AppManager.br.post(Const.EVENT_UPDATE_CONTENT_VIEW,
					new EVENT_UPDATE_CONTENT_VIEW_DATA(book, section, true));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Boolean setBefore(BookSection section, BookSection target) {

		BookSection parent = getParent(target);

		if (parent == null)
			return false;

		if (section.parent != parent.id)
			setParent(section, parent);

		List<BookSection> items = getChildren(parent);

		items.remove(section);
		int i = items.indexOf(target);
		if (i < 0)
			return false;
		items.add(i, section);
		// set(t, section);

		updateOrder(items);

		AppManager.br.post(Const.EVENT_UPDATE_CONTENT_VIEW,
				new EVENT_UPDATE_CONTENT_VIEW_DATA(book, parent, section));

		return true;
	}

	public Boolean setAfter(BookSection section, BookSection target) {
		BookSection parent = getParent(target);

		if (parent == null)
			return false;

		if (section.parent != parent.id)
			setParent(section, parent);

		List<BookSection> items = getChildren(parent);

		items.remove(section);
		int i = items.indexOf(target);
		items.add(i + 1, section);

		updateOrder(items);

		AppManager.br.post(Const.EVENT_UPDATE_CONTENT_VIEW,
				new EVENT_UPDATE_CONTENT_VIEW_DATA(book, parent, section));

		return true;
	}

	public Boolean setParent(BookSection section, BookSection target) {

		BookSection parent = getParent(section);

		try {
			Connection con = book.getConnection();

			String SQL = "UPDATE SECTIONS SET PARENT=? WHERE ID=?;";
			PreparedStatement prep = con.prepareStatement(SQL,
					Statement.CLOSE_CURRENT_RESULT);

			prep.setInt(1, target.id);
			prep.setInt(2, section.id);
			int affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			List<BookSection> items = getChildren(target);
			items.remove(section);
			items.add(section);

			updateOrder(items);

			AppManager.br.post(Const.EVENT_UPDATE_CONTENT_VIEW,
					new EVENT_UPDATE_CONTENT_VIEW_DATA(book, target, section));
			if (parent != null)
				AppManager.br.post(Const.EVENT_UPDATE_CONTENT_VIEW,
						new EVENT_UPDATE_CONTENT_VIEW_DATA(book, parent, true));

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	private void updateOrder(List<BookSection> items) {
		try {
			Connection con = book.getConnection();
			int order = 0;
			for (BookSection item : items) {

				String SQL = "UPDATE SECTIONS SET SORT=? WHERE ID=?;";
				PreparedStatement prep = con.prepareStatement(SQL,
						Statement.CLOSE_CURRENT_RESULT);

				prep.setInt(1, order);
				prep.setInt(2, item.id);
				int affectedRows = prep.executeUpdate();
				if (affectedRows == 0)
					throw new SQLException();

				order++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setText(BookSection section, String text) {
		try {
			Connection con = book.getConnection();
			String SQL = "SELECT TOP 1 T.ID FROM S_TEXT AS T;";
			Statement stat = con.createStatement();
			ResultSet rs = stat.executeQuery(SQL);
			try {

				if (rs.next()) {
					SQL = "UPDATE S_TEXT SET TEXT=? WHERE ID=?;";
					PreparedStatement prep = con.prepareStatement(SQL,
							Statement.CLOSE_CURRENT_RESULT);

					prep.setCharacterStream(1, new BufferedReader(
							new StringReader(text.toString())));
					prep.setInt(1, rs.getInt(1));
					int affectedRows = prep.executeUpdate();
					if (affectedRows == 0)
						throw new SQLException();
				} else {
					SQL = "INSERT INTO S_TEXT (TEXT) VALUES (?);";
					PreparedStatement prep = con.prepareStatement(SQL,
							Statement.CLOSE_CURRENT_RESULT);

					prep.setCharacterStream(1, new BufferedReader(
							new StringReader(text.toString())));
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

	public String getText(BookSection section) {

		StringBuilder result = new StringBuilder();

		try {
			Connection con = book.getConnection();
			String SQL = "SELECT TEXT FROM S_TEXT WHERE SECTION=?";
			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setInt(1, section.id);
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

}
