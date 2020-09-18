package de.bethibande.marketplace.service_client.Window;

// TODO: create implementation
public interface IWindowRenderer {

    // give the renderer the IWindowHandle of the IWindow it will be rendering
    void init(IWindowHandle handle);
    // get the IWindowHandle assigned to this renderer
    IWindowHandle getHandle();
    // get delta time (used for animations to be independent from fps cap, time elapsed since the last frame was drawn)
    float getDeltaTime();

}
