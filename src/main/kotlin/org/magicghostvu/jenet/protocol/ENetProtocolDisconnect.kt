package org.magicghostvu.jenet.protocol

class ENetProtocolDisconnect(
   override val header: ENetProtocolCommandHeader,
    val data: UInt,
): ENetProtocol() {
}