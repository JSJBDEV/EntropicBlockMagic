package ace.actually.ebm.items;

import ace.actually.ebm.EBM;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BiomeCharmItem extends Item {
    public BiomeCharmItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(user.getStackInHand(hand).getItem() instanceof BiomeCharmItem && world instanceof ServerWorld serverWorld)
        {
            if(user.isSneaking())
            {
                if(!user.getStackInHand(hand).hasNbt())
                {
                    NbtCompound compound = new NbtCompound();
                    compound.putString("entropy", EBM.entropyString(EBM.getEntropyAt(serverWorld,user.getBlockPos())));

                    user.getStackInHand(hand).setNbt(compound);
                }
            }
            else
            {
                user.sendMessage(EBM.asTranslatable(
                        EBM.entropyString(EBM.getEntropyAt(serverWorld, user.getBlockPos())))
                        .append(" ")
                        .append(Text.translatable("text.ebm.entropy")
                        )
                );
            }

        }

        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if(stack.hasNbt())
        {

            tooltip.add(EBM.asTranslatable(stack.getNbt().getString("entropy")).append(" ").append(Text.translatable("text.ebm.entropy")));
        }
    }
}
