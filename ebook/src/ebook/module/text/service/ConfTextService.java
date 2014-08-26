package ebook.module.text.service;

import java.util.ArrayList;
import java.util.List;

import ebook.module.conf.ConfService;
import ebook.module.conf.model.AdditionalInfo;
import ebook.module.conf.model.BuildInfo;
import ebook.module.conf.model.BuildType;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ContextInfoOptions;
import ebook.module.confLoad.model.ELevel;
import ebook.module.confLoad.services.CfBuildService;
import ebook.module.text.TextConnection;
import ebook.module.text.model.LineInfo;

public class ConfTextService extends TextService {

	public ConfTextService(TextConnection con) {
		super(con);

	}

	@Override
	public void saveItemText(String text) {
		try {

			CfBuildService build = cf.build(srv.getConnection());
			List<String> path = new ArrayList<String>();

			Integer id = build.getItemId((ConfService) srv, item, ELevel.proc,
					path);

			if (id != null)
				srv.saveText(id, text);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public String getItemText() {

		ContextInfoOptions opt = item.getOptions();
		if (opt.type == BuildType.module)

			return getModuleText();

		else

			return getText();

	}

	private String getModuleText() {
		Integer id = null;
		try {

			CfBuildService build = cf.build(srv.getConnection());
			List<String> path = new ArrayList<String>();

			id = build.getItemId((ConfService) srv, item, ELevel.module, path);

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

	private String getText() {

		Integer id = null;
		try {

			CfBuildService build = cf.build(srv.getConnection());
			List<String> path = new ArrayList<String>();

			id = build.getItemId((ConfService) srv, item, ELevel.proc, path);

		} catch (Exception e) {
			return e.getMessage();
		}
		return id == null ? null : srv.getText(id);
	}

	@Override
	public ContextInfo getItemByTitle(LineInfo selected) {

		Integer id = null;
		try {

			CfBuildService build = cf.build(srv.getConnection());
			List<String> path = new ArrayList<String>();

			id = build.getItemId((ConfService) srv, item, ELevel.module, path);

			if (id == null)
				return null;

			List<BuildInfo> proposals = new ArrayList<BuildInfo>();
			id = build.getProcs(selected.getTitle(), id, proposals, true);

			if (id == null)
				return null;

			ContextInfoOptions opt = new ContextInfoOptions();
			opt.type = BuildType.object;
			ContextInfo result = new ContextInfo(opt);
			result.setId(id);
			result.setTitle(selected.getTitle());
			result.setPath(item.getPath());
			// result.setParent(item.getId());

			return result;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void getItemPath() {

		if (item.hasPath())
			return;

		List<String> path = new ArrayList<String>();
		try {

			CfBuildService build = cf.build(srv.getConnection());
			ContextInfoOptions opt = item.getOptions();
			AdditionalInfo info = new AdditionalInfo();
			info.itemTitle = item.getTitle();
			String newTitle = "";
			if (build.getPathRoot(srv, item, info, opt, path) != null) {
				// for (String p : path) {
				// newTitle = newTitle + p + ".";
				// }
				item.setPath(path);
				// item.setTitle(newTitle + item.getTitle());
				// opt.type = BuildType.object;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

}
