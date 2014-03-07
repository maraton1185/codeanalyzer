package codeanalyzer.core;

import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.lifecycle.PreSave;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.IWindowCloseHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import codeanalyzer.core.interfaces.IAuthorize;
import codeanalyzer.db.services.FillProcLinkTableJob;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Strings;

public class LifeCycleManager {

	@PostContextCreate
	public void postContextCreate() {

	}

	@PreSave
	public void preSave() {

	}

	@ProcessAdditions
	public void processAdditions(IEventBroker eventBroker, MApplication app,
			EModelService modelService) {
		MWindow window = (MWindow) modelService.find(
				"codeanalyzer.trimmedwindow.main", app);

		eventBroker.subscribe(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE,
				new AppStartupCompleteEventHandler(window));

		eventBroker.subscribe(Const.EVENT_UPDATE_STATUS,
				new showStatusHandler());
	}

	private static class AppStartupCompleteEventHandler implements EventHandler {
		private MWindow theWindow;

		@Override
		public void handleEvent(Event event) {
			WindowCloseHandler closeHandler = new WindowCloseHandler();
			theWindow.getContext().set(IWindowCloseHandler.class, closeHandler);
			E4Services.br.send(Const.EVENT_UPDATE_STATUS, null);
		}

		AppStartupCompleteEventHandler(MWindow window) {
			theWindow = window;
		}
	}

	private static class WindowCloseHandler implements IWindowCloseHandler {

		@Override
		public boolean close(MWindow window) {

			IJobManager jobMan = Job.getJobManager();
			jobMan.cancel(FillProcLinkTableJob.MY_FAMILY);
			try {
				jobMan.join(FillProcLinkTableJob.MY_FAMILY, null);
			} catch (OperationCanceledException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Shell shell = (Shell) window.getWidget();

			if (MessageDialog.openConfirm(shell,
					Strings.get("QuitHandlerTitle"),
					Strings.get("QuitHandlerText"))) {

				// if (MessageDialog.openConfirm(shell, "Confirmation",
				// "Do you want to exit?")) {
				return true;
			}
			return false;
		}
	}

	private static class showStatusHandler implements EventHandler {

		@Override
		public void handleEvent(Event event) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					final String status = pico.get(IAuthorize.class).getInfo()
							.ShortMessage();
					E4Services.sync.asyncExec(new Runnable() {
						@Override
						public void run() {
							MHandledToolItem element = (MHandledToolItem) E4Services.model
									.find(Strings.get("model_id_activate"),
											E4Services.app);
							element.setLabel(status);
							element.setVisible(true);
						}
					});
				}
			}).start();
		}
	}

}
