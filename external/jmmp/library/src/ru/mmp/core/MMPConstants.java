package ru.mmp.core;

public class MMPConstants {


/*
typedef struct mrim_packet_header_t
{
    u_long      magic;		// Magic
    u_long      proto;		// ������ ���������
    u_long      seq;		// Sequence
    u_long      msg;		// ��� ������
    u_long      dlen; 		// ����� ������
    u_long	from;		// ����� �����������
    u_long	fromport;	// ���� �����������
    u_char	reserved[16];	// ���������������
}
mrim_packet_header_t;
*/
	
public final static int CS_MAGIC = 0xDEADBEEF;		// ���������� Magic ( C &lt;-&gt; S )
        

public final static int MRIM_CS_HELLO   = 0x1001;  // C -&gt; S
    // empty
    
public final static int MRIM_CS_HELLO_ACK = 0x1002;  // S -&gt; C
    // mrim_connection_params_t


public final static int MRIM_CS_LOGIN_ACK   = 0x1004;  // S -&gt; C
    // empty
    
public final static int MRIM_CS_LOGIN_REJ   = 0x1005;  // S -&gt; C
    // LPS reason
    
public final static int MRIM_CS_PING       = 0x1006;  // C -&gt; S
    // empty
    
public final static int MRIM_CS_MESSAGE		=	0x1008;  // C -&gt; S
	// UL flags
	// LPS to
	// LPS message
	// LPS rtf-formatted message (&gt;=1.1)
	public final static int MESSAGE_FLAG_OFFLINE = 	0x00000001;
	public final static int MESSAGE_FLAG_NORECV	=	0x00000004;
	public final static int MESSAGE_FLAG_AUTHORIZE	=	0x00000008; 	// X-MRIM-Flags: 00000008
	public final static int MESSAGE_FLAG_SYSTEM	=	0x00000040;
	public final static int MESSAGE_FLAG_RTF	=	0x00000080;
	public final static int MESSAGE_FLAG_CONTACT	=	0x00000200;
	public final static int MESSAGE_FLAG_NOTIFY	=	0x00000400;
	public final static int MESSAGE_FLAG_MULTICAST	=	0x00001000;
public final static int MAX_MULTICAST_RECIPIENTS = 50;
	public final static int MESSAGE_USERFLAGS_MASK	= 0x000036A8;	// Flags that user is allowed to set himself

	
public final static int MRIM_CS_MESSAGE_ACK	=	0x1009;  // S -&gt; C
	// UL msg_id
	// UL flags
	// LPS from
	// LPS message
	// LPS rtf-formatted message (&gt;=1.1)

	

	
public final static int MRIM_CS_MESSAGE_RECV = 0x1011;	// C -&gt; S
	// LPS from
	// UL msg_id

public final static int MRIM_CS_MESSAGE_STATUS	= 0x1012;	// S -&gt; C
	// UL status
	public final static int MESSAGE_DELIVERED	=	0x0000;	// Message delivered directly to user
	public final static int MESSAGE_REJECTED_NOUSER	=	0x8001;  // Message rejected - no such user
	public final static int MESSAGE_REJECTED_INTERR	=	0x8003;// Internal server error
	public final static int MESSAGE_REJECTED_LIMIT_EXCEEDED=	0x8004;	// Offline messages limit exceeded
	public final static int MESSAGE_REJECTED_TOO_LARGE	= 0x8005;	// Message is too large
	public final static int	MESSAGE_REJECTED_DENY_OFFMSG	= 0x8006;	// User does not accept offline messages

public final static int MRIM_CS_USER_STATUS	= 0x100F;// S -&gt; C
	// UL status
	public final static int STATUS_OFFLINE	= 	0x00000000;
	public final static int STATUS_ONLINE	= 	0x00000001;
	public final static int STATUS_AWAY	= 	0x00000002;
	public final static int STATUS_UNDETERMINATED	= 0x00000003;
	public final static int STATUS_FLAG_INVISIBLE= 	0x80000000;
	// LPS user

	
public final static int MRIM_CS_LOGOUT		=	0x1013;	// S -&gt; C
	// UL reason=
	public final static int LOGOUT_NO_RELOGIN_FLAG =	0x0010;		// Logout due to double login
	
public final static int MRIM_CS_CONNECTION_PARAMS=	0x1014;	// S -&gt; C
	// mrim_connection_params_t

public final static int MRIM_CS_USER_INFO		=	0x1015;	// S -&gt; C
	// (LPS key, LPS value)* X
	
			
public final static int MRIM_CS_ADD_CONTACT		=	0x1019;	// C -&gt; S
	// UL flags (group(2) or usual(0) 
	// UL group id (unused if contact is group)
	// LPS contact
	// LPS name
	// LPS unused
	public final static int CONTACT_FLAG_REMOVED=	0x00000001;
	public final static int CONTACT_FLAG_GROUP	=0x00000002;
	public final static int CONTACT_FLAG_INVISIBLE=	0x00000004;
	public final static int CONTACT_FLAG_VISIBLE=	0x00000008;
	public final static int CONTACT_FLAG_IGNORE	=0x00000010;
	public final static int CONTACT_FLAG_SHADOW=	0x00000020;
	
public final static int MRIM_CS_ADD_CONTACT_ACK	=		0x101A;	// S -&gt; C
	// UL status
	// UL contact_id or (u_long)-1 if status is not OK
	
	public final static int CONTACT_OPER_SUCCESS	=	0x0000;
	public final static int CONTACT_OPER_ERROR	=	0x0001;
	public final static int CONTACT_OPER_INTERR	=	0x0002;
	public final static int CONTACT_OPER_NO_SUCH_USER	=0x0003;
	public final static int CONTACT_OPER_INVALID_INFO=	0x0004;
	public final static int CONTACT_OPER_USER_EXISTS	=0x0005;
	public final static int CONTACT_OPER_GROUP_LIMIT=	0x6;
	
public final static int MRIM_CS_MODIFY_CONTACT		=	0x101B;	// C -&gt; S
	// UL id
	// UL flags - same as for MRIM_CS_ADD_CONTACT
	// UL group id (unused if contact is group)
	// LPS contact
	// LPS name
	// LPS unused
	
public final static int MRIM_CS_MODIFY_CONTACT_ACK	=	0x101C;	// S -&gt; C
	// UL status, same as for MRIM_CS_ADD_CONTACT_ACK

public final static int MRIM_CS_OFFLINE_MESSAGE_ACK	=	0x101D;	// S -&gt; C
	// UIDL
	// LPS offline message

public final static int MRIM_CS_DELETE_OFFLINE_MESSAGE =	0x101E;	// C -&gt; S
	// UIDL

	
public final static int MRIM_CS_AUTHORIZE		=	0x1020;	// C -&gt; S
	// LPS user
	
public final static int MRIM_CS_AUTHORIZE_ACK	=		0x1021;	// S -&gt; C
	// LPS user

public final static int MRIM_CS_CHANGE_STATUS		=	0x1022;	// C -&gt; S
	// UL new status


public final static int MRIM_CS_GET_MPOP_SESSION	=	0x1024;	// C -&gt; S
	
	
public final static int MRIM_CS_MPOP_SESSION		=	0x1025;	// S -&gt; C
	public final static int MRIM_GET_SESSION_FAIL	=	0;
	public final static int MRIM_GET_SESSION_SUCCESS	= 1;
	//UL status 
	// LPS mpop session


//white pages!
public final static int MRIM_CS_WP_REQUEST		=	0x1029; //C-&gt;S
//DWORD field, LPS value
public final static int PARAMS_NUMBER_LIMIT	=		50;
public final static int PARAM_VALUE_LENGTH_LIMIT	=	64;

//if last symbol in value eq '*' it will be replaced by LIKE '%' 
// params define
// must be  in consecutive order (0..N) to quick check in check_anketa_info_request
/*
enum {
  MRIM_CS_WP_REQUEST_PARAM_USER		= 0,
  MRIM_CS_WP_REQUEST_PARAM_DOMAIN,		
  MRIM_CS_WP_REQUEST_PARAM_NICKNAME,	
  MRIM_CS_WP_REQUEST_PARAM_FIRSTNAME,	
  MRIM_CS_WP_REQUEST_PARAM_LASTNAME,	
  MRIM_CS_WP_REQUEST_PARAM_SEX	,	
  MRIM_CS_WP_REQUEST_PARAM_BIRTHDAY,	
  MRIM_CS_WP_REQUEST_PARAM_DATE1	,	
  MRIM_CS_WP_REQUEST_PARAM_DATE2	,	
  //!!!!!!!!!!!!!!!!!!!online request param must be at end of request!!!!!!!!!!!!!!!
  MRIM_CS_WP_REQUEST_PARAM_ONLINE	,	
  MRIM_CS_WP_REQUEST_PARAM_STATUS	,	 // we do not used it, yet
  MRIM_CS_WP_REQUEST_PARAM_CITY_ID,	
  MRIM_CS_WP_REQUEST_PARAM_ZODIAC,		
  MRIM_CS_WP_REQUEST_PARAM_BIRTHDAY_MONTH,	
  MRIM_CS_WP_REQUEST_PARAM_BIRTHDAY_DAY,	
  MRIM_CS_WP_REQUEST_PARAM_COUNTRY_ID,	
  MRIM_CS_WP_REQUEST_PARAM_MAX		
};
*/

public final static int MRIM_CS_ANKETA_INFO		=	0x1028; //S-&gt;C
//DWORD status 
	public final static int MRIM_ANKETA_INFO_STATUS_OK	=	1;
	public final static int MRIM_ANKETA_INFO_STATUS_NOUSER		= 0;
	public final static int MRIM_ANKETA_INFO_STATUS_DBERR	=	2;
	public final static int MRIM_ANKETA_INFO_STATUS_RATELIMERR	= 3;
//DWORD fields_num				
//DWORD max_rows
//DWORD server_time sec since 1970 (unixtime)
// fields set 				//%fields_num == 0
//values set 				//%fields_num == 0
//LPS value (numbers too)

	
public final static int MRIM_CS_MAILBOX_STATUS	=		0x1033	;
//DWORD new messages in mailbox


public final static int MRIM_CS_CONTACT_LIST2	=	0x1037 ;//S-&gt;C
// UL status
public final static int GET_CONTACTS_OK		=	0x0000;
public final static int GET_CONTACTS_ERROR	=	0x0001;
public final static int GET_CONTACTS_INTERR	=	0x0002;
//DWORD status  - if ...OK than this staff:
//DWORD groups number
//mask symbols table:
//'s' - lps
//'u' - unsigned long
//'z' - zero terminated string 
//LPS groups fields mask 
//LPS contacts fields mask 
//group fields
//contacts fields
//groups mask 'us' == flags, name
//contact mask 'uussuu' flags, flags, internal flags, status
	public final static int CONTACT_INTFLAG_NOT_AUTHORIZED	= 0x0001;


//old packet cs_login with cs_statistic
public final static int MRIM_CS_LOGIN2     =  	0x1038;  // C -&gt; S
public final static int MAX_CLIENT_DESCRIPTION = 256;
// LPS login
// LPS password
// DWORD status
//+ statistic packet data: 
// LPS client description //max 256



/*
typedef struct mrim_connection_params_t
{
	unsigned long	ping_period;
}
mrim_connection_params_t;
*/






}
