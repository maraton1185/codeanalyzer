package ru.configviewer.core;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.osgi.service.prefs.Preferences;

import ru.configviewer.Application;
import ru.configviewer.utils.Const;

public class Service implements IService {

	@Override
	public String getText(String title) {
		String text = "";
		
		Connection con = null;
		InputStream inflInstream = null;
		ByteArrayOutputStream out = null;
		
		try {

			Preferences preferences = ConfigurationScope.INSTANCE.getNode(Application.PLUGIN_ID);
			String connection = preferences.get(Const.CONNECTION, "");
			
			con = getSQLConnection(connection);

			String SQL = "SELECT [BinaryData] FROM [dbo].[Config] where [filename]=?";
			PreparedStatement prep = con.prepareStatement(SQL);
			prep.setString(1, title);
			ResultSet rs = prep.executeQuery();
//			Statement stmt = con.createStatement();
//			ResultSet rs = stmt.executeQuery("SELECT TOP 1 [BinaryData] FROM [dbo].[Config]  where [filename]=?");
			
			while (rs.next()) {
				
				out = new ByteArrayOutputStream();
				
				inflInstream = new InflaterInputStream(
						rs.getBinaryStream(1),
						new Inflater(true));

				byte bytes[] = new byte[1024];
				while (true) {
					int length = inflInstream.read(bytes, 0, 1024);
					if (length == -1)
						break;
					
					out.write(bytes, 0, length);
				}

				text = out.toString(Const.DEFAULT_CHARACTER_ENCODING);
				
			}
			    
		} catch (Exception e) {
			
		} finally {
			try {
				con.close();
				inflInstream.close();
				out.close();
			} catch (Exception e) {
				
			}
		}
		
		return text;
	}
	
	@Override
	public List<LineInfo> getLines(String text) {

		List<LineInfo> list = new ArrayList<LineInfo>();
				
		Connection con = null;
		try {

			
			con = getSQLConnection(text);

			String SQL = "SELECT [filename] FROM [dbo].[Config]";
			PreparedStatement prep = con.prepareStatement(SQL);
			ResultSet rs = prep.executeQuery();
		
			while (rs.next()) {
				
				LineInfo item = new LineInfo();
				item.title = rs.getString(1);
				list.add(item);
				
			}
			    
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				
			}
		}
		
		return list;
	}

	private Connection getSQLConnection(String text) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {

		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		Connection con = DriverManager.getConnection(getConnectionString(text),
				"sa", "");

		return con;
	}
	
	private String getConnectionString(String path){
		String _p = path.replace('\\', '-');
		String[] _path = _p.split("-");
		if(_path.length!=2) return "";
		
		return "jdbc:sqlserver://" + _path[0] + 
				";databaseName=" + _path[1] +
				";selectMethod=cursor";
	}


	
}
