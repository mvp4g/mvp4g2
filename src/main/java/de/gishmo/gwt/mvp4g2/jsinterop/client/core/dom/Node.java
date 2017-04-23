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

package de.gishmo.gwt.mvp4g2.jsinterop.client.core.dom;

import de.gishmo.gwt.mvp4g2.jsinterop.client.core.events.EventTarget;

import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * A Node is an interface from which a number of DOM types inherit, and allows these various types to be
 * treated (or tested) similarly.
 */
@JsType(isNative = true)
public interface Node
  extends EventTarget {

  /**
   * Getter for childNodes attribute
   *
   * @return Returns a live NodeList containing all the children of this node. NodeList being live means
   * that if the children of the Node change, the NodeList object is automatically updated.
   */
  @JsProperty
  NodeList getChildNodes();

  /**
   * Getter for the firstChild attribute
   *
   * @return Returns a Node representing the first direct child node of the node, or null if the node has no
   * child.
   */
  @JsProperty
  Node getFirstChild();

  /**
   * Getter for the lastChild attribute
   *
   * @return Returns a Node representing the last direct child node of the node, or null if the node has no
   * child.
   */
  @JsProperty
  Node getLastChild();

  /**
   * Getter for the parentNode attribute
   *
   * @return Returns a Node that is the parent of this node. If there is no such node, like if this node is
   * the top of the tree or if doesn't participate in a tree, this property returns null.
   */
  @JsProperty
  Node getParentNode();

  /**
   * Getter for the parentElement attribute
   *
   * @return Returns an Element that is the parent of this node. If the node has no parent, or if that
   * parent is not an Element, this property returns null.
   */
  @JsProperty
  Element getParentElement();

  /**
   * Getter for textContent property.<br>
   * <b>Differences to innerText:</b><br>
   * Internet Explorer introduced element.innerText. The intention is similar but with the following
   * differences:<br>
   * While textContent gets the content of all elements, including &lt;script&gt; and &lt;style&gt;
   * elements, the IE-specific property innerText does not.<br>
   * innerText is aware of style and will not return the text of hidden elements, whereas textContent will.
   * <br>
   * As innerText is aware of CSS styling, it will trigger a reflow, whereas textContent will not.<br>
   * Unlike textContent, altering innerText in Internet Explorer (up to version 11 inclusive) not just
   * removes child nodes from the element, but also permanently destroys all descendant text nodes (so it is
   * impossible to insert the nodes again into any other element or into the same element anymore).
   *
   * @return The textContent property represents the text content of a node and its descendants. It returns
   * null if the element is a document, a document type, or a notation.
   *
   * @see #getInnerText()
   */
  @JsProperty
  String getTextContent();

  /**
   * @param s
   *   The textContent property represents the text content of a node and its descendants.
   */
  @JsProperty
  void setTextContent(String textContent);

  /**
   * Getter for innerText property.<br>
   * <p>
   * It is a nonstandard property that represents the text content of a node and its descendants. As a
   * getter, it approximates the text the user would get if they highlighted the contents of the element
   * with the cursor and then copied to the clipboard.
   * </p>
   *
   * @return Approximation of the text the user would get if they highlighted the contents of the element
   * with the cursor and then copied to the clipboard.
   *
   * @see #getTextContent()
   */
  @JsProperty
  String getInnerText();

  /**
   * Setter for innerText property.<br>
   * It is a nonstandard property that represents the text content of a node and its descendants.
   *
   * @param innerText
   *   The innerText value to set
   */
  @JsProperty
  void setInnerText(String innerText);

  /**
   * Returns whether the element has any child nodes
   *
   * @return Indicates if the element has any child nodes, or not.
   */
  boolean hasChildNodes();

  /**
   * Insert a Node as the last child node of this element.
   *
   * @param child
   *   The child to be inserted
   */
  void appendChild(Object child);

  /**
   * Removes a child node from the current element, which must be a child of the current node.
   *
   * @param child
   *   The child to remove
   */
  void removeChild(Object child);

  /**
   * Replaces one child Node of the current one with the second one given in parameter.
   *
   * @param newChild
   *   The new node to replace oldChild. If it already exists in the DOM, it is first removed.
   * @param oldChild
   *   The existing child to be replaced.
   *
   * @return The replaced node. This is the same node as oldChild
   */
  Node replaceChild(Node newChild,
                    Node oldChild);

  /**
   * Inserts the node newChild before the existing child node refChild. If refChild is null, insert newChild
   * at the end of the list of children.
   * If newChild is a DocumentFragment object, all of its children are inserted, in the same order, before
   * refChild. If the newChild is already in the tree, it is first removed.
   *
   * @param newChild
   *   The node to insert.
   * @param refChild
   *   The reference node, i.e., the node before which the new node must be inserted.
   *
   * @return The node being inserted
   */
  Node insertBefore(Node newChild,
                    Node refChild);

}