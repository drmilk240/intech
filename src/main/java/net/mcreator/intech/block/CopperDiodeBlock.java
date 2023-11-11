
package net.mcreator.intech.block;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class CopperDiodeBlock extends Block implements EntityBlock {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final EnumProperty<AttachFace> FACE = FaceAttachedHorizontalDirectionalBlock.FACE;

	public CopperDiodeBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.COPPER).strength(1f, 10f).noOcclusion().isRedstoneConductor((bs, br, bp) -> false));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(FACE, AttachFace.WALL));
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
		return true;
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 0;
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(FACING)) {
			default -> switch (state.getValue(FACE)) {
				case FLOOR -> Shapes.or(box(6, 2, 6, 10, 14, 10), box(5, 14, 5, 11, 16, 11), box(5, 0, 5, 11, 2, 11), box(5, 6, 5, 11, 10, 11));
				case WALL -> Shapes.or(box(6, 6, 2, 10, 10, 14), box(5, 5, 14, 11, 11, 16), box(5, 5, 0, 11, 11, 2), box(5, 5, 6, 11, 11, 10));
				case CEILING -> Shapes.or(box(6, 2, 6, 10, 14, 10), box(5, 0, 5, 11, 2, 11), box(5, 14, 5, 11, 16, 11), box(5, 6, 5, 11, 10, 11));
			};
			case NORTH -> switch (state.getValue(FACE)) {
				case FLOOR -> Shapes.or(box(6, 2, 6, 10, 14, 10), box(5, 14, 5, 11, 16, 11), box(5, 0, 5, 11, 2, 11), box(5, 6, 5, 11, 10, 11));
				case WALL -> Shapes.or(box(6, 6, 2, 10, 10, 14), box(5, 5, 0, 11, 11, 2), box(5, 5, 14, 11, 11, 16), box(5, 5, 6, 11, 11, 10));
				case CEILING -> Shapes.or(box(6, 2, 6, 10, 14, 10), box(5, 0, 5, 11, 2, 11), box(5, 14, 5, 11, 16, 11), box(5, 6, 5, 11, 10, 11));
			};
			case EAST -> switch (state.getValue(FACE)) {
				case FLOOR -> Shapes.or(box(6, 2, 6, 10, 14, 10), box(5, 14, 5, 11, 16, 11), box(5, 0, 5, 11, 2, 11), box(5, 6, 5, 11, 10, 11));
				case WALL -> Shapes.or(box(2, 6, 6, 14, 10, 10), box(14, 5, 5, 16, 11, 11), box(0, 5, 5, 2, 11, 11), box(6, 5, 5, 10, 11, 11));
				case CEILING -> Shapes.or(box(6, 2, 6, 10, 14, 10), box(5, 0, 5, 11, 2, 11), box(5, 14, 5, 11, 16, 11), box(5, 6, 5, 11, 10, 11));
			};
			case WEST -> switch (state.getValue(FACE)) {
				case FLOOR -> Shapes.or(box(6, 2, 6, 10, 14, 10), box(5, 14, 5, 11, 16, 11), box(5, 0, 5, 11, 2, 11), box(5, 6, 5, 11, 10, 11));
				case WALL -> Shapes.or(box(2, 6, 6, 14, 10, 10), box(0, 5, 5, 2, 11, 11), box(14, 5, 5, 16, 11, 11), box(6, 5, 5, 10, 11, 11));
				case CEILING -> Shapes.or(box(6, 2, 6, 10, 14, 10), box(5, 0, 5, 11, 2, 11), box(5, 14, 5, 11, 16, 11), box(5, 6, 5, 11, 10, 11));
			};
		};
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, FACE);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		if (context.getClickedFace().getAxis() == Direction.Axis.Y)
			return this.defaultBlockState().setValue(FACE, context.getClickedFace().getOpposite() == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR).setValue(FACING, context.getHorizontalDirection());
		return this.defaultBlockState().setValue(FACE, AttachFace.WALL).setValue(FACING, context.getClickedFace());
	}

	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		List<ItemStack> dropsOriginal = super.getDrops(state, builder);
		if (!dropsOriginal.isEmpty())
			return dropsOriginal;
		return Collections.singletonList(new ItemStack(this, 1));
	}

	@Override
	public void onPlace(BlockState blockstate, Level world, BlockPos pos, BlockState oldState, boolean moving) {
		super.onPlace(blockstate, world, pos, oldState, moving);
		world.scheduleTick(pos, this, 1);
	}

	@Override
	public void neighborChanged(BlockState blockstate, Level world, BlockPos pos, Block neighborBlock, BlockPos fromPos, boolean moving) {
		super.neighborChanged(blockstate, world, pos, neighborBlock, fromPos, moving);
		if (world.getBestNeighborSignal(pos) > 0) {
			CopperDiodeRedstoneOnProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ());
		} else {
			CopperDiodeBlockIsPlacedByProcedure.execute();
		}
	}

	@Override
	public void tick(BlockState blockstate, ServerLevel world, BlockPos pos, RandomSource random) {
		super.tick(blockstate, world, pos, random);
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		CopperDiodeUpdateTickProcedure.execute();
		world.scheduleTick(pos, this, 1);
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState blockstate, LivingEntity entity, ItemStack itemstack) {
		super.setPlacedBy(world, pos, blockstate, entity, itemstack);
		CopperDiodeBlockIsPlacedByProcedure.execute();
	}

	@Override
	public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos) {
		BlockEntity tileEntity = worldIn.getBlockEntity(pos);
		return tileEntity instanceof MenuProvider menuProvider ? menuProvider : null;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CopperDiodeBlockEntity(pos, state);
	}

	@Override
	public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int eventID, int eventParam) {
		super.triggerEvent(state, world, pos, eventID, eventParam);
		BlockEntity blockEntity = world.getBlockEntity(pos);
		return blockEntity == null ? false : blockEntity.triggerEvent(eventID, eventParam);
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof CopperDiodeBlockEntity be) {
				Containers.dropContents(world, pos, be);
				world.updateNeighbourForOutputSignal(pos, this);
			}
			super.onRemove(state, world, pos, newState, isMoving);
		}
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState blockState, Level world, BlockPos pos) {
		BlockEntity tileentity = world.getBlockEntity(pos);
		if (tileentity instanceof CopperDiodeBlockEntity be)
			return AbstractContainerMenu.getRedstoneSignalFromContainer(be);
		else
			return 0;
	}
}