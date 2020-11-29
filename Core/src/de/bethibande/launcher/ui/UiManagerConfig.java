package de.bethibande.launcher.ui;

import de.bethibande.launcher.ui.components.UiComponent;
import de.bethibande.launcher.ui.drawable.UiDrawable;
import lombok.Getter;

import java.util.HashMap;

public class UiManagerConfig {

    @Getter
    private final HashMap<String, Class<? extends UiComponent>> xmlComponentAssociations = new HashMap<>();
    @Getter
    private final HashMap<String, Class<? extends UiDrawable>> xmlDrawableAssociations = new HashMap<>();
    @Getter
    private final HashMap<String, Integer> xmlIntegerReplacementValues = new HashMap<>();

    public void addXmlIntegerReplacementValue(String value, Integer i) {
        this.xmlIntegerReplacementValues.put(value, i);
    }

    public void addXmlComponentAssociation(String name, Class<? extends UiComponent> clazz) {
        this.xmlComponentAssociations.put(name, clazz);
    }

    public void addXmlDrawableAssociation(String name, Class<? extends UiDrawable> clazz) {
        this.xmlDrawableAssociations.put(name, clazz);
    }

}
