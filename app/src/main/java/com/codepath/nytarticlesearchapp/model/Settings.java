package com.codepath.nytarticlesearchapp.model;

import org.parceler.Parcel;

/**
 * Created by mallikaa on 11/18/16.
 */
@Parcel
public class Settings {
    String date;
    boolean cbSports;
    boolean cbArts;
    boolean cbFashion;
    String order;

    private static Settings settings = null;

    public Settings(String date, boolean cbSports, boolean cbArts, boolean cbFashion, int order) {
        this.date = date;
        this.cbSports = cbSports;
        this.cbArts = cbArts;
        this.cbFashion = cbFashion;
        this.order = new SortOrder(order).getSortOrder();
    }

    public Settings() {
        this.date = "";
        this.cbSports = false;
        this.cbArts = false;
        this.cbFashion = false;
        this.order = new SortOrder(-1).getSortOrder();
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isCbSports() {
        return cbSports;
    }

    public void setCbSports(boolean cbSports) {
        this.cbSports = cbSports;
    }

    public boolean isCbArts() {
        return cbArts;
    }

    public void setCbArts(boolean cbArts) {
        this.cbArts = cbArts;
    }

    public boolean isCbFashion() {
        return cbFashion;
    }

    public void setCbFashion(boolean cbFashion) {
        this.cbFashion = cbFashion;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public static Settings getSettings() {
        return settings;
    }

    public static void setSettings(Settings settings) {
        Settings.settings = settings;
    }

    public static Settings getSettingsInstance(String date, boolean cbSports, boolean cbArts, boolean cbFashion, int order) {
        if (settings == null) {
            settings = new Settings(date, cbSports, cbArts, cbFashion, order);
        }
        return settings;
    }

    public static Settings getSettingsInstance() {
        if (settings == null) {
            settings = new Settings();
        }
        return settings;
    }
}
