package org.dice_research.opal.linking;

import java.util.LinkedList;
import java.util.List;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdfconnection.RDFConnection;
import org.dice_research.opal.exceptions.ConfigurationException;
import org.dice_research.opal.exceptions.ResourceException;

public abstract class Subgraph extends Endpoint {

	public static final String RESOURCE_SPARQL_SUBGRAPH = "sparql-subgraph.txt";
	public static final String RESOURCE_SPARQL_DATASET_URIS = "sparql-dataset-uris.txt";

	abstract protected String getCatalogUri();

	Model createModel() throws ConfigurationException, ResourceException {
		String sparqlQuery = Resources.getResourceAsString(RESOURCE_SPARQL_SUBGRAPH);
		sparqlQuery = sparqlQuery.replace("?catalog", "<" + getCatalogUri() + ">");
		RDFConnection rdfConnection = getConnection();
		QueryExecution queryExecution = rdfConnection.query(sparqlQuery);
		return queryExecution.execConstruct();
	}

	/**
	 * Returns all Dataset URIs of subgraph/catalog
	 */
	List<String> getDatasetUris() throws ResourceException, ConfigurationException {
		String sparqlQuery = Resources.getResourceAsString(RESOURCE_SPARQL_DATASET_URIS);
		sparqlQuery = sparqlQuery.replace("?catalog", "<" + getCatalogUri() + ">");
		RDFConnection rdfConnection = getConnection();
		QueryExecution queryExecution = rdfConnection.query(sparqlQuery);
		ResultSet resultSet = queryExecution.execSelect();

		List<String> uris = new LinkedList<String>();
		while (resultSet.hasNext()) {
			QuerySolution querySolution = resultSet.next();
			uris.add(querySolution.get("dataset").asResource().getURI());
		}
		return uris;
	}

}