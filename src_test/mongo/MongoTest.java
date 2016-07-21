package mongo;

import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.serotonin.mango.rt.dataImage.PointValueTime;

public class MongoTest {
	
	private Mongo mongo; 
	private DB db;
	
	@Before
	public void init()throws Exception{
		mongo = new Mongo("10.192.206.17:30001");
		db = mongo.getDB("IRBox_20_1");
	}
	
	@Test
	public void testIterate()throws Exception{
		Set<String>cnames = db.getCollectionNames();
		System.out.println(cnames.size());
		for(String cname:cnames){
			if(!cname.startsWith("pointValues_"))continue;
			DBCollection  c = db.getCollection(cname);
			List<Integer> pids = c.distinct("i");
			System.out.println(pids);
			for(int i:pids){
				DBObject query = new BasicDBObject();
				query.put("i", i);
				query.put("t", new BasicDBObject("$gt",1438833600000L));
				long l = System.currentTimeMillis();
				DBCursor cursor = c.find(query).limit(4320);
				int n=0;
				String s = buildRSP(cursor.toArray());
				System.out.println(s);
				long time = System.currentTimeMillis()-l;
				System.out.println("query point:"+i+",count:"+n+" , time:"+time);
				Thread.sleep(1000);
			}
			break;
		}
	}
	private String buildRSP(List<DBObject> points){
		StringBuilder sb = new StringBuilder();
		String [] arr = new String[4320];
		long stime = 1438833600000L;
		for(DBObject p:points){
			long time = (Long)p.get("t");
			int i = (int) ((time-stime)/20000);
			arr[i] = p.get("v")+"";
		}
		sb.append("\"MSGBODY\":{\"STIME\":"+stime+",\"POINTS\":[");
		for(String v:arr){
			String s = v!=null?v:""+0;;
			sb.append(s+",");
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append("]}");
		return sb.toString();
	}
}
