# Network & Transport Layer

## TCP/IP protocols
### TCP
- Connection-oriented
- Reliable
    - Error detect & correct
    - Segments are re-assembled in correct order
### UDP
- Connectionless
- No virtual circuit
- No acknowledgement
- Small header
- Use cases
    - Applications send small messages
    - loss of segments is acceptable

## Transport layer functions
## Addressing
### One address per layer
- Application layer: URL
- Transport Layer(TCP): port number, identifies the application that handles a message
- Network Layer(IP): IP address, used for identifying devices across networks
- Data Link Layer (Ethernet): MAC address, used for sending frames in a LAN

### Where to get
- DNS entries:
    - ICANN/Registrars manage top-level and second-level domains
    - Network admins manage DNS for their assigned domains
- Port numbers
    - IANA maintains official list of port numbers
- IP addresses
    - IANA and 5 RIRs allocate blocks of addresses, local registries redistribute to customers
    - Network admins configure (static or dynamic) addresses in theri assigned block
- MAC address: allocated by hardware manufacturers.
