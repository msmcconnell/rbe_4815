/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rbe_4815_final_project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import javax.swing.SwingWorker;

/**
 *
 * @author motmo
 */
public class TCPServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader inputStream;
    private PrintWriter outputStream;
    private boolean hasConnection = false;
    
    TCPServer(){

    }
    
    public void openSocket(int portNumber, int msTimeout) {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
            
            serverSocket = new ServerSocket(portNumber);
            serverSocket.setSoTimeout(msTimeout);
            
            //This line is blocking
            clientSocket = serverSocket.accept();
            //Setup Client
            inputStream = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
            outputStream = new PrintWriter(clientSocket.getOutputStream(), true);
            hasConnection = true;
        }
        catch (SocketTimeoutException e) {
           System.out.println("Possible Timeout: Exption-> " + e);
            hasConnection = false;
        }
        catch (IOException e) {
           System.out.println(e);
            hasConnection = false;
        }
    }
    public void closeSocket(){
        hasConnection = false;
        try {
            serverSocket.close();
        }
        catch (IOException e) {
           System.out.println(e);
        }
    }
    
    public void writeString(String str) {
        outputStream.println(str);
    }
    
    public void echo() {
        System.out.println("In Echo");
         try {
            String inputLine;
            while ((inputLine = inputStream.readLine()) != null) {
                System.out.println("Recieved");
                outputStream.println(inputLine);
            }
            
        }
        catch (IOException e) {
           System.out.println(e);
        }
    }
    
    public boolean isConnected() {
        return hasConnection;
    }
}
