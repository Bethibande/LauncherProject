package de.bethibande.launcher.ui.windows;

import lombok.Getter;
import lombok.Setter;

public class UiRenderConfig implements IUiRenderConfig {

    @Getter
    @Setter
    private int fpsCap;
    @Getter
    @Setter
    private boolean antialiasingEnabled;

}
