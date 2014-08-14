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

		map.add("конфигурация");
		map.add("общий");
		map.add("общая");
		map.add("констан");
		map.add("спр");
		map.add("документ");
		map.add("переч");
		map.add("отчет");
		map.add("обработк");
		map.add("планвидовх");
		map.add("плансчет");
		map.add("регистрсведений");
		map.add("регистрнакопления");
		map.add("регистрбух");
		map.add("регистррасч");
		map.add("бизнеспро");
		map.add("задача");

	}
}
