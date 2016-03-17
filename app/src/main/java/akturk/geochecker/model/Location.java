package akturk.geochecker.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public final class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("id")
    private int mId;

    @SerializedName("name")
    private String  mName;

    @SerializedName("latitude")
    private long mLatitude;

    @SerializedName("longitude")
    private long mLongitude;

    @SerializedName("unlock")
    private boolean mUnlock;

    @SerializedName("range")
    private int mRange;

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public long getLatitude() {
        return mLatitude;
    }

    public long getLongitude() {
        return mLongitude;
    }

    public boolean isUnlock() {
        return mUnlock;
    }

    public int getRange() {
        return mRange;
    }
}
