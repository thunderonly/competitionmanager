package fr.csmb.competition.type;

/**
 * Created by Administrateur on 24/11/15.
 */
public enum CategorieEnum {

    PUPILLES("Pupilles", 0),
    BENJAMINS("Benjamins", 1),
    MINIMES("Minimes", 2),
    CADETS("Cadets", 3),
    JUNIORS("Juniors", 4),
    SENIORS("Séniors", 5),
    VETERANS("Vétérans", 6);

    private String name;
    private Integer value;

    private CategorieEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }

    public static Integer getValueByName(String name) {
        for (CategorieEnum categorieEnum : CategorieEnum.values()) {
            if (categorieEnum.getName().equals(name)) {
                return categorieEnum.getValue();
            }
        }
        return 0;
    }
}
