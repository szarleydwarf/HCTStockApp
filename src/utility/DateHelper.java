package utility;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Calendar;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import org.json.simple.JSONArray;

import consts.ConstNums;
import consts.ConstStrings;

public class DateHelper {
	private ConstStrings cs;
	private ConstNums cn;
	
	public DateHelper(ConstStrings constStrings, ConstNums CN){
		this.cs = constStrings;
		this.cn = CN;
	}
	
	public String getFormatedDate(){
		Calendar today = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat(this.cs.DATE_FORMAT);
		
		return df.format(today.getTime());
	}

	public String getFormatedDateRev(){
		Calendar today = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat(this.cs.DATE_FORMAT_REVERSE);
		
		return df.format(today.getTime());
	}
	
	public String getFormatedDateAndTime() {
		Calendar today = Calendar.getInstance();
		SimpleDateFormat df= new SimpleDateFormat(this.cs.DATE_TIME_FORMAT);
		
		return df.format(today.getTime());
	}

	public String[] getDaysArray() {
		int month = getMonthNum();
		month++;

		switch(month){
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				return cs.DAYS_NUM_31;
	
			case 4:
			case 6:
			case 9:
			case 11:
				return cs.DAYS_NUM_30;
	
			case 2:
				boolean isLeap = isLeapYear();
				if(isLeap)
					return cs.DAYS_NUM_29;
				else
					return cs.DAYS_NUM_28;
	
			default:
				return cs.DAYS_NUM_31;
		}
	}

	public String[] getDaysArray(int month) {
		switch(month){
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				return cs.DAYS_NUM_31;
	
			case 4:
			case 6:
			case 9:
			case 11:
				return cs.DAYS_NUM_30;
	
			case 2:
				boolean isLeap = isLeapYear();
				if(isLeap)
					return cs.DAYS_NUM_29;
				else
					return cs.DAYS_NUM_28;
	
			default:
				return cs.DAYS_NUM_31;
		}
	}


	public int getDayOfMonthNum(){
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public int getMonthNum(){
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH);
	}


//	public String getIndexOfMonth(String month) {
//		for(int i = 0; i < cn.NUM_OF_MONTHS; i++){
//			if(cs.MONTHS_NAMES[i].equals(month)){
//				int monthNum = i+1;
//				return "" + monthNum;
//			}
//		}
//		return "";
//	}

	public int getYearNum(){
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}

	public int getYearIndex() {
		String year = ""+getYearNum();
		String[]ys = getYearsArr();
		for(int i = 0; i < ys.length; i++){
			if(ys[i].equals(year)){
				return i;
			}
		}
		return 0;
	}
	
	public boolean isLeapYear(int year){
        if(year % 4 == 0)
        {
            if( year % 100 == 0) {
                if ( year % 400 == 0)
                    return true;
                else
                	return false;
            }
            else
            	return true;
        }
        else {
        	return false;
        }
	}
	
	public boolean isLeapYear(){
		int year = this.getYearNum();
        if(year % 4 == 0)
        {
            if( year % 100 == 0) {
                if ( year % 400 == 0)
                    return true;
                else
                	return false;
            }
            else
            	return true;
        }
        else {
        	return false;
        }
	}

	public int getMonthNumber(String monthName) {
	    return Month.valueOf(monthName.toUpperCase()).getValue();
	}

	public String getRevDateYM() {
		Calendar today = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
		
		return df.format(today.getTime());
	}
	public String[] getYearsArr() {
		int y = this.getYearNum();
		String[]ys = new  String[10];
		for(int i = 4; i >= 0; i--){
			ys[i] = ""+y;
			y--;
		}
		y = this.getYearNum();
		for (int i = 5; i < ys.length; i++) {
			y++;
			ys[i] = ""+y;
		}
		
		return ys;
	}

	public String[] getDaysArray(int month, int year) {
		int daysInMont = getDaysInMonth(month, year);
		String[]ds = new String[daysInMont];
		for (int i = 1; i <= daysInMont; i++) {
			ds[i-1] = ""+i;
		}
		return ds;
	}

	private int getDaysInMonth(int month, int year) {
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
 			return 31;
		case 4:
		case 6:
		case 9:
		case 11:
			return 30;
		case 2:
			if(isLeapYear(year))
				return 29;
			else
				return 28;

		default:
			return 0;
		}
	}
	

	public DefaultComboBoxModel updateCBDays(String monthName, String yearString) {
		int month = this.getMonthNumber(monthName);
		int year = Integer.parseInt(yearString);
		String[]days = this.getDaysArray(month, year);
		DefaultComboBoxModel cbm = new DefaultComboBoxModel(days);
//		JComboBox cbDays = new JComboBox();
//        cbDays.setModel(cbm);
//        cbDays.setSelectedIndex(today);
        return cbm;
	}
}
