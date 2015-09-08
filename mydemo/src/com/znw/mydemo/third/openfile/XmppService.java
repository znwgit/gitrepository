package com.znw.mydemo.third.openfile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.RosterPacket.ItemType;

import com.znw.mydemo.utils.debug.DebugUtils;


public class XmppService{

	
	/** 
     * 删除当前用户 
     * @param connection 
     * @return 
     */  
    public static boolean deleteAccount(XMPPConnection connection)  
    {  
        try {  
            connection.getAccountManager().deleteAccount();         
            return true;  
        } catch (Exception e) {  
            return false;  
        }  
    }  
	/**
	 * 返回所有组信息 <RosterGroup>
	 * @return List(RosterGroup)
	 */
	public static List<RosterGroup> getGroups(Roster roster) {
		List<RosterGroup> groupsList = new ArrayList<RosterGroup>();
		Collection<RosterGroup> rosterGroup = roster.getGroups();
		Iterator<RosterGroup> i = rosterGroup.iterator();
		while (i.hasNext())
			groupsList.add(i.next());
		return groupsList;
	}

	/**
	 * 返回相应(groupName)组里的所有用户<RosterEntry>
	 * @return List(RosterEntry)
	 */
	public static List<RosterEntry> getEntriesByGroup(Roster roster,String groupName) {
		List<RosterEntry> EntriesList = new ArrayList<RosterEntry>();
		RosterGroup rosterGroup = roster.getGroup(groupName);
		Collection<RosterEntry> rosterEntry = rosterGroup.getEntries();
		Iterator<RosterEntry> i = rosterEntry.iterator();
		while (i.hasNext())
			EntriesList.add(i.next());
		return EntriesList;
	}

	/**
	 * 返回所有用户信息 <RosterEntry>
	 * @return List(RosterEntry)
	 */
	public static List<RosterEntry> getAllEntries(Roster roster) {
		List<RosterEntry> EntriesList = new ArrayList<RosterEntry>();
		Collection<RosterEntry> rosterEntry = roster.getEntries();
		Iterator<RosterEntry> i = rosterEntry.iterator();
		while (i.hasNext())
			EntriesList.add(i.next());
		return EntriesList;
	}
	
	
	/** 
     * 创建一个组 
     */ 
	public static boolean addGroup(Roster roster,String groupName)  
    {  
        try {  
            roster.createGroup(groupName);  
            return true;  
        } catch (Exception e) {  
            e.printStackTrace();  
            return false;  
        }  
    }  
      
    /** 
     * 删除一个组 
     */  
    public static boolean removeGroup(Roster roster,String groupName)  
    {  
        return false;  
    }
    
    /**
	 * 更改与对方好友的状态
	 */
	public static boolean changeItemType(Roster roster,String userName,String name,ItemType itemType,String toJID)
	{
		try {
			roster.changeItemType(userName, name, null, itemType,toJID);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
    
    /**
	 * 添加一个好友  无分组
	 */
	public static boolean addUser(Roster roster,String userName,String name)
	{
		try 
		{
			if(roster != null && roster.getEntries() != null)
			{
				Iterator<RosterEntry> it = roster.getEntries().iterator();
				while(it.hasNext())
				{
					RosterEntry re= it.next();
					DebugUtils.print("???????????????????????????????????????????????????????????????????????\n");
					DebugUtils.print("RosterEntry re =" + ((re == null) ? "re ==========null":re.toString()));
					DebugUtils.print("\n???????????????????????????????????????????????????????????????????????\n");
				}
					
			}
			
			RosterEntry re = roster.getEntry(userName);
			DebugUtils.print("??????????????????????????????????===?????????????????????????????????????\n");
			DebugUtils.print("RosterEntry re =" + ((re == null) ? "re ==========null":re.toString()));
			DebugUtils.print("\n????????????????????????????????===???????????????????????????????????????\n");
			roster.createEntry(userName, name, null);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	/**
	 * 添加一个好友到分组
	 * @param roster
	 * @param user
	 * @param nickName
	 * @return
	 */
	public static boolean addUsers(Roster roster,String user,String nickName,String[] groupNameArray)
	{
		try {
			roster.createEntry(user,nickName,groupNameArray);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	/**
	 * 删除一个好友
	 * @param roster
	 * @param userJid
	 * @return
	 */
	public static boolean removeUser(Roster roster,String userJid)
	{
		try {
			RosterEntry entry = roster.getEntry(userJid);
			roster.removeEntry(entry);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}		
	}
	
	/**
     * 把一个好友添加到一个组中
     * @param userJid
     * @param groupName
     */
    public static void addUserToGroup(final String userJid, final String groupName,final XMPPConnection connection) {
    	DebugUtils.print(XmppService.class,"群组1"+connection.getRoster().toString());
    	DebugUtils.print(XmppService.class,"群组2"+connection.getUser().toString());
    	RosterGroup group = connection.getRoster().getGroup(groupName);
        // 这个组已经存在就添加到这个组，不存在创建一个组
//        RosterEntry entry = connection.getRoster().getEntry(userJid);
        RosterEntry entry = null;
        Collection<RosterEntry> entries = connection.getRoster().getEntries();
        for (RosterEntry rosterEntry : entries) {
        	if(userJid.equals(rosterEntry.getUser()) || userJid.contains(rosterEntry.getUser())){
        		entry = rosterEntry;
        		break;
        	}
		}
        
        try {
            if (group != null) {
                if (entry != null){
                	group.addEntry(entry);
                	DebugUtils.print(XmppService.class,"群组【"+group.getName()+"】存在,添加:"+userJid+"成功");
                }else{
                	DebugUtils.print(XmppService.class,"群组【"+group.getName()+"】存在,添加:"+userJid+"失败");
                }
            } else {
                RosterGroup newGroup = connection.getRoster().createGroup(groupName);
                if (entry != null){
                	newGroup.addEntry(entry);
                	DebugUtils.print(XmppService.class,"创建新群组【"+newGroup.getName()+"】,添加:"+userJid+"成功");
                }else{
                	DebugUtils.print(XmppService.class,"创建新群组【"+newGroup.getName()+"】,添加:"+userJid+"失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addUG(final String userJid, final String groupName,XMPPConnection connection){
    	IQ iq = new IQ()
		{
			@Override
			public String getChildElementXML()
			{
				StringBuilder buf = new StringBuilder();
//				buf.append("<query xmlns=\"com:bjdaily:im:group:manager\" action=\"create_group\" group_name=\"测试组00111\"/>");	//创建组(ok)
//				buf.append("<query xmlns=\"com:bjdaily:im:group:manager\" action=\"update_group_name\" group_name=\"测试组-\" group_new_name=\"测试组-2\"/>");	//修改组名（ok）
//				buf.append("<query xmlns=\"com:bjdaily:im:group:manager\" action=\"update_group_desc\" group_name=\"测试组-2\" desc=\"测试组-2的描述\"/>");	//修改组说明（OK）
				buf.append("<query xmlns=\"com:bjdaily:im:group:manager\" action=\"add_group_user\" group_name=\""+groupName+"\" user_name=\""+userJid+"\"/>");	//增加用户（OK）
//				buf.append("<query xmlns=\"com:bjdaily:im:group:manager\" action=\"remove_group_user\" group_name=\"灵动创展\" user_name=\"18001282789\"/>");	//移除用户（OK）
//				buf.append("<query xmlns=\"com:bjdaily:im:group:manager\" action=\"delete_group\" group_name=\"测试组1\"/>");	//删除群组（OK）
//				buf.append("<query xmlns=\"com:bjdaily:im:group:manager\" action=\"update_group_name\" group_name=\"&#x7075;&#x52A8;&#x521B;&#x5C55;\" group_new_name=\"&#x7075;&#x52A8;&#x521B;&#x5C55;&#x79D1;&#x6280;&#x5927;&#x5B66;\"/>");	//修改组名（ok）
				
				return buf.toString();
			}

		};
		connection.sendPacket(iq);
    }
    public static void removeUG(final String userJid, final String groupName,XMPPConnection connection){
    	IQ iq = new IQ()
    	{
    		@Override
    		public String getChildElementXML()
    		{
    			StringBuilder buf = new StringBuilder();
    			buf.append("<query xmlns=\"com:bjdaily:im:group:manager\" action=\"remove_group_user\" group_name=\""+groupName+"\" user_name=\""+userJid+"\"/>");	//移除用户（OK）
    			
    			return buf.toString();
    		}
    		
    	};
    	connection.sendPacket(iq);
    }
    /**
     * 把一个好友从组中删除
     * @param userJid
     * @param groupName
     */
    public static void removeUserFromGroup(final String userJid,final String groupName, final XMPPConnection connection) {
    	RosterGroup group = connection.getRoster().getGroup(groupName);
    	
        if (group != null) {
            try {
//            	RosterEntry entry = connection.getRoster().getEntry(userJid);
//            	Collection<RosterEntry> entries = connection.getRoster().getEntries();
            	RosterEntry entry = null;
            	Collection<RosterEntry> entries2 = group.getEntries2();
            	if(entries2 != null){
            		for (RosterEntry rosterEntry : entries2) {
            			if(userJid.equals(rosterEntry.getUser()) || userJid.contains(rosterEntry.getUser())){
            				entry = rosterEntry;
            				break;
            			}
            		}
            	}
				if (entry != null) {
					group.removeEntry(entry);
					DebugUtils.print(XmppService.class,"群组【"+group.getName()+"】存在,删除:"+userJid+"成功");
				}else {
					DebugUtils.print(XmppService.class,"群组【"+group.getName()+"】存在,删除:"+userJid+"失败");
				}
            } catch (XMPPException e) {
                e.printStackTrace();
            }
        }else {
        	DebugUtils.print(XmppService.class,"群组【"+groupName+"】不存在,删除:"+userJid+"失败");
		}
     }
    
    /** 
     * 修改心情 
     * @param connection 
     * @param status 
     */  
    public static void changeStateMessage(final XMPPConnection connection,final String status)  
    {  
        Presence presence = new Presence(Presence.Type.available);  
        presence.setStatus(status);  
        connection.sendPacket(presence);      
    }  
    
    
    /** 
     * 判断OpenFire用户的状态 strUrl :  
     * 返回值 : 0 - 用户不存在; 1 - 用户在线; 2 - 用户离线  
     * 说明 ：必须要求 OpenFire加载 presence 插件，同时设置任何人都可以访问 
     */    
    public static int IsUserOnLine(String user) {  
        String url = "http://"+ XmppConnection.SERVER_HOST + ":9090/plugins/presence/status?jid="+ user +"@"+ XmppConnection.SERVER_HOST + "&type=xml";
        
        int shOnLineState = 0; // 不存在  
        try {  
            URL oUrl = new URL(url);  
            URLConnection oConn = oUrl.openConnection();  
            if (oConn != null) {  
                BufferedReader oIn = new BufferedReader(new InputStreamReader(oConn.getInputStream()));  
                if (null != oIn) {  
                    String strFlag = oIn.readLine();  
                    oIn.close();  
                    if (strFlag.indexOf("type=\"unavailable\"") >= 0) {  
                        shOnLineState = 2;  
                    }  
                    if (strFlag.indexOf("type=\"error\"") >= 0) {  
                        shOnLineState = 0;  
                    } else if (strFlag.indexOf("priority") >= 0 || strFlag.indexOf("id=\"") >= 0) {  
                        shOnLineState = 1;  
                    }  
                }  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return shOnLineState;  
    }  
  
    
}
