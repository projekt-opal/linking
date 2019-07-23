package org.dice_research.opal.linking.extraction;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.dice_research.opal.exceptions.ResourceException;
import org.dice_research.opal.linking.Configuration;

public class Extraction {

	private List<String> urisSource = new LinkedList<String>();
	private List<String> urisTarget = new LinkedList<String>();

	private File fileMcloud = new File(Configuration.DATA_DIRECTORY, Configuration.FILE_MCLOUD);
	private File fileGovdata = new File(Configuration.DATA_DIRECTORY, Configuration.FILE_GOVDATA);

	public static final String RESOURCE_SPARQL_RESULTS = "sparql-results.txt";

	public void extractUris(File limesResultsFile) throws IOException {

		// Go through lines
		for (String line : FileUtils.readLines(limesResultsFile, StandardCharsets.UTF_8)) {

			// Split line b tab
			String[] parts = line.split("\t");

			// Get URIs
			urisSource.add(parts[0].substring(1, parts[0].length() - 1));
			urisTarget.add(parts[1].substring(1, parts[1].length() - 1));
		}
	}

	/**
	 * Dev code to explore data to extract afterwards.
	 */
	public void printExploration() throws ResourceException {

		System.out.println("Size source and target:");
		System.out.println(urisSource.size());
		System.out.println(urisTarget.size());
		System.out.println();

		if (urisSource.isEmpty() || urisTarget.isEmpty()) {
			return;
		}

		System.out.println("First URI source and target:");
		System.out.println(urisSource.get(0));
		System.out.println(urisTarget.get(0));
		System.out.println();

		Model modelMCloud = ModelFactory.createDefaultModel().read(fileMcloud.toURI().toString());
		Model modelGovdata = ModelFactory.createDefaultModel().read(fileGovdata.toURI().toString());

		System.out.println("Model sizes mCLOUD and Govdata:");
		System.out.println(modelMCloud.size());
		System.out.println(modelGovdata.size());
		System.out.println();

		StmtIterator stmtIterator;
		System.out.println("Data first source/mCLOUD:");
		stmtIterator = modelMCloud.getResource(urisSource.get(0)).listProperties();
		while (stmtIterator.hasNext()) {
			Statement statement = stmtIterator.next();
			System.out.println(statement.getPredicate() + " " + statement.getObject());
		}
		System.out.println();

		System.out.println("Data first target/Govdata:");
		stmtIterator = modelGovdata.getResource(urisTarget.get(0)).listProperties();
		while (stmtIterator.hasNext()) {
			Statement statement = stmtIterator.next();
			System.out.println(statement.getPredicate() + " " + statement.getObject());
		}
	}

	public void extractResults() {
		if (urisSource.isEmpty()) {
			System.err.println("Source is empty");
			return;
		}
		if (urisTarget.isEmpty()) {
			System.err.println("Target is empty");
			return;
		}
		if (urisSource.size() != urisTarget.size()) {
			System.err.println("Source and target not equal");
			return;
		}

		class Container implements Comparable<Container> {
			public String titleSource;
			public String titleTarget;

			public boolean equal() {
				return titleSource.equals(titleTarget);
			}

			/**
			 * TSV line
			 */
			@Override
			public String toString() {
				if (titleSource.equals(titleTarget)) {
					return " \t \t" + titleSource;
				} else {
					return titleSource + "\t" + titleTarget + "\t ";
				}
			}

			@Override
			public int compareTo(Container o) {
				if (equal()) {
					if (o.equal()) {
						return titleSource.compareTo(o.titleSource);
					} else {
						return -1;
					}
				} else {
					if (o.equal()) {
						return 1;
					} else {
						return titleSource.compareTo(o.titleSource);
					}
				}
			}
		}
		List<Container> containers = new ArrayList<>(urisSource.size());

		Model modelMCloud = ModelFactory.createDefaultModel().read(fileMcloud.toURI().toString());
		Model modelGovdata = ModelFactory.createDefaultModel().read(fileGovdata.toURI().toString());

		Property propTitle = ResourceFactory.createProperty("http://purl.org/dc/terms/title");
		for (int i = 0; i < urisSource.size(); i++) {
			Container container = new Container();
			Resource resourceSource = ResourceFactory.createResource(urisSource.get(i));
			Resource resourceTarget = ResourceFactory.createResource(urisTarget.get(i));
			container.titleSource = modelMCloud.getProperty(resourceSource, propTitle).getObject().toString().trim();
			container.titleTarget = modelGovdata.getProperty(resourceTarget, propTitle).getObject().toString().trim();
			containers.add(container);
		}
		
		Collections.sort(containers);

		for (Container container : containers) {
			System.out.println(container);
		}
	}
}