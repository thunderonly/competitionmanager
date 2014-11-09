/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.network.sender;

import fr.csmb.competition.Helper.ClubConverter;
import fr.csmb.competition.Helper.ParticipantConverter;
import fr.csmb.competition.component.grid.bean.ParticipantBean;
import fr.csmb.competition.model.CategorieBean;
import fr.csmb.competition.model.ClubBean;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.EleveBean;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.xml.model.Categorie;
import fr.csmb.competition.xml.model.Club;
import fr.csmb.competition.xml.model.Competition;
import fr.csmb.competition.xml.model.Eleve;
import fr.csmb.competition.xml.model.Epreuve;
import fr.csmb.competition.xml.model.Participant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Enumeration;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class NetworkSender {

    private static final Logger LOGGER = LogManager.getFormatterLogger(NetworkSender.class);

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

    public void sendClub(ClubBean clubBean) {
        if (createDatagramSocket()) {

            try {
                if (address == null || address.equals("")) {
                    address = getMulticastIp();
                }
                LOGGER.info("Send club data on address %s and port %s", address, port);
                Club club = ClubConverter.convertClubBeanToClub(clubBean);

                ByteArrayOutputStream byteStream = new ByteArrayOutputStream(5000);
                ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(byteStream));
                os.flush();
                os.writeObject(club);
                os.flush();
                //retrieves byte array
                byte[] sendBuf = byteStream.toByteArray();
                DatagramPacket packet = new DatagramPacket(sendBuf, sendBuf.length, new InetSocketAddress(address,port));
                int byteCount = packet.getLength();
                ds.send(packet);
                LOGGER.info("Data sent on address %s and port %s", address, port);
            } catch (UnknownHostException e) {
                e.printStackTrace();
                LOGGER.error("Error when send data ", e);
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.error("Error when send data ", e);
            }
        }
    }

    public void send(CompetitionBean competitionBean, CategorieBean categorieBean, EpreuveBean epreuveBean) {

        if (createDatagramSocket()) {

            try {
                if (address == null || address.equals("")) {
                    address = getMulticastIp();
                }
                LOGGER.info("Send competition data on address %s and port %s", address, port);

                Competition competition = new Competition(competitionBean.getNom());
                Categorie categorie = new Categorie(categorieBean.getNom(), categorieBean.getType());
                competition.getCategories().add(categorie);
                Epreuve epreuve = new Epreuve(epreuveBean.getNom(), epreuveBean.getType());
                epreuve.setEtatEpreuve(epreuveBean.getEtat());
                categorie.getEpreuves().add(epreuve);

                for (ParticipantBean participantBean : epreuveBean.getParticipants()) {
                    Participant participant = ParticipantConverter.convertParticipantBeanToParticipant(participantBean);
                    epreuve.getParticipants().add(participant);
                }


                ByteArrayOutputStream byteStream = new ByteArrayOutputStream(5000);
                ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(byteStream));
                os.flush();
                os.writeObject(competition);
                os.flush();
                //retrieves byte array
                byte[] sendBuf = byteStream.toByteArray();
                DatagramPacket packet = new DatagramPacket(sendBuf, sendBuf.length, new InetSocketAddress(address,port));
                int byteCount = packet.getLength();
                ds.send(packet);
                LOGGER.info("Data sent on address %s and port %s", address, port);
            } catch (UnknownHostException e) {
                e.printStackTrace();
                LOGGER.error("Error when send data ", e);
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.error("Error when send data ", e);
            }
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
