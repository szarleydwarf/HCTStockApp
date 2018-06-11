package utility;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Calendar;

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

//	public String[] getDaysArray() {
//		int month = getMonthNum();
//		month++;
//
//		switch(month){
//			case 1:
//			case 3:
//			case 5:
//			case 7:
//			case 8:
//			case 10:
//			case 12:
//				return cs.DAYS_NUM_31;
//	
//			case 4:
//			case 6:
//			case 9:
//			case 11:
//				return cs.DAYS_NUM_30;
//	
//			case 2:
//				boolean isLeap = isLeapYear();
//				if(isLeap)
//					return cs.DAYS_NUM_29;
//				else
//					return cs.DAYS_NUM_28;
//	
//			default:
//				return cs.DAYS_NUM_31;
//		}
//	}
//
//	public String[] getDaysArray(int month) {
//		switch(month){
//			case 1:
//			case 3:
//			case 5:
//			case 7:
//			case 8:
//			case 10:
//			case 12:
//				return cs.DAYS_NUM_31;
//	
//			case 4:
//			case 6:
//			case 9:
//			case 11:
//				return cs.DAYS_NUM_30;
//	
//			case 2:
//				boolean isLeap = isLeapYear();
//				if(isLeap)
//					return cs.DAYS_NUM_29;
//				else
//					return cs.DAYS_NUM_28;
//	
//			default:
//				return cs.DAYS_NUM_31;
//		}
//	}


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
		int index = 0;
//		for(int i = 0; i < cs.YEARS_NO_STRING.length; i++){
//			if(cs.YEARS_NO_STRING[i].equals(year)){
//				return i;
//			}
//		}
		return index;
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
		System.out.println("Y "+y + " - " + ys.length);
		for(int i = 4; i >= 0; i--){
			System.out.println(i + " Y1 " + y);
			ys[i] = ""+y;
			y--;
		}
		y = this.getYearNum();
		for (int i = 5; i < ys.length; i++) {
			System.out.println(i + " Y2 " + y);
			y++;
			ys[i] = ""+y;
		}
		
		return ys;
	}

}
