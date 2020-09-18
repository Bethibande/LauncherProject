package de.bethibande.marketplace.service_client.Window;

// can also be seen as a scene component
public interface IWindowRootComponent extends IWindowComponent {

    // get the active window control, returns null if none selected
    IWindowControl getActiveControl();

}
