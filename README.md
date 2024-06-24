## How to play:

1. Download the .jar file
2. Execute the .jar file with the following command to start the server:
   ```
     java -jar <path to the .jar file> server
   ```
     or
   ```
     java -jar <path to the .jar file> server <path to a save file>
   ```
     to load a game from a save file
3. Execute the .jar file with the following command to start the client:
   ```
     client <rmi/socket> <server ip> <server port> <TUI/GUI>
   ```
   ```
     client socket localhost 1234 TUI
   ```


## Here the list of commands to play the game from the TUI:

Choose your nickname:
```
  1 <Nickname>
```
```
  1 Leo
```
  
Log in the server with the selected nickname:
```
  2
```
  
Create a new game and join it:
```
  3 <Number of players in the new game>
```
```
  3 2
```

Join an existing game:
```
  4
```

Place your starter card:
```
  5 <front/back>
```
```
  5 back
```

Choose your objective:
```
  6 <Objective name>
```
```
  6 O_098
```

Place a card:
```
  7 <Card number> <front/back> <placing spot coord 1> <placing spot coord 2>
```
```
  7 37 back 1 -1
```

Draw a card from the top of a deck:
```
  8 <resource/gold>
```
```
  8 gold
```

Draw a visible card:
```
  9 <Card number>
```
```
  9 02
```

Leave the server (usually to change nickname when reconnecting to an already started game):
```
  10
```

Quit:
```
  11
```

Send a message to all other players:
```
  12 <Text message>
```
```
  12 Hello everyone!
```

Send a message to a specific player:
```
  13 <Player nickname> <Text message>
```
```
  13 Leo Hi, my name is Andre!
```
