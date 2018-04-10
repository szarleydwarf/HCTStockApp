package frames;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.font.TextAttribute;
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
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.json.simple.JSONObject;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstStrings;
import managers.DatabaseManager;
import managers.StockManager;
import objects.Item;
import utility.Logger;
import utility.MiscHelper;

public class ItemAddNew {
	private  ConstNums cn;
	private  ConstStrings cs;
	private  ConstDB cdb;
	private  Logger log;
	private  DatabaseManager dm;
	private  JSONObject js;
	private  JSONObject jl;
	private  MainView mainView;
	private Font fonts;
	private Font fonts_title;
	private Map attributes;
	private Color color;
	private DecimalFormat df;
	private  MiscHelper msh;
	private  StockManager sm;

	private JFrame frame;
	protected boolean itemSaved;
	protected String itemCode;
	private Item i;
	private int vat, tran, vemc;
	protected double d_cost;
	protected String cost;
	private JLabel sugestedCost;
	private JLabel sugestedPrice;
	private JRadioButton rbTypedIn;
	private JRadioButton rbSuggested;
	private String price;
	protected boolean isSuggested;

	/**
	 * Launch the application.
	 */
	public static void main() {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ItemAddNew window = new ItemAddNew();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */public ItemAddNew(){
		 
	 }
	public ItemAddNew(MainView main, DatabaseManager dmn, ConstDB cDB, ConstStrings cS, ConstNums cN, Logger logger,
			JSONObject jSettings, JSONObject jLang, MiscHelper mSH, StockManager SM, DecimalFormat df_3_2) {
		mainView = main;
		jl = jLang;
		js = jSettings;

		dm = dmn;
		log = logger;
		cdb = cDB;
		cs = cS;
		cn = cN;
//		cp = cP;
		
		msh = mSH;
		
		
		sm = SM;
		
		df = df_3_2;
	
		this.itemCode = cs.OTHER_CODE;
		vat = 0;
		tran = 0;
		vemc = 0;
		
		isSuggested = true;
		
		fonts = new Font(js.get(cs.FONT).toString(), Font.PLAIN, Integer.parseInt(js.get(cs.FONT_SIZE_DEF).toString()));
		fonts_title = new Font(js.get(cs.FONT).toString(), Font.PLAIN, Integer.parseInt(js.get(cs.FONT_SIZE_TITLE).toString()));
		attributes = fonts_title.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		color = msh.getColor(cs.APP, cs, js);
//		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		int lblX = 10, lblY = 10; 
		int xOffset = 120, yOffset = 24;
		int lblW = 80, tfW = 260, rbW = 110;
		int lbltfH = 20;
		float sHigh = 1.75f;
		frame = new JFrame();
		frame.getContentPane().setBackground(color);
		frame.setTitle(jl.get(cs.LBL_STOCK).toString());
		frame.setBounds(cn.FRAME_X_BOUND, cn.FRAME_Y_BOUND, cn.ADD_EDIT_FRAME_WIDTH, cn.ADD_EDIT_FRAME_HEIGHT);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
				
		JLabel code = new JLabel(jl.get(cs.LBL_CODE).toString());
		code.setFont(fonts);
		code.setBounds(lblX, lblY, lblW, lbltfH);
		frame.getContentPane().add(code);
		
		JComboBox cbCodes = new JComboBox(cs.ITEM_CODES);
		cbCodes.setBounds(xOffset, lblY, tfW, lbltfH);
		frame.getContentPane().add(cbCodes);

		JLabel name = new JLabel(jl.get(cs.LBL_NAME).toString());
		name.setFont(fonts);
		lblY += yOffset;
		name.setBounds(lblX, lblY, lblW, lbltfH);
		frame.getContentPane().add(name);
		
		JTextField tfName = new JTextField();
		tfName.setFont(fonts);
		tfName.setBounds(xOffset, lblY, tfW, lbltfH);
		frame.getContentPane().add(tfName);
		
		JLabel lblCost = new JLabel(jl.get(cs.LBL_COST).toString());
		lblCost.setFont(fonts);
		lblY += yOffset;
		lblCost.setBounds(lblX, lblY, lblW, lbltfH);
		frame.getContentPane().add(lblCost);
		
		JTextField tfCost = new JTextField();
		tfCost.setFont(fonts);
		tfCost.setBounds(xOffset, lblY, tfW, lbltfH);
		frame.getContentPane().add(tfCost);
		//TODO - add radio button???
		Border b = BorderFactory.createLineBorder(Color.RED);
		TitledBorder border = BorderFactory.createTitledBorder(b, jl.get(cs.LBL_SUGGESTED_COST).toString());
		int x = lblX + lblCost.getWidth()+ tfCost.getWidth() + 40;

		sugestedCost = new JLabel();
		sugestedCost.setFont(fonts);
		sugestedCost.setBounds(x, lblY-15, (int) (lblW*1.25), (int) (lbltfH*sHigh));
		sugestedCost.setBorder(border);
		frame.getContentPane().add(sugestedCost);

		JLabel lblPrice = new JLabel(jl.get(cs.LBL_PRICE).toString());
		lblPrice.setFont(fonts);
		lblY += yOffset;
		lblPrice.setBounds(lblX, lblY, lblW, lbltfH);
		frame.getContentPane().add(lblPrice);
		
		JTextField tfPrice = new JTextField();
		tfPrice.setFont(fonts);
		tfPrice.setBounds(xOffset, lblY, tfW, lbltfH);
		frame.getContentPane().add(tfPrice);
		border = BorderFactory.createTitledBorder(b, jl.get(cs.LBL_SUGGESTED_PRICE).toString());
		
		sugestedPrice = new JLabel();
		sugestedPrice.setFont(fonts);
		sugestedPrice.setBounds(x, lblY-5, (int) (lblW*1.25), (int) (lbltfH*sHigh));
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
		
		JTextField tfQnt = new JTextField();
		tfQnt.setFont(fonts);
		tfQnt.setBounds(xOffset, lblY, tfW, lbltfH);
		frame.getContentPane().add(tfQnt);
		
		JLabel lblVat = new JLabel(jl.get(cs.LBL_VAT).toString());
		lblVat.setFont(fonts);
		lblY += yOffset;
		lblVat.setBounds(lblX, lblY, lblW, lbltfH);
		frame.getContentPane().add(lblVat);
		
		JCheckBox chbVAT = new JCheckBox();
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
		
		JCheckBox chbTransport = new JCheckBox();
		chbTransport.setSelected(Boolean.parseBoolean(""+tran));
		chbTransport.setBackground(color);
		chbTransport.setFont(fonts);
		chbTransport.setBounds(xOffset, lblY, tfW, lbltfH);
		frame.getContentPane().add(chbTransport);
		
		JLabel lblVemc = new JLabel(jl.get(cs.LBL_VEMC).toString());
		lblVemc.setFont(fonts);
		lblY += yOffset;
		lblVemc.setBounds(lblX, lblY, lblW, lbltfH);
		frame.getContentPane().add(lblVemc);
		
		JCheckBox chbVemc = new JCheckBox();
		chbVemc.setSelected(Boolean.parseBoolean(""+vemc));
		chbVemc.setBackground(color);
		chbVemc.setFont(fonts);
		chbVemc.setBounds(xOffset, lblY, tfW, lbltfH);
		frame.getContentPane().add(chbVemc);
		
		JButton btnSave = new JButton(jl.get(cs.BTN_SAVE).toString());
		btnSave.setForeground(Color.RED);
		btnSave.setBackground(Color.LIGHT_GRAY);
		btnSave.setFont(fonts);
		lblY += yOffset;
		btnSave.setBounds(xOffset, lblY, tfW, lbltfH+10);
		frame.getContentPane().add(btnSave);

		// LISTENERS SECTION
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

		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				i = getItem(i, cbCodes, tfName, tfPrice, tfQnt, chbVAT, chbTransport, chbVemc);
				System.out.println(""+i.toString());
				itemSaved = sm.addItem(i);
				if(itemSaved){
					JOptionPane.showMessageDialog(frame, jl.get(cs.SAVED_MSG).toString());
					frame.dispose();
				}else{
					log.logError(" - " + this.getClass().getName() + " ~ " + jl.get(cs.ITEM_ADD_ERROR).toString());
					JOptionPane.showMessageDialog(frame, jl.get(cs.ITEM_ADD_ERROR).toString());
				}
			}
		});
	}

	protected void recalculateCost(JTextField tfCost) {
		  d_cost = 0;
		  cost = ""; price = "";
		  try {
			  d_cost = Double.parseDouble(tfCost.getText()); 
		  } catch (NumberFormatException nfe) {
			  if(!tfCost.getText().isEmpty())
				  JOptionPane.showMessageDialog(frame, jl.get(cs.NOT_NUMBER_ERROR).toString());
		  }
		  
		  if(d_cost > 0){
			  cost = calculateCost();
			  price = calculatePrice();
		  }
		  setCostLBL();
		  setPriceLBL();
	}

	private String calculatePrice() {
		double dc = Double.parseDouble(cost);
		return i.calculateSuggestedPrice(dc, itemCode);
	}

	private void setPriceLBL() {
		sugestedPrice.setText(cs.EURO + price);
	}

	protected void setCostLBL() {
		sugestedCost.setText(cs.EURO + cost);
	}

	protected String calculateCost() {
		return i.calcCost(d_cost, vat, tran, vemc);
	}

	protected Item getItem(Item i, JComboBox cbCodes, JTextField tfName,  JTextField tfPrice,  
			JTextField tfQnt, JCheckBox chbVAT, JCheckBox chbTransport, JCheckBox chbVemc) {
		if(!itemCode.isEmpty())i.setCode(itemCode); else i.setCode(cs.OTHER_CODE);
		if(!tfName.getText().isEmpty())i.setName(tfName.getText());

		int vat, transport, vemc;
		
		if(chbVAT.isSelected()){ 
			vat = 1;
		} else {
			vat = 0;
		}
		i.setAddVat((byte) vat);

		if(chbTransport.isSelected()) {
			transport = 1;
		} else {
			transport = 0;
		}
		i.setAddTransportCost((byte) transport);
		
		if(chbVemc.isSelected()) {
			vemc = 1;
		} else {
			vemc = 0;
		}
		i.setAddVEMCCharge((byte) vemc);

		if(!cost.equals(cs.EURO) && !cost.isEmpty())
			i.setCost(Double.parseDouble(cost));
		
		if(!price.equals(cs.EURO) && !price.isEmpty()) {
			String pt = price;
			if(!isSuggested)
				pt = tfPrice.getText();
			else pt = price;
			i.setPrice(Double.parseDouble(pt));
		}
		
		if(!tfQnt.getText().isEmpty())i.setQnt(Integer.parseInt(tfQnt.getText()));
		return i;
	}
	public boolean isVisible(){
		if(frame != null)
			return frame.isVisible();
		return false;
	}
	
	public void setIsVisible(boolean b){
		initialize();
		i = new Item(dm, cdb, cn, cs, df, "", "", 0,0,0,0,0);

		frame.setVisible(b);
	}
	

}
