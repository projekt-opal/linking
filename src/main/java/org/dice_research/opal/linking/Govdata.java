package org.dice_research.opal.linking;

public class Govdata extends Subgraph {

	final static String CATALOG_URI = "http://projekt-opal.de/catalog/govdata";

	@Override
	protected String getCatalogUri() {
		return CATALOG_URI;
	}

}