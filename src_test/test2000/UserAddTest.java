package test2000;

import org.junit.Before;
import org.junit.Test;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.serotonin.mango.db.dao.UserDao;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.vo.scope.ScopeVO;

public class UserAddTest {
	private UserDao userDao;

	@Before
	public void init(){
		SQLServerDataSource ds = new SQLServerDataSource();
		String host = "192.168.1.117";
		String user = "sa";
		String password = "asus_admin1";
		ds.setURL("jdbc:sqlserver://"+host+":1433; DatabaseName=LssclDB");
		ds.setUser(user);
		ds.setPassword(password);
		userDao = new UserDao(ds);
	}
	/**
	 * 添加两千用户
	 * @throws Exception
	 */
	@Test
	public void add2000()throws Exception{
		User user = userDao.getUser("yxx");
		System.out.println(user.getPassword());
		ScopeVO scope = new ScopeVO();
		scope.setId(1);
		scope.setScopetype(0);
		user.setHomeScope(scope);
		long phone = 15067110000L;
		for(int i=0;i<2000;i++){
			user.setUsername("yxx-"+i);
			phone++;
			user.setPhone(phone+"");
			userDao.insertUser(user);
		}
	}

}
