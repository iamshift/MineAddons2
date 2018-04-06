package com.iamshift.mineaddons.cmds;

import java.util.ArrayList;
import java.util.List;

import com.iamshift.mineaddons.utils.TimeSkipHandler;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class TimeSkipCommand extends CommandBase
{
	private String name = "timeskip";
	private List<String> alias = new ArrayList<String>();

	public TimeSkipCommand()
	{
		alias.add("timeskip");
		alias.add("ts");
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public List<String> getAliases()
	{
		return alias;
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return "/timeskip [cancel]";
	}
	
	@Override
	public int getRequiredPermissionLevel()
	{
		return 0;
	}
	
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender)
	{
		return true;
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos)
	{
		if(args.length == 1)
			return getListOfStringsMatchingLastWord(args, "cancel");
		
		return super.getTabCompletions(server, sender, args, targetPos);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if(sender.getEntityWorld().isRemote)
			return;
		
		if(args.length < 1)
		{
			TimeSkipHandler.timeleft(sender);
			return;
		}
		
		TimeSkipHandler.cancelTimeSkip();
	}

}
