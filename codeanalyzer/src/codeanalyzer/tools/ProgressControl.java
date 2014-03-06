package codeanalyzer.tools;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ProgressBar;

import codeanalyzer.core.E4Services;
import codeanalyzer.utils.Const;

public class ProgressControl implements IProgressMonitor {

	private ProgressBar progressBar;

	@Inject
	UISynchronize sync;

	@PostConstruct
	public void createControls(Composite parent) {
		progressBar = new ProgressBar(parent, SWT.SMOOTH);
		progressBar.setBounds(0, 0, 362, 20);

		E4Services.br.send(Const.EVENT_UPDATE_STATUS, null);

	}

	@Override
	public void worked(final int work) {
		sync.syncExec(new Runnable() {
			@Override
			public void run() {
				System.out.println("Worked");
				progressBar.setSelection(progressBar.getSelection() + work);
			}
		});
	}

	@Override
	public void beginTask(final String name, final int totalWork) {

		sync.syncExec(new Runnable() {
			@Override
			public void run() {
				progressBar.setSelection(0);
				progressBar.setMaximum(totalWork);
				progressBar.setToolTipText(name);
			}
		});
		System.out.println("Starting");

	}

	@Override
	public void done() {
		System.out.println("Done");
	}

	@Override
	public void internalWorked(double work) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isCanceled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setCanceled(boolean value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTaskName(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void subTask(String name) {
		// TODO Auto-generated method stub

	}

}
