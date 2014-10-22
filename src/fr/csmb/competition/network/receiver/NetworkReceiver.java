/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.network.receiver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
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
            byte[] data = new byte[256];
            ds = new MulticastSocket(port);
            dp = new DatagramPacket(data, data.length);
            ds.joinGroup(InetAddress.getByName(address));

            for (;;) {

                try {
                    ds.receive(dp);
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!isOwnPacket(dp.getAddress())) {
                    fireReceive(rebuildNmeaSentence(dp.getData()));
                } else {
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

    /**
     * Extract NmeaSentence in received data.
     * <p/>
     * *
     */
    protected String rebuildNmeaSentence(byte[] data) {
        final StringBuilder nmeaBuffer = new StringBuilder();
        for (final byte c : data) {
            if (c == NMEA_SEP) {
                break;
            } else {
                nmeaBuffer.append((char) c);
            }
        }
        return nmeaBuffer.toString();
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
    private void fireReceive(String msg) {
        for (final DatagramListener datagramListener : listDatagramListener) {
            datagramListener.receive(msg);
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
