# checkers-game

# information about spefic files and overall game

Within the folder server code, the following files carry out the following tasks:
- Server mainn handles incoming connections
- server thread manages client-specific communication


# to do list

# checkers game
- chance piece to queen once it has gotten to the end of the board
- implement player hit detection
- actual implement the player movement method
-develop queen movement system
# Server code
- develop actual server
- ensure server can accept multiple clients
- develop login system
- develop database system

# in more general sense to do list

-develop checker system
-develop server communication
-develop the AI used in this game



# to run this project use the following commands

This is for the game portion
mvn exec:java -Dexec.mainClass="game_code.mainGame"

This is for server
mvn exec:java -Dexec.mainClass="server_code.ServerMain" -Dexec.args=9000

For the client connection part
mvn exec:java -Dexec.mainClass="server_code.ClientConnection"

key for requests
- USERRESPONSEREQ - used for sending messages to a user
- USERRESPONSE - what user sends to server
- USERWELCOMEMSG - server sends welcome message to user
- CLOSE - used for closing connections between server and client
- AUTHREQ - used to inform user that authentication is required
- AUTHCHECK - send by user to check that username and password are correct
- AUTHSUCCESS - successful login send by server
- AUTHNOTSUCCESS - not successful. Note: reason is provided
- ERROR - a error has happened and is reported back to the user
- SECRETE - used to notify client to not display 
- GAMESESSION - used to notify client to go itno gaming mode
- WELCOMEMSG - notifies client about incoming welcome message