package org.yunshanmc.custom.jewelry;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.minecraft.server.v1_7_R4.ItemStack;
import net.minecraft.server.v1_7_R4.NBTCompressedStreamTools;
import net.minecraft.server.v1_7_R4.NBTTagCompound;
import net.minecraft.util.org.apache.commons.codec.binary.Base64;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class AddonInvManager {
    private static final ListeningExecutorService EXECUTOR_SERVICE = MoreExecutors.listeningDecorator(new ThreadPoolExecutor(5, 10, 5L, TimeUnit.MINUTES, new LinkedBlockingQueue<>(), (new ThreadFactoryBuilder())

            .setNameFormat("Jewelry-thread-pool-%d")
            .setDaemon(false)
            .build(), new ThreadPoolExecutor.AbortPolicy()));

    private final Path dir;

    private Map<String, WeakHashMap<String, Inventory>> playerInvMap = new HashMap<>();

    private Map<String, String> titleMap = new HashMap<>();

    private Map<String, String> loreKeyMap = new HashMap<>();

    public AddonInvManager(Path dir) {
        this.dir = dir;
    }

    public void register(String key, String title, String loreKey) {
        this.titleMap.put(key, title);
        this.loreKeyMap.put(key, loreKey);
    }

    public String getLoreKey(String invName) {
        return this.loreKeyMap.get(invName);
    }

    public Inventory getInv(Player player, String invName) {
        String title = this.titleMap.get(invName);
        if (title == null)
            return null;
        WeakHashMap<String, Inventory> map = this.playerInvMap.computeIfAbsent(player.getName(), k -> new WeakHashMap<>());
        Inventory inv = map.get(invName);
        if (inv == null) {
            inv = (new CustomInvHolder(title, invName, this.loreKeyMap.get(invName), this)).getInventory();
            map.put(invName, inv);
            Path path = this.dir.resolve(invName).resolve(player.getName() + ".yml");
            if (Files.exists(path, new java.nio.file.LinkOption[0]))
                try {
                    byte[] bytes = Files.readAllBytes(path);
                    YamlConfiguration yml = YamlConfiguration.loadConfiguration(new StringReader(new String(bytes, StandardCharsets.UTF_8)));
                    for (String k : yml.getKeys(false)) {
                        int slot = Integer.parseInt(k);
                        String strItem = yml.getString(k);
                        ByteArrayInputStream buf = new ByteArrayInputStream(Base64.decodeBase64(strItem));
                        NBTTagCompound nbt = NBTCompressedStreamTools.a(buf);
                        ItemStack nmsItem = ItemStack.createStack(nbt);
                        if (nmsItem == null) {
                            System.out.println("[Jewelry] LOAD ERROR ITEM(" + invName + ":" + player.getName() + "):" + strItem);
                            continue;
                        }
                        inv.setItem(slot, CraftItemStack.asBukkitCopy(nmsItem));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return inv;
    }

    public void saveInv(Player player, Inventory inv, String invName) {
        EXECUTOR_SERVICE.submit(() -> {
            YamlConfiguration yml = new YamlConfiguration();
            int size = inv.getSize();
            for (int i = 0; i < size; i++) {
                org.bukkit.inventory.ItemStack item = inv.getItem(i);
                if (item != null && item.getType() != Material.AIR) {
                    ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
                    if (nmsItem == null) {
                        System.out.println("[Jewelry] SAVE ERROR ITEM(" + invName + ":" + player.getName() + "):" + item);
                    } else {
                        NBTTagCompound nbt = nmsItem.save(new NBTTagCompound());
                        ByteArrayOutputStream buf = new ByteArrayOutputStream();
                        NBTCompressedStreamTools.a(nbt, buf);
                        yml.set(String.valueOf(i), Base64.encodeBase64String(buf.toByteArray()));
                    }
                }
            }
            String str = yml.saveToString();
            Path path = this.dir.resolve(invName);
            try {
                if (!Files.exists(path, new java.nio.file.LinkOption[0]))
                    Files.createDirectories(path, (FileAttribute<?>[])new FileAttribute[0]);
                path = path.resolve(player.getName() + ".yml");
                Files.write(path, str.getBytes(StandardCharsets.UTF_8), new java.nio.file.OpenOption[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
