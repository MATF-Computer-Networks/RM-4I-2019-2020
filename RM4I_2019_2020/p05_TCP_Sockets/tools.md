### netstat / ss

`netstat` is mostly obsolete. Replacement for netstat is ss. 
Replacement for `netstat -r` is `ip route`. 
Replacement for  `netstat -i` is `ip -s link`. 
Replacement for `netstat -g` is `ip maddr`.

```
ss is used to dump socket statistics. It allows showing information similar to netstat. 
It can display more TCP and state informations than other tools. When no option is used 
ss displays a list of open non-listening sockets (e.g. TCP/UNIX/UDP) that have 
established connection.
```

Examples:
```
ss -ta
Display all TCP sockets.

ss -ua
Display all UDP sockets.

ss -ltn
Show only listening TCP sockets.

ss -lun
Show only listening UDP sockets.

ss -ltunp
Show all listening sockets, as well as their name and PID.

ss -s
Print statistics.

ss -tl4
Displays only IPv4 socket connections.

ss -tl6
Displays only IPv6 socket connections.

ss -t4 state established
Displays all IPv4 TCP sockets that are in "connected" state.

ss -o state established '( dport = :ssh or sport = :ssh )'
Display all established ssh connections.


### nmap

```
nmap ("Network Mapper") is an open source tool for network exploration and security 
auditing. It was designed to rapidly scan large networks, although it works fine 
against single hosts. nmap uses raw IP packets in novel ways to determine what hosts 
are available on the network, what services (application name and version) those 
hosts are offering, what operating systems (and OS versions) they are running, what 
type of packet filters/firewalls are in use, and dozens of other characteristics. 
While nmap is commonly used for security audits, many systems and network administrators 
find it useful for routine tasks such as network inventory, managing service upgrade 
schedules, and monitoring host or service uptime.
```

Examples:
```console
user@pc:~# nmap -sP 172.16.0.0/24
Starting Nmap 7.70 ( https://nmap.org ) at 2019-04-15 16:19 EDT
Nmap scan report for 172.16.0.0
Host is up (0.00028s latency).
Nmap scan report for 172.16.0.1
Host is up (0.0045s latency).
Nmap scan report for 172.16.0.2
...
Nmap scan report for 172.16.0.254
Host is up (0.00019s latency).
Nmap scan report for 172.16.0.255
Host is up (0.00039s latency).
Nmap done: 256 IP addresses (256 hosts up) scanned in 4.33 seconds

user@pc:~# nmap 172.16.0.0/24
Starting Nmap 7.70 ( http://nmap.org ) at 2019-04-15 16:20 EDT
Nmap scan report for 172.16.0.1
Host is up (0.0043s latency).
Not shown: 998 closed ports
PORT STATE SERVICE
22/tcp open ssh
80/tcp open http
443/tcp open https

user@pc:~# nmap -O 172.16.0.15
Starting Nmap 7.70 ( http://nmap.org ) at 2019-04-15 16:21 EDT
Nmap scan report for 172.16.0.15
Host is up (0.00032s latency).
Not shown: 996 closed ports
PORT STATE SERVICE
88/tcp open kerberos-sec
139/tcp open netbios-ssn
445/tcp open microsoft-ds
631/tcp open ipp
MAC Address: 00:00:00:00:00:00 (Unknown)
Device type: general purpose
Running: Apple Mac OS X 10.5.X
OS details: Apple Mac OS X 10.5 - 10.6 (Leopard - Snow Leopard) (Darwin 9.0.0b5 - 10.0.0)
Network Distance: 1 hop
```
