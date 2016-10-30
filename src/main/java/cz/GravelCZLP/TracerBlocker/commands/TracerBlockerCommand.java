package cz.GravelCZLP.TracerBlocker.commands;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cz.GravelCZLP.FakePlayer.FakePlayer1_10;
import cz.GravelCZLP.TracerBlocker.TracerBlocker;

public class TracerBlockerCommand implements CommandExecutor {

	public HashMap<UUID, FakePlayer1_10> debugEntites;
	
	TracerBlocker pl;
	
	public TracerBlockerCommand(TracerBlocker pl) {
		this.pl = pl;
		debugEntites = new HashMap<>();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission("antitracer.command") || sender.isOp()) {
			return false;
		}
		if (args[0].equalsIgnoreCase("reload")) {
			pl.loadConfig();
			return true;
		}
		if (args[0].equalsIgnoreCase("config")) {
			if (args[1].equalsIgnoreCase("save")) {
				pl.saveConfig();
				return true;
			}
			if (args[1].equalsIgnoreCase("set")) {
				if (args[2].equalsIgnoreCase("fakeplayers")) {
					if (args[3].equalsIgnoreCase("enable")) {
						
					}
				}
				if (args[2].equalsIgnoreCase("chest")) {
					//TODO:
				}
				if (args[2].equalsIgnoreCase("playerhider")) {
					//TODO: 
				}
			}
		}
		if (args[0].equalsIgnoreCase("debug")) {
			if (!(sender instanceof Player)) {
				return false;
			}
			Player p = (Player) sender;
			if (args[1].equalsIgnoreCase("entity")) {
				if (args[2].equalsIgnoreCase("spawn")) {
					FakePlayer1_10 fakePlayer = new FakePlayer1_10(pl, p.getLocation());
					fakePlayer.stopRunnble();
					fakePlayer.printInfo(p);
					debugEntites.put(p.getUniqueId(), fakePlayer);
				}
				if (args[2].equalsIgnoreCase("despawn")) {
					FakePlayer1_10 fakePlayer = debugEntites.get(p.getUniqueId());
					fakePlayer.destroy();
				}
			}
		}
		return false;
	}

}
