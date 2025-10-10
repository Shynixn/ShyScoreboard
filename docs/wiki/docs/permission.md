# Permissions Guide

This guide explains all permission nodes available in ShyScoreboard and how to properly configure them for your server. Understanding permissions is crucial for controlling who can see which scoreboards and who can manage them.

## 🔐 Permission Levels

ShyScoreboard uses two permission levels:

- **👤 User Level**: Permissions that regular players can have
- **🛡️ Admin Level**: Permissions that should only be given to trusted staff

---

## 📋 Complete Permission Reference

| Permission | Level | Description | Required For |
|------------|-------|-------------|--------------|
| `shyscoreboard.scoreboard.*` | 👤 User | Access to **all** scoreboards | Seeing any scoreboard |
| `shyscoreboard.scoreboard.<name>` | 👤 User | Access to a **specific** scoreboard | Seeing named scoreboard |
| `shyscoreboard.command` | 🛡️ Admin | Use the base `/shyscoreboard` command | Running any command |
| `shyscoreboard.reload` | 🛡️ Admin | Reload plugin configurations | `/shyscoreboard reload` |
| `shyscoreboard.add` | 🛡️ Admin | Add scoreboards to players | `/shyscoreboard add` |
| `shyscoreboard.set` | 🛡️ Admin | Set specific scoreboard for players | `/shyscoreboard set` |
| `shyscoreboard.remove` | 🛡️ Admin | Remove scoreboards from players | `/shyscoreboard remove` |
| `shyscoreboard.update` | 🛡️ Admin | Refresh player scoreboards | `/shyscoreboard update` |

---

## 🎯 Understanding Scoreboard Permissions

### Wildcard Permission: `shyscoreboard.scoreboard.*`
**Use case:** Give players access to ALL scoreboards

```yaml
# LuckPerms example - grants access to every scoreboard
/lp group default permission set shyscoreboard.scoreboard.* true
```

**⚠️ Important:** This permission allows access to **every** scoreboard on your server. Use with caution!

### Specific Scoreboard Permission: `shyscoreboard.scoreboard.<name>`
**Use case:** Give players access to specific scoreboards only

```yaml
# Examples for different scoreboard names
shyscoreboard.scoreboard.lobby_board      # Access to lobby_board scoreboard
shyscoreboard.scoreboard.vip_lounge       # Access to vip_lounge scoreboard  
shyscoreboard.scoreboard.pvp_arena        # Access to pvp_arena scoreboard
shyscoreboard.scoreboard.admin_panel      # Access to admin_panel scoreboard
```

**💡 Pro Tip:** The `<name>` must exactly match the `name:` field in your scoreboard YAML file.
