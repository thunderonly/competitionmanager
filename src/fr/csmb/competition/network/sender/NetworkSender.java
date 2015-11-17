/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.network.sender;

import fr.csmb.competition.Helper.DetailEpreuveConverter;
import fr.csmb.competition.Helper.EleveConverter;
import fr.csmb.competition.Helper.ParticipantConverter;
import fr.csmb.competition.model.ParticipantBean;
import fr.csmb.competition.model.ClubBean;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.EleveBean;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.xml.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class NetworkSender {

    private static NetworkSender INSTANCE;

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

    public static NetworkSender getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new NetworkSender("", 9878);
        }
        return INSTANCE;
    }

    /**
     * Datagram Server Constructor
     *
     * @param port    : port to send
     * @param address : address to send
     */
    private NetworkSender(String address, int port) {
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

            if (address == null || address.equals("")) {
                address = getMulticastIp();
            }
            LOGGER.info("Send club data on address %s and port %s", address, port);
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(new ClubSender(clubBean));
            executorService.shutdown();
        }
    }

    public void send(CompetitionBean competitionBean, EpreuveBean epreuveBean) {

        if (createDatagramSocket()) {

            if (address == null || address.equals("")) {
                address = getMulticastIp();
            }
            LOGGER.info("Send competition data on address %s and port %s", address, port);
            ExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.execute(new CompetitionSender(competitionBean, epreuveBean));
            executorService.shutdown();
        }
    }

    private void sendPacket(byte[] sendBuf) throws IOException, InterruptedException {
        DatagramPacket packet = new DatagramPacket(sendBuf, sendBuf.length, new InetSocketAddress(address, port));
        int byteCount = packet.getLength();
        ds.send(packet);
        LOGGER.info("Data sent on address %s and port %s. Length %s", address, port, byteCount);
        Thread.sleep(1000);
    }

    private byte[] buildPacketEpreuve(CompetitionBean competitionBean, EpreuveBean epreuveBean) throws IOException {
        Competition competition = new Competition(competitionBean.getNom());
        Categorie categorie = new Categorie(epreuveBean.getCategorie().getNom(), epreuveBean.getCategorie().getType());
        competition.getCategories().add(categorie);
        Discipline discipline = new Discipline(epreuveBean.getDiscipline().getNom(), epreuveBean.getDiscipline().getType());
        competition.getDiscipline().add(discipline);

        Epreuve epreuve = new Epreuve();
        epreuve.setCategorie(categorie);
        epreuve.setDiscipline(discipline);
        DetailEpreuve detailEpreuve = DetailEpreuveConverter.convertDetailEpreuveBeanToDetailEpreuve(epreuveBean.getDetailEpreuve());
        epreuve.setDetailEpreuve(detailEpreuve);
        epreuve.setEtatEpreuve(epreuveBean.getEtat());
        epreuve.setLabelEpreuve(epreuveBean.getLabel());
        competition.getEpreuve().add(epreuve);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(2500);
        ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(byteStream));
        os.flush();
        os.writeObject(competition);
        os.flush();
        //retrieves byte array
        byte[] sendBuf = byteStream.toByteArray();
        return sendBuf;
    }

    private byte[] buildPacketParticipants(CompetitionBean competitionBean, List<Participant> participants) throws IOException {
        Competition competition = new Competition(competitionBean.getNom());
        competition.setParticipant(participants);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(2500);
        ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(byteStream));
        os.flush();
        os.writeObject(competition);
        os.flush();
        //retrieves byte array
        byte[] sendBuf = byteStream.toByteArray();
        return sendBuf;
    }

    private byte[] buildPacket(CompetitionBean competitionBean, EpreuveBean epreuveBean, int nbSend, int nbParticipants, List<Participant> participants)
            throws IOException{
        Competition competition = new Competition(competitionBean.getNom());
        Categorie categorie = new Categorie(epreuveBean.getCategorie().getNom(), epreuveBean.getCategorie().getType());
        competition.getCategories().add(categorie);
        Discipline discipline = new Discipline(epreuveBean.getDiscipline().getNom(), epreuveBean.getDiscipline().getType());
        competition.getDiscipline().add(discipline);

        Epreuve epreuve = new Epreuve();
        epreuve.setCategorie(categorie);
        epreuve.setDiscipline(discipline);
        if (nbSend >= nbParticipants) {
            DetailEpreuve detailEpreuve = DetailEpreuveConverter.convertDetailEpreuveBeanToDetailEpreuve(epreuveBean.getDetailEpreuve());
            epreuve.setDetailEpreuve(detailEpreuve);
            epreuve.setEtatEpreuve(epreuveBean.getEtat());
        }
//        competition.getEpreuve().add(epreuve);
        LOGGER.info("Send epreuve %s with etat %s", epreuveBean.toString(), epreuveBean.getEtat());

//        competition.setParticipant(participants);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(5000);
        ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(byteStream));
        os.flush();
        os.writeObject(competition);
        os.flush();
        //retrieves byte array
        byte[] sendBuf = byteStream.toByteArray();
        return sendBuf;
    }


    private boolean createDatagramSocket() {
        boolean open = true;
        try {
            if (ds == null) {
                ds = new MulticastSocket();
                ds.setTimeToLive(5);
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

    private class ClubSender implements Runnable {

        private ClubBean clubBean;

        public ClubSender(ClubBean clubBean) {
            this.clubBean = clubBean;
        }

        @Override
        public void run() {
            try {
                int nbEleves = clubBean.getEleves().size();
                int nbSend = 0;
                List<Eleve> eleves = new ArrayList<Eleve>(2);
                for (EleveBean eleveBean : clubBean.getEleves()) {
                    eleves.add(EleveConverter.convertEleveBeanToEleve(eleveBean));
                    nbSend++;
                    if (nbSend % 4 == 0 || nbSend == nbEleves) {
                        Club club = new Club(clubBean.getIdentifiant(), clubBean.getNom());
                        club.setEleves(eleves);
                        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(5000);
                        ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(byteStream));
                        os.flush();
                        os.writeObject(club);
                        os.flush();
                        //retrieves byte array
                        byte[] sendBuf = byteStream.toByteArray();
                        sendPacket(sendBuf);
                        eleves.clear();
                    }
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
                LOGGER.error("Error when send data ", e);
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.error("Error when send data ", e);
            } catch (InterruptedException e) {
                e.printStackTrace();
                LOGGER.error("Error when send data ", e);
            }
        }
    }

    private class CompetitionSender implements Runnable {

        private CompetitionBean competitionBean;
        private EpreuveBean epreuveBean;

        public CompetitionSender(CompetitionBean competitionBean, EpreuveBean epreuveBean) {
            this.competitionBean = competitionBean;
            this.epreuveBean = epreuveBean;
        }

        @Override
        public void run() {
            try {
                byte[] sendBuf = buildPacketEpreuve(competitionBean, epreuveBean);
                sendPacket(sendBuf);

                int nbParticipants = competitionBean.getParticipantByEpreuve(epreuveBean).size();
                int nbSend = 0;
                List<Participant> participants = new ArrayList<Participant>(4);
                for (ParticipantBean participantBean : competitionBean.getParticipantByEpreuve(epreuveBean)) {
                    participants.add(ParticipantConverter.convertParticipantBeanToParticipant(participantBean));
                    nbSend++;
                    if (nbSend % 2 == 0 || nbSend == nbParticipants) {

                        //retrieves byte array
                        sendBuf = buildPacketParticipants(competitionBean, participants);
                        sendPacket(sendBuf);
                        for (Participant participant : participants) {
                            LOGGER.info("Send participant %s %s", participant.getNomParticipant(), participant.getPrenomParticipant());
                        }
                        participants.clear();
                    }
                }
//                if (!isSend) {
//                    //retrieves byte array
//                    byte[] sendBuf = buildPacket(competitionBean, epreuveBean, 0, 0, participants);
//                    sendPacket(sendBuf);
//                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
                LOGGER.error("Error when send data ", e);
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.error("Error when send data ", e);
            } catch (InterruptedException e) {
                e.printStackTrace();
                LOGGER.error("Error when send data ", e);
            }
        }
    }
}
