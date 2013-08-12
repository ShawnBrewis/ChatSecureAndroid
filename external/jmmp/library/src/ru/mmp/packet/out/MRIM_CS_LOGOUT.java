package ru.mmp.packet.out;

import ru.mmp.core.MMPConstants;
import ru.mmp.core.Status;
import ru.mmp.packet.Packet;
import ru.mmp.packet.PacketData;

/**
 * 
 * @author Raziel
 */
public class MRIM_CS_LOGOUT {

	public MRIM_CS_LOGOUT() {
		// setCommand(MRIM_CS_LOGIN2);
	}

	public Packet push() {
		PacketData data = new PacketData();
	
		return new Packet(MMPConstants.MRIM_CS_LOGOUT, data);
	}

}
