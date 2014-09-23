package ebook.auth;

import java.lang.reflect.Field;

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

	private String check_message;

	public String message;

	public String serial;

	public String name;

	public String password;

	// public Boolean withoutExpirationDate;

	// public String ExpirationDate;

	// ключ активирован
	public Boolean activated;

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
			result.append("UUID: " + serial);
		} else {
			result.append(Const.MSG_FREE);
			result.append(check_message);
		}

		return result.toString();
	}

	public boolean check() {

		if (!this.activated) {
			check_message = Const.MSG_INCORRECT_SERIAL;
			return false;
		}

		Preferences preferences = PreferenceSupplier.getScoupNode();
		String name = preferences.get("P_LOGIN", Strings.pref("P_LOGIN"));
		String password = preferences.get("P_PASSWORD",
				Strings.pref("P_PASSWORD"));

		String serial;
		try {
			serial = getComputerSerial();
		} catch (Exception e1) {
			check_message = Const.MSG_GETID;
			return false;
		}

		if (!(this.name.equalsIgnoreCase(name)
				&& this.password.equalsIgnoreCase(password) && this.serial
					.equalsIgnoreCase(serial))) {
			check_message = Const.MSG_INCORRECT_SERIAL;
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
	public static String getComputerSerial() throws Exception {
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