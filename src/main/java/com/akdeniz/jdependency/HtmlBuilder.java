package com.akdeniz.jdependency;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Set;

/**
 * 
 * @author akdeniz
 */
public class HtmlBuilder {

	static String HEADER = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
			+ "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /><title>jDependency</title>"
			+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"resources/style.css\" /><script type='text/javascript' src='resources/jquery.min.js'></script>"
			+ "<script type='text/javascript' src='resources/jdependency.js'></script>";

	static String HEADER_END = "</head><body><div id=\"page-wrap\">";

	static String FOOTER = "</div><div id=\"dependenciesInfo\" class=\"info\"></div></body></html>";

	public static void buildHtml(String outputFile, String[] moduleNames, Set<String>[][] dependencies) throws IOException {

		try (PrintStream stream = new PrintStream(new FileOutputStream(outputFile))) {

			stream.println(HEADER);
			printScripts(stream, moduleNames, dependencies);
			stream.println(HEADER_END);
			printTable(stream, moduleNames, dependencies);
			stream.println(FOOTER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void printTable(PrintStream stream, String[] moduleNames, Set<String>[][] dependencies) {
		
		stream.print("<table>");
		
		int length = moduleNames.length;
		for (int i = 0; i < length; i++) {
			stream.println("<colgroup class=\"slim\"></colgroup>");
		}

		stream.println("<tbody>");

		for (int i = 0; i < length; i++) {
			stream.println("<tr>");
			for (int j = 0; j < length; j++) {
				Set<String> set = dependencies[i][j];

				if (set != null && set.size() > 0) {
					stream.print("<td id='cell_"+i+"_"+j+"' class='back-black'>");
				} else {
					stream.print("<td id='cell_"+i+"_"+j+"'>");
				}

				stream.println("</td>");
			}
			stream.println("<td id='cell_mn_"+i+"' class='moduleName'>" + moduleNames[i] + "</td>");
			stream.println("</tr>");
		}
		stream.println("<tr>");
		for (int y = 0; y < length; y++) {
			stream.println("<td class='rotatedModuleName'>" + moduleNames[y] + "</td>");
		}
		stream.println("</tr>");
		stream.println("</table>");
	}

	private static void printScripts(PrintStream stream, String[] moduleNames, Set<String>[][] dependencies) {

		stream.println("<script type='text/javascript'> $(function() {");

		// print module names;
		stream.print("var modules=[");
		for (String name : moduleNames) {
			stream.print("\"" + name + "\",");
		}
		stream.println("];");

		// print packages
		int length = moduleNames.length;
		stream.print("var packages=[");
		for (int i = 0; i < length; i++) {
			stream.print("[");
			for (int j = 0; j < length; j++) {
				Set<String> set = dependencies[i][j];
				if (set != null && set.size() > 0) {
					stream.print("[");
					for (String string : set) {
						stream.print("\"" + string + "\", ");
					}
					stream.print("],");

				} else {
					stream.print("[],");
				}
			}
			stream.print("],");
		}
		stream.println("];");
		stream.print("callbacks(modules, packages);");
		stream.println("});</script>");
	}

}
