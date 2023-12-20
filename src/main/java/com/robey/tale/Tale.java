package com.robey.tale;

import org.apache.commons.cli.ParseException;

import java.io.FileInputStream;
import java.io.IOException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

class Tale {
	private static final int BUFFER_SIZE = 512;
	private static final int SLEEP_DURATION = 500;

	public static void main(final String[] args) {

		TaleContext ctx = getAppContextFromCLIArgs(args);
		if(ctx == null){
			System.exit(1);
			return;
		}

		//read from the file
		byte[] buffer = new byte[BUFFER_SIZE];

		try (FileInputStream fileStream = new FileInputStream(ctx.file)) {
			
			int numRead = fileStream.read(buffer);

			if(!ctx.isForever()){
				while(numRead != -1) {

					System.out.print(new String( buffer, 0, numRead));

					numRead = fileStream.read(buffer);
				}
			}
			else{
				// we're reading forever, so block while numRead is -1
				while(true) {
					if(numRead == -1){
						try {
							Thread.sleep(SLEEP_DURATION);

						} catch (InterruptedException e) {
							// interrupted, so exit
							System.out.println("Interrupted, exiting");
							break;
						}
					}
					else {
						System.out.print(new String( buffer, 0, numRead));
					}
					numRead = fileStream.read(buffer);
				}

				System.out.println(new String( buffer, 0, numRead));
			}
		}
		catch(IOException ex) {
			System.err.println( "Error: " + ex.getMessage());
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
