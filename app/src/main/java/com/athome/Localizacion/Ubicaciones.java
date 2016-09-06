package com.athome.Localizacion;

/**
 * Created by Lady on 01/01/2016.
 */
public class Ubicaciones {
    private String mLabel;
    private String mIcon;
    private Double mLatitude;
    private Double mLongitude;

    public Ubicaciones(String label, String icon, Double latitude, Double longitude)
    {
        this.mLabel = label;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mIcon = icon;

    }
    /*public static final List<Ubicaciones> AmasDisponibles = new ArrayList<Ubicaciones>();
    static {
        AmasDisponibles.add(new Ubicaciones("Brasil", "icon1", Double.parseDouble("-28.5971788"), Double.parseDouble("-52.7309824")));
        AmasDisponibles.add(new Ubicaciones("United States", "icon2", Double.parseDouble("33.7266622"), Double.parseDouble("-87.1469829")));
        AmasDisponibles.add(new Ubicaciones("England", "icon4", Double.parseDouble("52.4435047"), Double.parseDouble("-3.4199249")));
        AmasDisponibles.add(new Ubicaciones("España", "icon5", Double.parseDouble("41.8728262"), Double.parseDouble("-0.2375882")));
    }*/



    public String getmLabel(){return mLabel;}
    public void setmLabel(String mLabel){this.mLabel = mLabel;}
    public String getmIcon(){return mIcon;}
    public void setmIcon(String icon){this.mIcon = icon;}
    public Double getmLatitude(){return mLatitude;}
    public void setmLatitude(Double mLatitude){this.mLatitude = mLatitude;}
    public Double getmLongitude(){ return mLongitude;}
    public void setmLongitude(Double mLongitude){this.mLongitude = mLongitude;}
}

