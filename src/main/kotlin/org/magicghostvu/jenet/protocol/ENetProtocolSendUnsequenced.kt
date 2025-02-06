package org.magicghostvu.jenet.protocol

class ENetProtocolSendUnsequenced(
   override val header: ENetProtocolCommandHeader,
    var unsequencedGroup: UShort,
    val dataLength: UShort,
) : ENetProtocol() {
}