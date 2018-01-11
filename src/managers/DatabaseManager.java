package managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import consts.ConstDB;
import utility.Logger;

public class DatabaseManager {
	private ConstDB cdb;	
	private Logger  log;
	private String date;
	
	public DatabaseManager (Logger logger, String date, ConstDB cdb) {
		this.cdb = cdb;
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

//	TODO
//	add new record
	
//	TODO
//	edit record
	
//	 TODO
//	retrieve list of records
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
	
	public ArrayList<String> getList(String q) {
		Connection conn = this.connect();
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList<String> list = new ArrayList<String>();
		System.out.println(q);
		try {
			pst = conn.prepareStatement(q);		
			
			rs = pst.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			System.out.println("cnv "+columnsNumber);
			while (rs.next()){
				for(int i = 1; i <= columnsNumber; i++)
					list.add(rs.getString(i));
				
			}
		} catch (SQLException e1) {
			log.logError(date+" "+this.getClass().getName()+"\tGET ITEMS LIST\tE1 "+e1.getMessage());
		} finally {
			try{
				this.close(rs, pst, conn);
			} catch (Exception e2){
				e2.printStackTrace();
				log.logError(date+" "+this.getClass().getName()+"\tGET ITEMS LIST\tE2 "+e2.getMessage());
			}
		}
		return list;
	}


	

}
