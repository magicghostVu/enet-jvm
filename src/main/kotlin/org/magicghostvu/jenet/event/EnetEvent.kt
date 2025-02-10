package org.magicghostvu.jenet.event

import java.net.InetSocketAddress

sealed class EnetEvent {
    abstract val address: InetSocketAddress
}