/*
 * This file is part of GumTree.
 *
 * GumTree is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GumTree is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GumTree.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011-2015 Jean-Rémy Falleri <jr.falleri@gmail.com>
 * Copyright 2011-2015 Floréal Morandat <florealm@gmail.com>
 */

package com.github.gumtreediff.jdt;

import java.util.ArrayDeque;
import java.util.Deque;

import com.github.gumtreediff.jdt.cd.EntityType;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;

public abstract class AbstractJdtVisitor extends ASTVisitor {

    protected TreeContext context = new TreeContext();

    private Deque<ITree> trees = new ArrayDeque<>();

    public AbstractJdtVisitor() {
        super(true);
    }

    public TreeContext getTreeContext() {
        return context;
    }

    protected void pushNode(ASTNode n, String label) {
        int type = n.getNodeType();
        String typeName = n.getClass().getSimpleName();
        push(type, typeName, label, n.getStartPosition(), n.getLength(), n);
    }

    protected void pushFakeNode(EntityType n, int startPosition, int length) {
        int type = -n.ordinal(); // Fake types have negative types (but does it matter ?)
        String typeName = n.name();
        push(type, typeName, "", startPosition, length);
    }
    
    private void push(int type, String typeName, String label, int startPosition, int length,ASTNode node) {
        ITree t = context.createTree(type, label, typeName, node);
        t.setPos(startPosition);
        t.setLength(length);
        
//        if(node instanceof MethodInvocation){
//        	MethodInvocation md = (MethodInvocation) node;
//        	IMethodBinding mb = md.resolveMethodBinding();
//            //如果binding有效，且通过对象或类名调用
//            if(mb!=null&&md.getExpression()!=null){
//            	JdtMethodCall jdtBinding = new JdtMethodCall(md.getExpression().resolveTypeBinding().getQualifiedName(),
//            			mb.getName(), mb.getReturnType().getQualifiedName(), mb.getDeclaringClass().getQualifiedName());
//                ITypeBinding[] list = mb.getParameterTypes();
//                for(int i = 0; i < list.length; i++){
//                	jdtBinding.addParameter(list[i].getQualifiedName());
//                }
//                jdtBinding.setJdk(isJdk(md.getExpression().resolveTypeBinding().getQualifiedName()));
//                ((Tree)t).setMethodCall(jdtBinding);
//            }else{
//            	if(mb==null)
//            		System.out.println(md.getName()+" is null.");
//            	if(md.getExpression()==null)
//            		System.out.println(md.getName()+" is local method.");
//            }
//        }
        
        if (trees.isEmpty())
            context.setRoot(t);
        else {
            ITree parent = trees.peek();
            t.setParentAndUpdateChildren(parent);
        }

        trees.push(t);
    }
    private void push(int type, String typeName, String label, int startPosition, int length) {
        ITree t = context.createTree(type, label, typeName);
        t.setPos(startPosition);
        t.setLength(length);

        if (trees.isEmpty())
            context.setRoot(t);
        else {
            ITree parent = trees.peek();
            t.setParentAndUpdateChildren(parent);
        }

        trees.push(t);
    }

    protected ITree getCurrentParent() {
        return trees.peek();
    }

    protected void popNode() {
        trees.pop();
    }
//    private boolean isJdk(String s){
//    	try {
//    		String temp = s;
//    		if(temp.contains("<")){
//    			temp = temp.split("<")[0];
//    		}
//			Class.forName(temp);
//			return true;
//		} catch (ClassNotFoundException e) {
//	    	return false;
//		}
//    }
}
