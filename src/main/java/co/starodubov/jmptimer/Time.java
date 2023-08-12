package co.starodubov.jmptimer;

public record Time(int min, int sec) {

    public String getTime() {
        return min + ":" + sec;
    }

    public Time dec() {
        var curSec = sec - 1;
        if (curSec < 0 && min <= 0) {
            return new Time(0,0);
        }

        if (curSec < 0) {
            return new Time(min - 1, 59);
        }

        return new Time(min, sec - 1);
    }

    public boolean isZero() {
        return min == 0 && sec == 0;
    }

}

