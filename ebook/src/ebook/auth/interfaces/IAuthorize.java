package ebook.auth.interfaces;

import ebook.auth.ActivationInfo;

public interface IAuthorize {

	/**
	 * ��������� ����� ����� ������ � �����
	 * 
	 * @param name
	 * @param password
	 * @return - ���������� � ���������
	 */
	public ActivationInfo Activate(String name, String password);

	/**
	 * �������� ����������� ���
	 * 
	 * @param name
	 * @param password
	 * @return
	 */
	public boolean check();

	/**
	 * �������� ����������� ���
	 * 
	 * @param name
	 * @param password
	 * @return
	 */
	public ActivationInfo getInfo();

	/**
	 * �������� ����������
	 * 
	 * @param shell
	 * 
	 * @return ������ ��������� � ������� ����������
	 */
	// public String checkUpdates();

	// boolean checkBooksCount(Shell shell);
	//
	// public boolean checkUsersCount(Shell shell);
	//
	// public boolean checkSectionsCount(Shell shell, BookConnection book);

}