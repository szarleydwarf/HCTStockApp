package utility;

import java.util.Random;
import java.util.Scanner;

public class tScanner {
	private static Scanner kb = new Scanner(System.in);
	
	public static float getFloatHandleException(String prompt) {
		boolean bContinue = true;
		float value = 0; 
		System.out.print(prompt);
		do {
			try {
				value = kb.nextFloat();
				bContinue = false;
			}
			catch(Exception e) {
				kb.nextLine(); //cleaning the buffer of junk
				System.out.println("Invalid input!!!");
			}
		} while(bContinue == true);	
		return value;
	}
	
	public static float getFloat(String prompt, float lo, float hi) {
		float value = 0;
		boolean isInRange = false;
		do {
			System.out.print(prompt);
			value = kb.nextFloat();
			isInRange = (value < lo) || (value > hi);
			
			if(isInRange) {
				System.out.println("Invalid input! Outside range " 
											+ lo + " - " + hi);
			}
		} while(isInRange);
		
		return value;
	}
	
	public static float getFloat(String prompt) {
		System.out.print(prompt);
		return kb.nextFloat();
	}
	
	public static byte getByte(String prompt) {
		System.out.print(prompt);
		return kb.nextByte();
	}
	
	public static String getString(String prompt) {
		System.out.print(prompt);
		return kb.nextLine();
	}

	public String getRandomString(){
		String str = "";
		for(int i = 0; i < 10; i++){
			str+= rndChar();
		}
		return str;
	}
	private static char rndChar () {
	    int rnd = (int) (Math.random() * 52); // or use Random or whatever
	    char base = (rnd < 26) ? 'A' : 'a';
	    return (char) (base + rnd % 26);
	}
	public int getRandomInt(int min, int max){
		Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
}
