package com.virusbear.mayan.launcher.config.model

import com.rabbitmq.client.ConnectionFactory

data class RabbitMqConfig(
    val host: String = ConnectionFactory.DEFAULT_HOST,
    val port: Int = ConnectionFactory.DEFAULT_AMQP_PORT,
    val username: String = ConnectionFactory.DEFAULT_USER,
    val password: String = ConnectionFactory.DEFAULT_PASS,
    val vhost: String = ConnectionFactory.DEFAULT_VHOST,
    val queue: String = "mayanprocessor"
)