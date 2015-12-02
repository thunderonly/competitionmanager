package fr.csmb.competition.network;

import fr.csmb.competition.xml.model.Competition;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;

/**
 * Created by Administrateur on 28/11/15.
 */
public class NewReceiver extends Thread {

    public void run() {
        try {
            MulticastSocket socket = new MulticastSocket(4446);
            InetAddress group = InetAddress.getByName("224.192.168.1");
            socket.joinGroup(group);

            DatagramPacket packet;
            while (true) {
                byte[] data = new byte[5000];
                packet = new DatagramPacket(data, data.length);
                socket.receive(packet);
                if (NetworkInterface.getByInetAddress(packet.getAddress()) == null) {
                    ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
                    ObjectInputStream is = new
                            ObjectInputStream(new BufferedInputStream(byteStream));
                    Object receivedObj = is.readObject();
                    is.close();
                    byteStream.close();
                    if (receivedObj instanceof Competition) {
                        Competition competition = (Competition)receivedObj;
                        System.out.println("Receive compétition : " + competition.getNom());
                    }
//                    System.out.println("Bound : " + socket.isBound() + " " + socket.isConnected() + " " + socket.getReceiveBufferSize());
                }
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

}
