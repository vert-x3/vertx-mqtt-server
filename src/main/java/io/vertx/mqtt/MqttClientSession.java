/*
 * Copyright 2021 Red Hat Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.vertx.mqtt;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.impl.MqttClientSessionImpl;
import io.vertx.mqtt.messages.MqttPublishMessage;

public interface MqttClientSession {

  enum SessionState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    DISCONNECTING,
  }

  enum SubscriptionState {
    UNSUBSCRIBED,
    SUBSCRIBING,
    SUBSCRIBED,
    FAILED,
  }

  enum RequestedQoS {
    QOS_0(0),
    QOS_1(1);

    private final int value;

    RequestedQoS(int value) {
      this.value = value;
    }

    public int toInteger() {
      return this.value;
    }
  }

  class SessionEvent {

    private final SessionState sessionState;
    private final Throwable cause;

    public SessionEvent(final SessionState sessionState, final Throwable reason) {
      this.sessionState = sessionState;
      this.cause = reason;
    }

    public SessionState getSessionState() {
      return this.sessionState;
    }

    public Throwable getCause() {
      return this.cause;
    }
  }

  class SubscriptionEvent {
    private final String topic;
    private final SubscriptionState subscriptionState;
    private final Integer qos;

    public SubscriptionEvent(final String topic, final SubscriptionState subscriptionState, final Integer qos) {
      this.topic = topic;
      this.subscriptionState = subscriptionState;
      this.qos = qos;
    }

    public Integer getQos() {
      return this.qos;
    }

    public SubscriptionState getSubscriptionState() {
      return this.subscriptionState;
    }

    public String getTopic() {
      return this.topic;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      SubscriptionEvent that = (SubscriptionEvent) o;
      return topic.equals(that.topic) && subscriptionState == that.subscriptionState && Objects.equals(qos, that.qos);
    }

    @Override
    public int hashCode() {
      return Objects.hash(topic, subscriptionState, qos);
    }

    @Override
    public String toString() {
      return new StringJoiner(", ", SubscriptionEvent.class.getSimpleName() + "[", "]")
        .add("topic='" + topic + "'")
        .add("subscriptionState=" + subscriptionState)
        .add("qos=" + qos)
        .toString();
    }
  }

  static MqttClientSession create(Vertx vertx, MqttClientSessionOptions options) {
    return new MqttClientSessionImpl(vertx, options);
  }

  @Fluent
  MqttClientSession sessionStateHandler(Handler<SessionEvent> sessionStateHandler);

  @Fluent
  MqttClientSession subscriptionStateHandler(Handler<SubscriptionEvent> subscriptionStateHandler);

  @Fluent
  MqttClientSession publishHandler(Handler<Integer> publishHandler);

  void start();

  void stop();

  /**
   * Subscribes to the topics with related QoS levels
   *
   * @param topics topics and related QoS levels to subscribe to
   */
  @Fluent
  MqttClientSession subscribe(Map<String, RequestedQoS> topics);

  /**
   * Subscribes to a single topic with related QoS level.
   *
   * @param topic The topic to subscribe to.
   * @param qos The QoS to request from the server.
   */
  @Fluent
  default MqttClientSession subscribe(String topic, RequestedQoS qos) {
    return subscribe(Collections.singletonMap(topic, qos));
  }

  @Fluent
  default MqttClientSession subscribe(RequestedQoS qos, String... topics) {
    final Map<String, RequestedQoS> topicMap = new LinkedHashMap<>(topics.length);
    for (String topic : topics) {
      topicMap.put(topic, qos);
    }
    return subscribe(topicMap);
  }

  /**
   * Unsubscribe from receiving messages on given topics
   *
   * @param topics Topics you want to unsubscribe from
   */
  MqttClientSession unsubscribe(Collection<String> topics);

  /**
   * Unsubscribe from receiving messages on given topics
   *
   * @param topics Topics you want to unsubscribe from
   */
  default MqttClientSession unsubscribe(String... topics) {
    return unsubscribe(Arrays.asList(topics));
  }

  /**
   * Sets handler which will be called each time server publish something to client
   *
   * @param messageHandler handler to call
   * @return current MQTT client session instance
   */
  @Fluent
  MqttClientSession messageHandler(Handler<MqttPublishMessage> messageHandler);

  /**
   * Sends the PUBLISH message to the remote MQTT server
   *
   * @param topic topic on which the message is published
   * @param payload message payload
   * @param qosLevel QoS level
   * @param isDup if the message is a duplicate
   * @param isRetain if the message needs to be retained
   * @return a {@code Future} completed after PUBLISH packet sent with packetid (not when QoS 0)
   */
  Future<Integer> publish(String topic, Buffer payload, MqttQoS qosLevel, boolean isDup, boolean isRetain);

  /**
   * Sends the PUBLISH message to the remote MQTT server
   *
   * @param topic topic on which the message is published
   * @param payload message payload
   * @param qosLevel QoS level
   * @return a {@code Future} completed after PUBLISH packet sent with packetid (not when QoS 0)
   */
  default Future<Integer> publish(String topic, Buffer payload, MqttQoS qosLevel) {
    return publish(topic, payload, qosLevel, false, false);
  }
}