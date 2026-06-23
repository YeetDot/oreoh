package com.yeetdot.oreoh.set.id;

public sealed interface GemId extends NaturalId {
    record Set(String name) implements GemId {
        @Override
        public boolean isIngot() {
            return false;
        }
    }
}
