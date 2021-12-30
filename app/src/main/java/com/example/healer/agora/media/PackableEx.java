package com.example.healer.agora.media;

public interface PackableEx extends Packable {
    void unmarshal(ByteBuf in);
}
