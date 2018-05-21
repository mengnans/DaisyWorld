package entity;

import params.Params;

import java.util.ArrayList;

/**
 * Mengnan Shi 802123
 * Mohammed Sharukh Syed 896508
 * @create 2018-05-03-10:58
 */

public class Location {
    private int x;
    private int y;
    private static Location[] locationAdjustList;

    static {
        locationAdjustList = new Location[8];
        locationAdjustList[0] = new Location(1, -1);
        locationAdjustList[1] = new Location(1, 0);
        locationAdjustList[2] = new Location(1, 1);
        locationAdjustList[3] = new Location(0, -1);
        locationAdjustList[4] = new Location(0, 1);
        locationAdjustList[5] = new Location(-1, -1);
        locationAdjustList[6] = new Location(-1, 0);
        locationAdjustList[7] = new Location(-1, 1);
    }

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private Location addLocationAdjust(Location locationAdjust) {
        int x = this.x + locationAdjust.x;
        int y = this.y + locationAdjust.y;
        return new Location(x, y);
    }

    private boolean checkPossibleNeighbourLocation() {
        if (this.x >= 0 && this.x < Params.WORLD_WIDTH &&
                this.y >= 0 && this.y < Params.WORLD_HEIGHT) {
            return true;
        } else {
            return false;
        }

    }

    public ArrayList<Location> getAllNeighbourLocations() {
        ArrayList<Location> neighbourLocations = new ArrayList();
        for (int i = 0; i < locationAdjustList.length; i++) {
            Location locationAdjust = locationAdjustList[i];
            Location possibleNeighbourLocation
                    = addLocationAdjust(locationAdjust);
            if (possibleNeighbourLocation
                    .checkPossibleNeighbourLocation()) {
                neighbourLocations.add(possibleNeighbourLocation);
            }
        }
        return neighbourLocations;
    }

}
