package utility;

/**
 * @author Mengnan Shi
 * @create 2018-05-03-11:20
 */

public class Ticker {
    private static final int TICK_START_INDEX = 0;
    private int tick;

    public Ticker(){
        this.tick = TICK_START_INDEX;
    }

    public void resetTick(){
        this.tick = TICK_START_INDEX;
    }

    public int getTick() {
        return tick;
    }

    public void tick(){
        this.tick ++;
    }

}
