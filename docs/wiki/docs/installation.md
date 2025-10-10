# Configuration Guide

This guide will walk you through creating and configuring scoreboards in ShyScoreboard. By the end, you'll understand the different scoreboard types and how to set them up for your server.

## 📋 Understanding Scoreboard Types

ShyScoreboard offers three display modes, each designed for different server setups:

### 🌍 GLOBAL Scoreboards
**Best for: Servers with organized permission groups**

Global scoreboards are **always visible** to players who have the required permission. They automatically appear when a player joins and update when permissions change.

**✅ Use GLOBAL when:**
* You have well-defined permission groups (Admin, VIP, Member, etc.)
* You use permission plugins like LuckPerms
* You want scoreboards to automatically show based on player roles

**Example Permission:** `shyscoreboard.scoreboard.sample_scoreboard`

### ⚡ COMMAND Scoreboards  
**Best for: Servers with OP players or dynamic regions**

Command scoreboards only appear after being manually added via commands. This gives you full control over when and where scoreboards are displayed.

**✅ Use COMMAND when:**
* You have OP players who need flexible scoreboard control
* You want to show different scoreboards in different worlds/regions
* You're integrating with world management or minigame plugins

**Required:** Permission + `/shyscoreboard add <scoreboard>` command

### 🛡️ WORLDGUARD Scoreboards
**Best for: Servers already using WorldGuard**

WorldGuard scoreboards automatically appear when players enter regions with the appropriate flag. This integrates seamlessly with your existing region setup.

**✅ Use WORLDGUARD when:**
* You're already using WorldGuard for region management
* You want scoreboards tied to specific areas
* You need support for overlapping regions

**Required:** Permission + WorldGuard region flag

---

## 🔧 Creating Your First Scoreboard

### Step 1: Prepare the Configuration

1. **Navigate to the scoreboard folder:**
   ```
   /plugins/ShyScoreboard/scoreboard/
   ```

2. **Disable the sample scoreboard:**
   - Open `sample_scoreboard.yml`
   - Change `type: "GLOBAL"` to `type: "COMMAND"`
   - This prevents the sample from interfering with your setup

3. **Create your scoreboard file:**
   - Copy `sample_scoreboard.yml` and rename it (e.g., `lobby_scoreboard.yml`)
   - The filename should match your scoreboard's purpose

### Step 2: Configure Your Scoreboard

Open your new scoreboard file and configure these key settings:

```yaml
# Must match your filename (without .yml)
name: "lobby_scoreboard"

# Choose your display type
type: "GLOBAL"  # or "COMMAND" or "WORLDGUARD"

# Lower numbers = higher priority
priority: 1

# How often to update (60 ticks = 3 seconds)
refreshTicks: 60

# Display title (max 16 characters)
title: "&b&lMy Server"

# Scoreboard lines (max 32 characters each)
lines:
  - "&7Welcome, %player_name%!"
  - "&eOnline: %server_online%"
  - "&6Rank: %vault_rank%"
  - ""
  - "&awww.myserver.com"
```

### Step 3: Apply Your Configuration

Run the reload command in-game:
```
/shyscoreboard reload
```

---

## 🎮 Activating Your Scoreboard

The activation method depends on your chosen scoreboard type:

### For GLOBAL Scoreboards

1. **Grant the permission:**
   ```
   shyscoreboard.scoreboard.lobby_scoreboard
   ```

2. **That's it!** The scoreboard will automatically appear for players with this permission.

### For COMMAND Scoreboards

1. **Grant the permission:**
   ```
   shyscoreboard.scoreboard.lobby_scoreboard
   ```

2. **Add the scoreboard to players:**
   ```
   /shyscoreboard add lobby_scoreboard
   ```

3. **Remove when needed:**
   ```
   /shyscoreboard remove lobby_scoreboard
   ```

**💡 Pro Tip:** Add these commands to your world management or minigame plugins for automatic region-based display.

### For WORLDGUARD Scoreboards

1. **Grant the permission:**
   ```
   shyscoreboard.scoreboard.lobby_scoreboard
   ```

2. **Set the region flag:**
   ```
   /region flag spawn shyscoreboard lobby_scoreboard
   ```

3. **Players will see the scoreboard when entering the region!**

---

## 🎨 Customization Tips

### Color Codes
* Use `&` for traditional color codes (`&a` = green, `&c` = red)
* Use `&#RRGGBB` for hex colors (1.16+)
* Mix colors within lines for creative effects

### PlaceholderAPI Integration
* Install PlaceholderAPI for dynamic content
* Use placeholders like `%player_name%`, `%server_online%`
* Browse available placeholders with `/papi list`

### Line Length Optimization
* Maximum 32 characters per line (including color codes)
* Color codes count toward the limit
* Test your lines in-game to ensure they display correctly

### Performance Tuning
* Higher `refreshTicks` = better performance
* Lower `refreshTicks` = more responsive updates
* Start with 60 ticks (3 seconds) and adjust as needed

---

## ❓ Common Issues

**Q: My scoreboard isn't showing**
* Check that the player has the required permission
* Verify the scoreboard type matches your setup method
* Ensure you ran `/shyscoreboard reload` after changes

**Q: Colors aren't working**
* Make sure you're using `&` for color codes
* Check that your line length doesn't exceed 32 characters
* Verify PlaceholderAPI is installed for placeholder support

**Q: Multiple scoreboards are conflicting**
* Check the `priority` values (lower numbers = higher priority)
* Ensure different scoreboards have different names
* Use `/shyscoreboard update` to refresh player scoreboards
