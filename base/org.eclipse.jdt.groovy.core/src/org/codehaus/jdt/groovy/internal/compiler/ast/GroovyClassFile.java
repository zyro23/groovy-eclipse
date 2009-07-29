/*******************************************************************************
 * Copyright (c) 2009 Codehaus.org, SpringSource, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Andy Clement        - Initial API and implementation
 *     Andrew Eisenberg - Additional work
 *******************************************************************************/
package org.codehaus.jdt.groovy.internal.compiler.ast;

import java.util.ArrayList;

import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ClassFile;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.AnnotationMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.SyntheticMethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;

/**
 * Simple subtype of the JDT ClassFile that represents a fully built class file from groovy. It is immutable and just intended to be
 * a 'holder' for bytes from groovy that are passed back to JDT.
 * 
 * @author Andy Clement
 */
// FIXASC (M2) groovy mutator methods should through an exception
@SuppressWarnings("restriction")
class GroovyClassFile extends ClassFile {

	private byte[] bytes;
	private char[][] name;
	private char[] filename;

	@Override
	public void addAbstractMethod(AbstractMethodDeclaration method, MethodBinding methodBinding) {
		throw new ImmutableException();
	}

	@Override
	public void addAttributes() {
		throw new ImmutableException();
	}

	@Override
	public void addDefaultAbstractMethods() {
		throw new ImmutableException();
	}

	@Override
	public void addFieldInfos() {
		throw new ImmutableException();
	}

	@Override
	public void addProblemClinit(CategorizedProblem[] problems) {
		throw new ImmutableException();
	}

	@Override
	public void addProblemConstructor(AbstractMethodDeclaration method, MethodBinding methodBinding, CategorizedProblem[] problems,
			int savedOffset) {
		throw new ImmutableException();
	}

	@Override
	public void addProblemConstructor(AbstractMethodDeclaration method, MethodBinding methodBinding, CategorizedProblem[] problems) {
		throw new ImmutableException();
	}

	@Override
	public void addProblemMethod(AbstractMethodDeclaration method, MethodBinding methodBinding, CategorizedProblem[] problems,
			int savedOffset) {
		throw new ImmutableException();
	}

	@Override
	public void addProblemMethod(AbstractMethodDeclaration method, MethodBinding methodBinding, CategorizedProblem[] problems) {
		throw new ImmutableException();
	}

	@Override
	public void addSpecialMethods() {
		throw new ImmutableException();
	}

	@Override
	public void addSyntheticConstructorAccessMethod(SyntheticMethodBinding methodBinding) {
		throw new ImmutableException();
	}

	@Override
	public void addSyntheticEnumValueOfMethod(SyntheticMethodBinding methodBinding) {
		throw new ImmutableException();
	}

	@Override
	public void addSyntheticEnumValuesMethod(SyntheticMethodBinding methodBinding) {
		throw new ImmutableException();
	}

	@Override
	public void addSyntheticFieldReadAccessMethod(SyntheticMethodBinding methodBinding) {
		throw new ImmutableException();
	}

	@Override
	public void addSyntheticFieldWriteAccessMethod(SyntheticMethodBinding methodBinding) {
		throw new ImmutableException();
	}

	@Override
	public void addSyntheticMethodAccessMethod(SyntheticMethodBinding methodBinding) {
		throw new ImmutableException();
	}

	@Override
	public void addSyntheticSwitchTable(SyntheticMethodBinding methodBinding) {
		throw new ImmutableException();
	}

	@Override
	public void completeCodeAttribute(int codeAttributeOffset) {
		throw new ImmutableException();
	}

	@Override
	public void completeCodeAttributeForClinit(int codeAttributeOffset, int problemLine) {
		throw new ImmutableException();
	}

	@Override
	public void completeCodeAttributeForClinit(int codeAttributeOffset) {
		throw new ImmutableException();
	}

	@Override
	public void completeCodeAttributeForMissingAbstractProblemMethod(MethodBinding binding, int codeAttributeOffset,
			int[] startLineIndexes, int problemLine) {
		throw new ImmutableException();
	}

	@Override
	public void completeCodeAttributeForProblemMethod(AbstractMethodDeclaration method, MethodBinding binding,
			int codeAttributeOffset, int[] startLineIndexes, int problemLine) {
		throw new ImmutableException();
	}

	@Override
	public void completeCodeAttributeForSyntheticMethod(boolean hasExceptionHandlers, SyntheticMethodBinding binding,
			int codeAttributeOffset, int[] startLineIndexes) {
		throw new ImmutableException();
	}

	@Override
	public void completeCodeAttributeForSyntheticMethod(SyntheticMethodBinding binding, int codeAttributeOffset,
			int[] startLineIndexes) {
		throw new ImmutableException();
	}

	@Override
	public void completeMethodInfo(int methodAttributeOffset, int attributeNumber) {
		throw new ImmutableException();
	}

	@Override
	public char[] fileName() {
		return filename;
	}

	@Override
	public void generateCodeAttributeHeader() {
		throw new ImmutableException();
	}

	@Override
	public int generateMethodInfoAttribute(MethodBinding methodBinding, AnnotationMethodDeclaration declaration) {
		throw new ImmutableException();
	}

	@Override
	public int generateMethodInfoAttribute(MethodBinding methodBinding) {
		throw new ImmutableException();
	}

	@Override
	public void generateMethodInfoHeader(MethodBinding methodBinding, int accessFlags) {
		throw new ImmutableException();
	}

	@Override
	public void generateMethodInfoHeader(MethodBinding methodBinding) {
		throw new ImmutableException();
	}

	@Override
	public void generateMethodInfoHeaderForClinit() {
		throw new ImmutableException();
	}

	@Override
	public void generateMissingAbstractMethods(MethodDeclaration[] methodDeclarations, CompilationResult compilationResult) {
		throw new ImmutableException();
	}

	@Override
	public byte[] getBytes() {
		return bytes;
	}

	@Override
	public char[][] getCompoundName() {
		return name;
	}

	@Override
	protected void initByteArrays() {
		// FIXASC (M2) Auto-generated method stub
		super.initByteArrays();
	}

	@Override
	public void initialize(SourceTypeBinding type, ClassFile parentClassFile, boolean createProblemType) {
		// FIXASC (M2) Auto-generated method stub
		super.initialize(type, parentClassFile, createProblemType);
	}

	@Override
	public ClassFile outerMostEnclosingClassFile() {
		// FIXASC (M2) Auto-generated method stub
		return super.outerMostEnclosingClassFile();
	}

	@Override
	public void recordInnerClasses(TypeBinding binding) {
		// FIXASC (M2) Auto-generated method stub
		super.recordInnerClasses(binding);
	}

	@Override
	public void reset(SourceTypeBinding typeBinding) {
		// FIXASC (M2) Auto-generated method stub
		super.reset(typeBinding);
	}

	@Override
	public void setForMethodInfos() {
		// FIXASC (M2) Auto-generated method stub
		super.setForMethodInfos();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void traverse(MethodBinding methodBinding, int maxLocals, byte[] bytecodes, int codeOffset, int codeLength,
			ArrayList frames, boolean isClinit) {
		// FIXASC (M2) Auto-generated method stub
		super.traverse(methodBinding, maxLocals, bytecodes, codeOffset, codeLength, frames, isClinit);
	}

	@Override
	public char[] utf8At(byte[] reference, int absoluteOffset, int bytesAvailable) {
		// FIXASC (M2) Auto-generated method stub
		return super.utf8At(reference, absoluteOffset, bytesAvailable);
	}

	public GroovyClassFile(String name, byte[] bs, SourceTypeBinding sourceTypeBinding, String filename) {
		this.name = CharOperation.splitOn('.', name.toCharArray());
		this.bytes = bs;
		this.referenceBinding = sourceTypeBinding;
		this.header = new byte[0];
		this.headerOffset = 0;
		this.contents = bs;
		this.contentsOffset = bs.length;
		this.filename = filename.toCharArray();
	}

}