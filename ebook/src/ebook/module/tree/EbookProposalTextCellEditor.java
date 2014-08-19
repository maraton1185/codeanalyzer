package ebook.module.tree;

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.bindings.keys.ParseException;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;

import ebook.utils.Utils;

public class EbookProposalTextCellEditor extends TextCellEditor {

	private IContentProposalProvider contentProposalProvider;

	public EbookProposalTextCellEditor(
			IContentProposalProvider contentProposalProvider, Tree tree) {
		this.contentProposalProvider = contentProposalProvider;
		create(tree);
		// super(tree);

	}

	@Override
	protected Control createControl(Composite parent) {
		Control ctrl = super.createControl(parent);

		if (contentProposalProvider == null)
			return ctrl;

		final ControlDecoration deco = new ControlDecoration(ctrl, SWT.TOP
				| SWT.LEFT);

		deco.setDescriptionText("Use CNTL + SPACE to see possible values");
		deco.setImage(Utils.getImage("treeedit.png"));
		deco.setShowOnlyOnFocus(false);

		// hide the decoration if the text component has content
		// text.addModifyListener(new ModifyListener() {
		// @Override
		// public void modifyText(ModifyEvent e) {
		// Text text = (Text) e.getSource();
		// if (!text.getText().isEmpty()) {
		// deco.hide();
		// } else {
		// deco.show();
		// }
		// }
		// });

		// help the user with the possible inputs
		// "." and "#" activate the content proposals
		char[] autoActivationCharacters = new char[] { '.' };
		KeyStroke keyStroke;
		//
		// try {
		// keyStroke = KeyStroke.getInstance("Ctrl+Space");
		// ContentProposalAdapter adapter = new ContentProposalAdapter(text,
		// new TextContentAdapter(),
		// new SimpleContentProposalProvider(new String[] {
		// "ProposalOne", "ProposalTwo", "ProposalThree" }),
		// keyStroke, autoActivationCharacters);
		// } catch (ParseException e1) {
		// e1.printStackTrace();
		// }

		try {
			keyStroke = KeyStroke.getInstance("Ctrl+Space");
			ContentProposalAdapter adapter = new ContentProposalAdapter(text,
					new EbookTextContentAdapter(), contentProposalProvider,
					keyStroke, autoActivationCharacters);
			adapter.setPopupSize(new Point(300, 300));
			// adapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		return ctrl;
	}

}
