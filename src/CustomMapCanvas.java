import java.util.Random;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

import com.nokia.maps.common.GeoCoordinate;
import com.nokia.maps.component.feedback.FocalObserverComponent;
import com.nokia.maps.component.feedback.FocalEventListener;
import com.nokia.maps.component.touch.CenteringComponent;
import com.nokia.maps.component.touch.InfoBubbleComponent;
import com.nokia.maps.map.GestureMapCanvas;
import com.nokia.maps.map.MapDisplayState;
import com.nokia.maps.map.MapStandardMarker;


public class CustomMapCanvas extends GestureMapCanvas  {

	private static final String ADD_RANDOM_MARK = "Add rand mark";
	private static final String CLEAR_MAP = "Delete all marks";
	private static final String BUBBLE = "InfoBubble";
	
    private Command m_bubbleCommand = new Command(BUBBLE, Command.OK, 1);
	private Command m_addRandomMarkCommand = new Command(ADD_RANDOM_MARK, Command.HELP, 0);
	private Command m_clearMarkCommand = new Command(CLEAR_MAP, Command.HELP, 0);

    private String m_currentFocus;
	private FocalObserverComponent m_focalComponent;

	private InfoBubbleComponent m_infoBubble;
	private Alert m_alert = new Alert("");

	
	private CommandListener m_commandListener = new CommandListener() {
		
		public void commandAction(Command c, Displayable d) {

			// Adding random mark
			if (c == m_addRandomMarkCommand){
				Random rnd = new Random();
								
				double lat = map.getCenter().getLatitude() + rnd.nextDouble();
				double lon = map.getCenter().getLongitude() + rnd.nextDouble();
				
				addMark(lat, lon, "New mark "+lat+":\n"+lon);
				
			// Clearing map
			}else if ( c == m_clearMarkCommand){
		        map.removeAllMapObjects();

			}
		}
	};

	
	

	// When focus changed, we setup InfoBubbleComponent to display text of new focused element
	private FocalEventListener m_focalListener = new FocalEventListener() {
		
		public void onFocusChanged(Object arg0) {
			System.out.println("focus changed "+arg0.getClass());
			
	        m_currentFocus = (String) arg0;
	        if (m_currentFocus != null) {
	            m_infoBubble.addData(m_currentFocus, m_bubbleCommand);
	        } else {
	            m_infoBubble.clear();
	        }
		}
	};
	
	// This listener is called when map center is set on MapMarker
	private CommandListener m_bubbleListener = new CommandListener() {
		
		public void commandAction(Command c, Displayable d) {
			if (c == m_bubbleCommand)
				display.setCurrent(m_alert);
			
		}
	};

	
	
	public CustomMapCanvas(Display arg0, double lat, double lon, int zoom) {
		super(arg0);

		
		// Setting Alert object for 
        m_alert.setTimeout(1000);
		
        // This class defines a MapComponent which associates an infobubble to a MapObject. 
        // When the MapObject is moved to the centre of the screen the associated infobubble text is displayed
        m_infoBubble = new InfoBubbleComponent(this, m_bubbleListener );
        map.addMapComponent(m_infoBubble);
		
        m_focalComponent = new FocalObserverComponent(m_focalListener);
        
        // This component allows to process focusing events - when map center is placed on some map object
        map.addMapComponent(m_focalComponent);
        
        // This component allows to change map center position by pressing on map object
        map.addMapComponent(new CenteringComponent(this));
        
        // Setting map center and zoom
		map.setState(
                new MapDisplayState(new GeoCoordinate(lat, lon, 0),
                zoom));
		
        // Setup map control commands
		setCommandListener(m_commandListener);
		
		addCommand(m_addRandomMarkCommand);
		addCommand(m_clearMarkCommand);
		
	}
	
	
	// text - data which will be displayed at popup  message
	protected void addMark(double lat, double lon, String text) {
			
		MapStandardMarker marker = mapFactory.createStandardMarker(new GeoCoordinate(lat, lon, 0),
				10, null, MapStandardMarker.BALLOON);	
		
        m_focalComponent.addData(marker, text);		
        map.addMapObject(marker);
	}


	
	public void onMapContentComplete() {		
	}

	public void onMapUpdateError(String arg0, Throwable arg1, boolean arg2) {
		System.out.println(arg0);
		arg1.printStackTrace();
	}
}
