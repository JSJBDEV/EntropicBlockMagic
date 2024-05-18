package ace.actually.ebm.items.books;

import ace.actually.ebm.EBM;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ExchangeMagicBook extends Item {
    public ExchangeMagicBook(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if(context.getWorld() instanceof ServerWorld world)
        {
            if(EBM.shouldResolve(world,context.getBlockPos(),context.getPlayer(),context.getStack(),"Balanced"))
            {
                changeBlockAtPos(context.getWorld(),context.getBlockPos(),context.getPlayer(),context.getWorld().getBlockState(context.getBlockPos()).getBlock());
                return ActionResult.CONSUME;
            }
        }

        return super.useOnBlock(context);
    }

    public void changeBlockAtPos(World world, BlockPos pos, PlayerEntity player, Block change)
    {
        if(world.getBlockState(pos).isOf(change))
        {
            if(player.getOffHandStack().getItem() instanceof BlockItem blockItem)
            {
                if(!player.getOffHandStack().isEmpty())
                {

                    world.breakBlock(pos,true,player);
                    world.setBlockState(pos,blockItem.getBlock().getDefaultState());
                    player.getOffHandStack().decrement(1);


                    changeBlockAtPos(world, pos.east(), player, change);
                    changeBlockAtPos(world, pos.west(), player, change);
                    changeBlockAtPos(world, pos.north(), player, change);
                    changeBlockAtPos(world, pos.south(), player, change);
                    changeBlockAtPos(world, pos.up(), player, change);
                    changeBlockAtPos(world, pos.down(), player, change);
                }

            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.translatable("text.ebm.exchange"));
        tooltip.add(Text.translatable("text.ebm.requires").append(" ").append(Text.translatable("text.ebm.balanced")).append(" ").append(Text.translatable("text.ebm.entropy")));
        if(stack.hasNbt())
        {
            tooltip.add(Text.translatable("text.ebm.alt_requirement").append(EBM.asTranslatable(stack.getNbt().getString("altcast"))).append(" ").append(Text.translatable("text.ebm.entropy")));
        }
    }
}
