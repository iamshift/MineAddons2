package com.iamshift.mineaddons.init;

import java.util.ArrayList;
import java.util.List;

import com.iamshift.mineaddons.core.Refs;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class ModSounds
{
	public static final List<SoundEvent> SOUNDS = new ArrayList<SoundEvent>();

	public static SoundEvent carp_ambient1;
	public static SoundEvent carp_ambient2;
	public static SoundEvent carp_ambient3;
	public static SoundEvent carp_ambient4;

	public static SoundEvent carp_hurt;
	
	public static SoundEvent voidball;
	public static SoundEvent bossfight;
	public static SoundEvent charging;
	
	public static SoundEvent push;
	public static SoundEvent pull;
	
	public static SoundEvent spiritbomb;

	public static void init()
	{
			carp_ambient1 = register("carpambient1");
			carp_ambient2 = register("carpambient2");
			carp_ambient3 = register("carpambient3");
			carp_ambient4 = register("carpambient4");

			carp_hurt = register("carphurt");
			
			voidball = register("voidball");
			bossfight = register("bossfight");
			charging = register("charging");
			
			push = register("push");
			pull = register("pull");
			
			spiritbomb = register("spiritbomb");
	}

	private static SoundEvent register(String name)
	{
		ResourceLocation loc = new ResourceLocation(Refs.ID, name);
		SoundEvent temp = new SoundEvent(loc);
		temp.setRegistryName(loc);

		SOUNDS.add(temp);

		return temp;
	}
}
