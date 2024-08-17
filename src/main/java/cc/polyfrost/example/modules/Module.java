package cc.polyfrost.example.modules;

import net.minecraftforge.common.MinecraftForge;

public class Module {
    private boolean toggled;

    public Module() {
        toggled = false;
    }

    public boolean isToggled() {
        return toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;

        if (this.toggled) onEnable();
        else onDisable();

        onToggled();
    }

    public void toggle() {
        toggled = !toggled;

        if (toggled) onEnable();
        else onDisable();

        onToggled();
    }

    public void onToggled() {

    }

    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }
}

