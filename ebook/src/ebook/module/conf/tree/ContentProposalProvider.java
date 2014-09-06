package ebook.module.conf.tree;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.jface.fieldassist.ContentProposal;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;

import ebook.module.conf.model.AdditionalInfo;
import ebook.module.conf.model.BuildInfo;
import ebook.module.conf.model.BuildType;
import ebook.module.conf.service.ConfService;
import ebook.module.tree.item.ITreeItemInfo;

public class ContentProposalProvider implements IContentProposalProvider {

	private MWindow window;
	private ConfService tree;

	// public ICfServices cf = pico.get(ICfServices.class);

	public ContentProposalProvider(ConfService srv, MWindow window) {
		// proposals = new String[] { "Конфигурация", "ProposalTwo",
		// "ProposalThree" };
		this.window = window;
		this.tree = srv;
	}

	private boolean filterProposals = true;
	private IContentProposal[] contentProposals;
	private List<BuildInfo> proposals = new ArrayList<BuildInfo>();
	String filter;

	@Override
	public IContentProposal[] getProposals(String contents, int position) {

		filter = contents;
		// List<BuildInfo> list = new ArrayList<BuildInfo>();

		try {
			ContextInfo item = window.getContext().get(ContextInfo.class);
			ContextInfoOptions opt = new ContextInfoOptions();
			opt.type = item.getOptions().type;
			buildProposals(contents, item, opt);

		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// System.out.println(info.getTitle());

		if (filterProposals) {
			ArrayList<IContentProposal> list = new ArrayList<IContentProposal>();
			for (int i = 0; i < proposals.size(); i++) {
				if (proposals.get(i).title.length() >= filter.length()
						&& proposals.get(i).title.substring(0, filter.length())
								.equalsIgnoreCase(filter)) {
					list.add(new ContentProposal(proposals.get(i).title));
				}
			}
			return list.toArray(new IContentProposal[list.size()]);
		}
		if (contentProposals == null) {
			contentProposals = new IContentProposal[proposals.size()];
			for (int i = 0; i < proposals.size(); i++) {
				contentProposals[i] = new ContentProposal(
						proposals.get(i).title);
			}
		}
		return contentProposals;
	}

	private void buildProposals(String contents, ContextInfo item,
			ContextInfoOptions opt) throws SQLException, IllegalAccessException {
		List<String> path = new ArrayList<String>();
		AdditionalInfo info = new AdditionalInfo();
		info.itemTitle = contents;
		ITreeItemInfo root = tree.build().getPath(tree, item, info, opt, path);
		info.type = BuildType.object;
		if (root != null) {
			// get root without type between
			info.type = null;
			tree.build().buildWithPath(proposals, path, info);
		}
		if (info.type != null) {
			opt.type = info.type;
		}
		if (info.type == BuildType.object) {
			buildProposals(contents, item, opt);
		}
		filter = info.filter;

	}
}
