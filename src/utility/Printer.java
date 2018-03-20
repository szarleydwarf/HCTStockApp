package utility;

import java.awt.Color;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.text.DecimalFormat;
import java.util.Map;

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

	public Printer (ConstStrings cS, ConstNums cN, ConstPaths CP, Logger logger,
			JSONObject jSettings, JSONObject jLang, JSONObject jUser,
			MiscHelper mSH, DateHelper DH, DecimalFormat df_3_2) {
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
		
		this.fonts = new Font(js.get(cs.FONT).toString(), Font.PLAIN, Integer.parseInt(js.get(cs.FONT_SIZE_DEF).toString()));
		this.fonts_title = new Font(js.get(cs.FONT).toString(), Font.PLAIN, Integer.parseInt(js.get(cs.FONT_SIZE_TITLE).toString()));
		this.attributes = fonts_title.getAttributes();
		this.attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		this.color = msh.getColor(cs.APP, cs, js);
		this.date = dh.getFormatedDate();
	}
	
	public boolean saveDoc(){
		return false;
	}

	public boolean printDoc(){
		return false;
	}
}
