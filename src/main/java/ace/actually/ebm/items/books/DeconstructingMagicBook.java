package ace.actually.ebm.items.books;

import ace.actually.ebm.EBM;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
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

public class DeconstructingMagicBook extends Item {
    public DeconstructingMagicBook(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        if(world instanceof ServerWorld serverWorld)
        {
            if(EBM.shouldResolve(serverWorld,user.getBlockPos(),user,user.getMainHandStack(),"Low Chaotic"))
            {
                if(!user.getOffHandStack().isEmpty())
                {
                    Identifier i = Registries.ITEM.getId(user.getOffHandStack().getItem());
                    Identifier guess = new Identifier(i.getNamespace(),i.getPath().replace("polished_",""));
                    if(!guess.equals(i) && Registries.ITEM.containsId(guess))
                    {
                        user.setStackInHand(Hand.OFF_HAND,new ItemStack(Registries.ITEM.get(guess),user.getOffHandStack().getCount()));
                        return super.use(world, user, hand);
                    }
                    guess = new Identifier(i.getNamespace(),i.getPath().replace("_bricks",""));
                    if(!guess.equals(i) && Registries.ITEM.containsId(guess))
                    {
                        user.setStackInHand(Hand.OFF_HAND,new ItemStack(Registries.ITEM.get(guess),user.getOffHandStack().getCount()));
                        return super.use(world, user, hand);
                    }
                    guess = new Identifier(i.getNamespace(),i.getPath().replace("_wall",""));
                    if(!guess.equals(i) && Registries.ITEM.containsId(guess))
                    {
                        user.setStackInHand(Hand.OFF_HAND,new ItemStack(Registries.ITEM.get(guess),user.getOffHandStack().getCount()));
                        return super.use(world, user, hand);
                    }
                    guess = new Identifier(i.getNamespace(),i.getPath().replace("_tiles",""));
                    if(!guess.equals(i) && Registries.ITEM.containsId(guess))
                    {
                        user.setStackInHand(Hand.OFF_HAND,new ItemStack(Registries.ITEM.get(guess),user.getOffHandStack().getCount()));
                        return super.use(world, user, hand);
                    }
                }
            }
        }




        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.translatable("text.ebm.deconstructing1"));
        tooltip.add(Text.translatable("text.ebm.deconstructing2"));
        tooltip.add(Text.translatable("text.ebm.requires").append(" ").append(Text.translatable("text.ebm.low_chaotic")).append(" ").append(Text.translatable("text.ebm.entropy")));
        if(stack.hasNbt())
        {
            tooltip.add(Text.translatable("text.ebm.alt_requirement").append(EBM.asTranslatable(stack.getNbt().getString("altcast"))).append(" ").append(Text.translatable("text.ebm.entropy")));
        }
    }
}
