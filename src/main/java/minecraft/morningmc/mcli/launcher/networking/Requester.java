package minecraft.morningmc.mcli.launcher.networking;

import minecraft.morningmc.mcli.launcher.settings.NetworkSettings;
import minecraft.morningmc.mcli.utils.annotations.StaticClass;

import java.io.*;
import java.net.*;
import java.util.*;

@StaticClass
public class Requester {
	
	/**
	 * Creates a connection to a {@link URL}.
	 *
	 * @param url The URL to be connected.
	 * @return The created {@link URLConnection}.
	 * @throws IOException If an I/O error occurs.
	 */
	public static URLConnection createConnection(URL url) throws IOException {
		URLConnection connection = url.openConnection(NetworkSettings.proxy);
		connection.setConnectTimeout(NetworkSettings.timeout);
		connection.setReadTimeout(NetworkSettings.timeout);
		connection.setDoOutput(true);
		return connection;
	}
	
	/**
	 * Creates a connection to a {@link URL} with the given headers.
	 *
	 * @param url     The URL to be connected.
	 * @param headers The headers to be set for the connection.
	 * @return The created {@link URLConnection}.
	 * @throws IOException If an I/O error occurs.
	 */
	public static URLConnection createConnection(URL url, Map<String, String> headers) throws IOException {
		URLConnection connection = createConnection(url);
		
		for (Map.Entry<String, String> header : headers.entrySet()) {
			connection.setRequestProperty(header.getKey(), header.getValue());
		}
		
		return connection;
	}
	
	/**
	 * Creates a connection to a {@link URL} with the given method.
	 *
	 * @param url    The URL to be connected.
	 * @param method The request method.
	 * @return The created {@link HttpURLConnection}.
	 * @throws IOException If an I/O error occurs.
	 */
	public static HttpURLConnection createConnection(URL url, Method method) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) createConnection(url);
		connection.setRequestMethod(method.name());
		return connection;
	}
	
	/**
	 * Creates a connection to a {@link URL} with the given headers and method.
	 *
	 * @param url     The URL to be connected.
	 * @param headers The headers to be set for the connection.
	 * @param method  The request method.
	 * @return The created {@link HttpURLConnection}.
	 * @throws IOException If an I/O error occurs.
	 */
	public static HttpURLConnection createConnection(URL url, Map<String, String> headers, Method method) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) createConnection(url, headers);
		connection.setRequestMethod(method.name());
		return connection;
	}
	
	/**
	 * Sends a request to the given {@link URLConnection} and returns the response as a byte array.
	 *
	 * @param connection The connection to send the request.
	 * @return The response as a byte array.
	 * @throws IOException If an I/O error occurs.
	 */
	public static byte[] request(URLConnection connection) throws IOException {
		try (InputStream in = connection.getInputStream()) {
			return in.readAllBytes();
		}
	}
	
	/**
	 * Sends a request to the given {@link URLConnection} with the given payload and returns the response as a byte array.
	 *
	 * @param connection  The connection to send the request.
	 * @param payload     The payload to be sent with the request.
	 * @param contentType The content type of the payload.
	 * @return The response as a byte array.
	 * @throws IOException If an I/O error occurs.
	 */
	public static byte[] request(URLConnection connection, byte[] payload, String contentType) throws IOException {
		connection.setRequestProperty("Content-Type", contentType);
		connection.setRequestProperty("Content-Length", String.valueOf(payload.length));
		try (OutputStream out = connection.getOutputStream()) {
			out.write(payload);
		}
		return request(connection);
	}
	
	/**
	 * Enumerates different HTTP request methods.
	 */
	public enum Method {
		GET, POST, HEAD, OPTIONS, PUT, DELETE, TRACE
	}
}
