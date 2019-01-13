# jLock
A java program protector meant to prevent data leaks and cracking. Also my project for DVHacks 2019.

### How it was supposed to work
A web server is used for authentication and a database stores what apps each user has access to.
From the client, a user authenticates with the server and a list of available applications is available.
If the user selects an app to run, the encrypted data is downloaded and decrypted, all in memeory.
After the decrypted jar is in memory it would have been run (of course heavily obfuscated with junk code as well).

### How it turned out
There is no web server so instead the encrypted files must be downloaded and stored on disk.
The app uses a username, password, and hwid to generate a private key used for either encrypting or decrypting jar files.
A list is displayed of all .locked files (the encrypted file extension)
Pressing launch will then decrypt the file onto the disk.

IMPORTANT: The encryption doesn't work and the (stolen) obfuscator is very spotty.

### Some extra things I might do
 - Add fancy icons
 - Convert to material design for javafx
 - Write a more secure client in c++???

### Will this ever be finished?
Hopefully. I am fond of the user interface I have made and confident it could become a very usefull project.

## Screenshots
![Login screen](https://raw.githubusercontent.com/digitaldisarray/jLock/master/Screenshots/Login1.png)
![Login screen filled out](https://raw.githubusercontent.com/digitaldisarray/jLock/master/Screenshots/Login2.png)
![Dashboard](https://raw.githubusercontent.com/digitaldisarray/jLock/master/Screenshots/Dashboard1.png)
![Dashboard selected item](https://raw.githubusercontent.com/digitaldisarray/jLock/master/Screenshots/Dashboard2.png)
![Builder](https://raw.githubusercontent.com/digitaldisarray/jLock/master/Screenshots/Builder.png)
