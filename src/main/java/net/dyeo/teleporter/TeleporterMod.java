package net.dyeo.teleporter;

import net.dyeo.teleporter.network.TeleporterMessage;
import net.dyeo.teleporter.network.TeleporterMessageHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.dyeo.teleporter.command.CommandTeleportReset;
import net.dyeo.teleporter.common.config.ModConfiguration;
import net.dyeo.teleporter.event.TeleportEventHandler;
import net.dyeo.teleporter.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;


@Mod(modid = TeleporterMod.MODID, name = TeleporterMod.NAME, version = TeleporterMod.VERSION, updateJSON = TeleporterMod.UPDATEJSON, dependencies = "required-after:forge@[14.21.0.2363,);")
public class TeleporterMod
{
	public static final String MODID = "teleporter";
	public static final String NAME = "Vanilla-Inspired Teleporters";
	public static final String VERSION = "${version}";
	public static final String UPDATEJSON = "https://raw.githubusercontent.com/crazysnailboy/VanillaTeleporter/master/update.json";

	private static final String CLIENT_PROXY_CLASS = "net.dyeo.teleporter.proxy.ClientProxy";
	private static final String SERVER_PROXY_CLASS = "net.dyeo.teleporter.proxy.ServerProxy";

	@Instance(MODID)
	public static TeleporterMod instance;

	@SidedProxy(clientSide = CLIENT_PROXY_CLASS, serverSide = SERVER_PROXY_CLASS)
	public static CommonProxy proxy;

	public static final SimpleNetworkWrapper NETWORK_WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

	public static final Logger LOGGER = LogManager.getLogger(MODID);

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		proxy.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit();
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandTeleportReset());
	}

}
