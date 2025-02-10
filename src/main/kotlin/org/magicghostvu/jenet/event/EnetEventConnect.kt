package org.magicghostvu.jenet.event

import java.net.InetSocketAddress

class EnetEventConnect(override val address: InetSocketAddress, val eventData: UInt): EnetEvent() {
}