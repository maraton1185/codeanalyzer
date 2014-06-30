package ebook.core;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MStackElement;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MDirectToolItem;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.modeling.IWindowCloseHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import ebook.auth.interfaces.IAuthorize;
import ebook.core.exceptions.MakeConnectionException;
import ebook.core.interfaces.IDbConnection;
import ebook.core.interfaces.IManagerFactory;
import ebook.core.interfaces.IServiceFactory;
import ebook.module.book.BookConnection;
import ebook.module.book.BookOptions;
import ebook.module.book.tree.SectionInfo;
import ebook.module.book.views.section.SectionView;
import ebook.module.conf.ConfConnection;
import ebook.module.conf.services.FillProcLinkTableJob;
import ebook.utils.Events;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;
import ebook.utils.Utils;
import ebook.web.IJetty;

public class App {

	public enum Perspectives {
		lists, main;

		@Override
		public String toString() {
			switch (this) {
			case lists:
				return Strings.get("model.id.perspective.books");
			default:
				return Strings.get("model.id.perspective.default");
			}
		}
	}

	public enum ListParts {
		books, users, confs, current;

		@Override
		public String toString() {
			switch (this) {
			case books:
				return Strings.get("ebook.part.0");
			case users:
				return Strings.get("ebook.part.4");
			case confs:
				return Strings.get("ebook.part.confList");
			default:
				return "current";
			}
		}

		public static ListParts get(String id) {
			switch (id) {
			case "ebook.part.book":
				return ListParts.books;
			case "ebook.part.user":
				return ListParts.users;
			case "ebook.part.conf":
				return ListParts.confs;
			default:
				return ListParts.current;
			}
		}
	}

	public static Perspectives currentPerspective;

	public static IEventBroker br;
	public static IEclipseContext ctx;
	public static UISynchronize sync;
	public static EModelService model;
	public static MApplication app;
	public static EPartService ps;
	public static ESelectionService ss;

	public static IServiceFactory srv = pico.get(IServiceFactory.class);
	public static IManagerFactory mng = pico.get(IManagerFactory.class);

	private static IJetty jetty = pico.get(IJetty.class);

	@ProcessAdditions
	public void processAdditions(IEventBroker br, IEclipseContext ctx,
			UISynchronize sync, EModelService modelService,
			MApplication application, EPartService ps, ESelectionService ss) {

		App.ss = ss;
		App.br = br;
		App.ctx = ctx;
		App.sync = sync;
		App.model = modelService;
		App.ps = ps;
		// App.hs = hs;
		// App.cs = cs;
		App.app = application;

		MTrimmedWindow window = (MTrimmedWindow) modelService.find(
				"ebook.trimmedwindow.main", app);

		br.subscribe(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE,
				new AppStartupCompleteEventHandler(window));

		br.subscribe(Events.EVENT_UPDATE_STATUS, new EVENT_UPDATE_STATUS());

		br.subscribe(Events.EVENT_START_JETTY, new EVENT_START_JETTY());

		br.subscribe(Events.EVENT_UPDATE_PERSPECTIVE_ICON,
				new EVENT_UPDATE_PERSPECTIVE_ICON());

		dbInit(window);

	}

	// DB INIT
	private void dbInit(MTrimmedWindow window) {
		IDbConnection db = pico.get(IDbConnection.class);
		try {
			// throw new SQLException();

			try {
				db.check();
			} catch (InvocationTargetException e1) {
				if (e1.getTargetException() instanceof MakeConnectionException) {
					db.create();
				} else
					throw new Exception();
			}

			db.openConnection();

		} catch (Exception e) {
			Shell shell = (Shell) window.getWidget();
			if (MessageDialog.openQuestion(shell, Strings.get("appTitle"),
					Strings.get("error.initDb"))) {
				try {
					db.create();
					db.openConnection();
				} catch (Exception e1) {
					e1.printStackTrace();
					MessageDialog.openError(shell, Strings.get("appTitle"),
							Strings.get("error.start"));
					IWorkbench workbench = window.getContext().get(
							IWorkbench.class);
					workbench.close();
				}
			}

			e.printStackTrace();
		}
	}

	// APP STARTUP, TRAY

	private static class AppStartupCompleteEventHandler implements EventHandler {
		private final MTrimmedWindow window;

		@Override
		public void handleEvent(Event event) {
			WindowCloseHandler closeHandler = new WindowCloseHandler();
			window.getContext().set(IWindowCloseHandler.class, closeHandler);
			App.br.post(Events.EVENT_UPDATE_STATUS, null);
			App.br.post(Events.EVENT_START_JETTY, null);

			perspectiveActions();

			openBookOnStartup();

			trayOptions();
		}

		private void trayOptions() {
			final Image image = Utils.getImage("favicon.png");
			final Shell shell = ((Shell) window.getWidget());

			shell.addShellListener(new ShellAdapter() {
				@Override
				public void shellIconified(ShellEvent e) {

					if (!PreferenceSupplier
							.getBoolean(PreferenceSupplier.MINIMIZE_TO_TRAY))
						return;

					window.setVisible(false);
					final Tray tray = Display.getCurrent().getSystemTray();
					if (tray == null)
						return;
					if (tray.getItemCount() != 0)
						return;
					final TrayItem item = new TrayItem(tray, SWT.NONE);
					item.setToolTipText(Strings.get("appTitle"));
					item.setImage(image);

					item.addListener(SWT.DefaultSelection, new Listener() {

						@Override
						public void handleEvent(
								org.eclipse.swt.widgets.Event event) {

							window.setVisible(true);

							shell.setMinimized(false);

							shell.forceActive();
						}

					});

					item.addListener(SWT.MenuDetect, new Listener() {
						@Override
						public void handleEvent(
								org.eclipse.swt.widgets.Event event) {

							// Style must be pop up
							Menu m = new Menu(shell, SWT.POP_UP);

							MenuItem open = new MenuItem(m, SWT.NONE);
							open.setText("Открыть в браузере");
							open.addListener(SWT.Selection, new Listener() {
								@Override
								public void handleEvent(
										org.eclipse.swt.widgets.Event event) {

									Program.launch(App.getJetty().info());
									// IWorkbench workbench =
									// window.getContext()
									// .get(IWorkbench.class);
									// workbench.close();
								}
							});

							MenuItem exit = new MenuItem(m, SWT.NONE);
							exit.setText("Выход");
							exit.addListener(SWT.Selection, new Listener() {
								@Override
								public void handleEvent(
										org.eclipse.swt.widgets.Event event) {
									IWorkbench workbench = window.getContext()
											.get(IWorkbench.class);
									workbench.close();
								}
							});

							// We need to make the menu visible
							m.setVisible(true);
						};
					});
				}
			});

			if (PreferenceSupplier
					.getBoolean(PreferenceSupplier.MINIMIZE_TO_TRAY_ON_STARTUP))
				shell.setMinimized(true);

		}

		private void openBookOnStartup() {

			if (!PreferenceSupplier
					.getBoolean(PreferenceSupplier.OPEN_BOOK_ON_STARTUP))
				return;

			IPath p = new Path(
					PreferenceSupplier.get(PreferenceSupplier.BOOK_ON_STARTUP));
			if (p.isEmpty())
				return;

			App.mng.blm().open(p, (Shell) window.getWidget());

			App.br.post(Events.EVENT_SHOW_BOOK, null);

		}

		AppStartupCompleteEventHandler(MTrimmedWindow window) {
			this.window = window;
		}

	}

	// UPADTE PERSPECTIVE ICON

	private static class EVENT_UPDATE_PERSPECTIVE_ICON implements EventHandler {

		@Override
		public void handleEvent(Event event) {
			new Thread(new Runnable() {
				@Override
				public void run() {

					App.sync.asyncExec(new Runnable() {
						@Override
						public void run() {

							MDirectToolItem data = (MDirectToolItem) model
									.find(Strings
											.get("ebook.directtoolitem.data"),
											app);
							MDirectToolItem panel = (MDirectToolItem) model
									.find(Strings
											.get("ebook.directtoolitem.panel"),
											app);

							switch (currentPerspective) {
							case lists:
								data.setSelected(true);
								panel.setSelected(false);
								break;
							case main:
								data.setSelected(false);
								panel.setSelected(true);
								break;

							}

						}
					});
				}
			}).start();
		}
	}

	// UPADTE STATUS

	private static class EVENT_UPDATE_STATUS implements EventHandler {

		@Override
		public void handleEvent(Event event) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					final String status = pico.get(IAuthorize.class).getInfo()
							.ShortMessage();
					App.sync.asyncExec(new Runnable() {
						@Override
						public void run() {

							// MHandledToolItem element;
							MHandledToolItem h_element = (MHandledToolItem) App.model
									.find(Strings.get("model_id_activate"),
											App.app);
							h_element.setLabel(status);
							h_element.setVisible(true);

							MDirectToolItem d_element = (MDirectToolItem) App.model.find(
									Strings.get("ebook.directtoolitem.1"),
									App.app);
							d_element.setLabel(jetty.jettyMessage());
							d_element.setVisible(true);

						}
					});
				}
			}).start();
		}
	}

	// START JETTY

	private static class EVENT_START_JETTY implements EventHandler {

		@Override
		public void handleEvent(Event event) {
			new Thread(new Runnable() {
				@Override
				public void run() {

					jetty.startJetty();

					App.sync.asyncExec(new Runnable() {
						@Override
						public void run() {

							MDirectToolItem d_element = (MDirectToolItem) App.model.find(
									Strings.get("ebook.directtoolitem.1"),
									App.app);
							d_element.setLabel(jetty.jettyMessage());
							d_element.setVisible(false);
							d_element.setVisible(true);

							// MToolBar tb = (MToolBar) App.model.find(
							// Strings.get("ebook.toolbar.top"), App.app);
							// ToolBar tbw = (ToolBar) tb.getWidget();
							// tbw.layout(true);
							// ((ToolBar));
						}
					});
				}
			}).start();
		}
	}

	// WINDOWS CLOSING

	public static class WindowCloseHandler implements IWindowCloseHandler {

		@Override
		public boolean close(MWindow window) {

			PreferenceSupplier.save();

			IJobManager jobMan = Job.getJobManager();
			jobMan.cancel(FillProcLinkTableJob.FillProcLinkTableJob_FAMILY);
			try {
				jobMan.join(FillProcLinkTableJob.FillProcLinkTableJob_FAMILY,
						null);
			} catch (OperationCanceledException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			IWorkbench workbench = window.getContext().get(IWorkbench.class);
			workbench.close();
			return true;
		}
	}

	public static class BookWindowCloseHandler implements IWindowCloseHandler {

		@Override
		public boolean close(MWindow window) {

			BookConnection book = window.getContext().get(BookConnection.class);

			SectionInfo section = window.getContext().get(SectionInfo.class);
			BookOptions opt = new BookOptions();
			if (section != null)
				opt.selectedSection = section.getId();

			List<MPartStack> stacks = model.findElements(App.app,
					Strings.get("ebook.partstack.sections"), MPartStack.class,
					null);

			if (!stacks.isEmpty()) {

				opt.openSections = new ArrayList<Integer>();
				for (MStackElement _part : stacks.get(0).getChildren()) {

					if (!(_part instanceof MPart))
						continue;

					MPart part = (MPart) _part;

					if (!part.isVisible())
						continue;
					String id = part.getElementId();

					if (id.equals(Strings
							.get("ebook.partdescriptor.sectionView"))) {
						SectionView view = (SectionView) part.getObject();
						if (view != null)
							opt.openSections.add(view.getId());
					}

					// if (id.equals(Strings
					// .get("ebook.partdescriptor.sectionsBlockView"))) {
					// BlockView view = (BlockView) part.getObject();
					// opt.openSections.add(view.getId());
					// }

				}

			}

			book.srv().saveBookOptions(opt);

			book.closeConnection();

			return true;
		}
	}

	public static class ConfWindowCloseHandler implements IWindowCloseHandler {

		@Override
		public boolean close(MWindow window) {

			ConfConnection conf = window.getContext().get(ConfConnection.class);

			// book.srv().saveBookOptions(opt);

			conf.closeConnection();

			return true;
		}
	}

	// PERSPECTIVES
	// ******************************************************************

	public static void perspectiveActions() {

		if (PreferenceSupplier
				.getBoolean(PreferenceSupplier.SHOW_BOOK_PERSPECTIVE))
			currentPerspective = Perspectives.lists;
		else
			currentPerspective = Perspectives.main;

		showPerspective(currentPerspective, ListParts.get(PreferenceSupplier
				.get(PreferenceSupplier.SELECTED_LIST)));

	}

	public static void togglePerspective() {
		if (currentPerspective == Perspectives.lists)
			currentPerspective = Perspectives.main;
		else
			currentPerspective = Perspectives.lists;
		showPerspective(currentPerspective, ListParts.current);
	}

	public static void showPerspective(Perspectives perspType,
			ListParts partType) {

		MPerspective persp;

		persp = (MPerspective) model.find(perspType.toString(), app);
		persp.setVisible(true);
		ps.switchPerspective(persp);

		currentPerspective = perspType;
		MPart part;

		if (partType != ListParts.current) {
			part = (MPart) model.find(partType.toString(), app);
			App.ps.activate(part);
		}

		br.post(Events.EVENT_UPDATE_PERSPECTIVE_ICON, null);
	}

	public static IJetty getJetty() {
		return jetty;
	}
}
