package com.hw.weather.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"city", "temp","date"})})
public class WeatherCity {

@PrimaryKey(autoGenerate = true)
    public long id;
@ColumnInfo(name = "city")
    public String city;
@ColumnInfo(name = "temp")
    public String temp;
@ColumnInfo(name = "date")
    public String date;
}



