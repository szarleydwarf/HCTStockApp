package managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstStrings;
import objects.Item;
import utility.Logger;

public class DatabaseManager {
	private ConstDB cdb;	
	private Logger  log;
	private String date;
	private ConstNums cn;
	private ConstStrings cs;
	
	
	public DatabaseManager (Logger logger, String date, ConstDB cdbm, ConstNums cn, ConstStrings cs) {
		this.cdb = cdbm;
		this.cs = cs;
		this.cn = cn;
		this.log = logger;
		this.date = date;		
	}
		
	private Connection connect() {
		try {
			Class.forName(cdb.JDBC_DRIVER);
			Connection conn = DriverManager.getConnection(cdb.DB_URL, cdb.USER, cdb.PASS);
//			JOptionPane.showMessageDialog(null, "Connected!");
			return conn;
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Connection Error: "+ex.getMessage());
			log.logError(date+" "+this.getClass().getName()+"\t"+ex.getMessage());
			return null;		
		}		
	}
	
	private void close(ResultSet rs,PreparedStatement pst, Connection conn) {
		try{
            if (rs != null) {
                 rs.close();
             }
             if (pst != null) {
                 pst.close();
             }
             if (conn != null) {
                 conn.close();
             }
		} catch (Exception e3){
			log.logError(date+" "+this.getClass().getName()+"\tCLOSE E3\t"+e3.getMessage());
		}
	}


	public boolean addNewRecord(String query) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			if(conn == null || conn.isClosed())
				conn = this.connect();
		} catch (SQLException e) {
			log.logError(date+" 1st "+this.getClass().getName()+"\tAdd New Record E\t"+e.getMessage());
		}
			
		try {
			pst = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			int inserted = pst.executeUpdate();
			
			rs = pst.getGeneratedKeys();
			if(inserted == 1)
				return true;
			else
				return false;

		} catch (SQLException e11) {
			e11.printStackTrace();
			log.logError(date+" "+this.getClass().getName()+"\tAdd New Record E11\t"+e11.getMessage());
		}	finally {
			try{
				this.close(rs, pst, conn);
			} catch (Exception e3){
				e3.printStackTrace();
				log.logError(date+" "+this.getClass().getName()+"\tAdd New Record E3\t"+e3.getMessage());
			}
		}
		
		return false;
	}
	
//	TODO
//	add new record	
	public void addNewRecord(String table, ArrayList<?> list) {
		// TODO Auto-generated method stub
		
	}

//	TODO
//	edit record
	public boolean updateRecord(String q) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;
//		System.out.println(q);
		try {
			if(conn == null || conn.isClosed())
				conn = this.connect();
		} catch (SQLException e) {
			log.logError(date+" 1st "+this.getClass().getName()+"\tUpdate Record E\t"+e.getMessage());
		}
			
		try {
			pst = conn.prepareStatement(q, Statement.RETURN_GENERATED_KEYS);
			int updated = pst.executeUpdate();
			
			rs = pst.getGeneratedKeys();
			if(updated == 1)
				return true;
			else
				return false;

		} catch (SQLException e11) {
			e11.printStackTrace();
			log.logError(date+" "+this.getClass().getName()+"\tUpdate Record E11\t"+e11.getMessage());
		}	finally {
			try{
				this.close(rs, pst, conn);
			} catch (Exception e3){
				e3.printStackTrace();
				log.logError(date+" "+this.getClass().getName()+"\tUpdate Record E3\t"+e3.getMessage());
			}
		}
		return false;
	}

//	 TODO
//	retrieve list of records
	public ArrayList<?> selectData(String q, ArrayList<?> list){
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			if(conn == null || conn.isClosed())
				conn = this.connect();
		} catch (SQLException e) {
			log.logError(date+" 1st "+this.getClass().getName()+"\tSELECT DATA [E]\t"+e.getMessage());
		}
		
		try {
			pst = conn.prepareStatement(q);
			rs = pst.executeQuery();
			
			if(q.contains(ConstDB.TableNames.TB_STOCK.getName()))
				list = populateItemList(rs, (ArrayList<Item>) list);
			else {
				list =  populateStringList(rs, (ArrayList<String>)list);
			}
				
			
		} catch (SQLException e2) {
			log.logError(date+" "+this.getClass().getName()+"\tSELECT DATA [E2] \t"+e2.getMessage());
		} finally {
			try{
				this.close(rs, pst, conn);
			} catch (Exception e3){
				log.logError(date+" "+this.getClass().getName()+"\tSELECT DATA [E3]\t"+e3.getMessage());
			}
		}
		return list;
	}

	
	private ArrayList<?> populateStringList(ResultSet rs, ArrayList<String> list) throws SQLException {
		while(rs.next()){
			String s = rs.getString(1);
			list.add(s);
		}
		return list;
	}

	public Map<String, String> selectDataMap(String q) {
		Map<String, String> toReturn = new HashMap<String, String>();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			if(conn == null || conn.isClosed())
				conn = this.connect();
		} catch (SQLException e) {
			log.logError(date+" 1st "+this.getClass().getName()+"\tSELECT DATA MAP [E1]\t"+e.getMessage());
		}

		try {
			pst = conn.prepareStatement(q);
			rs = pst.executeQuery();		
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();   
			
			while(rs.next()) {			
				for(int i = 0 ; i <= columnsNumber; i++){
					if(i % 2 != 0){
						toReturn.put(rs.getString(i), rs.getString(i+1));
					}
				}        
			}
		} catch (SQLException e) {
			log.logError(date+" "+this.getClass().getName()+"\tSELECT DATA [E2] \t"+e.getMessage());
		} finally {
			try{
				this.close(rs, pst, conn);
			} catch (Exception e){
				log.logError(date+" "+this.getClass().getName()+"\tSELECT DATA MAP [E3]\t"+e.getMessage());
			}
		}
		return toReturn;
	}
		
//	 TODO
//	retrieve one record
	
//	 TODO
//	delete record
	public boolean deleteRecord(String q) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;
		System.out.println("DBM Delete " + q);
		try {
			if(conn == null || conn.isClosed())
				conn = this.connect();
		} catch (SQLException e) {
			log.logError(date+" 1st "+this.getClass().getName()+"\tDelete Record E\t"+e.getMessage());
		}
			
		try {
			pst = conn.prepareStatement(q, Statement.RETURN_GENERATED_KEYS);
			int inserted = pst.executeUpdate();
			
			rs = pst.getGeneratedKeys();
			if(inserted == 1)
				return true;
			else
				return false;

		} catch (SQLException e11) {
			e11.printStackTrace();
			log.logError(date+" "+this.getClass().getName()+"\ttDelete Record E11\t"+e11.getMessage());
		}	finally {
			try{
				this.close(rs, pst, conn);
			} catch (Exception e3){
				e3.printStackTrace();
				log.logError(date+" "+this.getClass().getName()+"\ttDelete Record E3\t"+e3.getMessage());
			}
		}
		
		return false;
	}
	
	
	public ArrayList<Item> populateItemList(ResultSet rs, ArrayList<Item> list) {

		ResultSetMetaData rsmd;
		try {
			rsmd = rs.getMetaData();
			int colNum = rsmd.getColumnCount();
			while(rs.next()){
				Item i = cretateItem(rs, colNum);
				list.add(i);
			}
		} catch (SQLException e) {
			log.logError(date+" "+this.getClass().getName()+"\tPOPULATE ITEM LIST E\t"+e.getMessage());
			e.printStackTrace();
		}
		return list;
	}

	private Item cretateItem(ResultSet rs, int colNum) throws NumberFormatException, SQLException {
		String  itID = "", itName = "";
		double cost = 0, price = 0;
		int qnt = 0, addTransport = 0, addVat = 0;
		for(int i = 1 ; i <= colNum; i++){
//			System.out.println(i + " "+ rs.getString(i));
			if(!rs.getString(i).isEmpty()) {
				switch(i){
				case 1:
					itID = rs.getString(i);
					break;
				case 2:
					itName = rs.getString(i);
					break;
				case 3:
					cost = Double.parseDouble(rs.getString(i));
					break;
				case 4:
					addVat = rs.getInt(i);
					break;
				case 5:
					addTransport = rs.getInt(i);
					break;
				case 6:
					price = Double.parseDouble(rs.getString(i));
					break;
				case 7:
					qnt = Integer.parseInt(rs.getString(i));
					break;
				}
			}
		}
		/*DatabaseManager dm, ConstDB cdb, ConstNums ci, ConstStrings cs, String p_stock_number, 
		 * String p_name, double p_cost, double p_price, int addVat, int addTransportCost, int qnt*/
		return new Item(this, this.cdb, this.cn, this.cs, itID, itName, cost, price, addVat, addTransport, qnt);	
	}





}
