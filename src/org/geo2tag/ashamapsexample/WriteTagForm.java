package org.geo2tag.ashamapsexample;

import java.util.Vector;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import javax.microedition.location.QualifiedCoordinates;

import ru.spb.osll.json.JsonRequestException;
import ru.spb.osll.objects.Channel;


public class WriteTagForm extends Form{

	private static final String WRITE_TAG_FORM = "WriteTag";

	private static final String TAG_LABEL = "EnterTagLabel";
	private static final String WRITE_TAG_COMMAND_TEXT = "WriteTag"; 
	private static final String CHANNELS_CHOICE_GROUP = "Select channel to write tag";
	private static final String CHANNEL_NAME = null;
		
	private TextField m_tagLabelEdit = new TextField(TAG_LABEL, "", 30, TextField.ANY);
	private TextField m_channelNameEdit = new TextField(CHANNEL_NAME, "Mark", 30, TextField.ANY);
	
	public static final Command WRITE_TAG_COMMAND = new Command(WRITE_TAG_COMMAND_TEXT, Command.HELP, 0);
	
	public WriteTagForm() {
		super(WRITE_TAG_FORM);
		append(m_tagLabelEdit);
		append(m_channelNameEdit);
		
		addCommand(WRITE_TAG_COMMAND);
		addCommand(Settings.BACK_COMMAND);
	}
	
	
	
	public void writeTag(){
		Thread thread = new Thread(){
			public void run(){
				
				final QualifiedCoordinates qc = LocationObtainer.getQualifiedCoordinates();

				if (qc == null){
					System.out.println("qc == null");
					return;
				}
				String channel = m_channelNameEdit.getString();
				String label = m_tagLabelEdit.getString();

				try {
					Geo2TagRequests.getInstance().doLoginRequest(Settings.LOGIN, Settings.PASSWORD);
					Geo2TagRequests.getInstance().doSetDbRequest(Settings.DB_NAME);

					Geo2TagRequests.getInstance().doWriteTag(qc.getLatitude(), qc.getLongitude(), qc.getAltitude(), channel, label);
				} catch (NullPointerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonRequestException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		thread.start();
		
	}


}
