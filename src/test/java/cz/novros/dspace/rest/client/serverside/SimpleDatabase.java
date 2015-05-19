package cz.novros.dspace.rest.client.serverside;

import org.dspace.rest.common.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rostislav on 11. 5. 2015.
 */
public class SimpleDatabase {
    private static int communitiesId = 0;
    private static int collectionsId = 0;
    private static int itemId = 0;
    private static int bitstreamId = 0;

    private static List<Community> communityList = new ArrayList<>();
    private static List<Collection> collectionList = new ArrayList<>();
    private static List<Item> itemList = new ArrayList<>();
    private static List<Bitstream> bitstreamList = new ArrayList<>();

    public static int handleIndex = 0;
    public static final String handle = "123456789/";

    public static Community addCommunity(Community object) {
        object.setID(communitiesId++);
        object.setHandle(handle+handleIndex++);
        communityList.add(object);
        return object;
    }

    public static Collection addCommunity(int communityId, Collection object) {
        object.setID(collectionsId++);
        object.setHandle(handle+handleIndex++);
        collectionList.add(object);
        return object;
    }

    public static Item addCommunity(int collection, Item object) {
        object.setID(itemId++);
        object.setHandle(handle+handleIndex++);
        itemList.add(object);
        return object;
    }

    public static Bitstream addCommunity(int item, Bitstream object) {
        object.setID(bitstreamId++);
        object.setHandle(handle+handleIndex++);
        bitstreamList.add(object);
        return object;
    }

}
