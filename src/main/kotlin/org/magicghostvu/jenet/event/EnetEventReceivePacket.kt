package org.magicghostvu.jenet.event

import java.net.InetSocketAddress

class EnetEventReceivePacket(
    override val address: InetSocketAddress,
    val data: ByteArray,
    val channelId: UByte,
) : EnetEvent() {
}