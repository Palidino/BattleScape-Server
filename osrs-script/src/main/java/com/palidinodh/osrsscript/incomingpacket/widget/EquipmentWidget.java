package com.palidinodh.osrsscript.incomingpacket.widget;

import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.dialogue.DialogueAction;
import com.palidinodh.osrscore.model.dialogue.OptionsDialogue;
import com.palidinodh.osrscore.model.player.Equipment;
import com.palidinodh.osrscore.model.player.Magic;
import com.palidinodh.osrscore.model.player.Player;
import lombok.var;

public class EquipmentWidget implements WidgetHandler {
  @Override
  public int[] getIds() {
    return new int[] { WidgetId.EQUIPMENT, WidgetId.EQUIPMENT_BONUSES,
        WidgetId.EQUIPMENT_BONUSES_INVENTORY };
  }

  @Override
  public void execute(Player player, int index, int widgetId, int childId, int slot, int itemId) {
    if (player.isLocked()) {
      return;
    }
    if (widgetId == WidgetId.EQUIPMENT || widgetId == WidgetId.EQUIPMENT_BONUSES) {
      var equipSlot = Equipment.Slot.getFromWidget(widgetId, childId);
      if (equipSlot != null) {
        if (index == 0) {
          player.getController().unequip(equipSlot.ordinal());
        } else {
          switch (player.getEquipment().getId(equipSlot)) {
            case ItemId.SERPENTINE_HELM:
            case ItemId.TANZANITE_HELM:
            case ItemId.MAGMA_HELM:
              player.getCharges().checkSerpentineHelm(equipSlot.ordinal() + 65536);
              break;
            case ItemId.AMULET_OF_GLORY:
            case ItemId.AMULET_OF_GLORY_T:
              player.openDialogue("amuletofglory", 0);
              break;
            case ItemId.AMULET_OF_GLORY_1:
            case ItemId.AMULET_OF_GLORY_2:
            case ItemId.AMULET_OF_GLORY_3:
            case ItemId.AMULET_OF_GLORY_4:
            case ItemId.AMULET_OF_GLORY_T1:
            case ItemId.AMULET_OF_GLORY_T2:
            case ItemId.AMULET_OF_GLORY_T3:
            case ItemId.AMULET_OF_GLORY_T4:
            case ItemId.AMULET_OF_ETERNAL_GLORY:
              if (!player.getController().canTeleport(30, true)) {
                break;
              }
              Tile gloryTeleport = null;
              if (index == 1) {
                gloryTeleport = new Tile(3087, 3491);
              } else if (index == 2) {
                gloryTeleport = new Tile(2915, 3152);
              } else if (index == 3) {
                gloryTeleport = new Tile(3085, 3249);
              } else if (index == 4) {
                gloryTeleport = new Tile(3293, 3177);
              }
              if (gloryTeleport == null) {
                break;
              }
              player.getMovement().animatedTeleport(gloryTeleport,
                  Magic.NORMAL_MAGIC_ANIMATION_START, Magic.NORMAL_MAGIC_ANIMATION_END,
                  Magic.NORMAL_MAGIC_GRAPHIC, null, 2);
              player.getController().stopWithTeleport();
              player.clearHits();
              break;
            case ItemId.CRAFTING_CAPE:
            case ItemId.CRAFTING_CAPE_T:
              if (index == 2) {
                if (!player.getController().canTeleport(20, true)) {
                  return;
                }
                Tile craftingGuildTeleport = new Tile(2935, 3283);
                player.getMovement().animatedTeleport(craftingGuildTeleport,
                    Magic.NORMAL_MAGIC_ANIMATION_START, Magic.NORMAL_MAGIC_ANIMATION_END,
                    Magic.NORMAL_MAGIC_GRAPHIC, null, 2);
                player.getController().stopWithTeleport();
                player.clearHits();
              }
              break;
            case ItemId.ARDOUGNE_CLOAK_1:
              if (!player.getController().canTeleport(30, true)) {
                break;
              }
              Tile ardougneCloak1Teleport = null;
              if (index == 1) {
                ardougneCloak1Teleport = new Tile(2606, 3223);
              }
              if (ardougneCloak1Teleport == null) {
                break;
              }
              player.getMovement().animatedTeleport(ardougneCloak1Teleport,
                  Magic.NORMAL_MAGIC_ANIMATION_START, Magic.NORMAL_MAGIC_ANIMATION_END,
                  Magic.NORMAL_MAGIC_GRAPHIC, null, 2);
              player.getController().stopWithTeleport();
              player.clearHits();
              break;
            case ItemId.ARDOUGNE_CLOAK_2:
            case ItemId.ARDOUGNE_CLOAK_3:
            case ItemId.ARDOUGNE_CLOAK_4:
              if (!player.getController().canTeleport(30, true)) {
                break;
              }
              Tile ardougneCloakTeleport = null;
              if (index == 1) {
                ardougneCloakTeleport = new Tile(2606, 3223);
              } else if (index == 2) {
                ardougneCloakTeleport = new Tile(2673, 3374);
              }
              if (ardougneCloakTeleport == null) {
                break;
              }
              player.getMovement().animatedTeleport(ardougneCloakTeleport,
                  Magic.NORMAL_MAGIC_ANIMATION_START, Magic.NORMAL_MAGIC_ANIMATION_END,
                  Magic.NORMAL_MAGIC_GRAPHIC, null, 2);
              player.getController().stopWithTeleport();
              player.clearHits();
              break;
            case ItemId.MAX_CAPE:
              if (index == 1) {
                Tile warriorsGuildTile = new Tile(2845, 3544);
                if (!player.getController().canTeleport(20, true)) {
                  return;
                }
                player.getMovement().animatedTeleport(warriorsGuildTile,
                    Magic.NORMAL_MAGIC_ANIMATION_START, Magic.NORMAL_MAGIC_ANIMATION_END,
                    Magic.NORMAL_MAGIC_GRAPHIC, null, 2);
                player.getController().stopWithTeleport();
                player.clearHits();
              } else if (index == 2) {
                player.openDialogue(new MaxCapeDialogue.FishDialogue(player));
              } else if (index == 3) {
                Tile craftingGuildTile = new Tile(2936, 3282);
                if (!player.getController().canTeleport(20, true)) {
                  return;
                }
                player.getMovement().animatedTeleport(craftingGuildTile,
                    Magic.NORMAL_MAGIC_ANIMATION_START, Magic.NORMAL_MAGIC_ANIMATION_END,
                    Magic.NORMAL_MAGIC_GRAPHIC, null, 2);
                player.getController().stopWithTeleport();
                player.clearHits();
              } else if (index == 4) {
                Tile homeTile = player.getWidgetManager().getHomeTile();
                if (!player.getController().canTeleport(20, true)) {
                  return;
                }
                if (homeTile == null) {
                  return;
                }
                player.getMovement().animatedTeleport(homeTile, Magic.NORMAL_MAGIC_ANIMATION_START,
                    Magic.NORMAL_MAGIC_ANIMATION_END, Magic.NORMAL_MAGIC_GRAPHIC, null, 2);
                player.getController().stopWithTeleport();
                player.clearHits();
              } else if (index == 5) {
                player.openDialogue(new MaxCapeDialogue.PortalsDialogue(player));
              } else if (index == 6) {
                player.openDialogue(new MaxCapeDialogue.OtherDialogue(player));
              } else if (index == 7) {
                player.openDialogue("spellbooks", 1);
              } else if (index == 8) {
                player.openDialogue(new MaxCapeDialogue.FeaturesDialogue(player));
              }
              break;
            case ItemId.BINDING_NECKLACE:
              player.getGameEncoder().sendMessage("Your Binding Necklace has "
                  + player.getSkills().getBindingNecklace() + " charges remaining.");
              break;
            case ItemId.RUBBER_CHICKEN:
              player.setAnimation(1835);
              break;
            case ItemId.TOXIC_STAFF_OF_THE_DEAD:
              player.getCharges().checkToxicStaff(equipSlot.ordinal() + 65536);
              break;
            case ItemId.TOXIC_BLOWPIPE:
              player.getCharges().checkToxicBlowpipe(equipSlot.ordinal() + 65536);
              break;
            case ItemId.BRACELET_OF_ETHEREUM:
            case ItemId.BRACELET_OF_ETHEREUM_UNCHARGED:
              if (index == 1) {
                player.getCharges().checkCharges(equipSlot.ordinal() + 65536);
              } else if (index == 2) {
                player.getCharges()
                    .setEthereumAutoAbsorb(!player.getCharges().getEthereumAutoAbsorb());
                player.getGameEncoder().sendMessage(
                    "Ether automatic absorption: " + player.getCharges().getEthereumAutoAbsorb());
              }
              break;
            case ItemId.RING_OF_RECOIL:
              player.getGameEncoder()
                  .sendMessage("You can inflict " + player.getCharges().getRingOfRecoil()
                      + " more points of damage before your ring will shatter.");
              break;
            case ItemId.RING_OF_WEALTH_I:
              player.openDialogue("ringwealth", 0);
              break;
            case ItemId.MAGIC_CAPE:
            case ItemId.MAGIC_CAPE_T:
              player.openDialogue("spellbooks", 1);
              break;
            default:
              player.getCharges().checkCharges(equipSlot.ordinal() + 65536);
              break;
          }
        }
      } else if (childId == 17) {
        player.getEquipment().openStats();
      } else if (childId == 21) {
        player.getCombat().openItemsKeptOnDeath();
      } else if (childId == 23) {
        player.getFamiliar().callPet(false);
      }
    } else if (widgetId == WidgetId.EQUIPMENT_BONUSES_INVENTORY) {
      switch (childId) {
        case 0:
          if (index == 0) {
            player.getEquipment().equip(itemId, slot);
          }
          break;
      }
    }
  }

  public static class MaxCapeDialogue {
    public static class FishDialogue extends OptionsDialogue {
      public FishDialogue(Player player) {
        DialogueAction action = (childId, slot) -> {
          Tile maxCapeTele = null;
          if (slot == 0) {
            maxCapeTele = new Tile(3093, 3495);
          } else if (slot == 1) {
            maxCapeTele = new Tile(1233, 3565);
          } else if (slot == 2) {
            maxCapeTele = new Tile(1666, 10050);
          }
          if (!player.getController().canTeleport(true)) {
            return;
          }
          if (maxCapeTele == null) {
            return;
          }
          player.getMovement().animatedTeleport(maxCapeTele, Magic.NORMAL_MAGIC_ANIMATION_START,
              Magic.NORMAL_MAGIC_ANIMATION_END, Magic.NORMAL_MAGIC_GRAPHIC, null, 2);
          player.getController().stopWithTeleport();
          player.clearHits();
        };
        addOption("Edgeville", action);
        addOption("Chambers of Xeric", action);
        addOption("Catacombs of Kourend", action);
      }
    }

    public static class OtherDialogue extends OptionsDialogue {
      public OtherDialogue(Player player) {
        DialogueAction action = (childId, slot) -> {
          Tile maxCapeTele = null;
          if (slot == 0) {
            maxCapeTele = new Tile(3093, 3495);
          } else if (slot == 1) {
            maxCapeTele = new Tile(1233, 3565);
          } else if (slot == 2) {
            maxCapeTele = new Tile(1666, 10050);
          }
          if (!player.getController().canTeleport(true)) {
            return;
          }
          if (maxCapeTele == null) {
            return;
          }
          player.getMovement().animatedTeleport(maxCapeTele, Magic.NORMAL_MAGIC_ANIMATION_START,
              Magic.NORMAL_MAGIC_ANIMATION_END, Magic.NORMAL_MAGIC_GRAPHIC, null, 2);
          player.getController().stopWithTeleport();
          player.clearHits();
        };
        addOption("Edgeville", action);
        addOption("Chambers of Xeric", action);
        addOption("Catacombs of Kourend", action);
      }
    }

    public static class PortalsDialogue extends OptionsDialogue {
      public PortalsDialogue(Player player) {
        DialogueAction action = (childId, slot) -> {
          Tile maxCapeTele = null;
          if (slot == 0) {
            maxCapeTele = new Tile(3093, 3495);
          } else if (slot == 1) {
            maxCapeTele = new Tile(1233, 3565);
          } else if (slot == 2) {
            maxCapeTele = new Tile(1666, 10050);
          }
          if (!player.getController().canTeleport(true)) {
            return;
          }
          if (maxCapeTele == null) {
            return;
          }
          player.getMovement().animatedTeleport(maxCapeTele, Magic.NORMAL_MAGIC_ANIMATION_START,
              Magic.NORMAL_MAGIC_ANIMATION_END, Magic.NORMAL_MAGIC_GRAPHIC, null, 2);
          player.getController().stopWithTeleport();
          player.clearHits();
        };
        addOption("Edgeville", action);
        addOption("Chambers of Xeric", action);
        addOption("Catacombs of Kourend", action);
      }
    }

    public static class TeleportsDialogue extends OptionsDialogue {
      public TeleportsDialogue(Player player) {
        DialogueAction action = (childId, slot) -> {
          Tile maxCapeTele = null;
          if (slot == 0) {
            maxCapeTele = new Tile(3093, 3495);
          } else if (slot == 1) {
            maxCapeTele = new Tile(1233, 3565);
          } else if (slot == 2) {
            maxCapeTele = new Tile(1666, 10050);
          }
          if (!player.getController().canTeleport(true)) {
            return;
          }
          if (maxCapeTele == null) {
            return;
          }
          player.getMovement().animatedTeleport(maxCapeTele, Magic.NORMAL_MAGIC_ANIMATION_START,
              Magic.NORMAL_MAGIC_ANIMATION_END, Magic.NORMAL_MAGIC_GRAPHIC, null, 2);
          player.getController().stopWithTeleport();
          player.clearHits();
        };
        addOption("Edgeville", action);
        addOption("Chambers of Xeric", action);
        addOption("Catacombs of Kourend", action);
      }
    }

    public static class FeaturesDialogue extends OptionsDialogue {
      public FeaturesDialogue(Player player) {
        DialogueAction action = (childId, slot) -> {
          Tile maxCapeTele = null;
          if (slot == 0) {
            maxCapeTele = new Tile(3093, 3495);
          } else if (slot == 1) {
            maxCapeTele = new Tile(1233, 3565);
          } else if (slot == 2) {
            maxCapeTele = new Tile(1666, 10050);
          }
          if (!player.getController().canTeleport(true)) {
            return;
          }
          if (maxCapeTele == null) {
            return;
          }
          player.getMovement().animatedTeleport(maxCapeTele, Magic.NORMAL_MAGIC_ANIMATION_START,
              Magic.NORMAL_MAGIC_ANIMATION_END, Magic.NORMAL_MAGIC_GRAPHIC, null, 2);
          player.getController().stopWithTeleport();
          player.clearHits();
        };
        addOption("Edgeville", action);
        addOption("Chambers of Xeric", action);
        addOption("Catacombs of Kourend", action);
      }
    }
  }
}
