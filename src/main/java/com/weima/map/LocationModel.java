package com.weima.map;

public class LocationModel {
	private String latitude;
	private String longitude;
	private String city_level;
    private String formatted_address = null;
    private String country = null;
    private String province = null;
    private String city = null;
    private String district = null;
    private String town = null;
    private String adcode = null;
    private String street = null;
    private String street_number = null;
    private String direction = null;
    private String distance = null;

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet_number() {
        return street_number;
    }

    public void setStreet_number(String street_number) {
        this.street_number = street_number;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }


    @Override
    public String toString() {
    	String result = formatted_address+","+country+","+province+","+city+","+district+","
    +town+","+adcode+","+street+","+street_number+","+direction+","+distance;
//        return "LocationModel{" +
//                "formatted_address='" + formatted_address + '\'' +
//                ", country='" + country + '\'' +
//                ", province='" + province + '\'' +
//                ", city='" + city + '\'' +
//                ", district='" + district + '\'' +
//                ", town='" + town + '\'' +
//                ", adcode='" + adcode + '\'' +
//                ", street='" + street + '\'' +
//                ", street_number='" + street_number + '\'' +
//                ", direction='" + direction + '\'' +
//                ", distance='" + distance + '\'' +
//                '}';
    	return result;
    }

    
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getCity_level() {
		return city_level;
	}

	public void setCity_level(String city_level) {
		this.city_level = city_level;
	}
}
