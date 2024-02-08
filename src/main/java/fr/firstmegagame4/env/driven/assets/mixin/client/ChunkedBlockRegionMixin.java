package fr.firstmegagame4.env.driven.assets.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.moulberry.axiom.render.regions.ChunkedBlockRegion;
import fr.firstmegagame4.env.driven.assets.client.EDAEnvJsonVisitors;
import fr.firstmegagame4.env.driven.assets.client.duck.BakedModelDuckInterface;
import fr.firstmegagame4.env.driven.assets.client.injected.ModelManagerContainer;
import fr.firstmegagame4.env.driven.assets.client.model.ModelManager;
import fr.firstmegagame4.env.json.api.EnvJson;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChunkedBlockRegion.class)
public class ChunkedBlockRegionMixin {

	@ModifyExpressionValue(method = "renderBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/BlockRenderManager;getModel(Lnet/minecraft/block/BlockState;)Lnet/minecraft/client/render/model/BakedModel;"))
	private static BakedModel mutateBakedModel(BakedModel original, BufferBuilder blockBuilder, BlockRenderManager renderManager, BlockPos.Mutable blockPos, Random rand, MatrixStack matrices, BlockRenderView blockAndTintGetter, Matrix4f currentPoseMatrix, Matrix4f basePoseMatrix, int x, int y, int z, BlockState dataState, boolean useAmbientOcclusion) {
		BakedModelDuckInterface ducked = (BakedModelDuckInterface) original;
		if (ducked.env_driven_assets$getEnvJson() != null) {
			EnvJson envJson = ducked.env_driven_assets$getEnvJson();
			Identifier identifier = envJson.apply(EDAEnvJsonVisitors.blockVisitor(blockAndTintGetter, new BlockPos(blockPos)));
			if (identifier != null) {
				ModelManager manager = ((ModelManagerContainer) MinecraftClient.getInstance().getBakedModelManager()).getModelManager();
				return manager.changeModelWithoutSettings(identifier);
			}
		}
		return original;
	}
}
