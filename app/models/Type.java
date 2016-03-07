package models;

import java.util.Set;

/**
 * Created by Alexander on 04.02.2016.
 */
public enum Type {
    SINGLE_LINE_TEXT("Single line text"),
    CHECK_BOX("Check box"),
    TEXTAREA("Textarea"),
    RADIO_BUTTON("Radio button"),
    COMBO_BOX("Combo box"),
    SLIDER("Slider"),
    DATE("Date");

    private final String type;

    private Type(final String type) {
        this.type = type;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return type;
    }
}