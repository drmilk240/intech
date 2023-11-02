
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.intech.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.Block;

import net.mcreator.intech.block.entity.CopperPipeOutputBlockEntity;
import net.mcreator.intech.block.entity.CopperPipeInputBlockEntity;
import net.mcreator.intech.block.entity.CopperPipeBlockEntity;
import net.mcreator.intech.IntechMod;

public class IntechModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, IntechMod.MODID);
	public static final RegistryObject<BlockEntityType<?>> COPPER_PIPE = register("copper_pipe", IntechModBlocks.COPPER_PIPE, CopperPipeBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> COPPER_PIPE_INPUT = register("copper_pipe_input", IntechModBlocks.COPPER_PIPE_INPUT, CopperPipeInputBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> COPPER_PIPE_OUTPUT = register("copper_pipe_output", IntechModBlocks.COPPER_PIPE_OUTPUT, CopperPipeOutputBlockEntity::new);

	private static RegistryObject<BlockEntityType<?>> register(String registryname, RegistryObject<Block> block, BlockEntityType.BlockEntitySupplier<?> supplier) {
		return REGISTRY.register(registryname, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
	}
}
