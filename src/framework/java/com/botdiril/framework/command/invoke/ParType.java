package com.botdiril.framework.command.invoke;

import com.botdiril.userdata.IGameObject;
import com.botdiril.framework.EntityPlayer;

public enum ParType
{
    /** Any parameter class type */
    BASIC,
    /** {@link IGameObject} only */
    ITEM_OR_CARD,
    /** {@link Long}/<code>long</code> only */
    AMOUNT_COINS,
    /** {@link Long}/<code>long</code> only */
    AMOUNT_CLASSIC_KEKS,
    /** {@link Long}/<code>long</code> only */
    AMOUNT_MEGA_KEKS,
    /** {@link Long}/<code>long</code> only */
    AMOUNT_KEK_TOKENS,
    /** {@link Long}/<code>long</code> only */
    AMOUNT_DUST,
    /** {@link Long}/<code>long</code> only */
    AMOUNT_KEYS,
    /** {@link Long}/<code>long</code> only, the previous parameter must be ITEM or CARD */
    AMOUNT_ITEM_OR_CARD,
    /** {@link Long}/<code>long</code> only, the previous parameter must be ITEM */
    AMOUNT_ITEM_BUY_COINS,
    /** {@link Long}/<code>long</code> only, the previous parameter must be ITEM */
    AMOUNT_ITEM_BUY_TOKENS,
    /** {@link EntityPlayer} only, The parameter must not be the same entity as the command's caller*/
    ENTITY_NOT_SELF
}
