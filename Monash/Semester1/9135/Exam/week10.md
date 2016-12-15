# Network Security

> Note that in addition to the prescribed reading, the following topics are relevant for the exam: types of attacks (e.g. DoS, worms), and the countermeasures discussed in the lecture (firewalls, encryption).

## Question
打勾的为已在sample exam中出现的, 加粗的为比较重要的

- 3 Goals
> - [x]Q1. Explain the three primary goals in network security.
     

- Authentication
> - [ ] Q2. Explain the purpose of Authentication and give example.

- Access Control
> - [ ] Q3. Explain the purpose of Access Control and give example.

- Malicious software
> - [ ] Q4. Explain the "Viruses" and "Worms".

- **DoS(Denial of Service) and DDoS(Distributed Denial of Service)**
> - [x] Q5. Explain TCP SYN Flood.
> - [ ] Q6. Explain ICMP Flood.
> - [ ] Q7. Dos and DDoS bad influence.

- Firewall
> - [ ] Q8. Explain Packet-level firewall, and give a example
> - [ ] Q9. Explain Application-level firewall, and give a example
> - [ ] Q10. Compare Packet-level firewall and Application-level 

- Encryption
> - [ ] Q11. Why use HTTPS ?
> - [ ] Q12. **How to share secret?**

## Answer

- Q1
    - Availability:
    prevent attacks like DDoS
        
    - Confidentiality: 
    protect private information
            
    - Integrity:
    make sure data can not be changed unexpected
      
- Q2
    Ensure that:

    - the communicating entities are who they claim to be
    - an entity cannot impersonate another entity

    Example:

        ATM PIN, Monash Authcate, fingerprint lock, ...

- Q3

    Access Control Relies on Authentication
    Ensure that:

    - only authorised entities can access systems and applications
    - resource limits are enforced
    
    Example:
    
        Swipe cards, Bank account, ...

- Q4

    Viruses:
    
    - spread when infected files are accessed
    - require human interaction to spread (e.g. opening a
file or program)

    Worms:
    
    - special type of virus that spreads without human
intervention
    - typically uses the network to copy itself from
computer to computer

- Q5
    
    TCP SYN Flood
    sends lots of tcp SYNs,but client never sends ACK,and cause:
        
    1. TCP/IP stack needs to allocate data structures for every connection
    2. older TCP/IP stacks may run out of memory - server
crashes
    3. newer TCP/IP stacks simply won’t accept new connections
            
- Q6
    ICMP Flood
    Use the LAN to amplify your attack,
    sends broadcast ping requests with fake source:
     
    1. you need control of a computer in a large LAN
    2. send a broadcast ping to all computers in the LAN
    3. **fake** your source IP to be the IP of the attack target
    4. the target gets a ping reply from **every computer in the LAN**

- Q7

    DoS and DDoS will flood a server with messages:
    
    1. server may crash under the load
    2. or network capacity to server is overloaded so that legitimate users can't reach the server

- Q8
    Examine **headers** of every packet passing through:
    
    - defines rules to determine which packets are
acceptable
    - can make decisions based on source or destination
IP or port addresses
    - example: iptables
    
    Access Control List (ACL)
    
    - a set of rules for a packet-level firewall
    - can be used to permit or deny packets into a network

- Q9
    Examines **application-layer** packet contents
    
    - can scan for known attacks on application-layer
server software
    - example: scan for viruses in email sent via SMTP

- Q10
    1. Application-level is more computationally expensive
    2. Application-level is more difficult to set up
    3. Application-level is more resource intensive

- Q11
    To prevent Man-in-the-middle attack: middle point in the network can intercept and fake packet. (See Q2)
    
- Q12
    Assume Alice is communicating with Bob, and there is a middle-man can catch packets when they are communicating:
    
    Alice has base key `C`, secret key `S1`, and public key `P1=encrypt(C1,S1)`;
    Bob has base key `C`, secret key `S2`, and public key `P2=encrypt(C2,S2)`;
    
    Alice and Bob exchange their public key;
    
    Alice has: `C`, `S1`, `P1`, `P2`, and calculate shared secret `PS=encrypt(P2,S1)`;
    Middle-man has: `P1`, `P2`, `C`;
    Bob has: `C`, `S2`, `P1`, `P2`, and calculate shared secret`PS=encrypt(P1,S2)`;
    Middle-man can not calculate `S1` or `S2` by using `P1`, `P2` and `C`, because the `encrypt` function is **Asymmetric Encryption**