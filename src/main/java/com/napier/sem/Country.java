package com.napier.sem;

// Simple class to represent a country
public class Country {
    public String code;
    public String name;
    public String continent;
    public String region;
    public long population;
    public String capital;

    public Country(String code, String name, String continent, String region, long population, String capital) {
        this.code = code;
        this.name = name;
        this.continent = continent;
        this.region = region;
        this.population = population;
        this.capital = capital;
    }
}
     */
    @Override
    public String toString()
    {
        return String.format("%-4s %-45s %-15s %-25s %15s %-35s", 
                code, name, continent, region, getFormattedPopulation(), capital);
    }
}