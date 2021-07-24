/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esmc.mcnp.client.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author HP
 */
public class Utils {

	public static LocalDate convertToLocalDate(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		return LocalDate.parse(date, formatter);
	}

	public static LocalDate convertToLocalDate(String date, DateTimeFormatter formatter) {
		return LocalDate.parse(date, formatter);
	}

	public static boolean IsServerAvailable() {
		try {
			final URL url = new URL("http://www.esmcgie.com");
			final URLConnection conn = url.openConnection();
			conn.connect();
			conn.getInputStream().close();
			return true;
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			return false;
		}
	}

	public static boolean serverIsAvailable() {
		try {
			Runtime run = Runtime.getRuntime();
			Process proc = run.exec("ping -n 1 http://www.esmcgie.com");
			boolean connected = proc.waitFor(5, TimeUnit.SECONDS);
			System.out.println("Connected ? " + connected);
			return connected;
		} catch (IOException | InterruptedException ex) {
			System.out.println(ex);
			return false;
		}
	}
}
