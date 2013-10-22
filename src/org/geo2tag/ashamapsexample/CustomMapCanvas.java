package org.geo2tag.ashamapsexample;

import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.location.QualifiedCoordinates;

import ru.spb.osll.json.JsonRequestException;
import ru.spb.osll.objects.Channel;
import ru.spb.osll.objects.Mark;
import ru.spb.osll.objects.User;

import com.nokia.maps.common.ApplicationContext;
import com.nokia.maps.common.GeoCoordinate;
import com.nokia.maps.component.feedback.FocalObserverComponent;
import com.nokia.maps.component.feedback.FocalEventListener;
import com.nokia.maps.component.touch.CenteringComponent;
import com.nokia.maps.component.touch.InfoBubbleComponent;
import com.nokia.maps.map.GestureMapCanvas;
import com.nokia.maps.map.MapDisplayState;
import com.nokia.maps.map.MapStandardMarker;


public class CustomMapCanvas extends GestureMapCanvas  {

	private static final String CLEAR_MAP = "Clear map";
	private static final String BUBBLE = "InfoBubble";
	private static final String NEAREST_TAGS = "Show nearest tags";
	private static final String SETTINGS = "Settings";
	private static final String WRITE_TAG = "Write tag";	


	
    private static final Command m_bubbleCommand = new Command(BUBBLE, Command.OK, 1);
	public static final Command CLEAR_MAP_COMMAND = new Command(CLEAR_MAP, Command.HELP, 0);
	public static final Command SHOW_NEAREST_TAGS_COMMAND = new Command(NEAREST_TAGS, Command.HELP, 1);
	public static final Command SETTINGS_COMMAND = new Command(SETTINGS, Command.HELP, 2);
	public static final Command WRITE_TAG_COMMAND = new Command(WRITE_TAG, Command.HELP, 3);	

    private String m_currentFocus;
	private FocalObserverComponent m_focalComponent;

	private InfoBubbleComponent m_infoBubble;
	private Alert m_alert = new Alert("");

	private  Vector m_channels = null;

	// When focus changed, we setup InfoBubbleComponent to display text of new focused element
	private FocalEventListener m_focalListener = new FocalEventListener() {
		
		public void onFocusChanged(Object arg0) {
			
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

	private void initialiseAuth() {
        ApplicationContext.getInstance().setAppID("L9v9rAbBD4RIKkteVoub");
        ApplicationContext.getInstance().setToken("NhjevMainHwsIKz8HObuvQ");
        ApplicationContext.getInstance().disableDirectUtils();
    }
	
	public CustomMapCanvas(Display arg0, int zoom) {
		super(arg0);
		initialiseAuth();
		
		// Setting Alert object for 
        m_alert.setTimeout(1000);
		
        // This class defines a MapComponent which associates an infobubble to a MapObject. 
        // When the MapObject is moved to the center of the screen the associated infobubble text is displayed
        m_infoBubble = new InfoBubbleComponent(this, m_bubbleListener );
        map.addMapComponent(m_infoBubble);
		
        m_focalComponent = new FocalObserverComponent(m_focalListener);
        
        // This component allows to process focusing events - when map center is placed on some map object
        map.addMapComponent(m_focalComponent);
        
        // This component allows to change map center position by pressing on map object
        map.addMapComponent(new CenteringComponent(this));
        
        // Setting map center and zoom
		changeMapCenterUsingLocation(zoom);
		
		addCommand(CLEAR_MAP_COMMAND);
		addCommand(SETTINGS_COMMAND);
		addCommand(SHOW_NEAREST_TAGS_COMMAND);
		addCommand(WRITE_TAG_COMMAND);
		
	}
	
	
	// text - data which will be displayed at popup  message
	protected void addMark(double lat, double lon, String text) {
			
		MapStandardMarker marker = mapFactory.createStandardMarker(new GeoCoordinate(lat, lon, 0),
				100, null, MapStandardMarker.BALLOON);
		
        m_focalComponent.addData(marker, text);		
        map.addMapObject(marker);
	}


	public void clearMap(){
		map.removeAllMapObjects();
	}
	public void changeMapCenterUsingLocation(final int zoom){
        // Setting map center and zoom
		new Thread(){
			public void run(){
				final QualifiedCoordinates qc = LocationObtainer.getQualifiedCoordinates();

				if (qc == null){
					System.out.println("qc == null");
					return;
				}
				changeMapCenter(qc.getLatitude(), qc.getLongitude(), zoom);
			}
		}.start();
		
	}
	
	
	public synchronized void changeMapCenter(double lat, double lon, int zoom){
        // Setting map center and zoom
		map.setState(
                new MapDisplayState(new GeoCoordinate(lat, lon, 0),
                zoom));
	}
	
	
	public void onMapContentComplete() {		
	}

	public void onMapUpdateError(String arg0, Throwable arg1, boolean arg2) {
		System.out.println(arg0);
		arg1.printStackTrace();
	}

	public void showNearestTags(final String channelFilter, final double radius){

		Thread thread = new Thread(){
			public void run(){
				try {
					clearMap();
					System.out.println("Starting retrival of the nearest tags");

					final QualifiedCoordinates qc = LocationObtainer.getQualifiedCoordinates();

					if (qc == null){
						System.out.println("qc == null");
						return;
					}

					System.out.println("qc = " +qc.toString());
					Geo2TagRequests.getInstance().doLoginRequest(Settings.LOGIN, Settings.PASSWORD);
					Geo2TagRequests.getInstance().doSetDbRequest(Settings.DB_NAME);

					Vector channels = Geo2TagRequests.getInstance().
							doFilterCircleRequest(qc.getLatitude(), qc.getLongitude(), radius);
					addChannels(channels, channelFilter);
					setChannels(channels);
					System.out.println("Successful retrival of the nearest tags");
				} catch (NullPointerException e) {
					e.printStackTrace();
				} catch (JsonRequestException e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	
	public synchronized void setChannels(Vector channels){
		m_channels = channels;
	}
	
	public synchronized Vector getChannels(){
		return m_channels;
	}
	
	private synchronized void addChannels(Vector channels, String channelFilter) {

		System.out.println("ChannelFilter == "+channelFilter + ", channels.size == " + channels.size() );
		
		for (int i=0; i<channels.size(); i++){
			Channel channel = (Channel)channels.elementAt(i);

			// Skip all channels which are not equal to ChannelFilter, if it is not set to SHOW_ALL_CHANNELS
			if (!channelFilter.equals(Settings.SHOW_ALL_CHANNELS) 
					&& !channel.getName().equals(channelFilter))
				continue;
			System.out.println("processing channel"+channel.getName());
			Vector marks = channel.getMarks();
			for (int j=0; j<marks.size(); j++){
				Mark mark = (Mark)marks.elementAt(j);
				
				addMark(mark.getLatitude(), mark.getLongitude(), mark.getUser()+": " +mark.getTitle());
			}
		}

	}
}
