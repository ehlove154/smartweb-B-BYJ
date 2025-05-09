package my.study.animal.component;

public enum Gender {
    FEMALE("Female"),
    MALE("Male"),
    NONE("None Binary");

    private final String displayText;

    Gender(String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayText() {
        return displayText;
    }
}
