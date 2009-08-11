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
package org.codehaus.groovy.eclipse.core.inference.internal;

import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.eclipse.core.types.Field;
import org.codehaus.groovy.eclipse.core.types.ITypeEvaluationContext;
import org.codehaus.groovy.eclipse.core.types.TypeEvaluationContextBuilder;
import org.codehaus.groovy.eclipse.core.types.TypeEvaluator;
import org.codehaus.groovy.eclipse.core.types.TypeUtil;
import org.codehaus.groovy.eclipse.core.types.TypeEvaluator.EvalResult;
import org.codehaus.groovy.eclipse.core.types.internal.InferringEvaluationContext;
import org.eclipse.jface.text.Region;

/**
 * Perform inference on fields defined in this class. Super classes are ignored.
 * 
 * @author empovazan
 */
public class InferThisClassFieldTypeOperation {
	private Field field;

	private InferringEvaluationContext evalContext;

	public InferThisClassFieldTypeOperation(Field field, InferringEvaluationContext evalContext) {
		this.field = field;
		this.evalContext = evalContext;
	}

	public Field getField() {
		// First try for field assignments in the same method closest to source code context location. These are the most
		// accurate match.
		Expression[] expressions = findFieldExpressionsInMethodScope(field, evalContext);
		if (expressions.length != 0) {
			return evalExpression(expressions[0], evalContext);
		}

		expressions = findAllFieldAssignExpressions(field, evalContext);
		if (expressions.length != 0) {
			return evalExpression(expressions[0], evalContext);
		} else {
			return new Field(field.getSignature(), field.getModifiers(), field.getName(), field.getDeclaringClass(),
					true);
		}
	}

	private Field evalExpression(Expression expression, InferringEvaluationContext evalContext) {
		ITypeEvaluationContext newEvalContext = new TypeEvaluationContextBuilder()
				.typeEvaluationContext(evalContext).project(evalContext.getProject())
				.location(new Region(expression.getStart(), expression.getLength()))
				.done();
		TypeEvaluator eval = new TypeEvaluator(newEvalContext);
		EvalResult result = eval.evaluate(expression);
		String signature; 
		
		// FUTURE: emp - When trying to complete something like a Closure expression, the evaluation is null.
		// Force to object for now. There needs to be a way to say 'null' and have inference give up.
		if (result == null) {
			signature = TypeUtil.OBJECT_TYPE;
		} else {
			signature = result.getName();
		}
		return new Field(signature, field.getModifiers(), field.getName(), field
				.getDeclaringClass(), true);
	}

	private Expression[] findFieldExpressionsInMethodScope(Field field, InferringEvaluationContext evalContext) {
		AssignmentExpressionCollector collector = new AssignmentExpressionCollector(evalContext)
				.fieldName(field.getName())
				.inCurrentScope()
				.closestToContext()
				.reverseResults();
		return collector.getExpressions();
	}
	
	private Expression[] findAllFieldAssignExpressions(Field field2, InferringEvaluationContext evalContext2) {
		AssignmentExpressionCollector collector = new AssignmentExpressionCollector(evalContext)
				.fieldName(field.getName());
		return collector.getExpressions();
	}
}