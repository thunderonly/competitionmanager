/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.component.grid.globalvision;

import java.util.*;

import fr.csmb.competition.type.CategorieEnum;
import fr.csmb.competition.xml.model.Participant;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class GlobalVision {
    private String disciplineName;
    private Map<String, Map<String, List<Participant>>> typeCategories;

    public GlobalVision(String name) {
        this.disciplineName = name;
        this.typeCategories = new LinkedHashMap<String, Map<String, List<Participant>>>();
    }

    public void setDisciplineName(String disciplineName) {
        this.disciplineName = disciplineName;
    }

    public void setTypeCategories(Map<String, Map<String, List<Participant>>> typeCategories) {
        this.typeCategories = typeCategories;
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public Map<String, Map<String, List<Participant>>> getTypeCategories() {
        return typeCategories;
    }

    public void sortTypeCategories() {
        Map<String, Map<String, List<Participant>>> typeCategoriesTmp =
                new LinkedHashMap<String, Map<String, List<Participant>>>();
        TypeCategoriesComparator typeCategoriesComparator = new TypeCategoriesComparator(typeCategoriesTmp);
        TreeMap<String, Map<String, List<Participant>>> result =
                new TreeMap<String, Map<String, List<Participant>>>(typeCategoriesComparator);
        for (String key : typeCategories.keySet()) {
            Map<String, List<Participant>> categoriesSexe = new HashMap<String, List<Participant>>();
            if (typeCategories.get(key) != null) {
                categoriesSexe = typeCategories.get(key);
            }
            typeCategoriesTmp.put(key, categoriesSexe);
        }
        result.putAll(typeCategoriesTmp);
        typeCategories.clear();
        Iterator<String> iterator = result.keySet().iterator();
        while (iterator.hasNext()) {
            String keyCategorie = iterator.next();
            typeCategories.put(keyCategorie, typeCategoriesTmp.get(keyCategorie));
        }
    }

    private class TypeCategoriesComparator implements Comparator<String> {
        Map<String, Map<String, List<Participant>>> base;
        public TypeCategoriesComparator(Map<String, Map<String, List<Participant>>> baseParam) {
            base = baseParam;
        }

        public int compare (String a, String b) {
            Integer valueA = CategorieEnum.getValueByName(a);
            Integer valueB = CategorieEnum.getValueByName(b);

            if (valueA < valueB) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
