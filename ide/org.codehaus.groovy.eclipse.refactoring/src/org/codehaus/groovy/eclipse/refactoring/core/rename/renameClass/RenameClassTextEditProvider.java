/* 
 * Copyright (C) 2007, 2009 Martin Kempf, Reto Kleeb, Michael Klenk
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 * http://ifs.hsr.ch/
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

package org.codehaus.groovy.eclipse.refactoring.core.rename.renameClass;

import java.util.List;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.eclipse.refactoring.core.documentProvider.IGroovyDocumentProvider;
import org.codehaus.groovy.eclipse.refactoring.core.rename.RenameTextEdit;
import org.codehaus.groovy.eclipse.refactoring.core.rename.RenameTextEditProvider;
import org.eclipse.text.edits.MultiTextEdit;

public class RenameClassTextEditProvider extends RenameTextEditProvider{
	
	private ClassNode oldNameClassNode;
	
	public RenameClassTextEditProvider(ClassNode oldNameClassNode, IGroovyDocumentProvider docprovider) {
		super(oldNameClassNode.getName(), docprovider);
		this.oldNameClassNode = oldNameClassNode;
	}

	@Override
    public MultiTextEdit getMultiTextEdit() {		
		RenameTextEdit textEdit = new RenameClassTextEdit(docProvider, oldNameClassNode, newName);
		textEdit.scanAST();
		return textEdit.getEdits();
	}

	@Override
    public List<String> getAlreadyUsedNames() {
		ClassNamesInSamePackageCollector usedNames = new ClassNamesInSamePackageCollector(docProvider.getRootNode(), oldNameClassNode);
		usedNames.scanAST();
		return usedNames.getClassNamesInSamePackage();
	}
	
}