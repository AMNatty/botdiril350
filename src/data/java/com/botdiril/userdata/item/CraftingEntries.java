package com.botdiril.userdata.item;

import com.botdiril.userdata.IGameObject;

import java.util.*;

public class CraftingEntries
{
    private static final Map<IGameObject, Recipe> recipeMap = new HashMap<>();
    private static final List<Recipe> recipes = new ArrayList<>();

    public static void add(Recipe recipe)
    {
        recipes.add(recipe);
        recipeMap.put(recipe.result(), recipe);
    }

    public static List<Recipe> getRecipes()
    {
        return Collections.unmodifiableList(recipes);
    }

    public static Recipe search(IGameObject result)
    {
        return recipeMap.get(result);
    }
}
