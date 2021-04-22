import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;


public class Player {
    private int orientation;
    private int room;
    private final Dictionary<String, Object> objects;

    public Player(int orientation, int room) {
        this.orientation = orientation;
        this.room = room;
        this.objects = new Hashtable<>();
    }

    public int getOrientation() {
        return orientation;
    }

    public int getRoom() {
        return room;
    }

    public void turn(String direction) {
        switch (direction) {
            case "left":
                orientation = (orientation + 3) % 4;
                break;
            case "right":
                orientation = (orientation + 1) % 4;
                break;
            default:
                throw new IllegalArgumentException("Direction not understood.\n");
        }
    }

    public int move(Room room) {
        this.room = room.move(orientation);
        return this.room;
    }

    public void coinsToGold() {
        Object.objectsRemove(objects, "coin", 5);
        Object.objectsAdd(objects, new Currency("gold", 1));
    }

    public void coinsToDiamond() {
        Object.objectsRemove(objects, "coin", 15);
        Object.objectsAdd(objects, new Currency("diamond", 1));
    }

    public void goldToDiamond() {
        Object.objectsRemove(objects, "gold", 3);
        Object.objectsAdd(objects, new Currency("diamond", 1));
    }

    public void unlock(LockableObject lockableObject) {
        Object keys = objects.get("key");
        if (keys != null) {
            Object key = keys.get(1);
            if (lockableObject.unlock(key)) {
                keys.remove(1);
                if (keys.getQuantity() == 0) {
                    objects.remove("key");
                }
            }
        } else {
            throw new IllegalStateException("You do not carry any key\n");
        }
    }

    public void lock(LockableObject lockableObject) {
        Object keys = objects.get("key");
        if (keys != null) {
            Object key = keys.get(1);
            if (lockableObject.lock(key)) {
                keys.remove(1);
                if (keys.getQuantity() == 0) {
                    objects.remove("key");
                }
            }
        } else {
            throw new IllegalStateException("You do not carry any key\n");
        }
    }

    public void take(Room room, String object, int quantity) {
        Object.objectsAdd(objects, room.take(object, quantity));
    }

    public void take(Box box, String object, int quantity) {
        Object.objectsAdd(objects, box.take(object, quantity));
    }

    public void drop(Room room, String type, int quantity) {
        room.drop(Object.objectsRemove(objects, type, quantity));
    }

    public void drop(Box box, String type, int quantity) {
        box.drop(Object.objectsRemove(objects, type, quantity));
    }

    public void print() {
        if (objects.isEmpty()) {
            System.out.println("You do not carry anything.\n");
        } else {
            System.out.print("You carry ");
            Enumeration<String> keys = objects.keys();
            String key;
            if (objects.size() > 1) {
                for (int i = 0; i < objects.size() - 1; i++) {
                    key = keys.nextElement();
                    System.out.print(Object.quantity(objects.get(key).getQuantity(), key) + ", ");
                }
                System.out.print("and ");
            }
            key = keys.nextElement();
            System.out.println(Object.quantity(objects.get(key).getQuantity(), key) + ".\n");
        }
    }
}
