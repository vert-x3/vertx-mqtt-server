/*
 * Copyright 2016 Red Hat Inc.
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

package enmasse.mqtt;

import enmasse.mqtt.messages.MqttMessage;
import enmasse.mqtt.messages.MqttPublishMessage;
import enmasse.mqtt.messages.MqttSubscribeMessage;
import enmasse.mqtt.messages.MqttUnsubscribeMessage;
import io.netty.handler.codec.mqtt.MqttConnectReturnCode;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.streams.ReadStream;
import io.vertx.core.streams.WriteStream;

import java.util.List;

/**
 * Represents an MQTT endpoint for point-to-point communication with the remote MQTT client
 */
@VertxGen
public interface MqttEndpoint extends ReadStream<MqttMessage>, WriteStream<MqttMessage> {

    @Override
    MqttEndpoint exceptionHandler(Handler<Throwable> handler);

    @Override
    MqttEndpoint handler(Handler<MqttMessage> handler);

    @Override
    MqttEndpoint pause();

    @Override
    MqttEndpoint resume();

    @Override
    MqttEndpoint endHandler(Handler<Void> handler);

    @Override
    MqttEndpoint write(MqttMessage mqttMessage);

    @Override
    MqttEndpoint setWriteQueueMaxSize(int i);

    @Override
    MqttEndpoint drainHandler(Handler<Void> handler);

    @Override
    void end();

    /**
     * Close the endpoint, so the connection with remote MQTT client
     */
    void close();

    /**
     * Client identifier as provided by the remote MQTT client
     * @return
     */
    String clientIdentifier();

    /**
     * Authentication information as provided by the remote MQTT client
     * @return
     */
    MqttAuth auth();

    /**
     * Will information as provided by the remote MQTT client
     * @return
     */
    MqttWill will();

    /**
     * Protocol version required by the remote MQTT client
     * @return
     */
    int protocolVersion();

    /**
     * If clean session is requested by the remote MQTT client
     * @return
     */
    boolean isCleanSession();

    /**
     * Sends the CONNACK message to the remote MQTT client
     *
     * @param connectReturnCode     the connect return code
     * @param sessionPresent        if a previous session is present
     * @return  a reference to this, so the API can be used fluently
     */
    MqttEndpoint writeConnack(MqttConnectReturnCode connectReturnCode, boolean sessionPresent);

    /**
     * Set a disconnect handler on the MQTT endpoint. This handler is called when a DISCONNECT
     * message is received by the remote MQTT client
     *
     * @param handler   the handler
     * @return  a reference to this, so the API can be used fluently
     */
    MqttEndpoint disconnectHandler(Handler<Void> handler);

    /**
     * Set a subscribe handler on the MQTT endpoint. This handler is called when a SUBSCRIBE
     * message is received by the remote MQTT client
     *
     * @param handler   the handler
     * @return  a reference to this, so the API can be used fluently
     */
    MqttEndpoint subscribeHandler(Handler<MqttSubscribeMessage> handler);

    /**
     * Set a unsubscribe handler on the MQTT endpoint. This handler is called when a UNSUBSCRIBE
     * message is received by the remote MQTT client
     *
     * @param handler   the handler
     * @return  a reference to this, so the API can be used fluently
     */
    MqttEndpoint unsubscribeHandler(Handler<MqttUnsubscribeMessage> handler);

    /**
     * Set the publish handler on the MQTT endpoint. This handler is called when a PUBLISH
     * message is received by the remote MQTT client
     *
     * @param handler   the handler
     * @return  a reference to this, so the API can be used fluently
     */
    MqttEndpoint publishHandler(Handler<MqttPublishMessage> handler);

    /**
     * Set the puback handler on the MQTT endpoint. This handler is called when a PUBACK
     * message is received by the remote MQTT client
     *
     * @param handler   the handler
     * @return  a reference to this, so the API can be used fluently
     */
    MqttEndpoint pubackHandler(Handler<Integer> handler);

    /**
     * Set the pubrec handler on the MQTT endpoint. This handler is called when a PUBREC
     * message is received by the remote MQTT client
     *
     * @param handler   the handler
     * @return  a reference to this, so the API can be used fluently
     */
    MqttEndpoint pubrecHandler(Handler<Integer> handler);

    /**
     * Set the pubrel handler on the MQTT endpoint. This handler is called when a PUBREL
     * message is received by the remote MQTT client
     *
     * @param handler   the handler
     * @return  a reference to this, so the API can be used fluently
     */
    MqttEndpoint pubrelHandler(Handler<Integer> handler);

    /**
     * Set the pubcomp handler on the MQTT endpoint. This handler is called when a PUBCOMP
     * message is received by the remote MQTT client
     *
     * @param handler   the handler
     * @return  a reference to this, so the API can be used fluently
     */
    MqttEndpoint pubcompHandler(Handler<Integer> handler);

    /**
     * Sends the SUBACK message to the remote MQTT client
     *
     * @param subscribeMessageId    identifier of the SUBSCRIBE message to acknowledge
     * @param grantedQoSLevels  granted QoS levels for the requested topics
     * @return  a reference to this, so the API can be used fluently
     */
    MqttEndpoint writeSuback(int subscribeMessageId, List<Integer> grantedQoSLevels);

    /**
     * Sends the UNSUBACK message to the remote MQTT client
     *
     * @param unsubscribeMessageId    identifier of the UNSUBSCRIBE message to acknowledge
     * @return  a reference to this, so the API can be used fluently
     */
    MqttEndpoint writeUnsuback(int unsubscribeMessageId);

    /**
     * Sends the PUBACK message to the remote MQTT client
     *
     * @param publishMessageId  identifier of the PUBLISH message to acknowledge
     * @return  a reference to this, so the API can be used fluently
     */
    MqttEndpoint writePuback(int publishMessageId);

    /**
     * Sends the PUBREC message to the remote MQTT client
     *
     * @param publishMessageId  identifier of the PUBLISH message to acknowledge
     * @return  a reference to this, so the API can be used fluently
     */
    MqttEndpoint writePubrec(int publishMessageId);

    /**
     * Sends the PUBREL message to the remote MQTT client
     *
     * @param publishMessageId  identifier of the PUBLISH message to acknowledge
     * @return  a reference to this, so the API can be used fluently
     */
    MqttEndpoint writePubrel(int publishMessageId);

    /**
     * Sends the PUBCOMP message to the remote MQTT client
     *
     * @param publishMessageId  identifier of the PUBLISH message to acknowledge
     * @return  a reference to this, so the API can be used fluently
     */
    MqttEndpoint writePubcomp(int publishMessageId);

    /**
     * Sends the PUBLISH message to the remote MQTT client
     *
     * @param topic topic on which the message is published
     * @param payload   message payload
     * @param qosLevel  quality of service level
     * @param isDup if the message is a duplicate
     * @param isRetain  if the message needs to be retained
     * @return  a reference to this, so the API can be used fluently
     */
    MqttEndpoint writePublish(String topic, Buffer payload, MqttQoS qosLevel, boolean isDup, boolean isRetain);
}
