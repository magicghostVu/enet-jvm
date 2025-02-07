package org.magicghostvu.jenet.protocol

class ENetProtocolCommandHeader(
    var command: UByte,// command id (từ 0-12), vì có 13 loại command
    var channelID: UByte,
    var reliableSequenceNumber: UShort,
) : ENetProtocol() {
    override val header: ENetProtocolCommandHeader
        get() = this

    constructor() : this(0.toUByte(), 0.toUByte(), 0.toUShort())
}