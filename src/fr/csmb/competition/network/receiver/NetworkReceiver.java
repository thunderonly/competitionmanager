/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.network.receiver;

import fr.csmb.competition.model.CategorieBean;
import fr.csmb.competition.xml.model.Competition;
import fr.csmb.competition.xml.model.Participant;

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
            byte[] data = new byte[5000];
            ds = new MulticastSocket(port);
            dp = new DatagramPacket(data, data.length);
            ds.joinGroup(InetAddress.getByName(address));

            for (;;) {

                try {
                    ds.receive(dp);
                    ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
                    ObjectInputStream is = new
                            ObjectInputStream(new BufferedInputStream(byteStream));
                    Competition competition = (Competition)is.readObject();
                    is.close();

                    if (!isOwnPacket(dp.getAddress())) {
                        fireReceive(competition);
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
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
                            multicast = "224." + ip[1] + "." + ip[2] + ".1";
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
    private void fireReceive(Competition competition) {
        for (final DatagramListener datagramListener : listDatagramListener) {
            datagramListener.receive(competition);
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
