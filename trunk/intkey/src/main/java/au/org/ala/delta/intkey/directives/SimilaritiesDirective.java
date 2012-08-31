/*******************************************************************************
 * Copyright (C) 2011 Atlas of Living Australia
 * All Rights Reserved.
 * 
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 ******************************************************************************/
package au.org.ala.delta.intkey.directives;

import java.util.ArrayList;
import java.util.List;

import au.org.ala.delta.intkey.directives.invocation.IntkeyDirectiveInvocation;
import au.org.ala.delta.intkey.directives.invocation.SimilaritiesDirectiveInvocation;
import au.org.ala.delta.intkey.model.IntkeyContext;

public class SimilaritiesDirective extends StandardIntkeyDirective {

    public SimilaritiesDirective() {
        super(true, "similarities");
    }

    @Override
    protected List<IntkeyDirectiveArgument<?>> generateArgumentsList(IntkeyContext context) {
        List<IntkeyDirectiveArgument<?>> arguments = new ArrayList<IntkeyDirectiveArgument<?>>();
        arguments.add(new BracketedTaxonListArgument("selectedTaxaSpecimen", null, false, false));
        arguments.add(new CharacterListArgument("characters", null, false, false));
        return arguments;
    }

    @Override
    protected List<IntkeyDirectiveFlag> buildFlagsList() {
        List<IntkeyDirectiveFlag> flags = new ArrayList<IntkeyDirectiveFlag>();
        flags.add(new IntkeyDirectiveFlag('O', "matchOverlap", false));
        flags.add(new IntkeyDirectiveFlag('S', "matchSubset", false));
        flags.add(new IntkeyDirectiveFlag('E', "matchExact", false));
        flags.add(new IntkeyDirectiveFlag('U', "matchUnknowns", false));
        flags.add(new IntkeyDirectiveFlag('I', "matchInapplicables", false));
        return flags;
    }

    @Override
    protected IntkeyDirectiveInvocation buildCommandObject() {
        return new SimilaritiesDirectiveInvocation();
    }

}
