# Objectives

## Understand application architectures

1. Presentation logic: The user interface. Controls the application.
2. Application / business logic: Defines what the application does.
3. Data access logic: Defines how the application manages it data.
4. Data storage: Where the data is kept.

### server-based
- client: sends keystrokes to server, displays text according to server's instructions
- server: 1,2,3,4
- problems: server can become a bottleneck, upgrade expensive and "lumpy".

### client-based
- client: 1,2,3
- server: 4
- problems: All data must travel back and forth between server and client

### client-server
- client: 1,2
- server: 3,4
- Advantage: balance the processing load between client and server

### thin-client architecture
- client: 1
- server: 2,3,4
- Advantage: Only one server needs updating

### multi-tier architecture
- client: 1
- server1: 2
- server2: 3,4

### peer-to-peer architecture
- client: 1,2,3,4
- server: 1,2,3,4
- use local logical to access data stored on another computer

## Understand how the Web works
- GET: retrieve specificed URL from server
- HEAD: retrieve only header for specified URL
- POST: add data specified in request body to specified URL

## Understand how e-mail works
- SMTP(Simple Mail Transfer Protocol)

        Handles transfer of text messages between email client
        and mail server, and between mail servers.

- POP(Post Office Protocol)

        Messages are downloaded onto client and deleted from server.

- IMAP
    - Messages remain on server
    - Multiple clients can be connected simulaneously to same mailbox

## Be aware of how FTP, Telnet/SSH and instant messaging works
- SSH: Secure Shell
