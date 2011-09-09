package au.org.ala.delta.intkey.directives;

import java.util.Queue;

import au.org.ala.delta.intkey.model.IntkeyContext;

public class IntegerArgument extends IntkeyDirectiveArgument {

    public IntegerArgument(String name, String promptText) {
        super(name, promptText);
    }

    @Override
    public Object parseInput(Queue<String> inputTokens, IntkeyContext context, String directiveName) throws IntkeyDirectiveParseException {
        String token = inputTokens.poll();

        if (token == null) {
            token = context.getDirectivePopulator().promptForString(getPromptText(), null, directiveName);
        }

        if (token != null) {
            try {
                int parsedInteger = Integer.parseInt(token);
                return parsedInteger;
            } catch (NumberFormatException ex) {
                throw new IntkeyDirectiveParseException("Integer value required", ex);
            }
        }
        return null;
    }
}