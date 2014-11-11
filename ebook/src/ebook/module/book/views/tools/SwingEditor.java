package ebook.module.book.views.tools;

import net.atlanticbb.tantlinger.shef.HTMLEditorPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import ebook.module.book.service.BookService;
import ebook.module.book.tree.SectionInfo;
import ebook.module.book.views.interfaces.ITextEditor;

public class SwingEditor extends Composite implements ITextEditor {
	public SwingEditor(Composite parent, SectionInfo section, BookService srv) {
		super(parent, SWT.EMBEDDED);

		HTMLEditorPane editor = new HTMLEditorPane();
		// JFrame frame = new JFrame();
		// frame.add(editor);

		// Composite swtAwtComponent = new Composite(parent, SWT.EMBEDDED);
		java.awt.Frame frame = SWT_AWT.new_Frame(this);
		// javax.swing.JPanel panel = new javax.swing.JPanel();
		frame.add(editor);

		// JMenuBar menuBar = new JMenuBar();
		// menuBar.add(editor.getEditMenu());
		// menuBar.add(editor.getFormatMenu());
		// menuBar.add(editor.getInsertMenu());
		// frame.setJMenuBar(menuBar);

		// panel.setLayout(new FlowLayout());
		// JButton swingButton = new javax.swing.JButton();
		// swingButton.setText("Swing button");
		// panel.add(swingButton);
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateUrl() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLayoutData(GridData gridData) {
		// setLayoutData(gridData);

	}

	@Override
	public void addSectionLink(Integer id, Integer id2, String string,
			String title) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addLink(Integer id, String title) {
		// TODO Auto-generated method stub

	}

}
