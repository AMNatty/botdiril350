package com.botdiril.command.general;

import com.botdiril.Botdiril;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.command.invoke.CommandException;
import com.botdiril.framework.response.ResponseEmbed;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

@Command(value = "patchnotes", category = CommandCategory.GENERAL, aliases = { "patch", "patchlist", "changelog", "changes" },
    description = "Shows the latest changes of the bot.")
public class CommandPatchNotes
{
    private static final String PATCH_NOTES_FILENAME = "assets/PATCH_NOTES.yaml";
    private static final Path PATCH_NOTES_FILE = Path.of(PATCH_NOTES_FILENAME);
    private static final String PATCH_NOTES_LINK = "%s/blob/master/%s".formatted(Botdiril.REPO_URL, PATCH_NOTES_FILENAME);

    @CmdInvoke
    public static void choose(CommandContext co)
    {
        var patchNotes = load();

        if (patchNotes == null)
            return;

        var patchText = patchNotes.patchList.get(patchNotes.latest);

        var eb = new ResponseEmbed();
        eb.setTitle("%s patch %s (latest patch)".formatted(Botdiril.BRANDING, patchNotes.latest));
        eb.setDescription(patchText.replaceAll(" ", "\u00A0"));
        eb.setColor(0x0090FF);
        eb.setThumbnail(co.botIconURL);
        eb.addField("",
            "*For the full list of changes, please see the [source code](%s).*".formatted(PATCH_NOTES_LINK),
            false);

        co.respond(eb);
    }

    @CmdInvoke
    public static void choose(CommandContext co, @CmdPar("patch ID") String patch)
    {
        var patchNotes = load();

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

        var eb = new ResponseEmbed();
        var patchNameFmt = patch.equals(patchNotes.latest) ?  "%s patch %s (latest patch)" : "%s patch %s";
        eb.setTitle(patchNameFmt.formatted(Botdiril.BRANDING, patch));
        eb.setDescription(patchText.replaceAll(" ", "\u00A0"));
        eb.setColor(0x0090FF);
        eb.setThumbnail(co.botIconURL);
        eb.addField("",
            "*For the full list of changes, please see the [source code](%s).*".formatted(PATCH_NOTES_LINK),
            false);

        co.respond(eb);
    }

    private static PatchNotes load()
    {
        try (var br = Files.newBufferedReader(PATCH_NOTES_FILE))
        {
            var yaml = new Yaml(new Constructor(PatchNotes.class));
            return yaml.load(br);
        }
        catch (Exception e)
        {
            throw new CommandException("*Error: Failed to retrieve the patch notes, sorry.*", e);
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
