package ebook.module.confList.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;

public class ExecuteLastCommand {

	@Execute
	public void execute() {
		// TODO Your code goes here
	}

	@CanExecute
	public boolean canExecute() {
		// TODO Your code goes here
		return true;
	}

}