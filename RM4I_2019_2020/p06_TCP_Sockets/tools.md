### netcat / nc

```
nc (or netcat) utility is used for just about anything involving TCP, UDP or
UNIX-domain sockets. It can open TCP connections, send UDP packets, listen on 
arbitrary TCP and UDP ports, do port scanning, and deal with both IPv4 and IPv6. 
Unlike telnet(1), nc scripts nicely, and separates error messages onto stderr 
instead of sending them to stdout, as telnet(1) does with some.
     
Common uses include:
- simple TCP proxies
- shell-script based HTTP clients and servers
- network daemon testing
- a SOCKS or HTTP ProxyCommand for ssh(1)
- and much, much more
```

Examples:
```
- Open a TCP connection to port 42 of host.example.com, using port 31337
  as the source port, with a timeout of 5 seconds:

  $ nc -p 31337 -w 5 host.example.com 42

- Open a UDP connection to port 53 of host.example.com:

  $ nc -u host.example.com 53

- Open a TCP connection to port 42 of host.example.com using 10.1.2.3 as 
  the IP for the local end of the connection:

  $ nc -s 10.1.2.3 host.example.com 42

- Create and listen on a UNIX-domain stream socket:

  $ nc -lU /var/tmp/dsocket

- Port scanning (either by range or list of ports):
  $ nc -zv 123.123.123.123 20-100
  $ echo "QUIT" | nc -zv 123.123.123.123 20-100
```
