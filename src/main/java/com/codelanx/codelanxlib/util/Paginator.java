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

import com.codelanx.codelanxlib.config.Lang;
import com.codelanx.codelanxlib.internal.InternalLang;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Wraps text in formatted bars with a title, and allows for paging through
 * text content
 *
 * @since 0.1.0
 * @author 1Rogue
 * @version 0.1.0
 */
public class Paginator {

    private final String BAR;
    private final List<String> pages = new ArrayList<>();

    /**
     * Constructor. Splits the {@code wholeText} parameter by a newline
     * character ({@code \n}) and forwards it to
     * {@link Paginator#Paginator(String, int, List)}
     * 
     * @since 0.1.0
     * @version 0.1.0
     * 
     * @see Paginator#Paginator(String, int, List)
     * @param title The title for the pages
     * @param itemsPerPage The number of items from the content parameter to
     *                     display on a page
     * @param wholeText A string to be split by the newline character {@code \n}
     */
    public Paginator(String title, int itemsPerPage, String wholeText) {
        this(title, itemsPerPage, wholeText.split("\n"));
    }

    /**
     * Constructor. Converts the passed {@code itr} parameter into a 
     * {@link List} and forwards it to
     * {@link Paginator#Paginator(String, int, List)}
     * 
     * @since 0.1.0
     * @version 0.1.0
     * 
     * @see Paginator#Paginator(String, int, List)
     * @param title The title for the pages
     * @param itemsPerPage The number of items from the content parameter to
     *                     display on a page
     * @param itr An iterable collection of strings
     */
    public Paginator(String title, int itemsPerPage, String... itr) {
        this(title, itemsPerPage, Arrays.asList(itr));
    }

    /**
     * Constructor. Takes a {@link List} of strings and creates formatted
     * pages which can be output to a
     * {@link org.bukkit.command.CommandSender CommandSender}. These pages
     * should be considered immutable as they are only rendered once and then
     * subsequently stored.
     * 
     * @since 0.1.0
     * @version 0.1.0
     * 
     * @param title The title for the pages
     * @param itemsPerPage The number of items from the content parameter to
     *                     display on a page
     * @param content A {@link List} of strings to display
     */
    public Paginator(String title, int itemsPerPage, List<String> content) {
        String s = InternalLang.PAGINATOR_BARCHAR.formatAndColor();
        if (s.isEmpty()) {
            this.BAR = "------------------------------"
                    + "------------------------------";
        } else {
            char[] barr = new char[60];
            char c = s.toCharArray()[0];
            for (int i = barr.length - 1; i >= 0; i--) {
                barr[i] = c;
            }
            this.BAR = new String(barr);
        }
        //divide into pages
        int pageCount = content.size() / itemsPerPage + ((content.size() % itemsPerPage) == 0 ? 0 : 1);
        for (int i = 0; i < pageCount; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.formatTitle(title,
                InternalLang.PAGINATOR_BARCOLOR.formatAndColor(),
                InternalLang.PAGINATOR_TITLECOLOR.formatAndColor()));
            sb.append('\n');
            sb.append(InternalLang.PAGINATOR_PAGEFORMAT.formatAndColor(i + 1, pageCount));
            sb.append('\n');
            int stop = (i + 1) * itemsPerPage;
            if (stop > content.size()) {
                stop = content.size();
            }
            for (int w = i * itemsPerPage; w < stop; w++) {
                sb.append(content.get(w)).append('\n');
            }
            sb.append(this.formatFooter(InternalLang.PAGINATOR_BARCOLOR.formatAndColor()));
            sb.append('\n');
            this.pages.add(sb.toString());
        }
    }

    /**
     * Returns the appropriately formatted page for this {@link Paginator}
     * 
     * @since 0.1.0
     * @version 0.1.0
     * 
     * @param page The page to retrieve
     * @return The page in the form of a string 
     */
    public String getPage(int page) {
        page--;
        if (page < 0 || page > this.pages.size()) {
            throw new IndexOutOfBoundsException("Page " + ++page + " does not exist");
        }
        return this.pages.get(page);
    }

    /**
     * Formats the title-bar for displaying help information
     *
     * @since 0.1.0
     * @version 0.1.0
     *
     * @param title The title to use
     * @param barcolor The color of the bar (ref: {@link ChatColor})
     * @param titlecolor The color of the title (ref: {@link ChatColor})
     * @return A formatted header
     */
    private String formatTitle(String title, String barcolor, String titlecolor) {
        String line = barcolor + this.BAR;
        int pivot = line.length() / 2;
        String center = InternalLang.PAGINATOR_TITLECONTAINER.formatAndColor(barcolor, titlecolor, title);
        return Lang.color(line.substring(0, pivot - center.length() / 2)
                + center
                + line.substring(0, pivot - center.length() / 2));
    }

    /**
     * Formats the footer-bar of the help information.
     *
     * @since 0.1.0
     * @version 0.1.0
     *
     * @param barcolor The color of the footer-bar
     * @return A formatted footer
     */
    private String formatFooter(String barcolor) {
        String back = barcolor + this.BAR;
        return Lang.color(back.substring(0, back.length() - 11));
    }

    /**
     * Returns the number of pages in this instance
     * 
     * @since 0.1.0
     * @version 0.1.0
     * 
     * @return The number of pages
     */
    public int size() {
        return this.pages.size();
    }

    /**
     * Returns a copy of all the pages in this instance
     * 
     * @since 0.1.0
     * @version 0.1.0
     * 
     * @return A copy of the pages
     */
    public List<String> getPages() {
        return Collections.unmodifiableList(this.pages);
    }

}
