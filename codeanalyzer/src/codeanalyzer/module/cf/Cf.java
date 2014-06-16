package codeanalyzer.module.cf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import codeanalyzer.core.pico;
import codeanalyzer.module.cf.CfInfo.SQLConnection;
import codeanalyzer.module.cf.interfaces.ICf;
import codeanalyzer.module.cf.interfaces.ICfManager;
import codeanalyzer.module.cf.interfaces.ILoaderManager.operationType;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.PreferenceSupplier;
import codeanalyzer.utils.Utils;

/**
 * ÍÎ‡ÒÒ ÔÓÎÛ˜ÂÌËˇ ‰‡ÌÌ˚ı ËÁ ·‡Á˚
 * 
 * @author Enikeev M.A.
 * 
 */
@SuppressWarnings("restriction")
public class Cf implements ICf {

	// »Õ»÷»¿À»«¿÷»ﬂ ******************************************************

	// DbService service = new DbService();

	private CfInfo data;

	private DbState status = DbState.notLoaded;
	private DbState link_status = DbState.notLoaded;

	String store_key = "";

	@Override
	public void initDbPath() {
		data.db_path = getPath().append(Const.DEFAULT_DB_NAME).toString()
				.concat(Const.DEFAULT_DB_EXTENSION);
		// setType(operationType.fromDb);
		save();
	}

	// —“¿“”— ******************************************************

	@Override
	public String status() {

		String op = "";
		switch (data.type) {
		case fromDb:
			op = "œÓ‰ÍÎ˛˜ËÚ¸ " + data.db_path;
			break;
		case fromDirectory:
			op = "«‡„ÛÁËÚ¸ ËÁ " + data.path;
			break;
		case update:
			op = "Œ·ÌÓ‚ËÚ¸ ËÁ " + data.path;
			break;
		case fromSQL:
			op = data.sql == null ? "-" : "«‡„ÛÁËÚ¸ ËÁ " + data.sql.path;
			break;
		default:
			op = pico.get(ICfManager.class).getOperationName(data.type);
			break;
		}
		return data.name + " : " + op;
	}

	@Override
	public String getId() {
		return store_key;
	}

	@Override
	public boolean isLoaded() {
		return getState() != DbState.notLoaded;
	}

	// —≈–»¿À»«¿÷»ﬂ ******************************************************

	@Override
	public void load(String key) {
		store_key = key;
		String s = PreferenceSupplier.get(store_key);
		if (s.isEmpty()) {
			this.data = new CfInfo();
			this.data.name = "ÕÓ‚‡ˇ ÍÓÌÙË„Û‡ˆËˇ";
		} else {
			ObjectInputStream ois = null;
			try {
				BASE64Decoder decoder = new BASE64Decoder();
				byte[] data = decoder.decodeBuffer(s);
				ois = new ObjectInputStream(new ByteArrayInputStream(data));

				this.data = (CfInfo) ois.readObject();

			} catch (Exception e) {
				this.data = new CfInfo();
				this.data.name = "ÕÓ‚‡ˇ ÍÓÌÙË„Û‡ˆËˇ";
			} finally {
				try {
					if (ois != null)
						ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void save() {
		String value = "";
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(data);
			oos.close();
			BASE64Encoder encoder = new BASE64Encoder();
			value = encoder.encodeBuffer(baos.toByteArray());
		} catch (Exception e) {
			value = "";
		}

		PreferenceSupplier.set(store_key, value);
		PreferenceSupplier.save();

	}

	// ”—“¿ÕŒ¬ ¿/œŒÀ”◊≈Õ»≈ —¬Œ…—“¬
	// ******************************************************

	// @Override
	// public String getId() {
	// return ID;
	// }

	@Override
	public void setType(operationType type) {
		data.type = type;
	}

	@Override
	public operationType getType() {
		return data.type;
	}

	@Override
	public void setAutoName(boolean value) {
		data.auto_name = value;
	}

	@Override
	public boolean getAutoName() {
		return data.auto_name;
	}

	@Override
	public void setPath(String path) {
		data.path = path;
	}

	@Override
	public IPath getPath() {
		return Utils.getAbsolute(new Path(data.path));
	}

	@Override
	public void setName(String name) {
		data.name = name;
	}

	@Override
	public String getName() {
		return data.getName();
	}

	@Override
	public void setDbPath(String path) {
		data.db_path = path;
	}

	@Override
	public IPath getDbPath() {
		return Utils.getAbsolute(new Path(data.db_path));
	}

	@Override
	public void setState(DbState status) {
		this.status = status;
		link_status = DbState.notLoaded;
	}

	@Override
	public void setLinkState(DbState status) {
		this.link_status = status;
	}

	@Override
	public DbState getState() {
		return status;
	}

	@Override
	public DbState getLinkState() {
		return link_status;
	}

	@Override
	public void setSQL(String path, String user, String password) {
		data.sql = data.new SQLConnection(path, user, password);
	}

	@Override
	public SQLConnection getSQL() {
		return data.sql == null ? getDefaultSQL() : data.sql;
	}

	@Override
	public SQLConnection getDefaultSQL() {
		return data.new SQLConnection("server\\base", "sa", "");
	}

	@Override
	public Boolean getDeleteSourceFiles() {
		return data.deleteSourceFiles;
	}

	@Override
	public void setDeleteSourceFiles(Boolean deleteSourceFiles) {
		data.deleteSourceFiles = deleteSourceFiles;
	}

	// –¿¡Œ“¿ — ƒ¿ÕÕ€Ã»  ŒÕ‘»√”–¿÷»»
	// ******************************************************
	//
	// @Override
	// public Connection getSQLConnection() throws InstantiationException,
	// IllegalAccessException, ClassNotFoundException, SQLException {
	//
	// Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	//
	// Connection con = DriverManager.getConnection(
	// data.sql.getConnectionString(),
	// data.sql.user,
	// data.sql.password);
	//
	// return con;
	// }
	//
	@Override
	public Connection getConnection(boolean exist)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {

		Class.forName("org.h2.Driver").newInstance();
		String ifExist = exist ? ";IFEXISTS=TRUE" : "";

		IPath path = exist ? getDbPath().removeFileExtension()
				.removeFileExtension() : getPath()
				.append(Const.DEFAULT_DB_NAME);

		return DriverManager.getConnection("jdbc:h2:" + path.toString()
				+ ifExist, "sa", "");
	}
	//
	// @Override
	// public List<BuildInfo> search(searchType type, String text,
	// IProgressMonitor monitor) {
	//
	// List<BuildInfo> result = new ArrayList<BuildInfo>();
	//
	// Connection con = null;
	// try {
	// con = getConnection(true);
	//
	// switch (type) {
	// case text:
	// service.getTextSearchList(result, con, text, monitor);
	// break;
	// case meta:
	// service.getObjectSearchList(result, con, text);
	// break;
	// case proc:
	// service.getProcsSearchList(result, con, text);
	// break;
	// default:
	// break;
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// con.close();
	// } catch (Exception e) {
	// }
	// }
	// return result;
	//
	// }
	//
	// //BUILD ******************************************************
	//
	// @Override
	// public boolean buildObject(List<BuildInfo> result, String topicText) {
	//
	// boolean isTips = false;
	//
	// Connection con = null;
	// try {
	// con = getConnection(true);
	//
	// service.getObjectList(result, con, topicText, true);
	// if(result.isEmpty())
	// {
	// service.getObjectList(result, con, topicText, false);
	// isTips = true;
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// con.close();
	// } catch (Exception e) {
	// }
	// }
	// return isTips;
	// }
	//
	// @Override
	// public boolean buildObject(List<BuildInfo> result, String parentText,
	// String topicText) {
	//
	// boolean isTips = false;
	//
	// Connection con = null;
	// try {
	// con = getConnection(true);
	//
	// service.getConcreteObjectList(result, con, parentText, topicText, true);
	// if(result.isEmpty())
	// {
	// service.getConcreteObjectList(result, con, parentText, topicText, false);
	// isTips = true;
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// con.close();
	// } catch (Exception e) {
	// }
	// }
	// return isTips;
	// }
	//
	// @Override
	// public void buildObject(List<BuildInfo> list) {
	//
	// Connection con = null;
	// try {
	// con = getConnection(true);
	//
	// service.getRootList(list, con);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// con.close();
	// } catch (Exception e) {
	// }
	// }
	// }
	//
	// @Override
	// public void buildModule(List<BuildInfo> list, BuildInfo data) {
	// Connection con = null;
	// try {
	// con = getConnection(true);
	//
	// service.getModuleList(list, con, data);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// con.close();
	// } catch (Exception e) {
	// }
	// }
	//
	// }
	//
	// @Override
	// public void buildParamsList(List<String> paramsList, BuildInfo data) {
	// Connection con = null;
	// try {
	// con = getConnection(true);
	//
	// service.getParamsList(paramsList, con, data);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// con.close();
	// } catch (Exception e) {
	// }
	// }
	// }
	//
	// //GET TEXT ******************************************************
	//
	// @Override
	// public String getProcText(BuildInfo data) {
	// Connection con = null;
	// try {
	// con = getConnection(true);
	//
	// return service.getProcText(con, data);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// con.close();
	// } catch (Exception e) {
	// }
	// }
	// return "";
	// }
	//
	// @Override
	// public String getModuleText(BuildInfo data) {
	// Connection con = null;
	// try {
	// con = getConnection(true);
	//
	// return service.getModuleText(con, data);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// con.close();
	// } catch (Exception e) {
	// }
	// }
	// return "";
	// }
	//
	// //HIERARCHY ******************************************************
	//
	// @Override
	// public void getCalled(List<BuildInfo> list, BuildInfo data) {
	// Connection con = null;
	// try {
	// con = getConnection(true);
	//
	// service.getCalled(list, con, data);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// con.close();
	// } catch (Exception e) {
	// }
	// }
	// }
	//
	// @Override
	// public void getCalls(List<BuildInfo> list, BuildInfo data, boolean
	// callsInObject, IProgressMonitor monitor) {
	//
	// Connection con = null;
	// try {
	// con = getConnection(true);
	//
	// service.getCalls(list, con, data, callsInObject, monitor);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// con.close();
	// } catch (Exception e) {
	// }
	// }
	//
	// }
	//
	// @Override
	// public void getProcsInLine(List<BuildInfo> list, String line, BuildInfo
	// context) {
	// Connection con = null;
	// try {
	// con = getConnection(true);
	//
	// service.getProcsInLine(list, con, line, context);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// con.close();
	// } catch (Exception e) {
	// }
	// }
	//
	// }
	//
	// //COMPARE ******************************************************
	//
	// @Override
	// public void compareModules(IDb nonActive, CompareResults compareResults,
	// BuildInfo data, IProgressMonitor monitor) {
	//
	// Connection con1 = null;
	// Connection con2 = null;
	// try {
	// con1 = getConnection(true);
	// con2 = nonActive.getConnection(true);
	//
	// service.compareModules(compareResults, con1, con2, data, monitor);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// con1.close();
	// con2.close();
	// } catch (Exception e) {
	// }
	// }
	//
	// }
	//
	// @Override
	// public void compareObjects(IDb nonActive, CompareResults compareResults,
	// String parentText, String topicText, IProgressMonitor monitor) {
	//
	// Connection con1 = null;
	// Connection con2 = null;
	// try {
	// con1 = getConnection(true);
	// con2 = nonActive.getConnection(true);
	//
	// service.compareObjects(compareResults, con1, con2, parentText, topicText,
	// monitor);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// con1.close();
	// con2.close();
	// } catch (Exception e) {
	// }
	// }
	//
	// }
	//
	// @Override
	// public void compareObjects(IDb nonActive, CompareResults compareResults,
	// String titleText, IProgressMonitor monitor) {
	//
	// Connection con1 = null;
	// Connection con2 = null;
	// try {
	// con1 = getConnection(true);
	// con2 = nonActive.getConnection(true);
	//
	// service.compareObjects(compareResults, con1, con2, titleText, monitor);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// con1.close();
	// con2.close();
	// } catch (Exception e) {
	// }
	// }
	//
	// }
	//
	// @Override
	// public void compareObjects(IDb nonActive, CompareResults compareResults,
	// IProgressMonitor monitor) {
	//
	// Connection con1 = null;
	// Connection con2 = null;
	// try {
	// con1 = getConnection(true);
	// con2 = nonActive.getConnection(true);
	//
	// service.compareObjects(compareResults, con1, con2, monitor);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// con1.close();
	// con2.close();
	// } catch (Exception e) {
	// }
	// }
	//
	// }
	//
}
//
//
//
//
//
//
//
// // @Override
// // public boolean checkVersion(String version) {
// // Connection con = null;
// // try {
// // con = getConnection(true);
// //
// // String db_version = service.getVersion(con);
// //
// // return db_version.equalsIgnoreCase(version);
// //
// // } catch (Exception e) {
// // e.printStackTrace();
// // } finally {
// // try {
// // con.close();
// // } catch (Exception e) {
// // }
// // }
// //
// // return false;
// // }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
// }
