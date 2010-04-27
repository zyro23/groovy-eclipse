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
package org.codehaus.groovy.eclipse.refactoring.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.eclipse.core.compiler.GroovySnippetParser;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;

/**
 * Various AST related helpers
 * @author mike klenk
 * 
 */

public class ASTTools {

	public static final int SPACE = 1;
	public static final int TAB = 2;

	/**
	 * Returns a selection which includes the whole block statement
	 * 
	 * @param block
	 * @param doc
	 * @return
	 */
	public static TextSelection getPositionOfBlockStatements(
			BlockStatement block, IDocument doc) {
		int startPosition, endPosition;
		TextSelection methodStatements = new TextSelection(0, 0);

		if (block.getStatements().size() > 0) {
			// Set relative position of the new statement block
			Statement firstStmt = (Statement) block.getStatements().get(0);
			Statement lastStmt = (Statement) block.getStatements().get(
					block.getStatements().size() - 1);

			// solve false line information in non explicit
			// return statement
			if (firstStmt instanceof ReturnStatement
					&& firstStmt.getLineNumber() == -1) {
				Expression exp = ((ReturnStatement) firstStmt).getExpression();
				startPosition = exp.getStart();
			} else {
				startPosition = firstStmt.getStart();
			}
			if (lastStmt instanceof ReturnStatement
					&& lastStmt.getLineNumber() == -1) {
				Expression exp = ((ReturnStatement) lastStmt).getExpression();
				endPosition = exp.getEnd();
			} else {
				endPosition = lastStmt.getEnd();
			}
			methodStatements = new TextSelection(doc, startPosition, endPosition-startPosition);

		}
		return methodStatements;
	}

	/**
	 * Return a selection which includes e possible leading gap in front of the
	 * given ast node
	 * 
	 * @param node
	 * @param doc
	 * @return
	 */
	public static TextSelection includeLeedingGap(ASTNode node, IDocument doc) {
		if (ASTTools.hasValidPosition(node)) {
			try {
				String firstLine = doc.get(	doc.getLineOffset(node.getLineNumber() - 1), 
											doc.getLineLength(node.getLineNumber() - 1));
				
				String prefix = firstLine.substring(0, node.getColumnNumber() - 1);

				if (trimLeadingGap(prefix).length() == 0) {
				    IRegion r = doc.getLineInformation(node.getLineNumber()-1);
					return new TextSelection(doc, r.getOffset(), r.getLength());
				}
			} catch (BadLocationException e) {
				throw new RuntimeException(e);
			}
		}
		return new TextSelection(doc, 0, doc.getLength());
	}

	/**
	 * Return true if all positions of an ast node are not -1
	 * 
	 * @param node
	 * @return
	 */
	public static boolean hasValidPosition(ASTNode node) {
		return (	node.getLineNumber() > 0 && 
					node.getColumnNumber() > 0 && 
					node.getLastLineNumber() > 0 && 
					node.getLastColumnNumber() > 0);
	}

	/**
	 * Remove the leading space (tabs/spaces) in front of the first character
	 * 
	 * @param text
	 * @return
	 */
	public static String trimLeadingGap(String text) {
		return text.replaceFirst("[ \t\f]*", "");
	}

	/**
	 * Returns the leading gap in front of the first character
	 * 
	 * @param text
	 * @return
	 */
	public static String getLeadingGap(String text) {
		return text.replace(trimLeadingGap(text), "");
	}

	/**
	 * Returns the given String with a changed intentation. Existing intentation
	 * is replaced with space with the given modus.
	 * 
	 * @param text
	 * @param intentation
	 *            given in "Tabs" <0 results in a movement on the left 0 has no
	 *            effect >0 moves the text to the right
	 * @param modus
	 *            fill the leading space with: ASTTools.SPACE or ASTTools.TAB.
	 *            Tab is the default behaviour.
	 * @return
	 */
	public static String setIndentationTo(String text, int intentation, int modus) {

		StringBuilder retString = new StringBuilder();

		// Compile the pattern
		String patternStr = ".+?(\n|\r\n|\r|\\z)";
		Pattern pattern = Pattern.compile(patternStr, Pattern.DOTALL);
		Matcher matcher = pattern.matcher(text);

		// Read the lines
		while (matcher.find()) {

			// Get the line with the line termination character sequence
			String line = matcher.group(0);

			int currentIntetnation = getCurrentIntentation(line);
			String space;
			switch (modus) {
				case SPACE:
					space = "    ";
					break;
				default:
					space = "\t";
					break;
			}
			for (int i = 0; i < (currentIntetnation + intentation); i++) {
				retString.append(space);
			}
			retString.append(trimLeadingGap(line));
		}
		return retString.toString();
	}

	/**
	 * Return the current Intentation of this string measured in "Tabs"
	 * 
	 * @param line
	 * @return
	 */
	public static int getCurrentIntentation(String line) {
		String leadingGap = getLeadingGap(line);
		int tabs = 0, spaces = 0;
		for (int i = 0; i < leadingGap.length(); i++) {
			switch (leadingGap.charAt(i)) {
				case '\t':
					tabs++;
					break;
				case ' ':
					spaces++;
					break;
				default:
					break;
			}
		}
		int currentIntetnation = tabs + (spaces / 4);
		return currentIntetnation;
	}

	public static IDocument getDocumentWithSystemLineBreak(String text) {

		Document document = new Document();
		String linebreak = document.getDefaultLineDelimiter();
		document.set(text);
		
		try {
			int lineCount = document.getNumberOfLines();
			MultiTextEdit multiEdit = new MultiTextEdit();
			for (int i = 0; i < lineCount; i++) {
				final String delimiter = document.getLineDelimiter(i);
				if (	delimiter != null && 
						delimiter.length() > 0 && 
						!delimiter.equals(linebreak)) {
					IRegion region = document.getLineInformation(i);
					multiEdit.addChild(new ReplaceEdit(region.getOffset()
									+ region.getLength(), delimiter.length(),
									linebreak));
				}
			}
			multiEdit.apply(document);
		} catch (Exception e) {
		}
		return document;
	}
	
	public static ModuleNode getASTNodeFromSource(String source) {
		GroovySnippetParser parser = new GroovySnippetParser();
		ModuleNode node = parser.parse(source);
		return node;
	}
	
	public static boolean hasMultipleReturnStatements(Statement statement) {
		List<ReturnStatement> returns = new ArrayList<ReturnStatement>();
		statement.visit(new FindReturns(returns));
		return returns.size() > 1;
	}
	
	private static class FindReturns extends ASTVisitorDecorator<List<ReturnStatement>> {

		public FindReturns(List<ReturnStatement> container) {
			super(container);
		}
		
		@Override
        public void visitReturnStatement(ReturnStatement statement) {
			container.add(statement);
			super.visitReturnStatement(statement);
		}
		
	}

	public static String getTextofNode(ASTNode node, IDocument document) {
		TextSelection sel = new TextSelection(document, node.getStart(), node.getEnd()-node.getStart());
		try {
			return document.get(sel.getOffset(), sel.getLength());
		} catch (BadLocationException e) {
			return "";
		}
	}

}