# Permissions Guide

This guide explains all permission nodes available in ShyScoreboard and how to properly configure them for your server. Understanding permissions is crucial for controlling who can see which scoreboards and who can manage them.

## 🔐 Permission Levels

ShyScoreboard uses two permission levels:

* **👤 User Level**: Permissions that regular players can have
* **🛡️ Admin Level**: Permissions that should only be given to trusted staff

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

