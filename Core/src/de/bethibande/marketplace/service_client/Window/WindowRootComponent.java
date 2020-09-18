package de.bethibande.marketplace.service_client.Window;

import lombok.Getter;
import lombok.Setter;

public class WindowRootComponent extends WindowComponent implements IWindowRootComponent {

    @Getter
    @Setter
    // do not override manually
    private IWindowControl activeControl;

}
