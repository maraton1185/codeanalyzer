package codeanalyzer.handlers.books;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

public class _SetFontHandler {
	@Execute
	public void execute(@Active Shell shell) {
		// FontDialog fd = new FontDialog(shell, SWT.NONE);
		// fd.setText("Select Font");
		// fd.setRGB(new RGB(0, 0, 255));
		//
		// Font defaultFont = SWTResourceManager.getFont("Tahoma", 12,
		// SWT.NORMAL);
		// fd.setFontList(defaultFont.getFontData());
		// FontData newFont = fd.open();
		// if (newFont == null)
		// return;
		// // t.setFont(new Font(d, newFont));
		// // t.setForeground(new Color(d, fd.getRGB()));
		//
		// AppManager.br.post(Const.EVENT_SET_FONT_CONTENT_VIEW,
		// new EVENT_SET_FONT_CONTENT_VIEW_DATA(newFont, fd.getRGB()));
	}
}