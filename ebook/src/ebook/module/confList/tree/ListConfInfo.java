package ebook.module.confList.tree;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import ebook.core.App;
import ebook.core.models.DbOptions;
import ebook.module.confList.tree.ConfInfo.SQLConnection;
import ebook.module.confLoad.interfaces.ILoaderManager.operationType;
import ebook.module.confLoad.model.DbState;
import ebook.module.tree.TreeItemInfo;
import ebook.utils.PreferenceSupplier;

public class ListConfInfo extends TreeItemInfo {

	ConfInfo data;

	public ListConfInfo(ListConfInfoOptions options) {
		super(options);
		data = getOptions().info;
	}

	public ListConfInfo() {
		super(null);
	}

	@Override
	public String getSuffix() {
		return getDbName();
	}

	@Override
	public ListConfInfoOptions getOptions() {
		return (ListConfInfoOptions) super.getOptions();
	}

	@Override
	public void setOptions(DbOptions options) {
		super.setOptions(options);
		data = getOptions().info;
	}

	public String status() {

		String op = "";
		switch (data.type) {
		case fromDb:
			op = "Подключить " + data.db_full_path;
			break;
		case fromDirectory:
			op = "Загрузить из " + data.load_path;
			break;
		case update:
			op = "Обновить из " + data.load_path;
			break;
		case fromSQL:
			op = data.sql == null ? "-" : "Загрузить из " + data.sql.path;
			break;
		default:
			op = App.mng.lm().getOperationName(data.type);
			break;
		}
		return data.name + " : " + op;
	}

	public boolean isLoaded() {
		return getState() != DbState.notLoaded;
	}

	public void setType(operationType type) {
		data.type = type;
	}

	@Override
	public String getTitle() {
		if (isGroup())
			return super.getTitle();
		else
			return data.getName();
	}

	@Override
	public void setTitle(String title) {
		if (!isGroup() && data != null)
			data.name = title;
		else
			super.setTitle(title);

	}

	public operationType getType() {
		return data.type;
	}

	public void setAutoName(boolean value) {
		data.auto_name = value;
	}

	public boolean getAutoName() {
		return data.auto_name;
	}

	public void setLoadPath(String _path) {

		IPath path = new Path(_path);
		// IPath rootLoc = getBasePath();
		// if (rootLoc.isPrefixOf(path))
		// path = path.setDevice(null).removeFirstSegments(
		// rootLoc.segmentCount());
		data.load_path = path.toString();
	}

	public IPath getLoadPath() {
		return data.load_path == null ? new Path("") : getAbsolute(new Path(
				data.load_path));
	}

	private IPath getAbsolute(IPath path) {
		if (!path.isAbsolute())
			return getBasePath().append(path);
		return path;
	}

	public void setName(String name) {
		data.name = name;
	}

	public String getName() {
		return data.getName();
	}

	public void setDbFullPath(String _path) {

		IPath path = new Path(_path);
		// IPath rootLoc = getBasePath();
		// if (rootLoc.isPrefixOf(path))
		// path = path.setDevice(null).removeFirstSegments(
		// rootLoc.segmentCount());
		data.db_full_path = path.toString();

		// data.db_full_path = path;
		data.db_path = path.removeLastSegments(1).toString();
		data.db_name = path.removeFileExtension().removeFileExtension()
				.lastSegment();
	}

	public IPath getDbFullPath() {
		return data.db_full_path == null ? new Path("") : getAbsolute(new Path(
				data.db_full_path));
	}

	public String getDbPath() {
		return data.db_path == null ? "" : getAbsolute(new Path(data.db_path))
				.toString();
	}

	public String getDbName() {
		return data == null || data.db_name == null ? "" : data.db_name;
	}

	public void setState(DbState status) {
		getOptions().status = status;
		getOptions().link_status = DbState.notLoaded;
		getOptions().status_date = new Date();
		try {
			App.srv.cl().saveOptions(this);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public void setLinkState(DbState status) {
		getOptions().link_status = status;
		getOptions().link_status_date = new Date();
		try {
			App.srv.cl().saveOptions(this);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public DbState getState() {
		return getOptions().status;
	}

	public DbState getLinkState() {
		return getOptions().link_status;
	}

	public void setSQL(String path, String user, String password) {
		data.sql = data.new SQLConnection(path, user, password);
	}

	public SQLConnection getSQL() {
		return data.sql == null ? getDefaultSQL() : data.sql;
	}

	public SQLConnection getDefaultSQL() {
		return data.new SQLConnection("server\\base", "sa", "");
	}

	public Boolean getDeleteSourceFiles() {
		return data.deleteSourceFiles;
	}

	public void setDeleteSourceFiles(Boolean deleteSourceFiles) {
		data.deleteSourceFiles = deleteSourceFiles;
	}

	protected IPath getBasePath() {

		return new Path(
				PreferenceSupplier
						.get(PreferenceSupplier.DEFAULT_CONF_DIRECTORY));

	}

	public boolean getDoLog() {

		return data.doLog;
	}

	public void setDoLog(boolean selection) {
		data.doLog = selection;

	}
}
