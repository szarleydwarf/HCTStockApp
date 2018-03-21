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
	public PDDocument createPDF(String docType, Object object, Customer customer) {
		// TODO Auto-generated method stub
		PDDocument pdd = null;
		if (docType.equals(cs.PDF_INVOICE)) {
			Invoice i = (Invoice) object;
			try {
				pdd = createInvoice(i, customer);
			} catch (IOException e) {
				log.logError(jl.get(cs.PDF_CREATION_ERROR).toString());
				e.printStackTrace();
			}
		} else if (docType.equals(cs.PDF_SALE_REPORT)){
			pdd = createSaleRep();
		} else if (docType.equals(cs.PDF_STOCK_REPORT)){
			pdd = createStockRep();
		} else if (docType.equals(ConstStrings.PDF_REPAK_REPORT)){
			pdd = createRepakRep();
		} else {
			JOptionPane.showMessageDialog(null, jl.get(cs.PDF_CREATION_ERROR).toString());
		}
		return pdd;
	}

	private PDDocument createStockRep() {
		// TODO Auto-generated method stub
		return null;
	}

	private PDDocument createSaleRep() {
		// TODO Auto-generated method stub
		return null;
	}

	private PDDocument createRepakRep() {
		// TODO Auto-generated method stub
		return null;
	}

	//create invoice
	private PDDocument createInvoice(Invoice i, Customer customer) throws IOException {
		// TODO Auto-generated method stub
		PDDocument pdd = new PDDocument();
		PDPage page = new PDPage();
		pdd.addPage(page);
		PDPageContentStream cst = new PDPageContentStream(pdd, page);
		// fill company details
		addLogo(cst, pdd);
		fillCompanyDetails(cst);
		// fill for who
		fillCustomerDetails(cst, i, customer);
		// fill what sold
		// fill customer msg
		
		cst.close();
		return pdd;
	}

	private void fillCustomerDetails(PDPageContentStream cst, Invoice i, Customer customer) throws IOException {
		cst.beginText();
		cst.setNonStrokingColor(Color.BLACK);
		cst.setFont(PDType1Font.COURIER_BOLD, this.fonts_title);
		cst.setLeading(20.5f);
		
		cst.newLineAtOffset(20, 530);
		if(!i.isBusiness()){
			CustomerInd c = null;
			if(customer != null)
				c = (CustomerInd) customer;
			if(c != null)
				cst.showText("Invoice # "+i.getId()+" for "+c.getCar().getBrandString());
			else
				cst.showText("Invoice # "+i.getId()+" for "+jl.get(cs.OTHER_STRING).toString());
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

	private void fillCompanyDetails(PDPageContentStream cst) throws IOException {
		cst.beginText();
		cst.setNonStrokingColor(Color.BLACK);
		cst.setFont(PDType1Font.COURIER, this.fonts_title);
		cst.newLineAtOffset(25f,  740);
		cst.setLeading(20.5f);
		
		cst.showText(ju.get(cs.JSON_COMPANY_NAME).toString() + "                                       " + date);
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
		PDImageXObject pdImage = PDImageXObject.createFromFile(ju.get(cs.LOGO_PATH).toString(), pdd);
		
		cst.drawImage(pdImage, 210,  675);
		cst.setNonStrokingColor(Color.darkGray);
		cst.addRect(15, 420, 560, 50);
		cst.fill();
	}

	
	//create sale report
	
	//create stock report
	
	//create repak report

}
