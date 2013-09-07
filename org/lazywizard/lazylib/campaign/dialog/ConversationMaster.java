package org.lazywizard.lazylib.campaign.dialog;

import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import java.util.HashMap;
import java.util.Map;

public class ConversationMaster
{
    private final Map<String, Node> conversations;

    public ConversationMaster()
    {
        conversations = new HashMap();
    }

    public void addConversation(String id, Node rootNode)
    {
        conversations.put(id, rootNode);
    }

    public void removeConversation(String id)
    {
        conversations.remove(id);
    }

    public void startConversation(CampaignFleetAPI fleet, String convId)
    {
        if (conversations.containsKey(convId))
        {
            conversations.get(convId).show(fleet);
        }
        else
        {
            throw new RuntimeException("No conversation with id '"
                    + convId + "' found!");
        }
    }
}
