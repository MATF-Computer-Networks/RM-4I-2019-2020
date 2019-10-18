### wget

```
GNU Wget is a free utility for non-interactive download of files from 
the Web. It supports HTTP , HTTPS , and FTP protocols, as well as 
retrieval through HTTP proxies.

Wget is non-interactive, meaning that it can work in the background, 
while the user is not logged on. This allows you to start a retrieval 
and disconnect from the system, letting Wget finish the work. By 
contrast, most of the Web browsers require constant user's presence, 
which can be a great hindrance when transferring a lot of data. 
```

Examples
```
wget http://www.matf.bg.ac.rs/images/matf.gif
Downloads file to current location with same name as in URL, shows progress bar.

wget http://www.matf.bg.ac.rs/images/matf.gif -qd
Quiet download, for HTTP shows header data.
```

### curl

```
curl is a tool to transfer data from or to a server, using one of the 
supported protocols (DICT, FILE, FTP, FTPS, GOPHER, HTTP, HTTPS, IMAP, 
IMAPS, LDAP, LDAPS, POP3, POP3S, RTMP, RTSP, SCP, SFTP, SMB, SMBS, 
SMTP, SMTPS, TELNET and TFTP). The command is designed to work without 
user interaction.

curl offers a busload of useful tricks like proxy support, user 
authentication, FTP upload, HTTP post, SSL connections, cookies, file 
transfer resume, Metalink, and more. 

curl is powered by libcurl for all transfer-related features. 
See libcurl(3) for details. 
```

Examples
```
curl https://www.matf.bg.ac.rs
Performs a HTTP GET request.

curl -o rename.txt https://my.server.come/css/MyTextFile.txt
curl -O https://my.server.come/css/MyTextFile.txt

curl -v http://www.matf.bg.ac.rs
Verbose mode.

curl -I https://www.matf.bg.ac.rs/
Performs a simple HTTP HEAD request.

curl -sD - -o /dev/null http://www.matf.bg.ac.rs 
Explanation:
	-s - Avoid showing progress bar
	-D - - Dump headers to a file, but - sends it to stdout
	-o /dev/null - Ignore response body

curl -z local.html http://remote.server.com/remote.html
Download only if resource is newer compared to local copy.

curl -z "Jan 12 2012" http://remote.server.com/remote.html 
Download only if resource is modified since given time.

curl -X POST http://remote.com/
Specify HTTP request type.

curl -d "param1=value1&param2=value2" -X POST http://example.com/api
curl -d '{"key1":"value1", "key2":"value2"}' \
			 -H "Content-Type: application/json" -X POST http://example.com/api
curl -d "@data.json" -X POST http://example.com/api
Specify HTTP post data.

curl -H "X-Header: Value" http://example.com/api
Specify additional HTTP request headers.

curl -D - https://remote.com/ -o /dev/null
Test download time without any outputs.

curl --limit-rate 200K -O https://remote.com/large.zip
Limit the download rate.

curl -C - -O https://remote.com/large.zip
Resume download.
```