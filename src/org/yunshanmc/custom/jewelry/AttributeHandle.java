package org.yunshanmc.custom.jewelry;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.yunshanmc.custom.buff.listener.BuffPlayerListener;
import think.rpgitems.api.RPGItems;
import think.rpgitems.item.RPGItem;

public class AttributeHandle {
    private static Pattern healthPattern;

    private static Pattern damagePattern;

    private static Pattern forgeRatePattern;

    private static List<PotionPattern> potionPatterns;

    private static final Pattern slotQuickBarPattern = Pattern.compile("\\d+");

    private static final Pattern slotInvPattern = Pattern.compile("(\\d+)[^\\d]+(\\d+)");

    private static boolean rpgItemsEnable;

    public static String expNeed;
    static {
        try {
            RPGItems.class.getName();
            rpgItemsEnable = true;
        } catch (Throwable t) {
            rpgItemsEnable = false;
        }
    }

    public static void init(ConfigurationSection config) {
        expNeed = config.getString("expNeed");
        healthPattern = newPattern(config.getString("health-name", "生命值"));
                damagePattern = newPattern(config.getString("damage-name", "物理攻击"));
                        forgeRatePattern = newPattern(config.getString("forge-rate-name", "锻造成功率"));
                                ConfigurationSection effects = config.getConfigurationSection("effects");
        potionPatterns = new ArrayList<>();
        for (String typeName : effects.getKeys(false)) {
            PotionEffectType type = PotionEffectType.getByName(typeName);
            if (type == null) {
                System.out.println("Invalid Potion Effect Type: " + typeName);
                continue;
            }
            potionPatterns.add(new PotionPattern(type, effects.getString(typeName)));
        }
    }

    public static InventoryData fetchInvData(Player player, Inventory inv, String loreKey, List<ItemStack> invalids) {
        int size = inv.getSize();
        List<ItemData> datas = new ArrayList<>();
        for (int slot = 0; slot < size; slot++) {
            ItemStack item = inv.getItem(slot);

            if (item == null || item.getType() == Material.AIR)
                continue;
            if(item.getItemMeta().hasLore()){
                boolean notAdd = false;
                for(String lore : item.getItemMeta().getLore()){
                    if(lore.contains(AttributeHandle.expNeed)){
                        int exp = (int)BuffPlayerListener.getHas(lore);
                        if(player.getLevel() < exp){
                            notAdd = true;
                            break;
                        }
                    }
                }
                if(notAdd){
                    continue;
                }
            }
            if (item.getAmount() > 1 || !item.hasItemMeta()) {
                if (invalids != null) {
                    invalids.add(item);
                    inv.setItem(slot, null);
                }
                continue;
            }
            ItemMeta meta = item.getItemMeta();
            if (!meta.hasLore()) {
                if (invalids != null) {
                    invalids.add(item);
                    inv.setItem(slot, null);
                }
                continue;
            }
            if (rpgItemsEnable) {
                RPGItem rpgItem = RPGItems.toRPGItem(item);
                if (rpgItem != null && player.getLevel() < rpgItem.c) {
                    if (invalids != null) {
                        invalids.add(item);
                        inv.setItem(slot, null);
                    }
                    continue;
                }
            }
            List<String> lore = meta.getLore();
            if (!checkLore(slot, lore, loreKey)) {
                if (invalids != null) {
                    invalids.add(item);
                    inv.setItem(slot, null);
                }
            } else {
                datas.add(computeData(item, lore));
            }
            continue;
        }
        return InventoryData.build(datas);
    }

    private static ItemData computeData(ItemStack item, List<String> lore) {
        int health = 0;
        int damage = 0;
        int forgeRate = 0;
        Map<PotionEffectType, Integer> effects = new HashMap<>(4);
        for (String line : lore) {
            int val = tryGetValue(healthPattern, line);
            if (val > 0) {
                health += val;
                continue;
            }
            val = tryGetValue(damagePattern, line);
            if (val > 0) {
                damage += val;
                continue;
            }
            val = tryGetValue(forgeRatePattern, line);
            if (val > 0) {
                forgeRate += val;
                continue;
            }
            for (PotionPattern pattern : potionPatterns) {
                val = pattern.tryGetValue(line);
                if (val > 0)
                    effects.put(pattern.getType(), Integer.valueOf(val - 1));
            }
        }
        return new ItemData(health, damage, forgeRate, effects, item);
    }

    private static boolean checkLore(int slot, List<String> lore, String key) {
        Pattern pattern;
        Predicate<Matcher> checker;
        key = Strings.emptyToNull(key);
        if (key == null) {
            if (slot <= 8) {
                key = "物品栏";
                pattern = slotQuickBarPattern;
                checker = (matcher -> {
                    int allowSlot = Integer.parseInt(matcher.group());
                    return (allowSlot - 1 == slot);
                });
            } else {
                key = "背包";
                pattern = slotInvPattern;
                checker = (matcher -> {
                    int x = Integer.parseInt(matcher.group(1));
                    int y = Integer.parseInt(matcher.group(2));
                    return (x * 9 + y - 1 == slot);
                });
            }
        } else {
            pattern = slotInvPattern;
            checker = (matcher -> {
                int x = Integer.parseInt(matcher.group(1));
                int y = Integer.parseInt(matcher.group(2));
                return ((x - 1) * 9 + y - 1 == slot);
            });
        }
        boolean valid = false;
        int end = Math.min((int)Math.ceil(lore.size() / 4.0D * 3.0D), lore.size());
        for (int i = 0; i < end &&
                !valid; i++) {
            String locLore = lore.get(i);
            if (locLore.contains(key)) {
                Matcher matcher = pattern.matcher(ChatColor.stripColor(locLore));
                boolean flag = false;
                while (matcher.find()) {
                    if (checker.test(matcher)) {
                        flag = true;
                        break;
                    }
                }
                if (flag || locLore.contains("任意位置"))
                        valid = true;
            }
        }
        return valid;
    }

    public static Pattern newPattern(String name) {
        return Pattern.compile("^.*" + Pattern.quote(name) + "[^\\d]*(\\d+)$");
    }

    public static int tryGetValue(Pattern pattern, String line) {
        Matcher matcher = pattern.matcher(ChatColor.stripColor(line));
        if (matcher.matches())
            return Integer.parseInt(matcher.group(1));
        return 0;
    }
}
