package ebook.auth;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.prefs.Preferences;

import ebook.auth.interfaces.IAuthorize;
import ebook.auth.interfaces.ICrypt;
import ebook.core.App;
import ebook.core.pico;
import ebook.module.book.BookConnection;
import ebook.utils.Const;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;

public class SignIn implements IAuthorize {

	@Override
	public ActivationInfo Activate(String name, String password) {

		ActivationInfo info = new ActivationInfo();

		Request msg = new Request();
		msg.name = name;
		msg.password = password;

		try {
			msg.uuid = ActivationInfo.getComputerUUID();
		} catch (Exception e1) {
			info.setMessage(Const.MSG_ACTIVATE_FAIL + Const.MSG_GETID);
			return info;
		}

		// ******************************************

		Request response = new Request();
		try {
			response = msg.send(Const.URL_ACTIVATE());
		} catch (Exception e) {
			info.setMessage(Const.MSG_ACTIVATE_FAIL + msg.getError(e));
			return info;
		}

		if (!response.error.isEmpty()) {
			info.setMessage(Const.MSG_ACTIVATE_FAIL
					+ msg.getError(response.error));
			return info;
		}

		if (!response.activated) {
			info.setMessage(Const.MSG_ACTIVATE_FAIL + Const.MSG_SEND_EMAIL_TO);
			return info;
		}

		// ******************************************
		String siteMessage = String.format(Const.MSG_ACTIVATED,
				response.dev_all, response.dev_activated, response.dev_free);
		String activationString = "";
		try {
			// response.clear();
			activationString = Request.getActivationString(response);
			ICrypt crypt = pico.get(ICrypt.class);
			info.serial = crypt.toString(crypt.Encrypt(activationString));
		} catch (Exception e) {
			info.setMessage(Const.MSG_ACTIVATE_FAIL + e.getMessage());
			return info;
		}

		info.setMessage(Const.MSG_ACTIVATE_OK + siteMessage);

		return info;
	}

	@Override
	public boolean check() {

		ActivationInfo info = getInfo();

		return info.check();

	}

	@Override
	public ActivationInfo getInfo() {

		ActivationInfo info = new ActivationInfo();

		Preferences preferences = PreferenceSupplier.getScoupNode();
		String activationString = preferences.get("P_SERIAL",
				Strings.pref("P_SERIAL"));

		if (activationString.isEmpty())
			info.setMessage(Const.MSG_EMPTY_SERIAL);

		if (!info.isEmpty())
			return info;

		// ******************************************

		try {
			ICrypt crypt = pico.get(ICrypt.class);
			Request msg = Request.get(crypt.Decrypt(crypt
					.toByteArray(activationString)));
			info.fill(msg);
		} catch (Exception e) {
			info.setMessage(Const.MSG_INCORRECT_SERIAL);
		}
		// ******************************************

		return info;
	}

	// ****************************************************************************

	@Override
	public boolean checkBooksCount(Shell shell) {
		if (!check() && !App.srv.bl().check()) {
			MessageDialog.openError(shell, Strings.title("appTitle"),
					"Для free-версии в списке может находиться не более "
							+ Const.FREE_TREE_ITEMS_COUNT + " книг.");
			return false;
		}
		return true;
	}

	@Override
	public boolean checkUsersCount(Shell shell) {
		if (!check() && !App.srv.us().check()) {
			MessageDialog.openError(shell, Strings.title("appTitle"),
					"Для free-версии доступно не более "
							+ Const.FREE_TREE_ITEMS_COUNT + " пользователей.");
			return false;
		}
		return true;
	}

	@Override
	public boolean checkSectionsCount(Shell shell, BookConnection book) {
		if (!check() && !book.srv().check()) {
			MessageDialog.openError(shell, Strings.title("appTitle"),
					"Для free-версии доступно не более "
							+ Const.FREE_BOOK_ITEMS_COUNT
							+ " разделов в книге.");
			return false;
		}
		return true;
	}

}
