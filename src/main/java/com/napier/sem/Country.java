package com.napier.sem;

/**
 * Represents a country with population data
 */
public class Country
{
    /**
     * Country code
     */
    public String code;

    /**
     * Country name
     */
    public String name;

    /**
     * Continent
     */
    public String continent;

    /**
     * Region
     */
    public String region;

    /**
     * Population
     */
    public long population;

    /**
     * Capital city
     */
    public String capital;

    /**
     * Constructor
     */
    public Country(String code, String name, String continent, String region, long population, String capital)
    {
        this.code = code;
        this.name = name;
        this.continent = continent;
        this.region = region;
        this.population = population;
        this.capital = capital;
    }

    /**
     * Format population with commas
     */
    public String getFormattedPopulation()
    {
        return String.format("%,d", population);
    }

    /**
     * String representation
     */
    @Override
    public String toString()
    {
        return String.format("%-4s %-45s %-15s %-25s %15s %-35s", 
                code, name, continent, region, getFormattedPopulation(), capital);
    }
}