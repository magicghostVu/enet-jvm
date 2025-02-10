package org.magicghostvu.jenet

enum class ENetPeerState(val code: Int) {
    ENET_PEER_STATE_DISCONNECTED(0),
    ENET_PEER_STATE_CONNECTING(1),
    ENET_PEER_STATE_ACKNOWLEDGING_CONNECT(2),
    ENET_PEER_STATE_CONNECTION_PENDING(3),
    ENET_PEER_STATE_CONNECTION_SUCCEEDED(4),
    ENET_PEER_STATE_CONNECTED(5),
    ENET_PEER_STATE_DISCONNECT_LATER(6),
    ENET_PEER_STATE_DISCONNECTING(7),
    ENET_PEER_STATE_ACKNOWLEDGING_DISCONNECT(8),
    ENET_PEER_STATE_ZOMBIE(9),
    ;

    companion object {
        private val t = ENetPeerState.entries.associateBy(ENetPeerState::code)
        fun fromCode(code: Int) = t[code]
    }
}