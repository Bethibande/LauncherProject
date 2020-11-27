package de.bethibande.launcher.ui;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class UiResourceContainer {

    @Getter
    private final List<UiResource> resources = new ArrayList<>();

    public void addResource(String id, Object value) {
        this.resources.add(new UiResource(id, value));
    }

    public void addResource(UiResource r) {
        this.resources.add(r);
    }

    public UiResource getResource(String id) {
        for(UiResource res : this.resources) {
            if(res.getId().equals(id)) return res;
        }
        return null;
    }

    public void removeResource(String id) {
         int i = 0;
         while(i < this.resources.size()) {
             UiResource res = this.resources.get(i);
             if(res != null) {
                 if(res.getId().equals(id)) {
                     this.resources.remove(res);
                     break;
                 }
             }
             i++;
         }
    }

}
