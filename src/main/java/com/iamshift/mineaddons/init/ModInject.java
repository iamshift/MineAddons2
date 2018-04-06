package com.iamshift.mineaddons.init;

import java.util.Map;

import com.iamshift.mineaddons.network.Inject;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public class ModInject implements IFMLLoadingPlugin
{
	public static boolean runtimeDeobf = false;
	
	@Override
	public String[] getASMTransformerClass()
	{
		System.out.println("taet");
		return new String[] {Inject.class.getName()};
	}

	@Override
	public String getModContainerClass()
	{
		return null;
	}

	@Override
	public String getSetupClass()
	{
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data)
	{
		runtimeDeobf = (Boolean) data.get("runtimeDeobfuscationEnabled");
	}

	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}

}
