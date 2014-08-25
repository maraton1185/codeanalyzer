package ebook.module.text;

import java.util.ArrayList;
import java.util.List;

import ebook.core.pico;
import ebook.module.conf.ConfService;
import ebook.module.conf.model.BuildInfo;
import ebook.module.conf.model.BuildType;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ContextInfoOptions;
import ebook.module.confLoad.interfaces.ICfServices;
import ebook.module.confLoad.model.ELevel;
import ebook.module.confLoad.services.CfBuildService;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.ITreeService;

public class TextService {

	private TextConnection con;
	ICfServices cf = pico.get(ICfServices.class);
	ITreeItemInfo item;
	ITreeService srv;

	public TextService(TextConnection con) {
		this.con = con;
		item = con.getItem();
		srv = con.getSrv();
	}

	public void saveItemText(String text) {
		if (con.isConf())
			saveConfText(text);
		else
			srv.saveText(item.getId(), text);

	}

	private void saveConfText(String text) {

		try {

			CfBuildService build = cf.build(srv.getConnection());
			List<String> path = new ArrayList<String>();

			Integer id = build.getItemId((ConfService) srv, (ContextInfo) item,
					ELevel.proc, path);

			srv.saveText(id, text);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getItemText() {

		ContextInfoOptions opt = (ContextInfoOptions) item.getOptions();
		if (opt.type == BuildType.module)
			if (con.isConf())
				return getConfModuleText();
			else
				return getBookModuleText();
		else

		if (con.isConf())
			return getConfText();
		else
			return srv.getText(item.getId());

	}

	private String getBookModuleText() {
		List<ITreeItemInfo> list = srv.getChildren(item.getId());

		StringBuilder result = new StringBuilder();

		for (ITreeItemInfo info : list) {

			String text = srv.getText(info.getId());

			result.append(text);
		}

		return result.toString();

	}

	private String getConfModuleText() {
		Integer id = null;
		try {

			CfBuildService build = cf.build(srv.getConnection());
			List<String> path = new ArrayList<String>();

			id = build.getItemId((ConfService) srv, (ContextInfo) item,
					ELevel.module, path);

			if (id == null)
				return null;

			List<BuildInfo> proposals = new ArrayList<BuildInfo>();
			build.getProcs(null, id, proposals);

			if (proposals.isEmpty())
				return null;

			StringBuilder result = new StringBuilder();

			for (BuildInfo buildInfo : proposals) {

				String text = srv.getText(buildInfo.id);

				result.append(text);
			}

			return result.toString();

		} catch (Exception e) {
			return e.getMessage();
		}
		// return null;
	}

	private String getConfText() {

		Integer id = null;
		try {

			CfBuildService build = cf.build(srv.getConnection());
			List<String> path = new ArrayList<String>();

			id = build.getItemId((ConfService) srv, (ContextInfo) item,
					ELevel.proc, path);

		} catch (Exception e) {
			return e.getMessage();
		}
		return id == null ? null : srv.getText(id);
	}

}
