/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.network.sender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class NetworkSender {


    private MulticastSocket ds ;

    /**
     * Nmea Sentences Separator
     * *
     */
    private static final char NMEA_SEP = '\n';

    /**
     * Port
     */
    int port = 9879;

    /**
     * Time to live
     */
    int ttl = 5;

    /**
     * Address
     */
    String address;

    /**
     * Datagram Server Constructor
     *
     * @param port    : port to send
     * @param address : address to send
     */
    public NetworkSender(String address, int port) {
        this.port = port;
        this.address = address;
    }

    /**
     * Close Method.
     */
    public void close() {
        if (this.ds != null) {
            this.ds.close();
        }

    }

    public void send(String nmeaSentence) {

        if (createDatagramSocket()) {

            try {
                if (address == null || address.equals("")) {
                    address = getMulticastIp();
                }
                final byte[] nmeaPacket = buildPacket(nmeaSentence);
                final DatagramPacket sendPacket = new DatagramPacket(nmeaPacket, nmeaPacket.length,
                        new InetSocketAddress(address,port));

                ds.send(sendPacket);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
        }
    }


    private byte[] buildPacket(String nmeaSentence) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(nmeaSentence).append(NMEA_SEP);
        return stringBuilder.toString().getBytes();
    }


    private boolean createDatagramSocket() {
        boolean open = true;
        try {
            if (ds == null) {
                ds = new MulticastSocket();
            }

        } catch (IOException e) {
            e.printStackTrace();
            open = false;
        }
        return open;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    private String getMulticastIp(){
        Enumeration<NetworkInterface> list;
        String multicast = null;
        try {
            list = NetworkInterface.getNetworkInterfaces();
            while(list.hasMoreElements()) {
                NetworkInterface iface = (NetworkInterface) list.nextElement();
                if(iface != null && !iface.isLoopback() && iface.isUp() && !iface.isVirtual()) {
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
     * Check if there is lan connection
     *
     * @return true if lan is connected otherwise false.
     */
    protected boolean isLanConnected() {
        try {
            final Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                final NetworkInterface interf = interfaces.nextElement();
                if (interf.isUp() && !interf.isLoopback()) {
                    return true;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return false;
    }
}
