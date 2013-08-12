package info.guardianproject.otr.app.im.plugin.jmmp;

import info.guardianproject.otr.app.im.engine.Address;
import info.guardianproject.otr.app.im.engine.ChatGroupManager;
import info.guardianproject.otr.app.im.engine.ChatSession;
import info.guardianproject.otr.app.im.engine.ChatSessionListener;
import info.guardianproject.otr.app.im.engine.ChatSessionManager;
import info.guardianproject.otr.app.im.engine.Contact;
import info.guardianproject.otr.app.im.engine.ContactList;
import info.guardianproject.otr.app.im.engine.ContactListListener;
import info.guardianproject.otr.app.im.engine.ContactListManager;
import info.guardianproject.otr.app.im.engine.ImConnection;
import info.guardianproject.otr.app.im.engine.ImEntity;
import info.guardianproject.otr.app.im.engine.ImErrorInfo;
import info.guardianproject.otr.app.im.engine.ImException;
import info.guardianproject.otr.app.im.engine.Message;
import info.guardianproject.otr.app.im.engine.Presence;
import info.guardianproject.otr.app.im.provider.Imps;
import info.guardianproject.util.LogCleaner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ru.mmp.core.MMPClient;
import ru.mmp.core.Status;
import ru.mmp.listener.MessageListener;
import ru.mmp.listener.StatusListener;
import android.content.ContentResolver;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Parcel;

public class JMMPConnection extends ImConnection {

    protected static final String TAG = "JMMPConnection";
    private JMMPContactList mContactListManager;
    private Contact mUser;

    private MMPClient mClient;
    
    private ChatSessionManager mChatSessionManager;
    
    private Context mContext;
    
    public JMMPConnection(Context context) {
        super(context);
    
        mContext = context;
        
    }

    @Override
    protected void doUpdateUserPresenceAsync(Presence presence) {
        
        int statusId = Status.STATUS_AWAY;
        
         if (presence.getStatus() == Presence.AVAILABLE)
             statusId = Status.STATUS_ONLINE;
        
        mClient.setStatus(statusId, presence.getStatusText(), presence.getStatusText());
        
        /*
        // mimic presence
        ContactList cl;
        try {
            cl = mContactListManager.getDefaultContactList();
        } catch (ImException e) {
            throw new RuntimeException(e);
        }
        if (cl == null)
            return;
        Collection<Contact> contacts = cl.getContacts();
        for (Iterator<Contact> iter = contacts.iterator(); iter.hasNext();) {
            Contact contact = iter.next();
            contact.setPresence(presence);
        }
        Contact[] contacts_array = new Contact[contacts.size()];
        contacts.toArray(contacts_array);
        mContactListManager.doPresence(contacts_array);
        */
    }

    @Override
    public int getCapability() {
        return 0;
    }

    @Override
    public ChatGroupManager getChatGroupManager() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ChatSessionManager getChatSessionManager() {
        
        if (mChatSessionManager == null)
        {
      
            mChatSessionManager = new ChatSessionManager() {
    
                @Override
                public void sendMessageAsync(ChatSession session, Message message) {
                 
                    mClient.sendMsg(message.getTo().getAddress(), message.getBody());
                }
    
                @Override
                public synchronized void addChatSessionListener(ChatSessionListener listener) {
                    super.addChatSessionListener(listener);
                }
    
                @Override
                public synchronized void removeChatSessionListener(ChatSessionListener listener) {
                    super.removeChatSessionListener(listener);
                }
    
                @Override
                public synchronized ChatSession createChatSession(ImEntity participant) {
                    return super.createChatSession(participant);
                }
    
                @Override
                public void closeChatSession(ChatSession session) {
                    super.closeChatSession(session);
                }
                
            };
        }
           
           return mChatSessionManager;

    }

    @Override
    public ContactListManager getContactListManager() {
        mContactListManager = new JMMPContactList();
        return mContactListManager;
    }

    @Override
    public Contact getLoginUser() {
        return mUser;
    }

    @Override
    public HashMap<String, String> getSessionContext() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int[] getSupportedPresenceStatus() {
        return new int[] { Presence.AVAILABLE, Presence.AWAY,  Presence.OFFLINE,
                     };
    }

    @Override
    public void loginAsync(long accountId, String passwordTemp, long providerId, boolean retry) {
        
        ContentResolver contentResolver = mContext.getContentResolver();
        String userName = Imps.Account.getUserName(contentResolver, accountId);
        
        
        Imps.ProviderSettings.QueryMap providerSettings = new Imps.ProviderSettings.QueryMap(
                contentResolver, providerId, false, null);
        String domain = providerSettings.getDomain();
        
        userName = userName + '@' + domain;
        
        mUserPresence = new Presence(Presence.AVAILABLE, "available", null, null,
                Presence.CLIENT_TYPE_DEFAULT);
        mUser = new Contact(new JMMPAddress(userName, "JMMP"), userName);                       
        
        if (mClient == null)
            mClient = new MMPClient();

        mClient.setEmail(userName);
        mClient.setPass(passwordTemp);

    
        mClient.addMessageListener(new MessageListener()
        {

            @Override
            public void onAuthorization(String arg0, String arg1) {
                LogCleaner.debug(TAG, "onAuthorization(): " + arg0 + "," + arg1);
                
            }

            @Override
            public void onIncomingMessage(String arg0, String arg1) {
                LogCleaner.debug(TAG, "onIncomingMessage(): " + arg0 + "," + arg1);
             
                
            }

            @Override
            public void onMessageAck() {
                LogCleaner.debug(TAG, "onMessageAck()");
                
            }

            @Override
            public void onMessageError() {
                LogCleaner.debug(TAG, "onMessageError()");
                
            }
            
        });
        
        mClient.addStatusListener(new StatusListener ()
        {

            @Override
            public void onAuthorizationError(String arg0) {
                LogCleaner.debug(TAG, "authorization error: " + arg0);
                
                ImErrorInfo info = new ImErrorInfo(ImErrorInfo.CANT_CONNECT_TO_SERVER, arg0);
                setState(ImConnection.DISCONNECTED, info);

            }

            @Override
            public void onLogin() {

                LogCleaner.debug(TAG, "onLogin()");
                setState(LOGGED_IN, null);
                
             //   mClient.Authorize("guardianproject@mail.ru");
             //   mClient.setStatus(ru.mmp.core.Status.STATUS_ONLINE,"online","having fun");

            }

            @Override
            public void onLogout() {
                
               LogCleaner.debug(TAG, "onLogout()");
               setState(ImConnection.DISCONNECTED, null);

            }
            
        });

        
        new LoginTask().execute("");

    }

    @Override
    public void logoutAsync() {

        try {
            mClient.disconnect();
            
        } catch (Exception e) {
           LogCleaner.warn(TAG, "could not disonnected: " + e.getMessage());
        }
        
        setState(ImConnection.DISCONNECTED, null);
        
        mClient = null;          
        
        
    }

    @Override
    public void reestablishSessionAsync(Map<String, String> sessionContext) {
       

    }

    @Override
    public void suspend() {
       

    }

    private final class JMMPContactList extends ContactListManager {
        @Override
        protected void setListNameAsync(String name, ContactList list) {
            // TODO Auto-generated method stub

        }

        @Override
        public String normalizeAddress(String address) {
            return address;
        }

        @Override
        public void loadContactListsAsync() {
            Collection<Contact> contacts = new ArrayList<Contact>();
            Contact[] contacts_array = new Contact[0];
            contacts.toArray(contacts_array);
            
            /*
            Address dummy_addr = new JMMPAddress("dummy", "dummy@google.com");

            Contact dummy = new Contact(dummy_addr, "dummy");
            dummy.setPresence(new Presence(Presence.AVAILABLE, "available", null, null,
                    Presence.CLIENT_TYPE_DEFAULT));
            contacts.add(dummy);
            */
            
            ContactList cl = new ContactList(mUser.getAddress(), "default", true, contacts, this);
            mContactLists.add(cl);
            mDefaultContactList = cl;
            notifyContactListLoaded(cl);
            notifyContactsPresenceUpdated(contacts.toArray(contacts_array));
            notifyContactListsLoaded();
        }

        public void doPresence(Contact[] contacts) {
            notifyContactsPresenceUpdated(contacts);
        }

        @Override
        protected ImConnection getConnection() {
            return JMMPConnection.this;
        }

        @Override
        protected void doRemoveContactFromListAsync(Contact contact, ContactList list) {
            // TODO Auto-generated method stub

        }

        @Override
        protected void doDeleteContactListAsync(ContactList list) {
            // TODO Auto-generated method stub

        }

        @Override
        protected void doCreateContactListAsync(String name, Collection<Contact> contacts,
                boolean isDefault) {
            // TODO Auto-generated method stub
            return;

        }

        @Override
        protected void doBlockContactAsync(String address, boolean block) {
            // TODO Auto-generated method stub

        }

        @Override
        protected void doAddContactToListAsync(String address, ContactList list) throws ImException {
            Contact contact = new Contact(new JMMPAddress(address, address), address);
            contact.setPresence(new Presence(Presence.AVAILABLE, "available", null, null,
                    Presence.CLIENT_TYPE_DEFAULT));
            notifyContactListUpdated(list, ContactListListener.LIST_CONTACT_ADDED, contact);
            Contact[] contacts = new Contact[] { contact };
            mContactListManager.doPresence(contacts);
        }

        @Override
        public void declineSubscriptionRequest(String contact) {
            // TODO Auto-generated method stub

        }

        @Override
        public Contact createTemporaryContact(String address) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void approveSubscriptionRequest(String contact) {
            // TODO Auto-generated method stub
            return;
        }
    }

    class JMMPAddress extends Address {

        private String address;
        private String name;

        public JMMPAddress() {
        }

        public JMMPAddress(String name, String address) {
            this.name = name;
            this.address = address;
        }

        @Override
        public String getAddress() {
            return name;
        }

        @Override
        public String getScreenName() {
            return address;
        }

        @Override
        public void readFromParcel(Parcel source) {
            name = source.readString();
            address = source.readString();
        }

        @Override
        public void writeToParcel(Parcel dest) {
            dest.writeString(name);
            dest.writeString(address);
        }

    }

    public void sendHeartbeat(long heartbeatInterval) {
    }

    public void setProxy(String type, String host, int port) {
    }

    @Override
    public void logout() {
        
        logoutAsync();
        
    }
    
    // The definition of our task class
    private class LoginTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
           super.onPreExecute();
        }
      
        @Override
        protected String doInBackground(String... params) {
            
    
            
            try {
                mClient.connect();
                mClient.login();
                setState(LOGGING_IN, null);
    
            } catch (IOException e) {
                ImErrorInfo info = new ImErrorInfo(ImErrorInfo.CANT_CONNECT_TO_SERVER, e.getMessage());
                setState(DISCONNECTED, info);
    
                LogCleaner.debug(TAG, "unable to login: " + e.getMessage());
            }
            
    
           return "success";
        }
      
        @Override
        protected void onProgressUpdate(Integer... values) {
           super.onProgressUpdate(values);
        }
      
        @Override
        protected void onPostExecute(String result) {
           super.onPostExecute(result);
        }
    }

    @Override
    public Presence getUserPresence() {
       
        return super.getUserPresence();
    }

    
}
