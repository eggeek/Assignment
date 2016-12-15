# Data Link Layer

## Understand the role of the data link layer
Manages the physical connection etween devices on the same circuit

- Media Access Control
    - Encode/decode between physical layer symbols and frames
    - Error detection
    - Controls when the device transmits
        1. Roll Call Polling
        2. Token Passing

- Logical Link Control
    - Handle PDU header
    - Error control
    - Defines interface with the network layer


- Error Control: make sure that data arrives correctly

- Data Link Protocols
    - Asynchronous transmission:

            each character sent independently
            used for dumb terminals

    - Synchronous Transmission

            several bytes sent together in a frame

- Transmission Efficiency: Message length vs error rate

## Become familiar with two basic approaches to controlling access to the media
- MAC address
    - Used in Ethernet and Wifi
    - In same LAN

## Become familiar with common sources of errors and their prevention
- CRC

        def crc(s, g):
            s = s.lstrip('0')
            if len(s) < len(g):
                return s
            s = list(s)
            for i in range(len(g)):
                s[i] = str(int(s[i]) ^ int(g[i]))
            s = ''.join(s).lstrip('0')
            return crc(s, g)


## Understand common error detection and correction methods
## Become familiar with several commonly used data link protocols
    - Ethernet
