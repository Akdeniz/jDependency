package com.akdeniz.jdependency.cli;

import java.util.List;

import com.akdeniz.jdependency.Parser;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.FeatureControl;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * 
 * @author akdeniz
 */
public class JDependency {

	private ArgumentParser argParser;

	public JDependency() {
		argParser = ArgumentParsers.newArgumentParser("jdependency").description("Static dependency resolver for java modules.");

		argParser.addArgument("-d", "--directory").required(true).help("Directory that modules will be searched in!");
		argParser.addArgument("-o", "--output").nargs("?")	.help("Output html file.").setDefault("jdependency.html");
		argParser.addArgument("-i", "--include").nargs("*").help("Class name patterns to include.").setDefault(FeatureControl.SUPPRESS);
		argParser.addArgument("-e", "--exclude").nargs("*").help("Class name patterns to exclude.").setDefault(FeatureControl.SUPPRESS);
	}

	public static void main(String[] args) throws Exception {
		new JDependency().operate(args);
	}

	public void operate(String[] argv) {
		Namespace namespace = null;
		try {
			namespace = argParser.parseArgs(argv);
		} catch (ArgumentParserException e) {
			System.err.println(e.getMessage());
			argParser.printHelp();
			System.exit(-1);
		}


		try {
			String directory = namespace.getString("directory");
			String output = namespace.getString("output");
			
			List<String> includePatterns = namespace.getList("include");
			List<String> excludePatterns = namespace.getList("exclude");
			
			Parser parser = new Parser(directory, output, includePatterns, excludePatterns);
			parser.parse();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
