package org.kd4.httpserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.cert.CRL;
import org.kd4.httpserver.config.Configuration;
import org.kd4.httpserver.config.ConfigurationManager;

/**
* Driver Class for the Http Server
*/
public class HttpServer {
  // If HttpServer is your driver class, then it needs to have a main method
  public static void main(String[] args) {
    System.out.println("Server starting...");
    ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
    Configuration config = ConfigurationManager.getInstance().getCurrentConfiguration();

    System.out.println("port : " + config.getPort());
    System.out.println("webRoot : " + config.getWebroot());




    try {
      ServerSocket serverSocket = new ServerSocket(config.getPort());
      System.out.println("Line1");
      /*
      * The given piece of code can only handle a single connection
      *
      * 1. Blocking nature of accept()
      * - The line Socket socket = serverSocket.accept(); blocks the program execution until a client connects to the server
      * - Once a client connects, the server processes the request, sends a response and then closes the connection
      * - During this time the server cannot accept any new connections because it is busy handling the first connection
      *
      * 2. No Multi Threading
      * - The code does not use threads or any concurrency mechanisms to handle multiple client connections simultaneously
      * - For a server to handle multiple connections, each connection should ideally be processed in its own thread or by using non-blocking IO
      *   with an event driven approach (e.g. Java NIO or an asynchronous framework)
      *
      * 3. Sequential Execution
      * - The code processes the input and output streams for the client connection (inputStream and outputStream) sequentially.
      * - Only after closing these streams, does the socket do server shutdown
      * - This means that the server handles one client at a time and then terminates.
      * */

      Socket socket = serverSocket.accept();
      /*
      * This line blocks the program until a client connects to the server. Once the client connects, the accept() method
      * returns a new Socket object that represents the connection to the client*/
      System.out.println("Line 2");

      InputStream inputStream = socket.getInputStream();
      OutputStream outputStream = socket.getOutputStream();

      String html = "<html><head><title>Simple Java HTTP Server</title></head><body><h1>This page was served using my Simple Java HTTP Server</h1></body></html>";

      final String CRLF = "\n\r"; //Carriage Return, Line Feed - ASCII characters 10 and 13
      /*
      * A carriage return, sometimes known as cartridge return and often shortened as CR, <CR> or return is a
      * control character or mechanism used to reset a device's position to the beginning of a line of text.
      *
      * /r/n or /n/r
      * */

      String response =
          "HTTP/1.1 200 OK" // Status Line : HTTP_VERSION RESPONSE_CODE RESPONSE_MESSAGE
          + CRLF
          + "Content-Length: " + html.getBytes().length // HEADER
          + CRLF + CRLF
          + html
          + CRLF + CRLF;

      outputStream.write(response.getBytes());

      inputStream.close();
      outputStream.close();
      socket.close();
      serverSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
