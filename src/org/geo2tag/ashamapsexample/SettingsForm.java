package org.geo2tag.ashamapsexample;

import java.util.Vector;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

import ru.spb.osll.objects.Channel;

public class SettingsForm extends Form implements CommandListener {
	private static final String RADIUS_EDIT_TEXT = "Radius";

	private static final String SETTINGS_FORM_HEADER = "Requests settings";
	private static final String CHANNELS_CHOICE_GROUP = "Select channel to display";

	private static final String BACK_COMMAND_TEXT = "Back";
	
	private TextField m_radiusEdit = new TextField(RADIUS_EDIT_TEXT, Settings.DEFAULT_RADIUS+"", 10, TextField.ANY);

	
	private ChoiceGroup m_channelsChoiceGroup = new ChoiceGroup(CHANNELS_CHOICE_GROUP, Choice.EXCLUSIVE);

	private static final Command BACK_COMMAND = new Command(BACK_COMMAND_TEXT, Command.HELP, 0);

	public SettingsForm() {
		super(SETTINGS_FORM_HEADER);
		
		addCommand(BACK_COMMAND);

		append(m_radiusEdit);
		append(m_channelsChoiceGroup);

	}

	public double getRadius(){
		System.out.println("getRadius()");
		double radius = Double.parseDouble( m_radiusEdit.getString() );
		return radius;
	}
	
	public String getSelectedChannel(){
		System.out.println("getSelectedChannel()");
		int  index = m_channelsChoiceGroup.getSelectedIndex();
		if (index < 0)
			return Settings.SHOW_ALL_CHANNELS;
		
		return m_channelsChoiceGroup.getString(index);
		
	}
	
	public void setChannels(Vector channels){
		m_channelsChoiceGroup.deleteAll();
		m_channelsChoiceGroup.append(Settings.SHOW_ALL_CHANNELS, null);
		for (int i=0; i<channels.size(); i++){
			Channel channel = (Channel) channels.elementAt(i);
			m_channelsChoiceGroup.append(channel.getName(), null);
		}
	}

	public void commandAction(Command c, Displayable d) {
		System.out.println(c.getLabel());

	}
	
	
}
