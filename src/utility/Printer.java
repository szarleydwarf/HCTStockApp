package utility;

import java.awt.Color;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.swing.JOptionPane;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.printing.PDFPageable;
import org.json.simple.JSONObject;

import consts.ConstNums;
import consts.ConstPaths;
import consts.ConstStrings;

public class Printer {
	private JSONObject jl;
	private JSONObject js;
	private Logger log;
	private ConstStrings cs;
	private ConstNums cn;
	private ConstPaths cp;

	private MiscHelper msh;
	private DateHelper dh;
	private DecimalFormat df;
	private Font fonts;
	private Font fonts_title;
	private Map attributes;
	private Color color;
	private String date;
	private JSONObject ju;
	private String printerName;

	public Printer (ConstStrings cS, ConstNums cN, ConstPaths CP, Logger logger,
			JSONObject jSettings, JSONObject jLang, JSONObject jUser,
			MiscHelper mSH, DateHelper DH) {
		this.jl = jLang;
		this.js = jSettings;
		this.ju = jUser;

		this.log = logger;
		this.cs = cS;
		this.cn = cN;
		this.cp = CP;
		
		this.msh = mSH;
		this.dh = DH;
		this.printerName = "";
		try{
			this.printerName = js.get(cs.JS_DEF_PRINTER_NAME).toString();
		}catch(NullPointerException ne){
			this.printerName = cs.PRINTER_NAME;
			log.log(cs.ERR_LOG, jl.get(cs.JL_ERR_PRINTER_NAME).toString());
		}
}
	

	public void printDoc(String docPath) throws IOException, PrinterException{
//		System.out.println("printpdf "+docPath);
		PDDocument document = null;
		document = PDDocument.load(new File(docPath));

		if(this.printerName.isEmpty() || this.printerName == "")
        	this.printerName = this.cs.PRINTER_NAME;

		PrintService myPrintService = findPrintService(this.printerName);
        PrintServiceAttributeSet set = myPrintService.getAttributes();
                
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new PDFPageable(document));
		job.setPrintService(myPrintService);

        //TODO /Uncomment bellow before export to app
		job.print();
        
		document.close();
   }
	
	private static PrintService findPrintService(String printerName) {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService printService : printServices) {
            if (printService.getName().trim().equals(printerName)) {
                return printService;
            } else {
            	return PrintServiceLookup.lookupDefaultPrintService();
            }
        }
        return null;
    }
}
