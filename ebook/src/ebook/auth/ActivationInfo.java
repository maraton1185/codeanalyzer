package ebook.auth;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.osgi.service.prefs.Preferences;

import ebook.utils.Const;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;
import ebook.utils.jWMI;

/**
 * информация по ключу
 * 
 * @author Enikeev M.A.
 * 
 */
public class ActivationInfo {

	public ActivationInfo() {
		super();

		for (Field f : this.getClass().getDeclaredFields()) {
			try {
				if (f.getType().isAssignableFrom(Boolean.class))
					f.set(this, false);
				else
					f.set(this, "");
			} catch (Exception e) {
			}
		}
	}

	public String serial;

	public String uuid;

	public String name;

	public String password;

	// ключ активирован
	public Boolean activated;

	// private String check_message;

	private String message;

	public String getMessage() {
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		Calendar cal = Calendar.getInstance();

		return "(" + dateFormat.format(cal.getTime()) + ") " + message;
	}

	public void setMessage(String msg) {
		this.message = msg;
	}

	public boolean isEmpty() {

		return message.isEmpty();
	}

	// public Boolean user_error = false;

	// public Boolean withoutExpirationDate;

	// public String ExpirationDate;

	public String ShortMessage() {
		StringBuilder result = new StringBuilder();

		if (check()) {
			result.append(Const.MSG_PRO_SHORT);
		} else {
			result.append(Const.MSG_FREE_SHORT);
		}

		return result.toString();
	}

	public String FullMessage() {
		StringBuilder result = new StringBuilder();

		if (check()) {
			result.append(Const.MSG_PRO);
			result.append("UUID: " + uuid);
		} else {
			result.append(Const.MSG_FREE);
			result.append(message);
		}

		return result.toString();
	}

	public boolean check() {

		if (!this.activated) {
			message = Const.MSG_INCORRECT_SERIAL;
			return false;
		}

		Preferences preferences = PreferenceSupplier.getScoupNode();
		String name = preferences.get("P_LOGIN", Strings.pref("P_LOGIN"));
		String password = preferences.get("P_PASSWORD",
				Strings.pref("P_PASSWORD"));

		String uuid;
		try {
			uuid = getComputerUUID();
		} catch (Exception e1) {
			message = Const.MSG_GETID;
			return false;
		}

		if (!(this.name.equalsIgnoreCase(name)
				&& this.password.equalsIgnoreCase(password) && this.uuid
					.equalsIgnoreCase(uuid))) {
			message = Const.MSG_INCORRECT_SERIAL;
			return false;
		}

		return true;

	}

	/**
	 * UUID компьютера
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getComputerUUID() throws Exception {
		return jWMI
				.getWMIValue("SELECT UUID FROM Win32_ComputerSystemProduct",
						"UUID").replace("\n", "").replace("\r", "");
	}

	public void fill(Request msg) {
		for (Field f : this.getClass().getDeclaredFields()) {
			for (Field f1 : msg.getClass().getDeclaredFields()) {
				if (f.getName().equalsIgnoreCase(f1.getName())) {
					try {
						f.set(this, f1.get(msg));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}