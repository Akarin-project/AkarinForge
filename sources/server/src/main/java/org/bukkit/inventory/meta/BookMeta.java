/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.BaseComponent
 */
package org.bukkit.inventory.meta;

import java.util.List;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.inventory.meta.ItemMeta;

public interface BookMeta
extends ItemMeta {
    public boolean hasTitle();

    public String getTitle();

    public boolean setTitle(String var1);

    public boolean hasAuthor();

    public String getAuthor();

    public void setAuthor(String var1);

    public boolean hasGeneration();

    public Generation getGeneration();

    public void setGeneration(Generation var1);

    public boolean hasPages();

    public String getPage(int var1);

    public void setPage(int var1, String var2);

    public List<String> getPages();

    public void setPages(List<String> var1);

    public /* varargs */ void setPages(String ... var1);

    public /* varargs */ void addPage(String ... var1);

    public int getPageCount();

    @Override
    public BookMeta clone();

    @Override
    public Spigot spigot();

    public static class Spigot
    extends ItemMeta.Spigot {
        public BaseComponent[] getPage(int page) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public /* varargs */ void setPage(int page, BaseComponent ... data) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public List<BaseComponent[]> getPages() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setPages(List<BaseComponent[]> pages) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public /* varargs */ void setPages(BaseComponent[] ... pages) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public /* varargs */ void addPage(BaseComponent[] ... pages) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static enum Generation {
        ORIGINAL,
        COPY_OF_ORIGINAL,
        COPY_OF_COPY,
        TATTERED;
        

        private Generation() {
        }
    }

}

