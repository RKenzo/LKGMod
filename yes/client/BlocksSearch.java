package cc.polyfrost.example.modules.gsabmodule.client;


import cc.polyfrost.example.modules.gsabmodule.reference.OreInfo;

import java.util.ArrayList;
import java.util.List;

public class BlocksSearch {
    public static List<OreInfo> searchList = new ArrayList(); // List of ores/blocks to search for.

    public static void add(String oreIdent, String name, int[] color) // Takes a string of id:meta or oreName to add to our search list.
    {
        oreIdent = oreIdent.replaceAll("\\p{C}", "?");
        int id = 0;
        int meta = 0;

        if (oreIdent.contains(":")) // Hopefully a proper id:meta string.
        {
            String[] splitArray = oreIdent.split(":");

            if (splitArray.length != 2) {
                //// // System.out.println( String.format( "Can't add %s to searchList. Invalid format.", oreIdent ) );
                String notify = String.format("[LKGMOD] %s is not a valid identifier. Try id:meta (example 1:0 for stone) or oreName (example oreDiamond or mossyStone)", oreIdent);
                // // // System.out.println(notify);
                return;
            }

            try {
                id = Integer.parseInt(splitArray[0]);
                meta = Integer.parseInt(splitArray[1]);
            } catch (NumberFormatException e) { // TODO: Some oredict ores are mod:block for some reason...
                //// // System.out.println( String.format( "%s is not a valid id:meta format.", oreIdent ) );
                String notify = String.format("[LKGMOD] %s contains data other than numbers and the colon. Failed to add.", oreIdent);
                // // // System.out.println(notify);
                return;
            }

        } else {
            try {
                id = Integer.parseInt(oreIdent);
                meta = 0;
            } catch (NumberFormatException e) {
                String notify = "[LKGMOD] Doesn't support in-game additions to the ore dictionary yet.. Failed to add.";
                // // // System.out.println(notify);
                return;
            }

        }
        //// // System.out.println( String.format( "Adding ore: %s", oreIdent ) );
        BlocksSearch.searchList.add(new OreInfo(name, id, meta, color, true));
        String notify = String.format("[LKGMOD] successfully added %s.", oreIdent);
        // // // System.out.println(notify);
    }

    public static List<OreInfo> get() // Return the searchList, create it if needed.
    {
        if (searchList.isEmpty()) {
            BlocksSearch.add("95:1", "Amber", new int[]{255, 0, 0});
        }
        return searchList;
    }

    public static void remove(String oreIdent) {
        oreIdent = oreIdent.replaceAll("\\p{C}", "?");
        int id = 0;
        int meta = 0;

        if (oreIdent.contains(":")) {
            String[] splitArray = oreIdent.split(":");

            if (splitArray.length != 2) {
                String notify = String.format("[LKGMOD] %s is not a valid identifier. Try id:meta (example 1:0 for stone) or oreName (example oreDiamond or mossyStone)", oreIdent);
                // // // System.out.println(notify);
                return;
            }

            try {
                id = Integer.parseInt(splitArray[0]);
                meta = Integer.parseInt(splitArray[1]);
            } catch (NumberFormatException e) {
                String notify = String.format("[LKGMOD] %s contains data other than numbers and the colon. Failed to remove.", oreIdent);
//                // // // System.out.println(notify);
                return;
            }
        } else {
            try {
                id = Integer.parseInt(oreIdent);
                meta = 0;
            } catch (NumberFormatException e) {
                String notify = "[LKGMOD] Doesn't support in-game removals from the ore dictionary yet. Failed to remove.";
//                // // // System.out.println(notify);
                return;
            }
        }

        final int finalId = id; // Tornar id final
        final int finalMeta = meta; // Tornar meta final

        // Remova o minério da lista searchList
        searchList.removeIf(oreInfo -> oreInfo.id == finalId && oreInfo.meta == finalMeta);
        String notify = String.format("[LKGMOD] Removed %s.", oreIdent);
//        // // // System.out.println(notify);
    }

    public static void clear() {
        searchList.clear();
        String notify = "[LKGMOD] Cleared all entries from the search list.";
        // // // System.out.println(notify);
    }

}
