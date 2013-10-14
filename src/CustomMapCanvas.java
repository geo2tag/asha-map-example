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

	
	private CommandListener m_commandListener = new CommandListener() {
		
		public void commandAction(Command c, Displayable d) {
			// TODO Auto-generated method stub			
			if (c == m_addRandomMarkCommand){
				Random rnd = new Random();
								
				double lat = map.getCenter().getLatitude() + rnd.nextDouble();
				double lon = map.getCenter().getLongitude() + rnd.nextDouble();
				
				addMark(lat, lon, "New mark "+lat+":\n"+lon);
			}else if ( c == m_clearMarkCommand){
		        map.removeAllMapObjects();

			}if (c == m_bubbleCommand){
				m_alert.setString("123456");
				display.setCurrent(m_alert);
			}
		}
	};

	private FocalObserverComponent m_focalComponent;

	private InfoBubbleComponent m_infoBubble;
	private Alert m_alert;
	private FocalEventListener m_focalListener = new FocalEventListener() {
		
		public void onFocusChanged(Object arg0) {
			// TODO Auto-generated method stub
			System.out.println("focus changed");
			
	        m_currentFocus = (String) arg0;
	        if (m_currentFocus != null) {
	            m_infoBubble.addData(m_currentFocus, m_bubbleCommand);
	        } else {
	            m_infoBubble.clear();
	        }
		}
	};

	
	
	public CustomMapCanvas(Display arg0, double lat, double lon, int zoom) {
		super(arg0);

		map.setState(
                new MapDisplayState(new GeoCoordinate(lat, lon, 0),
                zoom));
		
        m_alert = new Alert("");
        m_alert.setTimeout(1000);
		
        m_infoBubble = new InfoBubbleComponent(this, m_commandListener);
        map.addMapComponent(m_infoBubble);
		
        map.addMapComponent(new CenteringComponent(this));

        m_focalComponent = new FocalObserverComponent(m_focalListener);
        map.addMapComponent(m_focalComponent);
        
        map.addMapComponent(new CenteringComponent(this));
		
		setCommandListener(m_commandListener);
		
		addCommand(m_addRandomMarkCommand);
		addCommand(m_clearMarkCommand);
		
	}
	
	

	protected void addMark(double lat, double lon, String text) {
		// TODO Auto-generated method stub		
		MapStandardMarker marker = mapFactory.createStandardMarker(new GeoCoordinate(lat, lon, 0),
				10, null, MapStandardMarker.BALLOON);	
		
        m_focalComponent.addData(marker, text);

		
        map.addMapObject(marker);
	}


	
	public void onMapContentComplete() {
		// TODO Auto-generated method stub
		
	}

	public void onMapUpdateError(String arg0, Throwable arg1, boolean arg2) {
		// TODO Auto-generated method stub
		System.out.println(arg0);
		arg1.printStackTrace();
	}
}
