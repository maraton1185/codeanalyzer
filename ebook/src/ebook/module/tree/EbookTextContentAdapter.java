package ebook.module.tree;

import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

public class EbookTextContentAdapter extends TextContentAdapter {

	@Override
	public void insertControlContents(Control control, String contents,
			int cursorPosition) {

		String text = ((Text) control).getText();
		int i = text.lastIndexOf(".");
		if (i >= 0) {
			text = text.substring(0, i);
			text = text + "." + contents;
		} else
			text = contents;

		((Text) control).setText(text);
		((Text) control).setSelection(text.length());
		// Insert will leave the cursor at the end of the inserted text. If this
		// is not what we wanted, reset the selection.
		// if (cursorPosition < text.length()) {
		// ((Text) control).setSelection(selection.x + cursorPosition,
		// selection.x + cursorPosition);
		// }
	}

}
