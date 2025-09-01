# CoordLeak

CoordLeak is a simple Minecraft plugin that allows players to purchase "usages" with in-game currency to reveal the coordinates of a random online player.

This is a small hobby project, created for fun.

## Features

*   Leak the coordinates (X, Z, and dimension) of a random player.
*   Economy integration with Vault to buy usages.
*   Configurable messages and prefix.
*   Simple and lightweight.

## Commands & Usage

### `/coord`
Leaks the coordinates of a random online player.
*   **Usage:** `/coord`
*   **Permission:** `Not now` (Default for all players)
*   **Note:** This command requires the player to have at least one usage.

### `/buyusage`
Gives a player one usage of the `/coord` command, charging them the configured price.
*   **Usage:** `/buyusage <player>`
*   **Permission:** `Not now` (OP only)

### `/creload`
Reloads the plugin's configuration files (`config.yml` and `messages.yml`).
*   **Usage:** `/creload`
*   **Permission:** `Not now` (OP only)

## Configuration

### `config.yml`
```yaml
# The prefix that appears before plugin messages. Supports MiniMessage format.
prefix: "<i><gradient:#FFFFFF:#29E7D7>[ Coord ]</gradient></i>"

# The price for one usage of the /coord command when using /buyusage.
price: 1000
```

### `messages.yml`
```yaml
# Legacy format color codes are not supported, use MiniMessage instead
permission: "<red>You don't have permission to do this!</red>"
invalidArgument: "<yellow>Invalid argument. Please check your command.</yellow>"
invalidPlayer: "<yellow>That player is not online or does not exist.</yellow>"
notEnoughBalance: "<red>You don't have enough balance.</red>"
buySuccessfully: "<green>You have successfully purchased</green>"
soPoor: "<red>You have no more uses left for this command. Visit the shop to get more!</red>"
configReloaded: "Config reloaded"
noOneIsOnline: "<red>No players are currently online.</red>"

# Supported placeholder: {player}, {x}, {y}, {dimension}
randomSelect:
  message: "<white>ᴀ ʀᴀɴᴅᴏᴍ ᴘʟᴀʏᴇʀ ʜᴀѕ ʙᴇᴇɴ ѕᴇʟᴇᴄᴛᴇᴅ"
  target: "<white>ᴛᴀʀɢᴇᴛ: <cyan>{player}"
  coord: "<white>ᴄᴏᴏʀᴅ: <cyan>X={x}, Z={z}"
  dimension: "<white>ᴅɪᴍᴇɴѕɪᴏɴ: <cyan>{dimension}"

leak:
  exposed: "<red>Your location has been leaked!"
```

## Dependencies

*   **Vault**: (Required) For economy integration.
*   **Essentials**: (Optional) Soft dependency.

## Note

The permission system is not fully implemented yet. I will update it as soon as I have more time.
BTW im just a java beginner

## Contributing

Contributions are welcome! If you have any ideas or improvements, feel free to fork the repository and submit a pull request.

## License

This project is licensed under the **GNU General Public License v3.0**.
