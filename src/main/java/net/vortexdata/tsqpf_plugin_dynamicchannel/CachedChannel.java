package net.vortexdata.tsqpf_plugin_dynamicchannel;

public class CachedChannel {

    private String dynchaUUID;
    private int channelId;
    private String ownerUUID;

    public CachedChannel(String dynchaUUID, int channelId, String ownerUUID) {
        this.dynchaUUID = dynchaUUID;
        this.channelId = channelId;
        this.ownerUUID = ownerUUID;
    }

    public String getDynchaUUID() {
        return dynchaUUID;
    }

    public int getChannelId() {
        return channelId;
    }

    public String getOwnerUUID() {
        return ownerUUID;
    }
}
