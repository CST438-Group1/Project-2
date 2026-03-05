package com.team1.f1_api.repository;

// Projection for counting distinct countries per continent

public interface ContinentCountView {
    String getRegion();
    long getCountryCount();
}
