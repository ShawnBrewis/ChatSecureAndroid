package ru.mmp.core;

import android.util.Log;
import ru.mmp.listener.MessageListener;
import ru.mmp.listener.StatusListener;
import ru.mmp.packet.Packet;
import ru.mmp.packet.inc.*;

/**
 * 
 * @author Raziel
 */
public class PacketAnalyser {

	public static final int MRIM_CS_HELLO_ACK = 0x1002;
	public static final int MRIM_CS_LOGIN_ACK = 0x1004;
	public static final int MRIM_CS_LOGIN_REJ = 0x1005;
	public static final int MRIM_CS_MESSAGE_ACK = 0x00001009;
	public static final int MRIM_CS_MESSAGE_STATUS = 0x00001012;
	public static final int MRIM_CS_USER_STATUS = 0x100F;
	public static final int MRIM_CS_AUTHORIZE_ACK = 0x1021;
	public static final int MRIM_CS_CONTACT_LIST2 = 0x1037;

	private MMPClient client;
	private MRIM_CS_HELLO_ASC hello_asc;
	private MRIM_CS_LOGIN_REJ login_rej;
	private MRIM_CS_MESSAGE_ACK message_ack;

	public PacketAnalyser(MMPClient client) {
		this.client = client;
		hello_asc = new MRIM_CS_HELLO_ASC();
		login_rej = new MRIM_CS_LOGIN_REJ();
		message_ack = new MRIM_CS_MESSAGE_ACK();
	}

	public void parser(Packet packet) {
		int cmd = packet.getCommand();
		switch (cmd) {
		case MRIM_CS_HELLO_ACK:
			hello_asc.parser(packet);
			hello_asc.execute(client);
			break;
		case MRIM_CS_LOGIN_ACK:
			onLogin();
			break;
		case MRIM_CS_LOGIN_REJ:
			login_rej.parser(packet);
			login_rej.notifyEvent(client);
			break;
		case MRIM_CS_MESSAGE_ACK:
			message_ack.parser(packet);
			message_ack.notifyEvent(client);
			message_ack.execute(client);
			break;
		case MRIM_CS_MESSAGE_STATUS:
			// TODO Разобрать пакет. Узнать статус доставки.
			for (int i = 0; i < client.getMessageListener().size(); i++) {
				MessageListener l = (MessageListener) client
						.getMessageListener().get(i);
				l.onMessageAck();
			}
			//System.out.println("Сообщение доставлено");
			break;
		case MRIM_CS_USER_STATUS:
			// TODO Пользователь сменил статус. Узнать статус, оповестить
			// слушателей
			Log.d("MRA","got user status message");
			
			break;
		case MRIM_CS_AUTHORIZE_ACK:
			// TODO Пользователь авторизовался.
			Log.d("MRA","got authorize ack");
			
			break;
		case MRIM_CS_CONTACT_LIST2:
			// TODO Контакт лист
			Log.d("MRA","got contact list2");
			
			break;
		case MMPConstants.MRIM_CS_USER_INFO:
			Log.d("MRA","got user info");
			String key = packet.getData().getString();
			String value = packet.getData().getString();
			Log.d("MRA",key + "=" + value);
			
			
			break;
			
		case MMPConstants.MRIM_CS_MAILBOX_STATUS:
			Log.d("MRA","got mailbox status");
			int newMsgCount = (int)packet.getData().getDWord();
			Log.d("MRA","new messages: " + newMsgCount);
			break;
		default:
			Log.d("MRA","got other command: " + Integer.toHexString(cmd));
			
			break;
		}

	}

	/**
	 * Уведомляем слушателей об успешной авторизации
	 */
	private void onLogin() {
		for (int i = 0; i < client.getStatusListener().size(); i++) {
			StatusListener l = (StatusListener) client.getStatusListener().get(
					i);
			l.onLogin();
		}
	}
}
