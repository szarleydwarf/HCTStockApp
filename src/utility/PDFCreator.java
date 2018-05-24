package utility;

import java.awt.Color;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.table.TableModel;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.json.simple.JSONObject;

import consts.ConstNums;
import consts.ConstPaths;
import consts.ConstStrings;
import objects.Customer;
import objects.CustomerBusiness;
import objects.CustomerInd;
import objects.Invoice;

public class PDFCreator {
	private JSONObject jl;
	private JSONObject js;
	private Logger log;
	private ConstStrings cs;
	private ConstNums cn;
	private ConstPaths cp;

	private MiscHelper msh;
	private DateHelper dh;
	private DecimalFormat df;
	private int fonts;
	private int fonts_title;
	private Color color;
	private String date;
	private JSONObject ju;

	public PDFCreator(ConstStrings cS, ConstNums cN, ConstPaths CP, Logger logger,
			JSONObject jSettings, JSONObject jLang, JSONObject jUser,
			MiscHelper mSH, DateHelper DH, DecimalFormat df_3_2){
		this.jl = jLang;
		this.js = jSettings;
		this.ju = jUser;

		this.log = logger;
		this.cs = cS;
		this.cn = cN;
		this.cp = CP;
		
		this.msh = mSH;
		this.dh = DH;
		
		this.df = df_3_2;
		
		this.fonts_title = Integer.parseInt(js.get(cs.FONT_SIZE_TITLE).toString());
		this.fonts = Integer.parseInt(js.get(cs.FONT_SIZE_DEF).toString()) - 2;
		this.color = msh.getColor(cs.APP, cs, js);
		this.date = dh.getFormatedDate();
	}

	//create pdf sending doctype???
	public PDDocument createPDF(String docType, Object object1, Object obj2, String date) {
		// TODO Auto-generated method stub
		PDDocument pdd = null;
		if (docType.equals(cs.PDF_INVOICE)) {
			Invoice i = (Invoice) object1;
			Customer customer = (Customer) obj2;
			try {
				pdd = createInvoice(i, customer, date);
			} catch (IOException e) {
				log.log(cs.ERR_LOG, "INVOICE " + jl.get(cs.PDF_CREATION_ERROR).toString());
				e.printStackTrace();
			}
		} else if (docType.equals(cs.PDF_SALE_REPORT)){
			String[][] data = (String[][]) object1;
			String header = (String) obj2;
			try {
				pdd = createSaleRep(data, header, date);
			} catch (IOException e) {
				log.log(cs.ERR_LOG, "SALE REPORT " + jl.get(cs.PDF_CREATION_ERROR).toString());
				e.printStackTrace();
			}
		} else if (docType.equals(cs.PDF_STOCK_REPORT)){
			String[][] data = (String[][]) object1;
			String header = (String) obj2;
			try {
				pdd = createStockRep(data, header, date);
			} catch (IOException e) {
				log.log(cs.ERR_LOG, "STOCK REPORT " + jl.get(cs.PDF_CREATION_ERROR).toString());
				e.printStackTrace();
			}
		} else if (docType.equals(ConstStrings.PDF_REPAK_REPORT)){
			pdd = createRepakRep();
		} else {
			JOptionPane.showMessageDialog(null, "GENERAL " + jl.get(cs.PDF_CREATION_ERROR).toString());
		}
		return pdd;
	}

	private PDDocument createStockRep(String[][] data, String header, String date) throws IOException {
		// TODO work in progress
		PDDocument pdd = new PDDocument();
		PDPage page = new PDPage();
		pdd.addPage(page);
		PDPageContentStream cst = new PDPageContentStream(pdd, page);
		return pdd;
	}
	
	private PDDocument createRepakRep() {
		// TODO Auto-generated method stub
		return null;
	}

	// CREATE INVOICE SECTION
	private PDDocument createInvoice(Invoice i, Customer customer, String date) throws IOException {
		PDDocument pdd = new PDDocument();
		PDPage page = new PDPage();
		pdd.addPage(page);
		PDPageContentStream cst = new PDPageContentStream(pdd, page);
		// fill company details
		addLogo(cst, pdd);
		fillCompanyDetails(cst, date);
		// fill for who
		fillCustomerDetails(cst, i, customer);
		// fill what sold
		fillSales(cst, i);
		// fill customer msg
		if(saleListContainTyres(i))
			addMessage(cst);
		
		cst.close();
		return pdd;
	}

	private void fillSales(PDPageContentStream cst, Invoice i) throws IOException {
		displayHeadings(cst, jl.get(cs.PDF_SALES_HEADER).toString());
//		float yPos = 440f+cn.LEADING_LINE_SIZE;
		String[] salesList = msh.splitString(i.getList(), cs.SEMICOLON);
		if(salesList.length > 0){
			int count = 1;
			for (int j = 0; j < salesList.length-1; j++) {
				int qnt = Integer.parseInt(salesList[j].substring(0, salesList[j].indexOf(cs.STAR)));
				String description = salesList[j].substring(salesList[j].indexOf(cs.STAR)+1, salesList[j].indexOf(cs.HASH));
				description = msh.paddStringRight(description, cn.DESCRPTION_LENGTH, cs.DOT);
				double price = Double.parseDouble(salesList[j].substring(salesList[j].indexOf(cs.AT)+1));
				cst.showText(count + ". " + description + "  -  " + cs.EURO  + df.format(price) + " * " + qnt);
				cst.newLine();
				count++;
			}
		}
		cst.newLine();	
		cst.setFont(PDType1Font.COURIER, this.fonts_title);
		
		String symbol = cs.EURO;
		if(i.isPercent() == 1)
			symbol = cs.PERCENT;
		
		if(i.getDiscount()>0)
			cst.showText("                         Discount          "+symbol+" "+i.getDiscount());
		cst.newLine();	
		cst.showText("                         TOTAL            € "+df.format(i.getTotal()));
				
		cst.newLine();	
		cst.endText();
	}

	private boolean saleListContainTyres(Invoice i) {
		return (i.getList().contains(cs.TYRE_CODE_C) || i.getList().contains(cs.TYRE_CODE_A)) ? true : false;
	}

	private void addMessage(PDPageContentStream cst) throws IOException {
		cst.beginText();
		cst.newLineAtOffset(cn.PDF_DOC_X_OFFSET,  100f);
		cst.setLeading(cn.LEADING_LINE_SIZE);
		cst.setNonStrokingColor(Color.BLACK);
		cst.setFont(PDType1Font.COURIER, fonts);
		
		cst.showText(jl.get(cs.TYRE_CHECK_MESSAGE_1).toString());
		cst.newLine();
		cst.showText(jl.get(cs.TYRE_CHECK_MESSAGE_2).toString());
		cst.newLine();
		cst.showText(jl.get(cs.TYRE_CHECK_MESSAGE_3).toString());
		cst.endText();
	}
	
	private void fillCustomerDetails(PDPageContentStream cst, Invoice i, Customer customer) throws IOException {
		cst.beginText();
		cst.setNonStrokingColor(Color.BLACK);
		cst.setFont(PDType1Font.COURIER_BOLD, this.fonts_title);
		cst.setLeading(cn.LEADING_LINE_SIZE);
		
		cst.newLineAtOffset(20, 530);
		if(!i.isBusiness()){
			CustomerInd c = null;
			if(customer != null)
				c = (CustomerInd) customer;
			if(c != null)
				cst.showText("Invoice No: "+i.getId()+" for "+c.getCar().getBrandString());
			else
				cst.showText("Invoice No: "+i.getId()+" for "+jl.get(cs.OTHER_STRING).toString());
		} else {
			CustomerBusiness c = null;
			if(customer != null)
				c = (CustomerBusiness) customer;
			if(c != null){
				cst.showText("Invoice # "+i.getId()+" for "+c.getCompName());
				cst.newLine();
				cst.showText(c.getCompAddress() + " " + c.getVATTaxNUM());
			}
		}
		cst.endText();
	}


	// END CREATE INVOICE SECTION
	
	// SALES REPORTS SECTION
	private PDDocument createSaleRep(String[][] data, String header, String date) throws IOException {
		PDDocument pdd = new PDDocument();
		PDPage page = new PDPage();
		pdd.addPage(page);
		PDPageContentStream cst = new PDPageContentStream(pdd, page);
		
		addLogo(cst, pdd);
		fillCompanyDetails(cst, date);
		fillDate(cst, date);
		fillSalesReport(cst, data, header);
		
		cst.close();
		return pdd;
	}

	private void fillSalesReport(PDPageContentStream cst, String[][] data, String header) throws IOException {
		displayHeadings(cst, header);
		double  sc = 0, sp = 0, ss = 0;
//		String tt = msh.paddStringRight("***TOTAL ", cn.SALE_REPORT_PAD_LENGTH, cs.UNDERSCORE);
		if(data.length > 0) {
			for (int i = 0; i < data.length; i++) {
				String code = data[i][0];
//				double cost = data[i][1];
				
				
				String s = "", t = "";
				double d = 0;
				boolean b;
				for (int j = 0; j < data[i].length; j++) {
					if(j == 0){
						s = data[i][j];
						b = false;
						s = msh.paddStringRight(s, cn.SALE_REPORT_PAD_LENGTH, cs.UNDERSCORE);
					} else {
						d = Double.parseDouble(data[i][j]);
						switch (j) {
						case 1:
							sc += d;
							break;
						case 2:
							ss += d;
							break;
						case 3:
							sp += d;
							break;

						default:
							break;
						}
						t = "€ "+df.format(d);
						b = true;
						t = msh.paddStringRight(t, cn.SALE_REPORT_PAD_LENGTH, cs.UNDERSCORE);
					}
					s += t;
				}
				s = msh.removeLastChar(s, cs.UNDERSCORE);
				cst.showText(s);
				cst.newLine();
			}
			cst.newLine();
//			tt += msh.removeLastChar(msh.paddStringRight("€ "+df.format(sc), cn.SALE_REPORT_PAD_LENGTH, cs.UNDERSCORE) 
//					+ msh.paddStringRight("€ "+df.format(ss), cn.SALE_REPORT_PAD_LENGTH, cs.UNDERSCORE)
//					+ msh.paddStringRight("€ "+df.format(sp), cn.SALE_REPORT_PAD_LENGTH, cs.UNDERSCORE), cs.UNDERSCORE);
//			
//			cst.showText(tt);
		}
		cst.endText();
	}
	// END SALES REPORTS SECTION

	// GENERAL SECTION
	private void fillDate(PDPageContentStream cst, String date) throws IOException {
		cst.beginText();
		cst.setNonStrokingColor(Color.BLACK);
		cst.setFont(PDType1Font.COURIER_BOLD, this.fonts_title);
		cst.setLeading(cn.LEADING_LINE_SIZE);
		cst.newLineAtOffset(20, 530);
		cst.showText(jl.get(cs.BTN_SALES_REPORT).toString() + " from "+date);
		
		cst.endText();
	}

	private void displayHeadings(PDPageContentStream cst, String header) throws IOException {
		cst.beginText();
		cst.setNonStrokingColor(Color.lightGray);
		cst.setFont(PDType1Font.COURIER_BOLD, this.fonts_title);
		cst.newLineAtOffset(cn.PDF_DOC_X_OFFSET,  440f);
		cst.setLeading(cn.LEADING_LINE_SIZE);
		cst.showText(header);
		cst.newLine();
		cst.newLine();
		cst.setNonStrokingColor(Color.BLACK);
		cst.setFont(PDType1Font.COURIER, fonts);
	}

//TODO add accountancy copy boolean
	private void fillCompanyDetails(PDPageContentStream cst, String date) throws IOException {
		cst.beginText();
		cst.setNonStrokingColor(Color.BLACK);
		cst.setFont(PDType1Font.COURIER, this.fonts_title);
		cst.newLineAtOffset(cn.PDF_DOC_X_OFFSET,  740);
		cst.setLeading(cn.LEADING_LINE_SIZE);
		
		cst.showText(ju.get(cs.JSON_COMPANY_NAME).toString() + "                                           " + date);
		cst.newLine();
		
		cst.setFont(PDType1Font.COURIER, this.fonts);
		cst.showText(ju.get(cs.JSON_ADDRESS).toString() + " " + ju.get(cs.JSON_TOWN).toString());
		cst.newLine();
		cst.showText(ju.get(cs.JSON_COUNTY).toString());
		cst.newLine();
		cst.showText(ju.get(cs.JSON_POST_CODE).toString());
		cst.newLine();
		cst.showText(ju.get(cs.JSON_TELEPHONE).toString());
		cst.newLine();
		cst.showText(ju.get(cs.JSON_EMAIL).toString());
		cst.newLine();
		cst.showText(ju.get(cs.JSON_WWW).toString());
		cst.newLine();
		cst.showText("FB: "+ju.get(cs.JSON_FB).toString());
		cst.endText();
		
	}
	
	private void addLogo(PDPageContentStream cst, PDDocument pdd) throws IOException {
		PDImageXObject pdImage = PDImageXObject.createFromFile(js.get(cs.LOGO_PATH).toString(), pdd);
		
		cst.drawImage(pdImage, 210,  675);
		cst.setNonStrokingColor(Color.darkGray);
		cst.addRect(15, 420, 560, 50);
		cst.fill();
	}

	
	//create sale report
	
	//create stock report
	
	//create repak report

}
