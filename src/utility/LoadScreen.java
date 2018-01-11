package utility;

import utility.SplashScreen;
import javax.swing.ImageIcon;

import consts.ConstInts;
import consts.ConstPaths;


public class LoadScreen {
	private static ConstPaths cp;	
	  SplashScreen screen;

	  public LoadScreen(ConstPaths cp, ConstInts ci) {
		this.cp = cp;
		String poop;
	    splashScreenInit();
	    for (int i = 0; i <= 100; i++) {
	      for (long j=0; j<ci.SPLASHSCREEN_TIME; ++j) {
	        poop = " " + (j + i);
	      }
	      // run either of these two -- not both
	      screen.setProgress("Starting in - " + i, i);  // progress bar with a message
//	      screen.setProgress(i);           // progress bar with no message
	    }

	  }

	  public void splashScreenDestruct() {
	    screen.setScreenVisible(false);
	  }

	  private void splashScreenInit() {
		ImageIcon myImage = new ImageIcon(cp.LOGO_PATH);
		screen = new SplashScreen(myImage);
		screen.setLocationRelativeTo(null);
		screen.setProgressMax(100);
		screen.setScreenVisible(true);
	  }
}  
