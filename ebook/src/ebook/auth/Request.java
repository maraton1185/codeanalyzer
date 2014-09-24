package ebook.auth;

import java.lang.reflect.Field;

/**
 * ����� ��������� ��� ���������� �������� � �����
 * 
 * @author Enikeev M.A.
 * 
 */
public class Request {

	public String name = "";

	public String password = "";

	public Boolean activated = false;

	public String serial = "";

	public Boolean crypt_error = false;

	public Boolean user_error = false;

	public String message = "";

	// public Boolean accept = false;
	//
	// public Boolean pro = false;

	// public Boolean withoutExpirationDate = false;
	//
	// public String ExpirationDate = "";
	//
	// public String InitialDate = "";
	//
	// public String version = "";

	public void clear() {

		try {
			for (Field f : getClass().getDeclaredFields()) {

				if (f.getName().equalsIgnoreCase("name")
						|| f.getName().equalsIgnoreCase("activated")
						|| f.getName().equalsIgnoreCase("password")
						|| f.getName().equalsIgnoreCase("serial")) {

					continue;
				}
				if (f.getType().isAssignableFrom(Boolean.class))
					f.set(this, false);
				else
					f.set(this, "");

			}
		} catch (Exception e) {
		}
	}
}
