/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverPackage;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author gniammo
 */
public class ThreadedEchoHandler implements Runnable {

    private Socket incoming;
    private int counter;

    public ThreadedEchoHandler(Socket in, int c) {
        incoming = in;
        counter = c;
    }

    public void run() {
        String nomeAccount = "";

        //PHASE 1: The server receives the email
        try {
            InputStream inStream = incoming.getInputStream();
            Scanner in = new Scanner(inStream);
            nomeAccount = in.nextLine(); //ricevo il nome
        } catch (IOException ex) {
            ex.getMessage();
        } finally {
            try {
                incoming.close();
            } catch (IOException ex) {
                ex.getMessage();
            }
        }

    }
}
