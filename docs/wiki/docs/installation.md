# How to create a new scoreboard

## Understanding scoreboard types

ShyScoreboard comes with 2 types of scoreboards ``GLOBAL`` and ``COMMAND``. Both types can be set in the file of the invidiual scoreboard.

#### GLOBAL

A global scoreboard is always visible as long as a player has got the permission to that scoreboard. For example, if you have got a scoreboard 
named ``sample_scoreboard`` it will always be displayed if a player has got the permission ``shyscoreboard.scoreboard.sample_scoreboard``.

Global scoreboards should be used if you **do not have** ``OP PLAYERS`` and have got correctly setup roles with a permission plugin like ``LuckPerms``. 
e.g. you have got admin roles, user roles, etc. Now, if a player enters minigames or enters regions on your server, then he should be added to different permissions roles like ``blockball`` or ``skyblock`` etc. 
Global scoreboards automatically detect permission changes and display the correct scoreboard.

#### COMMAND

A command scoreboard is only visible after being added via the command ``/shyscoreboard add <scoreboard> [player]`` to a player. This ensures a scoreboard is not always visible, which works
best for servers having ``OP Players``. Add this command to worlds or regions on your server managed by your world or region plugins. This allows to 
display different scoreboards when a player enters different worlds, regions or minigames.

## Creating a scoreboard

### Managing files

1. Open the ``/plugins/ShyScoreboard/scoreboard`` folder.
2. Open the ``sample_scoreboard.yml`` file.
3. Set type from ``GLOBAL`` to ``COMMAND``. This disables the sample scoreboard.
4. Copy the ``sample_scoreboard.yml`` to a new file in the same folder and name it ``my_scoreboard.yml``.
5. Open ``my_scoreboard.yml`` and set the name to ``my_scoreboard.``
6. Decide if you want to go with a ``GLOBAL`` or ``COMMAND`` scoreboard and set the type.
7. Set the title to your wanted title. The title is limited by 16 characters.
8. Add or remove lines as you want. Lines can only be 32 character long. Chat Colors and HTML color codes count as well.

### InGame

* Execute the ``/shyscoreboard reload`` command.

#### GLOBAL

* If you have selected ``GLOBAL``, the scoreboard will be visible after you have obtained the ``shyscoreboard.scoreboard.my_scoreboard`` permission.

#### COMMAND

* If you have selected ``COMMAND``, the scoreboard will be visible after you have obtained the ``shyscoreboard.scoreboard.my_scoreboard`` permission and executed the command ``/shyscoreboard add <scoreboard>``. 
* You need to add the ``/shyscoreboard add <scoreboard>`` command and the ``/shyscoreboard remove <scoreboard>`` command to all worlds and regions managed by your world and region plugins to display the correct scoreboard in the correct region.
