package ebook.module.confLoad.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectSortMap {

	public Integer get(String s) {

		for (String k : map) {

			if (s.contains(k))
				return map.indexOf(k);
		}
		return 1000;

	}

	static ObjectSortMap instance;

	public static ObjectSortMap getInstance() {
		instance = instance == null ? new ObjectSortMap() : instance;
		return instance;
	}

	List<String> map = new ArrayList<String>();

	ObjectSortMap() {

		map.add("������������");
		map.add("�����");
		map.add("�����");
		map.add("�������");
		map.add("���");
		map.add("��������");
		map.add("�����");
		map.add("�����");
		map.add("��������");
		map.add("����������");
		map.add("��������");
		map.add("���������������");
		map.add("�����������������");
		map.add("����������");
		map.add("�����������");
		map.add("���������");
		map.add("������");

	}
}
