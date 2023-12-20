package com.robey.tale;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class TaleContext {
    Boolean forever;
    Integer bytesToSkip;
    File file;

    public TaleContext(String filename, boolean forever, int bytesToSkip){
        this.forever = forever;
        this.bytesToSkip =  bytesToSkip;
        file = new File(filename);
    }

    public File getFile(){
        return file;
    }

    public boolean isForever(){
        return forever.booleanValue();
    }

    public int bytesToSkip(){
        return this.bytesToSkip.intValue();
    }

    public static TaleContext getAppContextFromCLIArgs(String[] args) {

		CommandLineParser parser = new DefaultParser();
	    try {
            Options options = createOptions();
			CommandLine cmd = parser.parse(options, args);

            if(cmd.hasOption("h")) {
                printHelp(options, "");
                return null;
            }

			Boolean forever = cmd.hasOption("f");
            
            if(cmd.getArgs().length != 1){
                printHelp(options, "Must specify a file to tail.");
                return null;
            }
			String filename = cmd.getArgs()[0];

            int bytesToSkip = 0;
			if(cmd.hasOption("c")) {
                bytesToSkip = Math.max(0, Integer.parseInt(cmd.getOptionValue("c")));
            }
            else if (cmd.hasOption("b")) {
                bytesToSkip = Math.max(0, Integer.parseInt(cmd.getOptionValue("b"))) * 512; // 512 bytes per block
            }

			return new TaleContext(filename, forever, bytesToSkip);
		}
		catch(final ParseException e) {
			System.out.println("Cannot parse arguments: " + e.getMessage());
			return null;
		}
	}

    private static Options createOptions(){

		Options options = new Options();
		options.addOption("f", null, false, "tail forever");
		options.addOption("c", "bytes", true, "begin taleing at the specified byte count");
        options.addOption("b", "blocks", true, "begin taleing at the specified block count. each block is 512 bytes");
        options.addOption("h", "help", false, "print this help message");

		return options;
	}

    private static void printHelp(Options options, String header) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("tale", header, options, "", true);
    }
}
