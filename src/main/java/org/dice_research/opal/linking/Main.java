package org.dice_research.opal.linking;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.jena.rdf.model.Model;
import org.dice_research.opal.linking.extraction.Extraction;

/**
 * Main entry point CLI.
 *
 * @author Adrian Wilke
 */
public class Main {

	public final static String MODE_EXTRACT_GRAPHS = "graphs";
	public final static String MODE_EXTRACT_RESULTS = "results";

	public final static String LANGUAGE = "TTL";

	public final static boolean CREATE_SUBGRAPH_MCLOUD = true;
	public final static boolean CREATE_SUBGRAPH_GOVDATA = true;
	public final static boolean PRINT_DATASET_SIZE_MCLOUD = true;
	public final static boolean PRINT_DATASET_SIZE_GOVDATA = true;

	public static void main(String[] args) throws Exception {
		if (args.length == 1 && args[0].equals(MODE_EXTRACT_GRAPHS)) {
			new Main().extractGraphs();

		} else if (args.length == 1 && args[0].equals(MODE_EXTRACT_RESULTS)) {
			new Main().extractResults();

		} else {
			System.err.println("Please provide the required argument(s):");
			System.err.println(" " + MODE_EXTRACT_GRAPHS + "    Extracts source graphs (e.g. mCLOUD graph)");
			System.err.println(" " + MODE_EXTRACT_RESULTS + "   Extracts LIMES results");
		}
	}

	protected void extractResults() throws IOException {
		Extraction extraction = new Extraction();
		extraction.extractUris(new File(Configuration.DATA_DIRECTORY, Configuration.FILE_LIMES));
		extraction.printResultExample();
	}

	protected void extractGraphs() throws Exception {

		Subgraph subgraph;

		// mCLOUD

		subgraph = new Mcloud();
		subgraph.setEndpoint(Configuration.OPAL_ENDPOINT);
		if (CREATE_SUBGRAPH_MCLOUD) {
			extractGraph(subgraph, new File(Configuration.DATA_DIRECTORY, Configuration.FILE_MCLOUD));
		}
		if (PRINT_DATASET_SIZE_MCLOUD) {
			System.out.println(subgraph.getDatasetUris().size());
		}

		// Govdata

		subgraph = new Govdata();
		subgraph.setEndpoint(Configuration.OPAL_ENDPOINT);
		if (CREATE_SUBGRAPH_GOVDATA) {
			extractGraph(subgraph, new File(Configuration.DATA_DIRECTORY, Configuration.FILE_GOVDATA));
		}
		if (PRINT_DATASET_SIZE_GOVDATA) {
			System.out.println(subgraph.getDatasetUris().size());
		}
	}

	protected void extractGraph(Subgraph subgraph, File file) throws Exception {
		Model model = subgraph.createModel();
		FileOutputStream fop = new FileOutputStream(file);
		model.write(fop, LANGUAGE);
		fop.close();
	}

}