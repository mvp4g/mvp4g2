/*
 * Copyright (C) 2017 Frank Hossfeld <frank.hossfeld@googlemail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package de.gishmo.gwt.mvp4g2.jsinterop.client.core.html;

import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@JsType(isNative = true)
public interface Location {

  /**
   * The hash property returns the anchor part of a URL, including the hash sign (#).
   *
   * @return A String, representing the anchor part of the URL, including the hash sign (#)
   */
  @JsProperty
  String getHash();

  /**
   * The hash property sets the anchor part of a URL, including the hash sign (#).
   *
   * @param anchorname
   *   Specifies the anchor part of a URL
   */
  @JsProperty
  void setHash(String anchorname);

  /**
   * The host property returns the hostname and port of a URL.
   *
   * @return A String, representing the domain name and port number, or the IP address of a URL
   */
  @JsProperty
  String getHost();

  /**
   * The host property sets the hostname and port of a URL.
   *
   * @param host
   *   Specifies the hostname and port number of the URL
   */
  @JsProperty
  void setHost(String host);

  /**
   * The hostname property sets the hostname of a URL.
   *
   * @return A String, representing the domain name, or the IP address of a URL
   */
  @JsProperty
  String getHostname();

  /**
   * The hostname property sets the hostname of a URL.
   *
   * @param hostname
   *   Specifies the hostname of the URL
   */
  @JsProperty
  void setHostname(String hostname);

  /**
   * The href property returns the entire URL of the current page.
   *
   * @return A String, representing the entire URL of the page, including the protocol (like http://)
   */
  @JsProperty
  String getHref();

  /**
   * The href property sets the entire URL of the current page.
   *
   * @param href
   *   Specifies the URL of the link.<br>
   *   Possible values:<br>
   *   <ul>
   *   <li>An absolute URL - points to another web site (like
   *   location.href="http://www.example.com/default.htm")
   *   <li>A relative URL - points to a file within a web site (like location.href="default.htm")
   *   <li>An anchor URL - points to an anchor within a page (like location.href="#top")
   *   <li>A new protocol - specifies a different protocol (like
   *   location.href="ftp://someftpserver.com", location.href="mailto:someone@example.com" or
   *   location.href="file://host/path/example.txt")
   *   </ul>
   */
  @JsProperty
  void setHref(String href);

  /**
   * The pathname property returns the pathname of a URL.
   *
   * @return A String, representing the pathname
   */
  @JsProperty
  String getPathname();

  /**
   * The pathname property sets the pathname of a URL.
   *
   * @param pathname
   *   Specifies the pathname of the URL
   */
  @JsProperty
  void setPathname(String pathname);

  /**
   * The port property returns the port number the server uses for a URL.<br>
   * <p>
   * Note: If the port number is not specified in the URL (or if it is the scheme's default port - like 80,
   * or 443), some browsers will display 0 or nothing.
   *
   * @return A String, representing the port number of a URL.
   */
  @JsProperty
  String getPort();

  /**
   * The port property sets the port number the server uses for a URL.
   *
   * @param port
   *   Specifies the port number of the URL
   */
  @JsProperty
  void setPort(String port);

  /**
   * The protocol property returns the protocol of the current URL, including the colon (:).
   *
   * @return A String, representing the protocol of the current URL, including the colon (:)
   */
  @JsProperty
  String getProtocol();

  /**
   * The protocol property sets the protocol of the current URL, including the colon (:).
   *
   * @param protocol
   *   The protocol of the URL. Possible values:
   *   <ul>
   *   <li>file:
   *   <li>http:
   *   <li>https:
   *   <li>mailto:
   *   <li>etc...
   *   </ul>
   */
  @JsProperty
  void setProtocol(String protocol);

  /**
   * The search property returns the querystring part of a URL, including the question mark (?).
   *
   * @return A String, representing the querystring part of a URL, including the question mark (?)
   */
  @JsProperty
  String getSearch();

  /**
   * The search property sets the querystring part of a URL, including the question mark (?).
   *
   * @param search
   *   Specifies the search part of the URL
   */
  @JsProperty
  void setSearch(String search);

  /**
   * The assign() method loads a new document.<br>
   * <p>
   * The difference between this method and replace(), is that replace() removes the URL of the current
   * document from the document history, meaning that it is not possible to use the "back" button to
   * navigate back to the original document.
   *
   * @param url
   *   Required. Specifies the URL of the page to navigate to
   */
  void assign(String url);

  /**
   * The reload() method is used to reload the current document.<br>
   * The reload() method does the same as the reload button in your browser.<br>
   * By default, the reload() method reloads the page from the cache, but you can force it to reload the
   * page from the server by setting the forceGet parameter to true: location.reload(true).
   */
  void reload();

  /**
   * The reload() method is used to reload the current document.<br>
   * The reload() method does the same as the reload button in your browser.<br>
   * By default, the reload() method reloads the page from the cache, but you can force it to reload the
   * page from the server by setting the forceGet parameter to true: location.reload(true).
   *
   * @param forceGet
   *   Optional. Specifies the type of reloading:<br>
   *   false - Default. Reloads the current page from the cache.<br>
   *   true - Reloads the current page from the server
   */
  void reload(boolean forceGet);

  /**
   * The replace() method replaces the current document with a new one.<br>
   * <br>
   * The difference between this method and assign(), is that replace() removes the URL of the current
   * document from the document history, meaning that it is not possible to use the "back" button to
   * navigate back to the original document.<br>
   * <br>
   * Tip: Use the assign() method if you want to load a new document, and the option to navigate back to the
   * original document.
   *
   * @param url
   *   Required. Specifies the URL of the page to navigate to
   */
  void replace(String url);
}