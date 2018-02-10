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
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

import org.json.simple.JSONObject;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstStrings;
import managers.DatabaseManager;
import managers.StockManager;
import utility.Logger;
import utility.MiscHelper;

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
	private static MiscHelper msh;
	private static String[][] data;
	private static StockManager sm;

	/**
	 * Launch the application.
	 */
	public static void main(DatabaseManager dmn, ConstDB cDB, ConstStrings cS, ConstNums cN, Logger logger,
			JSONObject jSettings, JSONObject jLang, MiscHelper mSH, StockManager SM) {
		jl = jLang;
		js = jSettings;

		dm = dmn;
		log = logger;
		cdb = cDB;
		cs = cS;
		cn = cN;
//		cp = cP;
		
		msh = mSH;
		
		data = SM.getData();
		sm = SM;

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DisplayStock window = new DisplayStock();
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
	public DisplayStock() {
		initialize();
	}

	public DisplayStock(MainView main, DatabaseManager dmn, ConstDB cDB, ConstStrings cS, ConstNums cN, Logger logger,
			JSONObject jSettings, JSONObject jLang, MiscHelper mSH, StockManager SM) {
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
		
		data = SM.getData();
		sm = SM;

		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Font fonts = new Font(js.get(cs.FONT).toString(), Font.PLAIN, Integer.parseInt(js.get(cs.FONT_SIZE_DEF).toString()));
		Font fonts_title = new Font(js.get(cs.FONT).toString(), Font.PLAIN, Integer.parseInt(js.get(cs.FONT_SIZE_TITLE).toString()));
		Map attributes = fonts_title.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		Color color = msh.getColor(cs.APP, cs, js);

		frame = new JFrame();
		this.setIsVisible(false);
		frame.getContentPane().setBackground(color);
		frame.setBounds(cn.FRAME_X_BOUND, cn.FRAME_Y_BOUND, (msh.getScreenDimension()[0]), (msh.getScreenDimension()[1]));
		frame.getContentPane().setLayout(null);

		JLabel lblTitle = new JLabel(jl.get(cs.STOCK).toString());
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(fonts_title);
		lblTitle.setBounds(msh.getXenterXofFrame(frame, lblTitle), 10, 260, 24);
		frame.getContentPane().add(lblTitle);

		JLabel lblSearch = new JLabel(jl.get(cs.SEARCH_TEXT_FIELD_FRAZE).toString());
		lblSearch.setHorizontalAlignment(SwingConstants.CENTER);
		lblSearch.setFont(fonts);
		lblSearch.setBounds(10, 40, 200, 24);
		frame.getContentPane().add(lblSearch);
		
		tfSearch = new JTextField();
		tfSearch.setHorizontalAlignment(SwingConstants.CENTER);
		tfSearch.setFont(fonts);
		tfSearch.setBounds(240, 40, 300, 24);
		frame.getContentPane().add(tfSearch);
		tfSearch.setColumns(10);
		
		JButton btnBack = new JButton(jl.get(cs.BACK).toString());
		btnBack.setFont(fonts_title);
		btnBack.setBounds((frame.getWidth() - cn.BACK_BTN_X_OFFSET), (frame.getHeight() - cn.BACK_BTN_Y_OFFSET), cn.BACK_BTN_WIDTH, cn.BACK_BTN_HEIGHT);
		frame.getContentPane().add(btnBack);

		btnEdit = new JButton(jl.get(cs.EDIT).toString());
		btnEdit.setForeground(new Color(245, 245, 245));
		btnEdit.setBackground(Color.GRAY);
		btnEdit.setFont(fonts);
		btnEdit.setBounds((frame.getWidth() - cn.BACK_BTN_X_OFFSET), 40, cn.BACK_BTN_WIDTH, cn.BACK_BTN_HEIGHT);
		btnEdit.setEnabled(false);
		frame.getContentPane().add(btnEdit);

		
		createTable();
		
		// LISTENERS SECTION
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				editRecordInDatabase();
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

		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(frame, 
			            jl.get(cs.CLOSE).toString(), "", 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		        	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					if(mainView != null)
						if(!mainView.isVisible())
							mainView.setIsVisible(true);
		        }
		    }
		});		
	}

	protected void editRecordInDatabase() {
		// TODO Auto-generated method stub
		
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
//				helper.toggleJButton(btnDelete, Color.red, Color.gray, true);
				msh.toggleJButton(btnEdit, Color.yellow, Color.GREEN, true);	
			}
	    };
	    return listener;

	}

	// GETTERS & SETTERS
	public boolean isVisible(){
		return frame.isVisible();
	}
	
	public void setIsVisible(boolean b){
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
