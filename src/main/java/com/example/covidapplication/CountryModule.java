package com.example.covidapplication;

public class CountryModule {

    String countryName,countryFlag;

    public CountryModule(String countryName, String countryFlag) {
        this.countryName = countryName;
        this.countryFlag = countryFlag;
    }

    public CountryModule() {
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryFlag() {
        return countryFlag;
    }

    public void setCountryFlag(String countryFlag) {
        this.countryFlag = countryFlag;
    }
}
