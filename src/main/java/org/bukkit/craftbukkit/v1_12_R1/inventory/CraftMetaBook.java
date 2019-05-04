/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Strings
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.chat.ComponentSerializer
 *  org.apache.commons.lang3.Validate
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.apache.commons.lang3.Validate;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaItem;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftChatMessage;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

@DelegateDeserialization(value=CraftMetaItem.SerializableMeta.class)
public class CraftMetaBook
extends CraftMetaItem
implements BookMeta {
    static final CraftMetaItem.ItemMetaKey BOOK_TITLE = new CraftMetaItem.ItemMetaKey("title");
    static final CraftMetaItem.ItemMetaKey BOOK_AUTHOR = new CraftMetaItem.ItemMetaKey("author");
    static final CraftMetaItem.ItemMetaKey BOOK_PAGES = new CraftMetaItem.ItemMetaKey("pages");
    static final CraftMetaItem.ItemMetaKey RESOLVED = new CraftMetaItem.ItemMetaKey("resolved");
    static final CraftMetaItem.ItemMetaKey GENERATION = new CraftMetaItem.ItemMetaKey("generation");
    static final int MAX_PAGES = 50;
    static final int MAX_PAGE_LENGTH = 320;
    static final int MAX_TITLE_LENGTH = 32;
    protected String title;
    protected String author;
    public List<hh> pages = new ArrayList<hh>();
    protected Integer generation;
    private final BookMeta.Spigot spigot;

    CraftMetaBook(CraftMetaItem meta) {
        super(meta);
        this.spigot = new BookMeta.Spigot(){

            @Override
            public BaseComponent[] getPage(int page) {
                Validate.isTrue((boolean)CraftMetaBook.this.isValidPage(page), (String)"Invalid page number", (Object[])new Object[0]);
                return ComponentSerializer.parse((String)hh.a.a(CraftMetaBook.this.pages.get(page - 1)));
            }

            @Override
            public /* varargs */ void setPage(int page, BaseComponent ... text) {
                if (!CraftMetaBook.this.isValidPage(page)) {
                    throw new IllegalArgumentException("Invalid page number " + page + "/" + CraftMetaBook.this.pages.size());
                }
                BaseComponent[] newText = text == null ? new BaseComponent[]{} : text;
                CraftMetaBook.this.pages.set(page - 1, hh.a.a(ComponentSerializer.toString((BaseComponent[])newText)));
            }

            @Override
            public /* varargs */ void setPages(BaseComponent[] ... pages) {
                CraftMetaBook.this.pages.clear();
                this.addPage(pages);
            }

            @Override
            public /* varargs */ void addPage(BaseComponent[] ... pages) {
                for (BaseComponent[] page : pages) {
                    if (CraftMetaBook.this.pages.size() >= 50) {
                        return;
                    }
                    if (page == null) {
                        page = new BaseComponent[]{};
                    }
                    CraftMetaBook.this.pages.add(hh.a.a(ComponentSerializer.toString((BaseComponent[])page)));
                }
            }

            @Override
            public List<BaseComponent[]> getPages() {
                ImmutableList copy = ImmutableList.copyOf(CraftMetaBook.this.pages);
                return new AbstractList<BaseComponent[]>((List)copy){
                    final /* synthetic */ List val$copy;

                    @Override
                    public BaseComponent[] get(int index) {
                        return ComponentSerializer.parse((String)hh.a.a((hh)this.val$copy.get(index)));
                    }

                    @Override
                    public int size() {
                        return this.val$copy.size();
                    }
                };
            }

            @Override
            public void setPages(List<BaseComponent[]> pages) {
                CraftMetaBook.this.pages.clear();
                for (BaseComponent[] page : pages) {
                    this.addPage(page);
                }
            }

        };
        if (meta instanceof CraftMetaBook) {
            CraftMetaBook bookMeta = (CraftMetaBook)meta;
            this.title = bookMeta.title;
            this.author = bookMeta.author;
            this.pages.addAll(bookMeta.pages);
            this.generation = bookMeta.generation;
        }
    }

    CraftMetaBook(fy tag) {
        this(tag, true);
    }

    CraftMetaBook(fy tag, boolean handlePages) {
        super(tag);
        this.spigot = new ;
        if (tag.e(CraftMetaBook.BOOK_TITLE.NBT)) {
            this.title = tag.l(CraftMetaBook.BOOK_TITLE.NBT);
        }
        if (tag.e(CraftMetaBook.BOOK_AUTHOR.NBT)) {
            this.author = tag.l(CraftMetaBook.BOOK_AUTHOR.NBT);
        }
        boolean resolved = false;
        if (tag.e(CraftMetaBook.RESOLVED.NBT)) {
            resolved = tag.q(CraftMetaBook.RESOLVED.NBT);
        }
        if (tag.e(CraftMetaBook.GENERATION.NBT)) {
            this.generation = tag.h(CraftMetaBook.GENERATION.NBT);
        }
        if (tag.e(CraftMetaBook.BOOK_PAGES.NBT) && handlePages) {
            ge pages = tag.c(CraftMetaBook.BOOK_PAGES.NBT, 8);
            for (int i2 = 0; i2 < Math.min(pages.c(), 50); ++i2) {
                String page = pages.h(i2);
                if (resolved) {
                    try {
                        this.pages.add(hh.a.a(page));
                        continue;
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
                this.addPage(page);
            }
        }
    }

    CraftMetaBook(Map<String, Object> map) {
        super(map);
        this.spigot = new ;
        this.setAuthor(CraftMetaItem.SerializableMeta.getString(map, CraftMetaBook.BOOK_AUTHOR.BUKKIT, true));
        this.setTitle(CraftMetaItem.SerializableMeta.getString(map, CraftMetaBook.BOOK_TITLE.BUKKIT, true));
        Iterable pages = CraftMetaItem.SerializableMeta.getObject(Iterable.class, map, CraftMetaBook.BOOK_PAGES.BUKKIT, true);
        if (pages != null) {
            for (Object page : pages) {
                if (!(page instanceof String)) continue;
                this.addPage((String)page);
            }
        }
        this.generation = CraftMetaItem.SerializableMeta.getObject(Integer.class, map, CraftMetaBook.GENERATION.BUKKIT, true);
    }

    @Override
    void applyToItem(fy itemData) {
        this.applyToItem(itemData, true);
    }

    void applyToItem(fy itemData, boolean handlePages) {
        super.applyToItem(itemData);
        if (this.hasTitle()) {
            itemData.a(CraftMetaBook.BOOK_TITLE.NBT, this.title);
        }
        if (this.hasAuthor()) {
            itemData.a(CraftMetaBook.BOOK_AUTHOR.NBT, this.author);
        }
        if (handlePages) {
            if (this.hasPages()) {
                ge list = new ge();
                for (hh page : this.pages) {
                    list.a(new gm(CraftChatMessage.fromComponent(page)));
                }
                itemData.a(CraftMetaBook.BOOK_PAGES.NBT, list);
            }
            itemData.r(CraftMetaBook.RESOLVED.NBT);
        }
        if (this.generation != null) {
            itemData.a(CraftMetaBook.GENERATION.NBT, this.generation);
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isBookEmpty();
    }

    boolean isBookEmpty() {
        return !this.hasPages() && !this.hasAuthor() && !this.hasTitle();
    }

    @Override
    boolean applicableTo(Material type) {
        switch (type) {
            case WRITTEN_BOOK: 
            case BOOK_AND_QUILL: {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasAuthor() {
        return !Strings.isNullOrEmpty((String)this.author);
    }

    @Override
    public boolean hasTitle() {
        return !Strings.isNullOrEmpty((String)this.title);
    }

    @Override
    public boolean hasPages() {
        return !this.pages.isEmpty();
    }

    @Override
    public boolean hasGeneration() {
        return this.generation != null;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public boolean setTitle(String title) {
        if (title == null) {
            this.title = null;
            return true;
        }
        if (title.length() > 32) {
            return false;
        }
        this.title = title;
        return true;
    }

    @Override
    public String getAuthor() {
        return this.author;
    }

    @Override
    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public BookMeta.Generation getGeneration() {
        return this.generation == null ? null : BookMeta.Generation.values()[this.generation];
    }

    @Override
    public void setGeneration(BookMeta.Generation generation) {
        this.generation = generation == null ? null : Integer.valueOf(generation.ordinal());
    }

    @Override
    public String getPage(int page) {
        Validate.isTrue((boolean)this.isValidPage(page), (String)"Invalid page number", (Object[])new Object[0]);
        return CraftChatMessage.fromComponent(this.pages.get(page - 1));
    }

    @Override
    public void setPage(int page, String text) {
        if (!this.isValidPage(page)) {
            throw new IllegalArgumentException("Invalid page number " + page + "/" + this.pages.size());
        }
        String newText = text == null ? "" : (text.length() > 320 ? text.substring(0, 320) : text);
        this.pages.set(page - 1, CraftChatMessage.fromString(newText, true)[0]);
    }

    @Override
    public /* varargs */ void setPages(String ... pages) {
        this.pages.clear();
        this.addPage(pages);
    }

    @Override
    public /* varargs */ void addPage(String ... pages) {
        for (String page : pages) {
            if (this.pages.size() >= 50) {
                return;
            }
            if (page == null) {
                page = "";
            } else if (page.length() > 320) {
                page = page.substring(0, 320);
            }
            this.pages.add(CraftChatMessage.fromString(page, true)[0]);
        }
    }

    @Override
    public int getPageCount() {
        return this.pages.size();
    }

    @Override
    public List<String> getPages() {
        ImmutableList copy = ImmutableList.copyOf(this.pages);
        return new AbstractList<String>((List)copy){
            final /* synthetic */ List val$copy;

            @Override
            public String get(int index) {
                return CraftChatMessage.fromComponent((hh)this.val$copy.get(index));
            }

            @Override
            public int size() {
                return this.val$copy.size();
            }
        };
    }

    @Override
    public void setPages(List<String> pages) {
        this.pages.clear();
        for (String page : pages) {
            this.addPage(page);
        }
    }

    private boolean isValidPage(int page) {
        return page > 0 && page <= this.pages.size();
    }

    @Override
    public CraftMetaBook clone() {
        CraftMetaBook meta = (CraftMetaBook)super.clone();
        meta.pages = new ArrayList<hh>(this.pages);
        return meta;
    }

    @Override
    int applyHash() {
        int original;
        int hash = original = super.applyHash();
        if (this.hasTitle()) {
            hash = 61 * hash + this.title.hashCode();
        }
        if (this.hasAuthor()) {
            hash = 61 * hash + 13 * this.author.hashCode();
        }
        if (this.hasPages()) {
            hash = 61 * hash + 17 * this.pages.hashCode();
        }
        if (this.hasGeneration()) {
            hash = 61 * hash + 19 * this.generation.hashCode();
        }
        return original != hash ? CraftMetaBook.class.hashCode() ^ hash : hash;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaBook) {
            CraftMetaBook that = (CraftMetaBook)meta;
            return (this.hasTitle() ? that.hasTitle() && this.title.equals(that.title) : !that.hasTitle()) && (this.hasAuthor() ? that.hasAuthor() && this.author.equals(that.author) : !that.hasAuthor()) && (this.hasPages() ? that.hasPages() && this.pages.equals(that.pages) : !that.hasPages()) && (this.hasGeneration() ? that.hasGeneration() && this.generation.equals(that.generation) : !that.hasGeneration());
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaBook || this.isBookEmpty());
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);
        if (this.hasTitle()) {
            builder.put((Object)CraftMetaBook.BOOK_TITLE.BUKKIT, (Object)this.title);
        }
        if (this.hasAuthor()) {
            builder.put((Object)CraftMetaBook.BOOK_AUTHOR.BUKKIT, (Object)this.author);
        }
        if (this.hasPages()) {
            ArrayList<String> pagesString = new ArrayList<String>();
            for (hh comp : this.pages) {
                pagesString.add(CraftChatMessage.fromComponent(comp));
            }
            builder.put((Object)CraftMetaBook.BOOK_PAGES.BUKKIT, pagesString);
        }
        if (this.generation != null) {
            builder.put((Object)CraftMetaBook.GENERATION.BUKKIT, (Object)this.generation);
        }
        return builder;
    }

    @Override
    public BookMeta.Spigot spigot() {
        return this.spigot;
    }

}

