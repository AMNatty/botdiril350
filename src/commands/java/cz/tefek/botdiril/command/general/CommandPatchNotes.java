package cz.tefek.botdiril.command.general;

import net.dv8tion.jda.api.EmbedBuilder;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import cz.tefek.botdiril.Botdiril;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;

@Command(value = "patchnotes", category = CommandCategory.GENERAL, aliases = { "patch", "patchlist", "changelog", "changes" },
    description = "Shows the latest changes of the bot.")
public class CommandPatchNotes
{
    private static final Path PATCH_NOTES_FILE = Path.of("assets/PATCH_NOTES.yaml");

    @CmdInvoke
    public static void choose(CallObj co)
    {
        var patchNotes = load(co);

        if (patchNotes == null)
            return;

        var eb = new EmbedBuilder();
        eb.setTitle("%s patch %s (latest patch)".formatted(Botdiril.BRANDING, patchNotes.latest));
        eb.setDescription(patchNotes.patchList.get(patchNotes.latest));
        eb.setColor(0x0090FF);
        eb.setThumbnail(co.bot.getEffectiveAvatarUrl());
        eb.addField("",
            "*For the full list of changes, please see the [source code](%s/blob/master/assets/PATCH_NOTES.yml).*".formatted(Botdiril.REPO_URL),
            false);

        co.respond(eb.build());
    }

    @CmdInvoke
    public static void choose(CallObj co, @CmdPar("patch ID") String patch)
    {
        var patchNotes = load(co);

        if (patchNotes == null)
            return;

        var patchText = patchNotes.patchList.get(patch);

        if (patchText == null)
        {
            var patches = patchNotes.patchList.keySet().stream().map("`%s`"::formatted).collect(Collectors.joining(", "));
            co.respond("""
                **No such patch.**

                *Try one of these:* %s
                """.formatted(patches));

            return;
        }

        var eb = new EmbedBuilder();
        var patchNameFmt = patch.equals(patchNotes.latest) ?  "%s patch %s (latest patch)" : "%s patch %s";
        eb.setTitle(patchNameFmt.formatted(Botdiril.BRANDING, patch));
        eb.setDescription(patchText);
        eb.setColor(0x0090FF);
        eb.setThumbnail(co.bot.getEffectiveAvatarUrl());
        eb.addField("",
            "*For the full list of changes, please see the [source code](%s/blob/master/assets/PATCH_NOTES.yml).*".formatted(Botdiril.REPO_URL),
            false);

        co.respond(eb.build());
    }

    private static PatchNotes load(CallObj co)
    {
        try (var br = Files.newBufferedReader(PATCH_NOTES_FILE))
        {
            var yaml = new Yaml(new Constructor(PatchNotes.class));
            return yaml.load(br);
        }
        catch (Exception e)
        {
            co.respond("*Error: Failed to retrieve the patch notes, sorry.*");
            e.printStackTrace();
            return null;
        }
    }

    public static class PatchNotes
    {
        private String latest;
        private LinkedHashMap<String, String> patchList;

        public PatchNotes()
        {
        }

        public void setLatest(String latest)
        {
            this.latest = latest;
        }

        public void setPatchList(LinkedHashMap<String, String> patchList)
        {
            this.patchList = patchList;
        }
    }
}
