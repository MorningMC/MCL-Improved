package minecraft.morningmc.mcli.launcher.networking;

import minecraft.morningmc.mcli.launcher.settings.NetworkSettings;
import minecraft.morningmc.mcli.utils.interfaces.Builder;

import java.io.IOException;
import java.net.*;
import java.nio.charset.Charset;

/**
 * A builder for creating {@link HttpURLConnection} objects.
 */
public class ConnectionBuilder implements Builder<HttpURLConnection> {
	private final HttpURLConnection connection;
	
	/**
	 * Constructs a new {@link ConnectionBuilder} with the given {@link URL}.
	 *
	 * @param url The {@link URL} to be connected.
	 * @throws IOException If an I/O error occurs.
	 */
	public ConnectionBuilder(URL url) throws IOException {
		connection = (HttpURLConnection) url.openConnection(NetworkSettings.proxy);
		connection.setConnectTimeout(NetworkSettings.connectTimeout);
		connection.setReadTimeout(NetworkSettings.readTimeout);
		connection.setDoOutput(true);
	}
	
	/**
	 * Creates a new {@link ConnectionBuilder} with the given {@link URL}.
	 *
	 * @param url The {@link URL} to be connected.
	 * @return The created {@link ConnectionBuilder} instance.
	 * @throws IOException If an I/O error occurs.
	 */
	public static ConnectionBuilder create(URL url) throws IOException {
		return new ConnectionBuilder(url);
	}
	
	/**
	 * Adds a header for the connection.
	 *
	 * @param key   The header key to be added.
	 * @param value The header value to be added.
	 * @return The current {@link ConnectionBuilder} instance.
	 */
	public ConnectionBuilder header(String key, String value) {
		connection.setRequestProperty(key, value);
		return this;
	}
	
	/**
	 * Sets the request method for the connection.
	 *
	 * @param method The request method.
	 * @return The current {@link ConnectionBuilder} instance.
	 * @throws IOException If an I/O error occurs.
	 * @see Method
	 */
	public ConnectionBuilder method(Method method) throws IOException {
		connection.setRequestMethod(method.name());
		return this;
	}
	
	/**
	 * Sends the request and returns the response.
	 * <p>
	 * This method is equivalent to {@link Requester#request(URLConnection)}.
	 *
	 * @return The response as a {@link Requester.Response} object.
	 * @throws IOException If an I/O error occurs.
	 */
	public Requester.Response request() throws IOException {
		return Requester.request(connection);
	}
	
	/**
	 * Sends the request with the given payload and returns the response.
	 * <p>
	 * This method is equivalent to {@link Requester#request(URLConnection, String, String)}.
	 *
	 * @param payload     The payload to be sent with the request.
	 * @param contentType The content type of the payload.
	 * @return The response as a {@link Requester.Response} object.
	 * @throws IOException If an I/O error occurs.
	 */
	public Requester.Response request(String payload, String contentType) throws IOException {
		return Requester.request(connection, payload, contentType);
	}
	
	/**
	 * Sends the request with the given payload and returns the response.
	 * <p>
	 * This method is equivalent to {@link Requester#request(URLConnection, byte[], String)}.
	 *
	 * @param payload     The payload to be sent with the request.
	 * @param contentType The content type of the payload.
	 * @return The response as a {@link Requester.Response} object.
	 * @throws IOException If an I/O error occurs.
	 */
	public Requester.Response request(byte[] payload, String contentType) throws IOException {
		return Requester.request(connection, payload, contentType);
	}
	
	/**
	 * Sends the request with the given payload and returns the response.
	 * <p>
	 * This method is equivalent to {@link Requester#request(URLConnection, byte[], Charset, String)}.
	 *
	 * @param payload     The payload to be sent with the request.
	 * @param encoding    The encoding of the payload.
	 * @param contentType The content type of the payload.
	 * @return The response as a {@link Requester.Response} object.
	 * @throws IOException If an I/O error occurs.
	 */
	public Requester.Response request(byte[] payload, Charset encoding, String contentType) throws IOException {
		return Requester.request(connection, payload, encoding, contentType);
	}
	
	/**
	 * Builds the {@link HttpURLConnection} instance.
	 *
	 * @return The built {@link HttpURLConnection} instance.
	 */
	@Override
	public HttpURLConnection build() {
		return connection;
	}
	
	/**
	 * Enumerates different HTTP request methods.
	 */
	public enum Method {
		GET, POST, HEAD, OPTIONS, PUT, DELETE, TRACE
	}
}
