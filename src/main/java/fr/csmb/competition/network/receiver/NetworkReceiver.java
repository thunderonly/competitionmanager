/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.network.receiver;

import fr.csmb.competition.model.CategorieBean;
import fr.csmb.competition.xml.model.Club;
import fr.csmb.competition.xml.model.Competition;
import fr.csmb.competition.xml.model.Participant;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class NetworkReceiver extends Thread {

    private static final Logger LOGGER = LogManager.getFormatterLogger(NetworkReceiver.class);

    /**
     * Listening port
     */
    int port = 9878;

    /**
     * Time to live
     */
    int ttl = 5;

    /**
     * Multicast Address
     */
    private String address;

    private static final char NMEA_SEP = '\n';

    /**
     * List of datagram listener
     */
    List<DatagramListener> listDatagramListener = new CopyOnWriteArrayList<DatagramListener>();

    private MulticastSocket ds;

    private DatagramPacket dp;

    private boolean running = true;

    /**
     * Constructor
     *
     * @param port
     *            : listenning port.
     */
    public NetworkReceiver(String address, int port) {
        this.address = address;
        this.port = port;
    }

    @Override
    public void run() {
        try {

            MulticastSocket socket = new MulticastSocket(4446);
            InetAddress group = InetAddress.getByName("224.192.168.1");
            LOGGER.info("Try to join group a the address %s on port %s", address, port);
            socket.joinGroup(group);
            LOGGER.info("DataSource join the address %s on port %s", address, port);

            DatagramPacket packet;

            while (running) {

                try {
                    byte[] data = new byte[5000];
                    packet = new DatagramPacket(data, data.length);
                    socket.receive(packet);
                    LOGGER.info("DataSource receive datagram packet from address %s. Length %s", packet.getAddress(), packet.getLength());
                    if (!isOwnPacket(packet.getAddress())) {
                        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
                        ObjectInputStream is = new
                                ObjectInputStream(new BufferedInputStream(byteStream));
                        Object receivedObj = is.readObject();
                        is.close();
                        byteStream.close();
                        if (receivedObj instanceof Competition) {
                            LOGGER.info("Receive a competition");
                            Competition competition = (Competition)receivedObj;
                            fireReceive(competition);
                        } else if (receivedObj instanceof Club) {
                            LOGGER.info("Receive a club");
                            Club club = (Club) receivedObj;
                            fireReceive(club);
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                    LOGGER.error("Exception when receive data ", e);
                } catch (Exception e) {
                    e.printStackTrace();
                    LOGGER.error("Exception when receive data ", e);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("Exception when receive data ", e);
        }
    }

    /**
     * Verify if InetAddress is own address.
     *
     * @param inetAddress
     *            : inetaddress to verify
     * @return true if inetaddress is own address otherwise false.
     */
    protected boolean isOwnPacket(InetAddress inetAddress) {
        boolean ownPacket = true;
        try {
            if (NetworkInterface.getByInetAddress(inetAddress) == null) {
                ownPacket = false;
            }
        } catch (SocketException e) {
            e.printStackTrace();
            ownPacket = false;
        }
        return ownPacket;
    }

    public void stopSocket() {
        if (this.ds != null) {
            running = false;
            this.ds.close();
        }
    }

    /**
     * Fire all listener
     */
    private void fireReceive(final Competition competition) {
        Platform.runLater(new Runnable() {
            public void run() {
                for (final DatagramListener datagramListener : listDatagramListener) {
                    datagramListener.receive(competition);
                }
            }
        });
    }

    /**
     * Fire all listener
     */
    private void fireReceive(Club club) {
        for (final DatagramListener datagramListener : listDatagramListener) {
            datagramListener.receive(club);
        }
    }

    public void addListener(DatagramListener datagramListener) {
        if (!listDatagramListener.contains(datagramListener)) {
            listDatagramListener.add(datagramListener);
        }
    }

    public void removeNmeaUdpListener(DatagramListener datagramListener) {
        listDatagramListener.remove(datagramListener);
    }
}
