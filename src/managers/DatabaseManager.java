package managers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JOptionPane;

import org.json.simple.JSONObject;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstPaths;
import consts.ConstStrings;
import objects.Car;
import objects.Customer;
import objects.CustomerBusiness;
import objects.CustomerInd;
import objects.Invoice;
import objects.Item;
import utility.Logger;

public class DatabaseManager {
	private ConstDB cdb;	
	private Logger  log;
	private String date;
	private ConstNums cn;
	private ConstStrings cs;
	private DecimalFormat df;
	private ConstPaths cp;
	private JSONObject js;
	
	
	public DatabaseManager (Logger logger, String date, ConstDB cdbm, ConstNums cn, ConstStrings cs, ConstPaths CP, 
			JSONObject JS,DecimalFormat df) throws IOException {
		this.cdb = cdbm;
		this.cs = cs;
		this.cn = cn;
		this.cp = CP;
		this.log = logger;
		this.df = df;
		this.date = date;	
		this.js = JS;
//		this.checkIfDatabaseExists();
	}
		
	public void checkIfDatabaseExists() throws IOException {
		Connection con = this.connect();
		this.executeQuery(con, cdb.CREATE_BRANDS_TABLE);
		this.executeQuery(con, cdb.CREATE_BUSINESS_TABLE);
		this.executeQuery(con, cdb.CREATE_CAR_TABLE);
		this.executeQuery(con, cdb.CREATE_CUSTOMER_TABLE);
		this.executeQuery(con, cdb.CREATE_INVOICE_TABLE);
		this.executeQuery(con, cdb.CREATE_REPAK_REPORT_TABLE);
		this.executeQuery(con, cdb.CREATE_SETTINGS_TABLE);
		this.executeQuery(con, cdb.CREATE_STOCK_TABLE);
		this.executeQuery(con, cdb.POPULATE_BRANDS);
		this.executeQuery(con, cdb.ALTER_CAR_TABLE);
		this.executeQuery(con, cdb.ALTER_CUSTOMER_TABLE);
	}

	private Connection connect() {
		try {
			Class.forName(cdb.JDBC_DRIVER);
//			Connection conn = DriverManager.getConnection(cdb.DB_URL, cdb.USER, cdb.PASS);
			Connection conn = DriverManager.getConnection(js.get(cs.DB_URL).toString(), js.get(cs.DB_USER).toString(), js.get(cs.DB_PASS).toString());

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
			log.logError(" "+this.getClass().getName()+"\tCLOSE E\t"+e3.getMessage());
		}
	}

	private boolean executeQuery(Connection dbConnection, String q) {
	    boolean bCreatedTables = false;
	    Statement statement = null;
	    try {
	        statement = dbConnection.createStatement();
	        statement.execute(q);
	        bCreatedTables = true;
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	    
	    return bCreatedTables;
	}

	public boolean addNewRecord(String query) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			if(conn == null || conn.isClosed())
				conn = this.connect();
		} catch (SQLException e) {
			log.logError(" 1st "+this.getClass().getName()+"\tADD NEW RECORD E1\t"+e.getMessage());
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
			log.logError(" "+this.getClass().getName()+"\tADD NEW RECORD E2\t"+e11.getMessage());
		}	finally {
			try{
				this.close(rs, pst, conn);
			} catch (Exception e3){
				e3.printStackTrace();
				log.logError(" "+this.getClass().getName()+"\tADD NEW RECORD E3\t"+e3.getMessage());
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
			log.logError(" 1st "+this.getClass().getName()+"\tUPDATE RECORD E1\t"+e.getMessage());
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
			log.logError(" "+this.getClass().getName()+"\tUPDATE RECORD E2\t"+e11.getMessage());
		}	finally {
			try{
				this.close(rs, pst, conn);
			} catch (Exception e3){
				e3.printStackTrace();
				log.logError(" "+this.getClass().getName()+"\tUPDATE RECORD E3\t"+e3.getMessage());
			}
		}
		return false;
	}

	public boolean updateRecord(String tableName, String columnToSet, String valueToSet, String columnToFind, String valueToFind) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;
		String q = this.cdb.UPDATE + "`"+tableName+"`" + this.cdb.SET + "`"+columnToSet+"`"+this.cdb.EQUAL+"'"+valueToSet+"'" 
				+ this.cdb.WHERE + "`"+ConstDB.TableNames.TB_BUSINESS.getName()+"`.`"+columnToFind+"` = '"+valueToFind+"'";
//		System.out.println(q);
		try {
			if(conn == null || conn.isClosed())
				conn = this.connect();
		} catch (SQLException e) {
			log.logError(" 1st "+this.getClass().getName()+"\tUPDATE RECORD 5ST E1\t"+e.getMessage());
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
			log.logError(" "+this.getClass().getName()+"\tUPDATE RECORD 5ST E2\t"+e11.getMessage());
		}	finally {
			try{
				this.close(rs, pst, conn);
			} catch (Exception e3){
				e3.printStackTrace();
				log.logError(" "+this.getClass().getName()+"\tUPDATE RECORD 5ST E3\t"+e3.getMessage());
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
			log.logError(" 1st "+this.getClass().getName()+"\tSELECT DATA ARRAYLIST [E]\t"+e.getMessage());
		}
		
		try {
			pst = conn.prepareStatement(q);
			rs = pst.executeQuery();
			
			if(q.contains(ConstDB.TableNames.TB_STOCK.getName())){
				list = populateItemList(rs, (ArrayList<Item>) list);
			} else if(q.contains(ConstDB.TableNames.TB_INVOICES.getName())) {
				list = populateInvoiceList(rs, (ArrayList<Invoice>) list);
			} else if(q.contains(ConstDB.TableNames.TB_CUSTOMERS.getName()) || q.contains(ConstDB.TableNames.TB_BUSINESS.getName())) {
				boolean isIndividual;
				if(q.contains(ConstDB.TableNames.TB_CUSTOMERS.getName()))
					isIndividual = true;
				else isIndividual = false;
				list =  populateCustomersList(rs, (ArrayList<Customer>)list, isIndividual);
			} else {
				list =  populateStringList(rs, (ArrayList<String>)list);
			}
				
			
		} catch (SQLException e2) {
			log.logError(" "+this.getClass().getName()+"\tSELECT DATA ARRAYLIST [E2] \t"+e2.getMessage());
		} finally {
			try{
				this.close(rs, pst, conn);
			} catch (Exception e3){
				log.logError(" "+this.getClass().getName()+"\tSELECT DATA ARRAYLIST [E3]\t"+e3.getMessage());
			}
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
			log.logError(" 1st "+this.getClass().getName()+"\tSELECT DATA MAP [E1]\t"+e.getMessage());
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
			log.logError(" "+this.getClass().getName()+"\tSELECT DATA MAP [E2] \t"+e.getMessage());
		} finally {
			try{
				this.close(rs, pst, conn);
			} catch (Exception e){
				log.logError(" "+this.getClass().getName()+"\tSELECT DATA MAP [E3]\t"+e.getMessage());
			}
		}
		return toReturn;
	}
		
//	 TODO
//	retrieve one record
	public String selectData(String q) {
		String toReturn = "";
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			if(conn == null || conn.isClosed())
				conn = this.connect();
		} catch (SQLException e) {
			log.logError(" 1st "+this.getClass().getName()+"\tSELECT DATA ONE STRING [E1]\t"+e.getMessage());
		}

		try {
			pst = conn.prepareStatement(q);
			rs = pst.executeQuery();		
			while(rs.next()){
				toReturn = rs.getString(1);
			}
		} catch (SQLException e) {
			log.logError(" "+this.getClass().getName()+"\tSELECT DATA ONE STRING [E2] \t"+e.getMessage());
		} finally {
			try{
				this.close(rs, pst, conn);
			} catch (Exception e){
				log.logError(" "+this.getClass().getName()+"\tSELECT DATA ONE STRING [E3]\t"+e.getMessage());
			}
		}
		return toReturn;
	}

	public boolean deleteRecord(String q) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			if(conn == null || conn.isClosed())
				conn = this.connect();
		} catch (SQLException e) {
			log.logError(" 1st "+this.getClass().getName()+"\tDELETE RECORD E1\t"+e.getMessage());
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
			log.logError(" "+this.getClass().getName()+"\tDELETE RECORD E2\t"+e11.getMessage());
		}	finally {
			try{
				this.close(rs, pst, conn);
			} catch (Exception e3){
				e3.printStackTrace();
				log.logError(" "+this.getClass().getName()+"\tDELETE RECORD E3\t"+e3.getMessage());
			}
		}
		
		return false;
	}
	
	private ArrayList<Customer> populateCustomersList(ResultSet rs, ArrayList<Customer> list, boolean isIndividual) {
		ResultSetMetaData rsmd;
		try {
			rsmd = rs.getMetaData();
			int colNum = rsmd.getColumnCount();
			while(rs.next()){
				Customer c = null;
				if(isIndividual) c = cretateCustomer(rs, colNum);
				else c = createBusiness(rs, colNum);
				
				list.add(c);
			}
		} catch (SQLException e) {
			log.logError(" "+this.getClass().getName()+"\tPOPULATE CUSTOMER LIST E\t"+e.getMessage());
			e.printStackTrace();
		}
		return list;
	}

	private Customer createBusiness(ResultSet rs, int colNum) throws SQLException {
		int id = 0, noOfServices = 0;
		String vatTax = "", name = "", address = "", cars = "";
		for(int i = 1; i <= colNum; i++){
//			if(!rs.getString(i).isEmpty()) {
				switch (i) {
				case 1:
					id = rs.getInt(i);
					break;
				case 2:
					vatTax = rs.getString(i);
					break;
				case 3:
					name= rs.getString(i);
					break;
				case 4:
					address = rs.getString(i);
					break;
				case 5:
					cars = rs.getString(i);
					break;
				case 6:
					noOfServices = rs.getInt(i);
					break;
	
				default:
					break;
				}
//			}
		}
		return new CustomerBusiness(this, this.cdb, this.cn, this.cs, ""+id, noOfServices, vatTax, name, address, cars);
	}

	private Customer cretateCustomer(ResultSet rs, int colNum) throws SQLException {
		int id = 0, carID = 0, noOfServices = 0;
		for(int i = 1; i <= colNum; i++){
//			if(rs.getInt(i) >= 0)
			switch (i) {
			case 1:
				id = rs.getInt(i);
				break;
			case 2:
				carID = rs.getInt(i);
				break;
			case 3:
				noOfServices = rs.getInt(i);
				break;

			default:
				break;
			}
		}
		return new CustomerInd(this, this.cdb, this.cn, this.cs, id, noOfServices, carID);
	}

	private ArrayList<String> populateStringList(ResultSet rs, ArrayList<String> list) throws SQLException {
		while(rs.next()){
			String s = rs.getString(1);
			list.add(s);
		}
		return list;
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
			log.logError(" "+this.getClass().getName()+"\tPOPULATE ITEM LIST E\t"+e.getMessage());
			e.printStackTrace();
		}
		return list;
	}

	private Item cretateItem(ResultSet rs, int colNum) throws NumberFormatException, SQLException {
		String  code = "", itName = "";
		double cost = 0, price = 0;
		int id = 0, qnt = 0, addTransport = 0, addVat = 0, addVEMCCharge = 0;
		for(int i = 1 ; i <= colNum; i++){
//			System.out.println(i + " "+ rs.getString(i));
			if(!rs.getString(i).isEmpty()) {
				switch(i){
				case 1:
					id = rs.getInt(i);
					break;
				case 2:
					code = rs.getString(i);
					break;
				case 3:
					itName = rs.getString(i);
					break;
				case 4:
					cost = Double.parseDouble(rs.getString(i));
					break;
				case 5:
					addVat = rs.getInt(i);
					break;
				case 6:
					addTransport = rs.getInt(i);
					break;
				case 7:
					addVEMCCharge = rs.getInt(i);
					break;
				case 8:
					price = Double.parseDouble(rs.getString(i));
					break;
				case 9:
					qnt = Integer.parseInt(rs.getString(i));
					break;
				}
			}
		}
		return new Item(this, this.cdb, this.cn, this.cs, this.df, id, code, itName, cost, price, addVat, addTransport, addVEMCCharge, qnt);	
	}

	private ArrayList<?> populateInvoiceList(ResultSet rs, ArrayList<Invoice> list) {
		ResultSetMetaData rsmd;
		try {
			rsmd = rs.getMetaData();
			int colNum = rsmd.getColumnCount();
			while(rs.next()){
				Invoice i = cretateInvoice(rs, colNum);
				list.add(i);
			}
		} catch (SQLException e) {
			log.logError(" "+this.getClass().getName()+"\tPOPULATE ITEM LIST E\t"+e.getMessage());
			e.printStackTrace();
		}
		return list;
	}

	private Invoice cretateInvoice(ResultSet rs, int colNum) throws SQLException {
		int id = 0;
		double discount = 0, total = 0;
		boolean isPercent = false, isBusiness = false;
		String customerID = "", codeList = "", date = "", fileName = "";
		for(int i = 1 ; i <= colNum; i++){
//			System.out.println(i + " "+ rs.getString(i));
			if(!rs.getString(i).isEmpty()) {
				switch(i){
				case 1:
					id = rs.getInt(i);
					break;
				case 2:
					customerID = rs.getString(i);
					if(customerID.contains(this.cs.CUST_BUS_CODE))
						isBusiness = true;
					break;
				case 3:
					codeList = rs.getString(i);
					break;
				case 4:
					discount = Double.parseDouble(rs.getString(i));
					break;
				case 5:
					byte t = rs.getByte(i);
					if(t == 1)
						isPercent = true;
					break;
				case 6:
					total = Double.parseDouble(rs.getString(i));
					break;
				case 7:
					date = rs.getString(i);
					break;
				case 8:
					fileName = rs.getString(i);
					break;
				}
			}
		}return new Invoice(this, cdb, cs, cn, id, customerID, isBusiness, codeList, discount, isPercent, total, date, fileName);
	}

	public Object getObject(int id, String str) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			if(conn == null || conn.isClosed())
				conn = this.connect();
		} catch (SQLException e) {
			log.logError(" 1st "+this.getClass().getName()+"\tSELECT DATA ARRAYLIST [E]\t"+e.getMessage());
		}
		String q;
		if(str.equals(this.cs.CAR))
			q = this.cdb.SELECT_CAR + id;
		else q = "";
		try {
			pst = conn.prepareStatement(q);
			rs = pst.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int colNum = rsmd.getColumnCount();   
			while(rs.next()){
				return this.createCar(rs, colNum);
			}

			
		} catch (SQLException e2) {
			log.logError(" "+this.getClass().getName()+"\tSELECT DATA ARRAYLIST [E2] \t"+e2.getMessage());
		} finally {
			try{
				this.close(rs, pst, conn);
			} catch (Exception e3){
				log.logError(" "+this.getClass().getName()+"\tSELECT DATA ARRAYLIST [E3]\t"+e3.getMessage());
			}
		}
		return null;
	}

	private Car createCar(ResultSet rs, int colNum) throws SQLException {
		String registration = "";
		int id = 0, brand = 0;
		for(int i = 1 ; i <= colNum; i++){
			switch (i) {
			case 1:
				id = rs.getInt(i);
				break;
			case 2:
				registration = rs.getString(i);
				break;
			case 3:
				brand = rs.getInt(i);
				break;

			default:
				break;
			}
		}
		return  new Car(this, cdb, cs, registration, id, brand);
	}
}
