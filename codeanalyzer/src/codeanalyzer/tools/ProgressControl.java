package codeanalyzer.tools;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;

import codeanalyzer.core.interfaces.IDb;

public class ProgressControl implements IProgressMonitor {

	private ProgressBar progressBar;
	private volatile boolean cancelled = false;

	@Inject
	UISynchronize sync;

	private Label lable;

	private IDb db;
	private Button btnCancel;

	@PostConstruct
	public void createControls(Composite parent) {
		parent.setLayout(new RowLayout(SWT.HORIZONTAL));

		progressBar = new ProgressBar(parent, SWT.SMOOTH);

		btnCancel = new Button(parent, SWT.NONE);
		btnCancel.setLayoutData(new RowData(SWT.DEFAULT, 17));
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cancelled = true;
				progressBar.setSelection(0);
				lable.setText("");
				btnCancel.setVisible(false);
			}
		});
		btnCancel.setText("Отмена");
		btnCancel.setVisible(false);

		lable = new Label(parent, SWT.NONE);
		lable.setLayoutData(new RowData(500, SWT.DEFAULT));

	}

	@Override
	public void worked(final int work) {
		if (cancelled)
			return;

		sync.syncExec(new Runnable() {
			@Override
			public void run() {
				if (progressBar.isDisposed())
					return;
				progressBar.setSelection(progressBar.getSelection() + work);
			}
		});
	}

	@Override
	public void beginTask(final String name, final int totalWork) {

		if (cancelled)
			return;

		sync.syncExec(new Runnable() {
			@Override
			public void run() {
				if (progressBar.isDisposed())
					return;

				progressBar.setSelection(0);
				progressBar.setMaximum(totalWork);
				progressBar.setToolTipText(name);
				lable.setText(db.getName() + ": " + name);
				btnCancel.setVisible(true);
			}
		});
		// System.out.println("Starting");

	}

	@Override
	public void done() {
		if (cancelled)
			return;

		sync.syncExec(new Runnable() {
			@Override
			public void run() {
				if (progressBar.isDisposed())
					return;

				progressBar.setSelection(0);
				lable.setText("");
				btnCancel.setVisible(false);

			}
		});
	}

	@Override
	public void internalWorked(double work) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isCanceled() {
		return cancelled;
	}

	@Override
	public void setCanceled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	@Override
	public void setTaskName(final String name) {

	}

	@Override
	public void subTask(final String name) {
		// sync.syncExec(new Runnable() {
		// @Override
		// public void run() {
		// lblSubTask.setText(name);
		// // comp.layout();
		// }
		// });
	}

	public void setDb(IDb db) {
		this.db = db;
		cancelled = false;

	}

}
