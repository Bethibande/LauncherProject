package de.bethibande.marketplace.service_client.Window;

import java.util.List;

public interface IWindowComponent {

    // add window component
    void add(IWindowComponent component);
    // remove window component
    List<IWindowComponent> getComponents();
    // get all the added IWindowComponents
    void remove(IWindowComponent component);
    // set component visible
    void show();
    // set component invisible
    void hide();
    // check whether the component is visible or not
    boolean isVisible();

}
