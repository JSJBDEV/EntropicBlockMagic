package ace.actually.ebm;

import net.minecraft.block.ChestBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StowingMagicBook extends Item {
    public StowingMagicBook(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        if(world instanceof ServerWorld serverWorld)
        {
            if(EBM.shouldResolve(serverWorld,user.getBlockPos(),user,user.getMainHandStack(),"Medium Chaotic"))
            {
                user.getInventory().main.forEach(a->
                {
                    Identifier v = Registries.ITEM.getId(a.getItem());
                    if(v.getPath().contains("raw") || v.getPath().contains("ore") || v.getPath().contains("cobble"))
                    {
                        user.dropItem(a.copy(),true);
                        a.setCount(0);
                    }
                });
            }

        }

        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.literal("Throws any raw, ore, or cobble items onto the ground in front of you"));
        tooltip.add(Text.literal("requires Medium Chaotic Entropy"));
        if(stack.hasNbt())
        {
            tooltip.add(Text.literal("Alt Requirement: "+stack.getNbt().getString("altcast")));
        }
    }
}
