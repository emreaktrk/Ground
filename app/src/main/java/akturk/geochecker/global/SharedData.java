package akturk.geochecker.global;

public class SharedData {

    private static long sMinTime;
    private static long sMinDistance;

    public static long getMinTime() {
        return sMinTime;
    }

    public static void setMinTime(long minTime) {
        SharedData.sMinTime = minTime;
    }

    public static long getMinDistance() {
        return sMinDistance;
    }

    public static void setMinDistance(long minDistance) {
        SharedData.sMinDistance = minDistance;
    }
}
