package cz.tefek.botdiril.framework.permission;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.Botdiril;
import cz.tefek.botdiril.framework.sql.DBConnection;
import cz.tefek.botdiril.serverdata.RolePreferences;

public enum EnumPowerLevel
{
    EVERYONE("Default",
        "Default execution level. Everyone has this power level.",
        Set.of(), (member, channel) -> true,
        false,
        "DEFAULT"),

    ELEVATED("Elevated User",
        "Grants elevated permissions like using the bot in a disabled channel. Automatically granted by having a manage channel/permission role for the text channel. Assignable by a role.",
        Set.of(EVERYONE),
        (member, channel) -> member.hasPermission(channel, Permission.MANAGE_PERMISSIONS) || member.hasPermission(channel, Permission.MANAGE_CHANNEL),
        true,
        "ELEVATED"),

    VIP("VIP",
        "Grants special perks. Assignable by a role.",
        Set.of(EVERYONE),
        (member, channel) -> false,
        true,
        "VIP"),

    SUPERUSER("SuperUser",
        "Grants access to some administrator commands. All administrators have this execution level by default. Assignable by a role.",
        Set.of(ELEVATED, VIP),
        (member, channel) -> member.hasPermission(Permission.ADMINISTRATOR),
        true,
        "SUPERUSER"),

    VIP_PLUS("VIP+",
        "Grants more special perks. Assignable by a role.",
        Set.of(VIP),
        (member, channel) -> false,
        false,
        "VIP_PLUS"),

    SUPERUSER_OVERRIDE("Executive SuperUser",
        "Grants (almost) full control of the bot. Executive SuperUsers can be only defined by the bot developer.",
        Set.of(SUPERUSER, VIP_PLUS), (member, channel) -> BotMain.config.getSuperuserOverrideIDs().contains(member.getUser().getIdLong()),
        false,
        "EXECUTIVE_SUPERUSER"),

    SUPERUSER_OWNER("Executive Bot Owner",
        "Full control of the bot.",
        Set.of(SUPERUSER_OVERRIDE, VIP_PLUS),
        (member, channel) -> member.getUser().getIdLong() == Botdiril.AUTHOR_ID,
        false,
        "OWNER");


    private static final Map<String, EnumPowerLevel> nameLookup = new HashMap<>();

    static
    {
        for (var value : values())
            if (value.id != null)
                nameLookup.put(value.id, value);
    }

    public static EnumPowerLevel getByID(String id)
    {
        return nameLookup.get(id);
    }

    private final String formalName;
    private final BiPredicate<Member, TextChannel> predicate;
    private final Set<EnumPowerLevel> cumulativePowers;
    private final Set<EnumPowerLevel> managedPowers;
    private final boolean assignable;

    private final String id;
    private final String description;

    EnumPowerLevel(String formalName, String description, Set<EnumPowerLevel> inheritsFrom, BiPredicate<Member, TextChannel> preconditions, boolean assignable, String id)
    {
        this.description = description;
        this.formalName = formalName;
        this.predicate = preconditions;
        this.assignable = assignable;
        this.id = id;

        this.cumulativePowers = inheritsFrom
            .stream()
            .map(EnumPowerLevel::getImplicitCumulativePowers)
            .flatMap(Set::stream)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        this.managedPowers = Set.copyOf(this.cumulativePowers);

        this.cumulativePowers.add(this);
    }

    public boolean isAssignable()
    {
        return this.assignable;
    }

    public String getDescription()
    {
        return this.description;
    }

    public String getID()
    {
        return this.id;
    }

    public Set<EnumPowerLevel> getManagedPowers()
    {
        return this.managedPowers;
    }

    public Set<EnumPowerLevel> getImplicitCumulativePowers()
    {
        return Collections.unmodifiableSet(this.cumulativePowers);
    }

    public boolean isImplicitlyGranted(Member member, TextChannel tc)
    {
        return this.predicate.test(member, tc);
    }

    public boolean check(DBConnection db, Member member, TextChannel tc)
    {
        final var roles = member.getRoles();

        var pl = RolePreferences.getAllPowerLevels(db, roles);
        pl.addAll(PowerLevel.getImplicitlyGrantedPowers(member, tc));

        return pl
            .stream()
            .anyMatch(this::satisfies);
    }

    /**
     * Reversed satisfies.
     */
    private boolean isSatisfiedBy(EnumPowerLevel permLevel)
    {
        if (permLevel == this)
        {
            return true;
        }

        return this.getImplicitCumulativePowers().contains(permLevel);
    }

    public boolean satisfies(EnumPowerLevel permLevel)
    {
        if (permLevel == this)
        {
            return true;
        }

        return permLevel.getImplicitCumulativePowers().contains(this);
    }

    @Override
    public String toString()
    {
        return this.formalName;
    }
}
