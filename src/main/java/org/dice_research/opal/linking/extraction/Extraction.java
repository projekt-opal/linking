package org.dice_research.opal.linking.extraction;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class Extraction {

	private List<String> urisS = new LinkedList<String>();
	private List<String> urisT = new LinkedList<String>();

	public void extract(File file) throws IOException {

		// Go through lines
		for (String line : FileUtils.readLines(file, StandardCharsets.UTF_8)) {

			// Split line b tab
			String[] parts = line.split("\t");

			// Get URIs
			urisS.add(parts[0].substring(1, parts[0].length() - 1));
			urisT.add(parts[1].substring(1, parts[1].length() - 1));
		}

		// TODO

		System.out.println(urisS.size());
		System.out.println(urisT.size());
		
		System.out.println(urisS.get(0));
		System.out.println(urisT.get(0));
	}
}