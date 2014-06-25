package ebook.module.confList.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.program.Program;

import ebook.utils.PreferenceSupplier;

public class OpenConfFolder {
	@Execute
	public void execute() {
		Program.launch(PreferenceSupplier
				.get(PreferenceSupplier.DEFAULT_CONF_DIRECTORY));
	}
}