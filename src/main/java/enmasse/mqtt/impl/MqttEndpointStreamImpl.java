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

package enmasse.mqtt.impl;

import enmasse.mqtt.MqttEndpoint;
import enmasse.mqtt.MqttEndpointStream;
import io.vertx.core.Handler;

/**
 * A stream used for notifying an MQTT client connection to the MQTT server
 */
public class MqttEndpointStreamImpl implements MqttEndpointStream {

    private Handler<MqttEndpoint> handler;

    Handler<MqttEndpoint> handler() {
        return this.handler;
    }

    @Override
    public MqttEndpointStream exceptionHandler(Handler<Throwable> handler) {
        return null;
    }

    @Override
    public MqttEndpointStream handler(Handler<MqttEndpoint> handler) {
        return null;
    }

    @Override
    public MqttEndpointStream pause() {
        return null;
    }

    @Override
    public MqttEndpointStream resume() {
        return null;
    }

    @Override
    public MqttEndpointStream endHandler(Handler<Void> handler) {
        return null;
    }
}