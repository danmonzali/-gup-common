package it.tim.gup.common.util;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Random;

/**
 * This class encapsulate a TID.
 * <p>
 *
 * The TID is an opaque data of 24 headecimal digits, used to identify a
 * particular data flow into ExistBUS. Requests and responses will match using
 * TID.<br>
 * Each pair request/response has a TID different from other request/responses.
 * <p>
 *
 * This class calculates TID as:
 * <ul>
 * <li>8 hex digits: IP address of the machine that generates the TID
 * <li>8 hex digits: time stamp in seconds
 * <li>4 hex digits: counter
 * <li>4 hex digits: random
 * </ul>
 *
 * <b>NOTE: it is possible that TID implementation can vary among languages and
 * OS.<br>
 * We repeat: the TID must be considered an opaque data, it is not legal to
 * extract informations from the TID.</b>
 *
 */
public class TIDUtil implements Serializable {
	// --------------------------------------------------------------------------
	// STATIC INITIALIZERS
	// --------------------------------------------------------------------------

	private static final long serialVersionUID = 5843655749753904433L;

	// The IP component of the TID is calculated only at initialization time
	// calling the appropriate method.
	//
	static {
		calculateIP();
	}

	// --------------------------------------------------------------------------
	// FIELDS
	// --------------------------------------------------------------------------

	/**
	 * Valid chars on the TID.
	 */
	transient private static final String sValidChar = "0123456789ABCDEF";

	/**
	 * TID. This field must no be transient.
	 */
	private String sTID = null;

	// --------------------------------------------------------------------------
	// CONSTRUCTORS
	// --------------------------------------------------------------------------

	/**
	 * Initialize this object with 24 random headecimal characters.
	 */
	public TIDUtil() {
		sTID = getTID();
	}

	/**
	 * Initialize this object with the given String. The string is checked for
	 * correctness.
	 *
	 * @param tid
	 *            24 hexadecimal uppercase characters. tid cannot terminate with
	 *            '0000' (reserved for future uses).
	 *
	 * @exception EBException
	 *                if tid is not correct
	 */
	public TIDUtil(String tid) throws Exception {
		// Check the length
		int len = tid.length();
		if (len != 24) {
			throw new Exception("INVALID_TID_FORMAT -> " + tid);
		}

		// Check for headecimal digits
		for (int i = 0; i < len; i++) {
			if (sValidChar.indexOf(tid.charAt(i)) == -1) {
				throw new Exception("INVALID_TID_FORMAT -> " + tid);
			}
		}
		sTID = tid;
	}

	/**
	 * Copy constructor.
	 *
	 * @param tid
	 *            TID to copy. If it is null, then the new object is initialized
	 *            with a new TID, as for default constructor.
	 */
	public TIDUtil(TIDUtil tid) {
		if (tid == null) {
			sTID = getTID();
		} else {
			sTID = tid.sTID;
		}
	}

	// --------------------------------------------------------------------------
	// METHODS
	// --------------------------------------------------------------------------

	/**
	 * Check for equality with an Object.
	 */
	@Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TIDUtil tidUtil = (TIDUtil) obj;
        return Objects.equals(sTID, tidUtil.sTID);
    }
	/**
	 * Convert this TID to a String.
	 */
	public String toString() {
		return sTID;
	}

	// --------------------------------------------------------------------------
	// PRIVATE METHODS and implementation fields.
	// --------------------------------------------------------------------------

	/**
	 * Increased each time a TID is calculated.
	 */
	transient private static int counter = 0;

	/**
	 * Used to calculate random component.
	 */
	transient private static SecureRandom secureRandom = new SecureRandom();

	/**
	 * Contains the IP component. Calculate only once.
	 */
	transient private static String sIP;

	/**
	 * Used in toHexString().
	 *
	 * @see #toHexString(int)
	 */
	transient private static final String fill = "00000000";

	/**
	 * Formats an integer to a String 0 filled. The String is length 8 hex digits.
	 */
	private static String toHexString(int number) {
		String hex = Integer.toHexString(number).toUpperCase();
		return fill.substring(hex.length()) + hex;
	}

	/**
	 * Calculate the TID as documented in the class documentation.
	 *
	 * @see it.reply.sytel.TIDUtil.data.TID
	 */
	private String getTID() {
		// Random component.
		// La formula seguente calcola un numero casuale su 16 bit, escludendo
		// lo zero.
		//
		int rand = secureRandom.nextInt(0xFFFF) + 1;
		
		// Counter component
		// 0 is not admitted for the counter (0 is reserved for future uses)
		//
		counter = (counter + 1) & 0xFFFF;
		if (counter == 0) {
			counter = 1;
		}
		// String for random component and counter.
		// The counter component must shift 16 bit (4 hex digits) before
		// calculate the string.
		//
		String sCounterAndRandom = toHexString((counter << 16) | rand);

		// Time stamp in seconds
		//
		int time = (int) (System.currentTimeMillis() / 1000);
		String sTimeStamp = toHexString(time);

		// Generate the string
		//
		return sIP + sTimeStamp + sCounterAndRandom;
	}

	/**
	 * Calculate the IP address. Select the first IP address available that is not
	 * the local IP address (i.e. 127.0.0.1 is not selected).
	 */
	private static void calculateIP() {
		// Default value
		// Will be overwrite if another IP address exists.
		//
		sIP = "7F000001";

		try {
			InetAddress localHost = InetAddress.getLocalHost();
			InetAddress[] allInetAddress = InetAddress.getAllByName(localHost.getHostName());
			if (allInetAddress != null) {
				boolean found = false;

				for (int i = 0; (i < allInetAddress.length) && !found; ++i) {
					String sAddress = allInetAddress[i].getHostAddress();

					// The address is ok if it is not 127.0.0.1 not and multicast
					//
					if (!sAddress.equals("127.0.0.1") && !allInetAddress[i].isMulticastAddress()) {
						sIP = addressToString(allInetAddress[i]);
						found = true;
					}
				}
			}
		} catch (IOException exc) {}
	}

	/**
	 * Set the sIP field from an InetAddress.
	 */
	private static String addressToString(InetAddress inetAddress) {
		byte[] buff = inetAddress.getAddress();
		int address = 0;
		for (int i = 0; i < buff.length; ++i) {
			address <<= 8;
			address |= (buff[i] & 0xFF);
		}
		return toHexString(address);
	}

}
