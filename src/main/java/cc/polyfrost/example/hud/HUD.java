package cc.polyfrost.example.hud;

import cc.polyfrost.example.config.Config;
import cc.polyfrost.oneconfig.hud.SingleTextHud;

/**
 * An example OneConfig HUD that is started in the config and displays text.
 *
 * @see Config#hud
 */
public class HUD extends SingleTextHud {
    public HUD() {
        super("Test", true);
    }

    @Override
    public String getText(boolean example) {
        return "I'm an example HUD";
    }
}
