package ebook.module.book.handlers;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import ebook.auth.interfaces.IAuthorize;
import ebook.core.pico;
import ebook.module.book.BookConnection;
import ebook.module.book.tree.SectionInfo;
import ebook.utils.Strings;

public class Add {
	@Execute
	public void execute(@Active SectionInfo section, Shell shell,
			@Active BookConnection book) {

		if (!pico.get(IAuthorize.class).checkSectionsCount(shell, book))
			return;

		try {

			SectionInfo data = new SectionInfo();
			data.setTitle(Strings.value("block"));
			data.setGroup(false);
			book.srv().add(data, section, true);

		} catch (InvocationTargetException e) {
			MessageDialog.openError(shell, Strings.title("appTitle"),
					"Ошибка создания блока текста.");
		}
	}

	@CanExecute
	public boolean canExecute(@Active @Optional SectionInfo item) {
		return item != null;
	}
}