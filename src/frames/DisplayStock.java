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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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
import javax.swing.table.TableRowSorter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstStrings;
import managers.DatabaseManager;
import managers.StockManager;
import objects.Item;
import utility.DateHelper;
import utility.Logger;
import utility.MiscHelper;
import utility.PDFCreator;
import utility.Printer;

public class DisplayStock {

	private static ConstNums cn;
	private static ConstStrings cs;
	private static ConstDB cdb;
	private static Logger log;
	private static DatabaseManager dm;
	private static JSONObject js;
	private static JSONObject jl;
	private static MiscHelper msh;
	private static String[][] data;
	private static StockManager sm;

	private JFrame frame;
	private JTextField tfSearch, tfName, tfCost, tfPrice, tfQnt;
	private JTable table;
	private JLabel sugestedCost, sugestedPrice;
	private JButton btnDelete;
	private JRadioButton rbTypedIn, rbSuggested;
	private JComboBox cbCodes;
	private JCheckBox chbTransport, chbVAT, chbVemc;
	private TableRowSorter rowSorter;
	
	private MainView mainView;
	private Printer printer;
	private PDFCreator pdfc;
	private DateHelper dh;
	protected ItemAddNew newItemFrame;

	private Font fonts;
	private Font fonts_title;
	private Map attributes;
	private Color color;
	private DecimalFormat df;
	private boolean itemSaved = false, isSuggested;
	protected String itemCode;
	private int profitPercent;
	private int vat, tran, vemc;
	protected double d_cost, transportCharge;
	protected String cost, price;

	/**
	 * Create the application.
	 */
	public DisplayStock() {
		initialize();
	}

	public DisplayStock(MainView main, ItemAddNew AN, DatabaseManager dmn, 
			ConstDB cDB, ConstStrings cS, ConstNums cN, Logger logger, Printer printer,
			JSONObject jSettings, JSONObject jLang, MiscHelper mSH, DateHelper DH, 
			PDFCreator PDFC, StockManager SM, DecimalFormat df_3_2) {
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
		dh = DH;
		pdfc = PDFC;
		
		this.printer = printer;
		itemCode = cs.TYRE_CODE_C;
		
		sm = SM;
		
		df = df_3_2;
		vat = 0;
		tran = 0;
		vemc = 0;

		
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
		lblTitle.setHorizontalAlignment(SwingConstants.LEFT);
		lblTitle.setFont(fonts_title);
		lblTitle.setBounds(msh.getCenterXofFrame(frame, lblTitle), 10, 260, 24);
		frame.getContentPane().add(lblTitle);

		JLabel lblSearch = new JLabel(jl.get(cs.SEARCH_TEXT_FIELD_FRAZE).toString());
		lblSearch.setHorizontalAlignment(SwingConstants.CENTER);
		lblSearch.setFont(fonts);
		lblSearch.setBounds(10, 40, 200, 24);
		frame.getContentPane().add(lblSearch);
		
		String[] tCodes = msh.json2Array((JSONArray) jl.get(cs.ITEM_CODES_ARR));
		JComboBox cbCodes = new JComboBox(tCodes);
		cbCodes.setSelectedIndex(0);
		cbCodes.setFont(fonts);
		cbCodes.setBounds((frame.getWidth() - cn.BACK_BTN_X_OFFSET), 160, cn.BACK_BTN_WIDTH, cn.BACK_BTN_HEIGHT);
		frame.getContentPane().add(cbCodes);
		
		JButton btnPrint = new JButton(jl.get(cs.BTN_PRINT).toString());
		btnPrint.setFont(fonts);
		btnPrint.setBounds((frame.getWidth() - cn.BACK_BTN_X_OFFSET), 200, cn.BACK_BTN_WIDTH, cn.BACK_BTN_HEIGHT);
		frame.getContentPane().add(btnPrint);

		TitledBorder lblCosts = msh.createBorders(jl.get(cs.LBL_COST).toString(), Color.YELLOW);
		TitledBorder lblPrice = msh.createBorders(jl.get(cs.LBL_PRICE).toString(), Color.YELLOW);
		TitledBorder lblQ = msh.createBorders(jl.get(cs.LBL_QNT).toString(), Color.YELLOW);

		int xOffset = 120, heigh = 32;
		int xPosLbl = (frame.getWidth() / 2)+40, yPosLbl = frame.getHeight() /2 + 50;

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
		yPosLbl = yPosLbl + 50;
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

		btnDelete = new JButton(jl.get(cs.BTN_DELETE).toString());
		btnDelete.setBounds((frame.getWidth() - cn.BACK_BTN_X_OFFSET), 80, cn.BACK_BTN_WIDTH, cn.BACK_BTN_HEIGHT);
		btnDelete.setForeground(new Color(245, 245, 245));
		btnDelete.setBackground(Color.GRAY);
		btnDelete.setFont(fonts);
		btnDelete.setEnabled(false);
		frame.getContentPane().add(btnDelete);

		// LISTENERS SECTION

		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteRecord();
			}
		});
		
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goBack();
			}
		});
		
		btnPrint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String code = "";
				switch (cbCodes.getSelectedIndex()) {
				case 0:
					code = cs.ALL_EN;
					break;
				case 1:
					code = cs.TYRE_CODE_C;
					break;
				case 2:
					code = cs.TYRE_CODE_A;
					break;
				case 3:
					code = cs.TUBE_CODE;
					break;
				case 4:
					code = cs.SHOP_CODE;
					break;

				default:
					code = cs.ALL_EN;
					break;
				}
				String path = createPath(code);
				
				if(printStock(path, code)){
					
				} else{
					JOptionPane.showMessageDialog(frame, "BTN PRESSED " + jl.get(cs.PDF_SAVE_ERROR).toString());
				}
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
		editForm();
		
	}

	protected void goBack() {
		frame.dispose();
		setIsVisible(false);
		if(mainView != null)
			if(!mainView.isVisible())
				mainView.setIsVisible(true);
	}

	private void editForm() {
		int lblX = frame.getWidth()/2+30, lblY = 70, lblW = frame.getWidth()/2-200, lblH = (int) (frame.getHeight()/2.2);
		int lbltfH = 24, xOffset = 120, yOffset = 28, tfW = 260, rbW = 110;
		float sHigh = 1.25f, wDiv = 1.7f;
		
		TitledBorder lblBorder = msh.createBorders(jl.get(cs.BTN_EDIT).toString(), Color.RED);

		JLabel edit = new JLabel();
		edit.setBorder(lblBorder);
		edit.setFont(fonts_title);
		edit.setBounds(lblX, lblY, lblW, lblH);
		frame.getContentPane().add(edit);
		
		JLabel code = new JLabel(jl.get(cs.LBL_CODE).toString());
		code.setFont(fonts);
		lblX += 10;
		lblY += 15;
		code.setBounds(lblX, lblY, lblW, lbltfH);
		frame.getContentPane().add(code);
		
		cbCodes = new JComboBox(cs.ITEM_CODES);
		xOffset = lblX + xOffset;
		cbCodes.setBounds(xOffset, lblY, tfW, lbltfH);
		frame.getContentPane().add(cbCodes);

		JLabel name = new JLabel(jl.get(cs.LBL_NAME).toString());
		name.setFont(fonts);
		lblY += yOffset;
		name.setBounds(lblX, lblY, lblW, lbltfH);
		frame.getContentPane().add(name);
		
		tfName = new JTextField();
		tfName.setFont(fonts);
		tfName.setBounds(xOffset, lblY, tfW, lbltfH);
		frame.getContentPane().add(tfName);
		
		JLabel lblCost = new JLabel(jl.get(cs.LBL_COST).toString());
		lblCost.setFont(fonts);
		lblY += yOffset;
		lblCost.setBounds(lblX, lblY, lblW, lbltfH);
		frame.getContentPane().add(lblCost);
		
		tfCost = new JTextField();
		tfCost.setFont(fonts);
		tfCost.setText("0.00");
		tfCost.setBounds(xOffset, lblY, (int) (tfW/wDiv), lbltfH);
		frame.getContentPane().add(tfCost);

		Border b = BorderFactory.createLineBorder(Color.RED);
		TitledBorder border = BorderFactory.createTitledBorder(b, jl.get(cs.LBL_SUGGESTED_COST).toString());
		int x = xOffset + tfCost.getWidth();

		sugestedCost = new JLabel();
		sugestedCost.setFont(fonts);
		sugestedCost.setBounds(x, lblY-5, (int) (lblW*0.25), (int) (lbltfH*sHigh));
		sugestedCost.setBorder(border);
		frame.getContentPane().add(sugestedCost);

		JLabel lblPrice = new JLabel(jl.get(cs.LBL_PRICE).toString());
		lblPrice.setFont(fonts);
		lblY += yOffset;
		lblPrice.setBounds(lblX, lblY, lblW, lbltfH);
		frame.getContentPane().add(lblPrice);
		
		tfPrice = new JTextField();
		tfPrice.setFont(fonts);
		tfPrice.setText("0.00");
		tfPrice.setBounds(xOffset, lblY, (int) (tfW/wDiv), lbltfH);
		frame.getContentPane().add(tfPrice);
		border = BorderFactory.createTitledBorder(b, jl.get(cs.LBL_SUGGESTED_PRICE).toString());
		
		sugestedPrice = new JLabel();
		sugestedPrice.setFont(fonts);
		sugestedPrice.setBounds(x, lblY-5, (int) (lblW*0.25), (int) (lbltfH*sHigh));
		sugestedPrice.setBorder(border);
		frame.getContentPane().add(sugestedPrice);
		
		rbTypedIn = new JRadioButton(jl.get(cs.RB_TYPED_IN).toString(), false);
		rbTypedIn.setFont(fonts);
		lblY += yOffset;
		rbTypedIn.setBounds(xOffset, lblY, rbW, lbltfH);
		frame.getContentPane().add(rbTypedIn);
		
		rbSuggested = new JRadioButton(jl.get(cs.RB_SUGGESTED).toString(), true);
		rbSuggested.setFont(fonts);
		rbSuggested.setBounds(xOffset+150, lblY, rbW, lbltfH);
		frame.getContentPane().add(rbSuggested);
		
		ButtonGroup radioGroup = new ButtonGroup();
		radioGroup.add(rbSuggested);
		radioGroup.add(rbTypedIn);

		
		JLabel qnt = new JLabel(jl.get(cs.LBL_QNT).toString());
		qnt.setFont(fonts);
		lblY += yOffset;
		qnt.setBounds(lblX, lblY, lblW, lbltfH);
		frame.getContentPane().add(qnt);
		
		tfQnt = new JTextField();
		tfQnt.setFont(fonts);
		tfQnt.setText(""+0);
		tfQnt.setBounds(xOffset, lblY, (int) (tfW/wDiv), lbltfH);
		frame.getContentPane().add(tfQnt);
//
////TODO - profit percent combobox
		JComboBox cbProfit = new JComboBox(msh.json2Array((JSONArray) js.get(cs.PROFITS)));
		cbProfit.setBounds(x+5, lblY, (int) (lblW*0.25), lbltfH);
		cbProfit.setSelectedIndex(0);
		frame.getContentPane().add(cbProfit);
		profitPercent = msh.removeSpecialChars(cbProfit.getItemAt(0).toString());

		
		JLabel lblVat = new JLabel(jl.get(cs.LBL_VAT).toString());
		lblVat.setFont(fonts);
		lblY += yOffset;
		lblVat.setBounds(lblX, lblY, lblW, lbltfH);
		frame.getContentPane().add(lblVat);
		
		chbVAT = new JCheckBox();
		chbVAT.setSelected(Boolean.parseBoolean(""+vat));
		chbVAT.setBackground(color);
		chbVAT.setFont(fonts);
		chbVAT.setBounds(xOffset, lblY, tfW, lbltfH);
		frame.getContentPane().add(chbVAT);

		JLabel transport = new JLabel(jl.get(cs.LBL_TRANSPORT).toString());
		transport.setFont(fonts);
		lblY += yOffset;
		transport.setBounds(lblX, lblY, lblW, lbltfH);
		frame.getContentPane().add(transport);
		
		String[] tSupNames = msh.json2Array((JSONArray) js.get(cs.SUPLIERS_NAMES_ARR));
		String[] tSupTrans = msh.json2Array((JSONArray) js.get(cs.SUPPLIERS_CHARGES_ARR));
		
		chbTransport = new JCheckBox();
		chbTransport.setSelected(Boolean.parseBoolean(""+tran));
		chbTransport.setBackground(color);
		chbTransport.setFont(fonts);
		chbTransport.setBounds(xOffset, lblY, 30, lbltfH);
		frame.getContentPane().add(chbTransport);
		
		JComboBox cbTransCharges = new JComboBox(tSupNames);
		cbTransCharges.setBounds(xOffset + 30, lblY, tfW/2, lbltfH);
		cbTransCharges.setSelectedIndex(0);
		frame.getContentPane().add(cbTransCharges);
		

		JLabel lblTransportCharges = new JLabel("€ 0");
		lblTransportCharges.setFont(fonts);
		lblTransportCharges.setBounds(x+50, lblY, lblW, lbltfH);
		frame.getContentPane().add(lblTransportCharges);
		
		JLabel lblVemc = new JLabel(jl.get(cs.LBL_VEMC).toString());
		lblVemc.setFont(fonts);
		lblY += yOffset;
		lblVemc.setBounds(lblX, lblY, lblW, lbltfH);
		frame.getContentPane().add(lblVemc);
		
		chbVemc = new JCheckBox();
		chbVemc.setSelected(Boolean.parseBoolean(""+vemc));
		chbVemc.setBackground(color);
		chbVemc.setFont(fonts);
		chbVemc.setBounds(xOffset, lblY, tfW, lbltfH);
		frame.getContentPane().add(chbVemc);

		JButton btnUpdate = new JButton(jl.get(cs.BTN_UPDATE).toString());
		btnUpdate.setForeground(Color.RED);
		btnUpdate.setBackground(Color.LIGHT_GRAY);
		btnUpdate.setFont(fonts);
		lblY += yOffset;
		btnUpdate.setBounds(xOffset, lblY, tfW / 2, lbltfH+10);
		frame.getContentPane().add(btnUpdate);

		JButton btnAddNew = new JButton(jl.get(cs.BTN_NEW).toString());
		btnAddNew.setForeground(Color.RED);
		btnAddNew.setBackground(Color.GREEN);
		btnAddNew.setFont(fonts);
		btnAddNew.setBounds((btnUpdate.getX() + btnUpdate.getWidth() + 10), lblY, tfW / 2, lbltfH+10);
		frame.getContentPane().add(btnAddNew);
		
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//TODO -  update
				if(getSelected() != null) {
					Item i = getSelected();
					i = setItemUpdates(i, cbCodes, tfName, tfCost, tfPrice, tfQnt, chbVAT, chbTransport, chbVemc);
					updateEdition(i);
				} else{
					JOptionPane.showMessageDialog(frame, jl.get(cs.UPDATE_SELECTED_ERR).toString());
				}
			}
		});
		

		btnAddNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO save new item, check if not exist already
				Item i = new Item(dm, cdb, cn, cs, df);
				i = setItemUpdates(i, cbCodes, tfName, tfCost, tfPrice, tfQnt, chbVAT, chbTransport, chbVemc);
				addNewItem(i);
			}
		});

		tfCost.getDocument().addDocumentListener(new DocumentListener() {
			  public void changedUpdate(DocumentEvent e) {
			  }
			  public void removeUpdate(DocumentEvent e) {
				  recalculateCost(tfCost);
			  }
			  public void insertUpdate(DocumentEvent e) {
				  recalculateCost(tfCost);
			  }
		});
		
		chbVAT.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				cost = "";price = "";
				if(chbVAT.isSelected())	vat = 1;
				else vat = 0;

				if(d_cost > 0) {
					cost = calculateCost();
					price = calculatePrice();
				}
				
				setCostLBL();
				setPriceLBL();
		    }
		});

		chbTransport.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				cost = "";price = "";
				if(chbTransport.isSelected())	tran = 1;
				else tran = 0;
				
				if(tran != 0){
					transportCharge = Double.parseDouble(tSupTrans[cbTransCharges.getSelectedIndex()]);
				}
				
				if(d_cost > 0) {
					cost = calculateCost();
					price = calculatePrice();
				}
			
				setCostLBL();
				setPriceLBL();
			}
		});
		
		chbVemc.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				cost = ""; price = "";
				if(chbVemc.isSelected())	vemc = 1;
				else vemc = 0;
				if(d_cost > 0) {
					cost = calculateCost();
					price = calculatePrice();
				}
				
				setCostLBL();
				setPriceLBL();
			}
		});
		
		rbSuggested.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isSuggested = true;
			}
		});

		rbTypedIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isSuggested = false;
			}
		});
		
		cbCodes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbCodes ){
					JComboBox cb = (JComboBox) a.getSource();
					itemCode = (cb.getSelectedItem().toString());
				}		
			}
		});
		
		cbProfit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbProfit ){
					JComboBox cb = (JComboBox) a.getSource();
					profitPercent = msh.removeSpecialChars(cb.getSelectedItem().toString());
					
					  if(d_cost > 0){
						  cost = calculateCost();
						  price = calculatePrice();
					  }
					setCostLBL();
					setPriceLBL();
				}
			}
		});

		cbTransCharges.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbTransCharges ){
					JComboBox cb = (JComboBox) a.getSource();
					for (int i=0; i<tSupNames.length; i++) {
						if(tSupNames[i].equals(cb.getSelectedItem().toString())){
							transportCharge = Double.parseDouble(tSupTrans[i]);
							lblTransportCharges.setText(cs.EURO+df.format(transportCharge));
							if(tran != 0 && d_cost > 0) {
								cost = calculateCost();
								price = calculatePrice();
							}
							setCostLBL();
							setPriceLBL();
						}
					}
					
				}		
			}
		});

	}

	protected void recalculateCost(JTextField tfCost) {
		  d_cost = 0;
		  cost = ""; price = "";
		  if(tfCost.getText() != null && !tfCost.getText().isEmpty() && tfCost.getText() != "")
			  d_cost = msh.isDouble(tfCost.getText()); 
		  
		  if(d_cost > 0){
			  cost = calculateCost();
			  price = calculatePrice();
		  }
		  setCostLBL();
		  setPriceLBL();
	}

	private String calculatePrice() {
		double dc = 0;
		if(cost != "" && !cost.isEmpty() && cost != null)
			dc = msh.isDouble(cost);//Double.parseDouble(cost);
		if(getSelected() != null)
			return getSelected().calculateSuggestedPrice(dc, profitPercent);
		else {
			Item i = new Item(dm, cdb, cn, cs, df, "", "", 0.00,0,0,0,0);
			return i.calculateSuggestedPrice(dc, profitPercent);
		}
	}

	private void setPriceLBL() {
		sugestedPrice.setText(cs.EURO + price);
	}

	protected void setCostLBL() {
		sugestedCost.setText(cs.EURO + cost);
	}

	protected String calculateCost() {
		if(getSelected() != null)
			return getSelected().calcCost(d_cost, transportCharge, vat, tran, vemc);
		else {
			Item i = new Item(dm, cdb, cn, cs, df, "", "", 0.00,0,0,0,0);
			return i.calcCost(d_cost, transportCharge, vat, tran, vemc);
		}
//		return "";
	}

	protected String createPath(String code) {
		String p = msh.createPdfPath(dh.getFormatedDateRev(), cs.STOCK_REP_PATH, cs.LBL_STOCK);
		String t = p.substring(p.lastIndexOf(cs.SLASH)+1);
		p = p.substring(0, p.lastIndexOf(cs.SLASH)) + cs.SLASH + code + cs.UNDERSCORE + t; 
		return p;
	}

	protected boolean printStock(String path, String code) {
		PDDocument pdf = null;
		JSONArray jArr = (JSONArray) jl.get(cs.STOCK_REPORT_HEADINGS);
		String header = msh.stringArr2String(msh.json2Array(jArr), "");
		String date = path.substring(path.lastIndexOf(cs.SPACE)+1, path.lastIndexOf(cs.DOT));
		date = date.replaceAll(cs.UNDERSCORE, cs.MINUS);
		System.out.println("DD "+path);
		String[][] data = sm.getDataByCode(code);
//		msh.printData(data);

		pdf = pdfc.createPDF(cs.PDF_STOCK_REPORT, data, header, date);
		if(pdf != null){
			try {
				pdf.save(path);
				pdf.close();
				JOptionPane.showMessageDialog(frame, jl.get(cs.INVOICE_SAVED_2).toString());
				return true;
			} catch (IOException e) {
				JOptionPane.showMessageDialog(frame, jl.get(cs.PDF_SAVE_ERROR).toString());
				log.log(cs.ERR_LOG, jl.get(cs.PDF_SAVE_ERROR).toString() +"    " + e.getMessage());
				e.printStackTrace();
				return false;
			}
		} else {
			log.log(cs.ERR_LOG, "PDF NULL "+jl.get(cs.PDF_SAVE_ERROR).toString());
			JOptionPane.showMessageDialog(frame, "PDF NULL "+jl.get(cs.PDF_SAVE_ERROR).toString());
			return false;
		}
	}

	protected void fillLabels(JLabel lblTyreCost, JLabel lblTyrePrices, JLabel lblQnt, String code) {
		double cSum = 0, pSum = 0, pQ = 0;
		int q = 0;
		for (int i = 0; i < sm.getList().size(); i++) {
			if(sm.getList().get(i).getCode().equals(code)){
				double ct = sm.getList().get(i).getCost();
				double pt = sm.getList().get(i).getPrice();
				q = sm.getList().get(i).getQnt();

				ct = ct * q;
				pt = pt * q;
				cSum += ct;
				pSum += pt;
				pQ += q;
			}
		}
		lblTyreCost.setText(cs.EURO+df.format(cSum));
		lblTyrePrices.setText(cs.EURO+df.format(pSum));
		lblQnt.setText(""+pQ);
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
			log.log(cs.ERR_LOG, jl.get(cs.ITEM_DELETING_ERROR).toString());
			System.out.println("Delete NULL");
		}
	}


	private void updateEdition(Item i) {
		boolean saved = sm.edit(i);
		if(saved){
			JOptionPane.showMessageDialog(frame, jl.get(cs.JL_ITEM_UPDATED).toString());
			goBack();
		}
	}

	protected void addNewItem(Item i) {
		boolean saved = false;
		Item t = null;
		String[] options = msh.json2Array((JSONArray) jl.get(cs.JL_OPTIONS_BTN));

		if(sm.search(i.getCode()+i.getID()) || sm.search(i.getName())){
			String str = "";
			if(sm.search(i.getName()))
				str = i.getName();
			else
				str = i.getCode()+i.getID();
			
			t = sm.find(str);
			
		}
		if(t != null){
			if(JOptionPane.showOptionDialog(frame,jl.get(cs.JL_WARN_ITEM_UPDATE).toString(),"Title",JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, 
				    options, options[0]) == JOptionPane.YES_OPTION){
				t = sm.editNew(t, i);
				saved = t.updateRecord();
			} else {
				saved = sm.addItem(i);
			}
		} else {
			saved = sm.addItem(i);
		}
		if(saved){
			JOptionPane.showMessageDialog(frame, jl.get(cs.JL_NEW_ITEM_SAVED).toString());
			goBack();
		}
	}

	public void refreashTable() {
		sm.getListFromDatabase();
		data = null;
		data = sm.getData();
		table.removeAll();
		createTable();
		itemSaved = false;		
	}

	protected Item setItemUpdates(Item i, JComboBox cbCodes, JTextField tfName, JTextField tfCost, JTextField tfPrice, JTextField tfQnt,
			JCheckBox chbVAT, JCheckBox chbTransport, JCheckBox chbVemc) {
		String sp = sugestedPrice.getText();
		sp = sp.replaceAll(cs.EURO, "");
		if(!itemCode.isEmpty())i.setCode(itemCode); else i.setCode(cs.OTHER_CODE);
		if(!tfName.getText().isEmpty())i.setName(tfName.getText());
		if(!tfCost.getText().isEmpty())i.setCost(msh.isDouble(tfCost.getText()));
		if(!isSuggested) {
			if(!tfPrice.getText().isEmpty())i.setPrice(msh.isDouble(tfPrice.getText()));
		} else {
			i.setPrice(msh.isDouble(sp));
		}
		
		if(!tfQnt.getText().isEmpty())i.setQnt(msh.isInt(tfQnt.getText()));
		
		if(chbVAT.isSelected()) i.setAddVat((byte) 1); else i.setAddVat((byte) 0);
		if(chbTransport.isSelected()) i.setAddTransportCost((byte) 1); else i.setAddTransportCost((byte) 0);
		if(chbVemc.isSelected()) i.setAddVEMCCharge((byte) 1); else i.setAddVEMCCharge((byte) 0);
		
		return i;
	}

	private void populateFields(Item i) {
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
		scrollPane.setBounds(20, 66, frame.getWidth()/2 , frame.getHeight() - yOffset);
		frame.getContentPane().add(scrollPane);
	}

	private ListSelectionListener createTableListener() {
		ListSelectionListener listener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				msh.toggleJButton(btnDelete, Color.red, Color.gray, true);
				if(getSelected() != null)
					populateFields(getSelected());
				
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
		vat = 0;
		tran = 0;
		vemc = 0;
		isSuggested = true;
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
	
	public JFrame getFrame() {
		return frame;
	}

}
