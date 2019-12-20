# Simple HTTP example

We will implement a simple HTTP server. For simplification, we will assume that
the client sends a request consisting only of filename without headers. The
server will respond fully with correct headers and data.

In order to optimize the server, we will have a `Map<String, ByteBuffer>` cache
of all files located inside `public_html` folder. When the client requests a
file, we will pull the approprate data to send from the cache. 