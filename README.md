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

   If RMI connections don't work add the following parameter
   ```
   -Djava.rmi.server.hostname=<server ip>
   ```
   ```
   java -Djava.rmi.server.hostname=172.20.10.3 -jar IS24-AM05.jar server
   ```
4. Execute the .jar file with the following command to start the client:
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


## NOTA
Codex Naturalis è un gioco da tavolo sviluppato ed edito da Cranio Creations Srl. I contenuti grafici di questo progetto riconducibili al prodotto editoriale da tavolo sono utilizzati previa approvazione di Cranio Creations Srl a solo scopo didattico. È vietata la distribuzione, la copia o la riproduzione dei contenuti e immagini in qualsiasi forma al di fuori del progetto, così come la redistribuzione e la pubblicazione dei contenuti e immagini a fini diversi da quello sopracitato. È inoltre vietato l'utilizzo commerciale di suddetti contenuti.
