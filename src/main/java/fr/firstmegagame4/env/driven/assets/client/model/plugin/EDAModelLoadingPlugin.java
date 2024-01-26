package fr.firstmegagame4.env.driven.assets.client.model.plugin;

import fr.firstmegagame4.env.driven.assets.client.EDAUtils;
import fr.firstmegagame4.env.driven.assets.client.duck.BakedModelDuckInterface;
import fr.firstmegagame4.env.driven.assets.client.model.EDABakedModel;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import net.minecraft.client.render.model.BakedModel;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;

public class EDAModelLoadingPlugin implements ModelLoadingPlugin {

	private static BakedModel modifyModelAfterBake(@Nullable BakedModel model, ModelModifier.AfterBake.Context context) {
		if (model != null && !model.isBuiltin() && model instanceof BakedModelDuckInterface ducked) {
			EDAUtils.retrieveJsonUnbakedModelDuckInterfaces(context.sourceModel(), context.loader()::getOrLoadModel).forEach(
				jum -> ducked.env_driven_assets$setEnvJson(jum.env_driven_assets$getEnvJson())
			);
			return new EDABakedModel(context.loader(), model);
		}
		return model;
	}

	@Override
	public void onInitializeModelLoader(Context pluginContext) {
		pluginContext.modifyModelAfterBake().register(EDAModelLoadingPlugin::modifyModelAfterBake);
	}
}
