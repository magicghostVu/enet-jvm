package org.magicghostvu.jenet.event

import java.net.InetSocketAddress

class EnetEventDisconnect(override val address: InetSocketAddress, val data: Int) : EnetEvent() {
}