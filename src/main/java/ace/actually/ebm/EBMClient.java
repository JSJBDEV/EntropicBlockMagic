package ace.actually.ebm;

import ace.actually.ebm.screens.SearchScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EBMClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(EBM.COLLECT_PACKET,((client, handler, buf, responseSender) ->
        {
            List<ItemStack> stacks = new ArrayList<>();
            int size = buf.readInt();
            for (int i = 0; i < size; i++) {
                stacks.add(buf.readItemStack());
            }
            client.execute(()-> client.setScreen(new SearchScreen(stacks)));
        }));
    }
}
