
Working
========
* Multiple simultaneous game sessions
* GUI interactions

Bugged/Broken
==============
* Starting a second (or more) game as the same client 
  - Correctly takes you to client list scene, but challenging another 
    client doesn't change their scene to the game scene, only the challenger's GUI updates
    
  - Scores are bugged when a second game is attempted
  
  - Most likely caused by Client.java (of client project) where 
    the client is permanently in the listen() function even after 
    a game ends because clients can only be challenged in the ready() function.
    
* Clients can challenge any other client, even if they are already in a game
  
