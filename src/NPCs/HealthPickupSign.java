package NPCs;

import Utils.Point;

public class HealthPickupSign extends Sign {

    public HealthPickupSign(Point location) {
        super(location, "BlankSigns.png", "If you are missing health after taking a hit use the health pickups\n to recover health. They look like hearts.");
    }
}
