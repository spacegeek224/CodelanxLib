/*
 * Copyright (C) 2015 Codelanx, All Rights Reserved
 *
 * This work is licensed under a Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 *
 * This program is protected software: You are free to distrubute your
 * own use of this software under the terms of the Creative Commons BY-NC-ND
 * license as published by Creative Commons in the year 2015 or as published
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

import com.codelanx.codelanxlib.internal.InternalLang;
import com.codelanx.codelanxlib.config.Lang;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

/**
 * Represents the status of an executed command, and is returned upon the
 * completion of the command's execution
 *
 * @since 0.1.0
 * @author 1Rogue
 * @version 0.1.0
 */
public enum CommandStatus {

    /** The command executed successfully */
    SUCCESS,
    /** The command did not execute correctly */
    FAILED,
    /** User supplied bad command arguments */
    BAD_ARGS,
    /** Command is intended to be run by players only */
    PLAYER_ONLY("players"),
    /** Command is intended to be run by the console only */
    CONSOLE_ONLY("the console"),
    /** Command is intended to be run by remote consoles (rcon) only */
    RCON_ONLY("remote consoles"),
    /** The user does not have the appropriate permissions to execute this */
    NO_PERMISSION,
    /** The command is not meant to be an endpoint */
    NOT_EXECUTABLE;

    /** Formatter arguments for output */
    private final Object[] args;

    /**
     * Private enum constructor. Assigns formatter arguments to be used later if
     * any are present
     *
     * @since 0.1.0
     * @version 0.1.0
     *
     * @param args The formatter arguments to store
     */
    private CommandStatus(Object... args) {
        this.args = args;
    }

    /**
     * Handles output to a {@link CommandSender} upon the execution of a command
     * 
     * @since 0.1.0
     * @version 0.1.0
     * 
     * @param sender The {@link CommandSender} object that executed the command
     * @param format The {@link Lang} being used as a format string
     * @param cmd The {@link CommandNode} that was executed
     */
    public void handle(CommandSender sender, Lang format, CommandNode<?> cmd) {
        switch (this) {
            case FAILED:
                Lang.sendMessage(sender, format, InternalLang.COMMAND_STATUS_FAILED);
                break;
            case BAD_ARGS:
                Lang.sendMessage(sender, format, InternalLang.COMMAND_STATUS_USAGE, cmd.getUsage());
                Lang.sendMessage(sender, format, cmd.info());
                break;
            case PLAYER_ONLY:
            case CONSOLE_ONLY:
            case RCON_ONLY:
                Lang.sendMessage(sender, format, InternalLang.COMMAND_STATUS_RESTRICTED, this.args);
                break;
            case NO_PERMISSION:
                Lang.sendMessage(sender, format, InternalLang.COMMAND_STATUS_NOPERM);
                break;
            case NOT_EXECUTABLE:
                List<CommandNode<? extends Plugin>> cmds = new ArrayList<>(cmd.closestCommands());
                if (cmds.size() > 3) {
                    cmds = cmds.subList(0, 2);
                }
                StringBuilder sb = new StringBuilder();
                cmds.forEach(c -> sb.append('\n').append('/').append(c.getUsage()));
                Lang.sendMessage(sender, format, InternalLang.COMMAND_STATUS_NOTEXEC, sb.toString());
                break;
        }
    }
    
}
