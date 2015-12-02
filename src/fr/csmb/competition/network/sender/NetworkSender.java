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
    ExecutorService executorService = null;

    private MulticastSocket ds ;


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

    public void sendClub(CompetitionBean competitionBean, ClubBean clubBean) {
            LOGGER.info("Send club data on address %s and port %s", address, port);
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(new ClubSender(competitionBean, clubBean));
    }

    public void send(CompetitionBean competitionBean, EpreuveBean epreuveBean) {

            LOGGER.info("Send competition data on address %s and port %s", address, port);
            executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.execute(new CompetitionSender(competitionBean, epreuveBean));
    }

    private byte[] buildPacketEpreuve(CompetitionBean competitionBean, EpreuveBean epreuveBean, List<Participant> participants) throws IOException {
        Competition competition = new Competition(competitionBean.getNom());
        Categorie categorie = new Categorie(epreuveBean.getCategorie().getNom(), epreuveBean.getCategorie().getSexe());
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

        competition.setParticipant(participants);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(5000);
        ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(byteStream));
        os.flush();
        os.writeObject(competition);
        os.flush();
        //retrieves byte array
        byte[] sendBuf = byteStream.toByteArray();
        return sendBuf;
    }

    private byte[] buildPacketClub(CompetitionBean competitionBean, ClubBean clubBean, List<Eleve> eleves) throws IOException {
        Competition competition = new Competition(competitionBean.getNom());
        Club club = new Club(clubBean.getIdentifiant(), clubBean.getNom());
        club.setEleves(eleves);
        List<Club> clubs = new ArrayList<Club>(1);
        clubs.add(club);
        competition.setClubs(clubs);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(5000);
        ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(byteStream));
        os.flush();
        os.writeObject(competition);
        os.flush();
        //retrieves byte array
        byte[] sendBuf = byteStream.toByteArray();
        return sendBuf;
    }

    private class ClubSender implements Runnable {

        private ClubBean clubBean;
        private CompetitionBean competitionBean;

        public ClubSender(CompetitionBean competitionBean, ClubBean clubBean) {
            this.competitionBean = competitionBean;
            this.clubBean = clubBean;
        }

        @Override
        public void run() {
            try {
                List<Eleve> eleves = new ArrayList<Eleve>(2);
                for (EleveBean eleveBean : clubBean.getEleves()) {
                    eleves.add(EleveConverter.convertEleveBeanToEleve(eleveBean));
                }
                MulticastSocket socket = new MulticastSocket();
                byte[] buf = buildPacketClub(competitionBean, clubBean, eleves);
                InetAddress group = InetAddress.getByName("224.192.168.1");
                DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 4446);
                socket.send(packet);
                executorService.shutdown();
                socket.close();

            } catch (UnknownHostException e) {
                e.printStackTrace();
                LOGGER.error("Error when send data ", e);
            } catch (IOException e) {
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

                List<Participant> participants = new ArrayList<Participant>(4);
                for (ParticipantBean participantBean : competitionBean.getParticipantByEpreuve(epreuveBean)) {
                    participants.add(ParticipantConverter.convertParticipantBeanToParticipant(participantBean));
                }
                MulticastSocket socket = new MulticastSocket();
                byte[] buf = buildPacketEpreuve(competitionBean, epreuveBean, participants);
                InetAddress group = InetAddress.getByName("224.192.168.1");
                DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 4446);
                socket.send(packet);
                executorService.shutdown();
                socket.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
                LOGGER.error("Error when send data ", e);
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.error("Error when send data ", e);
            }
        }
    }
}
