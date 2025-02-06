package org.magicghostvu.jenet.protocol

class ENetProtocolSendReliable(
   override val header: ENetProtocolCommandHeader,
    val dataLength: UShort,
) : ENetProtocol() {
}