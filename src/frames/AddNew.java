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
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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

public class AddNew {
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

	/**
	 * Launch the application.
	 */
	public static void main() {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddNew window = new AddNew();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */public AddNew(){
		 
	 }
	public AddNew(MainView main, DatabaseManager dmn, ConstDB cDB, ConstStrings cS, ConstNums cN, Logger logger,
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
		int lblW = 80, tfW = 260;
		int lbltfH = 20;
		float sHigh = 1.75f;
		frame = new JFrame();
		frame.getContentPane().setBackground(color);
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
		frame.getContentPane().add(name);
		
		JTextField tfName = new JTextField();
		tfName.setFont(fonts);
		tfName.setBounds(xOffset, lblY, tfW, lbltfH);
		frame.getContentPane().add(tfName);
		
		JLabel cost = new JLabel(jl.get(cs.LBL_COST).toString());
		cost.setFont(fonts);
		lblY += yOffset;
		cost.setBounds(lblX, lblY, lblW, lbltfH);
		frame.getContentPane().add(cost);
		
		JTextField tfCost = new JTextField();
		tfCost.setFont(fonts);
		tfCost.setBounds(xOffset, lblY, tfW, lbltfH);
		frame.getContentPane().add(tfCost);
		//TODO
		Border b = BorderFactory.createLineBorder(Color.RED);
		TitledBorder border = BorderFactory.createTitledBorder(b, jl.get(cs.LBL_SUGGESTED_COST).toString());
		int x = lblX + cost.getWidth()+ tfCost.getWidth() + 40;

		JLabel sugestedCost = new JLabel();
		sugestedCost.setFont(fonts);
		sugestedCost.setBounds(x, lblY-15, lblW, (int) (lbltfH*sHigh));
		sugestedCost.setBorder(border);
		frame.getContentPane().add(sugestedCost);

		JLabel price = new JLabel(jl.get(cs.LBL_PRICE).toString());
		price.setFont(fonts);
		lblY += yOffset;
		price.setBounds(lblX, lblY, lblW, lbltfH);
		frame.getContentPane().add(price);
		
		JTextField tfPrice = new JTextField();
		tfPrice.setFont(fonts);
		tfPrice.setBounds(xOffset, lblY, tfW, lbltfH);
		frame.getContentPane().add(tfPrice);
		border = BorderFactory.createTitledBorder(b, jl.get(cs.LBL_SUGGESTED_PRICE).toString());
		
		JLabel sugestedPrice = new JLabel();
		sugestedPrice.setFont(fonts);
		sugestedPrice.setBounds(x, lblY-5, lblW, (int) (lbltfH*sHigh));
		sugestedPrice.setBorder(border);
		frame.getContentPane().add(sugestedPrice);
		
		
		JLabel qnt = new JLabel(jl.get(cs.LBL_QNT).toString());
		qnt.setFont(fonts);
		lblY += yOffset;
		qnt.setBounds(lblX, lblY, lblW, lbltfH);
		frame.getContentPane().add(qnt);
		
		JTextField tfQnt = new JTextField();
		tfQnt.setFont(fonts);
		tfQnt.setBounds(xOffset, lblY, tfW, lbltfH);
		frame.getContentPane().add(tfQnt);
		
		JLabel vat = new JLabel(jl.get(cs.LBL_VAT).toString());
		vat.setFont(fonts);
		lblY += yOffset;
		vat.setBounds(lblX, lblY, lblW, lbltfH);
		frame.getContentPane().add(vat);
		
		JCheckBox chbVAT = new JCheckBox();
		chbVAT.setSelected(true);
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
		chbTransport.setSelected(true);
		chbTransport.setBackground(color);
		chbTransport.setFont(fonts);
		chbTransport.setBounds(xOffset, lblY, tfW, lbltfH);
		frame.getContentPane().add(chbTransport);
		
		JLabel vemc = new JLabel(jl.get(cs.LBL_VEMC).toString());
		vemc.setFont(fonts);
		lblY += yOffset;
		vemc.setBounds(lblX, lblY, lblW, lbltfH);
		frame.getContentPane().add(vemc);
		
		JCheckBox chbVemc = new JCheckBox();
		chbVemc.setSelected(true);
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
				  System.out.println("CHANGE "+tfCost.getText());
			  }
			  public void removeUpdate(DocumentEvent e) {
			  }
			  public void insertUpdate(DocumentEvent e) {
				  System.out.println("INSERT "+tfCost.getText());
//				  TODO
				  //check if number, double
				  
				  //check if check boxes selected
				  
//				  calculate cost
				  
//				  set lblCost
			  }
		});
		
		chbVAT.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				System.out.println("Checked? " + chbVAT.isSelected());
		    }
		});

		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Item i = new Item(dm, cdb, cn, cs, df, "", "", 0,0,0,0,0);
				i = getItem(i, cbCodes, tfName, tfCost, tfPrice, tfQnt, chbVAT, chbTransport, chbVemc);
				System.out.println(""+i.toString());
//				itemSaved = sm.addItem(i);
//				if(itemSaved){
//					JOptionPane.showMessageDialog(frame, jl.get(cs.SAVED_MSG).toString());
//					frame.dispose();
//				}else{
//					JOptionPane.showMessageDialog(frame, jl.get(cs.ITEM_EDITION_ERROR_MSG).toString());
//				}
			}
		});
	}

	protected Item getItem(Item i, JComboBox cbCodes, JTextField tfName, JTextField tfCost, JTextField tfPrice, 
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

		String costStr;
		if(!tfCost.getText().isEmpty()){
			double cost = Double.parseDouble(tfCost.getText());
			costStr = i.calcCost(cost, vat, transport, vemc);
		} else {
			costStr = "0.00";
		}
		i.setCost(Double.parseDouble(costStr));
		
		if(!tfPrice.getText().isEmpty()){
			i.setPrice(Double.parseDouble(df.format(Double.parseDouble(tfPrice.getText()))));
		} else {
			
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
		frame.setVisible(b);
	}
	

}
