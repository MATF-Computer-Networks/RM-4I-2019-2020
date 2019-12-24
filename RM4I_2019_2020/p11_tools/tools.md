# Miscellaneous useful programs

### ssh

```
ssh (SSH client) is a program for logging into a remote machine and for executing 
commands on a remote machine. It is intended to provide secure encrypted communications 
between two untrusted hosts over an insecure network.
```

Examples:
```console
user@pc:~# ssh mi15123@alas.matf.bg.ac.rs
mi15123@alas.matf.bg.ac.rs's password: 
Last login: Mon May 20 10:59:15 2019 from 147.91.36.2
Linux 3.10.17.
mi15123@alas:~$ ls
mail/  public_html/
mi15123@alas:~$ echo "test test test test test" >> 1.txt
mi15123@alas:~$ ls
1.txt  mail/  public_html/
mi15123@alas:~$ cat 1.txt
test test test test test
mi15123@alas:~$ mkdir aaa
mi15123@alas:~$ ls
1.txt  aaa/  mail/  public_html/
mi15123@alas:~$ exit
logout
Connection to alas.matf.bg.ac.rs closed.
```

### sftp

```
sftp is an interactive file transfer program, similar to ftp, which performs all 
operations over an encrypted ssh(1) transport. It may also use many features of ssh, 
such as public key authentication and compression. sftp connects and logs into the 
specified host, then enters an interactive command mode.
```

Examples:
```console
user@pc:~# ls
Desktop  Documents  Downloads  Music  Pictures  Public  Templates  Videos
user@pc:~# sftp mi15123@alas.matf.bg.ac.rs
mi15123@alas.matf.bg.ac.rs's password: 
Connected to alas.matf.bg.ac.rs.
sftp> ls
1.txt         aaa           mail          public_html   
sftp> get 1.txt
Fetching /home/mi15123/1.txt to 1.txt
/home/mi15123/1.txt                                    100%   25     5.5KB/s   00:00    
sftp> !ls
1.txt  Desktop	Documents  Downloads  Music  Pictures  Public  Templates  Videos
sftp> !cat 1.txt
test test test test test
sftp> !echo "test2 test2" >> 1.txt
sftp> put 1.txt 
Uploading 1.txt to /home/mi15123/1.txt
1.txt                                                  100%   37    11.2KB/s   00:00    
sftp> bye
user@pc:~# 
```

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
Displays only IPv4 socket connections

ss -tl4
Displays only IPv6 socket connections

ss -t4 state established
Displays all IPv4 TCP sockets that are in "connected" state.

ss -o state established '( dport = :ssh or sport = :ssh )'
Display all established ssh connections.
```

### tcpdump

```
Tcpdump prints out a description of the contents of packets on a network interface 
that match the boolean expression; the description is preceded by a time stamp, 
printed, by default, as hours, minutes, seconds, and fractions of a second since 
midnight. It can also be run with the -w flag, which causes it to save the packet 
data to a file for later analysis, and/or with the -r flag, which causes it to read 
from a saved packet file rather than to read packets from a network interface. 
It can also be run with the -V flag, which causes it to read a list of saved packet 
files. In all cases, only packets that match expression will be processed by tcpdump.
```

### ping

```
ping uses the ICMP protocol's mandatory ECHO_REQUEST datagram to elicit an ICMP 
ECHO_RESPONSE from a host or gateway. ECHO_REQUEST datagrams ("pings") have an 
IP and ICMP header, followed by a struct timeval and then an arbitrary number of 
"pad" bytes used to fill out the packet. ping works with both IPv4 and IPv6. 
Using only one of them explicitly can be enforced by specifying -4 or -6.
```

Examples:
```console
user@pc:~/Desktop# ping google.com
PING google.com (216.58.201.110) 56(84) bytes of data.
64 bytes from prg03s02-in-f110.1e100.net (216.58.201.110): icmp_seq=2 ttl=128 time=29.8 ms
64 bytes from prg03s02-in-f110.1e100.net (216.58.201.110): icmp_seq=3 ttl=128 time=26.7 ms
64 bytes from prg03s02-in-f110.1e100.net (216.58.201.110): icmp_seq=4 ttl=128 time=29.8 ms
64 bytes from prg03s02-in-f110.1e100.net (216.58.201.110): icmp_seq=5 ttl=128 time=27.0 ms
64 bytes from prg03s02-in-f110.1e100.net (216.58.201.110): icmp_seq=6 ttl=128 time=32.0 ms
64 bytes from prg03s02-in-f110.1e100.net (216.58.201.110): icmp_seq=7 ttl=128 time=30.3 ms
^C
--- google.com ping statistics ---
7 packets transmitted, 6 received, 14% packet loss, time 6015ms
rtt min/avg/max/mdev = 26.726/29.324/32.058/1.867 ms
```

### traceroute / tracepath

```
traceroute will show the route of a packet. It attempts to list the series of hosts 
through which your packets travel on their way to a given destination. Also have a 
look at xtraceroute (one of several graphical equivalents of this program). 
traceroute is a network troubleshooting utility which shows number of hops taken to 
reach destination also determine packets traveling path. Below we are tracing route 
to global DNS server IP Address and able to reach destination also shows path of 
that packet is traveling.
```

Examples:
```console
user@pc:~# traceroute -I google.com
traceroute to google.com (216.58.198.78), 128 hops max, 72 byte packets
1  52.93.7.1 (52.93.7.1)  6.361 ms  6.229 ms  6.106 ms
2  72.14.215.85 (72.14.215.85)  5.939 ms  5.460 ms  5.914 ms
3  209.85.252.198 (209.85.252.198)  6.012 ms  5.694 ms  5.761 ms
4  64.233.174.27 (64.233.174.27)  5.079 ms  4.776 ms  4.662 ms
5  dub08s02-in-f78.1e100.net (216.58.198.78)  6.650 ms  5.509 ms  5.596 ms
```

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

### dig

```
dig is a flexible tool for interrogating DNS name servers. It performs 
DNS lookups and displays the answers that are returned from the name server(s) that 
were queried. Most DNS administrators use dig to troubleshoot DNS problems because 
of its flexibility, ease of use and clarity of output. Other lookup tools tend to 
have less functionality than dig.
```

Examples:
```console
user@pc:~# dig amazon.com

; <<>> DiG 9.8.3-P1 <<>> amazon.com
;; global options: +cmd
;; Got answer:
;; ->>HEADER<<- opcode: QUERY, status: NOERROR, id: 30832
;; flags: qr rd ra; QUERY: 1, ANSWER: 6, AUTHORITY: 0, ADDITIONAL: 0

;; QUESTION SECTION:
;amazon.com.			IN	A

;; ANSWER SECTION:
amazon.com.		17	IN	A	54.239.25.200
amazon.com.		17	IN	A	54.239.25.208
amazon.com.		17	IN	A	54.239.26.128
amazon.com.		17	IN	A	54.239.17.6
amazon.com.		17	IN	A	54.239.17.7
amazon.com.		17	IN	A	54.239.25.192

;; Query time: 3 msec
;; SERVER: 10.78.97.142#53(10.78.97.142)
;; WHEN: Tue May 14 13:56:22 2019
;; MSG SIZE  rcvd: 124
```

### nslookup

```
nslookup is a program to query Internet domain name servers. nslookup has two modes: 
interactive and non-interactive. Interactive mode allows the user to query name 
servers for information about various hosts and domains or to print a list of hosts
in a domain. Non-interactive mode is used to print just the name and requested 
information for a host or domain.
```

Examples:
```console
user@pc:~# nslookup amazon.com
Server:		10.78.97.142
Address:	10.78.97.142#53

Non-authoritative answer:
Name:	amazon.com
Address: 54.239.25.192
Name:	amazon.com
Address: 54.239.25.200
Name:	amazon.com
Address: 54.239.25.208
Name:	amazon.com
Address: 54.239.26.128
Name:	amazon.com
Address: 54.239.17.6
Name:	amazon.com
Address: 54.239.17.7
```

### host

```
host is a simple utility for performing DNS lookups. It is normally used to convert 
names to IP addresses and vice versa.
```

Examples:
```console
user@pc:~# host amazon.com
amazon.com has address 54.239.17.6
amazon.com has address 54.239.17.7
amazon.com has address 54.239.25.192
amazon.com has address 54.239.25.200
amazon.com has address 54.239.25.208
amazon.com has address 54.239.26.128
amazon.com mail is handled by 10 amazon-smtp.amazon.com.
```

### hostname

```
hostname is used to display the system's DNS name, and to  display or set its 
hostname or NIS domain name.
```

### arp

```
ARP (Address Resolution Protocol) is useful to view / add the contents of the 
kernel's ARP tables. To see default table use the command as.
```

## Configuration

### ip

```
Show / Manipulate routing, network devices, interfaces and tunnels.
```

```console
ip addr
Shows addresses assigned to all network interfaces.

ip neigh
Shows the current neighbour table in kernel.

ip link set x up
Bring up interface x.

ip link set x down
Bring down interface x.

ip route
Show table routes.
```

### ifconfig

```
ifconfig is used to configure the kernel-resident network interfaces. It is 
used at boot time to set up interfaces as necessary. After that, it is usually 
only needed when debugging or when system tuning is needed.

In addition to activating and deactivating interfaces with the "up" and "down" 
settings, this command is necessary for setting an interface's address information 
if you don't have the ifcfg script.
  - ifup   - Use ifup device-name to bring an interface up by following a script 
             (which will contain your default networking settings). Simply type 
             ifup and you will get help on using the script.
  - ifdown - Use ifdown device-name to bring an interface down using a script 
             (which will contain your default network settings). Simply type ifdown 
             and you will get help on using the script.
  - ifcfg  - Use ifcfg to configure a particular interface. Simply type ifcfg 
             to get help on using this script.
```

### route

```
route manipulates the kernel's IP routing tables. It's primary use is to set up 
static routes to specific hosts or networks via an interface after it has been 
configured with the ifconfig program.
```

### iwconfig

```
iwconfig is similar to ifconfig(8), but is dedicated to the wireless interfaces. 
It is used to set the parameters of the network interface which are specific to 
the wireless operation (for example : the fre‐ quency). Iwconfig may also be used 
to display those parameters, and the wireless statistics (extracted from 
/proc/net/wireless).
```

## Is there anything else?

If you are reading this, thank you for holding on till the end. Have a little 
ASCII movie to brighten your day:
```console
user@pc:~# telnet towel.blinkenlights.nl
```