package ace.actually.ebm;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.biome.Biome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EBM implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("ebm");

	public static final String[] MAGE_NAMES = new String[]{"AgentCross","Acro","Inky","SmilingSloth"};

	public static final ItemGroup TAB = FabricItemGroup.builder()
			.icon(() -> new ItemStack(EBM.DECONSTRUCTING_MAGIC_BOOK))
			.displayName(Text.of("Entropic Block Magic"))
			.build();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		Registry.register(Registries.ITEM_GROUP,new Identifier("ebm","tab"),TAB);
		registerItems();
		LOGGER.info("Hello Fabric world!");

	}


	public static final ExchangeMagicBook EXCHANGE_MAGIC_BOOK = new ExchangeMagicBook(new Item.Settings());
	public static final SealingMagicBook SEALING_MAGIC_BOOK  = new SealingMagicBook(new Item.Settings());
	public static final RaisingMagicBook RAISING_MAGIC_BOOK = new RaisingMagicBook(new Item.Settings());
	public static final DeconstructingMagicBook DECONSTRUCTING_MAGIC_BOOK = new DeconstructingMagicBook(new Item.Settings());
	public static final BiomeCharmItem BIOME_CHARM_ITEM = new BiomeCharmItem(new Item.Settings());
	private void registerItems()
	{
		int v = Registries.ITEM.size();
		Registry.register(Registries.ITEM,new Identifier("ebm","exchange_magic_book"),EXCHANGE_MAGIC_BOOK);
		Registry.register(Registries.ITEM,new Identifier("ebm","sealing_magic_book"),SEALING_MAGIC_BOOK);
		Registry.register(Registries.ITEM,new Identifier("ebm","raising_magic_book"),RAISING_MAGIC_BOOK);
		Registry.register(Registries.ITEM,new Identifier("ebm","deconstructing_magic_book"),DECONSTRUCTING_MAGIC_BOOK);
		Registry.register(Registries.ITEM,new Identifier("ebm","biome_charm"),BIOME_CHARM_ITEM);

		for (int i = v; i < Registries.ITEM.size(); i++) {
			int finalI = i;
			ItemGroupEvents.modifyEntriesEvent(Registries.ITEM_GROUP.getKey(TAB).get()).register(a->
			{
				a.add(Registries.ITEM.get(finalI).getDefaultStack());
			});
		}
	}

	public static int getEntropyAt(ServerWorld world, BlockPos pos)
	{

		Biome biome = world.getBiome(pos).value();
		long seed = world.getSeed();
		seed = seed * biome.getFogColor() / biome.getFoliageColor() * biome.getWaterFogColor() / biome.getWaterColor() * biome.getSkyColor() / (pos.getY()/30);
		Random random = Random.create(seed);


		return random.nextBetween(-350,350);
	}

	public static String entropyString(int entropy)
	{

		if(entropy>=-150 && entropy<-50) return "("+entropy+") "+"Low Chaotic";
		if(entropy>=-250 && entropy<-150) return "("+entropy+") "+"Medium Chaotic";
		if(entropy>=-350 && entropy<-250) return "("+entropy+") "+"High Chaotic";
		if(entropy>=50 && entropy<150) return "("+entropy+") "+"Low Ordered";
		if(entropy>=150 && entropy<250) return "("+entropy+") "+"Medium Ordered";
		if(entropy>=250 && entropy<350) return "("+entropy+") "+"High Ordered";
		return "("+entropy+") "+"Balanced";

	}

	public static boolean hasCorrectEntropyCharm(PlayerEntity player, String entropyString)
	{
        for (ItemStack a : player.getInventory().main) {
            if (a.getItem()==EBM.BIOME_CHARM_ITEM)
			{
				if(a.hasNbt())
				{
					if(a.getNbt().getString("entropy").contains(entropyString))
					{
						return true;
					}
				}
			}
        }
		return false;
    }


	/**
	 *
	 * @param world for the biome
	 * @param pos for the biome and y
	 * @param player check inventory for charms
	 * @param stackForAltCost the book this is being cast from
	 * @param normal what the spell usually costs anyway
	 * @return Should the book do its spell?
	 */
	public static boolean shouldResolve(ServerWorld world, BlockPos pos, PlayerEntity player, ItemStack stackForAltCost,String normal)
	{
		boolean resolves = EBM.entropyString(EBM.getEntropyAt(world,pos)).contains(normal)
				|| EBM.hasCorrectEntropyCharm(player,normal);

		if(!resolves && stackForAltCost.hasNbt())
		{
			String altcost = stackForAltCost.getNbt().getString("altcost").split("\\(")[0];
			resolves = EBM.entropyString(EBM.getEntropyAt(world,pos)).contains(altcost)
					|| EBM.hasCorrectEntropyCharm(player,altcost);
		}
		return resolves;
	}

}