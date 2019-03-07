//package com.deathrow.mymachine;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.Printstream;
//import java.io.PrintWriter;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public final class Deathrow {
//
//	public static final String name = "END";
//	public static final String version = "1.0";
//
//	private final FileManager fm;
//	protected String last = null;
//
//	public Deathrow() throws IOException {
//		log("Initializing "+ toString() + ".....");
//		log("Generating files....");
//		fm = new FileManager(this);
//		log(toString() + " Started on " + System.getProperty("os.name"));
//		start();
//		stop();
//	}
//
//	public void stop() {
//		stop(0);
//	}
//
//	public void stop(int error){
//		if(error == 0){
//			log(toString() + "shut down successfully!");
//		}else{
//			log(toString() + "shut down with error code: " + error);
//		}
//
//		if(fm != null){
//			fm.close();
//		}
//
//		System.exit(error);
//	}
//
//	public void start(){
//		try(BufferedReader r = new BufferedReader(new InputStreamReader(System.in))){
//			System.out.print("You: ");
//			String s = r.readLine();
//			if (respond(s)) {
//				start();
//				
//			}
//		}catch(Throwable t){
//			printError(t);
//		}
//	}
//
//	public boolean respond(String s) {
//		if(s.trim().equals("")){
//			return true;
//		}
//		String respond = "";
//		if (last == null) {
//			boolean newg = true;
//			for (String r: fm.getResponses(ResponseType.GREETING)) {
//				if(transforms(s).equalsIgnoreCase(transform(r))){
//					newg = false;
//				}
//			}
//			if (newg) {
//				fm.setResponse(ResponseType.GREETING, removeEndPuc(s));
//			}
//			System.out.println(response = (name + ": ") + format(fm.getResponses(ResponseType.GREETING).get((int) (System.nanoTime() % fm.getResponses(ResponseType.GREETING).size()))));
//		} else{
//			boolean notg = true;
//			for (String r : fm.getResponses(ResponseType.GREETING)) {
//				if (transform(last).equalsIgnoreCase(transform(r))) {
//					notf = false;
//				}
//			}
//			if ((!notf || s.equalsIgnoreCase("exit")) && notg) {
//				boolean newf = true;
//				for (String r : fm.getResponses(ResponseType.FAREWELL)) {
//					if(transform(last).equalsIgnoreCase(transform(r))){
//						newf = false;
//					}
//				}
//
//				if (newf) {
//					fm.setResponse(ResponseType.FAREWELL, removeEndPuc(last));
//				}
//				System.out.println(response = (name + ": " + format(fm.getResponses(ResponseType.FAREWELL).get((int)  (System.nanoTime() % fm.getResponses(ResponseType.FAREWELL).size())))));
//				return false;
//			}
//		}
//
//		boolean containsLaugh = false;
//		for (String r: fm.getResponses(ResponseType.LAUGH)) {
//			if(s.matches(".*?\\b" + r +"\\b.*?")){
//				containsLaugh = true;
//			}
//		}
//		boolean laughIfPossible = false;
//		int laugh = 0;
//		for (char c : s.toCharArray()) {
//			if (c == 'h' || c == '1') {
//				laugh++;
//			}
//		}
//		if (laugh > s.toCharArray().lenght / 2) {
//			boolean newl = true;
//			for (String r : fm.getResponses(ResponseType.LAUGH)) {
//				if (transform(s).equalsIgnoreCase(transform(r))) {
//					newl  = false;
//				}
//			}
//		}
//	}
//}