import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import com.nokia.maps.common.ApplicationContext;


public class AshaMapsExampleMIDlet extends MIDlet {

	//private MapForm m_mapForm = new MapForm("Map", Display.getDisplay(this)) ;
	
	private static final double START_CENTER_LAT = 60;
	private static final double START_CENTER_LON = 30;
	private static final int START_ZOOM = 6;
	
	
	public AshaMapsExampleMIDlet() {
		// TODO Auto-generated constructor stub
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		// TODO Auto-generated method stub

	}

	protected void pauseApp() {
		// TODO Auto-generated method stub

	}

	protected void startApp() throws MIDletStateChangeException {
		// TODO Auto-generated method stub   
		initialiseAuth();
		Display display = Display.getDisplay(this);
		CustomMapCanvas canvas = new CustomMapCanvas(display, START_CENTER_LAT, START_CENTER_LON, START_ZOOM);
        display.setCurrent(canvas);

	}
	
	private void initialiseAuth() {
        ApplicationContext.getInstance().setAppID("L9v9rAbBD4RIKkteVoub");
        ApplicationContext.getInstance().setToken("NhjevMainHwsIKz8HObuvQ");
        ApplicationContext.getInstance().disableDirectUtils();
    }

}
