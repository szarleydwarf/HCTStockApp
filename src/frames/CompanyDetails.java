package frames;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.json.simple.JSONObject;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstPaths;
import consts.ConstStrings;
import managers.DatabaseManager;
import utility.Logger;
import utility.MiscHelper;

public class CompanyDetails {

	private static JSONObject js;
	private static JSONObject ju;
	private static JSONObject jl;
	private JFrame frame;

	private static DatabaseManager dm;
	private static ConstDB cdb;
	private static ConstNums cn;
	private static ConstStrings cs;
	private static MiscHelper msh;
	private static ConstPaths cp;
	private static Logger log;
	private JTextField tfName;
	private JTextField tfVat;
	private JTextField tfAddress;
	private JTextField tfTown;
	private JTextField tfCounty;
	private JTextField tfPostCode;
	private JTextField tfTelephone;
	private JTextField tfEmail;
	private JTextField tfWWW;
	private JTextField tfFB;
	protected String lang;
	private String info;
	/**
	 * Launch the application.
	 * @param jSettings 
	 * @param jUser 
	 * @param isNew 
	 */
	public static void main(DatabaseManager dmn, Logger logger, 
			ConstDB cDB, ConstStrings cS, ConstNums cN, ConstPaths cP, 
			JSONObject jSettings, JSONObject jUser, JSONObject jLang, MiscHelper msch) {
		jl = jLang;
		js = jSettings;
		ju = jUser;
		msh = msch;
		
		dm = dmn;
		log = logger;
		cdb = cDB;
		cs = cS;
		cn = cN;
		cp = cP;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CompanyDetails window = new CompanyDetails();
					window.frame.setVisible(true);
				} catch (Exception e) {
					log.logError("Fail to display Company Details window. "+e.getMessage());
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CompanyDetails() {
		initialize();
	}

	/* TODO add
	 * VAT number, phone number, email validation 
	*/
	protected boolean saveCompanyDetails() {
		saveLanguage();
		JSONObject newJU = new JSONObject();
		String t = "";
		info = jl.get(cs.FILL_UP).toString();
		boolean allGood = false;
		for (Object key : ju.keySet()) {
	        String keyStr = (String)key;
	        if(keyStr.equals(cs.JSON_COMPANY_NAME) && !validateTextField(this.tfName)){
	        	t = this.tfName.getText();
	        } else if(keyStr.equals(cs.JSON_VAT) && !validateTextField(this.tfVat)) {
	        	t = this.tfVat.getText();
	        } else if(keyStr.equals(cs.JSON_ADDRESS) && !validateTextField(this.tfAddress)) {
	        	t = this.tfAddress.getText();
	        } else if(keyStr.equals(cs.JSON_TOWN) && !validateTextField(this.tfTown)) {
	        	t = this.tfTown.getText();
	        } else if(keyStr.equals(cs.JSON_COUNTY) && !validateTextField(this.tfCounty)) {
	        	t = this.tfCounty.getText();
	        } else if(keyStr.equals(cs.JSON_POST_CODE) && !validateTextField(this.tfPostCode)) {
	        	t = this.tfPostCode.getText();
	        } else if(keyStr.equals(cs.JSON_TELEPHONE) && !validateTextField(this.tfTelephone)) {
	        	t = this.tfTelephone.getText();
	        } else if(keyStr.equals(cs.JSON_EMAIL) && !validateTextField(this.tfEmail)) {
	        	t = this.tfEmail.getText();
	        } else if(keyStr.equals(cs.JSON_WWW) && !validateTextField(this.tfWWW)) {
	        	t = this.tfWWW.getText();
	        } else if(keyStr.equals(cs.JSON_FB) && !validateTextField(this.tfFB)) {
	        	t = this.tfFB.getText();
	        }
	        newJU.put(keyStr, t);
	    }
        if(!info.equals(jl.get(cs.FILL_UP).toString())){
        	return false;
        }
		return msh.saveJSON(cp.JSON_USER_PATH, newJU);
	}

	private boolean validateTextField(JTextField tf) {
		if(tf.getName() != null && tf.getText().isEmpty()){
			if(tf.getName().equals(cs.JSON_COMPANY_NAME))
				info += " Company Name,";
			if(tf.getName().equals(cs.JSON_VAT))
				info += " VAT number,";
			if(tf.getName().equals(cs.JSON_TOWN))
				info += " Town,";
			if(tf.getName().equals(cs.JSON_ADDRESS))
				info += " Address,";
			if(tf.getName().equals(cs.JSON_COUNTY))
				info += " County,";
		}
		return tf.getText().isEmpty();
	}

	private boolean saveLanguage() {
		JSONObject newJS = new JSONObject();
		String t = "";
		for (Object key : js.keySet()) {
			String keyStr = (String)key;
			if(keyStr.equals(cs.JLANG)){
				if(lang != null)
					t = lang;
				else
					t = cp.JSON_ENG;
			} else {
				t = (String) js.get(keyStr);
			}
	    	newJS.put(keyStr, t);
		}
		return msh.saveJSON(cp.JSON_SETTINGS_PATH, newJS);
	}

//	private boolean saveJSON(String path, JSONObject object){
//        try (FileWriter file = new FileWriter(path)) {
//            file.write(object.toJSONString());
//            file.flush();
//            return true;
//        } catch (IOException e) {
//        	log.logError("Fail to save JSON file in "+this.getClass().getName() + ". E: " + e.getMessage());
//            e.printStackTrace();
//        }
//        return false;
//	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		System.out.println("Gattering info");
		Font fonts = new Font(js.get(cs.FONT).toString(), Font.PLAIN, Integer.parseInt(js.get(cs.FONT_SIZE_DEF).toString()));
		Font fonts_title = new Font(js.get(cs.FONT).toString(), Font.PLAIN, Integer.parseInt(js.get(cs.FONT_SIZE_TITLE).toString()));
		Map attributes = fonts_title.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		int yPos = 42, xPos = 30, line = 26, tfxPos = 130, tfLength = 540, tfHeight = 24;
		Color color = msh.getColor(cs.APP, cs, js);
		
		frame = new JFrame();
		frame.getContentPane().setBackground(color);
		frame.setBounds(cn.FRAME_X_BOUND, cn.FRAME_Y_BOUND, 704, 410);
		
		JLabel lblTitle = new JLabel(jl.get(cs.ENTER_DETAILS).toString());
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(0, 5, 700, 24);
		lblTitle.setFont(fonts_title.deriveFont(attributes));
		frame.getContentPane().add(lblTitle);
		
		JLabel lblLang = new JLabel("Language");
		lblLang.setBounds(xPos, yPos, 96, 28);
		lblLang.setFont(fonts);
		frame.getContentPane().add(lblLang);

		JComboBox cbLang = new JComboBox(cs.LANG);
		cbLang.setFont(fonts);
		cbLang.setBounds(tfxPos, yPos, tfLength/2, tfHeight);
		frame.getContentPane().add(cbLang);
		
		cbLang.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbLang ){
					JComboBox cb = (JComboBox) a.getSource();
					lang = (cb.getSelectedItem().toString());				
					if(lang.equals(cs.LANG[0])){
						lang = cp.JSON_ENG;
					} else {
						lang = cp.JSON_PL;
					}
				}		
			}
		});

		
		JLabel lblName = new JLabel(cs.JSON_COMPANY_NAME+cs.STAR);
		yPos+=line;
		lblName.setBounds(xPos, yPos, 96, 28);
		lblName.setFont(fonts);
		frame.getContentPane().add(lblName);

		tfName = new JTextField();
		tfName.setBounds(tfxPos, yPos, tfLength, tfHeight);
		tfName.setName(cs.JSON_COMPANY_NAME);
		frame.getContentPane().add(tfName);
		tfName.setColumns(10);


		JLabel lblVAT = new JLabel(cs.JSON_VAT+cs.STAR);
		yPos+=line;
		lblVAT.setBounds(xPos, yPos, 96, 28);
		lblVAT.setFont(fonts);
		frame.getContentPane().add(lblVAT);
		
		tfVat = new JTextField();
		tfVat.setColumns(10);
		tfVat.setBounds(tfxPos, yPos, tfLength, tfHeight);
		tfVat.setName(cs.JSON_VAT);
		frame.getContentPane().add(tfVat);
	
		JLabel lblAddress = new JLabel(cs.JSON_ADDRESS+cs.STAR);
		yPos+=line;
		lblAddress.setBounds(xPos, yPos, 96, 28);
		lblAddress.setFont(fonts);
		frame.getContentPane().add(lblAddress);
		
		tfAddress = new JTextField();
		tfAddress.setColumns(10);
		tfAddress.setBounds(tfxPos, yPos, tfLength, tfHeight);
		tfAddress.setName(cs.JSON_ADDRESS);
		frame.getContentPane().add(tfAddress);
		
		JLabel lblTown = new JLabel(cs.JSON_TOWN+cs.STAR);
		yPos+=line;
		lblTown.setBounds(xPos, yPos, 96, 28);
		lblTown.setFont(fonts);
		frame.getContentPane().add(lblTown);
		
		tfTown = new JTextField();
		tfTown.setColumns(10);
		tfTown.setName(cs.JSON_TOWN);
		tfTown.setBounds(tfxPos, yPos, tfLength, tfHeight);
		frame.getContentPane().add(tfTown);
		
		JLabel lblCounty = new JLabel(cs.JSON_COUNTY+cs.STAR);
		yPos+=line;
		lblCounty.setBounds(xPos, yPos, 96, 28);
		lblCounty.setFont(fonts);
		frame.getContentPane().add(lblCounty);
		
		tfCounty = new JTextField();
		tfCounty.setColumns(10);
		tfCounty.setName(cs.JSON_COUNTY);
		tfCounty.setBounds(tfxPos, yPos, tfLength, tfHeight);
		frame.getContentPane().add(tfCounty);

		JLabel lblPostCode = new JLabel(cs.JSON_POST_CODE);
		yPos+=line;
		lblPostCode.setBounds(xPos, yPos, 96, 28);
		lblPostCode.setFont(fonts);
		frame.getContentPane().add(lblPostCode);
		
		tfPostCode = new JTextField();
		tfPostCode.setColumns(10);
		tfPostCode.setName(cs.JSON_POST_CODE);
		tfPostCode.setBounds(tfxPos, yPos, tfLength, tfHeight);
		frame.getContentPane().add(tfPostCode);

		JLabel lblTelephone = new JLabel(cs.JSON_TELEPHONE);
		yPos+=line;
		lblTelephone.setBounds(xPos, yPos, 96, 28);
		lblTelephone.setFont(fonts);
		frame.getContentPane().add(lblTelephone);
		
		tfTelephone = new JTextField();
		tfTelephone.setColumns(10);
		tfTelephone.setName(cs.JSON_TELEPHONE);
		tfTelephone.setBounds(tfxPos, yPos, tfLength, tfHeight);
		frame.getContentPane().add(tfTelephone);
		
		JLabel lblEmail = new JLabel(cs.JSON_EMAIL);
		yPos+=line;
		lblEmail.setBounds(xPos, yPos, 96, 28);
		lblEmail.setFont(fonts);
		frame.getContentPane().add(lblEmail);
		
		tfEmail = new JTextField();
		tfEmail.setColumns(10);
		tfEmail.setName(cs.JSON_EMAIL);
		tfEmail.setBounds(tfxPos, yPos, tfLength, tfHeight);
		frame.getContentPane().add(tfEmail);
		
		JLabel lblWWW = new JLabel(cs.JSON_WWW);
		yPos+=line;
		lblWWW.setBounds(xPos, yPos, 96, 28);
		lblWWW.setFont(fonts);
		frame.getContentPane().add(lblWWW);
		
		tfWWW = new JTextField();
		tfWWW.setColumns(10);
		tfWWW.setName(cs.JSON_WWW);
		tfWWW.setBounds(tfxPos, yPos, tfLength, tfHeight);
		frame.getContentPane().add(tfWWW);

		JLabel lblFB = new JLabel(cs.JSON_FB);
		yPos+=line;
		lblFB.setBounds(xPos, yPos, 96, 28);
		lblFB.setFont(fonts);
		frame.getContentPane().add(lblFB);
		
		tfFB = new JTextField();
		tfFB.setColumns(10);
		tfFB.setName(cs.JSON_FB);
		tfFB.setBounds(tfxPos, yPos, tfLength, tfHeight);
		frame.getContentPane().add(tfFB);

		
		JButton nextBtn = new JButton(jl.get(cs.BTN_SAVE).toString());
		nextBtn.setBounds(478, 330, 200, 36);
		nextBtn.setBackground(new Color(135, 206, 235));
		nextBtn.setFont(fonts);
		nextBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean saved = saveCompanyDetails();
				if(saved){
					frame.dispose();
					MainView.main(null);
				} else{
		        	info = msh.removeLastChar(info, ',');
		        	JOptionPane.showMessageDialog(frame, info);
				}
			}
		});
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(nextBtn);
	}

	
	// GETTERS & SETTERS
	public static JSONObject getJdl() {
		return js;
	}

	public static JSONObject getJu() {
		return ju;
	}

	public static JSONObject getJL() {
		return jl;
	}
		
	public static DatabaseManager getDm() {
		return dm;
	}

	public static ConstDB getCdb() {
		return cdb;
	}

	public static ConstNums getCn() {
		return cn;
	}

	public static ConstStrings getCs() {
		return cs;
	}
}
