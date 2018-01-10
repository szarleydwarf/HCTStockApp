package managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import consts.ConstDB;

public class DatabaseManager {
	private ConstDB cdb;	
	private String date;
	
	public DatabaseManager (String p_loggerFolderPath, ConstDB cdb) {
		this.cdb = cdb;
	}
	
	
	private Connection connect() {
		try {
			Class.forName(cdb.JDBC_DRIVER);
			Connection conn = DriverManager.getConnection(cdb.DB_URL, cdb.USER, cdb.PASS);
			JOptionPane.showMessageDialog(null, "Connected!");
			return conn;
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Error: "+ex.getMessage());
//			log.logError(date+" "+this.getClass().getName()+"\t"+ex.getMessage());
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
//			log.logError(date+" "+this.getClass().getName()+"\tCLOSE E3\t"+e3.getMessage());
		}
	}

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
//			log.logError(date+" "+this.getClass().getName()+"\tGET ITEMS LIST\tE1 "+e1.getMessage());
		} finally {
			try{
				this.close(rs, pst, conn);
			} catch (Exception e2){
				e2.printStackTrace();
//				log.logError(date+" "+this.getClass().getName()+"\tGET ITEMS LIST\tE2 "+e2.getMessage());
			}
		}
		return list;
	}

}
