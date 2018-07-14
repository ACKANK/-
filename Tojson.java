package moogodb;

public class Tojson {

/*	public static void main(String[] args) {
		Tojson tojson=new Tojson();
		Stock stock=new Stock();
		stock.setSname("cn_300228");
		stock.setCollection("test");
		stock.setDatabase("mydata");
		stock.setEndday("20161224");
		stock.setStartday("20161030");
		MongoDBJDBC mongo = new MongoDBJDBC();
		mongo.finddata(stock);
		tojson.tojson(stock);
	}
	*/
	
	public  void tojson(Stock stock)
	{

		String[][] abc=stock.data;
/*		for(int a=0;a<60;a++) {
			for(int b=0;b<10;b++) {
				System.out.println(abc[a][b]);
			}
		}*/
		StringBuilder stringBuilder=new StringBuilder();
		int i=0;
		stringBuilder.append("'{\"FT\":[");
		for(i=0;;i++)
		{
				
				stringBuilder.append("{\"time\":\"");
				stringBuilder.append(abc[i][0]);
				stringBuilder.append(" 00:00:00.000");
				stringBuilder.append("\",\"topen\":\"");
				stringBuilder.append(abc[i][1]);
				stringBuilder.append("\",\"tclose\":\"");
				stringBuilder.append(abc[i][2]);
				stringBuilder.append("\",\"minclose\":\"");
				stringBuilder.append(abc[i][5]);
				stringBuilder.append("\",\"maxclose\":\"");
				stringBuilder.append(abc[i][6]);
				stringBuilder.append("\",\"qty\":\"");
				stringBuilder.append(abc[i][7]);
				stringBuilder.append("\",\"amount\":\"");
				stringBuilder.append(abc[i][8]);
				stringBuilder.append("\"},");
				if(abc[i+1][0]==null)
				{
					stringBuilder.deleteCharAt(stringBuilder.length()-1);
					stringBuilder.append("],\"status\":0}'");
					break;
				}
				
		}
		System.out.println(stringBuilder.toString());

		
		
	}
	
}



