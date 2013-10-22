package org.geo2tag.ashamapsexample;

import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;


public class AshaMapsExampleMIDlet extends MIDlet {
	

	
	private Display m_display = Display.getDisplay(this);
	
	private CustomMapCanvas m_mapCanvas = new CustomMapCanvas(m_display, 
			Settings.START_ZOOM);
	private SettingsForm m_settingsForm = new SettingsForm();
	private WriteTagForm m_writeTagForm = new WriteTagForm();
	
	private CommandListener m_formCommandListener = new CommandListener() {
		
		public void commandAction(Command c, Displayable d) {
			System.out.println(c.getLabel());
			
			if (c.equals(CustomMapCanvas.CLEAR_MAP_COMMAND)){
				m_mapCanvas.clearMap();
			}else if (c.equals(CustomMapCanvas.SETTINGS_COMMAND)){
				m_display.setCurrent(m_settingsForm);
			}else if (c.equals(Settings.BACK_COMMAND)){
				refreshMapCanvas();
				m_display.setCurrent(m_mapCanvas);
			}else if (c.equals(CustomMapCanvas.SHOW_NEAREST_TAGS_COMMAND)){
				refreshMapCanvas();	
			}else if (c.equals(WriteTagForm.WRITE_TAG_COMMAND)){
				m_writeTagForm.writeTag();
			}else if (c.equals(CustomMapCanvas.WRITE_TAG_COMMAND)){
				m_display.setCurrent(m_writeTagForm);
			}
			
		}
	};
	
	private void refreshMapCanvas(){
		String channelFilter = m_settingsForm.getSelectedChannel();
		double radius  = m_settingsForm.getRadius();

		m_mapCanvas.showNearestTags(channelFilter, radius);
		m_settingsForm.setChannels(m_mapCanvas.getChannels());
	}
	
	public AshaMapsExampleMIDlet() {

		
		m_mapCanvas.setCommandListener(		m_formCommandListener);
		m_settingsForm.setCommandListener(	m_formCommandListener);
		m_writeTagForm.setCommandListener(  m_formCommandListener);
		
		m_settingsForm.setChannels(new Vector());
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {

	}

	protected void pauseApp() {
	}

	protected void startApp() throws MIDletStateChangeException {
		
		
        m_display.setCurrent(m_mapCanvas);
        refreshMapCanvas();
	}
	

}
