package codeanalyzer.core;

import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.lifecycle.PreSave;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.modeling.IWindowCloseHandler;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import codeanalyzer.core.interfaces.IAuthorize;
import codeanalyzer.core.interfaces.IBookManager;
import codeanalyzer.db.services.FillProcLinkTableJob;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.PreferenceSupplier;
import codeanalyzer.utils.Strings;

public class AppManager {

	public static IEventBroker br;
	public static IEclipseContext ctx;
	public static UISynchronize sync;
	public static EModelService model;
	public static MApplication app;
	public static EPartService ps;
	public static ESelectionService ss;

	// private static EContextService cs;
	// public static EHandlerService hs;
	// public static ECommandService cs;

	@PostContextCreate
	public void postContextCreate() {

	}

	@PreSave
	public void preSave() {

	}

	@ProcessAdditions
	public void processAdditions(IEventBroker br, IEclipseContext ctx,
			UISynchronize sync, EModelService modelService,
			MApplication application, EPartService ps, ESelectionService ss) {

		AppManager.ss = ss;
		AppManager.br = br;
		AppManager.ctx = ctx;
		AppManager.sync = sync;
		AppManager.model = modelService;
		AppManager.ps = ps;
		AppManager.app = application;

		MWindow window = (MWindow) modelService.find(
				"codeanalyzer.trimmedwindow.main", app);

		br.subscribe(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE,
				new AppStartupCompleteEventHandler(window));

		br.subscribe(Const.EVENT_UPDATE_STATUS, new EVENT_UPDATE_STATUS());
	}

	private static class AppStartupCompleteEventHandler implements EventHandler {
		private MWindow theWindow;

		@Override
		public void handleEvent(Event event) {
			WindowCloseHandler closeHandler = new WindowCloseHandler();
			theWindow.getContext().set(IWindowCloseHandler.class, closeHandler);
			AppManager.br.send(Const.EVENT_UPDATE_STATUS, null);

			perspectiveActions();

			openBookOnStartup();

		}

		private void openBookOnStartup() {
			
			if(!PreferenceSupplier.getBoolean(PreferenceSupplier.OPEN_BOOK_ON_STARTUP))
				return;
			
			IPath p = new Path(
					PreferenceSupplier.get(PreferenceSupplier.BOOK_ON_STARTUP));
			if (p.isEmpty())
				return;

			IBookManager bm = pico.get(IBookManager.class);

			bm.openBook(p, (Shell) theWindow.getWidget());

			AppManager.br.post(Const.EVENT_SHOW_BOOK, null);
			
		}

		AppStartupCompleteEventHandler(MWindow window) {
			theWindow = window;
		}

	}

	private static class EVENT_UPDATE_STATUS implements EventHandler {

		@Override
		public void handleEvent(Event event) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					final String status = pico.get(IAuthorize.class).getInfo()
							.ShortMessage();
					AppManager.sync.asyncExec(new Runnable() {
						@Override
						public void run() {
							MHandledToolItem element = (MHandledToolItem) AppManager.model
									.find(Strings.get("model_id_activate"),
											AppManager.app);
							element.setLabel(status);
							element.setVisible(true);
						}
					});
				}
			}).start();
		}
	}

	public static class WindowCloseHandler implements IWindowCloseHandler {

		@Override
		public boolean close(MWindow window) {

			IJobManager jobMan = Job.getJobManager();
			jobMan.cancel(FillProcLinkTableJob.MY_FAMILY);
			try {
				jobMan.join(FillProcLinkTableJob.MY_FAMILY, null);
			} catch (OperationCanceledException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return true;
		}
	}

	public static void perspectiveActions() {
		MPerspective persp;

		if (PreferenceSupplier
				.getBoolean(PreferenceSupplier.SHOW_BOOK_PERSPECTIVE)) {

			persp = (MPerspective) model.find(
					Strings.get("model.id.perspective.books"), app);
			persp.setVisible(true);
			ps.switchPerspective(persp);

		} else {

			persp = (MPerspective) model.find(
					Strings.get("model.id.perspective.default"), app);
			persp.setVisible(true);
			ps.switchPerspective(persp);

			List<MPart> parts = model.findElements(app,
					Strings.get("model.id.part.start"), MPart.class, null);
			MPart part = parts.get(0);
			if (PreferenceSupplier
					.getBoolean(PreferenceSupplier.SHOW_START_PAGE))
				ps.showPart(part, PartState.ACTIVATE);
			else
				ps.hidePart(part);

		}
	}
}
