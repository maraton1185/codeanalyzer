package ebook.module.text.handlers;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import ebook.module.text.TextConnection;
import ebook.module.text.tree.BookmarkInfo;
import ebook.utils.Strings;

public class BmrkAddSubGroup {

	@Execute
	public void execute(Shell shell, @Optional BookmarkInfo item,
			@Active TextConnection con) {

		try {

			BookmarkInfo data = new BookmarkInfo();
			data.setTitle(Strings.value("bookmark"));
			data.setGroup(true);
			con.bmkSrv().add(data, item, true);
			// con.bmkSrv().edit(data);

		} catch (InvocationTargetException e) {
			MessageDialog.openError(shell, Strings.title("appTitle"),
					"Ошибка создания подраздела.");
		}

	}

	@CanExecute
	public boolean canExecute(@Optional @Active BookmarkInfo item,
			@Active @Optional TextConnection con) {
		return con != null && con.isValid() && item != null;
	}
}
