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
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RaisingMagicBook extends Item {
    public RaisingMagicBook(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        if(context.getWorld() instanceof ServerWorld world)
        {
            if(EBM.shouldResolve(world,context.getBlockPos(),context.getPlayer(),context.getStack(),"Medium Ordered"))
            {
                buildInDirectionAndSearch(context.getWorld(), context.getBlockPos(), context.getPlayer(), context.getSide(), context.getWorld().getBlockState(context.getBlockPos()).getBlock());
                return ActionResult.CONSUME;
            }
        }
        return super.useOnBlock(context);
    }

    private void buildInDirectionAndSearch(World world, BlockPos pos, PlayerEntity player, Direction side, Block move)
    {
        if(world.getBlockState(pos.add(side.getVector())).isAir())
        {
            if(world.getBlockState(pos).isOf(move))
            {
                if(player.getOffHandStack().getItem() instanceof BlockItem blockItem)
                {
                    if(!player.getOffHandStack().isEmpty() && blockItem.getBlock()==world.getBlockState(pos).getBlock())
                    {
                        world.setBlockState(pos.add(side.getVector()),blockItem.getBlock().getDefaultState());
                        player.getOffHandStack().decrement(1);

                        for(Direction direction: Direction.values())
                        {
                            if(direction!=side)
                            {
                                buildInDirectionAndSearch(world,pos.add(direction.getVector()),player,side,move);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.translatable("text.ebm.raising"));
        tooltip.add(Text.translatable("text.ebm.requires").append(" ").append(Text.translatable("text.ebm.medium_ordered")).append(Text.translatable("text.ebm.entropy")));
        if(stack.hasNbt())
        {
            tooltip.add(Text.translatable("text.ebm.alt_requirement").append(EBM.asTranslatable(stack.getNbt().getString("altcast"))).append(" ").append(Text.translatable("text.ebm.entropy")));
        }
    }
}
