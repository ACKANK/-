package moogodb;

public class Stock {
	String sname;
	String[][] data;
	String database;
	String collection;
	String startday;
	String endday;

	String[] aStrings;

	Stock() {
		// ��Ʊ����
		sname = null;
		// ����
		data = new String[100][10];
		// ���ݿ�����
		database = null;
		// ������
		collection = null;
		// ��ʼ����
		startday = null;
		// ��������
		endday = null;
		// ��ת�ַ���
		aStrings=null;
	}




	public String[] getaStrings() {
		return aStrings;
	}

	public void setaStrings(String[] aStrings) {
		this.aStrings = aStrings;
	}

	public String getStartday() {
		return startday;
	}

	public void setStartday(String startday) {
		this.startday = startday;
	}

	public String getEndday() {
		return endday;
	}

	public void setEndday(String endday) {
		this.endday = endday;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String[][] getData() {
		return data;
	}

	public void setData(String[][] data) {
		this.data = data;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}
}
