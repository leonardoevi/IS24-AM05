## How to play:

1. Download the .jar file
2. Execute the .jar file with the following command to start the server:
     java -jar <path to the .jar file> server
     or
     java -jar <path to the .jar file> server <path to a save file>
     to load a game from a save file
3. Execute the .jar file with the following command to start the client:
     client <rmi/socket> <server ip> <server port> <TUI/GUI>


## Here the list of commands to play the game from the TUI:

Choose your nickname with:
```
  1 <Nickname>
```
```
  1 Leo
```
  
Log in the server with the selected nickname with:
```
  2
```
  
Create a new game and join it with:
```
  3 <Number of players in the new game>
```
```
  3 2
```

Join an existing game with:
```
  4
```

Place your starter card with:
```
  5 <front/back>
```
```
  5 back
```

Choose your objective with:
```
  6 <Objective name>
```
```
  6 O_098
```

Place a card with
```
  7 <Card number> <front/back> <placing spot coord 1> <placing spot coord 2>
```
```
  7 37 back 1 -1
```

Draw a card from the top of a deck with:
```
  8 <resource/gold>
```
```
  8 gold
```

Draw a visible card with:
```
  9 <Card number>
```
```
  9 02
```

Leave the server (usually to change nickname when reconnecting to an already started game) with:
```
  10
```

Quit with:
```
  11
```

Send a message to all other players with:
```
  12 <Text message>
```
```
  12 Hello everyone!
```

Send a message to a specific player with:
```
  13 <Player nickname> <Text message>
```
```
  13 Leo Hi, my name is Andre!
```
