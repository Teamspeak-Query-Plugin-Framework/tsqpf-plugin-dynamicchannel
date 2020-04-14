package net.vortexdata.tsqpf_plugin_dynamicchannel;

import com.github.theholywaffle.teamspeak3.*;
import com.github.theholywaffle.teamspeak3.api.event.*;
import com.github.theholywaffle.teamspeak3.api.wrapper.*;
import net.vortexdata.tsqpf.plugins.*;

import java.util.*;

public class ChannelCache {

    ArrayList<CachedChannel> dynamicChannels;
    private TS3Api api;
    private PluginLogger logger;

    public ChannelCache(TS3Api api, PluginLogger logger) {
        dynamicChannels = new ArrayList<>();
        this.api = api;
        this.logger = logger;
    }

    public CachedChannel registerNewChannel(String UUID, int channelId, String ownerId) {
        CachedChannel exportChannel = new CachedChannel(
                UUID,
                channelId,
                ownerId
        );
        dynamicChannels.add(exportChannel);
        logger.printDebug("Registered new channel to " + api.getClientByUId(ownerId).getNickname() + " ("+ownerId+"): " + UUID);
        return exportChannel;
    }

    public boolean removeChannelById(int channelId) {
        CachedChannel remove = getChannelById(channelId);
        if (remove != null) {
            dynamicChannels.remove(remove);
            return true;
        } else
            return false;
    }

    public CachedChannel getChannelByOwner(String ownerUUID) {
        for (CachedChannel channel : dynamicChannels) {
            if (channel.getOwnerUUID().equals(ownerUUID))
                return channel;
        }
        return null;
    }

    public CachedChannel getChannelById(int channelId) {
        for (CachedChannel channel : dynamicChannels) {
            if (channel.getChannelId() == channelId)
                return channel;
        }
        return null;
    }

    private String getOwnerByChannelId(String dynchaUUID) {
        for (CachedChannel channel : dynamicChannels) {
            if (channel.getDynchaUUID().equals(dynchaUUID))
                return channel.getOwnerUUID();
        }
        return null;
    }

    public boolean doesClientHaveChannel(String clientUUID) {
        for (CachedChannel channel : dynamicChannels)
            if (channel.getOwnerUUID().equals(clientUUID))
                return true;
        return false;
    }

    public void onChannelDelete(ChannelDeletedEvent cde) {
        CachedChannel removedChannel = getChannelById(cde.getChannelId());
        if (removedChannel != null) {
            logger.printDebug("Deleted dynamic channel from cache ("+removedChannel.getDynchaUUID()+").");
            dynamicChannels.remove(removedChannel);
        }
    }

}
