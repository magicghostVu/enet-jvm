package org.magicghostvu.jenet

object ENetProtocolFlag {
    val ENET_PROTOCOL_COMMAND_FLAG_ACKNOWLEDGE = (1 shl 7)
    val ENET_PROTOCOL_COMMAND_FLAG_UNSEQUENCED = (1 shl 6)

    val ENET_PROTOCOL_HEADER_FLAG_COMPRESSED = (1 shl 14)
    val ENET_PROTOCOL_HEADER_FLAG_SENT_TIME = (1 shl 15)
    val ENET_PROTOCOL_HEADER_FLAG_MASK = ENET_PROTOCOL_HEADER_FLAG_COMPRESSED or ENET_PROTOCOL_HEADER_FLAG_SENT_TIME

    val ENET_PROTOCOL_HEADER_SESSION_MASK = (3 shl 12)
    val ENET_PROTOCOL_HEADER_SESSION_SHIFT = 12
}