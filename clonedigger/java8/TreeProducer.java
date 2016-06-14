/*  Copyright 2008 Peter Bulychev
 *
 *  This file is part of Clone Digger.
 *
 *  Clone Digger is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Clone Digger is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Clone Digger.  If not, see <http://www.gnu.org/licenses/>.
 */
import org.antlr.v4.runtime.*;
/*
import org.antlr.v4.stringtemplate.*;
*/
import org.antlr.v4.runtime.tree.*;
import java.lang.reflect.*;
import java.io.*;
import java.util.*;
import java.text.*;
import java.lang.*;

public class TreeProducer
{
    public TreeProducer ()
    {
	super ();
    }

    /*
     * forXml function was taken from http://www.javapractices.com/topic/TopicAction.do?Id=96
     * the license is: http://creativecommons.org/licenses/by/3.0/
     */
    public static String forXML (String aText)
    {
	final StringBuilder result = new StringBuilder ();
	final StringCharacterIterator iterator =
	    new StringCharacterIterator (aText);
	char character = iterator.current ();
	while (character != CharacterIterator.DONE)
	{
	    if (character == '<')
	    {
		result.append ("&lt;");
	    }
	    else if (character == '>')
	    {
		result.append ("&gt;");
	    }
	    else if (character == '\"')
	    {
		result.append ("&quot;");
	    }
	    else if (character == '\'')
	    {
		result.append ("&#039;");
	    }
	    else if (character == '&')
	    {
		result.append ("&amp;");
	    }
	    else
	    {
		//the char is not a special one
		//        //add it to the result as is
		result.append (character);
	    }
	    character = iterator.next ();
	}
	return result.toString ();
    }
    //                                      }

    public static void printTree (MyAstNode tree, PrintWriter outputStream,	String indent)
    {
    String xml_node_name = (tree.is_statement?"statement_node":"node");
    outputStream.println (indent + "<" + xml_node_name + " name=\"" + forXML (tree.value) + "\"" + 
	    " line_number=\"" + tree.getLineNumber() + "\" " + 
	    "start=\"" +  tree.getStartIndex() + "\" " + 
	    "stop=\"" + tree.getEndIndex() + "\">");
    for (MyAstNode child : tree.getChildren())
    {
        printTree (child, outputStream, indent + "  ");
    }
    outputStream.println (indent + "</"+xml_node_name+">");
    }

public static void main (String[]args) throws Exception
{
    ANTLRFileStream input = new ANTLRFileStream (args[0]);
//    JavaASTLexer lexer = new JavaASTLexer (input);
//    CommonTokenStream tokens = new CommonTokenStream (lexer);
//    JavaASTParser parser = new JavaASTParser (tokens);
//    MyAstNodeAdaptor adaptor = new MyAstNodeAdaptor ();
//    parser.setTreeAdaptor (adaptor);
//    MyAstNode tree = (MyAstNode) parser.compilationUnit ().getTree ();
	Java8Lexer lexer = new Java8Lexer(input);
	CommonTokenStream tokens = new CommonTokenStream(lexer);
	Java8Parser parser = new Java8Parser(tokens);
	Java8Parser.CompilationUnitContext tree = parser.compilationUnit(); // parse a compilationUnit
    
    MyAstNode rootNode = new MyAstNode("ROOT", 0, 0, 0);
	Java8Listener extractor = new EveryRuleJava8BaseListener(rootNode);
	ParseTreeWalker.DEFAULT.walk(extractor, tree); // initiate walk of tree with listener in use of default walker

	PrintWriter outputStream =
	new PrintWriter (new FileWriter (args[1], false));
    outputStream.println ("<?xml version=\"1.0\" ?>");
    printTree (rootNode, outputStream, "");
    outputStream.close ();
}
}
