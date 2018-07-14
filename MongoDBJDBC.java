package moogodb;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.el.stream.Stream;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MongoDBJDBC {

	public static void main(String args[]) {
/*
		for (int i = 000001; i < 4001; i++) {
			StringBuffer abc=new StringBuffer();
			abc.append("cn_");
			abc.append(String.format("%6d", i).replace(" ", "0"));
			Stock stock = new Stock();
			stock.setSname(abc.toString());
			stock.setCollection(abc.toString());
			stock.setDatabase("mydata");
			stock.setStartday("20131020");
			stock.setEndday("20131030");
			MongoDBJDBC mongo = new MongoDBJDBC();
			mongo.readurldata(stock);
			mongo.insertdata(stock);
		}*/
		
		Stock stock = new Stock();
		stock.setSname("cn_000001");
		stock.setCollection("cn_000001");
		stock.setDatabase("mydata");
		stock.setStartday("20141010");
		stock.setEndday("20141130");
		MongoDBJDBC mongo = new MongoDBJDBC();
		mongo.finddata(stock);
		String[][] aaa=stock.getData();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 11; j++) {
				System.out.println(aaa[i][j]);
				System.out.println(i+"    "+j);
			}
		}
	}
	//bString���������API�ж�ȡ���������������
		String[] bStrings;

		// ���ѹ¶�ȡ���ݣ��洢��bStrings�ַ���������
		public String[] readurldata(Stock stock) {
			URL ur = null;
			
			String[] a = null;
			int i = 5;
			int j = 0;
			try {
				String ss = stock.getSname();
				//��ȡ�̶�ʱ��20160101��20160401��ĳֻ��Ʊ������
				String url = "http://q.stock.sohu.com/hisHq?code='" + ss + "'&start=20130930&end=20131231&stat=1&order=D&period=d&callback=historySearchHandler&rt=jsonp";
				ur = new URL(url);
				BufferedReader reader = new BufferedReader(new InputStreamReader(ur.openStream(), "utf-8"));
				//line�д洢δ�������ԭʼ����
				String line;
				line = reader.readLine();
				//line�д洢δ�������ԭʼ����
				a = line.split("\"");
				bStrings = new String[a.length];

				//��a�Ļ�����������Ҫ�����ݴ浽bString��
				while (i < a.length) {
					bStrings[j] = a[i];
					i = i + 2;
					j = j + 1;
				}
				return bStrings;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return a;

		}

		// ��bStrings����������ݲ������ݿ�
		public void insertdata(Stock stock) {
			try {
				// ���ӵ� mongodb ����
				MongoClient mongoClient = new MongoClient("localhost", 27017);

				// ���ӵ����ݿ�
				MongoDatabase mongoDatabase = mongoClient.getDatabase(stock.getDatabase());
				//������ļ�������������
				mongoDatabase.createCollection(stock.getCollection());
				MongoCollection<Document> collection = mongoDatabase.getCollection(stock.getCollection());
				int b = 0;
				for (int i = 0; i < bStrings.length / 2 - 10; i = i + 10, b = b + 10) {
					Document document = new Document("��Ʊ����",stock.sname).append("����",bStrings[b]).append("���̼� ", bStrings[b + 1])
							.append("���̼�", bStrings[b + 2]).append("�ǵ���", bStrings[b + 3]).append("�ǵ�����", bStrings[b + 4])
							.append("��ͼ�", bStrings[b + 5]).append("��߼�", bStrings[b + 6]).append("�ɽ���", bStrings[b + 7])
							.append("�ɽ����", bStrings[b + 8]).append("ת����", bStrings[b + 9]);
					List<Document> documents = new ArrayList<Document>();
					documents.add(document);
					collection.insertMany(documents);

				}
			} catch (Exception e) {
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
			}
		}


	// �����ݿ��ж�ȡ���ݣ����ݷ��ص�stock�����е�data��ṹΪ��ά�ַ�������
	public Stock readdata(Stock stock) {
		int a = 0;
		int j=0;
		int i=0;
		String abc = null;
		String[][] abcd = new String[100][22];

		// ���ӵ� mongodb ����
		MongoClient mongoClient = new MongoClient("localhost", 27017);

		// ���ӵ����ݿ�
		MongoDatabase mongoDatabase = mongoClient.getDatabase(stock.getDatabase());
		// ѡ�񼯺�test
		try {
			MongoCollection<Document> collection = mongoDatabase.getCollection(stock.getCollection());
			FindIterable<Document> findIterable = collection.find();
			MongoCursor<Document> mongoCursor = findIterable.iterator();
			// �����ݿ��ж�ȡ���ݣ���ת���ַ�������洢��split��ɷָ��ַ�������

/*			for(i=0;;) {
				
			}*/
			do {
				try {
					Object o = mongoCursor.next();
					abc = String.valueOf(o);
				} catch (Exception e) {
					break;
				}
				abcd[i] = abc.split("=|,|}");
				j++;i++;
			} while (i<100);
			
			// aaa�����洢����õ�����
			String[][] aaa=new String[j][11];	
			for (i = 0; i < j; i++) {
				aaa[i][0]=stock.sname ;
				aaa[i][1] = abcd[i][3];
				aaa[i][2] = abcd[i][5];
				aaa[i][3] = abcd[i][7];
				aaa[i][4] = abcd[i][9];
				aaa[i][5] = abcd[i][11];
				aaa[i][6] = abcd[i][13];
				aaa[i][7] = abcd[i][15];
				aaa[i][8] = abcd[i][17];
				aaa[i][9] = abcd[i][19];
				aaa[i][10] = abcd[i][21];	
			}


			stock.setData(aaa);
			return stock;
		} catch (Exception e) {
			return stock;

		}

	}

	public Stock finddata(Stock stock) {

		// �����ݿ��ж�����ά����
		String startday;
		String endday;
		startday = stock.getStartday();
		endday = stock.getEndday();
		readdata(stock);
		String[][] abc = new String[35][10];
		abc = stock.getData();
		// �����������������ݿ��У������ݿ��в�ѯ
		if ((abc[0][0]!=null)&&(endday.compareTo(abc[0][0]) <= 0) && (startday.compareTo(abc[35][0]) >= 0))

		{
			// System.out.println(abc[35][0]);
			int a, b, c, d;
			// ����ѯ�����������ݿ��е�λ�ã�a��b������¼���ݶεĿ�ʼ�����λ��
			for (a = 0; a < 35; a++) {
				if (endday.compareTo(abc[a][0]) >= 0) {
					break;
				}
			}
			for (b = 35; b > 0; b--) {
				if (startday.compareTo(abc[35][0]) <= 0) {
					break;
				}
			}
			String[][] abcd = new String[b - a + 1][10];
			for (c = 0; c < b - a + 1; c++, a++) {
				for (d = 0; d < 10; d++) {
					abcd[c][d] = abc[a][d];
				}
			}
			stock.setData(abcd);
			return stock;
		} else {
			findfuture(stock);

			return stock;
		}
	}

	// ��ѯר�÷�����������ã����ò�ѯ�Զ�����
	public Stock findfuture(Stock stock) {
		URL ur = null;
		String[] a = null;

		int i = 5;
		int j = 0;
		int k;
		try {
			String ss = stock.sname;
			String startday = stock.getStartday();
			String endday = stock.getEndday();
			String url = "http://q.stock.sohu.com/hisHq?code='" + ss + "'&start=" + startday + "&end=" + endday
					+ "&stat=1&order=D&period=d&callback=historySearchHandler&rt=jsonp";
			ur = new URL(url);
			HttpURLConnection uc = (HttpURLConnection) ur.openConnection();

			BufferedReader reader = new BufferedReader(new InputStreamReader(ur.openStream(), "utf-8"));
			String line;
			line = reader.readLine();
			a = line.split("\"");
			String[] aaa = new String[a.length];
			while (i < a.length - 16) {
				aaa[j] = a[i];
				i = i + 2;
				j = j + 1;
			}
			String[][] abc = new String[aaa.length / 10][11];

			for (i = 0, k = 0; i < aaa.length / 10; i++) {
				for (j = 1; j < 11; j++, k++) {
					abc[i][0]=stock.sname;
					abc[i][j] = aaa[k];
				}
			}
			stock.setData(abc);
			return stock;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return stock;
	}

}
