package ru.codeanalyzer.interfaces;

public interface ICData {

	public enum Type{
		Config, Document, Catalog, Enum, Report, CommonModules, DataProcessor, NotDefined, NotObject;
		
		public String getCaption(){
			switch (this) {
			case Catalog:
				return "����������";
			case Document:
				return "��������";
			case Enum:
				return "������������";
			case Report:
				return "�����";
			case CommonModules:
				return "����� ������";
			case DataProcessor:
				return "���������";
			default:
				return "";
			}	
		}
	}
	
	public abstract void setText(String text);
	
	public abstract Type getType();

	public abstract boolean needLoading();

	public abstract String getName();

}