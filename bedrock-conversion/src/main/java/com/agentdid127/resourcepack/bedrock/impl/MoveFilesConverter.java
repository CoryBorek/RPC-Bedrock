package com.agentdid127.resourcepack.bedrock.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.agentdid127.resourcepack.bedrock.utilities.FileUtil;
import com.agentdid127.resourcepack.bedrock.utilities.ImageConverter;
import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.Util;
import com.agentdid127.resourcepack.library.pack.Pack;

public class MoveFilesConverter extends Converter {
    public MoveFilesConverter(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path root = pack.getWorkingPath();

        Path assetsPath = root.resolve("assets");
        if (!assetsPath.toFile().exists())
            return;

        Path minecraftPath = assetsPath.resolve("minecraft");
        if (!minecraftPath.toFile().exists())
            return;

        // Lang
        Path langPath = minecraftPath.resolve("lang");
        if (langPath.toFile().exists()) {

        }

        // Splash/Credits/Other
        Path textsPath = minecraftPath.resolve("texts");
        if (textsPath.toFile().exists()) {
            // TODO
        }

        // Sounds
        FileUtil.moveIfExists(minecraftPath.resolve("sounds.json"), root.resolve("sounds.json"));
        // TODO: remap sounds json

        // Textures
        Path texturesPath = minecraftPath.resolve("textures");
        if (texturesPath.toFile().exists()) {
            // Move Everything First
            Path newTexturesPath = root.resolve("textures");
            FileUtil.moveIfExists(texturesPath, newTexturesPath);

            {
                // Move (block -> blocks) Path
                Path blockPath = newTexturesPath.resolve("block");
                FileUtil.moveIfExists(blockPath, newTexturesPath.resolve("blocks"));

                // Move (item -> items) Path
                Path itemPath = newTexturesPath.resolve("item");
                FileUtil.moveIfExists(itemPath, newTexturesPath.resolve("items"));
            }

            {
                // Move Block Breaking Overlay
                Path blocksPath = newTexturesPath.resolve("blocks");
                if (blocksPath.toFile().exists()) {
                    Path environmentPath = newTexturesPath.resolve("environment");
                    for (int i = 0; i < 10; ++i) {
                        String name = "destroy_stage_" + i + ".png";
                        Path texturePath = blocksPath.resolve(name);
                        if (texturePath.toFile().exists() && !environmentPath.toFile().exists())
                            environmentPath.toFile().mkdirs();
                        FileUtil.moveIfExists(texturePath, environmentPath.resolve(name));
                    }
                }
            }

            {
                // Move Empty Slot Textures
                Path itemsPath = newTexturesPath.resolve("items");
                Path uiPath = newTexturesPath.resolve("ui");
                for (String texture : new String[] {
                        "empty_armor_slot_helmet.png",
                        "empty_armor_slot_chestplate.png",
                        "empty_armor_slot_leggings.png",
                        "empty_armor_slot_boots.png",
                        "empty_armor_slot_shield.png",
                        "empty_horse_slot_armor.png",
                }) {
                    Path texturePath = itemsPath.resolve(texture);
                    if (texturePath.toFile().exists() && !uiPath.toFile().exists())
                        uiPath.toFile().mkdirs();
                    FileUtil.moveIfExists(texturePath, uiPath.resolve(texture));
                }
            }

            {
                // Move Panorama
                Path panoramaPath = newTexturesPath.resolve("gui/title/background".replace("/", File.separator));
                if (panoramaPath.toFile().exists()) {
                    Path uiPath = newTexturesPath.resolve("ui");
                    for (int i = 0; i < 6; ++i) {
                        String name = "panorama_" + i + ".png";
                        Path texturePath = panoramaPath.resolve(name);
                        if (texturePath.toFile().exists() && !uiPath.toFile().exists())
                            uiPath.toFile().mkdirs();
                        FileUtil.moveIfExists(texturePath, uiPath.resolve(name));
                    }
                    Path overlayPath = panoramaPath.resolve("panorama_overlay.png");
                    if (overlayPath.toFile().exists() && !uiPath.toFile().exists())
                        uiPath.toFile().mkdirs();
                    FileUtil.moveIfExists(overlayPath, uiPath.resolve("panorama_overlay.tga"));
                }
            }

            {
                // Mob Effects (mob_effect)
                Path mobEffectPath = newTexturesPath.resolve("mob_effect");
                if (mobEffectPath.toFile().exists()) {
                    Path newGuiPath = newTexturesPath.resolve("gui/newgui".replace("/",
                            File.separator));
                    if (!newGuiPath.toFile().exists())
                        newGuiPath.toFile().mkdirs();
                    FileUtil.moveIfExists(mobEffectPath, newGuiPath.resolve("mob_effects"));
                }
            }

            {
                // Font
                Path fontPath = newTexturesPath.resolve("font");
                if (fontPath.toFile().exists()) {
                    FileUtil.moveIfExists(fontPath.resolve("ascii.png"), fontPath.resolve("default8.png"));

                    FileUtil.moveIfExists(fontPath, root.resolve("font"));
                }
            }

            {
                // Glint
                Path miscPath = newTexturesPath.resolve("misc");
                if (miscPath.toFile().exists()) {
                    Path itemGlintPath = miscPath.resolve("enchanted_glint_item.png");
                    Path itemGlintMetaPath = miscPath.resolve("enchanted_glint_item.png.mcmeta");
                    if (itemGlintMetaPath.toFile().exists())
                        itemGlintMetaPath.toFile().delete();
                    FileUtil.moveIfExists(itemGlintPath, miscPath.resolve("enchanted_item_glint.png"));
                    Path entityGlintPath = miscPath.resolve("enchanted_glint_entity.png");
                    if (entityGlintPath.toFile().exists())
                        entityGlintPath.toFile().delete();
                    Path entityGlintMetaPath = miscPath.resolve("enchanted_glint_entity.png.mcmeta");
                    if (entityGlintMetaPath.toFile().exists())
                        entityGlintMetaPath.toFile().delete();
                }
            }

            // GUI STUFF (Messy)
            {
                Path guiPath = newTexturesPath.resolve("gui");
                Path spritesPath = guiPath.resolve("sprites");

                Path uiPath = newTexturesPath.resolve("ui");
                if (spritesPath.toFile().exists() && !uiPath.toFile().exists())
                    uiPath.toFile().mkdirs();

                FileUtil.moveIfExists(guiPath.resolve("title/minecraft.png".replace("/", File.separator)),
                        uiPath.resolve("title.png"));

                Path hudPath = spritesPath.resolve("hud");

                // Icons
                {
                    // Crosshair
                    FileUtil.moveIfExists(hudPath.resolve("crosshair.png"), uiPath.resolve("cross_hair.png"));

                    // Hearts
                    Path heartFolderPath = hudPath.resolve("heart");
                    if (heartFolderPath.toFile().exists()) {
                        FileUtil.moveIfExists(heartFolderPath.resolve("container.png"),
                                uiPath.resolve("heart_background.png"));
                        FileUtil.moveIfExists(heartFolderPath.resolve("container_blinking.png"),
                                uiPath.resolve("heart_blink.png"));
                        FileUtil.moveIfExists(heartFolderPath.resolve("full.png"), uiPath.resolve("heart.png"));
                        FileUtil.moveIfExists(heartFolderPath.resolve("half.png"), uiPath.resolve("heart_half.png"));
                        FileUtil.moveIfExists(heartFolderPath.resolve("full_blinking.png"),
                                uiPath.resolve("heart_flash.png"));
                        FileUtil.moveIfExists(heartFolderPath.resolve("half_blinking.png"),
                                uiPath.resolve("heart_flash_half.png"));
                        FileUtil.moveIfExists(heartFolderPath.resolve("vehicle_full.png"),
                                uiPath.resolve("horse_heart.png"));
                        FileUtil.moveIfExists(heartFolderPath.resolve("vehicle_half.png"),
                                uiPath.resolve("horse_heart_half.png"));
                        // . -> horse_heart_flash
                        // . -> horse_heart_flash_half
                        FileUtil.moveIfExists(heartFolderPath.resolve("poisoned_full.png"),
                                uiPath.resolve("poison_heart.png"));
                        FileUtil.moveIfExists(heartFolderPath.resolve("poisoned_half.png"),
                                uiPath.resolve("poison_heart_half.png"));
                        FileUtil.moveIfExists(heartFolderPath.resolve("poisoned_full_blinking.png"),
                                uiPath.resolve("poison_heart_flash.png"));
                        FileUtil.moveIfExists(heartFolderPath.resolve("poisoned_half_blinking.png"),
                                uiPath.resolve("poison_heart_flash_half.png"));
                        FileUtil.moveIfExists(heartFolderPath.resolve("withered_full.png"),
                                uiPath.resolve("wither_heart.png"));
                        FileUtil.moveIfExists(heartFolderPath.resolve("withered_half.png"),
                                uiPath.resolve("wither_heart_half.png"));
                        FileUtil.moveIfExists(heartFolderPath.resolve("withered_full_blinking.png"),
                                uiPath.resolve("wither_heart_flash.png"));
                        FileUtil.moveIfExists(heartFolderPath.resolve("withered_half_blinking.png"),
                                uiPath.resolve("wither_heart_flash_half.png"));
                        FileUtil.moveIfExists(heartFolderPath.resolve("absorbing_full.png"),
                                uiPath.resolve("absorption_heart.png"));
                        FileUtil.moveIfExists(heartFolderPath.resolve("absorbing_half.png"),
                                uiPath.resolve("absorption_heart_half.png"));
                        FileUtil.moveIfExists(heartFolderPath.resolve("hardcore_full.png"),
                                uiPath.resolve("hardcore_heart.png"));
                        FileUtil.moveIfExists(heartFolderPath.resolve("hardcore_half.png"),
                                uiPath.resolve("hardcore_heart_half.png"));
                        FileUtil.moveIfExists(heartFolderPath.resolve("hardcore_full_blinking.png"),
                                uiPath.resolve("hardcore_heart_flash.png"));
                        FileUtil.moveIfExists(heartFolderPath.resolve("hardcore_half_blinking.png"),
                                uiPath.resolve("hardcore_heart_flash_half.png"));
                    }

                    // Armor
                    for (String texture : new String[] {
                            "armor_empty.png",
                            "armor_full.png",
                            "armor_half.png"
                    })
                        FileUtil.moveIfExists(hudPath.resolve(texture), uiPath.resolve(texture));

                    // Bubbles
                    FileUtil.moveIfExists(hudPath.resolve("air.png"), uiPath.resolve("bubble.png"));
                    FileUtil.moveIfExists(hudPath.resolve("air_bursting.png"), uiPath.resolve("bubble_pop.png"));

                    // Hunger
                    FileUtil.moveIfExists(hudPath.resolve("food_empty.png"), uiPath.resolve("hunger_background.png"));
                    FileUtil.moveIfExists(hudPath.resolve("food_empty_hunger.png"),
                            uiPath.resolve("hunger_effect_background.png"));
                    FileUtil.moveIfExists(hudPath.resolve("food_full_hunger.png"),
                            uiPath.resolve("hunger_effect_full.png"));
                    FileUtil.moveIfExists(hudPath.resolve("food_half_hunger.png"),
                            uiPath.resolve("hunger_effect_half.png"));
                    FileUtil.moveIfExists(hudPath.resolve("food_full.png"),
                            uiPath.resolve("hunger_full.png"));
                    FileUtil.moveIfExists(hudPath.resolve("food_half.png"),
                            uiPath.resolve("hunger_half.png"));

                    // XP
                    FileUtil.moveIfExists(hudPath.resolve("jump_bar_background.png"),
                            uiPath.resolve("horse_jump_empty.png"));
                    FileUtil.moveIfExists(hudPath.resolve("horse_jump_full.png"),
                            uiPath.resolve("horse_jump_full.png"));
                    FileUtil.moveIfExists(hudPath.resolve("jump_bar_cooldown.png"),
                            uiPath.resolve("dash_cooldown.png"));

                    // Ping Bars?
                }

                // HUD
                {
                    Path hotbarPath = hudPath.resolve("hotbar.png");
                    if (hotbarPath.toFile().exists()) {
                        int hotbar_width = 182;
                        int hotbar_height = 22;
                        ImageConverter hotbar = new ImageConverter(hotbar_width, hotbar_height, hotbarPath);
                        hotbar.saveSlice(0, 0, 1, hotbar_height, uiPath.resolve("hotbar_start_cap.png"));

                        int hotbar_slot_width = 20;
                        for (int i = 0; i < 9; ++i) {
                            hotbar.saveSlice(1 + (hotbar_slot_width * i), 0, hotbar_slot_width, hotbar_height,
                                    uiPath.resolve("hotbar_" + i + ".png"));
                        }

                        hotbar.saveSlice(hotbar_width - 1, 0, 1, hotbar_height, uiPath.resolve("hotbar_end_cap.png"));

                        // TODO: write json for hotbar_start/end_cap

                        hotbarPath.toFile().delete();
                    }

                    Path hotbarSelectionPath = hudPath.resolve("hotbar_selection.png");
                    if (hotbarSelectionPath.toFile().exists()) {
                        ImageConverter hotbarSelector = new ImageConverter(24, 23, hotbarSelectionPath);
                        hotbarSelector.newImage(24, 24);
                        hotbarSelector.subImageSized(0, 0, 24, 23);
                        hotbarSelector.store(uiPath.resolve("selected_hotbar_slot.png"));
                        hotbarSelectionPath.toFile().delete();
                    }

                    // . -> invite_hover?
                    // . -> invite_pressed?

                    // TODO: Buttons?
                }
            }

            {
                Path chestEntityPath = newTexturesPath.resolve("entity/chest".replace("/", File.separator));
                if (chestEntityPath.toFile().exists()) {
                    FileUtil.moveIfExists(chestEntityPath.resolve("normal_double.png"),
                            chestEntityPath.resolve("double_normal.png"));
                    Files.deleteIfExists(chestEntityPath.resolve("christmas.png"));
                    Files.deleteIfExists(chestEntityPath.resolve("christmas_left.png"));
                    Files.deleteIfExists(chestEntityPath.resolve("christmas_right.png"));
                }
            }
        }

        // etc
        Util.deleteDirectoryAndContents(assetsPath);
    }
}
