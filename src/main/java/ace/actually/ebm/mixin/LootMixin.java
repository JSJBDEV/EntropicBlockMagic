package ace.actually.ebm.mixin;

import ace.actually.ebm.EBM;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.NameGenerator;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.RandomUtils;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;


@Mixin(LootableContainerBlockEntity.class)
public abstract class LootMixin extends LockableContainerBlockEntity {


    @Shadow @Nullable protected Identifier lootTableId;

    @Shadow public abstract ItemStack getStack(int slot);

    @Shadow public abstract void setStack(int slot, ItemStack stack);

    @Shadow protected abstract DefaultedList<ItemStack> getInvStackList();

    protected LootMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }


    @Inject(method = "checkLootInteraction", at = @At("HEAD"))
    private void loot(PlayerEntity player, CallbackInfo ci)
    {
        if (this.lootTableId != null && this.world.getServer() != null)
        {
            if(player.getRandom().nextInt(5)==0)
            {
                ItemStack stack = switch (world.getRandom().nextInt(5))
                {
                    case 1 -> new ItemStack(EBM.SEALING_MAGIC_BOOK);
                    case 2 -> new ItemStack(EBM.RAISING_MAGIC_BOOK);
                    case 3 -> new ItemStack(EBM.DECONSTRUCTING_MAGIC_BOOK);
                    case 4 -> new ItemStack(EBM.STOWING_MAGIC_BOOK);
                    case 5 -> new ItemStack(EBM.SEARCHING_MAGIC_BOOK);
                    default -> new ItemStack(EBM.EXCHANGE_MAGIC_BOOK);
                };

                if(player.getRandom().nextInt(5)==0)
                {
                    stack.setCustomName(Text.empty().append(Text.of(EBM.MAGE_NAMES[world.getRandom().nextInt(EBM.MAGE_NAMES.length)] + "'s ")).append(stack.getName()));
                }
                else
                {
                    String v = NameGenerator.name(UUID.randomUUID());
                    stack.setCustomName(Text.empty().append(v).append("'s ").append(stack.getName()));
                }
                NbtCompound compound = stack.getNbt();
                compound.putString("altcast",EBM.entropyString(player.getRandom().nextBetween(-350,350)));
                stack.setNbt(compound);
                stack.addEnchantment(Enchantments.LOOTING,1);
                this.getInvStackList().set(RandomUtils.nextInt(0,27),stack);
            }



        }

    }
}
