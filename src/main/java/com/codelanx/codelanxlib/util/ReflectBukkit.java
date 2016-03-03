/*
 * Copyright (C) 2016 Codelanx, All Rights Reserved
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
package com.codelanx.codelanxlib.util;

import com.codelanx.commons.logging.Debugger;
import com.codelanx.commons.util.Reflections;
import com.codelanx.commons.util.exception.Exceptions;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Rogue on 11/17/2015.
 */
public class ReflectBukkit { //blergh, save me from this classname

    /**
     * Gets the default {@link World} loaded by Bukkit
     *
     * @since 0.1.0
     * @version 0.1.0
     *
     * @return Bukkit's default {@link World} object
     */
    public static World getDefaultWorld() {
        Exceptions.illegalState(!Bukkit.getServer().getWorlds().isEmpty(), "No worlds loaded");
        return Bukkit.getServer().getWorlds().get(0);
    }


    /**
     * Returns the {@link JavaPlugin} that immediately called the method in the
     * current context. Useful for finding out which plugins accessed static API
     * methods. This method is equivalent to calling
     * {@code Reflections.getCallingPlugin(0)}
     *
     * @since 0.1.0
     * @version 0.1.0
     *
     * @see ReflectBukkit#getCallingPlugin(int)
     * @return The relevant {@link JavaPlugin}
     * @throws UnsupportedOperationException If not called from a
     * {@link JavaPlugin} class (either through an alternative ClassLoader,
     * executing code directly, or some voodoo magic)
     */
    public static JavaPlugin getCallingPlugin() {
        return ReflectBukkit.getCallingPlugin(1);
    }


    /**
     * Façade method for determining if Bukkit is the invoker of the method
     *
     * @since 0.1.0
     * @version 0.1.0
     *
     * @return {@code true} if Bukkit is the direct invoker of the method
     */
    public static boolean accessedFromBukkit() {
        return Reflections.getCaller(1).getClassName().startsWith("org.bukkit.");
    }

    /**
     * Returns the {@link JavaPlugin} that immediately called the method in the
     * current context. Useful for finding out which plugins accessed static API
     * methods
     *
     * @since 0.1.0
     * @version 0.1.0
     *
     * @param offset The number of additional methods to look back
     * @return The relevant {@link JavaPlugin}
     * @throws UnsupportedOperationException If not called from a
     * {@link JavaPlugin} class (either through an alternative ClassLoader,
     * executing code directly, or some voodoo magic)
     */
    public static JavaPlugin getCallingPlugin(int offset) {
        try {
            Class<?> cl = Class.forName(Reflections.getCaller(1 + offset).getClassName());
            JavaPlugin back = JavaPlugin.getProvidingPlugin(cl);
            if (back == null) {
                throw new UnsupportedOperationException("Must be called from a class loaded from a plugin");
            }
            return back;
        } catch (ClassNotFoundException ex) {
            //Potentially dangerous (Stackoverflow)
            Debugger.error(ex, "Error reflecting for plugin class");
            throw new IllegalStateException("Could not load from called class (Classloader issue?)", ex);
        }
    }
}
