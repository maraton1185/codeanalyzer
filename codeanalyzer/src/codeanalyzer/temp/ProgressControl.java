package codeanalyzer.temp;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.core.App;
import codeanalyzer.module.cf.interfaces.ICf;
import codeanalyzer.utils.Events;

public class ProgressControl implements IProgressMonitor {

	public static class BeginTaskData {
		public String name;
		public int total;
	}

	private ProgressBar progressBar;
	private volatile boolean cancelled = false;

	@Inject
	UISynchronize sync;

	private Label lable;

	private ICf db;
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
	public void beginTask(String name, int totalWork) {

		if (cancelled)
			return;

		BeginTaskData data = new BeginTaskData();
		data.name = name;
		data.total = totalWork;
		App.br.post(Events.EVENT_PROGRESS_BEGIN_TASK, data);

	}

	@Override
	public void worked(final int work) {
		if (cancelled)
			return;

		App.br.post(Events.EVENT_PROGRESS_WORKED, work);

	}

	@Override
	public void done() {
		if (cancelled)
			return;
		App.br.post(Events.EVENT_PROGRESS_DONE, null);
	}

	@Inject
	@Optional
	public void EVENT_PROGRESS_BEGIN_TASK(
			@UIEventTopic(Events.EVENT_PROGRESS_BEGIN_TASK) Object o) {

		if (progressBar.isDisposed())
			return;

		BeginTaskData data = (BeginTaskData) o;
		progressBar.setSelection(0);
		progressBar.setMaximum(data.total);
		progressBar.setToolTipText(data.name);
		lable.setText(db.getName() + ": " + data.name);
		btnCancel.setVisible(true);
	}

	@Inject
	@Optional
	public void EVENT_PROGRESS_WORKED(
			@UIEventTopic(Events.EVENT_PROGRESS_WORKED) Object o) {
		if (progressBar.isDisposed())
			return;
		progressBar.setSelection(progressBar.getSelection() + (int) o);
	}

	@Inject
	@Optional
	public void EVENT_PROGRESS_DONE(
			@UIEventTopic(Events.EVENT_PROGRESS_DONE) Object o) {
		if (progressBar.isDisposed())
			return;
		progressBar.setSelection(0);
		lable.setText("");
		btnCancel.setVisible(false);
	}

	@Inject
	@Optional
	public void EVENT_PROGRESS_ERROR(
			@UIEventTopic(Events.EVENT_PROGRESS_ERROR) Object o, Shell shell) {
		MessageDialog
				.openError(shell, "Ошибка выполнения операции", (String) o);
	}

	@Override
	public void internalWorked(double work) {

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

	}

	public void setDb(ICf db) {
		this.db = db;
		cancelled = false;

	}

}
