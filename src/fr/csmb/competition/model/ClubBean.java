package fr.csmb.competition.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * Created by Administrateur on 13/10/14.
 */
public class ClubBean implements Comparable<ClubBean> {

    private StringProperty identifiant;
    private StringProperty nom;
    private StringProperty responsable;
    private IntegerProperty totalTechnique;
    private IntegerProperty totalCombat;
    private IntegerProperty totalGeneral;
    private IntegerProperty classementTechnique;
    private IntegerProperty classementCombat;
    private IntegerProperty classementGeneral;
    private ListProperty<EleveBean> eleves;

    public ClubBean() {
        this.identifiant = new SimpleStringProperty();
        this.nom = new SimpleStringProperty();
        this.responsable = new SimpleStringProperty();
        this.totalCombat = new SimpleIntegerProperty();
        this.totalGeneral = new SimpleIntegerProperty();
        this.totalTechnique = new SimpleIntegerProperty();
        this.classementTechnique = new SimpleIntegerProperty();
        this.classementCombat = new SimpleIntegerProperty();
        this.classementGeneral = new SimpleIntegerProperty();
        this.eleves = new SimpleListProperty<EleveBean>();
        ObservableList<EleveBean> eleveBeans = FXCollections.observableArrayList();
        this.eleves.set(eleveBeans);
    }

    public String getIdentifiant() {
        return this.identifiant.get();
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant.set(identifiant);
    }

    public String getResponsable() {
        return this.responsable.get();
    }

    public void setResponsable(String responsable) {
        this.responsable.set(responsable);
    }

    public String getNom() {
        return this.nom.get();
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public Integer getTotalTechnique() {
        return this.totalTechnique.get();
    }

    public void setTotalTechnique(Integer totalTechnique) {
        this.totalTechnique.set(totalTechnique);
    }

    public Integer getTotalCombat() {
        return this.totalCombat.get();
    }

    public void setTotalCombat(Integer totalCombat) {
        this.totalCombat.set(totalCombat);
    }

    public Integer getTotalGeneral() {
        return this.totalGeneral.get();
    }

    public void setTotalGeneral(Integer totalGeneral) {
        this.totalGeneral.set(totalGeneral);
    }

    public Integer getClassementTechnique() {
        return this.classementTechnique.get();
    }

    public void setClassementTechnique(Integer classementTechnique) {
        this.classementTechnique.set(classementTechnique);
    }

    public Integer getClassementCombat() {
        return this.classementCombat.get();
    }

    public void setClassementCombat(Integer classementCombat) {
        this.classementCombat.set(classementCombat);
    }

    public Integer getClassementGeneral() {
        return this.classementGeneral.get();
    }

    public void setClassementGeneral(Integer classementGeneral) {
        this.classementGeneral.set(classementGeneral);
    }

    public ObservableList<EleveBean> getEleves() {
        return this.eleves.get();
    }

    public void setEleves(ObservableList<EleveBean> eleves) {
        this.eleves.set(eleves);
    }


    public StringProperty identifiantProperty() {
        return identifiant;
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public StringProperty responsableProperty() {
        return responsable;
    }

    public IntegerProperty totalTechniqueProperty() {
        return totalTechnique;
    }

    public IntegerProperty totalCombatProperty() {
        return totalCombat;
    }

    public IntegerProperty totalGeneralProperty() {
        return totalGeneral;
    }

    public IntegerProperty classementTechniqueProperty() {
        return classementTechnique;
    }

    public IntegerProperty classementCombatProperty() {
        return classementCombat;
    }

    public IntegerProperty classementGeneralProperty() {
        return classementGeneral;
    }

    public ListProperty<EleveBean> elevesProperty() {
        return eleves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClubBean)) return false;

        ClubBean clubBean = (ClubBean) o;

        if (!identifiant.get().equals(clubBean.identifiant.get())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return identifiant.get().hashCode();
    }

    @Override
    public int compareTo(ClubBean clubBean) {
        if (this.getTotalGeneral() > clubBean.getTotalGeneral()) {
            return -1;
        } else if (this.getTotalGeneral() < clubBean.getTotalGeneral()) {
            return 1;
        }
        return 0;
    }

    public EleveBean getEleveByLicence(String licence) {
        for (EleveBean eleveBean : getEleves()) {
            if (eleveBean.getLicence().equals(licence)) {
                return eleveBean;
            }
        }
        return null;
    }
}
