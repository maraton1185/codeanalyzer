package codeanalyzer.module.books.handlers.section;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.module.books.WindowBookInfo;
import codeanalyzer.module.books.section.SectionInfo;

public class Add {
	@Execute
	public void execute(Shell shell, WindowBookInfo book, @Active SectionInfo section) {

		book.sections().add(section);

	}

	@CanExecute
	public boolean canExecute(@Optional @Active SectionInfo section) {
		return section != null;
	}

}