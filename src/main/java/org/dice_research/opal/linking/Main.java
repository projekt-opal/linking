package org.dice_research.opal.linking;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.jena.rdf.model.Model;
import org.dice_research.opal.exceptions.ConfigurationException;
import org.dice_research.opal.exceptions.ResourceException;

public class Main {

	public final static String FILE_MCLOUD = "mcloud.ttl";
	public final static String FILE_GOVDATA = "govdata.ttl";
	public final static String LANGUAGE = "TTL";

	public final static boolean CREATE_SUBGRAPH_MCLOUD = true;
	public final static boolean CREATE_SUBGRAPH_GOVDATA = true;

	public final static boolean GET_URIS_MCLOUD = true;
	public final static boolean GET_URIS_GOVDATA = true;

	public static void main(String[] args) throws ConfigurationException, ResourceException, IOException {

		Main main = new Main();
		File file;
		Subgraph subgraph;

		// mCLOUD

		subgraph = new Mcloud();
		subgraph.setEndpoint(Configuration.OPAL_ENDPOINT);

		if (CREATE_SUBGRAPH_MCLOUD) {
			file = new File(FILE_MCLOUD);
			main.createMcloudSubgraph(subgraph, file);
		}

		if (GET_URIS_MCLOUD) {
			System.out.println(subgraph.getDatasetUris().size());
		}

		// Govdata

		subgraph = new Govdata();
		subgraph.setEndpoint(Configuration.OPAL_ENDPOINT);

		if (CREATE_SUBGRAPH_GOVDATA) {
			file = new File(FILE_GOVDATA);
			main.createMcloudSubgraph(subgraph, file);
		}

		if (GET_URIS_GOVDATA) {
			System.out.println(subgraph.getDatasetUris().size());
		}
	}

	protected void createMcloudSubgraph(Subgraph subgraph, File file)
			throws ConfigurationException, ResourceException, IOException {
		Model model = subgraph.createModel();
		FileOutputStream fop = new FileOutputStream(file);
		model.write(fop, LANGUAGE);
		fop.close();
	}

}