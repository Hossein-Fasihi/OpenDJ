/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at
 * trunk/opends/resource/legal-notices/OpenDS.LICENSE
 * or https://OpenDS.dev.java.net/OpenDS.LICENSE.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at
 * trunk/opends/resource/legal-notices/OpenDS.LICENSE.  If applicable,
 * add the following below this CDDL HEADER, with the fields enclosed
 * by brackets "[]" replaced with your own identifying information:
 *      Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 *
 *      Portions Copyright 2007 Sun Microsystems, Inc.
 */

package org.opends.messages;

import java.util.Locale;
import java.util.List;
import java.util.LinkedList;
import java.io.Serializable;

/**
 * A builder used specifically for messages.  As messages are
 * appended they are translated to their string representation
 * for storage using the locale specified in the constructor.
 *
 * Note that before you use this class you should consider whether
 * it is appropriate.  In general composing messages by appending
 * message to each other may not produce a message that is
 * formatted appropriately for all locales.  It is usually better
 * to create messages by composition.  In other words you should
 * create a base message that contains one or more string argument
 * specifiers (%s) and define other message objects to use as
 * replacement variables.  In this way language translators have
 * a change to reformat the message for a particular locale if
 * necessary.
 */
public class MessageBuilder implements Appendable, CharSequence,
        Serializable
{

  private static final long serialVersionUID = -3292823563904285315L;

  /** Used internally to store appended messages. */
  StringBuilder sb = new StringBuilder();

  /** Used internally to store appended messages. */
  List<Message> messages = new LinkedList<Message>();

  /** Used to render the string representation of appended messages. */
  Locale locale;

  /**
   * Constructs an instance that will build messages
   * in the default locale.
   */
  public MessageBuilder() {
    this(Locale.getDefault());
  }

  /**
   * Constructs an instance that will build messages
   * in the default locale having an initial message.
   *
   * @param message initial message
   */
  public MessageBuilder(Message message) {
    append(message);
  }

  /**
   * Constructs an instance that will build messages
   * in the default locale having an initial message.
   *
   * @param message initial message
   */
  public MessageBuilder(String message) {
    append(message);
  }

  /**
   * Constructs an instance from another <code>MessageBuilder</code>.
   *
   * @param mb from which to construct a new message builder
   */
  public MessageBuilder(MessageBuilder mb) {
    for (Message msg : mb.messages) {
      this.messages.add(msg);
    }
    this.sb.append(sb);
    this.locale = mb.locale;
  }

  /**
   * Constructs an instance that will build messages
   * in a specified locale.
   *
   * @param locale used for translating appended messages
   */
  public MessageBuilder(Locale locale) {
    this.locale = locale;
  }

  /**
   * Append a message to this builder.  The string
   * representation of the locale specifed in the
   * constructor will be stored in this builder.
   *
   * @param message to be appended
   * @return reference to this builder
   */
  public MessageBuilder append(Message message) {
    if (message != null) {
      sb.append(message.toString(locale));
      messages.add(message);
    }
    return this;
  }

  /**
   * Append an integer to this builder.
   *
   * @param number to append
   * @return reference to this builder
   */
  public MessageBuilder append(int number) {
    append(String.valueOf(number));
    return this;
  }

  /**
   * Append an object to this builder.
   *
   * @param object to append
   * @return reference to this builder
   */
  public MessageBuilder append(Object object) {
    if (object != null) {
      append(String.valueOf(object));
    }
    return this;
  }


  /**
   * Append a string to this builder.
   *
   * @param rawString to append
   * @return reference to this builder
   */
  public MessageBuilder append(CharSequence rawString) {
    if (rawString != null) {
      sb.append(rawString);
      messages.add(Message.raw(rawString));
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public MessageBuilder append(CharSequence csq, int start, int end)
  {
    return append(csq.subSequence(start, end));
  }

  /**
   * {@inheritDoc}
   */
  public MessageBuilder append(char c) {
    return append(String.valueOf(c));
  }

  /**
   * {@inheritDoc}
   */
  public String toString() {
    return sb.toString();
  }

  /**
   * Returns a string representation of the appended content
   * in the specific locale.  Only <code>Message</code>s
   * appended to this builder are rendered in the requested
   * locale.  Raw strings appended to this buffer are not
   * translated to different locale.
   *
   * @param locale requested
   * @return String representation
   */
  public String toString(Locale locale) {
    StringBuilder sb = new StringBuilder();
    for (Message m : messages) {
      sb.append(m.toString(locale));
    }
    return sb.toString();
  }

  /**
   * Returns a raw message representation of the appended
   * content.
   *
   * @return Message raw message representing builder content
   */
  public Message toMessage() {
    return Message.raw(sb.toString());
  }

  /**
   * Returns a raw message representation of the appended
   * content in a specific locale.  Only <code>Message</code>s
   * appended to this builder are rendered in the requested
   * locale.  Raw strings appended to this buffer are not
   * translated to different locale.
   *
   * @param locale requested
   * @return Message raw message representing builder content
   */
  public Message toMessage(Locale locale) {
    return Message.raw(toString(locale));
  }

  /**
   * Returns the length of the string representation of this builder
   * using the default locale.
   *
   * @return  the number of <code>char</code>s in this message
   */
  public int length() {
    return length(Locale.getDefault());
  }

  /**
   * Returns the <code>char</code> value at the specified index of
   * the string representation of this builder using the default locale.
   *
   * @param   index   the index of the <code>char</code> value to be returned
   *
   * @return  the specified <code>char</code> value
   *
   * @throws  IndexOutOfBoundsException
   *          if the <tt>index</tt> argument is negative or not less than
   *          <tt>length()</tt>
   */
  public char charAt(int index) throws IndexOutOfBoundsException {
    return charAt(Locale.getDefault(), index);
  }

  /**
   * Returns a new <code>CharSequence</code> that is a subsequence of
   * the string representation of this builder using the default locale.
   * The subsequence starts with the <code>char</code>
   * value at the specified index and ends with the <code>char</code>
   * value at index <tt>end - 1</tt>.  The length (in <code>char</code>s)
   * of the returned sequence is <tt>end - start</tt>, so if
   * <tt>start == end</tt> then an empty sequence is returned.
   *
   * @param   start   the start index, inclusive
   * @param   end     the end index, exclusive
   *
   * @return  the specified subsequence
   *
   * @throws  IndexOutOfBoundsException
   *          if <tt>start</tt> or <tt>end</tt> are negative,
   *          if <tt>end</tt> is greater than <tt>length()</tt>,
   *          or if <tt>start</tt> is greater than <tt>end</tt>
   */
  public CharSequence subSequence(int start, int end)
          throws IndexOutOfBoundsException
  {
    return subSequence(Locale.getDefault(), start, end);
  }

  /**
   * Returns the length of the string representation of this builder
   * using a specific locale.
   *
   * @param   locale for which the rendering of this message will be
   *          used in determining the length
   * @return  the number of <code>char</code>s in this message
   */
  public int length(Locale locale) {
    return toString(locale).length();
  }

  /**
   * Returns the <code>char</code> value at the specified index of
   * the string representation of this builder using a specific locale.
   *
   * @param   locale for which the rendering of this message will be
   *          used in determining the character
   * @param   index   the index of the <code>char</code> value to be returned
   *
   * @return  the specified <code>char</code> value
   *
   * @throws  IndexOutOfBoundsException
   *          if the <tt>index</tt> argument is negative or not less than
   *          <tt>length()</tt>
   */
  public char charAt(Locale locale, int index)
          throws IndexOutOfBoundsException
  {
    return toString(locale).charAt(index);
  }

  /**
   * Returns a new <code>CharSequence</code> that is a subsequence of
   * the string representation of this builder using a specific locale.
   * The subsequence starts with the <code>char</code>
   * value at the specified index and ends with the <code>char</code>
   * value at index <tt>end - 1</tt>.  The length (in <code>char</code>s)
   * of the returned sequence is <tt>end - start</tt>, so if
   * <tt>start == end</tt> then an empty sequence is returned.
   *
   * @param   locale for which the rendering of this message will be
   *          used in determining the character
   * @param   start   the start index, inclusive
   * @param   end     the end index, exclusive
   *
   * @return  the specified subsequence
   *
   * @throws  IndexOutOfBoundsException
   *          if <tt>start</tt> or <tt>end</tt> are negative,
   *          if <tt>end</tt> is greater than <tt>length()</tt>,
   *          or if <tt>start</tt> is greater than <tt>end</tt>
   */
  public CharSequence subSequence(Locale locale, int start, int end)
    throws IndexOutOfBoundsException
  {
    return toString(locale).subSequence(start, end);
  }


}
