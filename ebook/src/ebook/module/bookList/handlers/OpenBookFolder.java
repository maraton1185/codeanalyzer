 
package ebook.module.bookList.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.program.Program;

import ebook.utils.PreferenceSupplier;

public class OpenBookFolder {
	@Execute
	public void execute() {
		Program.launch(PreferenceSupplier
				.get(PreferenceSupplier.DEFAULT_BOOK_DIRECTORY));
	}
		
}