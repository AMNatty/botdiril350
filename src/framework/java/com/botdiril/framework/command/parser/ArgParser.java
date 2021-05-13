package com.botdiril.framework.command.parser;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ArgParser
{
    private static final int CP_QUOTE = '"';

    public static List<String> splitArgs(List<Parameter> parameters, String contents)
    {
        int paramCount = parameters.size();
        var quoteCount = (int) contents.codePoints().filter(c -> c == CP_QUOTE).count();
        var parser = new ArgParser(paramCount, quoteCount);
        var cps = contents.codePoints();
        cps.forEachOrdered(parser::accept);

        return parser.result();
    }

    private final List<StringBuilder> paramBuilders;
    private int currentParam;
    private int remainingQuotes;
    private final int paramCount;
    private State state;

    private ArgParser(int paramCount, int quoteCount)
    {
        this.paramBuilders = new ArrayList<>(paramCount);
        this.remainingQuotes = quoteCount;
        this.paramCount = paramCount;
        this.currentParam = 0;
        this.state = State.WHITESPACE;
    }

    private void accept(int cp)
    {
        if (cp == CP_QUOTE)
            this.remainingQuotes--;

        this.state = switch (this.state)
        {
            case WHITESPACE -> {
                if (cp == CP_QUOTE && this.remainingQuotes >= 1 && this.currentParam + 1 < this.paramCount)
                {
                    this.paramBuilders.add(new StringBuilder());
                    this.currentParam++;

                    yield State.PARAM_QUOTED;
                }

                if (!Character.isWhitespace(cp))
                {
                    var sb = new StringBuilder();
                    this.paramBuilders.add(sb);
                    this.currentParam++;

                    if (cp != CP_QUOTE)
                        sb.appendCodePoint(cp);

                    yield State.PARAM_UNQUOTED;
                }

                yield State.WHITESPACE;
            }

            case PARAM_UNQUOTED -> {
                if (Character.isWhitespace(cp) && this.currentParam < this.paramCount)
                {
                    yield State.WHITESPACE;
                }

                var sb = this.paramBuilders.get(this.currentParam - 1);
                sb.appendCodePoint(cp);

                yield State.PARAM_UNQUOTED;
            }

            case PARAM_QUOTED -> {
                if (cp == CP_QUOTE)
                {
                    yield State.PARAM_QUOTE_END_CANDIDATE;
                }

                var sb = this.paramBuilders.get(this.currentParam - 1);
                sb.appendCodePoint(cp);

                yield State.PARAM_QUOTED;
            }

            case PARAM_QUOTE_END_CANDIDATE -> {
                if (Character.isWhitespace(cp))
                {
                    yield State.WHITESPACE;
                }

                var sb = this.paramBuilders.get(this.currentParam - 1);
                sb.appendCodePoint(CP_QUOTE);

                if (cp != CP_QUOTE)
                {
                    sb.appendCodePoint(cp);
                }

                yield State.PARAM_QUOTED;
            }
        };
    }

    private List<String> result()
    {
        if (this.currentParam != this.paramCount)
            return null;

        return this.paramBuilders.stream()
            .map(StringBuilder::toString)
            .map(str -> StringUtils.unwrap(str, '"'))
            .map(String::strip)
            .collect(Collectors.toList());
    }

    private enum State
    {
        WHITESPACE,
        PARAM_UNQUOTED,
        PARAM_QUOTED,
        PARAM_QUOTE_END_CANDIDATE
    }
}
