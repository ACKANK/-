package moogodb;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MongoDBJDBC {

	public static void main(String args[]) {
		Stock stock = new Stock();
		stock.setSname("cn_300228");
		stock.setCollection("test1");
		stock.setDatabase("mydata");
		stock.setStartday("20161030");
		stock.setEndday("20161224");
		MongoDBJDBC mongo = new MongoDBJDBC();
		mongo.readurldata(stock);
		mongo.insertdata(stock);

		

	}
	//bString用来储存从API中读取并经过处理的数据
		String[] bStrings;

		// 从搜孤读取数据，存储在bStrings字符串数组里
		public String[] readurldata(Stock stock) {
			URL ur = null;
			
			String[] a = null;
			int i = 5;
			int j = 0;
			try {
				String ss = stock.getSname();
				//读取固定时间20160101到20160401内某只股票的数据
				String url = "http://q.stock.sohu.com/hisHq?code='" + ss + "'&start=20130930&end=20131231&stat=1&order=D&period=d&callback=historySearchHandler&rt=jsonp";
				ur = new URL(url);
				BufferedReader reader = new BufferedReader(new InputStreamReader(ur.openStream(), "utf-8"));
				//line中存储未经处理的原始数据
				String line;
				line = reader.readLine();
				//line中存储未经处理的原始数据
				a = line.split("\"");
				bStrings = new String[a.length];

				//在a的基础上挑出需要的数据存到bString中
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

		// 将bStrings数组里的数据插入数据库
		public void insertdata(Stock stock) {
			try {
				// 连接到 mongodb 服务
				MongoClient mongoClient = new MongoClient("localhost", 27017);

				// 连接到数据库
				MongoDatabase mongoDatabase = mongoClient.getDatabase(stock.getDatabase());
				//用输入的集合名创建集合
				mongoDatabase.createCollection(stock.getCollection());
				MongoCollection<Document> collection = mongoDatabase.getCollection(stock.getCollection());
				int b = 0;
				for (int i = 0; i < bStrings.length / 2 - 10; i = i + 10, b = b + 10) {
					Document document = new Document("日期", bStrings[b]).append("开盘价 ", bStrings[b + 1])
							.append("收盘价", bStrings[b + 2]).append("涨跌额", bStrings[b + 3]).append("涨跌幅度", bStrings[b + 4])
							.append("最低价", bStrings[b + 5]).append("最高价", bStrings[b + 6]).append("成交量", bStrings[b + 7])
							.append("成交金额", bStrings[b + 8]).append("转手率", bStrings[b + 9]);
					List<Document> documents = new ArrayList<Document>();
					documents.add(document);
					collection.insertMany(documents);

				}
			} catch (Exception e) {
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
			}
		}


	// 从数据库中读取数据，数据返回到stock对象中的data里，结构为二维字符串数组
	public Stock readdata(Stock stock) {
		int a = 0;
		int b = 0;
		String abc = null;
		String[][] abcd = new String[50][30];
		int i = 0;
		// 连接到 mongodb 服务
		MongoClient mongoClient = new MongoClient("localhost", 27017);

		// 连接到数据库
		MongoDatabase mongoDatabase = mongoClient.getDatabase(stock.getDatabase());
		// 选择集合test
		try {
			MongoCollection<Document> collection = mongoDatabase.getCollection(stock.getCollection());
			FindIterable<Document> findIterable = collection.find();
			MongoCursor<Document> mongoCursor = findIterable.iterator();
			// 从数据库中读取数据，并转成字符串数组存储，split完成分割字符串任务
			while (mongoCursor.hasNext()) {
				Object o = mongoCursor.next();
				abc = String.valueOf(o);
				abcd[i] = abc.split("=|,|}");
				i++;
			}

			// aaa用来存储整理好的数据
			String[][] aaa = new String[35][10];
			// 3579

			for (a = 0; a < 35; a++) {
				aaa[a][0] = abcd[a][3];
				aaa[a][1] = abcd[a][5];
				aaa[a][2] = abcd[a][7];
				aaa[a][3] = abcd[a][9];
				aaa[a][4] = abcd[a][11];
				aaa[a][5] = abcd[a][13];
				aaa[a][6] = abcd[a][15];
				aaa[a][7] = abcd[a][17];
				aaa[a][8] = abcd[a][19];
				aaa[a][9] = abcd[a][21];
			}
			// System.out.println(aaa[0][0]);
			stock.setData(aaa);
			return stock;
		} catch (Exception e) {
			return stock;

		}

	}

	// 查询数据方法
	public Stock finddata(Stock stock) {

		// 从数据库中读出二维数组
		String startday;
		String endday;
		startday = stock.getStartday();
		endday = stock.getEndday();
		readdata(stock);
		String[][] abc = new String[35][10];
		abc = stock.getData();
		// 如果输入的日期在数据库中，从数据库中查询
		if ((abc[0][0]!=null)&&(endday.compareTo(abc[0][0]) <= 0) && (startday.compareTo(abc[35][0]) >= 0))

		{
			// System.out.println(abc[35][0]);
			int a, b, c, d;
			// 检查查询的数据在数据库中的位置，a，b用来记录数据段的开始与结束位置
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

	// 查询专用方法，无需调用，调用查询自动调用
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
			String[][] abc = new String[aaa.length / 10][10];

			for (i = 0, k = 0; i < aaa.length / 10; i++) {
				for (j = 0; j < 10; j++, k++) {
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
