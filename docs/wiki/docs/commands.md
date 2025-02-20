# Commands

The following commands are available in ShyScoreboard. You can access them by typing:

```
/shyscoreboard help 1
```

### /shyscoreboard add

```
/shyscoreboard add <scoreboard> [player]
```

Adds a new scoreboard with type COMMAND to the given player. If a scoreboard has got type GLOBAl, it does nothing. If multiple scoreboards have been added to a player, 
the one with the highest priority is displayed.

### /shyscoreboard remove

```
/shyscoreboard remove <scoreboard> [player]
```

Removes a scoreboard with type COMMAND from the given player. If a scoreboard has got type GLOBAl, it does nothing.

### /shyscoreboard update

```
/shyscoreboard update [player]
```

Refreshes the scoreboard of the current player. If another scoreboard of another plugin has overwritten this scoreboard, then this fixes the state of the scoreboard of a player.

### /shyscoreboard reload

```
/shyscoreboard reload
```

Reloads all configurations and scoreboards.
