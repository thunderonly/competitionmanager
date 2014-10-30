package fr.csmb.competition.xml.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrateur on 13/10/14.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Eleve {

    @XmlElement(name="licenceEleve")
    private String licenceEleve;
    @XmlElement(name="nomEleve")
    private String nomEleve;
    @XmlElement(name="prenomEleve")
    private String prenomEleve;
    @XmlElement(name="ageEleve")
    private String ageEleve;
    @XmlElement(name="categorieEleve")
    private String categorieEleve;
    @XmlElement(name="sexeEleve")
    private String sexeEleve;
    @XmlElement(name="poidsEleve")
    private String poidsEleve;
    @XmlElementWrapper(name="epreuvesEleve")
    @XmlElement(name="epreuveEleve")
    private List<String> epreuvesEleves;


    public Eleve(String nomEleve, String prenomEleve) {
        this.nomEleve = nomEleve;
        this.prenomEleve = prenomEleve;
        this.epreuvesEleves = new ArrayList<String>();
    }

    public Eleve() {
        this(null, null);
    }

    public String getLicenceEleve() {
        return licenceEleve;
    }

    public void setLicenceEleve(String licenceEleve) {
        this.licenceEleve = licenceEleve;
    }

    public String getNomEleve() {
        return nomEleve;
    }

    public void setNomEleve(String nomEleve) {
        this.nomEleve = nomEleve;
    }

    public String getPrenomEleve() {
        return prenomEleve;
    }

    public void setPrenomEleve(String prenomEleve) {
        this.prenomEleve = prenomEleve;
    }

    public String getAgeEleve() {
        return ageEleve;
    }

    public void setAgeEleve(String ageEleve) {
        this.ageEleve = ageEleve;
    }

    public String getCategorieEleve() {
        return categorieEleve;
    }

    public void setCategorieEleve(String categorieEleve) {
        this.categorieEleve = categorieEleve;
    }

    public String getSexeEleve() {
        return sexeEleve;
    }

    public void setSexeEleve(String sexeEleve) {
        this.sexeEleve = sexeEleve;
    }

    public String getPoidsEleve() {
        return poidsEleve;
    }

    public void setPoidsEleve(String poidsEleve) {
        this.poidsEleve = poidsEleve;
    }

    public List<String> getEpreuvesEleves() {
        return epreuvesEleves;
    }

    public void setEpreuvesEleves(List<String> epreuvesEleves) {
        this.epreuvesEleves = epreuvesEleves;
    }
}
