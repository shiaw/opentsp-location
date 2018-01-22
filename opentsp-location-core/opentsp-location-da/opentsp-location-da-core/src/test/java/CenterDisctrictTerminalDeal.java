import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class CenterDisctrictTerminalDeal {
	static Connection centerConn;
	static Connection districtConn;
	static String centerUrl = "jdbc:mysql://172.16.1.227:3306/zhlc?"
			+ "user=root&password=root&useUnicode=true&characterEncoding=UTF8";


	static String districtUrl = "jdbc:mysql://172.16.1.219:3306/zhlc?"
			+ "user=root&password=root&useUnicode=true&characterEncoding=UTF8";
	static List<String> centerData = new ArrayList<String>();
	static List<String> districtData = new ArrayList<String>();
	static File f = new File("c:/out.txt");
	static void print(String s) {
		if(!f.exists())
			try {
				f.createNewFile();
				PrintWriter out = new PrintWriter(f);
				out.println(s);
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			// 一个Connection代表一个数据库连接
			centerConn = DriverManager.getConnection(centerUrl);
			districtConn = DriverManager.getConnection(districtUrl);
			System.out.println(centerConn);
			String sql = "select TERMINAL_ID,DEVICE_ID,CHANGE_TID from LC_TERMINAL_INFO c where c.DATA_STATUS =1";
			java.sql.PreparedStatement pCneter = centerConn.prepareStatement(sql);
			java.sql.PreparedStatement pDistrict = districtConn.prepareStatement(sql);

			ResultSet rCenter = pCneter.executeQuery();
			ResultSet rDistrict = pDistrict.executeQuery();

			while(rCenter.next()) {
				long tId = rCenter.getLong("TERMINAL_ID");
				String deviceId = rCenter.getString("DEVICE_ID");
				long changeTid = rCenter.getLong("CHANGE_TID");
				String r = ""+tId+","+deviceId+","+changeTid;
				centerData.add(r);
			}

			while(rDistrict.next()) {
				long tId = rDistrict.getLong("TERMINAL_ID");
				String deviceId = rDistrict.getString("DEVICE_ID");
				long changeTid = rDistrict.getLong("CHANGE_TID");
				String r = ""+tId+","+deviceId+","+changeTid;
				districtData.add(r);
			}

			int c = 0; int piPeiCounts=0;
			for(Iterator<String> centerIt = centerData.iterator();centerIt.hasNext();) {
				String s = centerIt.next();
				String arr[] = s.split(",");
				String tId = arr[0];
				String deviceId = arr[1];
				String changeTid = arr[2];
				System.out.println("center-for-counts="+(++c));
				int innerCount=0;
				for(Iterator<String> districtIt = districtData.iterator();districtIt.hasNext();) {//district
					System.out.println("district-for-counts center="+c+",district-counts="+(++innerCount));
					String _s = districtIt.next();
					String _arr[] = _s.split(",");
					String _tId = _arr[0];
					String _deviceId = _arr[1];
					String _changeTid = _arr[2];
					if(tId.equals(_tId) && deviceId.equals(_deviceId) && changeTid.equals(_changeTid)) {
						System.out.println("piPeiCounts,removeCounts="+(++piPeiCounts));
						centerIt.remove();
//						centerData.remove(centerIt);
						centerData.remove(s);

						districtIt.remove();
//						districtData.remove(districtIt);
						districtData.remove(_s);
					}else {
//						print("terminalId="+tId+",deviceId="+deviceId+",changeTid="+changeTid);
//						System.out.println("terminalId="+tId+",deviceId="+deviceId+",changeTid="+changeTid);
					}
				}
			}

			for(Iterator<String> it= centerData.iterator();it.hasNext();) {
				String s = it.next();
				String arr[] = s.split(",");
				String tId = arr[0];
				String deviceId = arr[1];
				String changeTid = arr[2];
				print("terminalId="+tId+",deviceId="+deviceId+",changeTid="+changeTid);
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
