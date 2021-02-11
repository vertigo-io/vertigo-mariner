package io.vertigo.mariner.map;

import javax.inject.Inject;

import io.vertigo.vega.webservice.WebServices;
import io.vertigo.vega.webservice.stereotype.AnonymousAccessAllowed;
import io.vertigo.vega.webservice.stereotype.GET;
import io.vertigo.vega.webservice.stereotype.PathPrefix;
import io.vertigo.vega.webservice.stereotype.SessionLess;

@PathPrefix("/map")
public class MapWebServices implements WebServices {
	@Inject
	private MapServices mapservices;

	@SessionLess
	@AnonymousAccessAllowed
	@GET("/")
	public Map get() {
		return mapservices.getMap();
	}
}
