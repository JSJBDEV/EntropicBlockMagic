package ace.actually.ebm.items.books;

import ace.actually.ebm.EBM;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SealingMagicBook extends Item {
    public SealingMagicBook(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {


        if(world instanceof ServerWorld serverWorld)
        {
            if(EBM.shouldResolve(serverWorld,user.getBlockPos(),user,user.getMainHandStack(),"Low Ordered"))
            {
                if(user.getOffHandStack().isIn(ItemTags.SLABS))
                {
                    Item guess = guessFullBlockNameSlab(user.getOffHandStack().getItem());
                    if(guess!=null)
                    {
                        if(user.getOffHandStack().getCount()%2==0)
                        {
                            user.setStackInHand(Hand.OFF_HAND,new ItemStack(guess,user.getOffHandStack().getCount()/2));
                        }
                        else
                        {
                            user.giveItemStack(new ItemStack(user.getOffHandStack().getItem()));
                            user.setStackInHand(Hand.OFF_HAND,new ItemStack(guess,user.getOffHandStack().getCount()/2));

                        }
                    }
                }
                if(user.getOffHandStack().isIn(ItemTags.STAIRS))
                {
                    Item guess = guessFullBlockNameStair(user.getOffHandStack().getItem());
                    if(guess!=null)
                    {
                        float blocksBack = user.getOffHandStack().getCount()*1.5f;
                        user.dropItem(new ItemStack(guess,(int) blocksBack),true);
                        user.setStackInHand(Hand.OFF_HAND,ItemStack.EMPTY);

                    }
                }
            }
        }

        return super.use(world, user, hand);
    }

    private Item guessFullBlockNameSlab(Item item)
    {
        Identifier i = Registries.ITEM.getId(item);

        //e.g SMOOTH_RED_SANDSTONE_SLAB -> SMOOTH_RED_SANDSTONE
        Identifier guess = new Identifier(i.getNamespace(),i.getPath().replace("_slab",""));
        if(Registries.ITEM.containsId(guess))
        {
            return Registries.ITEM.get(guess);
        }

        //e.g OAK_SLAB -> OAK -> OAK_PLANKS
        guess = new Identifier(i.getNamespace(),i.getPath().replace("_slab","")+"_planks");
        if(Registries.ITEM.containsId(guess))
        {
            return Registries.ITEM.get(guess);
        }
        return null;
    }

    private Item guessFullBlockNameStair(Item item)
    {
        Identifier i = Registries.ITEM.getId(item);

        //e.g SMOOTH_RED_SANDSTONE_SLAB -> SMOOTH_RED_SANDSTONE
        Identifier guess = new Identifier(i.getNamespace(),i.getPath().replace("_stairs",""));
        if(Registries.ITEM.containsId(guess))
        {
            return Registries.ITEM.get(guess);
        }

        //e.g OAK_SLAB -> OAK -> OAK_PLANKS
        guess = new Identifier(i.getNamespace(),i.getPath().replace("_stairs","")+"_planks");
        if(Registries.ITEM.containsId(guess))
        {
            return Registries.ITEM.get(guess);
        }
        return null;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.translatable("text.ebm.sealing"));
        tooltip.add(Text.translatable("text.ebm.requires").append(" ").append(Text.translatable("text.ebm.low_ordered")).append(Text.translatable("text.ebm.entropy")));
        if(stack.hasNbt())
        {
            tooltip.add(Text.translatable("text.ebm.alt_requirement").append(EBM.asTranslatable(stack.getNbt().getString("altcast"))).append(" ").append(Text.translatable("text.ebm.entropy")));
        }
    }
}
