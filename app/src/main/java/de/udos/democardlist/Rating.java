package de.udos.democardlist;

public class Rating {

    private float value;
    private String title;

    public Rating (float value, String title) {

        this.value = value;
        this.title = title;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
