package org.magicghostvu.jenet

import java.net.InetSocketAddress

class Peer(val numChannels: Int) {


    val channels: MutableMap<Int, EChannel> = mutableMapOf()

    // khi tạo mới nó là connecting
    var state: PeerState = PeerState.ENET_PEER_STATE_CONNECTING


    // setup sau
    lateinit var remoteAddress: InetSocketAddress
}