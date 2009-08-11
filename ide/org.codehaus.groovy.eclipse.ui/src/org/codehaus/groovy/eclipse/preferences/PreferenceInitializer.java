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
package org.codehaus.groovy.eclipse.preferences;

import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.codehaus.groovy.eclipse.core.preferences.PreferenceConstants;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

    /*
     * (non-Javadoc)
     * 
     * @seeorg.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
     * initializeDefaultPreferences()
     */
    public void initializeDefaultPreferences() {
        IPreferenceStore store = GroovyPlugin.getDefault().getPreferenceStore();

        store.setDefault(PreferenceConstants.GROOVY_LOG_TRACE_MESSAGES_ENABLED,
                false);

        // GJDK Prefs
        store.setDefault(
                PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_GJDK_ENABLED, true);
        PreferenceConverter.setDefault(store,
                PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_GJDK_COLOR,
                new RGB(102, 204, 255));

        // Multiline Comment Prefs
        store.setDefault(PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_MULTILINECOMMENTS_ENABLED,
                         true);
        PreferenceConverter
                .setDefault(
                        store,
                        PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_MULTILINECOMMENTS_COLOR,
                        new RGB(204, 0, 0));

        // Java Keyword Prefs
        store.setDefault(PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_JAVAKEYWORDS_ENABLED,
                         true);
        PreferenceConverter.setDefault(store,
                PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_JAVAKEYWORDS_COLOR,
                new RGB(151, 44, 120));

        // Groovy Keyword Prefs
        store.setDefault(PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_GROOVYKEYWORDS_ENABLED,
                         true);
        PreferenceConverter
                .setDefault(
                        store,
                        PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_GROOVYKEYWORDS_COLOR,
                        new RGB(151, 44, 120));

        // Java Types Prefs
        store.setDefault(
                PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_JAVATYPES_ENABLED,
                true);
        PreferenceConverter.setDefault(store,
                PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_JAVATYPES_COLOR,
                new RGB(151, 44, 120));

        // String Prefs
        store.setDefault(
                PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_STRINGS_ENABLED,
                true);
        PreferenceConverter.setDefault(store,
                PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_STRINGS_COLOR,
                new RGB(255, 0, 204));

        // Number Prefs
        store.setDefault(
                PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_NUMBERS_ENABLED,
                true);
        PreferenceConverter.setDefault(store,
                PreferenceConstants.GROOVY_EDITOR_HIGHLIGHT_NUMBERS_COLOR,
                new RGB(205, 50, 0));
        
        // JUnit Monospace font
        store.setDefault(
                PreferenceConstants.GROOVY_JUNIT_MONOSPACE_FONT,
                false);
    }

}