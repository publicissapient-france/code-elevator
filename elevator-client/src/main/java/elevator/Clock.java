package elevator;

import java.util.Observable;

public class Clock extends Observable {

    public void tick() {
        setChanged();
        notifyObservers();
    }

}
