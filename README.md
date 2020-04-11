# DynamicChannel
Remove any unnecessary static channels that aren't used frequently and replace them with dynamically generated channels. 

## 💡 How does it work?

When a client joins a specific channel, the framework will go ahead and create a new temporary channel and move them there.

## 🚀 Gettings started

Just download the latest release that's compatible with your TSQPF version and copy it into its plugin directory. After you've done that, either reload or restart your framework instance in order to get it loaded and initiated.

## ⚙️ Configuration

Here's a list of all config keys, value datatypes and a description:

KEY | DATATYPE | DESCRIPTION

- **messageCreationRunning** : [String] Message when channel is being created.
- **messageCreationFailed** : [String] When channel creation has failed for unknown reason.
- **messageCreationFailedExisting** : [String] When channel creation has failed because a channel with same name already exists.
- **messageCreationFailedPermission** : [String] When channel creation has failed because client does not have permission.
- **messageCreationSuccess** : [String] When channel creation was completed successfully.
- **whitelistedGroups** : [Integer] Array of group ids which can create a channel if useGroupWhitelist is set to true (Format: 6,12,4,14).
- **useGroupWhitelist** : [Boolean] Defines if plugin uses whitelistedGroups.
- **channelName** : [String] Defines the name pattern of a new channel.
- **parentChannelId** : [Integer] Defines the parent channel which all dynamic channel will be a child of.
- **createChannelId** : [Integer] Defines the channel which users have to join to create a channel.
- **lobbyChannelId** : [Integer] Defines the lobby channel id.

## ⚙️ Channel Name Placeholder

Here's a list of all placeholders that can be used in the channel name:

KEY | DESCRIPTION

- **clientNickname** : The clients nickname.


## 📁 Directory Tree

DynamicChannel/<br>
└── plugin.conf<br>

## 📜 Vortexdata Certification

This plugin is developed by VortexdataNET for the Teamspeak Query Plugin Framework. Every release is being tested for any bugs, its performance or security issues. You are free to use, modify or redistribute the plugin.
