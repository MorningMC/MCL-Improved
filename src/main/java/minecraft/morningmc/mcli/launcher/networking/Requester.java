package minecraft.morningmc.mcli.launcher.networking;

import minecraft.morningmc.mcli.launcher.settings.NetworkSettings;
import minecraft.morningmc.mcli.utils.annotations.StaticClass;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * A utility class that creates connections and sends requests.
 */
@StaticClass
public class Requester {
	
	/**
	 * Creates a new {@link URL} from a string.
	 *
	 * @param url The string to be converted to a {@link URL}.
	 * @return A new {@link URL} from a string. or {@code null} if the {@link URL} is malformed.
	 */
	public static URL newURL(String url) {
		try {
			return new URI(url).toURL();
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Creates a connection to a {@link URL}.
	 *
	 * @param url The URL to be connected.
	 * @return The created {@link URLConnection}.
	 * @throws IOException If an I/O error occurs.
	 */
	public static URLConnection openConnection(URL url) throws IOException {
		URLConnection connection = url.openConnection(NetworkSettings.proxy);
		connection.setConnectTimeout(NetworkSettings.connectTimeout);
		connection.setReadTimeout(NetworkSettings.readTimeout);
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
	public static URLConnection openConnection(URL url, Map<String, String> headers) throws IOException {
		URLConnection connection = openConnection(url);
		
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
	public static HttpURLConnection openConnection(URL url, Method method) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) openConnection(url);
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
	public static HttpURLConnection openConnection(URL url, Map<String, String> headers, Method method) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) openConnection(url, headers);
		connection.setRequestMethod(method.name());
		return connection;
	}
	
	/**
	 * Sends a request to the given {@link URLConnection} and returns the response.
	 *
	 * @param connection The connection to send the request.
	 * @return The response as a {@link Response} object.
	 * @throws IOException If an I/O error occurs.
	 */
	public static Response request(URLConnection connection) throws IOException {
		byte[] data;
		try (InputStream in = connection.getInputStream()) {
			data = in.readAllBytes();
		}
		
		Charset encoding;
		try {
			encoding = Charset.forName(connection.getContentEncoding());
		} catch (Exception e) {
			encoding = Charset.defaultCharset();
		}
		
		String contentType = connection.getContentType();
		
		// HttpURLConnection only
		int code = 0;
		if (connection instanceof HttpURLConnection httpConnection) {
			code = httpConnection.getResponseCode();
		}
		
		return new Response(data, encoding, contentType, code);
	}
	
	/**
	 * Sends a request to the given {@link URLConnection} with the given payload and returns the response.
	 *
	 * @param connection  The connection to send the request.
	 * @param payload     The payload to be sent with the request.
	 * @param contentType The content type of the payload.
	 * @return The response as a {@link Response} object.
	 * @throws IOException If an I/O error occurs.
	 */
	public static Response request(URLConnection connection, String payload, String contentType) throws IOException {
		return request(connection, payload.getBytes(), contentType);
	}
	
	/**
	 * Sends a request to the given {@link URLConnection} with the given payload and returns the response.
	 * @param connection  The connection to send the request.
	 * @param payload     The payload to be sent with the request.
	 * @param contentType The content type of the payload.
	 * @return The response as a {@link Response} object.
	 * @throws IOException If an I/O error occurs.
	 */
	public static Response request(URLConnection connection, byte[] payload, String contentType) throws IOException {
		return request(connection, payload, Charset.defaultCharset(), contentType);
	}
	
	/**
	 * Sends a request to the given {@link URLConnection} with the given payload and encoding and returns the response.
	 *
	 * @param connection  The connection to send the request.
	 * @param payload     The payload to be sent with the request.
	 * @param encoding    The encoding of the payload.
	 * @param contentType The content type of the payload.
	 * @return The response as a {@link Response} object.
	 * @throws IOException If an I/O error occurs.
	 */
	public static Response request(URLConnection connection, byte[] payload, Charset encoding, String contentType) throws IOException {
		connection.setRequestProperty("Content-Type", contentType);
		connection.setRequestProperty("Content-Length", String.valueOf(payload.length));
		connection.setRequestProperty("Content-Encoding", encoding.name());
		try (OutputStream out = connection.getOutputStream()) {
			out.write(payload);
		}
		return request(connection);
	}
	
	/**
	 * Represents a {@link URLConnection} response.
	 *
	 * @param data        The response data.
	 * @param encoding    The encoding of the response data.
	 * @param contentType The content type of the response data.
	 * @param code        The response code, or {@code 0} if the connection is not a {@link HttpURLConnection}.
	 */
	public record Response(byte[] data, Charset encoding, String contentType, int code) {
		
		/**
		 * Returns the response data as a string.
		 *
		 * @return The response data as a string.
		 */
		@Override
		public String toString() {
			return new String(data, encoding);
		}
	}
	
	/**
	 * Enumerates different HTTP request methods.
	 */
	public enum Method {
		GET, POST, HEAD, OPTIONS, PUT, DELETE, TRACE
	}
}
