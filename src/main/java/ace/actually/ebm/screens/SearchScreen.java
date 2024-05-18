package ace.actually.ebm.screens;


import ace.actually.ebm.EBM;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class SearchScreen extends Screen {
    List<ItemStack> results;
    TextFieldWidget search;
    String lastSearch;
    int selected = 0;

    List<ItemStack> stacks;

    public SearchScreen(List<ItemStack> stacks) {
        super(Text.of("Wish Screen"));
        results=new ArrayList<>();
        this.stacks=stacks;

    }

    @Override
    public void init() {
        int offsetX = (width/2)-100;
        super.init();
        
        search=this.addDrawableChild(new TextFieldWidget(textRenderer,offsetX,10,100,10,Text.of("search")));
        int up=0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int finalUp = up;
                this.addDrawableChild(ButtonWidget.builder(Text.of(""),press-> selected=finalUp).dimensions(offsetX+20*i,20+20*j,20,20).build());
                up++;
            }
        }
        results.addAll(stacks);

        this.addDrawableChild(ButtonWidget.builder(Text.of("Get"),press-> pressButton()).dimensions(offsetX+220,80,40,20).build());

    }



    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        int offsetX = (width/2)-98;
        int up = 0;
        if(!search.getText().equals(lastSearch))
        {
            trySearch();
            lastSearch=search.getText();
        }
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if(results.size()>up)
                {
                    context.drawItem(results.get(up),offsetX+20*i,22+20*j);

                    context.drawItemInSlot(textRenderer,results.get(up),offsetX+20*i,22+20*j);


                    if(mouseX>(offsetX+20*i) && mouseX<((offsetX+20)+20*i) && mouseY>(22+20*j) && mouseY<(42+20*j))
                    {
                        context.drawItemTooltip(textRenderer,results.get(up),offsetX+20*i,22+20*j);
                    }

                    up++;
                }
            }
        }
    }

    private void trySearch()
    {
        results=new ArrayList<>();
        stacks.forEach(item->
        {
            if(item.getTranslationKey().toLowerCase().contains(search.getText().toLowerCase())
                    || item.getName().getString().toLowerCase().contains(search.getText().toLowerCase()))
            {
                results.add(item);
            }
        });
    }

    private void pressButton()
    {

        if(selected<results.size())
        {
            ItemStack item = results.get(selected);
            PacketByteBuf data = PacketByteBufs.create();
            data.writeItemStack(item);
            ClientPlayNetworking.send(EBM.SEARCH_PACKET,data);
            MinecraftClient.getInstance().setScreen(null);
        }


    }




}
