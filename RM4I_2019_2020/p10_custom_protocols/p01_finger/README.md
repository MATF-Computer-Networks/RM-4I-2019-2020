To demonstrate a complete protocol handler, let’s write 
one for the `finger` protocol defined in RFC 1288. 
Finger is a relatively simple protocol compared to 
JDK-supported protocols such as HTTP and FTP. The client 
connects to port 79 on the server and sends a list of 
usernames followed by a CR/LF pair. The server responds 
with ASCII text containing informationabout each of the 
named users or, if no names are listed, a list of the 
currently logged in users. For example:
```
%nc localhost 79
Login       Name               TTY      Idle    When        Where
jacola      Jane Colaginae    *pts/7            Tue 08:01   208.34.37.104
marcus      Marcus Tullius     pts/15  13d      Tue 17:33   farm-dialup11.poly.e
matewan     Sepin Matewan     *pts/17  17:      Thu 15:32   128.238.10.177
hengpi      Heng Pin          *pts/10           Tue 10:36   128.238.18.119
```
Or to request information about a specific user:
```
%nc localhost 79
marcus
Login       Name                TTY     Idle    When        Where
marcus      Marcus Tullius     pts/15   13d     Tue 17:33   farm-dialup11.poly.e
```

Since there’s no standard for the format of a finger URL, we 
will start by creating one. Ideally, this should look as much 
like an HTTP URL as possible. Therefore, we will implement a 
finger URL like this: 
```
finger://hostname:port/usernames
```