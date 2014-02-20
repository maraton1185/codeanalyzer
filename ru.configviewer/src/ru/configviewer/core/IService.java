package ru.configviewer.core;

import java.util.List;

public interface IService {

	List<LineInfo> getLines(String text);

	String getText(String title);

}