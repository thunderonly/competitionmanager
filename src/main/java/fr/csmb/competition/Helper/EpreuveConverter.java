package fr.csmb.competition.Helper;

import fr.csmb.competition.model.CategorieBean;
import fr.csmb.competition.model.DisciplineBean;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.xml.model.Categorie;
import fr.csmb.competition.xml.model.Discipline;
import fr.csmb.competition.xml.model.Epreuve;

/**
 * Created by Administrateur on 25/11/14.
 */
public class EpreuveConverter {

    public static EpreuveBean convertEpreuveToEpreuveBean(Epreuve epreuve) {
        EpreuveBean epreuveBean = new EpreuveBean();
        convertEpreuveToEpreuveBean(epreuve, epreuveBean);
        return epreuveBean;
    }

    public static void convertEpreuveToEpreuveBean(Epreuve epreuve, EpreuveBean epreuveBean) {
        epreuveBean.setEtat(epreuve.getEtatEpreuve());

        CategorieBean categorieBean = new CategorieBean(epreuve.getCategorie().getNomCategorie());
        categorieBean.setSexe(epreuve.getCategorie().getTypeCategorie());
        epreuveBean.setCategorie(categorieBean);

        DisciplineBean disciplineBean = new DisciplineBean(epreuve.getDiscipline().getNom(), epreuve.getDiscipline().getType());
        disciplineBean.setType(epreuve.getDiscipline().getType());
        epreuveBean.setDiscipline(disciplineBean);

        epreuveBean.setDetailEpreuve(DetailEpreuveConverter.convertDetailEpreuveToDetailEpreuveBean(epreuve
                .getDetailEpreuve()));
        epreuveBean.setId(epreuve.getId());

        epreuveBean.setLabel(epreuve.getLabelEpreuve());
    }

    public static Epreuve convertEpreuveBeanToEpreuve(EpreuveBean epreuveBean) {
        Discipline discipline = new Discipline(epreuveBean.getDiscipline().getNom(), epreuveBean.getDiscipline().getType());
        Categorie categorie = new Categorie(epreuveBean.getCategorie().getNom(), epreuveBean.getCategorie().getSexe());
        Epreuve epreuve = new Epreuve(categorie, discipline);
        epreuve.setEtatEpreuve(epreuveBean.getEtat());
        epreuve.setLabelEpreuve(epreuveBean.getLabel());
        epreuve.setDetailEpreuve(DetailEpreuveConverter.convertDetailEpreuveBeanToDetailEpreuve(epreuveBean.getDetailEpreuve()));
        epreuve.setId(epreuveBean.getId());
        return epreuve;
    }
}
