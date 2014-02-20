package ru.codeanalyzer.interfaces;

import ru.codeanalyzer.core.model.ActivationInfo;

public interface IAuthorize {

		
	/**
	 * ��������� ����� ����� ������ � �����
	 * @param name
	 * @param password
	 * @return - ���������� � ���������
	 */
	public ActivationInfo Activate(String name, String password);

	/**
	 * �������� ����������� ���
	 * @param name
	 * @param password
	 * @return 
	 */
	public boolean check();
	
	/**
	 * �������� ����������� ���
	 * @param name
	 * @param password
	 * @return 
	 */
	public ActivationInfo getInfo();
	
	/**
	 * �������� ����������
	 * @return ������ ��������� � ������� ����������
	 */
	public String checkUpdates();
		
}