package com.robey.tale;

import org.apache.commons.cli.ParseException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

class Tale {

	


	public static void main(final String[] args) {
		System.out.println("Hello world");

		TaleContext ctx = getAppContextFromCLIArgs(args);
		if(ctx == null){
			System.exit(1);
			return;
		}
		
		//read from the file
		try (FileReader fileStream = new FileReader(ctx.file);
		     BufferedReader bufferedReader = new BufferedReader(fileStream)
			) {
			
			String thisLine = bufferedReader.readLine();
			while(thisLine != null) {

				System.out.println(thisLine);

				thisLine = bufferedReader.readLine();
			}
		}
		catch(IOException ex) {
			System.err.println( "Error;" + ex.getMessage());
		}

		System.exit(0);
	}

	private static TaleContext getAppContextFromCLIArgs(String[] args) {

		CommandLineParser parser = new DefaultParser();
	    try {
			CommandLine cmd = parser.parse(createOptions(), args);

			Boolean forever = cmd.hasOption("f");
			String filename = cmd.getArgs()[0];

			return new TaleContext(filename, forever);
		}
		catch(final ParseException e) {
			System.out.println("Cannot parse arguments: " + e.getMessage());
			return null;
		}
	}

	private static Options createOptions(){

		Options options = new Options();
		options.addOption("f", null, false, "tail forever");

		return options;
	}


}
