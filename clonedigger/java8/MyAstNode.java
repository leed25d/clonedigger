import java.util.*;

import java.io.*;

public class MyAstNode  {

    boolean is_statement = false;

    String value;
    final int lineNumber;
    final int startIndex;
    final int endIndex;

    public MyAstNode(String value, int lineNumber, int startIndex, int endIndex) {
        this.value = value;
        this.lineNumber = lineNumber;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    private final List<MyAstNode> children = new ArrayList<>();

    public List<MyAstNode> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public int getStartIndex() {
        return this.startIndex;
    }

    public int getEndIndex() {
        return this.endIndex;
    }

    public void addChild(MyAstNode child) {
        this.children.add(child);
    }

    public void setValue(String value) {
        this.value = value;
    }

    /*public MyAstNode(Token t) {
	super(t);
    }*/
}
