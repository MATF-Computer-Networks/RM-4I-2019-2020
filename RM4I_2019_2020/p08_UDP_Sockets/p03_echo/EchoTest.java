package p03_echo;

import java.io.IOException;

class EchoTest {

	public static void main(String[] args) throws IOException {
		try (EchoServer server = new EchoServer();
			 EchoClient client = new EchoClient();
		) {
			server.start();

			String echo;
			echo = client.sendEcho("(test1) hello!");
			System.out.println("(test1) returned: " + echo);
			echo = client.sendEcho("(test2) works?");
			System.out.println("(test2) returned: " + echo);
			echo = client.sendEcho("(test3) \uD83D\uDE0B");
			System.out.println("(test3) returned: " + echo);

			client.sendEcho("end");
		}

		System.out.println("Test finished.");
	}

}
