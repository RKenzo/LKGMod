package cc.polyfrost.example.config;

import cc.polyfrost.example.LKGModMain;
import cc.polyfrost.example.modules.gsabmodule.GSABModule;
//import cc.polyfrost.example.modules.teleport.AutoTeleport;
import cc.polyfrost.oneconfig.config.annotations.Checkbox;
import cc.polyfrost.oneconfig.config.annotations.KeyBind;
import cc.polyfrost.oneconfig.config.annotations.Number;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.libs.universal.UKeyboard;

/**
 * The main Config entrypoint that extends the Config type and inits the config options.
 * See <a href="https://docs.polyfrost.cc/oneconfig/config/adding-options">this link</a> for more config Options
 */
public class Config extends cc.polyfrost.oneconfig.config.Config {
    @KeyBind(
            name = "Glass Breaker"
    )
// using OneKeyBind to set the default key combo to Shift+S
    public static OneKeyBind keyBind = new OneKeyBind(UKeyboard.KEY_Z);

//    @KeyBind(
//            name = "Glass Breaker QUEUE CLEAR"
//    )
//// using OneKeyBind to set the default key combo to Shift+S
//    public static OneKeyBind gbQueueClear = new OneKeyBind(UKeyboard.KEY_X);

    @Checkbox(
            name = "Panel"
    )
    public static boolean isPanelOn = false;        // default value

//    @Checkbox(
//            name = "Auto OFF"
//    )
//    public static boolean autoOFF = true;        // default value


    @Checkbox(
            name = "Ruby"
    )
    public static boolean gemRuby = false;        // default value

    @Checkbox(
            name = "Amber"
    )
    public static boolean gemAmber = false;        // default value

    @Checkbox(
            name = "Amethyst"
    )
    public static boolean gemAmethyst = false;        // default value
    @Checkbox(
            name = "Jade"
    )
    public static boolean gemJade = false;        // default value
    @Checkbox(
            name = "Sapphire"
    )
    public static boolean gemSapphire = false;        // default value
    @Checkbox(
            name = "Topaz"
    )
    public static boolean gemTopaz = false;        // default value
    @Checkbox(
            name = "Jasper"
    )
    public static boolean gemJasper = false;        // default value
    @Checkbox(
            name = "Opal"
    )
    public static boolean gemOpal = false;        // default value
    @Checkbox(
            name = "Onyx"
    )
    public static boolean gemOnyx = false;        // default value
    @Checkbox(
            name = "Aquamarine"
    )
    public static boolean gemAquamarine = false;        // default value
    @Checkbox(
            name = "Citrine"
    )
    public static boolean gemCitrine = false;        // default value
    @Checkbox(
            name = "Peridot"
    )
    public static boolean gemPeridot = false;        // default value



    @Number(            name = "Radius",    // name of the component
            min = 0, max = 1000,        // min and max values (anything above/below is set to the max/min
            step = 1// each time the arrow is clicked it will increase/decrease by this amount
    )
    public static int radiusDefault = 5; // default value

//    @Number(            name = "Glass META",    // name of the component
//            min = 0, max = 15,        // min and max values (anything above/below is set to the max/min
//            step = 1// each time the arrow is clicked it will increase/decrease by this amount
//    )
//    public static int metaDefault = 13; // default value

    @Number(            name = "Smoothiness",    // name of the component
            min = 0.0001f, max = 1f       // min and max values (anything above/below is set to the max/min
    )
    public static float smoothinessDefault = 0.2f; // default value

    @Number(            name = "Fov Limit",    // name of the component
            min = 30f, max = 110f       // min and max values (anything above/below is set to the max/min
    )
    public static float fovLimit = 90f; // default value

//    @Number(            name = "Distance Block",    // name of the component
//            min = 1, max = 1000       // min and max values (anything above/below is set to the max/min
//    )
//    public static int distanceBlock = 6; // default value



    public Config() {
        super(new Mod(LKGModMain.NAME, ModType.UTIL_QOL), LKGModMain.MODID + ".json");
        initialize();
        GSABModule GSABModule = new GSABModule();
//        AutoTeleport autoTeleport = new AutoTeleport();
        registerKeyBind(keyBind, () -> {
            GSABModule.toggle();
        });
//        registerKeyBind(tpkeyBind, () -> {
//            if (glassBreaker.checkQueue()) {
////                System.out.println("true");
//                autoTeleport.ativarProximaCoordenada();
//            } else {
////                System.out.println("false");
//            }
////            autoTeleport.toggle();
//        });
    }
}

