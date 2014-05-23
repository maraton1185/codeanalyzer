 
package codeanalyzer.module.books.handlers.list;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.program.Program;

import codeanalyzer.utils.PreferenceSupplier;

public class OpenBookFolder {
	@Execute
	public void execute() {
		Program.launch(PreferenceSupplier
				.get(PreferenceSupplier.DEFAULT_BOOK_DIRECTORY));
	}
		
}