package org.yunshanmc.custom.jewelry;

import java.util.regex.Pattern;
import org.bukkit.potion.PotionEffectType;

public class PotionPattern {
    private final PotionEffectType type;

    private final Pattern pattern;

    public PotionPattern(PotionEffectType type, String name) {
        this.type = type;
        this.pattern = AttributeHandle.newPattern(name);
    }

    public PotionEffectType getType() {
        return this.type;
    }

    public int tryGetValue(String line) {
        return AttributeHandle.tryGetValue(this.pattern, line);
    }
}
