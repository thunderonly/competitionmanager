package fr.csmb.competition.network;

import fr.csmb.competition.Helper.DetailEpreuveConverter;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.xml.model.*;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrateur on 28/11/15.
 */
public class NewSender extends Thread {

    public void run() {
        try {
            int i = 0;
            while (true) {
                MulticastSocket socket = new MulticastSocket();
                byte[] buf = buildPacketEpreuve(i);
                InetAddress group = InetAddress.getByName("224.192.168.1");
                DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 4446);
//                System.out.println("Send compétition lenght: " + packet.getLength());
                socket.send(packet);
                i++;

                sleep(1000);
            }
        } catch (Exception exc) {

        }
    }
    private byte[] buildPacketEpreuve(int numCompetition) throws IOException {
        Competition competition = new Competition("Compétition " + numCompetition);
        Categorie categorie = new Categorie("Catégorie 1", "Masculin 1");
        competition.getCategories().add(categorie);
        Discipline discipline = new Discipline("Discipline 1", "Technique");
        competition.getDiscipline().add(discipline);

        Epreuve epreuve = new Epreuve();
        epreuve.setCategorie(categorie);
        epreuve.setDiscipline(discipline);
        DetailEpreuve detailEpreuve = new DetailEpreuve();
        detailEpreuve.setAdministrateur("Admin");
        detailEpreuve.setChronometreur("Chrono");
        detailEpreuve.setJuge1("Juge1");
        detailEpreuve.setJuge2("");
        detailEpreuve.setJuge3("");
        detailEpreuve.setJuge4("");
        detailEpreuve.setJuge5("");
        detailEpreuve.setTapis("");
        detailEpreuve.setHeureDebut("");
        detailEpreuve.setHeureFin("");
        detailEpreuve.setDuree("");
        epreuve.setDetailEpreuve(detailEpreuve);
        epreuve.setEtatEpreuve("Validée");
        epreuve.setLabelEpreuve("Epreuve");
        competition.getEpreuve().add(epreuve);

        List<Participant> participants = new ArrayList<Participant>();
        for (int i=0; i < 30; i++) {
            Participant participant = new Participant();
            participant.setNomParticipant("Nom" + i);
            participant.setPrenomParticipant("Préom" + i);
            participant.setClubParticipant("Club 1");
            participant.setPoidsParticipant(String.valueOf(60));
            participant.setNote1(String.valueOf(15));
            participant.setNote2(String.valueOf(15));
            participant.setNote3(String.valueOf(15));
            participant.setNote4(String.valueOf(15));
            participant.setNote5(String.valueOf(15));
            participant.setClassementAuto(String.valueOf(1));
            participant.setClassementManuel(String.valueOf(1));
            participant.setClassementFinal(String.valueOf(1));
            participant.setPlaceOnGrid(String.valueOf(1));
            participant.setParticipe(true);

            participants.add(participant);
        }

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
}
