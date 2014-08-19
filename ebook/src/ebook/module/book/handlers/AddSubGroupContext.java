package ebook.module.book.handlers;

import java.lang.reflect.InvocationTargetException;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import ebook.module.book.BookConnection;
import ebook.module.book.tree.SectionInfo;
import ebook.module.conf.tree.ContextInfo;
import ebook.utils.Strings;

public class AddSubGroupContext {
	@Inject
	Shell shell;

	@Execute
	public void execute(@Active BookConnection book,
			@Active SectionInfo section, @Active ContextInfo item) {

		try {

			ContextInfo data = new ContextInfo();
			data.setTitle("");// Strings.value("context"));
			data.setGroup(true);
			book.ctxsrv(section).add(data, item, true);
			book.ctxsrv(section).edit(data);

		} catch (InvocationTargetException e) {
			MessageDialog.openError(shell, Strings.title("appTitle"),
					"Ошибка создания раздела.");
		}

	}

	@CanExecute
	public boolean canExecute(@Optional @Active ContextInfo item,
			@Optional @Active SectionInfo section) {
		return item != null && section != null;
	}

}