/*
 * Copyright (C) 2014 Codelanx, All Rights Reserved
 *
 * This work is licensed under a Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 *
 * This program is protected software: You are free to distrubute your
 * own use of this software under the terms of the Creative Commons BY-NC-ND
 * license as published by Creative Commons in the year 2014 or as published
 * by a later date. You may not provide the source files or provide a means
 * of running the software outside of those licensed to use it.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the Creative Commons BY-NC-ND license
 * long with this program. If not, see <https://creativecommons.org/licenses/>.
 */
package com.codelanx.codelanxlib.command;

import com.codelanx.codelanxlib.events.ReloadEvent;
import com.codelanx.codelanxlib.implementers.Reloadable;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

/**
 * Class description for {@link ReloadCommand}
 *
 * @since 1.0.0
 * @author 1Rogue
 * @version 1.0.0
 */
public class ReloadCommand extends SubCommand {

    public ReloadCommand(Plugin plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (this.plugin instanceof Reloadable) {
            ((Reloadable) this.plugin).reload();
            this.plugin.getServer().getPluginManager().callEvent(new ReloadEvent(this.plugin));
            sender.sendMessage(this.plugin.getName() + " v" + this.plugin.getDescription().getVersion() + " reloaded!");
        } else {
            sender.sendMessage("This plugin does not support reloading!");
        }
        return true;
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String info() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}