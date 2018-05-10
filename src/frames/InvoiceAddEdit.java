package frames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.font.TextAttribute;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstStrings;
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

public class InvoiceAddEdit {

	private JFrame frame;
	
	private static ConstNums cn;
	private static ConstStrings cs;
	private static ConstDB cdb;
	private static Logger log;
	private static DatabaseManager dm;
	private static JSONObject js;
	private static JSONObject jl;
	private MainView mainView;
	private Font fonts;
	private Font fonts_title;
	private Map attributes;
	private Color color;
	private DecimalFormat df;
	private TableModel modTBchosen;

	private TableRowSorter rSortCars, rSortStock, rSortCustomer;
	private JTextField tfSearch;
	private JLabel lblTotal;
	private JTable tbChoosen;
	private JTextField tfDiscount;
	private JRadioButton rbPercent;
	private JRadioButton rbMoney;
	private ButtonGroup radioGroup;
	private JLabel lblForWho;
	private JTextField tfPrice;
	private JTextField tfQnt;
	private JTable tbStock;
	private JTextField tfCustomers;
	private JCheckBox chbInd;
	private JSONObject ju;

	private CustomersManager cm;
	protected Customer customer;
	private StockManager sm;
	private InvoiceManager im;
	private DateHelper dh;
	private static MiscHelper msh;
	protected Item item;

	private ArrayList<String> carList;
	private Map<Item, Integer> selectedRowItem;

	private String lastInvoice;
	private String date;
	protected boolean isPercent;
	private boolean isBusiness;
	protected double total;
	protected int noOfServices;
	private double discount;

	private Printer printer;
	private PDFCreator pdfCreator;

	private FileHelper fh;

	private boolean isNew;

	private Invoice editedInvoice;

	private RepakReport repakReport;

	
	/**
	 * Launch the application.
	 */
	public static void main(DatabaseManager dmn, ConstDB cDB, ConstStrings cS, ConstNums cN, Logger logger,
			JSONObject jSettings, JSONObject jLang, MiscHelper mSH, StockManager SM) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InvoiceAddEdit window = new InvoiceAddEdit();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public InvoiceAddEdit() {
		initialize();
	}

	public InvoiceAddEdit(MainView main, DatabaseManager dmn, ConstDB cDB, ConstStrings cS, ConstNums cN, Logger logger, 
			PDFCreator PDFCreator, Printer printer,
			JSONObject jSettings, JSONObject jLang, JSONObject jUser, 
			MiscHelper mSH, DateHelper DH, FileHelper FH,
			StockManager SM, CustomersManager cMng, InvoiceManager invMng, RepakReport rr,
			ArrayList<String> carList, DecimalFormat DF) {
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
		this.repakReport = rr;

		this.sm = SM;
		this.cm = cMng;
		this.im = invMng;
		this.carList = carList;

		this.df = DF;
		
		this.fonts = new Font(js.get(cs.FONT).toString(), Font.PLAIN, Integer.parseInt(js.get(cs.FONT_SIZE_DEF).toString()));
		this.fonts_title = new Font(js.get(cs.FONT).toString(), Font.PLAIN, Integer.parseInt(js.get(cs.FONT_SIZE_TITLE).toString()));
		this.attributes = fonts_title.getAttributes();
		this.attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		this.color = msh.getColor(cs.APP, cs, js);
		
		this.selectedRowItem = new HashMap<Item, Integer>();
		this.date = dh.getFormatedDateRev();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		int lblX = 10, lblY = 30; 
		int xOffset = 660, yOffset = 300;
		int lblW = 540, tfW = 256;
		int lbltfH = 22, lblH = 280;
		
		frame = new JFrame();
		frame.getContentPane().setBackground(color);
		frame.setBounds(cn.FRAME_X_BOUND, cn.FRAME_Y_BOUND, (msh.getScreenDimension()[0]), (msh.getScreenDimension()[1]));
		frame.setTitle(jl.get(cs.BTN_INVOICE).toString());
		frame.getContentPane().setLayout(null);

		JLabel lblTitle = new JLabel(jl.get(cs.BTN_INVOICE).toString());
		lblTitle.setHorizontalAlignment(SwingConstants.LEFT);
		lblTitle.setFont(fonts_title);
		lblTitle.setBounds(msh.getCenterXofFrame(frame, lblTitle), lblY/3, (msh.getScreenDimension()[0]), 20);
		frame.getContentPane().add(lblTitle);
		
		// BORDERS
		TitledBorder lblTB = msh.createBorders(jl.get(cs.LBL_CUSTOMER).toString(), Color.YELLOW);

		// LABELS & TEXTFIELDS
		JLabel lblCarList = new JLabel("");
		lblCarList.setBorder(lblTB);
		lblCarList.setFont(fonts);
		lblCarList.setBounds(lblX, lblY, lblW, lblH);
		frame.getContentPane().add(lblCarList);
		
		JTextField tfBrands = new JTextField();
		tfBrands.setFont(fonts);
		tfBrands.setColumns(10);
		int brandW = (int)(lblW*0.25);
		tfBrands.setBounds(lblX+6, lblY+20, brandW, lbltfH);
		frame.getContentPane().add(tfBrands);

		tfCustomers = new JTextField(jl.get(cs.TF_CUST_HINT).toString());
		tfCustomers.setFont(fonts);
		tfCustomers.setColumns(10);
		int custW = (int)(lblW*0.6);
		int custX = lblX+tfBrands.getWidth()+10;
		int custY = lblY+20;
		tfCustomers.setBounds(custX, custY, custW, lbltfH);
		frame.getContentPane().add(tfCustomers);
		
		chbInd = new JCheckBox(cs.CHECKBOX_LBL);
		chbInd.setFont(fonts);
		chbInd.setBackground(color);
		chbInd.setSelected(true);
		int chbX = custX + tfCustomers.getWidth()+10;
		chbInd.setBounds(chbX, custY, 50, lbltfH);
		frame.getContentPane().add(chbInd);
		
		lblTB = msh.createBorders(jl.get(cs.LBL_STOCK).toString(), Color.RED);
		int tY = lblY + yOffset;
		JLabel lblItemsList = new JLabel("");
		lblItemsList.setBorder(lblTB);
		lblItemsList.setFont(fonts);
		lblItemsList.setBounds(lblX, tY, lblW, lblH);
		frame.getContentPane().add(lblItemsList);

		tfSearch = new JTextField();
		tfSearch.setText(jl.get(cs.ENTER_TEXT).toString());
		tfSearch.setFont(fonts);
		tfSearch.setColumns(10);
		int stockW = (int) (lblW*0.75);
		tfSearch.setBounds(lblX+6, tY+20, stockW, lbltfH);
		frame.getContentPane().add(tfSearch);
		
		JLabel lblPriceListed = new JLabel(jl.get(cs.LBL_PRICE).toString());
		lblPriceListed.setFont(fonts);
		int lblPX = lblX + tfSearch.getWidth()+10;
		int lblPY = tY + 20;
		int tfPQW = 54, tfPQH = 22;
		lblPriceListed.setBounds(lblPX, lblPY, tfPQW, tfPQH);
		frame.getContentPane().add(lblPriceListed);

		tfPrice = new JTextField();
		tfPrice.setFont(fonts);
		int lbltPX = lblPX + lblPriceListed.getWidth()+10;
		tfPrice.setBounds(lbltPX, lblPY, tfPQW, tfPQH);
		frame.getContentPane().add(tfPrice);

		JLabel lblQntListed = new JLabel(jl.get(cs.LBL_QNT).toString());
		lblQntListed.setFont(fonts);
		lblPY = tY + 60;
		lblQntListed.setBounds(lblPX, lblPY, tfPQW, tfPQH);
		frame.getContentPane().add(lblQntListed);

		tfQnt = new JTextField();
		tfQnt.setFont(fonts);
		tfQnt.setBounds(lbltPX, lblPY, tfPQW, tfPQH);
		frame.getContentPane().add(tfQnt);
		
	
		// TABLES
		int tableH = lblH-60;
		custW = (int)(lblW*0.7);

		createInvoicePreview();
		populateCustomerTable(custX, custY + 30, custW, tableH);
		populateStockTable(lblX+6, tY+50, stockW, tableH);
		populateCarTable(lblX+6, lblY+50, brandW, tableH);

		// BUTTONS
		int btnX = (frame.getWidth() - cn.BACK_BTN_X_OFFSET),
			btnY = (frame.getHeight() - cn.BACK_BTN_Y_OFFSET);
		JButton btnBack = new JButton(jl.get(cs.BTN_BACK).toString());
		btnBack.setFont(fonts_title);
		btnBack.setBounds(btnX, btnY, cn.BACK_BTN_WIDTH, cn.BACK_BTN_HEIGHT);
		frame.getContentPane().add(btnBack);

		JButton btnAddToInvoice = new JButton(cs.PLUS);
		btnAddToInvoice.setFont(fonts_title);
		btnAddToInvoice.setBounds(lblPX, lblPY+30, tfPQW*2, tfPQH+6);
		frame.getContentPane().add(btnAddToInvoice);

		JButton btnSave = new JButton(jl.get(cs.BTN_SAVE).toString());
		btnSave.setForeground(new Color(0, 0, 102));
		btnSave.setFont(fonts_title);
		btnSave.setBackground(new Color(102, 204, 0));
		btnX = btnBack.getX() - cn.BACK_BTN_X_OFFSET;
		btnSave.setBounds(btnX, btnY, cn.BACK_BTN_WIDTH, cn.BACK_BTN_HEIGHT);
		frame.getContentPane().add(btnSave);
		
		JButton btnPrint = new JButton(jl.get(cs.BTN_PRINT).toString());
		btnPrint.setForeground(Color.YELLOW);
		btnPrint.setFont(fonts);
		btnPrint.setBackground(new Color(204, 0, 0));
		btnX = btnSave.getX() - cn.BACK_BTN_X_OFFSET;
		btnPrint.setBounds(btnX, btnY, cn.BACK_BTN_WIDTH, cn.BACK_BTN_HEIGHT);
		frame.getContentPane().add(btnPrint);

		
		// LISTENERS
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goBack();
			}
		});
		
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				checkIsBusiness();
				PDDocument pdf = null;
				String invPath = getInvoicePath();
				pdf = createPDFDInvoice(invPath);
			}
		});
		
		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				checkIsBusiness();
				PDDocument pdf = null;
				String invPath = getInvoicePath();
				pdf = createPDFDInvoice(invPath);
				try {
					printer.printDoc(invPath);
				} catch (IOException e) {
					log.log(cs.ERR_LOG, js.get(cs.PRINTING_PDF_ERROR+" IOException: "+e.getMessage()).toString());
					e.printStackTrace();
				} catch (PrinterException e) {
					log.log(cs.ERR_LOG, js.get(cs.PRINTING_PDF_ERROR+" PrinterException: "+e.getMessage()).toString());
					e.printStackTrace();
				}
			}
		});
		
		btnAddToInvoice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addToInvoice();
			}
		});
		

		tfBrands.addFocusListener(new FocusListener(){
	        @Override
	        public void focusGained(FocusEvent e){
	        	tfBrands.setText("");
			}
			@Override
			public void focusLost(FocusEvent arg0) {
			}
		});
		
		tfBrands.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = tfBrands.getText();

                if (text.trim().length() == 0) rSortCars.setRowFilter(null);
                else rSortCars.setRowFilter(RowFilter.regexFilter(cs.REGEX_FILTER + text));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = tfBrands.getText();

                if (text.trim().length() == 0) rSortCars.setRowFilter(null);
                else rSortCars.setRowFilter(RowFilter.regexFilter(cs.REGEX_FILTER + text));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
		
		tfCustomers.addFocusListener(new FocusListener(){
	        @Override
	        public void focusGained(FocusEvent e){
	        	tfCustomers.setText("");
			}
			@Override
			public void focusLost(FocusEvent arg0) {
			}
		});
		
		tfCustomers.getDocument().addDocumentListener(new DocumentListener(){
			@Override
			public void insertUpdate(DocumentEvent e) {
				String text = tfCustomers.getText();
				if (text.trim().length() == 0) rSortCustomer.setRowFilter(null);
				else rSortCustomer.setRowFilter(RowFilter.regexFilter(cs.REGEX_FILTER + text));
				updateInvoiceLbl(text);
			}
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				String text = tfCustomers.getText();
				if (text.trim().length() == 0) rSortCustomer.setRowFilter(null);
				else  rSortCustomer.setRowFilter(RowFilter.regexFilter(cs.REGEX_FILTER + text));
				updateInvoiceLbl(text);
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
			}
		});
		
		tfSearch.addFocusListener(new FocusListener(){
	        @Override
	        public void focusGained(FocusEvent e){
	        	tfSearch.setText("");
			}
			@Override
			public void focusLost(FocusEvent arg0) {
			}
		});
	
		tfSearch.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = tfSearch.getText();

                if (text.trim().length() == 0) rSortStock.setRowFilter(null);
                else rSortStock.setRowFilter(RowFilter.regexFilter(cs.REGEX_FILTER + text));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = tfSearch.getText();

                if (text.trim().length() == 0) rSortStock.setRowFilter(null);
                else rSortStock.setRowFilter(RowFilter.regexFilter(cs.REGEX_FILTER + text));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });


	}

//END OF INITIALIZE
	private void createInvoicePreview() {
		int lblX = 10, lblY = 30; 
		int xOffset = 660;
		int lblW = 540;
		int  lblH = 280;

		//PREVIEW
		TitledBorder lblTB = msh.createBorders(jl.get(cs.LBL_PREVIEW).toString(), Color.ORANGE);
		int tx = lblX + xOffset;
		JLabel lblPreview = new JLabel("");
		lblPreview.setBorder(lblTB);
		lblPreview.setFont(fonts);
		lblPreview.setBounds(tx, lblY, lblW, lblH*2);
		frame.getContentPane().add(lblPreview);

		lblTB = msh.createBorders("", Color.GRAY);
		int lblPrevX = tx + 10, lblPrevY = lblY+14;
		JLabel lblCompanyDetails = new JLabel(jl.get(cs.LBL_YOU).toString());
		lblCompanyDetails.setBorder(lblTB);
		lblCompanyDetails.setFont(fonts);
		lblCompanyDetails.setHorizontalAlignment(SwingConstants.CENTER);
		lblCompanyDetails.setBounds(lblPrevX, lblPrevY, lblW/4, lblH/4);
		frame.getContentPane().add(lblCompanyDetails);
		
		lblTB = msh.createBorders("", Color.GRAY);
		JLabel lblLOGO = new JLabel(jl.get(cs.LBL_LOGO).toString());
		lblLOGO.setBorder(lblTB);
		lblLOGO.setFont(fonts);
		lblLOGO.setHorizontalAlignment(SwingConstants.CENTER);
		lblPrevX = lblPrevX+lblCompanyDetails.getWidth()+10;
		lblLOGO.setBounds(lblPrevX, lblPrevY, lblW/2, lblH/4);
		frame.getContentPane().add(lblLOGO);
		
		lblTB = msh.createBorders("", Color.GRAY);
		JLabel lblDate = new JLabel(date);
		lblDate.setBorder(lblTB);
		lblDate.setHorizontalAlignment(SwingConstants.CENTER);
		lblDate.setFont(fonts);
		lblPrevX = lblPrevX+lblLOGO.getWidth()+10;
		lblDate.setBounds(lblPrevX, lblPrevY, lblW/6, lblH/4);
		frame.getContentPane().add(lblDate);
		
		lblPrevX = tx + 10;
		lblForWho = new JLabel(jl.get(cs.LBL_INVOICE_FOR).toString());
		lblForWho.setBorder(lblTB);
		lblForWho.setFont(fonts);
		lblPrevY = lblPrevY + lblDate.getHeight() + 10;
		lblForWho.setBounds(lblPrevX, lblPrevY, lblW-16, lblH/4);
		frame.getContentPane().add(lblForWho);
		
		lblTotal = new JLabel(jl.get(cs.LBL_TOTAL).toString());
		lblTotal.setBorder(lblTB);
		lblTotal.setFont(fonts_title);
		lblTotal.setHorizontalAlignment(SwingConstants.CENTER);
		int lblTotX = lblPrevX + 200;
		lblPrevY = lblPrevY + lblForWho.getHeight() + 300;
		lblTotal.setBounds(lblTotX, lblPrevY, lblW/2, lblH/4);
		frame.getContentPane().add(lblTotal);
		
		// CHECKBOXES & RADIO BUTTONS
		JLabel lblFreebies = new JLabel("");
		int chbW = 160, chbH = 24;
		lblFreebies.setBounds(lblPrevX, lblPrevY, chbW, chbH*4);
		Border b7 = BorderFactory.createLineBorder(color);
		TitledBorder border7 = BorderFactory.createTitledBorder(b7, jl.get(cs.LBL_DISCOUNT).toString());
		lblFreebies.setBorder(border7);
		frame.getContentPane().add(lblFreebies);
		
		tfDiscount = new JTextField();
		tfDiscount.setFont(fonts);
		tfDiscount.setBounds(lblPrevX+8, lblPrevY+16, chbW/2+4, chbH);
		frame.getContentPane().add(tfDiscount);
		
		rbPercent = new JRadioButton(cs.PERCENT, false);
		rbPercent.setFont(fonts);
		rbPercent.setBounds(lblPrevX+8, lblPrevY+42, chbW/4, chbH);
		frame.getContentPane().add(rbPercent);
		
		rbMoney = new JRadioButton(cs.EURO, true);
		rbMoney.setFont(fonts);
		rbMoney.setBounds(lblPrevX+rbPercent.getWidth()+12, lblPrevY+42, chbW/4, chbH);
		frame.getContentPane().add(rbMoney);
		
		radioGroup = new ButtonGroup();
		radioGroup.add(rbMoney);
		radioGroup.add(rbPercent);
		
		rbMoney.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isPercent = false;
				double sum = calculateSum();
				sum = applyDiscount(sum);
				lblTotal.setText(cs.EURO+" "+df.format(sum));
			}
		});
		rbPercent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isPercent = true;
				double sum = calculateSum();
				sum = applyDiscount(sum);
				lblTotal.setText(cs.EURO+" "+df.format(sum));
			}
		});


		// BUTTONS
		JButton btnRemove = new JButton(cs.MINUS);
		btnRemove.setForeground(Color.YELLOW);
		btnRemove.setFont(fonts);
		btnRemove.setBackground(new Color(204, 0, 0));
		int btnX = (int) (lblPrevX + (lblW*0.88));
		lblPrevY = lblForWho.getY()+ lblForWho.getHeight()+10;
		btnRemove.setBounds(btnX, lblPrevY, (int) (cn.BACK_BTN_WIDTH*0.37), (int) (cn.BACK_BTN_HEIGHT*0.8));
		frame.getContentPane().add(btnRemove);
		
		JButton btnRemoveAll = new JButton(cs.MINUS+" "+cs.MINUS);
		btnRemoveAll.setForeground(Color.YELLOW);
		btnRemoveAll.setFont(fonts);
		btnRemoveAll.setBackground(new Color(204, 0, 0));
		int ty  = lblPrevY +btnRemove.getHeight()+ 10;
		btnRemoveAll.setBounds(btnX, ty, (int) (cn.BACK_BTN_WIDTH*0.37), (int) (cn.BACK_BTN_HEIGHT*0.8));
		frame.getContentPane().add(btnRemoveAll);
		
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(item != null && tbChoosen.getSelectedRow() != -1){
					DefaultTableModel model = (DefaultTableModel) tbChoosen.getModel();
					int choosenRow = tbChoosen.getSelectedRow();
					int row = msh.compareItemKeyMap(item, selectedRowItem);
					updateStockTableQnt(model, choosenRow, row);
					model.removeRow(tbChoosen.getSelectedRow());
				}
			}
		});
		
		btnRemoveAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DefaultTableModel model = (DefaultTableModel) tbChoosen.getModel();
				DefaultTableModel dtm = (DefaultTableModel) tbStock.getModel();
				ArrayList<String> its = msh.getTableDataToStringArray(tbChoosen);
				int chosenRow, stRow;
				for(int j = 0; j < its.size(); j++){
					chosenRow = msh.getSelectedItemRow(model, its.get(j));
					//TODO NULL POINTER
					stRow = msh.getSelectedItemRow(dtm, its.get(j));
					if(chosenRow != -1)
						updateStockTableQnt(model, chosenRow, stRow);
				}
				
				model.setRowCount(0);
			}
		});
		createChoosenItemsTable(lblPrevX+6, lblPrevY ,lblW-80, lblH-60);
	}
//END OF INVOICE PREVIEW

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
		for(int i = 0; i < modTBchosen.getRowCount(); i++){
			Item it = null;
			if(modTBchosen.getValueAt(i, 0).toString().equals(cs.SHOP_CODE)
					|| modTBchosen.getValueAt(i, 0).toString().equals(cs.TUBE_CODE)
					|| modTBchosen.getValueAt(i, 0).toString().equals(cs.TYRE_CODE_C)
					|| modTBchosen.getValueAt(i, 0).toString().equals(cs.TYRE_CODE_A)){
				it = this.getItemByName(this.modTBchosen.getValueAt(i, cn.NAME_COLUMN).toString());
			}
			if(it != null){
				it.updateRecord();
				if(it.getCode().equals(cs.TYRE_CODE_C) || it.getCode().equals(cs.TYRE_CODE_A)){
					JSONArray jArr = (JSONArray) jl.get(cs.REPAK_TB_COL_NAMES);
					String[]sHeadings = msh.json2Array(jArr);
					boolean b = (it.getCode().equals(cs.TYRE_CODE_C)) ? true : false;
					int col = (it.getCode().equals(cs.TYRE_CODE_C)) ? 1 : 4;
					String d = date.substring(0, date.lastIndexOf(cs.MINUS));
					
					int tQ = repakReport.getTyresSold(d, b);
					int t = Integer.parseInt(modTBchosen.getValueAt(i, 3).toString());
					
					tQ = tQ + t;
					repakReport.updateRepakList(d, sHeadings[col], tQ);
					repakReport.setList(repakReport.listUpdate());
				}
			}
		}
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
				//1*TR_225/45/18 GOALSTAR V78#@134.74;
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
			if(customer.getNumOfServices()>= Integer.parseInt(ju.get(cs.NUMBER_OF_SERVICES).toString())){
				n = 0;
			} else {
				n = customer.getNumOfServices();
				n++;
			}
			customer.setNumOfServices(n);
			customer.updateRecord();
		}else{
			String str = lblForWho.getText();
			String car = str.substring(str.indexOf(cs.AT)+1, str.indexOf(cs.AMP)-1);
			str = str.substring(str.indexOf(cs.AMP)+1);
			int brand = findBrand(car);
			if(str.contains(cs.COMA) && this.chbInd.isSelected() ){
				JOptionPane.showMessageDialog(frame, jl.get(cs.COMA_ERROR).toString());
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
		Iterator<Item> it = sm.getList().iterator();
		while(it.hasNext()){
			Item itit = it.next();
			if (itit.getQnt() == 0 && (itit.getCode().equals(cs.TYRE_CODE_C) || itit.getCode().equals(cs.TYRE_CODE_A))){
				it.remove();
			}
		}

		String[][] data = new String [sm.getList().size()][cs.STOCK_TB_HEADINGS_SHORT.length];
		data = sm.getDataNoCost();
		JTable table = new JTable();
		table = msh.createTable(fonts, data, cs.STOCK_TB_HEADINGS_SHORT, cs.STOCK_TB_NAME, 40, 240);
		addListener(table, cs.STOCK_TB_NAME);

		rSortStock = new TableRowSorter<>(table.getModel());
		table.setRowSorter(rSortStock);
		this.tbStock = table;
		
		JScrollPane spStockList = new JScrollPane(table);
		spStockList.setBounds(x, y, w, h);
		frame.getContentPane().add(spStockList);
	}

	private void populateCustomerTable(int x, int y, int w, int h) {
		String[][] data = new String [cm.getList().size()][cs.CUSTOMER_TB_HEADINGS.length];
		data = cm.getDataShort();
		JTable table = new JTable();
		table = msh.createTable(fonts, data, cs.CUSTOMER_TB_HEADINGS, cs.CUSTOMER_TB_NAME, 140, 20);
		addListener(table, cs.CUSTOMER_TB_NAME);

		rSortCustomer = new TableRowSorter<>(table.getModel());
		table.setRowSorter(rSortCustomer);
		
		JScrollPane spCustomerList = new JScrollPane(table);
		spCustomerList.setBounds(x, y, w, h);
		frame.getContentPane().add(spCustomerList);
	}
	
	private void createChoosenItemsTable(int x, int y, int w, int h) {
		ArrayList<Item>emptyArray = new ArrayList<Item>();
		String[][] data = new String [0][this.cs.STOCK_TB_HEADINGS_SHORT.length];
		data = populateDataArray(emptyArray, data, 0, 0);

		tbChoosen = new JTable();
		tbChoosen = msh.createTable(fonts, data, this.cs.STOCK_TB_HEADINGS_SHORT, cs.CHOSEN_TB_NAME, 40, 240);
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
		if(item.getCode().equals(cs.CARWASH_CODE) || item.getCode().equals(cs.SERVICE_CODE))
			itemQnt  = 1;
		else
			itemQnt = item.getQnt();
		
		if(!tfQnt.getText().isEmpty()) tfQntInt = Integer.parseInt(this.tfQnt.getText());
		else tfQntInt = 1;
		
		while(tfQntInt > itemQnt && (!item.getCode().equals(cs.CARWASH_CODE) || !item.getCode().equals(cs.SERVICE_CODE))){
			JOptionPane.showMessageDialog(frame, "Dostępnych "+itemQnt+"szt.");
			if(tfQntInt > itemQnt) return;
		}
		
		if(!item.getCode().equals(cs.CARWASH_CODE) || !item.getCode().equals(cs.SERVICE_CODE)){
			itemQnt = (itemQnt <= 0) ? 0 : itemQnt - tfQntInt;
			if(this.selectedRowItem.containsKey(item)){
				item.setQnt(itemQnt);
				int row = this.selectedRowItem.get(item);
				tbStock.setValueAt(item.getQnt(), row, cn.QNT_COLUMN);
			}	
		}
		//TODO - move below to method?
		String[] rowData = new String[this.cs.STOCK_TB_HEADINGS_SHORT.length+1];

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
		if(stRow != -1){
			int qnt = Integer.parseInt(model.getValueAt(chosenRow, cn.QNT_COLUMN).toString());
			if(qnt <= 0)
				qnt = 1;
			int actualQnt = Integer.parseInt(tbStock.getValueAt(stRow, cn.QNT_COLUMN).toString());
			qnt = actualQnt + qnt;
			tbStock.setValueAt(qnt, stRow, cn.QNT_COLUMN);
		}
	}

	protected boolean isUpdateRequred() {
		//TODO check if this method doesn't need any improvements, check by codes?
		for(int i = 0; i < modTBchosen.getRowCount(); i++){
			if(modTBchosen.getValueAt(i, 0).toString().equals(cs.SHOP_CODE)
				|| modTBchosen.getValueAt(i, 0).toString().equals(cs.TUBE_CODE)
				|| modTBchosen.getValueAt(i, 0).toString().equals(cs.TYRE_CODE_C)
				|| modTBchosen.getValueAt(i, 0).toString().equals(cs.TYRE_CODE_A)){
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
		String invoicePath = js.get(cs.INVOICE_PATH).toString()+subPath;
		fh.createFolderIfNotExist(invoicePath);
		invoicePath+=cs.SLASH + date + cs.UNDERSCORE + lastInvoice + cs.PDF_EXT;
		return invoicePath;
	}

	protected PDDocument createPDFDInvoice(String invoicePath) {
		PDDocument pdf = null;
		if(!isBusiness && !checkInvoiceForString()){
			String toFill = getFieldsToFill();
			JOptionPane.showMessageDialog(frame, jl.get(cs.FILL_UP).toString() + " " + toFill);
		} else if(isBusiness && !checkBusinesFields()) {
			String toFill = getBFieldsToFill();
			JOptionPane.showMessageDialog(frame, jl.get(cs.FILL_UP).toString() + " " + toFill);
		} else {
			if (!tableHasElements()){
				JOptionPane.showMessageDialog(frame, jl.get(cs.TABLE_EMPTY).toString());
			} else {
				boolean update = isUpdateRequred();
				String listOfServices = getSalesList();
				Invoice i = collectDataForInvoice(listOfServices, invoicePath);
				int dialogResult = JOptionPane.showConfirmDialog (frame, jl.get(cs.SAVE_PDF).toString(),"Warning",JOptionPane.YES_NO_OPTION);
				if(dialogResult == JOptionPane.YES_OPTION){
					pdf = pdfCreator.createPDF(cs.PDF_INVOICE, i, customer);
				}
				if(update){
					updateDBStock(listOfServices);
				}
				if(pdf != null){
					try {
						pdf.save(invoicePath);
						pdf.close();
						JOptionPane.showMessageDialog(frame, jl.get(cs.INVOICE_SAVED_2).toString());
						goBack();
					} catch (IOException e) {
						JOptionPane.showMessageDialog(frame, jl.get(cs.PDF_SAVE_ERROR).toString());
						log.log(cs.ERR_LOG, jl.get(cs.PDF_SAVE_ERROR).toString() +"    " + e.getMessage());
						e.printStackTrace();
					}
				} else if(dialogResult == JOptionPane.NO_OPTION) {
					JOptionPane.showMessageDialog(frame, jl.get(cs.INVOICE_SAVED_1).toString());
				} else {
					JOptionPane.showMessageDialog(frame, jl.get(cs.PDF_SAVE_ERROR).toString());
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
			s += jl.get(cs.BRAND).toString()+ " ";
		
		if(lblForWho.getText().contains(jl.get(cs.LBL_COMPANY).toString()) )
			s +=jl.get(cs.LBL_COMPANY).toString();
		
		return s;
	}
	
	protected String getBFieldsToFill() {
		String s = "";
		if(lblForWho.getText().contains(jl.get(cs.TF_CUST_HINT).toString()) )
			s += jl.get(cs.ENTER_DETAILS).toString()+ " ";
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
		if(this.mainView != null)
			if(!car.equals(jl.get(cs.LBL_CUSTOMER).toString()))
				return Integer.parseInt(this.mainView.getCars_BI().get(car));
		return cn.DEFAULT_CAR_BRAND_ID;
	}

	protected void goBack() {
		frame.dispose();
		setIsVisible(false);
		if(mainView != null)
			if(!mainView.isVisible())
				mainView.setIsVisible(true);		
	}

	// GETTERS & SETTERS
	public boolean isVisible(){
		if(frame != null)
			return frame.isVisible();
		return false;
	}
	
	public void setIsVisible(boolean b){
		lastInvoice = dm.selectData(cdb.SELECT_LAST_INVOICE);
		if(lastInvoice.equals(""))
			lastInvoice = "0";
		int li = Integer.parseInt(lastInvoice);
		li++;
		lastInvoice = ""+(li);
		customer = null;
		isBusiness = false;

		initialize();
		frame.setVisible(b);
		setLastInvoiceNum();
		isNew = true;
	}

	public void setIsVisible(String[] forPreview, boolean b) {
		lastInvoice = forPreview[0];
		initialize();

		String s = forPreview[1].substring(forPreview[1].indexOf(cs.AMP));
		customer = this.getCustomer(s);
		
		if(customer != null)
			isBusiness = customer.isBusiness();
		else
			isBusiness = false;
		
		this.chbInd.setSelected(isBusiness);

		this.date = forPreview[2];
		
		setSaleTable(forPreview[3]);
		
		frame.setVisible(b);
		setLastInvoiceNum();
		updateInvoiceLblFP(forPreview[1]);
		
		isNew = false;
	}


	public void setIsVisible(Invoice invoice, boolean b) {
		editedInvoice = invoice;
		lastInvoice = ""+invoice.getId();
		initialize();
// TODO
		
		customer = this.cm.findByID(invoice.getCustId());
		
		String str = cs.AT;
		if(customer != null) {
			isBusiness = customer.isBusiness();
			if(!customer.isBusiness()) {
				str = ((CustomerInd) customer).getCar().getBrandString();
				str += " " + cs.HASH;
				str += ((CustomerInd) customer).getCar().getRegistration();
			} else if(customer.isBusiness()) {
				str = ((CustomerBusiness) customer).getVATTaxNUM() +cs.COMA + ((CustomerBusiness) customer).getCompName()+cs.COMA+((CustomerBusiness) customer).getCompAddress();
			}
		} else {
			str = jl.get(cs.OTHER_STRING).toString() + cs.AT + jl.get(cs.NONAME).toString();
			isBusiness = false;
		}
		
		setSaleTable(invoice.getList());
		if(invoice.getDiscount() > 0.00){
			this.discount = invoice.getDiscount();
			this.tfDiscount.setText(""+this.discount);
			if(invoice.isPercent()==0)
				this.rbMoney.setSelected(true);
			else if(invoice.isPercent() > 0)
				this.rbPercent.setSelected(true);
			double sum = calculateSum();
			sum = this.applyDiscount(sum);
			this.lblTotal.setText(cs.EURO+" "+df.format(sum));
		}
		this.chbInd.setSelected(isBusiness);

		this.date = invoice.getDate();
		
		
		frame.setVisible(b);
		setLastInvoiceNum();
		updateInvoiceLblFP(str);
		isNew = false;
	}

	private void setSaleTable(String string) {
		String[] shopping = msh.splitString(string, cs.SEMICOLON);
		for (int i = 0; i < shopping.length-1; i++) {
			if(shopping[i] != "" || !shopping[i].isEmpty() || shopping[i] != null){
				String ss = shopping[i];
				String[] rowData = new String[this.cs.STOCK_TB_HEADINGS_SHORT.length+1];
				rowData[3] = ""+ss.substring(0, ss.indexOf(cs.STAR));
				rowData[0] = ss.substring(ss.indexOf(cs.STAR)+1, ss.indexOf(cs.UNDERSCORE));
				rowData[1] = ss.substring(ss.indexOf(cs.UNDERSCORE)+1, ss.indexOf(cs.HASH));
				
				rowData[4] = ss.substring(ss.indexOf(cs.HASH)+1, ss.indexOf(cs.AT));
				rowData[2] = ss.substring(ss.indexOf(cs.AT)+1);
				
				DefaultTableModel model = (DefaultTableModel) tbChoosen.getModel();
				model.addRow(rowData);
			}
		}
	}

	public static ConstNums getCn() {
		return cn;
	}

	public static ConstStrings getCs() {
		return cs;
	}

	public static ConstDB getCdb() {
		return cdb;
	}

	public static Logger getLog() {
		return log;
	}

	public static DatabaseManager getDm() {
		return dm;
	}

	public static JSONObject getJs() {
		return js;
	}

	public static JSONObject getJl() {
		return jl;
	}


}
