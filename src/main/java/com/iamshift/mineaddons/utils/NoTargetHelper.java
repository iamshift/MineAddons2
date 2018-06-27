package com.iamshift.mineaddons.utils;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Set;

import javax.swing.text.html.parser.Entity;

import com.google.common.collect.Sets;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class NoTargetHelper
{
	public static void removeTargetTasks(EntityLiving target)
	{
		Set<EntityAITasks.EntityAITaskEntry> set = Sets.<EntityAITasks.EntityAITaskEntry>newLinkedHashSet();
		for(EntityAITaskEntry ai : target.targetTasks.taskEntries)
			set.add(ai);

		for(EntityAITaskEntry ai : set)
			target.targetTasks.removeTask(ai.action);

		if(!target.hasCustomName())
			target.setCustomNameTag(" " + target.getName() + " ");

		target.enablePersistence();
	}
}
