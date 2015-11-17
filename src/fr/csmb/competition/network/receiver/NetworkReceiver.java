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
        this.setName("LocationNMEA-UDP-Thread");
    }

    @Override
    public void run() {
        try {

            if (address == null || address.equals("")) {
                address = getMulticastIp();
            }
            ds = new MulticastSocket(port);
            ds.setTimeToLive(5);
            ds.joinGroup(InetAddress.getByName(address));

            LOGGER.info("DataSource join the address %s on port %s", address, port);

            while (running) {

                try {

                    byte[] data = new byte[2500];
                    dp = new DatagramPacket(data, data.length);
                    ds.receive(dp);
                    LOGGER.info("DataSource receive datagram packet from address %s. Length %s", dp.getAddress(), dp.getLength());
                    if (!isOwnPacket(dp.getAddress())) {
                        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
                        ObjectInputStream is = new
                                ObjectInputStream(new BufferedInputStream(byteStream));
                        Object receivedObj = is.readObject();
                        is.close();
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

    private String getMulticastIp() {
        Enumeration<NetworkInterface> list;
        String multicast = null;
        try {
            list = NetworkInterface.getNetworkInterfaces();
            while (list.hasMoreElements()) {
                NetworkInterface iface = (NetworkInterface) list.nextElement();
                if (iface != null && !iface.isLoopback() && iface.isUp()
                        && !iface.isVirtual()) {
                    Enumeration<InetAddress> iadds = iface.getInetAddresses();
                    while (iadds.hasMoreElements()) {
                        InetAddress address = iadds.nextElement();
                        if (address != null && !"".equals(address.getHostAddress())) {
                            String addressStr = address.getHostAddress();
                            String[] ip = addressStr.split("\\.");
                            multicast = "224." + "0" + "." + "0" + ".1";
                            return multicast;
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * Fire all listener
     */
    private void fireReceive(final Competition competition) {
        Platform.runLater(new Runnable() {
            @Override
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

    public void addNmeaUdpListener(DatagramListener datagramListener) {
        if (!listDatagramListener.contains(datagramListener)) {
            listDatagramListener.add(datagramListener);
        }
    }

    public void removeNmeaUdpListener(DatagramListener datagramListener) {
        listDatagramListener.remove(datagramListener);
    }
}
