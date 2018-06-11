package logic;

import java.awt.Color;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstStrings;
import frames.RepakReportFrame;
import managers.CustomersManager;
import managers.DatabaseManager;
import managers.InvoiceManager;
import managers.StockManager;
import objects.Customer;
import objects.CustomerBusiness;
import objects.CustomerInd;
import objects.Invoice;
import objects.Item;
import utility.DateHelper;
import utility.FileHelper;
import utility.Logger;
import utility.MiscHelper;
import utility.PDFCreator;
import utility.Printer;

public class InvoiceAddEditLogic {
	private MainView mainView;
	private static ConstNums cn;
	private static ConstStrings cs;
	private static ConstDB cdb;
	private static Logger log;
	private static DatabaseManager dm;
	private static JSONObject js;
	private static JSONObject jl;
	private JSONObject ju; 

	private Font fonts;
	private Font fonts_title;
	private Map attributes;
	private Color color;

	private CustomersManager cm;
	private StockManager sm;
	private InvoiceManager im;
	private DateHelper dh;
	private static MiscHelper msh;
	private Printer printer;
	private PDFCreator pdfCreator;
	private FileHelper fh;
	private RepakReportFrame repakReport;
	private DecimalFormat df;

	private Map<Item, Integer> selectedRowItem;

	private String[] itemCodes;
	private String[] stockTBHeadings;
	private String date;
	private boolean isBusiness, restore;

	
	public InvoiceAddEditLogic(MainView main, DatabaseManager dmn, ConstDB cDB, ConstStrings cS, ConstNums cN, Logger logger, 
			PDFCreator PDFCreator, Printer printer,
			JSONObject jSettings, JSONObject jLang, JSONObject jUser, 
			MiscHelper mSH, DateHelper DH, FileHelper FH,
			StockManager SM, CustomersManager cMng, InvoiceManager invMng, RepakReportFrame RR){
		this.mainView = main;
		this.jl = jLang;
		this.js = jSettings;
		this.ju = jUser;

		this.dm = dmn;
		this.log = logger;
		this.cdb = cDB;
		this.cs = cS;
		this.cn = cN;
//		cp = cP;
		
		this.msh = mSH;
		this.fh = FH;
		this.dh = DH;
		this.pdfCreator = PDFCreator;
		this.printer = printer;
		this.repakReport = RR;

		this.sm = SM;
		this.cm = cMng;
		this.im = invMng;

		this.df = new DecimalFormat(cs.DECIMAL_FORMAT_5_2);
		
		this.restore = false;
		
		this.itemCodes = msh.json2Array((JSONArray) jl.get(cs.JL_A_ITEM_CODES));
		this.stockTBHeadings = msh.json2Array((JSONArray) jl.get(cs.JL_STOCK_TB_HEADINGS_SHORT));

		this.fonts = new Font(js.get(cs.JS_FONT).toString(), Font.PLAIN, Integer.parseInt(js.get(cs.JS_FONT_SIZE_DEF).toString()));
		this.fonts_title = new Font(js.get(cs.JS_FONT).toString(), Font.PLAIN, Integer.parseInt(js.get(cs.JS_FONT_SIZE_TITLE).toString()));
		this.attributes = fonts_title.getAttributes();
		this.attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		this.color = msh.getColor(cs.APP, cs, js);
		
		this.selectedRowItem = new HashMap<Item, Integer>();
		this.date = dh.getFormatedDateRev();
	}
	
	protected Invoice collectDataForInvoice(String listOfServices, String invoicePath) {
		checkCustomer();
		checkIsBusiness();
		Invoice i= null;
		if(!listOfServices.equals("")){
			double sum = calculateSum();
			sum = applyDiscount(sum);
			total = Double.parseDouble(df.format(sum));
			if(isNew) {
				i = createNewInvoice(listOfServices, invoicePath, total);
			} else {
				if(editedInvoice != null){
					i = editedInvoice;
					i.setList(listOfServices);
					i.setTotal(total);
					i.setDiscount(discount);
					i.setPercent(isPercent);
					i.setDate(date);
					i.updateRecord();
				}
			}
		}
		return i;
	}

	protected void updateDBStock(String listOfServices) {
		boolean updateRepak = false, TRC = false, TRA = false;
		String d = date.substring(0, date.lastIndexOf(cs.MINUS));
		int ct = repakReport.getTyresSold(d, true), at = repakReport.getTyresSold(d, false), t;
		for(int i = 0; i < modTBchosen.getRowCount(); i++){
			Item it = null;
			t = 0;
			if(modTBchosen.getValueAt(i, 0).toString().equals(itemCodes[3])
					|| modTBchosen.getValueAt(i, 0).toString().equals(itemCodes[2])
					|| modTBchosen.getValueAt(i, 0).toString().equals(itemCodes[0])
					|| modTBchosen.getValueAt(i, 0).toString().equals(itemCodes[1])){
				it = this.getItemByName(this.modTBchosen.getValueAt(i, cn.NAME_COLUMN).toString());
			}
			if(it != null){
				it.updateRecord();
				t = Integer.parseInt(modTBchosen.getValueAt(i, 3).toString());
				if(it.getCode().equals(itemCodes[cn.TCC])) {
					TRC = true;
					ct += t;
				}
				if(it.getCode().equals(itemCodes[cn.TCA])){
					TRA = true;
					at += t;
				}
			}
		}
		//TODO
		JSONArray jArr = (JSONArray) jl.get(cs.JL_REPAK_TB_COL_NAMES);
		String[]sHeadings = msh.json2Array(jArr);

		if(TRC){
			repakReport.updateRepakList(d, sHeadings[cn.CAR_TYRE_COL], ct);
		}
		if(TRA){
			repakReport.updateRepakList(d, sHeadings[cn.AGRI_TYRE_COL], at);
//			repakReport.setList(repakReport.listUpdate());
		}
		repakReport.setList(repakReport.listUpdate());
	}

	private void checkIsBusiness() {
		if(customer != null)
			isBusiness = customer.getId().contains(cs.CUST_BUS_CODE) ? true : false;
		else
			isBusiness = !this.chbInd.isSelected();
	}

	protected boolean tableHasElements() {
		return this.tbChoosen.getRowCount() > 0 ? true : false;
	}

	private String getSalesList() {
		String s = "";
		if(this.tableHasElements()){
			int rc = this.tbChoosen.getRowCount();
			int cc = this.tbChoosen.getColumnCount();
			for(int i = 0; i < rc; i++){
				//qnt
				s += this.tbChoosen.getModel().getValueAt(i, 3);
				s += cs.STAR;
				//code of product/service
				s += this.tbChoosen.getModel().getValueAt(i, 0);
				s += cs.UNDERSCORE;
				//name of product/service
				s += this.tbChoosen.getModel().getValueAt(i, 1);
				s += cs.HASH;
				//cost of item
				s += this.tbChoosen.getModel().getValueAt(i, 4);
				s += cs.AT;
				//sale price
				s += this.tbChoosen.getModel().getValueAt(i, 2);
				s += cs.SEMICOLON;
			}
		}
		return s;
	}

	private Invoice createNewInvoice(String listOfServices, String invoicePath, double sum) {
		String path = invoicePath.substring(invoicePath.lastIndexOf(cs.SLASH)+1);
		Invoice in = new Invoice(dm, cdb, cs, cn, customer.getId(),isBusiness, listOfServices, discount, isPercent, sum, date, path);
		this.im.add(in);
		return in;
	}

	private void checkCustomer() {
		if(customer != null){
			int n;
			if(customer.getNumOfServices()>= Integer.parseInt(js.get(cs.JS_NUMBER_OF_SERVICES).toString())){
				n = 0;
			} else {
				n = customer.getNumOfServices();
				n++;
			}
			customer.setNumOfServices(n);
			customer.updateRecord();
		} else {
			String str = lblForWho.getText();
			String car = str.substring(str.indexOf(cs.AT)+1, str.indexOf(cs.AMP)-1);
			str = str.substring(str.indexOf(cs.AMP)+1);
			int brand = findBrand(car);
			if(str.contains(cs.COMA) && this.chbInd.isSelected() ){
				JOptionPane.showMessageDialog(frame, jl.get(cs.JL_ERR_COMA).toString());
				return;
			}else if(this.chbInd.isSelected() && !str.contains(cs.COMA)){
				customer =  new CustomerInd(this.dm, this.cdb, this.cn, this.cs, 1, str, brand);
			}else{
				String[] t = msh.splitStringRemoveSpecialChars(str, cs.COMA);
				String vat="", name="", address="";
				if(t!=null){
					if(t[0]!= null && !t[0].isEmpty()) vat = t[0];
					if(t[1]!= null && !t[1].isEmpty()) name = t[1];
					if(t[2]!= null && !t[2].isEmpty()) address = t[2];
				}
				if(brand > 0)
					customer =  new CustomerBusiness(this.dm, this.cdb, this.cn, this.cs, 1, vat, name, address, brand);
				else
					customer =  new CustomerBusiness(this.dm, this.cdb, this.cn, this.cs, 1, vat, name, address);
					
			}
			this.cm.addCustomer(customer);
		}		
	}

	private void populateCarTable(int x, int y, int w, int h) {
		String[][] data = new String [carList.size()][1];
		data = msh.populateDataArrayString(carList, data, carList.size());

		JTable table = new JTable();
		table = msh.createTable(fonts, data, cs.CARS_TABLE_HEADING, cs.CARS_TB_NAME, 150, 0);
		addListener(table, cs.CARS_TB_NAME);

		rSortCars = new TableRowSorter<>(table.getModel());
		table.setRowSorter(rSortCars);

		JScrollPane spCars = new JScrollPane(table);
		spCars.setSize(w, h);
		spCars.setLocation(x, y);
		frame.getContentPane().add(spCars);
	
	}

	private void populateStockTable(int x, int y, int w, int h) {
		ArrayList<Item> list = sm.getSortedList();
		Iterator<Item> it = list .iterator();
		while(it.hasNext()){
			Item itit = it.next();
			if (itit.getQnt() == 0 && (itit.getCode().equals(itemCodes[cn.TCC]) || itit.getCode().equals(itemCodes[cn.TCA]))){
				it.remove();
			}
		}
		String[][] data = new String [list.size()][stockTBHeadings.length];
		data = sm.getDataNoCost();
		JTable table = new JTable();
		table = msh.createTable(fonts, data, stockTBHeadings, cs.STOCK_TB_NAME, 40, 240);
		addListener(table, cs.STOCK_TB_NAME);

		rSortStock = new TableRowSorter<>(table.getModel());
		table.setRowSorter(rSortStock);
		this.tbStock = table;
		
		JScrollPane spStockList = new JScrollPane(table);
		spStockList.setBounds(x, y, w, h);
		frame.getContentPane().add(spStockList);
	}

	private void populateCustomerTable(int x, int y, int w, int h) {
		String[] custTbHeadings = msh.json2Array((JSONArray) jl.get(cs.JL_CUSTOMER_TB_HEADINGS));
		String[][] data = new String [cm.getList().size()][custTbHeadings.length];
		data = cm.getDataShort();
		JTable table = new JTable();
		table = msh.createTable(fonts, data, custTbHeadings, cs.CUSTOMER_TB_NAME, 140, 20);
		addListener(table, cs.CUSTOMER_TB_NAME);

		rSortCustomer = new TableRowSorter<>(table.getModel());
		table.setRowSorter(rSortCustomer);
		
		JScrollPane spCustomerList = new JScrollPane(table);
		spCustomerList.setBounds(x, y, w, h);
		frame.getContentPane().add(spCustomerList);
	}
	
	private void createChoosenItemsTable(int x, int y, int w, int h) {
		String[] stockTbHeadings = msh.json2Array((JSONArray) jl.get(cs.JL_STOCK_TB_HEADINGS_SHORT));
		ArrayList<Item>emptyArray = new ArrayList<Item>();
		String[][] data = new String [0][stockTbHeadings.length];
		data = populateDataArray(emptyArray, data, 0, 0);

		tbChoosen = new JTable();
		tbChoosen = msh.createTable(fonts, data, stockTbHeadings, cs.CHOSEN_TB_NAME, 40, 240);
		addListener(tbChoosen, cs.CHOSEN_TB_NAME);
		
		JScrollPane spInvoice = new JScrollPane(tbChoosen);
		spInvoice.setBounds(x, y, w, h);
		frame.getContentPane().add(spInvoice);
		
		modTBchosen = tbChoosen.getModel();
		modTBchosen.addTableModelListener(new TableModelListener(){
			@Override
			public void tableChanged(TableModelEvent arg0) {
				double sum = calculateSum();
				lblTotal.setText(cs.EURO+" "+df.format(sum));
			}			
		});
	}

	private void addListener(JTable table, String tbName) {
		ListSelectionListener listener = null;
		if(tbName == cs.STOCK_TB_NAME || tbName == cs.CHOSEN_TB_NAME){
			listener = createStockTableListener(table);
			table.removeColumn(table.getColumnModel().getColumn(4));
		}else if(tbName == cs.CARS_TB_NAME)
			listener = createCarTableListener(table);
		else if(tbName == cs.CUSTOMER_TB_NAME)
			listener = createCustomerTableListener(table);

		table.getSelectionModel().addListSelectionListener(listener);
	}

	private String[][] populateDataArray(ArrayList<Item> list, String[][] data, int startIndex, int rowNumber){
		int j = 0;
		for(int i = startIndex; i < rowNumber; i++) {
			data[i][0] = list.get(j).getCode();
			data[i][1] = list.get(j).getName();
			data[i][2] = ""+df.format(list.get(j).getPrice());
			data[i][3] = ""+list.get(j).getQnt();
			data[i][4] = ""+list.get(j).getCost();
			j++;
		}		
		return data;
	}

	private ListSelectionListener createCarTableListener(JTable table) {
			ListSelectionListener listener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int row = table.getSelectedRow();
				if(row != -1) {
					String car = (table.getModel().getValueAt(table.convertRowIndexToModel(row), 0).toString());
					setBrandLbl(car);
				} 
			}
	    };
	    return listener;
	}

	private ListSelectionListener createCustomerTableListener(JTable table) {
		ListSelectionListener listener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int row = table.getSelectedRow();
				String customerStr = "";
				if(row != -1) {
					if(!(table.getModel().getValueAt(table.convertRowIndexToModel(row), cn.NAME_C_COLUMN).toString()).isEmpty()){
						customerStr = (table.getModel().getValueAt(table.convertRowIndexToModel(row), cn.NAME_C_COLUMN).toString()) + ",";
					}
					
					if(table.getModel().getValueAt(table.convertRowIndexToModel(row), cn.VAT_COLUMN) != null &&
							!(table.getModel().getValueAt(table.convertRowIndexToModel(row), cn.VAT_COLUMN).toString()).isEmpty()){
						customerStr += (table.getModel().getValueAt(table.convertRowIndexToModel(row), cn.VAT_COLUMN).toString());
					}
					
					if(!customerStr.isEmpty()){
						customerStr = msh.removeLastChar(customerStr, cs.COMA);
						updateInvoiceLbl(customerStr);
						customer = null;
						customer = getCustomer(customerStr);
						if(customer != null){
							if(customer instanceof CustomerInd){
								String car = (((CustomerInd) customer).getCar().getBrandString());
								setBrandLbl(car);
							} else if(customer instanceof CustomerBusiness){
								setBrandLbl("OTHER");
							}
						}
					}
				} 			
			}
		};
		return listener;
	}

	private ListSelectionListener createStockTableListener(JTable table) {
		ListSelectionListener listener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				item = null;
				int row = table.getSelectedRow();
				if(row != -1) {
					item = getItemByName(table.getModel().getValueAt(table.convertRowIndexToModel(row), cn.NAME_COLUMN).toString());
					if(item != null && table.getName().equals(cs.STOCK_TB_NAME)){
						selectedRowItem.put(item,row);
					}
				}
			}
	    };
	    return listener;
	}
	
	protected double calculateSum() {
		int rowCount = modTBchosen.getRowCount();
		double sum = 0;
		for(int i = 0; i < rowCount;i++) {
			double price = Double.parseDouble((String) modTBchosen.getValueAt(i, cn.PRICE_COLUMN));
			int qnt = Integer.parseInt((String)modTBchosen.getValueAt(i, cn.QNT_COLUMN));
			price = price * qnt;
			sum += price;
		}
		return sum;
	}

	protected void addToInvoice() {
		double productPrice;
		if(!tfPrice.getText().isEmpty()){
			String tPrice = tfPrice.getText();
			if(tPrice.contains(",")){
				tPrice = tPrice.replace(',', '.');
			}
			productPrice = Double.parseDouble(tPrice);
		} else
			productPrice = item.getPrice();

		int itemQnt = 0, tfQntInt = 0;
		if(item.getCode().equals(itemCodes[cn.CW]) || item.getCode().equals(itemCodes[cn.SR]))
			itemQnt  = 1;
		else
			itemQnt = item.getQnt();
		
		if(!tfQnt.getText().isEmpty()) tfQntInt = Integer.parseInt(this.tfQnt.getText());
		else tfQntInt = 1;
		
		while(tfQntInt > itemQnt && !(item.getCode().equals(itemCodes[cn.CW]) || item.getCode().equals(itemCodes[cn.SR]))){
			JOptionPane.showMessageDialog(frame, "Dostępnych "+itemQnt+"szt.");
			if(tfQntInt > itemQnt) return;
		}
		
		if(!item.getCode().equals(itemCodes[cn.CW]) || !item.getCode().equals(itemCodes[cn.SR])){
			itemQnt = (itemQnt <= 0) ? 0 : itemQnt - tfQntInt;
			if(this.selectedRowItem.containsKey(item)){
				item.setQnt(itemQnt);
				int row = this.selectedRowItem.get(item);
				tbStock.setValueAt(item.getQnt(), row, cn.QNT_COLUMN);
			}	
		}
		//TODO - move below to method?
		String[] rowData = new String[this.stockTBHeadings.length+1];

		rowData[0] = item.getCode();
		rowData[1] = item.getName();
		rowData[2] = ""+df.format(productPrice);
		rowData[3] = ""+tfQntInt;
		rowData[4] = ""+item.getCost();
		
		DefaultTableModel model = (DefaultTableModel) tbChoosen.getModel();
		model.addRow(rowData);
	}

	protected double applyDiscount(double sum) {
		if(sum > 0 && !tfDiscount.getText().isEmpty()){
			discount = Double.parseDouble(tfDiscount.getText());
			if(!isPercent){
				return sum - discount;
			}else if(isPercent){
				return sum - (sum * (discount/100));
			}
		}
		return sum;
	}

	protected void updateStockTableQnt(DefaultTableModel model, int chosenRow, int stRow) {
		if(stRow != -1 && (model.getValueAt(chosenRow, cn.NAME_C_COLUMN).toString().equals(itemCodes[cn.TCC]) 
				|| model.getValueAt(chosenRow, cn.NAME_C_COLUMN).toString().equals(itemCodes[cn.TCA]) 
				|| model.getValueAt(chosenRow, cn.NAME_C_COLUMN).toString().equals(itemCodes[cn.TB]) 
				|| model.getValueAt(chosenRow, cn.NAME_C_COLUMN).toString().equals(itemCodes[cn.SH]))){
			int qnt = Integer.parseInt(model.getValueAt(chosenRow, cn.QNT_COLUMN).toString());
			if(qnt <= 0)
				qnt = 1;
			int actualQnt = Integer.parseInt(tbStock.getValueAt(stRow, cn.QNT_COLUMN).toString());
			qnt = actualQnt + qnt;
			item.setQnt(qnt);
			tbStock.setValueAt(qnt, stRow, cn.QNT_COLUMN);
		}
	}

	protected boolean isUpdateRequred() {
		//TODO check if this method doesn't need any improvements, check by codes?
		for(int i = 0; i < modTBchosen.getRowCount(); i++){
			if(modTBchosen.getValueAt(i, 0).toString().equals(itemCodes[cn.SH])
				|| modTBchosen.getValueAt(i, 0).toString().equals(itemCodes[cn.TB])
				|| modTBchosen.getValueAt(i, 0).toString().equals(itemCodes[cn.TCC])
				|| modTBchosen.getValueAt(i, 0).toString().equals(itemCodes[cn.TCA])){
				return true;
			}
		}
		return false;
	}

	protected Customer getCustomer(String str) {
		String[] t = {"","",""};
		if(str.contains(cs.COMA))
			t = msh.splitStringRemoveSpecialChars(str, cs.COMA);
		else if(str.contains(cs.AMP))
			t[0] = str.substring(str.indexOf(cs.AMP)+1);
		else
			t[0] = str;
		Customer c = null;
//TODO - need to improve
		if(t != null)
			c = cm.find(t);
		
		return c;
	}
	
	protected String getInvoicePath() {
		String subPath = date.replace(cs.MINUS, cs.SLASH); 
		String invoicePath = js.get(cs.JS_INVOICE_PATH).toString()+subPath;
		fh.createFolderIfNotExist(invoicePath);
		invoicePath+=cs.SLASH + date + cs.UNDERSCORE + lastInvoice + cs.PDF_EXT;
		return invoicePath;
	}

	protected PDDocument createPDFDInvoice(String invoicePath) {
		PDDocument pdf = null;
		if(!isBusiness && !checkInvoiceForString()){
			String toFill = getFieldsToFill();
			JOptionPane.showMessageDialog(frame, jl.get(cs.JL_FILL_UP).toString() + " " + toFill);
		} else if(isBusiness && !checkBusinesFields()) {
			String toFill = getBFieldsToFill();
			JOptionPane.showMessageDialog(frame, jl.get(cs.JL_FILL_UP).toString() + " " + toFill);
		} else {
			if (!tableHasElements()){
				JOptionPane.showMessageDialog(frame, jl.get(cs.JL_WARN_EMPTY_TABLE).toString());
			} else {
				boolean update = isUpdateRequred();
				String listOfServices = getSalesList();
				Invoice i = collectDataForInvoice(listOfServices, invoicePath);
				int dialogResult = JOptionPane.showConfirmDialog (frame, jl.get(cs.JL_SAVE_PDF).toString(),"Warning",JOptionPane.YES_NO_OPTION);
				if(dialogResult == JOptionPane.YES_OPTION){
					pdf = pdfCreator.createPDF(cs.PDF_INVOICE, i, customer, this.date);
				}
				if(update){
					updateDBStock(listOfServices);
				}
				if(pdf != null){
					try {
						pdf.save(invoicePath);
						pdf.close();
						JOptionPane.showMessageDialog(frame, jl.get(cs.JL_INVOICE_SAVED_2).toString());
						goBack();
					} catch (IOException e) {
						JOptionPane.showMessageDialog(frame, jl.get(cs.JL_ERR_PDF_SAVE).toString());
						log.log(cs.ERR_LOG, jl.get(cs.JL_ERR_PDF_SAVE).toString() +"    " + e.getMessage());
						e.printStackTrace();
					}
				} else if(dialogResult == JOptionPane.NO_OPTION) {
					JOptionPane.showMessageDialog(frame, jl.get(cs.JL_INVOICE_SAVED_1).toString());
				} else {
					JOptionPane.showMessageDialog(frame, jl.get(cs.JL_ERR_PDF_SAVE).toString());
				}
			}
		}
		return pdf;
	}

	protected boolean checkInvoiceForString() {
		if(!lblForWho.getText().contains(jl.get(cs.LBL_CUSTOMER).toString()) && !lblForWho.getText().contains(jl.get(cs.LBL_COMPANY).toString()))
			return true;
		return false;
	}

	protected String getFieldsToFill() {
		String s = "";
		if(lblForWho.getText().contains(jl.get(cs.LBL_CUSTOMER).toString()) )
			s += jl.get(cs.JL_BRAND).toString()+ " ";
		
		if(lblForWho.getText().contains(jl.get(cs.LBL_COMPANY).toString()) )
			s +=jl.get(cs.LBL_COMPANY).toString();
		
		return s;
	}
	
	protected String getBFieldsToFill() {
		String s = "";
		if(lblForWho.getText().contains(jl.get(cs.TF_CUST_HINT).toString()) )
			s += jl.get(cs.JL_ENTER_DETAILS).toString()+ " ";
		return s;
	}

	protected boolean checkBusinesFields() {
		if(!lblForWho.getText().contains(jl.get(cs.TF_CUST_HINT).toString()))
				return true;
		return false;
	}

	protected void updateInvoiceLbl(String text) {
		String car = lblForWho.getText().substring(lblForWho.getText().indexOf(cs.AT)+1, lblForWho.getText().indexOf(cs.AMP)-1);
		String invFor = jl.get(cs.LBL_INVOICE_FOR).toString();

		invFor = invFor.replace(invFor.substring(invFor.indexOf(cs.AMP)+1), text);
		lblForWho.setText(invFor);
		this.setLastInvoiceNum();
		this.setBrandLbl(car);
	}

	private void updateInvoiceLblFP(String string) {
		String s = this.lblForWho.getText();
		s = s.replace(s.substring(s.indexOf(cs.AT)), string);
		lblForWho.setText(s);
	}

	protected void setBrandLbl(String car) {
		String invFor = lblForWho.getText();
		invFor = invFor.replace(invFor.substring(invFor.indexOf(cs.AT)+1, invFor.indexOf(cs.AMP)-1), car);
		lblForWho.setText(invFor);
	}

	protected Item getItemByName(String string) {
		Item item = null;
		for(Item i : sm.getList()){
			if(i.getName().equals(string))
				item = i;
		}
		return item;
	}
	
	private void setLastInvoiceNum() {
		String invFor = lblForWho.getText();
		invFor = invFor.replace(invFor.substring(invFor.indexOf(cs.HASH)+1, invFor.indexOf(cs.AT)-1), lastInvoice);
		lblForWho.setText(invFor);
	}

	private int findBrand(String car) {
		if(this.mainView != null) {
			if(!car.equals(jl.get(cs.LBL_CUSTOMER).toString())) {
//				return Integer.parseInt(this.mainView.getCars_BI().get(car));
			}
		}
		return cn.DEFAULT_CAR_BRAND_ID;
	}


}
