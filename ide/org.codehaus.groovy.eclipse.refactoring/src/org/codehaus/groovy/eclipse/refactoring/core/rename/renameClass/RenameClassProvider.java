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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.ConstructorNode;
import org.codehaus.groovy.ast.ImportNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ConstructorCallExpression;
import org.codehaus.groovy.eclipse.refactoring.core.GroovyChange;
import org.codehaus.groovy.eclipse.refactoring.core.MultiFileRefactoringProvider;
import org.codehaus.groovy.eclipse.refactoring.core.UserSelection;
import org.codehaus.groovy.eclipse.refactoring.core.documentProvider.IGroovyDocumentProvider;
import org.codehaus.groovy.eclipse.refactoring.core.documentProvider.IGroovyFileProvider;
import org.codehaus.groovy.eclipse.refactoring.core.rename.IRenameProvider;
import org.codehaus.groovy.eclipse.refactoring.core.rename.RenameTextEditProvider;
import org.codehaus.groovy.eclipse.refactoring.core.utils.GroovyConventionsBuilder;
import org.codehaus.groovy.eclipse.refactoring.core.utils.ImportResolver;
import org.codehaus.groovy.eclipse.refactoring.ui.GroovyRefactoringMessages;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;

/**
 * Provider for the Refactoring Rename Class
 * @author martin kempf
 *
 */
public class RenameClassProvider extends MultiFileRefactoringProvider implements IRenameProvider{
	
	private final ClassNode selectedNode;
	private String alias;
	protected List<RenameTextEditProvider> textEditProviders = new ArrayList<RenameTextEditProvider>();

	public RenameClassProvider(IGroovyFileProvider docProvider, UserSelection selection, ClassNode selectedNode) {
		super(docProvider, selection);
		this.selectedNode = selectedNode;
		createProvidersConsideringAlias();
	}

	private void createProvidersConsideringAlias() {
		//If the selected ClassNode is written as Alias, the rename is local
		alias = ImportResolver.asAlias(getDocumentProvider().getRootNode(), selectedNode);
		if (!alias.equals("")) {
			textEditProviders.add(new RenameAliasTextEditProvider(alias,selectedNode,getDocumentProvider()));
		} else {
			//selected ClassNode can be used in every file in the project
			for(IGroovyDocumentProvider document : getUsedDocuments()){
				textEditProviders.add(new RenameClassTextEditProvider(selectedNode,document));
			}
		}
	}

	@Override
    public RefactoringStatus checkFinalConditions(IProgressMonitor pm)
			throws CoreException, OperationCanceledException {
		
		RefactoringStatus state = new RefactoringStatus();
		List<String> alreadyUsedNames = new ArrayList<String>();
		for(RenameTextEditProvider textEdit : textEditProviders) {
			alreadyUsedNames.addAll(textEdit.getAlreadyUsedNames());
		}
		
		String newName = textEditProviders.get(0).getNewName();
		for (String usedName : alreadyUsedNames) {
			if (usedName.equals(newName)) {
				state.addFatalError(MessageFormat.format(
						GroovyRefactoringMessages.RenameClassInfo_ClassNameAlreadyExists,newName));
			}
		}
		return state;
	}
	
	@Override
    public void addInitialConditionsCheckStatus(RefactoringStatus status) {
		//check if the class can be renamed, only groovy classes, that are defined in the project can 
		//be renamed. Alias can always be renamed, since they are like a local class definition
		if (alias == "") {
			List<String> groovyClassNames = new ArrayList<String>();
			for(RenameTextEditProvider textEdit : textEditProviders) {
				ModuleNode rootNode = textEdit.getDocProvider().getRootNode();
				GroovyClassDefinitionCollector collector = new GroovyClassDefinitionCollector(rootNode);
				collector.scanAST();
				groovyClassNames.addAll(collector.getGroovyClasses());
			}
			if (selectedNode != null && !groovyClassNames.contains(selectedNode.getName())) {
				status.addFatalError(GroovyRefactoringMessages.RenameClassInfo_RenameNotPossible);
			}
		}
	}

	@Override
    public GroovyChange createGroovyChange(IProgressMonitor pm)
			throws CoreException, OperationCanceledException {
		GroovyChange change = new GroovyChange(GroovyRefactoringMessages.RenameClassRefactoring);
		for(RenameTextEditProvider textEdit : textEditProviders) {
			change.addEdit(textEdit);
		}
		return change;
	}

	public void checkUserInput(RefactoringStatus status, String userInput) {
		IStatus stateValidName = new GroovyConventionsBuilder(userInput, GroovyConventionsBuilder.CLASS)
				.validateGroovyIdentifier().validateUpperCase(IStatus.ERROR).done();
		addStatusEntries(status, stateValidName);
	}

	public String getOldName() {
		if (alias.equals("")) {
			return selectedNode.getName();
		}
        return alias;
	}

	public void setNewName(String newName) {
		for(RenameTextEditProvider provider : textEditProviders){
			provider.setNewName(newName);
		}
	}
	
	public static ClassNode giveClassNodeToRename(ASTNode node){
		if(node instanceof ClassNode){
			return (ClassNode)node;
		} else if(node instanceof ConstructorCallExpression){
			return ((ConstructorCallExpression)node).getType();
		} else if(node instanceof ClassExpression){
			return ((ClassExpression)node).getType();
		} else if(node instanceof ConstructorNode){
			return ((ConstructorNode)node).getDeclaringClass();
		}  else if(node instanceof ImportNode){
			return ((ImportNode)node).getType();
		}
		return null;
	}

	@Override
    protected void prepareCandidateLists() { }
	
}