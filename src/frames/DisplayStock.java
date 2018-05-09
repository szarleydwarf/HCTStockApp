package frames;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.font.TextAttribute;
import java.text.DecimalFormat;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstStrings;
import managers.DatabaseManager;
import managers.StockManager;
import objects.Item;
import utility.Logger;
import utility.MiscHelper;
import utility.Printer;

public class DisplayStock {

	private static ConstNums cn;
	private static ConstStrings cs;
	private static ConstDB cdb;
	private static Logger log;
	private static DatabaseManager dm;
	private static JSONObject js;
	private static JSONObject jl;
	private JFrame frame;
	private JTextField tfSearch;
	private JTable table;
	private TableRowSorter rowSorter;
	private JButton btnEdit;
	private MainView mainView;
	private Font fonts;
	private Font fonts_title;
	private Map attributes;
	private Color color;
	private DecimalFormat df;
	private static MiscHelper msh;
	private static String[][] data;
	private static StockManager sm;
	private boolean itemSaved = false;
	protected String itemCode;
	private JButton btnDelete;
	protected ItemAddNew newItemFrame;
	private Printer printer;

	/**
	 * Launch the application.
	 */
//	public static void main(DatabaseManager dmn, ConstDB cDB, ConstStrings cS, ConstNums cN, Logger logger,
//			JSONObject jSettings, JSONObject jLang, MiscHelper mSH, StockManager SM) {
//		jl = jLang;
//		js = jSettings;
//
//		dm = dmn;
//		log = logger;
//		cdb = cDB;
//		cs = cS;
//		cn = cN;
////		cp = cP;
//		
//		msh = mSH;
//		
//		data = SM.getData();
//		sm = SM;
//
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					DisplayStock window = new DisplayStock();
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the application.
	 */
	public DisplayStock() {
		initialize();
	}

	public DisplayStock(MainView main, ItemAddNew AN, DatabaseManager dmn, ConstDB cDB, ConstStrings cS, ConstNums cN, Logger logger, Printer printer,
			JSONObject jSettings, JSONObject jLang, MiscHelper mSH, StockManager SM, DecimalFormat df_3_2) {
		mainView = main;
		this.newItemFrame = AN;
		jl = jLang;
		js = jSettings;

		dm = dmn;
		log = logger;
		cdb = cDB;
		cs = cS;
		cn = cN;
//		cp = cP;
		
		msh = mSH;
		this.printer = printer;
		
		sm = SM;
		
		df = df_3_2;
		
		fonts = new Font(js.get(cs.FONT).toString(), Font.PLAIN, Integer.parseInt(js.get(cs.FONT_SIZE_DEF).toString()));
		fonts_title = new Font(js.get(cs.FONT).toString(), Font.PLAIN, Integer.parseInt(js.get(cs.FONT_SIZE_TITLE).toString()));
		attributes = fonts_title.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		color = msh.getColor(cs.APP, cs, js);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle(jl.get(cs.LBL_STOCK).toString());
		frame.getContentPane().setBackground(color);
		frame.setBounds(cn.FRAME_X_BOUND, cn.FRAME_Y_BOUND, (msh.getScreenDimension()[0]), (msh.getScreenDimension()[1]));
		frame.getContentPane().setLayout(null);

		JLabel lblTitle = new JLabel(jl.get(cs.LBL_STOCK).toString());
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(fonts_title);
		lblTitle.setBounds(msh.getCenterXofFrame(frame, lblTitle), 10, 260, 24);
		frame.getContentPane().add(lblTitle);

		JLabel lblSearch = new JLabel(jl.get(cs.SEARCH_TEXT_FIELD_FRAZE).toString());
		lblSearch.setHorizontalAlignment(SwingConstants.CENTER);
		lblSearch.setFont(fonts);
		lblSearch.setBounds(10, 40, 200, 24);
		frame.getContentPane().add(lblSearch);

		TitledBorder lblCosts = msh.createBorders(jl.get(cs.LBL_COST).toString(), Color.YELLOW);
		TitledBorder lblPrice = msh.createBorders(jl.get(cs.LBL_PRICE).toString(), Color.YELLOW);
		TitledBorder lblQ = msh.createBorders(jl.get(cs.LBL_QNT).toString(), Color.YELLOW);

		int xOffset = 120, heigh = 32;
		int xPosLbl = (msh.getScreenDimension()[0] / 5), yPosLbl = frame.getHeight() - 76;

// TODO
		JSONArray jArr = (JSONArray) jl.get(cs.ITEM_CATEGORY);
		String[] tCat = msh.json2Array(jArr);

		JComboBox cbCategory = new JComboBox(tCat);
		cbCategory.setSelectedIndex(0);
		cbCategory.setFont(fonts);
		cbCategory.setBounds(xPosLbl, yPosLbl+6, (int) (xOffset*1.2), heigh-10);
		frame.getContentPane().add(cbCategory);

		JLabel lblTyreCost = new JLabel();
		lblTyreCost.setBorder(lblCosts);
		lblTyreCost.setHorizontalAlignment(SwingConstants.CENTER);
		lblTyreCost.setFont(fonts);
		xPosLbl = xPosLbl+(2*xOffset);
		lblTyreCost.setBounds(xPosLbl, yPosLbl, xOffset, heigh);
		frame.getContentPane().add(lblTyreCost);
		
		JLabel lblTyrePrices = new JLabel();
		lblTyrePrices.setHorizontalAlignment(SwingConstants.CENTER);
		lblTyrePrices.setBorder(lblPrice);
		lblTyrePrices.setFont(fonts);
		xPosLbl += (xOffset * 1.25);
		lblTyrePrices.setBounds(xPosLbl, yPosLbl, xOffset, heigh);
		frame.getContentPane().add(lblTyrePrices);
		
		JLabel lblQnt = new JLabel();
		lblQnt.setHorizontalAlignment(SwingConstants.CENTER);
		lblQnt.setBorder(lblQ);
		lblQnt.setFont(fonts);
		xPosLbl += (xOffset * 1.25);
		lblQnt.setBounds(xPosLbl, yPosLbl, xOffset, heigh);
		frame.getContentPane().add(lblQnt);
		
		fillLabels(lblTyreCost, lblTyrePrices, lblQnt, cs.TYRE_CODE_C);
//		
		tfSearch = new JTextField();
		tfSearch.setHorizontalAlignment(SwingConstants.CENTER);
		tfSearch.setFont(fonts);
		tfSearch.setBounds(240, 40, 300, 24);
		frame.getContentPane().add(tfSearch);
		tfSearch.setColumns(10);
		
		// BUTTONS
		JButton btnBack = new JButton(jl.get(cs.BTN_BACK).toString());
		btnBack.setFont(fonts_title);
		btnBack.setBounds((frame.getWidth() - cn.BACK_BTN_X_OFFSET), (frame.getHeight() - cn.BACK_BTN_Y_OFFSET), cn.BACK_BTN_WIDTH, cn.BACK_BTN_HEIGHT);
		frame.getContentPane().add(btnBack);
		
		btnEdit = new JButton(jl.get(cs.BTN_EDIT).toString());
		btnEdit.setForeground(new Color(245, 245, 245));
		btnEdit.setBackground(Color.GRAY);
		btnEdit.setFont(fonts);
		btnEdit.setBounds((frame.getWidth() - cn.BACK_BTN_X_OFFSET), 40, cn.BACK_BTN_WIDTH, cn.BACK_BTN_HEIGHT);
		btnEdit.setEnabled(false);
		frame.getContentPane().add(btnEdit);

		btnDelete = new JButton(jl.get(cs.BTN_DELETE).toString());
		btnDelete.setBounds((frame.getWidth() - cn.BACK_BTN_X_OFFSET), 80, cn.BACK_BTN_WIDTH, cn.BACK_BTN_HEIGHT);
		btnDelete.setForeground(new Color(245, 245, 245));
		btnDelete.setBackground(Color.GRAY);
		btnDelete.setFont(fonts);
		btnDelete.setEnabled(false);
		frame.getContentPane().add(btnDelete);

		JButton btnAddNew = new JButton(jl.get(cs.BTN_NEW).toString());
		btnAddNew.setForeground(new Color(245, 245, 245));
		btnAddNew.setBackground(Color.GREEN);
		btnAddNew.setFont(fonts);
		btnAddNew.setBounds((frame.getWidth() - cn.BACK_BTN_X_OFFSET), 120, cn.BACK_BTN_WIDTH, cn.BACK_BTN_HEIGHT);
		frame.getContentPane().add(btnAddNew);
		
		// LISTENERS SECTION
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				editRecordInDatabase();
			}
		});

		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteRecord();
			}
		});
		
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				setIsVisible(false);
				if(mainView != null)
					if(!mainView.isVisible())
						mainView.setIsVisible(true);
			}
		});

		btnAddNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!newItemFrame.isVisible())
					newItemFrame.setIsVisible(true);
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

                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = tfSearch.getText();

                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

		cbCategory.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbCategory){
					JComboBox cb = (JComboBox) a.getSource();
					String defaultCategory = cb.getSelectedItem().toString();
					String code = "";
// TODO 					
					if(defaultCategory.equals(tCat[0])){
						code = cs.TYRE_CODE_C;
					} else if(defaultCategory.equals(tCat[1])){
						code = cs.TYRE_CODE_A;
					} else if(defaultCategory.equals(tCat[2])){
						code = cs.TUBE_CODE;
					} else if(defaultCategory.equals(tCat[3])){
						code = cs.SHOP_CODE;
					} 
					fillLabels(lblTyreCost, lblTyrePrices, lblQnt, code);
				}		
			}
		});

		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(frame, 
			            jl.get(cs.BTN_CLOSE).toString(), "", 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		        	frame.setVisible(false);
		        	frame.dispose();
					
		        	if(mainView != null)
						if(!mainView.isVisible())
							mainView.setIsVisible(true);
		        }
		    }
		});		
	}

	protected void fillLabels(JLabel lblTyreCost, JLabel lblTyrePrices, JLabel lblQnt, String code) {
		double cSum = 0, pSum = 0;
		int q = 0;
		for (int i = 0; i < sm.getList().size(); i++) {
//			System.out.println("Code "+code+"\t"+sm.getList().get(i).getCode());
			if(sm.getList().get(i).getCode().equals(code)){
				double ct = sm.getList().get(i).getCost();
				double pt = sm.getList().get(i).getPrice();
				q+=sm.getList().get(i).getQnt();
				ct = ct * q;
				pt = pt * q;
				cSum += ct;
				pSum += pt;
			}
		}
		lblTyreCost.setText(cs.EURO+df.format(cSum));
		lblTyrePrices.setText(cs.EURO+df.format(pSum));
		lblQnt.setText(""+q);
	}

	protected void deleteRecord() {
		Item i = getSelected();
		if(i != null){
			 if (JOptionPane.showConfirmDialog(frame, 
			            jl.get(cs.DELETE_MSG).toString(), "", 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
				boolean deleted = sm.deleteItem(i);//i.deleteRecordFromDatabase();
				if(deleted) refreashTable();
			 }
		} else {
			log.logError(jl.get(cs.ITEM_DELETING_ERROR).toString());
			System.out.println("Delete NULL");
		}
	}

	protected void editRecordInDatabase() {
		Item i = getSelected();
		if(i != null){
			openEditFrame(i);
		} else {
			log.logError(jl.get(cs.ITEM_EDITION_ERROR).toString());
			System.out.println("Edit NULL");
		}
	}

	private void openEditFrame(Item i) {
		int lblX = 10, lblY = 10; 
		int xOffset = 120, yOffset = 24;
		int lblW = 80, tfW = 260;
		int lbltfH = 20;
		JFrame editFrame = new JFrame();
		editFrame.getContentPane().setBackground(color);
		editFrame.setBounds(cn.FRAME_X_BOUND, cn.FRAME_Y_BOUND,  cn.ADD_EDIT_FRAME_WIDTH, cn.ADD_EDIT_FRAME_HEIGHT);
		editFrame.getContentPane().setLayout(null);
		editFrame.setVisible(true);
				
		JLabel code = new JLabel(jl.get(cs.LBL_CODE).toString());
		code.setFont(fonts);
		code.setBounds(lblX, lblY, lblW, lbltfH);
		editFrame.getContentPane().add(code);
		
		JComboBox cbCodes = new JComboBox(cs.ITEM_CODES);
		cbCodes.setBounds(xOffset, lblY, tfW, lbltfH);
		editFrame.getContentPane().add(cbCodes);
		cbCodes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbCodes ){
					JComboBox cb = (JComboBox) a.getSource();
					System.out.println("CB: "+cb.getSelectedItem().toString());
					itemCode = (cb.getSelectedItem().toString());
				}		
			}
		});

		JLabel name = new JLabel(jl.get(cs.LBL_NAME).toString());
		name.setFont(fonts);
		lblY += yOffset;
		name.setBounds(lblX, lblY, lblW, lbltfH);
		editFrame.getContentPane().add(name);
		
		JTextField tfName = new JTextField();
		tfName.setFont(fonts);
		tfName.setBounds(xOffset, lblY, tfW, lbltfH);
		editFrame.getContentPane().add(tfName);
		
		JLabel cost = new JLabel(jl.get(cs.LBL_COST).toString());
		cost.setFont(fonts);
		lblY += yOffset;
		cost.setBounds(lblX, lblY, lblW, lbltfH);
		editFrame.getContentPane().add(cost);
		
		JTextField tfCost = new JTextField();
		tfCost.setFont(fonts);
		tfCost.setBounds(xOffset, lblY, tfW, lbltfH);
		editFrame.getContentPane().add(tfCost);
		
		JLabel price = new JLabel(jl.get(cs.LBL_PRICE).toString());
		price.setFont(fonts);
		lblY += yOffset;
		price.setBounds(lblX, lblY, lblW, lbltfH);
		editFrame.getContentPane().add(price);
		
		JTextField tfPrice = new JTextField();
		tfPrice.setFont(fonts);
		tfPrice.setBounds(xOffset, lblY, tfW, lbltfH);
		editFrame.getContentPane().add(tfPrice);
		
		JLabel qnt = new JLabel(jl.get(cs.LBL_QNT).toString());
		qnt.setFont(fonts);
		lblY += yOffset;
		qnt.setBounds(lblX, lblY, lblW, lbltfH);
		editFrame.getContentPane().add(qnt);
		
		JTextField tfQnt = new JTextField();
		tfQnt.setFont(fonts);
		tfQnt.setBounds(xOffset, lblY, tfW, lbltfH);
		editFrame.getContentPane().add(tfQnt);
		
		JLabel vat = new JLabel(jl.get(cs.LBL_VAT).toString());
		vat.setFont(fonts);
		lblY += yOffset;
		vat.setBounds(lblX, lblY, lblW, lbltfH);
		editFrame.getContentPane().add(vat);
		
		JCheckBox chbVAT = new JCheckBox();
		chbVAT.setSelected(true);
		chbVAT.setBackground(color);
		chbVAT.setFont(fonts);
		chbVAT.setBounds(xOffset, lblY, tfW, lbltfH);
		editFrame.getContentPane().add(chbVAT);

		JLabel transport = new JLabel(jl.get(cs.LBL_TRANSPORT).toString());
		transport.setFont(fonts);
		lblY += yOffset;
		transport.setBounds(lblX, lblY, lblW, lbltfH);
		editFrame.getContentPane().add(transport);
		
		JCheckBox chbTransport = new JCheckBox();
		chbTransport.setSelected(true);
		chbTransport.setBackground(color);
		chbTransport.setFont(fonts);
		chbTransport.setBounds(xOffset, lblY, tfW, lbltfH);
		editFrame.getContentPane().add(chbTransport);
		
		JLabel vemc = new JLabel(jl.get(cs.LBL_VEMC).toString());
		vemc.setFont(fonts);
		lblY += yOffset;
		vemc.setBounds(lblX, lblY, lblW, lbltfH);
		editFrame.getContentPane().add(vemc);
		
		JCheckBox chbVemc = new JCheckBox();
		chbVemc.setSelected(true);
		chbVemc.setBackground(color);
		chbVemc.setFont(fonts);
		chbVemc.setBounds(xOffset, lblY, tfW, lbltfH);
		editFrame.getContentPane().add(chbVemc);
		
		JButton btnSave = new JButton(jl.get(cs.BTN_SAVE).toString());
		btnSave.setForeground(Color.RED);
		btnSave.setBackground(Color.LIGHT_GRAY);
		btnSave.setFont(fonts);
		lblY += yOffset;
		btnSave.setBounds(xOffset, lblY, tfW, lbltfH+10);
		btnSave.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				setItemUpdates(i, cbCodes, tfName, tfCost, tfPrice, tfQnt, chbVAT, chbTransport, chbVemc);
				itemSaved = sm.edit(i);//i.updateRecord();
				if(itemSaved){
					JOptionPane.showMessageDialog(editFrame, jl.get(cs.SAVED_MSG).toString());
					editFrame.dispose();
					if(itemSaved){
						refreashTable();
					}
				}else{
					JOptionPane.showMessageDialog(editFrame, jl.get(cs.ITEM_EDITION_ERROR_MSG).toString());
				}
			}
		});
		editFrame.getContentPane().add(btnSave);
		
		populateFields(i, cbCodes, tfName, tfCost, tfPrice, tfQnt, chbVAT, chbTransport, chbVemc);
	}

	public void refreashTable() {
		System.out.println("table refreash");
		sm.getListFromDatabase();
		data = null;
		data = sm.getData();
		table.removeAll();
		createTable();
		itemSaved = false;		
	}

	protected void setItemUpdates(Item i, JComboBox cbCodes, JTextField tfName, JTextField tfCost, JTextField tfPrice, JTextField tfQnt,
			JCheckBox chbVAT, JCheckBox chbTransport, JCheckBox chbVemc) {
	
		if(!itemCode.isEmpty())i.setCode(itemCode); else i.setCode(cs.OTHER_CODE);
		if(!tfName.getText().isEmpty())i.setName(tfName.getText());
		if(!tfCost.getText().isEmpty())i.setCost(Double.parseDouble(tfCost.getText()));
		if(!tfPrice.getText().isEmpty())i.setPrice(Double.parseDouble(tfPrice.getText()));
		if(!tfQnt.getText().isEmpty())i.setQnt(Integer.parseInt(tfQnt.getText()));
		
		if(chbVAT.isSelected()) i.setAddVat((byte) 1); else i.setAddVat((byte) 0);
		if(chbTransport.isSelected()) i.setAddTransportCost((byte) 1); else i.setAddTransportCost((byte) 0);
		if(chbVemc.isSelected()) i.setAddVEMCCharge((byte) 1); else i.setAddVEMCCharge((byte) 0);
	}

	private void populateFields(Item i, JComboBox<?> cbCodes, JTextField tfName, JTextField tfCost, JTextField tfPrice,
			JTextField tfQnt, JCheckBox chbVAT, JCheckBox chbTransport, JCheckBox chbVemc) {
		if(!i.getCode().isEmpty()){
			int index = 0;
			for (int j = 0; j < cs.ITEM_CODES.length; j++) {
				if(cs.ITEM_CODES[j].equals(i.getCode()))
					index = j;
			}
			cbCodes.setSelectedIndex(index);
		}
		
		if(!i.getName().isEmpty())
			tfName.setText(i.getName());
		if(i.getCost() != 0)
			tfCost.setText(""+i.getCost());
		if(i.getPrice() != 0)
			tfPrice.setText(""+i.getPrice());
		if(i.getQnt() != 0)
			tfQnt.setText(""+i.getQnt());
		
		if(i.getAddVat() == 1) chbVAT.setSelected(true);
		else chbVAT.setSelected(false);

		if(i.getAddTransportCost() == 1) chbTransport.setSelected(true);
		else chbTransport.setSelected(false);
		
		if(i.getAddVEMCCharge() == 1) chbVemc.setSelected(true);
		else chbVemc.setSelected(false);


	}

	private Item getSelected() {
		//TODO - find way to refresh this
		int row = table.getSelectedRow();
		if(row > -1){
			for(Item i : sm.getList()){
				if(i.getName().equals(table.getValueAt(row, 2).toString())){
					return i;
				}
			}
		}
		return null;
	}

	private void createTable() {
		ListSelectionListener listener = createTableListener();
		DefaultTableModel dm = new DefaultTableModel(data, this.cs.STOCK_TB_HEADINGS);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		table.getSelectionModel().addListSelectionListener(listener);
		table.setModel(dm);
		
		table.setFillsViewportHeight(true);
		
		table.getColumnModel().getColumn(2).setPreferredWidth(320);
		
		rowSorter = new TableRowSorter<>(table.getModel());
		table.setRowSorter(rowSorter);
		
		JTableHeader header = table.getTableHeader();
		header.setBackground(Color.BLACK);
		header.setForeground(Color.GRAY);
		table.setTableHeader(header);
	      
		JScrollPane scrollPane = new JScrollPane(table);
		int xOffset = 250, yOffset = 140;
		scrollPane.setBounds(20, 66, frame.getWidth() - xOffset , frame.getHeight() - yOffset);
		frame.getContentPane().add(scrollPane);
	}

	private ListSelectionListener createTableListener() {
		ListSelectionListener listener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
//				helper.toggleJButton(btnAddToInvoice, Color.green, Color.darkGray, true);
				msh.toggleJButton(btnDelete, Color.red, Color.gray, true);
				msh.toggleJButton(btnEdit, Color.yellow, Color.GREEN, true);	
			}
	    };
	    return listener;

	}

	// GETTERS & SETTERS
	public boolean isVisible(){
		if(frame != null)
			return frame.isVisible();
		return false;
	}
	
	public void setIsVisible(boolean b){
		initialize();
		sm.getListFromDatabase();
		data = sm.getData();
		createTable();
		frame.setVisible(b);
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
