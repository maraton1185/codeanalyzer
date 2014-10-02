package ebook.auth;

import java.io.DataOutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import ebook.auth.interfaces.ICrypt;
import ebook.core.pico;
import ebook.core.exceptions.CryptException;
import ebook.core.exceptions.RequestParseException;
import ebook.core.exceptions.SiteAccessException;
import ebook.core.exceptions.SiteCryptException;
import ebook.utils.Const;

/**
 * класс сообщени€ дл€ выполнени€ запросов к сайту
 * 
 * @author Enikeev M.A.
 * 
 */
public class Request {

	public String name = "";

	public String password = "";

	public Boolean activated = false;

	public String uuid = "";

	public String error = "";

	public String dev_all = "";
	public String dev_activated = "";
	public String dev_free = "";

	public boolean activationRequest = true;
	public String msg_type = "";
	public String msg_text = "";

	// public Boolean dev_exist = false;

	public String getError(String code) {
		switch (code) {
		case "01":
			return Const.MSG_LOGIN;
		case "02":
			return Const.MSG_NO_FREE_DEVICES;
		case "03":
			return Const.MSG_SEND_EMAIL_TO;
		case "11":
			return "Ќе указаны адрес электронной почты или тело сообщени€.";
		case "14":
			return "ѕроверьте адрес электронной почты.\n"
					+ "Ќа него не удалось отправить подтверждающее письмо.";
		default:
			return activationRequest ? Const.MSG_SEND_EMAIL_TO
					: Const.MSG_SEND_EMAIL_TO_MSG;
		}
	}

	public String getError(Exception e) {
		if (e instanceof MalformedURLException) {
			return Const.ERROR_NO_ADRESS;
		} else if (e instanceof CryptException) {
			return Const.ERROR_CRYPT;
		} else if (e instanceof SiteAccessException) {
			return Const.ERROR_SITE_ACCESS;
		} else if (e instanceof SiteCryptException) {
			return Const.ERROR_SITE_CRYPT;
		} else if (e instanceof RequestParseException) {
			return activationRequest ? Const.MSG_SEND_EMAIL_TO
					: Const.MSG_SEND_EMAIL_TO_MSG;
		} else {
			return "exception error";
		}
	}

	public Request send(String _url) throws CryptException,
			MalformedURLException, SiteAccessException, SiteCryptException,
			RequestParseException {

		String body;
		if (activationRequest)
			body = getActivationString(this);
		else
			body = getMessageString(this);

		ICrypt crypt = pico.get(ICrypt.class);

		URL url = new URL(_url);

		HttpURLConnection connection;
		DataOutputStream wr;
		try {
			connection = (HttpURLConnection) url.openConnection();

			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection.setRequestProperty("charset", "utf-8");
			// connection.setRequestProperty("Content-Length",
			// "" + Integer.toString(urlData.length));
			connection.setUseCaches(false);

			wr = new DataOutputStream(connection.getOutputStream());
			// wr.write(urlData);
			String input = crypt.toString(crypt.Encrypt(body));
			String urlData = "msg=" + URLEncoder.encode(input, "UTF-8");

			// System.out.println(input.length());
			// String output = crypt.Decrypt(crypt.toByteArray(input));
			// System.out.println(output);

			wr.writeBytes(urlData);
			wr.flush();
			wr.close();

			byte[] cipheredBytes = crypt.toByteArray(connection
					.getInputStream());

			System.out.println(new String(cipheredBytes).trim());

			String responceString = new String(cipheredBytes);

			if (!responceString.startsWith("!")) {
				throw new RequestParseException();
			}

			responceString = responceString.substring(1);
			if (responceString.equalsIgnoreCase("00"))
				throw new SiteCryptException();

			try {
				responceString = crypt.Decrypt(crypt.toByteArray(responceString
						.trim()));
			} catch (Exception e) {
				throw new CryptException();
			}

			connection.disconnect();

			Request responce = get(responceString);

			return responce;

		} catch (SiteCryptException e) {
			throw new SiteCryptException();
		} catch (CryptException e) {
			throw new CryptException();
		} catch (RequestParseException e) {
			throw new RequestParseException();
		} catch (Exception e) {
			throw new SiteAccessException();
		}
	}

	private String getMessageString(Request msg) throws RequestParseException {
		String result = ICrypt.CRYPT_PREFIX;

		try {
			for (Field f : msg.getClass().getDeclaredFields()) {

				if (!(f.getName().equalsIgnoreCase("name")
						|| f.getName().equalsIgnoreCase("msg_type") || f
						.getName().equalsIgnoreCase("msg_text"))) {

					continue;
				}

				result += f.getName() + '=' + f.get(msg).toString() + '&';
			}
			result = result.substring(0, result.length() - 1);
		} catch (Exception e) {

			throw new RequestParseException();
		}

		return result;
	}

	public static String getActivationString(Request msg)
			throws RequestParseException {

		String result = ICrypt.CRYPT_PREFIX;

		try {
			for (Field f : msg.getClass().getDeclaredFields()) {

				if (!(f.getName().equalsIgnoreCase("name")
						|| f.getName().equalsIgnoreCase("activated")
						|| f.getName().equalsIgnoreCase("password") || f
						.getName().equalsIgnoreCase("uuid"))) {

					continue;
				}
				result += f.getName() + '=' + f.get(msg).toString() + '&';
			}
			result = result.substring(0, result.length() - 1);
		} catch (Exception e) {

			throw new RequestParseException();
		}

		return result;
	}

	/**
	 * из строки вида "[свойство]=[значение]&" получает класс сообщени€
	 * 
	 * @param decript
	 *            - исходна€ строка
	 * @return класс сообщени€ Request
	 * @throws RequestParseException
	 */
	public static Request get(String decript) throws RequestParseException {

		Request result = new Request();

		String msg = decript.trim().substring(ICrypt.CRYPT_PREFIX.length());
		String[] lines = msg.split("&");
		if (lines.length == 0)
			throw new RequestParseException();

		for (Field f : result.getClass().getDeclaredFields()) {
			for (String line : lines) {
				String[] v = line.split("=");
				if (v.length != 2)
					continue;
				if (f.getName().equalsIgnoreCase(v[0])) {
					try {
						if (f.getType().isAssignableFrom(Boolean.class))
							f.set(result, Boolean.parseBoolean(v[1]));
						else
							f.set(result, v[1]);
					} catch (Exception e) {
						throw new RequestParseException();
					}
					break;
				}
			}
		}

		return result;
	}
}
