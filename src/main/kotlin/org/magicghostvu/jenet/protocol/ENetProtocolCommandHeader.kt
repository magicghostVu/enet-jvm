package org.magicghostvu.jenet.protocol

class ENetProtocolCommandHeader(
    val command: UByte,// command id (từ 0-12), vì có 13 loại command
    val channelID: UByte,
    var reliableSequenceNumber: UShort,
) : ENetProtocol() {
    override val header: ENetProtocolCommandHeader
        get() = this
}