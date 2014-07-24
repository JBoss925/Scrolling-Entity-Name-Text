package me.JBoss925.scroll;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by jagger1 on 7/6/14.
 */
public class Main extends JavaPlugin implements Listener{

    HashMap<UUID, Entity> PE = new HashMap<UUID, Entity>();
    HashMap<Entity, String> entities = new HashMap<Entity, String>();
    HashMap<Entity, Integer> entitycurrent = new HashMap<Entity, Integer>();
    String tempname;
    private int taskid = 1;

    @Override
    public void onEnable(){
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        startscheduler();
    }

    @Override
    public void onDisable(){
        Bukkit.getServer().getScheduler().cancelTask(taskid);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandlabel, String[] args){
        if(sender.hasPermission("scroll.change.entity")){
            if(cmd.getName().equalsIgnoreCase("scrolltext") && sender instanceof Player){
                if(PE.containsKey(((Player) sender).getUniqueId())){
                    if(args.length <= 0){
                        sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Please enter a message!");
                        return true;
                    }
                    if(args.length != 0){
                        StringBuilder sb = new StringBuilder();
                        for(int x = 0; x <= args.length; x++){
                            if(x == args.length){
                                break;
                            }
                            sb.append(args[x]);
                            if(x != args.length-1){
                                sb.append(" ");
                            }
                        }
                        String s = sb.toString();
                        String ss = s;
                        for(int x = 0; x < s.length(); x++){
                            ss = ss.concat(" ");
                        }
                        entitycurrent.put(PE.get(((Player) sender).getUniqueId()), 0);
                        entities.put(PE.get(((Player) sender).getUniqueId()), ss);
                        sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Entity's name has been changed!");
                        return true;
                    }

                }
            }
        }

        return false;
    }

    public void startscheduler(){
        taskid = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new BukkitRunnable() {
            @Override
            public void run() {
                for(Entity e : entities.keySet()){
                    if(e instanceof LivingEntity){
                        ((LivingEntity) e).setCustomNameVisible(true);
                        StringBuilder stb = new StringBuilder();
                        Integer orig = entitycurrent.get(e);
                        Double origd = Double.parseDouble(orig.toString() + ".0");
                        Integer entitylength = entities.get(e).length();
                        tempname = "";
                        Double entitylengthd = Double.parseDouble(entitylength.toString() + ".0");
                        for(int x = entitycurrent.get(e); x < entitylengthd+2; x++){
                            if(x >= entities.get(e).length()){
                                x=0;
                            }
                            stb.append(entities.get(e).charAt(x));
                            tempname = stb.toString();
                            if(tempname.length() > entitylengthd){
                                break;
                            }
                        }
                        int i = entities.get(e).length();
                        double d = i;
                        ((LivingEntity) e).setCustomName(tempname.substring(0,(int) Math.round(d/2.0)));
                        entitycurrent.put(e, entitycurrent.get(e) + 1);
                        if(entitycurrent.get(e) >= entitylength){
                            entitycurrent.put(e, 0);
                        }
                    }
                }
            }
        }, 0l, 10l);
    }

    @EventHandler
    public void onEntityRightClick(PlayerInteractEntityEvent e){
        if(e.getPlayer().hasPermission("scroll.choose.entity") && e.getPlayer().isSneaking()){
            e.setCancelled(true);
            PE.put(e.getPlayer().getUniqueId(), e.getRightClicked());
            e.getPlayer().sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Entity (instance of " + ChatColor.GREEN + "" + ChatColor.BOLD + e.getRightClicked().getType().toString() + ChatColor.GREEN + "" + ChatColor.BOLD + ") has been selected!");
        }
    }
}
