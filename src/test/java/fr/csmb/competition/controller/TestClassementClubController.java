/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Spy;

import fr.csmb.competition.Helper.ClassementHelper;
import fr.csmb.competition.Helper.ParticipantHelper;
import fr.csmb.competition.model.CategorieBean;
import fr.csmb.competition.model.ClubBean;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.DisciplineBean;
import fr.csmb.competition.model.EleveBean;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.model.ParticipantBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class TestClassementClubController {

    @Spy
    CompetitionBean competitionBean = new CompetitionBean("CompétitionTest");
    @Spy
    CategorieBean categorieBeanCadetMasculin = new CategorieBean("Cadet");
    @Spy
    CategorieBean categorieBeanCadetFeminin = new CategorieBean("Cadet");
    @Spy
    CategorieBean categorieBeanJuniorMasculin = new CategorieBean("Junior");
    @Spy
    CategorieBean categorieBeanJuniorFeminin = new CategorieBean("Junior");
    @Spy
    CategorieBean categorieBeanSeniorMasculin = new CategorieBean("Senior");
    @Spy
    CategorieBean categorieBeanSeniorFeminin = new CategorieBean("Senior");
    @Spy
    DisciplineBean disciplineBeanQMN = new DisciplineBean("Quyen Main Nue", "Technique");
    @Spy
    DisciplineBean disciplineBeanQA = new DisciplineBean("Quyen Arme", "Technique");
    @Spy
    EpreuveBean epreuveBeanQMNCadetMasculin = new EpreuveBean();
    @Spy
    EpreuveBean epreuveBeanQMNCadetFeminin = new EpreuveBean();
    @Spy
    EpreuveBean epreuveBeanQMNJuniorMasculin = new EpreuveBean();
    @Spy
    EpreuveBean epreuveBeanQMNJuniorFeminin = new EpreuveBean();
    @Spy
    EpreuveBean epreuveBeanQMNSeniorMasculin = new EpreuveBean();
    @Spy
    EpreuveBean epreuveBeanQMNSeniorFeminin = new EpreuveBean();
    @Spy
    EpreuveBean epreuveBeanQACadetMasculin = new EpreuveBean();
    @Spy
    EpreuveBean epreuveBeanQACadetFeminin = new EpreuveBean();
    @Spy
    EpreuveBean epreuveBeanQAJuniorMasculin = new EpreuveBean();
    @Spy
    EpreuveBean epreuveBeanQAJuniorFeminin = new EpreuveBean();
    @Spy
    EpreuveBean epreuveBeanQASeniorMasculin = new EpreuveBean();
    @Spy
    EpreuveBean epreuveBeanQASeniorFeminin = new EpreuveBean();
    @Spy
    ClubBean club1 = new ClubBean();
    @Spy
    ClubBean club2 = new ClubBean();
    @Spy
    ClubBean club3 = new ClubBean();
    @Spy
    ClubBean club4 = new ClubBean();

    @Before
    public void startTest () {
        competitionBean.setCategories(FXCollections.observableArrayList(
                categorieBeanCadetMasculin,
                categorieBeanCadetFeminin,
                categorieBeanJuniorFeminin,
                categorieBeanJuniorMasculin,
                categorieBeanSeniorFeminin,
                categorieBeanSeniorMasculin));
        competitionBean.setDisciplines(FXCollections.<DisciplineBean>observableArrayList(
                disciplineBeanQA,
                disciplineBeanQMN));
        competitionBean.setEpreuves(FXCollections.<EpreuveBean>observableArrayList(
                epreuveBeanQMNCadetFeminin,
                epreuveBeanQMNCadetMasculin,
                epreuveBeanQMNJuniorFeminin,
                epreuveBeanQMNJuniorMasculin,
                epreuveBeanQMNSeniorFeminin,
                epreuveBeanQMNSeniorMasculin,
                epreuveBeanQACadetFeminin,
                epreuveBeanQACadetMasculin,
                epreuveBeanQAJuniorFeminin,
                epreuveBeanQAJuniorMasculin,
                epreuveBeanQASeniorFeminin,
                epreuveBeanQASeniorMasculin));
        competitionBean.setClubs(FXCollections.<ClubBean>observableArrayList(club1,
                club2,
                club3,
                club4));

        categorieBeanCadetFeminin.setSexe("Féminin");
        categorieBeanJuniorFeminin.setSexe("Féminin");
        categorieBeanSeniorFeminin.setSexe("Féminin");
        categorieBeanCadetMasculin.setSexe("Masculin");
        categorieBeanJuniorMasculin.setSexe("Masculin");
        categorieBeanSeniorMasculin.setSexe("Masculin");

        epreuveBeanQMNCadetFeminin.setCategorie(categorieBeanCadetFeminin);
        epreuveBeanQMNCadetFeminin.setDiscipline(disciplineBeanQMN);
        epreuveBeanQMNCadetFeminin.setId(
                categorieBeanCadetFeminin.getNom().concat(categorieBeanCadetFeminin.getSexe().
                        concat(disciplineBeanQMN.getNom().concat(disciplineBeanQMN.getType()))));
        epreuveBeanQMNCadetMasculin.setCategorie(categorieBeanCadetMasculin);
        epreuveBeanQMNCadetMasculin.setDiscipline(disciplineBeanQMN);
        epreuveBeanQMNCadetMasculin.setId(
                categorieBeanCadetMasculin.getNom().concat(categorieBeanCadetMasculin.getSexe().
                        concat(disciplineBeanQMN.getNom().concat(disciplineBeanQMN.getType()))));
        epreuveBeanQMNJuniorFeminin.setCategorie(categorieBeanJuniorFeminin);
        epreuveBeanQMNJuniorFeminin.setDiscipline(disciplineBeanQMN);
        epreuveBeanQMNJuniorFeminin.setId(
                categorieBeanJuniorFeminin.getNom().concat(categorieBeanJuniorFeminin.getSexe().
                        concat(disciplineBeanQMN.getNom().concat(disciplineBeanQMN.getType()))));
        epreuveBeanQMNJuniorMasculin.setCategorie(categorieBeanJuniorMasculin);
        epreuveBeanQMNJuniorMasculin.setDiscipline(disciplineBeanQMN);
        epreuveBeanQMNJuniorMasculin.setId(
                categorieBeanJuniorMasculin.getNom().concat(categorieBeanJuniorMasculin.getSexe().
                        concat(disciplineBeanQMN.getNom().concat(disciplineBeanQMN.getType()))));
        epreuveBeanQMNSeniorFeminin.setCategorie(categorieBeanSeniorFeminin);
        epreuveBeanQMNSeniorFeminin.setDiscipline(disciplineBeanQMN);
        epreuveBeanQMNSeniorFeminin.setId(
                categorieBeanSeniorFeminin.getNom().concat(categorieBeanSeniorFeminin.getSexe().
                        concat(disciplineBeanQMN.getNom().concat(disciplineBeanQMN.getType()))));
        epreuveBeanQMNSeniorMasculin.setCategorie(categorieBeanSeniorMasculin);
        epreuveBeanQMNSeniorMasculin.setDiscipline(disciplineBeanQMN);
        epreuveBeanQMNSeniorMasculin.setId(
                categorieBeanSeniorMasculin.getNom().concat(categorieBeanSeniorMasculin.getSexe().
                        concat(disciplineBeanQMN.getNom().concat(disciplineBeanQMN.getType()))));

        epreuveBeanQACadetFeminin.setCategorie(categorieBeanCadetFeminin);
        epreuveBeanQACadetFeminin.setDiscipline(disciplineBeanQA);
        epreuveBeanQACadetFeminin.setId(
                categorieBeanCadetFeminin.getNom().concat(categorieBeanCadetFeminin.getSexe().
                        concat(disciplineBeanQA.getNom().concat(disciplineBeanQA.getType()))));
        epreuveBeanQACadetMasculin.setCategorie(categorieBeanCadetMasculin);
        epreuveBeanQACadetMasculin.setDiscipline(disciplineBeanQA);
        epreuveBeanQACadetMasculin.setId(
                categorieBeanCadetMasculin.getNom().concat(categorieBeanCadetMasculin.getSexe().
                        concat(disciplineBeanQA.getNom().concat(disciplineBeanQA.getType()))));
        epreuveBeanQAJuniorFeminin.setCategorie(categorieBeanJuniorFeminin);
        epreuveBeanQAJuniorFeminin.setDiscipline(disciplineBeanQA);
        epreuveBeanQAJuniorFeminin.setId(
                categorieBeanJuniorFeminin.getNom().concat(categorieBeanJuniorFeminin.getSexe().
                        concat(disciplineBeanQA.getNom().concat(disciplineBeanQA.getType()))));
        epreuveBeanQAJuniorMasculin.setCategorie(categorieBeanJuniorMasculin);
        epreuveBeanQAJuniorMasculin.setDiscipline(disciplineBeanQA);
        epreuveBeanQAJuniorMasculin.setId(
                categorieBeanJuniorMasculin.getNom().concat(categorieBeanJuniorMasculin.getSexe().
                        concat(disciplineBeanQA.getNom().concat(disciplineBeanQA.getType()))));
        epreuveBeanQASeniorFeminin.setCategorie(categorieBeanSeniorFeminin);
        epreuveBeanQASeniorFeminin.setDiscipline(disciplineBeanQA);
        epreuveBeanQASeniorFeminin.setId(
                categorieBeanSeniorFeminin.getNom().concat(categorieBeanSeniorFeminin.getSexe().
                        concat(disciplineBeanQA.getNom().concat(disciplineBeanQA.getType()))));
        epreuveBeanQASeniorMasculin.setCategorie(categorieBeanSeniorMasculin);
        epreuveBeanQASeniorMasculin.setDiscipline(disciplineBeanQA);
        epreuveBeanQASeniorMasculin.setId(
                categorieBeanSeniorMasculin.getNom().concat(categorieBeanSeniorMasculin.getSexe().
                        concat(disciplineBeanQA.getNom().concat(disciplineBeanQA.getType()))));

        club1.setNom("Club1");
        club1.setIdentifiant("Club1");
        club2.setNom("Club2");
        club2.setIdentifiant("Club2");
        club3.setNom("Club3");
        club3.setIdentifiant("Club3");
        club4.setNom("Club4");
        club4.setIdentifiant("Club4");

        generateEleves(club1);
        generateEleves(club2);
        generateEleves(club3);
        generateEleves(club4);
        generateParticipants();
    }

    @Test
    public void controlModel() {
        //control nb Club
        Assert.assertEquals(4, competitionBean.getClubs().size());

        //control nb Eleve Club1
        Assert.assertEquals(30, competitionBean.getClubByIdentifiant("Club1").getEleves().size());

        //control nb Participant
        Assert.assertEquals(240, competitionBean.getParticipants().size());

        //control nb Participant Epreuve QMNCadetMasculin
        Assert.assertEquals(20, competitionBean.getParticipantByEpreuve(epreuveBeanQMNCadetMasculin).size());
    }

    @Test
    public void controlClassements() {
        ObservableList<ParticipantBean> participantBeen =
                competitionBean.getParticipantByEpreuve(epreuveBeanQMNCadetMasculin);
        epreuveBeanQMNCadetMasculin.setEtat("Termine");

        setNoteToParticipant(participantBeen.get(0), 10D);
        setNoteToParticipant(participantBeen.get(1), 11D);
        setNoteToParticipant(participantBeen.get(2), 12D);
        setNoteToParticipant(participantBeen.get(3), 10D);
        setNoteToParticipant(participantBeen.get(4), 13D);
        setNoteToParticipant(participantBeen.get(5), 9D);
        setNoteToParticipant(participantBeen.get(6), 14D);
        setNoteToParticipant(participantBeen.get(7), 16D);
        setNoteToParticipant(participantBeen.get(8), 16D);
        setNoteToParticipant(participantBeen.get(9), 12D);
        setNoteToParticipant(participantBeen.get(10), 15D);
        setNoteToParticipant(participantBeen.get(11), 14D);
        setNoteToParticipant(participantBeen.get(12), 13D);
        setNoteToParticipant(participantBeen.get(13), 18D);
        setNoteToParticipant(participantBeen.get(14), 18D);
        setNoteToParticipant(participantBeen.get(15), 19D);
        setNoteToParticipant(participantBeen.get(16), 12D);
        setNoteToParticipant(participantBeen.get(17), 13D);
        setNoteToParticipant(participantBeen.get(18), 13D);
        setNoteToParticipant(participantBeen.get(19), 12D);

        ClassementHelper.computeClassementAutoEpreuveTechnique(participantBeen);
        Assert.assertEquals(1, participantBeen.get(15).getClassementAuto().intValue());
        Assert.assertEquals(2, participantBeen.get(13).getClassementAuto().intValue());
        Assert.assertEquals(2, participantBeen.get(14).getClassementAuto().intValue());
        Assert.assertEquals(4, participantBeen.get(7).getClassementAuto().intValue());
        Assert.assertEquals(4, participantBeen.get(8).getClassementAuto().intValue());
        Assert.assertEquals(6, participantBeen.get(10).getClassementAuto().intValue());
        Assert.assertEquals(7, participantBeen.get(6).getClassementAuto().intValue());
        Assert.assertEquals(7, participantBeen.get(11).getClassementAuto().intValue());
        Assert.assertEquals(9, participantBeen.get(4).getClassementAuto().intValue());
        Assert.assertEquals(9, participantBeen.get(12).getClassementAuto().intValue());
        Assert.assertEquals(9, participantBeen.get(17).getClassementAuto().intValue());
        Assert.assertEquals(9, participantBeen.get(18).getClassementAuto().intValue());
        Assert.assertEquals(13, participantBeen.get(2).getClassementAuto().intValue());
        Assert.assertEquals(13, participantBeen.get(9).getClassementAuto().intValue());
        Assert.assertEquals(13, participantBeen.get(16).getClassementAuto().intValue());
        Assert.assertEquals(13, participantBeen.get(19).getClassementAuto().intValue());
        Assert.assertEquals(17, participantBeen.get(1).getClassementAuto().intValue());
        Assert.assertEquals(18, participantBeen.get(0).getClassementAuto().intValue());
        Assert.assertEquals(18, participantBeen.get(3).getClassementAuto().intValue());
        Assert.assertEquals(20, participantBeen.get(5).getClassementAuto().intValue());

        ClassementHelper.computeClassementClubs(competitionBean);
        ClassementHelper.computeClassementGeneralClub(competitionBean.getClubs());
        Assert.assertEquals(1, competitionBean.getClubs().get(2).getClassementGeneral().intValue());
        Assert.assertEquals(2, competitionBean.getClubs().get(3).getClassementGeneral().intValue());
        Assert.assertEquals(3, competitionBean.getClubs().get(1).getClassementGeneral().intValue());
        Assert.assertEquals(4, competitionBean.getClubs().get(0).getClassementGeneral().intValue());

        ClassementHelper.computeClassementTechniqueClub(competitionBean.getClubs());
        Assert.assertEquals(1, competitionBean.getClubs().get(2).getClassementTechnique().intValue());
        Assert.assertEquals(2, competitionBean.getClubs().get(3).getClassementTechnique().intValue());
        Assert.assertEquals(3, competitionBean.getClubs().get(1).getClassementTechnique().intValue());
        Assert.assertEquals(4, competitionBean.getClubs().get(0).getClassementTechnique().intValue());
    }

    private void setNoteToParticipant(ParticipantBean participantBean, Double note) {
        participantBean.setNote1(note);
        participantBean.setNote2(note);
        participantBean.setNote3(note);
        participantBean.setNote4(note);
        participantBean.setNote5(note);
        participantBean.setNoteTotal(note);
    }

    private void generateEleves(ClubBean clubBean) {
        ObservableList<EleveBean> eleves = FXCollections.observableArrayList();
        //Cadet Masculin
        for (int i=1; i<=5; i++) {
            EleveBean eleveBean = generateEleve(i, clubBean.getIdentifiant(), "Masculin", "Cadet",
                    FXCollections.<String>observableArrayList("Quyen Main Nue", "Quyen Arme"), "60", "15" );
            eleves.add(eleveBean);
        }
        //Cadet Féminin
        for (int i=6; i<=10; i++) {
            EleveBean eleveBean = generateEleve(i, clubBean.getIdentifiant(), "Féminin", "Cadet",
                    FXCollections.<String>observableArrayList("Quyen Main Nue", "Quyen Arme"), "60", "15" );
            eleves.add(eleveBean);
        }
        //Junior Masculin
        for (int i=11; i<=15; i++) {
            EleveBean eleveBean = generateEleve(i, clubBean.getIdentifiant(), "Masculin", "Junior",
                    FXCollections.<String>observableArrayList("Quyen Main Nue", "Quyen Arme"), "60", "17" );
            eleves.add(eleveBean);
        }
        //Junior Féminin
        for (int i=16; i<=20; i++) {
            EleveBean eleveBean = generateEleve(i, clubBean.getIdentifiant(), "Féminin", "Junior",
                    FXCollections.<String>observableArrayList("Quyen Main Nue", "Quyen Arme"), "60", "17" );
            eleves.add(eleveBean);
        }
        //Senior Masculin
        for (int i=21; i<=25; i++) {
            EleveBean eleveBean = generateEleve(i, clubBean.getIdentifiant(), "Masculin", "Senior",
                    FXCollections.<String>observableArrayList("Quyen Main Nue", "Quyen Arme"), "70", "20" );
            eleves.add(eleveBean);
        }
        //Senior Féminin
        for (int i=26; i<=30; i++) {
            EleveBean eleveBean = generateEleve(i, clubBean.getIdentifiant(), "Féminin", "Senior",
                    FXCollections.<String>observableArrayList("Quyen Main Nue", "Quyen Arme"), "65", "20" );
            eleves.add(eleveBean);
        }

        clubBean.setEleves(eleves);
    }

    private void generateParticipants() {
        for (ClubBean clubBean : competitionBean.getClubs()) {
            for (EleveBean eleveBean : clubBean.getEleves()) {
                ParticipantHelper.createParticipantsFromEleveBean(competitionBean, eleveBean, clubBean);
            }
        }
    }

    private EleveBean generateEleve(int index, String clubIdentifiant, String sexe, String categorie,
            ObservableList<String> epreuves, String poids, String age) {
        EleveBean eleveBean = new EleveBean();
        eleveBean.setLicence(clubIdentifiant.concat("000").concat(String.valueOf(index)));
        eleveBean.setNom(clubIdentifiant.concat("Nom").concat(String.valueOf(index)));
        eleveBean.setPrenom(clubIdentifiant.concat("Prenom").concat(String.valueOf(index)));
        eleveBean.setAge(age);
        eleveBean.setCategorie(categorie);
        eleveBean.setSexe(sexe);
        eleveBean.setEpreuves(epreuves);
        eleveBean.setPresence(true);
        eleveBean.setPoids(poids);

        return eleveBean;
    }
}
