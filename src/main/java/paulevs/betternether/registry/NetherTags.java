package paulevs.betternether.registry;

import net.fabricmc.fabric.mixin.object.builder.AbstractBlockAccessor;
import net.fabricmc.fabric.mixin.object.builder.AbstractBlockSettingsAccessor;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import paulevs.betternether.blocks.BlockTerrain;
import ru.bclib.api.BonemealAPI;
import ru.bclib.api.TagAPI;
import ru.bclib.blocks.BaseVineBlock;
import ru.bclib.blocks.SimpleLeavesBlock;
import ru.bclib.mixin.common.ComposterBlockAccessor;

public class NetherTags {
	public static final Tag<Block> SOUL_GROUND_BLOCK = TagAPI.makeCommonBlockTag( "soul_ground");
	public static final Tag<Block> NETHERRACK = TagAPI.makeCommonBlockTag("netherrack");
	public static final Tag<Block> MYCELIUM = TagAPI.makeCommonBlockTag("nether_mycelium");
	public static final Tag.Named<Block> NYLIUM = BlockTags.NYLIUM;

	public static final Tag<Item> SOUL_GROUND_ITEM = TagAPI.makeCommonItemTag("soul_ground");

	private static void allowCompost(float chance, Item item){
		if (item!=null && item != Items.AIR) {
			ComposterBlockAccessor.callAdd(chance, item);
		}
	}
	
	public static void register() {
		NetherBlocks.getModBlocks().forEach(block -> {
			BlockBehaviour.Properties properties = ((AbstractBlockAccessor) block).getSettings();
			Material material = ((AbstractBlockSettingsAccessor) properties).getMaterial();
			Item item = block.asItem();

			if (material.equals(Material.STONE) || material.equals(Material.METAL)) {
				TagAPI.addTag(TagAPI.MINEABLE_PICKAXE, block);
			}
			else if (material.equals(Material.WOOD) || material.equals(Material.NETHER_WOOD)) {
				TagAPI.addTag(TagAPI.MINEABLE_AXE, block);
			}
			else if (material.equals(Material.LEAVES) || material.equals(Material.PLANT) || material.equals(Material.WATER_PLANT)) {
				TagAPI.addTag(TagAPI.MINEABLE_HOE, block);
				TagAPI.addTag(BlockTags.LEAVES, block);
				//TODO: for BCLib 0.5.3
				//ComposterAPI.allowCompost(0.3f, item);
				allowCompost(0.3F, item);
			}
			else if (material.equals(Material.SAND)) {
				TagAPI.addTag(TagAPI.MINEABLE_SHOVEL, block);
			}

			if (block instanceof BlockTerrain) {
				TagAPI.addNetherGround(block);
				TagAPI.addTag(NYLIUM, block);
				BonemealAPI.addSpreadableBlock(block, Blocks.NETHERRACK);
			}
			
			else if (block instanceof LeavesBlock || block instanceof SimpleLeavesBlock) {
				TagAPI.addTag(BlockTags.LEAVES, block);
				//TODO: for BCLib 0.5.3
				//ComposterAPI.allowCompost(0.3f, item);
				allowCompost(0.3F, item);
			}
			else if (block instanceof BaseVineBlock) {
				TagAPI.addTag(BlockTags.CLIMBABLE, block);
			}
			/*else if (block instanceof BlockCincinnasitePedestal) {
				TagHelper.addTag(PEDESTALS, block);
			}*/

			Material mat = block.defaultBlockState().getMaterial();
			if (mat.equals(Material.PLANT) || mat.equals(Material.REPLACEABLE_PLANT)) {
				//TODO: for BCLib 0.5.3
				//ComposterAPI.allowCompost(0.1f, item);
				allowCompost(0.1F, item);
			}
		});
	}
}