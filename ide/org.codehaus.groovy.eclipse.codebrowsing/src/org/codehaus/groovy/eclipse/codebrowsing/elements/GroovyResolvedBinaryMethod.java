/*
 * Copyright 2003-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codehaus.groovy.eclipse.codebrowsing.elements;

import org.eclipse.jdt.internal.core.JavaElement;
import org.eclipse.jdt.internal.core.ResolvedBinaryMethod;

/**
 * A resolved java element suitable for hovers.  Includes
 * extra javaDoc information to appear in the hover
 * @author Andrew Eisenberg
 * @created Nov 22, 2010
 */
public class GroovyResolvedBinaryMethod extends ResolvedBinaryMethod implements IGroovyResolvedElement {

    private final String extraDoc;
    
    public GroovyResolvedBinaryMethod(JavaElement parent, String name,
            String[] parameterTypes, String uniqueKey, String extraDoc) {
        super(parent, name, parameterTypes, uniqueKey);
        this.extraDoc = extraDoc;
    }

    public String getExtraDoc() {
        return extraDoc;
    }
}