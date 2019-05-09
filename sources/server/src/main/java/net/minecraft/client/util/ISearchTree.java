package net.minecraft.client.util;

import java.util.List;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface ISearchTree<T>
{
    List<T> search(String searchText);
}