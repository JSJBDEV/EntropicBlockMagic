package ace.actually.ebm.items.books;

import ace.actually.ebm.EBM;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SearchingMagicBook extends Item {
    public SearchingMagicBook(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        if(world instanceof ServerWorld serverWorld)
        {
            if(EBM.shouldResolve(serverWorld,user.getBlockPos(),user,user.getMainHandStack(),"High Ordered"))
            {
                PacketByteBuf buf = PacketByteBufs.create();
                List<ItemStack> stacks = EBM.collectAllChestStacks(serverWorld,user);
                buf.writeInt(stacks.size());
                stacks.forEach(buf::writeItemStack);
                ServerPlayNetworking.send((ServerPlayerEntity) user,EBM.COLLECT_PACKET,buf);
            }
        }


        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.translatable("text.ebm.searching"));
        tooltip.add(Text.translatable("text.ebm.requires").append(" ").append(Text.translatable("text.ebm.high_ordered")).append(" ").append(Text.translatable("text.ebm.entropy")));
        if(stack.hasNbt())
        {
            tooltip.add(Text.translatable("text.ebm.alt_requirement").append(EBM.asTranslatable(stack.getNbt().getString("altcast"))).append(" ").append(Text.translatable("text.ebm.entropy")));
        }
    }
}
