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
import logic.MainView;
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

public class InvoiceAddEditFrame {

	private JFrame frame;
	
	private Font fonts;
	private Font fonts_title;
	private Map attributes;
	private Color color;
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
//	private JTextField tfCustomers;
	private JCheckBox chbInd;
	protected Customer customer;
	protected Item item;

	private ArrayList<String> carList;
	private Map<Item, Integer> selectedRowItem;

	private String lastInvoice;
	private String date;
	protected boolean isPercent;
	private boolean isBusiness, restore;
	protected double total;
	protected int noOfServices;
	private double discount;

	private boolean isNew;
	private Invoice editedInvoice;

	private static MiscHelper msh;
	private static JSONObject js;
	private static JSONObject jl;
	private JSONObject ju; 
	private static ConstNums cn;
	private static ConstStrings cs;
	private static ConstDB cdb;
	/**
	 * Create the application.
	 */
//	public InvoiceAddEditFrame() {
//		initialize();
//	}

	public InvoiceAddEditFrame(ConstDB cDB, ConstStrings cS, ConstNums cN,
			JSONObject jSettings, JSONObject jLang, JSONObject jUser,
			MiscHelper mSH) {
	
		this.jl = jLang;
		this.js = jSettings;
		this.ju = jUser;

		this.cdb = cDB;
		this.cs = cS;
		this.cn = cN;
//		cp = cP;
		
		this.msh = mSH;

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

		JTextField tfCustomers = new JTextField(jl.get(cs.TF_CUST_HINT).toString());
		tfCustomers.setFont(fonts);
		tfCustomers.setColumns(10);
		int custW = (int)(lblW*0.6);
		int custX = lblX+tfBrands.getWidth()+10;
		int custY = lblY+20;
		tfCustomers.setBounds(custX, custY, custW, lbltfH);
		frame.getContentPane().add(tfCustomers);
		
		chbInd = new JCheckBox(cs.LBL_CHECKBOX_I_B);
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
		tfSearch.setText(jl.get(cs.JL_ENTER_TEXT).toString());
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
					log.log(cs.ERR_LOG, js.get(cs.JL_ERR_PRINTING_PDF+" IOException: "+e.getMessage()).toString());
					e.printStackTrace();
				} catch (PrinterException e) {
					log.log(cs.ERR_LOG, js.get(cs.JL_ERR_PRINTING_PDF+" PrinterException: "+e.getMessage()).toString());
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
				String text = tfCustomers.getText().toUpperCase();
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


		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				restore = true;
				restoreItems();
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
		btnRemove.setForeground(Color.RED);
		btnRemove.setFont(fonts);
		btnRemove.setBackground(new Color(204, 0, 0));
		int btnX = (int) (lblPrevX + (lblW*0.88));
		lblPrevY = lblForWho.getY()+ lblForWho.getHeight()+10;
		btnRemove.setBounds(btnX, lblPrevY, (int) (cn.BACK_BTN_WIDTH*0.37), (int) (cn.BACK_BTN_HEIGHT*0.8));
		frame.getContentPane().add(btnRemove);
		
		JButton btnRemoveAll = new JButton(cs.MINUS+" "+cs.MINUS);
		btnRemoveAll.setForeground(Color.RED);
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
					chosenRow = msh.getSelectedItemRow((DefaultTableModel) modTBchosen, its.get(j));
					//TODO NULL POINTER
					stRow = msh.getSelectedItemRow(dtm, its.get(j));
					if(chosenRow != -1)
						updateStockTableQnt((DefaultTableModel) modTBchosen, chosenRow, stRow);
				}
				
				model.setRowCount(0);
			}
		});
		createChoosenItemsTable(lblPrevX+6, lblPrevY ,lblW-80, lblH-60);
	}
//END OF INVOICE PREVIEW

	protected void goBack() {
		restore = true;
		restoreItems();

		frame.dispose();
		setIsVisible(mainView, false);
		if(mainView != null)
			if(!mainView.isVisible())
				mainView.setIsVisible(true);		
	}

	private void restoreItems() {
//		log.log(" IAE ", "restore "+restore + " "+ isNew + " " + modTBchosen.getRowCount() + " "  + tableHasElements());
		if(restore && isNew){
			//TODO - restore all qnt of the selected items
			if(this.tableHasElements()){
				for(int i = 0; i < modTBchosen.getRowCount(); i++){
					int row = msh.compareItemKeyMap(item, selectedRowItem);
					updateStockTableQnt((DefaultTableModel) modTBchosen, i, row);
				}
			}
		}		
	}

	// GETTERS & SETTERS
	public boolean isVisible(){
		if(frame != null)
			return frame.isVisible();
		return false;
	}
	
	// method used for the new invoice
	public void setIsVisible(MainView MV, boolean b){
		this.restore = false;
		this.discount = 0.00;
		lastInvoice = dm.selectData(cdb.SELECT_LAST_INVOICE);
		if(lastInvoice.equals(""))
			lastInvoice = "0";
		int li = Integer.parseInt(lastInvoice);
		li++;
		this.date = dh.getFormatedDateRev();
		lastInvoice = ""+(li);
		customer = null;
		isBusiness = false;
		this.mainView = MV;
		initialize();
		frame.setVisible(b);
		setLastInvoiceNum();
		isNew = true;
	}

	// method used for the invoice edition
	public void setIsVisible(String[] forPreview, boolean b) {
		this.restore = false;
		this.discount = 0.00;
		
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

	// method used for the invoice edition
	public void setIsVisible(Invoice invoice, boolean b) {
		this.restore = false;
		editedInvoice = invoice;
		lastInvoice = ""+invoice.getId();
		initialize();
		
		customer = this.cm.findByID(invoice.getCustId());
		String str = cs.AT;
		if(customer != null) {
			isBusiness = customer.isBusiness();
			if(!customer.isBusiness()) {
				str += ((CustomerInd) customer).getCar().getBrandString();
				str += " " + cs.AMP;
				str += ((CustomerInd) customer).getCar().getRegistration();
			} else if(customer.isBusiness()) {
				str += ((CustomerBusiness) customer).getVATTaxNUM() +cs.COMA + ((CustomerBusiness) customer).getCompName()+cs.COMA+((CustomerBusiness) customer).getCompAddress();
			}
		} else {
			str += jl.get(cs.JL_OTHER_STRING).toString() + cs.AT + jl.get(cs.JL_NONAME).toString();
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
		} else
			this.discount = 0.00;
		
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
				String[] rowData = new String[this.stockTBHeadings.length+1];
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

	public JFrame getFrame() {
		return frame;
	}


}
