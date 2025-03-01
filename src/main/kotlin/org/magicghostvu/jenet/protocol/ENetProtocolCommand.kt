package org.magicghostvu.jenet.protocol

object ENetProtocolCommand {
    val ENET_PROTOCOL_COMMAND_NONE = 0
    val ENET_PROTOCOL_COMMAND_ACKNOWLEDGE = 1
    val ENET_PROTOCOL_COMMAND_CONNECT = 2
    val ENET_PROTOCOL_COMMAND_VERIFY_CONNECT = 3
    val ENET_PROTOCOL_COMMAND_DISCONNECT = 4
    val ENET_PROTOCOL_COMMAND_PING = 5
    val ENET_PROTOCOL_COMMAND_SEND_RELIABLE = 6
    val ENET_PROTOCOL_COMMAND_SEND_UNRELIABLE = 7
    val ENET_PROTOCOL_COMMAND_SEND_FRAGMENT = 8
    val ENET_PROTOCOL_COMMAND_SEND_UNSEQUENCED = 9
    val ENET_PROTOCOL_COMMAND_BANDWIDTH_LIMIT = 10
    val ENET_PROTOCOL_COMMAND_THROTTLE_CONFIGURE = 11
    val ENET_PROTOCOL_COMMAND_SEND_UNRELIABLE_FRAGMENT = 12
    val ENET_PROTOCOL_COMMAND_COUNT = 13

    val ENET_PROTOCOL_COMMAND_MASK = 0x0F
}