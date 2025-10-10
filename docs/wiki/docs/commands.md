# Commands Reference

This page provides a complete reference for all ShyScoreboard commands. All commands require the `shyscoreboard.command` permission unless otherwise specified.

## 🎮 Getting Started

To see all available commands in-game, use:
```
/shyscoreboard help 1
```

---

## 📝 Command Overview

| Command | Purpose | Permission Required |
|---------|---------|-------------------|
| `/shyscoreboard add` | Add a COMMAND scoreboard to a player | `shyscoreboard.add` |
| `/shyscoreboard remove` | Remove a COMMAND scoreboard from a player | `shyscoreboard.remove` |
| `/shyscoreboard set` | Replace all scoreboards with a specific one | `shyscoreboard.set` |
| `/shyscoreboard update` | Refresh a player's scoreboard display | `shyscoreboard.update` |
| `/shyscoreboard reload` | Reload all configurations | `shyscoreboard.reload` |

---

## 🔧 Detailed Command Reference

### `/shyscoreboard add`
**Purpose:** Add a COMMAND-type scoreboard to a player's display list

```
/shyscoreboard add <scoreboard> [player]
```

**Parameters:**
- `<scoreboard>` - The name of the scoreboard to add (required)
- `[player]` - Target player name (optional, defaults to command sender)

**Behavior:**
- ✅ **Works with:** COMMAND-type scoreboards only
- ❌ **Ignored for:** GLOBAL and WORLDGUARD types
- 🔄 **Priority:** If multiple scoreboards are added, highest priority (lowest number) is displayed
- 🔒 **Requires:** Player must have `shyscoreboard.scoreboard.<scoreboard>` permission

**Examples:**
```bash
# Add lobby scoreboard to yourself
/shyscoreboard add lobby_scoreboard

# Add minigame scoreboard to specific player
/shyscoreboard add pvp_arena Steve

# Add VIP scoreboard (useful for temporary promotions)
/shyscoreboard add vip_board
```

**Common Use Cases:**
- Minigame plugin integration (add game scoreboard when joining)
- Region-based displays (add via world management plugins)
- Temporary status displays (events, competitions)

---

### `/shyscoreboard remove`
**Purpose:** Remove a COMMAND-type scoreboard from a player's display list

```
/shyscoreboard remove <scoreboard> [player]
```

**Parameters:**
- `<scoreboard>` - The name of the scoreboard to remove (required)
- `[player]` - Target player name (optional, defaults to command sender)

**Behavior:**
- ✅ **Works with:** COMMAND-type scoreboards only
- ❌ **Ignored for:** GLOBAL and WORLDGUARD types
- 🔄 **Priority:** After removal, next highest priority scoreboard is displayed
- 🔒 **Requires:** `shyscoreboard.remove` permission (not scoreboard permission)

**Examples:**
```bash
# Remove lobby scoreboard from yourself
/shyscoreboard remove lobby_scoreboard

# Remove minigame scoreboard from specific player
/shyscoreboard remove pvp_arena Steve

# Remove event scoreboard when event ends
/shyscoreboard remove summer_event
```

**Common Use Cases:**
- Minigame plugin integration (remove when leaving game)
- Region exit handling (remove region-specific scoreboards)
- Event cleanup (remove temporary displays)

---

### `/shyscoreboard set`
**Purpose:** Replace all active COMMAND scoreboards with a single specific scoreboard

```
/shyscoreboard set <scoreboard> [player]
```

**Parameters:**
- `<scoreboard>` - The name of the scoreboard to set (required)
- `[player]` - Target player name (optional, defaults to command sender)

**Behavior:**
- 🧹 **Clears:** All existing COMMAND-type scoreboards
- ➕ **Adds:** The specified scoreboard as the only COMMAND scoreboard
- ✅ **Works with:** COMMAND-type scoreboards only
- 🔒 **Requires:** Player must have `shyscoreboard.scoreboard.<scoreboard>` permission

**Examples:**
```bash
# Set only the lobby scoreboard (removes all others)
/shyscoreboard set lobby_scoreboard

# Force a specific player to see only the admin board
/shyscoreboard set admin_panel Notch

# Switch to event scoreboard (clearing previous displays)
/shyscoreboard set halloween_event
```

**Common Use Cases:**
- Clean state switching (ensure only one scoreboard is shown)
- Admin panel display (override all other scoreboards)
- Event mode activation (clear normal displays, show event board)

---

### `/shyscoreboard update`
**Purpose:** Refresh a player's scoreboard display and fix display issues

```
/shyscoreboard update [respawn] [player]
```

**Parameters:**
- `[respawn]` - Force a complete scoreboard respawn (optional)
- `[player]` - Target player name (optional, defaults to command sender)

**Behavior:**
- 🔄 **Refreshes:** Recalculates which scoreboard should be displayed
- 🛠️ **Fixes:** Issues caused by other plugins overriding scoreboards
- 💾 **Respawn mode:** Completely recreates the scoreboard display

**Examples:**
```bash
# Refresh your own scoreboard
/shyscoreboard update

# Fix display issues with complete respawn
/shyscoreboard update respawn

# Refresh a specific player's scoreboard
/shyscoreboard update false Steve

# Force respawn for a specific player
/shyscoreboard update respawn Alex
```

**Common Use Cases:**
- Troubleshooting display issues
- After other plugins modify scoreboards
- Permission changes not reflecting immediately
- Player reports "blank" or "wrong" scoreboard

---

### `/shyscoreboard reload`
**Purpose:** Reload all plugin configurations and scoreboard files

```
/shyscoreboard reload
```

**Parameters:** None

**Behavior:**
- 📁 **Reloads:** All `.yml` files in the scoreboard folder
- 🔄 **Refreshes:** All player scoreboards with new configurations
- ⚡ **Updates:** Settings take effect immediately

**Examples:**
```bash
# Reload after editing scoreboard files
/shyscoreboard reload
```

**Common Use Cases:**
- After editing scoreboard configuration files
- Adding new scoreboards without server restart
- Modifying existing scoreboard content
- Testing configuration changes

**⚠️ Important Notes:**
- Always run this command after editing `.yml` files
- Players will see updated scoreboards immediately
- Invalid configurations will show error messages

---

## 💡 Usage Tips

### Command Integration
Many server administrators integrate these commands with other plugins:

**World Management:**
```bash
# In MV-portals or similar
/shyscoreboard remove lobby_board
/shyscoreboard add minigame_board
```

**Minigame Plugins:**
```bash
# On arena join
/shyscoreboard set arena_stats %player%

# On arena leave  
/shyscoreboard set lobby_board %player%
```

**Event Plugins:**
```bash
# Event start
/shyscoreboard add event_board

# Event end
/shyscoreboard remove event_board
```

### Priority Management
When using multiple COMMAND scoreboards:
- Lower priority numbers = higher display priority
- Priority 1 will override priority 5
- Use priority gaps (1, 5, 10) to allow future additions

### Troubleshooting Commands
If players report scoreboard issues:
1. `/shyscoreboard update respawn [player]` - Force refresh
2. Check permissions: `shyscoreboard.scoreboard.<name>`
3. Verify scoreboard type matches usage method