package ebook.core;

import org.eclipse.e4.ui.model.application.ui.menu.MDirectToolItem;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import ebook.auth.interfaces.IAuthorize;
import ebook.utils.Strings;

public abstract class GlobalEvents {

	// UPADTE PERSPECTIVE ICON

	public static class EVENT_UPDATE_PERSPECTIVE_ICON implements EventHandler {

		@Override
		public void handleEvent(Event event) {
			// new Thread(new Runnable() {
			// @Override
			// public void run() {

			App.sync.asyncExec(new Runnable() {
				@Override
				public void run() {

					MDirectToolItem data = (MDirectToolItem) App.model.find(
							Strings.model("ebook.directtoolitem.data"), App.app);
					MDirectToolItem panel = (MDirectToolItem) App.model.find(
							Strings.model("ebook.directtoolitem.panel"),
							App.app);

					switch (App.currentPerspective) {
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
			// }
			// }).start();
		}
	}

	// UPADTE STATUS

	public static class EVENT_UPDATE_STATUS implements EventHandler {

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
									.find(Strings.model("model_id_activate"),
											App.app);
							h_element.setLabel(status);
							// h_element.setVisible(true);

							MDirectToolItem d_element = (MDirectToolItem) App.model
									.find(Strings
											.model("ebook.directtoolitem.1"),
											App.app);
							d_element.setLabel(App.getJetty().jettyMessage());
							// d_element.setVisible(true);

						}
					});
				}
			}).start();
		}
	}

	// START JETTY

	public static class EVENT_START_JETTY implements EventHandler {

		@Override
		public void handleEvent(Event event) {
			new Thread(new Runnable() {
				@Override
				public void run() {

					App.getJetty().start();

					// App.getJetty().openBookOnStratUp();

					App.sync.asyncExec(new Runnable() {
						@Override
						public void run() {

							MDirectToolItem d_element = (MDirectToolItem) App.model
									.find(Strings
											.model("ebook.directtoolitem.1"),
											App.app);
							d_element.setLabel(App.getJetty().jettyMessage());
							d_element.setVisible(false);
							d_element.setVisible(true);

						}
					});
				}
			}).start();
		}
	}

	public static class EVENT_STOP_JETTY implements EventHandler {

		@Override
		public void handleEvent(Event event) {
			new Thread(new Runnable() {
				@Override
				public void run() {

					App.getJetty().stop();

					App.sync.asyncExec(new Runnable() {
						@Override
						public void run() {

							MDirectToolItem d_element = (MDirectToolItem) App.model
									.find(Strings
											.model("ebook.directtoolitem.1"),
											App.app);
							d_element.setLabel(App.getJetty().jettyMessage());
							d_element.setVisible(false);
							d_element.setVisible(true);

						}
					});
				}
			}).start();
		}
	}

	public static class RESTART_WORKBENCH implements EventHandler {

		@Override
		public void handleEvent(Event event) {

			final Shell shell = App.ctx.get(Shell.class);
			final IWorkbench workbench = App.ctx.get(IWorkbench.class);

			App.sync.asyncExec(new Runnable() {
				@Override
				public void run() {

					boolean restart = MessageDialog.openQuestion(shell,
							Strings.title("appTitle"),
							"Обновление установлено. Перезапустить приложение?");
					if (restart)

						workbench.restart();

				}
			});

		}
	}

	public static class SHOW_UPDATE_AVAILABLE implements EventHandler {

		@Override
		public void handleEvent(Event event) {

			App.sync.asyncExec(new Runnable() {
				@Override
				public void run() {

					MDirectToolItem d_element = (MDirectToolItem) App.model
							.find(Strings.model("ebook.directtoolitem.3"),
									App.app);
					d_element.setLabel(Strings.msg("updateAvailable"));
					d_element.setVisible(true);

				}
			});

		}

	}

}
