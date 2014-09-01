package ebook.module.confList.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import ebook.core.models.ModelObject;
import ebook.module.confList.tree.ListConfInfo;
import ebook.module.confLoad.model.DbState;

public class ConfViewModel extends ModelObject {

	public ListConfInfo data;

	public String description = "";

	public ConfViewModel(ListConfInfo data) {
		super();
		this.data = data;
	}

	public ListConfInfo getData() {
		return data;
	}

	public String getdbPath() {
		return data.getDbPath();
	}

	public String getdbFileName() {
		return data.getDbName();
	}

	public String getloadFolder() {
		return data.getLoadPath().toString();
	}

	public String getTitle() {
		return data.getTitle();
	}

	public boolean isGroup() {
		return data.isGroup();
	}

	public boolean isItem() {
		return !data.isGroup();
	}

	public String getDescription() {
		return data.getOptions().description;
	}

	public void setDescription(String value) {

		fireIndexedPropertyChange("description", data.getOptions().description,
				data.getOptions().description = value);
	}

	public String getStatus() {
		DbState status = data.getOptions().status;
		Date status_date = data.getOptions().status_date;

		if (status == null)
			status = DbState.notLoaded;

		switch (status) {
		case notLoaded:
			return "Конфигурация не загружалась";
		case Loaded:
			return "Конфигурация загружена ("
					+ (status_date == null ? "-" : new SimpleDateFormat(
							"dd.MM.yyyy HH:mm").format(status_date)) + ")";

		default:
			return "-";
		}
	}

	// public String getLinkStatus() {
	// DbState status = data.getOptions().link_status;
	// Date status_date = data.getOptions().link_status_date;
	//
	// if (status == null)
	// status = DbState.notLoaded;
	//
	// switch (status) {
	// case notLoaded:
	// return "Таблица вызовов не загружалась";
	// case Loaded:
	// return "Таблица вызовов загружена ("
	// + (status_date == null ? "-" : new SimpleDateFormat(
	// "dd.MM.yyyy HH:mm").format(status_date)) + ")";
	//
	// default:
	// return "-";
	// }
	// }

	// public Image getStatusImage() {
	// DbState status = data.getOptions().status;
	// // Date status_date = data.getOptions().status_date;
	//
	// if (status == null)
	// status = DbState.notLoaded;
	//
	// switch (status) {
	// case Loaded:
	// return Utils.getImage("loaded.png");
	// default:
	// return Utils.getImage("not_loaded.png");
	// }
	// }
}
