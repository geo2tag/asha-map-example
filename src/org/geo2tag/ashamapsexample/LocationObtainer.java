package org.geo2tag.ashamapsexample;

import javax.microedition.location.Location;
import javax.microedition.location.LocationException;
import javax.microedition.location.LocationProvider;
import javax.microedition.location.QualifiedCoordinates;

import com.nokia.mid.location.LocationUtil;

public class LocationObtainer {

	public static QualifiedCoordinates getQualifiedCoordinates(){
		QualifiedCoordinates qc = null;
		
		int[] methods = {(Location.MTA_ASSISTED | Location.MTE_CELLID | Location.MTE_SHORTRANGE | Location.MTY_NETWORKBASED),
				(Location.MTA_ASSISTED | Location.MTE_SATELLITE | Location.MTY_TERMINALBASED),
				(Location.MTA_UNASSISTED |  Location.MTE_SATELLITE | Location.MTY_TERMINALBASED),
				(Location.MTA_UNASSISTED |  Location.MTE_CELLID | Location.MTY_TERMINALBASED)};

        LocationProvider provider;
        
		try {
			provider = LocationUtil.getLocationProvider(methods, null);
			Location location=provider.getLocation(10);
			if (location != null)
				qc = location.getQualifiedCoordinates();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (LocationException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return qc;
		
	}
}
