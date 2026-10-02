package com.aim.project.ssp.instance.reader;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.aim.project.ssp.instance.Location;
import com.aim.project.ssp.instance.SSPInstance;
import com.aim.project.ssp.interfaces.SSPInstanceInterface;
import com.aim.project.ssp.interfaces.SSPInstanceReaderInterface;

/**
 * @author Warren G. Jackson
 * @since 17/03/2025
 *
 */
public class SSPInstanceReader implements SSPInstanceReaderInterface {

    @Override
    public SSPInstanceInterface readSSPInstance(Path path, Random random) {
        try {
            List<String> lines = Files.readAllLines(path);

            int hotelX = 0, hotelY = 0;
            int airportX = 0, airportY = 0;
            List<Location> poiList = new ArrayList<>();

            int i = 0;
            while (i < lines.size()) {
                String line = lines.get(i).trim();

                if (line.equals("HOTEL_LOCATION")) {
                    String[] coords = lines.get(++i).trim().split("\\s+");
                    hotelX = Integer.parseInt(coords[0]);
                    hotelY = Integer.parseInt(coords[1]);
                } else if (line.equals("AIRPORT_LOCATION")) {
                    String[] coords = lines.get(++i).trim().split("\\s+");
                    airportX = Integer.parseInt(coords[0]);
                    airportY = Integer.parseInt(coords[1]);
                } else if (line.equals("POINTS_OF_INTEREST")) {
                    i++;
                    while (i < lines.size() && !lines.get(i).trim().equals("EOF")) {
                        String[] coords = lines.get(i).trim().split("\\s+");
                        poiList.add(new Location(
                                Integer.parseInt(coords[0]),
                                Integer.parseInt(coords[1])
                        ));
                        i++;
                    }
                }
                i++;
            }

            // Convert POIs to array
            Location[] aoLocations = poiList.toArray(new Location[0]);

            // Create hotel and airport Location objects
            Location oHotel = new Location(hotelX, hotelY);
            Location oAirport = new Location(airportX, airportY);

            return new SSPInstance(aoLocations.length, aoLocations, oHotel, oAirport, random);

        } catch (IOException e) {
            System.err.println("Error reading SSP instance: " + path);
            e.printStackTrace();
            return null;
        }
    }
}