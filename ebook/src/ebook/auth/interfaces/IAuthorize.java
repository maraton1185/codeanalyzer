package ebook.auth.interfaces;

import ebook.auth.ActivationInfo;

public interface IAuthorize {

	/**
	 * получение ключа через запрос к сайту
	 * 
	 * @param name
	 * @param password
	 * @return - информация о активации
	 */
	public ActivationInfo Activate(String name, String password);

	/**
	 * проверка доступности про
	 * 
	 * @param name
	 * @param password
	 * @return
	 */
	public boolean check();

	/**
	 * проверка доступности про
	 * 
	 * @param name
	 * @param password
	 * @return
	 */
	public ActivationInfo getInfo();

	/**
	 * проверка обновлений
	 * 
	 * @param shell
	 * 
	 * @return строка сообщения о наличии обновлений
	 */
	// public String checkUpdates();

	// boolean checkBooksCount(Shell shell);
	//
	// public boolean checkUsersCount(Shell shell);
	//
	// public boolean checkSectionsCount(Shell shell, BookConnection book);

}