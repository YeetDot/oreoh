package com.yeetdot.oreoh.set.tag;

public sealed interface GemTag extends NaturalTag {
    record Set(String name) implements GemTag {
        @Override
        public boolean isIngot() {
            return false;
        }
    }
}
