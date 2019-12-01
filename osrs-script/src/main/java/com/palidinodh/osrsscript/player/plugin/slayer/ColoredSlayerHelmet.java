package com.palidinodh.osrsscript.player.plugin.slayer;

import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.model.player.slayer.SlayerUnlock;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
enum ColoredSlayerHelmet {
    RED(ItemId.SLAYER_HELMET, ItemId.ABYSSAL_HEAD, SlayerUnlock.UNHOLY_HELMET,
            ItemId.RED_SLAYER_HELMET),
    RED_I(ItemId.SLAYER_HELMET_I, ItemId.ABYSSAL_HEAD, SlayerUnlock.UNHOLY_HELMET,
            ItemId.RED_SLAYER_HELMET_I),
    GREEN(ItemId.SLAYER_HELMET, ItemId.KQ_HEAD, SlayerUnlock.KALPHITE_KHAT,
            ItemId.GREEN_SLAYER_HELMET),
    GREEN_I(ItemId.SLAYER_HELMET_I, ItemId.KQ_HEAD, SlayerUnlock.KALPHITE_KHAT,
            ItemId.GREEN_SLAYER_HELMET_I),
    BLACK(ItemId.SLAYER_HELMET, ItemId.KBD_HEADS, SlayerUnlock.KING_BLACK_BONNET,
            ItemId.BLACK_SLAYER_HELMET),
    BLACK_I(ItemId.SLAYER_HELMET_I, ItemId.KBD_HEADS, SlayerUnlock.KING_BLACK_BONNET,
            ItemId.BLACK_SLAYER_HELMET_I),
    PURPLE(ItemId.SLAYER_HELMET, ItemId.DARK_CLAW, SlayerUnlock.DARK_MANTLE,
            ItemId.PURPLE_SLAYER_HELMET),
    PURPLE_I(ItemId.SLAYER_HELMET_I, ItemId.DARK_CLAW, SlayerUnlock.DARK_MANTLE,
            ItemId.PURPLE_SLAYER_HELMET_I),
    TURQUOISE(ItemId.SLAYER_HELMET, ItemId.VORKATHS_HEAD_21907, SlayerUnlock.UNDEAD_HEAD,
            ItemId.TURQUOISE_SLAYER_HELMET),
    TURQUOISE_I(ItemId.SLAYER_HELMET_I, ItemId.VORKATHS_HEAD_21907, SlayerUnlock.UNDEAD_HEAD,
            ItemId.TURQUOISE_SLAYER_HELMET_I),
    HYDRA(ItemId.SLAYER_HELMET, ItemId.ALCHEMICAL_HYDRA_HEADS, SlayerUnlock.USE_MORE_HEAD,
            ItemId.HYDRA_SLAYER_HELMET),
    HYDRA_I(ItemId.SLAYER_HELMET_I, ItemId.ALCHEMICAL_HYDRA_HEADS, SlayerUnlock.USE_MORE_HEAD,
            ItemId.HYDRA_SLAYER_HELMET_I);

    private int fromHelmetId;
    private int fromAttachmentId;
    private SlayerUnlock unlock;
    private int toHelmetId;
}
