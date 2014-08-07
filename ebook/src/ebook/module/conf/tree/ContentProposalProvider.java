package ebook.module.conf.tree;

import java.util.ArrayList;

import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.jface.fieldassist.ContentProposal;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;

import ebook.module.conf.ConfConnection;

public class ContentProposalProvider implements IContentProposalProvider {

	private MWindow window;

	public ContentProposalProvider(ConfConnection con, MWindow window) {
		proposals = new String[] { "Конфигурация", "ProposalTwo",
				"ProposalThree" };
		this.window = window;
	}

	private boolean filterProposals = false;
	private IContentProposal[] contentProposals;
	private String[] proposals;

	@Override
	public IContentProposal[] getProposals(String contents, int position) {

		// ContextInfo info = window.getContext().get(ContextInfo.class);

		// System.out.println(info.getTitle());

		if (filterProposals) {
			ArrayList<IContentProposal> list = new ArrayList<IContentProposal>();
			for (int i = 0; i < proposals.length; i++) {
				if (proposals[i].length() >= contents.length()
						&& proposals[i].substring(0, contents.length())
								.equalsIgnoreCase(contents)) {
					list.add(new ContentProposal(proposals[i]));
				}
			}
			return list.toArray(new IContentProposal[list.size()]);
		}
		if (contentProposals == null) {
			contentProposals = new IContentProposal[proposals.length];
			for (int i = 0; i < proposals.length; i++) {
				contentProposals[i] = new ContentProposal(proposals[i]);
			}
		}
		return contentProposals;
	}
}
