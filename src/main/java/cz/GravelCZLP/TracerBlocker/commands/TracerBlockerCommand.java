
package cz.GravelCZLP.TracerBlocker.commands;

import cz.GravelCZLP.TracerBlocker.TracerBlocker;
import cz.GravelCZLP.TracerBlocker.v1_11.FakePlayer.FakePlayer1_11;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TracerBlockerCommand implements CommandExecutor {

	public HashMap<UUID, FakePlayer1_11> debugEntites;

	TracerBlocker pl;

	public TracerBlockerCommand(TracerBlocker pl) {
		this.pl = pl;
		debugEntites = new HashMap<>();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission("antitracer.command") || sender.isOp()) { return false; }
		if (args[0].equalsIgnoreCase("reload")) {
			pl.loadConfig();
			return true;
		}
		if (args[0].equalsIgnoreCase("debug")) {
			if (!(sender instanceof Player)) { return false; }
			Player p = (Player) sender;
			if (args[1].equalsIgnoreCase("entity")) {
				if (args[2].equalsIgnoreCase("spawn")) {
					FakePlayer1_11 fakePlayer = new FakePlayer1_11(pl, p.getLocation());
					fakePlayer.stopRunnble();
					fakePlayer.printInfo(p);
					debugEntites.put(p.getUniqueId(), fakePlayer);
				}
				if (args[2].equalsIgnoreCase("despawn")) {
					FakePlayer1_11 fakePlayer = debugEntites.get(p.getUniqueId());
					fakePlayer.destroy();
				}
			}
			
		}
		return false;
	}

}
