package net.vortexdata.tsqpf_plugin_dynamicchannel;

import com.github.theholywaffle.teamspeak3.api.*;
import com.github.theholywaffle.teamspeak3.api.event.*;
import net.vortexdata.tsqpf.plugins.*;

import java.util.*;
import java.util.stream.*;

public class DynamicChannel extends TeamspeakPlugin {

    private List<Integer> createChannelIds;
    private int parentChannelId;
    private int lobbyChannelId;
    private int channelOwnerChannelGroup;
    private boolean useGroupWhitelist;
    private int[] whitelistedGroups;

    @Override
    public void onEnable() {
        getConfig().setDefault("messageCreationRunning", "Creating channel...");
        getConfig().setDefault("messageCreationFailed", "Sorry, but your channel could not be created as an unknown error occurred. Please contact the server administration.");
        getConfig().setDefault("messageCreationFailedExisting", "Sorry, but it looks like you already have a channel.");
        getConfig().setDefault("messageCreationFailedPermission", "Sorry, but it looks like you don't have permission to create a channel.");
        getConfig().setDefault("messageCreationSuccess", "Channel created, moving you now...");
        getConfig().setDefault("whitelistedGroups", "7");
        getConfig().setDefault("useGroupWhitelist", "false");
        getConfig().setDefault("channelName", "%clientNickname%'s Channel");
        getConfig().setDefault("channelCodecQuality", "6");
        getConfig().setDefault("channelDescription", "6");
        getConfig().setDefault("channelDeleteDelaySeconds", "120");
        getConfig().setDefault("parentChannelId", "0");
        getConfig().setDefault("createChannelIds", "0");
        getConfig().setDefault("lobbyChannelId", "0");
        getConfig().setDefault("channelOwnerChannelGroup", "0");
        getConfig().saveAll();

        channelOwnerChannelGroup = Integer.parseInt(getConfig().readValue("channelOwnerChannelGroup"));

        createChannelIds = Arrays.stream(getConfig().readValue("createChannelIds").split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        parentChannelId = Integer.parseInt(getConfig().readValue("parentChannelId"));
        lobbyChannelId = Integer.parseInt(getConfig().readValue("lobbyChannelId"));
        useGroupWhitelist = Boolean.parseBoolean(getConfig().readValue("useGroupWhitelist"));

        if (useGroupWhitelist) {
            try {
                String[] groupsS = getConfig().readValue("whitelistedGroups").split(",");
                whitelistedGroups = new int[groupsS.length];
                for (int i = 0; i < whitelistedGroups.length; ++i)
                    whitelistedGroups[i] = Integer.parseInt(groupsS[i]);
            } catch (Exception e) {
                getLogger().printError("Failed to parse whitelistedGroups, setting useGroupWhitelist to false.");
                useGroupWhitelist = false;
            }
        }

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onClientMoved(ClientMovedEvent clientMovedEvent) {
        if (createChannelIds.contains(clientMovedEvent.getTargetChannelId())) {
            if (useGroupWhitelist) {
                boolean hasPermissions = false;
                int[] clientGroups = getAPI().getClientInfo(clientMovedEvent.getClientId()).getServerGroups();
                for (int c : clientGroups) {
                    for (int w : whitelistedGroups)
                        if (w == c) {
                            hasPermissions = true;
                        }
                }

                if (hasPermissions) {
                    createChannel(clientMovedEvent);
                } else {
                    getAPI().sendPrivateMessage(getAPI().getClientInfo(clientMovedEvent.getClientId()).getId(), getConfig().readValue("messageCreationFailedPermission"));
                }
            } else {
                createChannel(clientMovedEvent);
            }
        }
    }

    private int createChannel(ClientMovedEvent cme) {
        HashMap<ChannelProperty, String> channelProperties = new HashMap<>();
        channelProperties.put(ChannelProperty.CHANNEL_FLAG_TEMPORARY, "1");
        channelProperties.put(ChannelProperty.CHANNEL_DELETE_DELAY, getConfig().readValue("channelDeleteDelaySeconds"));
        channelProperties.put(ChannelProperty.CHANNEL_DESCRIPTION, getConfig().readValue("channelDescription"));
        channelProperties.put(ChannelProperty.CHANNEL_CODEC_QUALITY, getConfig().readValue("channelCodecQuality"));
        channelProperties.put(ChannelProperty.CPID, getConfig().readValue("parentChannelId"));

        int newChannelId = -1;

        try {
            newChannelId = getAPI().createChannel(PatternParser.parseMessage(getConfig().readValue("channelName"), cme, getAPI()), channelProperties);
            getAPI().sendPrivateMessage(getAPI().getClientInfo(cme.getClientId()).getId(), getConfig().readValue("messageCreationSuccess"));
            getAPI().moveClient(getAPI().getClientInfo(cme.getClientId()).getId(), newChannelId);
            // Move API back to default channel
            getAPI().moveClient(getAPI().whoAmI().getId(), lobbyChannelId);
        } catch (Exception e) {
            getAPI().sendPrivateMessage(getAPI().getClientInfo(cme.getClientId()).getId(), getConfig().readValue("messageCreationFailed"));
            getLogger().printError("Failed to create channel for client " + cme.getInvokerName());
        }

        if (newChannelId != -1) {
            getAPI().setClientChannelGroup(channelOwnerChannelGroup, newChannelId, getAPI().getClientInfo(cme.getClientId()).getDatabaseId());
        }


        return newChannelId;
    }
}
