package edu.gatech.seclass.jobcompare6300;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity
public class Location implements Parcelable {
    @ColumnInfo(name="location_id")
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String city;
    public String state;

    // Default constructor
    public Location() {
    }

    // Constructor
    public Location(String city, String state) {
        this.city = city;
        this.state = state;
    }

    // Parcelable constructor
    protected Location(Parcel in) {
        city = in.readString();
        state = in.readString();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(city);
        dest.writeString(state);
    }

    // getters
    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    // don't think we need setters rn
}
