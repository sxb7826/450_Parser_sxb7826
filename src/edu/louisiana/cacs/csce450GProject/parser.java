import java.io.*;
import java.util.*;



class node
{
	private String node_name;		
	private Vector<node> all_nodes;	
	
	public node(String givenName)
	{
		node_name = givenName;
		all_nodes = new Vector<node>();
	}	
	
	public void toList()
	{
		if (node_name != null && all_nodes.size() == 0) {
			all_nodes.add(new node(node_name));
			node_name = null;
		}
	}
        
	public void addNode(node n)
	{
		all_nodes.add(n);
	}
	
	public node elementAt(int ind)
	{
		return all_nodes.elementAt(ind);
	}
	
	public int size()
	{
		return all_nodes.size();
	}

	public void reduceLastItems(int n, String lhs)
	{
		node x = new node(lhs);
		for (int i = 0; i < n; i++)
			x.addNode(elementAt(size() - n + i));
	
		for (int i = 0; i < n; i++)
			all_nodes.remove(all_nodes.size() - 1);
			
		all_nodes.add(x);
	}
	
	// for parsing table
	public String getLine()
	{
		String x = node_name == null ? "" : node_name;
		
		if (all_nodes.size() > 0) {
			if (node_name != null)
				x += " [";
			
			for (int i = 0; i < all_nodes.size(); i++)
				x += (i > 0 ? " " : "") + all_nodes.elementAt(i).getLine();
			
			if (node_name != null)
				x += "]";
		}
			
		return x;
	}

	
	static public String addTab(String x)
	{
		String t;
		t = "";
		while (x.length() > 0) {
			int i;
			for (i = 0; i < x.length(); i++)
				if (x.charAt(i) == '\n')
					break;
			
			int k = i + (i < x.length() ? 1 : 0);
			t += "\t" + x.substring(0, k);
			x = x.substring(k);
		}
		
		return t;
	}
	
	// parse tree
	public String getTree()
	{
		String x = node_name == null ? "" : node_name;
		
		if (all_nodes.size() > 0) {
			if (node_name != null)
				x += " \n";
			
			for (int i = 0; i < all_nodes.size(); i++)
				x += addTab(all_nodes.elementAt(i).getTree());
			
			if (node_name != null)
				x += "\n";
		} else
			x += "\n";
			
		return x;
	}
	
	public String getTreeDoc()
	{
		String x = "";
		final int n = all_nodes.size();
		
		if (n > 0) {
			for (int i = 0; i < n; i++)
				x += all_nodes.elementAt(i).getTree() + "\n";
		}
			
		return x;
	}
}

/*************
 * LR-Parser *
 *************/
public class parser
{
	// action table values
	static final int NO_ACTION       	= 10000;
	static final int ACTION_ACCEPT      = 10001;
	static final int ACTION_COL_ID 		= 0;
	static final int ACTION_COL_PLUS	= 1;
	static final int ACTION_COL_MULT 	= 2;
	static final int ACTION_COL_LCOMP 	= 3;
	static final int ACTION_COL_RCOMP 	= 4;
	static final int ACTION_COL_END 	= 5;
	
	// action table column names
	static final String nodeActionName[] = {
		"id", "+", "*", "(", ")", "$"
	};
	
	// goto table column names
	static final String nodeTermName[] = {
		"E", "T", "F"
	};
	
	static int nodeAction[][] = {
		{ 5, NO_ACTION, NO_ACTION, 4, NO_ACTION, NO_ACTION },
		{ NO_ACTION, 6, NO_ACTION, NO_ACTION, NO_ACTION, ACTION_ACCEPT },
		{ NO_ACTION, -2 - 1, 7, NO_ACTION, -2 - 1, -2 - 1 },
		{ NO_ACTION, -4 - 1, -4 - 1, NO_ACTION, -4 - 1, -4 - 1 },
		{ 5, NO_ACTION, NO_ACTION, 4, NO_ACTION, NO_ACTION },
		{ NO_ACTION, -6 - 1, -6 - 1, NO_ACTION, -6 - 1, -6 - 1 },
		{ 5, NO_ACTION, NO_ACTION, 4, NO_ACTION, NO_ACTION },
		{ 5, NO_ACTION, NO_ACTION, 4, NO_ACTION, NO_ACTION },
		{ NO_ACTION, 6, NO_ACTION, NO_ACTION, 11, NO_ACTION },
		{ NO_ACTION, -1 - 1, 7, NO_ACTION, -1 - 1, -1 - 1 },
		{ NO_ACTION, -3 - 1, -3 - 1, NO_ACTION, -3 - 1, -3 - 1 },
		{ NO_ACTION, -5 - 1, -5 - 1, NO_ACTION, -5 - 1, -5 - 1 },
	};
	
	// goto table: the values are pushed into the stack after a reduction.
	static int nodeGoto[][] = {
		{ 1, 2, 3 },
		{ -1, -1, -1 },
		{ -1, -1, -1 },
		{ -1, -1, -1 },
		{ 8,  2,  3 },
		{ -1, -1, -1 },
		{ -1, 9, 3 },
		{ -1, -1, 10 },
		{ -1, -1, -1 },
		{ -1, -1, -1 },
		{ -1, -1, -1 },
		{ -1, -1, -1 },
	};
	
	
	static int nodeRules[][] = {
		{ 0, 3 },	// E -> E + T
		{ 0, 1 },	// E -> T
		{ 1, 3 },	// T -> T * F
		{ 1, 1 },	// T -> F
		{ 2, 1 },	// F -> (E)
		{ 2, 1 } 	// F -> id
	};
	
	String nodeText;	// the text to parse
	int nodeOfs;		// current parsing position 
	int nodeOrgOfs;	// current parsed token value
	String nodeToken;	// current parsed token
	Vector<Integer> nodeStack;	// stack, which contains the current state on the top.
	
	int yylex()
	{
		nodeOrgOfs = nodeOfs;
		int token = -1;
		
		nodeToken = "";
		char c;
		while (nodeOfs < nodeText.length()) {
			c = nodeText.charAt(nodeOfs);
			
			int t = -1;
			boolean bTaken = false;
			switch (c) {
			case '+':
				t = ACTION_COL_PLUS;
				bTaken = true;
				break;
			case '*':
				t = ACTION_COL_MULT;
				bTaken = true;
				break;
			case '(':
				t = ACTION_COL_LCOMP;
				bTaken = true;
				break;
			case ')':
				t = ACTION_COL_RCOMP;
				bTaken = true;
				break;
			case '$':
				t = ACTION_COL_END;
				bTaken = true;
				break;
			case 32:
				while ((c = nodeText.charAt(nodeOfs)) == 32) {
					nodeOfs++;
				}				
				c = 0;
				break;
			}
			if (c != 0 && bTaken == false) {
				if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c == '_' || c >= '0' && c <= '9') {
					t = ACTION_COL_ID;
					bTaken = true;
				}
			}			
			if (token != -1 && token != t)
				bTaken = false;
			if (c == 0 || bTaken == false)
				break;
			else
				nodeToken += c;
			token = t;			
			nodeOfs++;
		}
		return token;
	}
	
	// Adding spaces for output
	public String padString(String t, int paddedSize)
	{
		while (t.length() < paddedSize)
			t += " ";
			
		return t;
	}
	
	// Truncate the string with no longer than n characters in result.
	public String limitSize(String t, int n)
	{
		final int len = t.length();
		if (len > n) {
			t = t.substring(0, n - 3) + "...";
		}
		
		return t;
	}
	
	// stack values
	public String getStackContent()
	{
		String t = "";
		for (int i = 0; i < nodeStack.size(); i++) {
			if (i > 0)
				t += " ";
			t += nodeStack.elementAt(i);
		}
		return t;
	}
	
	// current state from the top of stack
	public int getCurrentState()
	{
		return nodeStack.elementAt(nodeStack.size() - 1);
	}
	
	// main parsing function
	public void parse() throws Exception
	{
		System.out.println(nodeText);
		
		nodeOfs = 0;
		nodeOrgOfs = 0;
		nodeStack = new Vector<Integer>();
		nodeStack.add(0);
		System.out.println("                   | input             | action  | action  | value   | length  | goto    | goto    | stack   | parse tree");
		System.out.println("   Stack           | text              | lookup  | value   | of LHS  | of RHS  | lookup  | value   | action  | stack");
		System.out.println("---------------------------------------------------------------------------------------------------------------------------");
		
		boolean bCont = true;
		int token = -1;
		node s = new node(null);	// stack nodes list
		while (bCont) {
			System.out.print(padString(getStackContent(), 20) + limitSize(padString(nodeText.substring(nodeOrgOfs), 20), 20));
			if (token == -1)
				token = yylex();
				
			final int currentState = getCurrentState();
			System.out.print(padString("[" + currentState + ", " + nodeActionName[token] + "]", 10));	// action lookup

			String aval = "";	// action value
			String lhs = "";	// value of LHS
			String rhs = "";	// length of RHS
			String g = "";		// goto lookup
			String gv = "";		// goto lookup
			String sa = "";		// goto lookup
			int action = nodeAction[currentState][token];
			if (action >= 0 && action < NO_ACTION) {				
				nodeOrgOfs = nodeOfs;
				nodeStack.add(action);
				aval = "S" + action;
				sa = "push " + nodeToken + ":" + action;
				s.toList();
				s.addNode(new node(nodeToken));				
				token = -1;
			} else if (action < 0) {				
				action = -action - 2;
				aval = "R" + (action + 1);
				final int lhs_num = nodeRules[action][0];
				final int n = nodeRules[action][1];
				for (int i = 0; i < n; i++)
					nodeStack.remove(nodeStack.size() - 1);
				
				lhs = nodeTermName[lhs_num];
				rhs = String.valueOf(n);
				final int state = getCurrentState();
				final int goto_value = nodeGoto[state][lhs_num];
				nodeStack.add(goto_value);
				g = "[" + state + ", " + lhs + "]";
				gv = String.valueOf(goto_value);
				sa = "push " + lhs + ":" + gv;
				s.reduceLastItems(n, lhs);
			} else if (action == NO_ACTION) {
				throw new Exception("Error in the text content.");
			} else if (action == ACTION_ACCEPT) {
				nodeStack.add(action);
				aval = "accept";
				bCont = false;
			} else 
				throw new Exception("Wrong action " + action);
			
			System.out.print(padString(aval, 10) + padString(lhs, 10) + padString(rhs, 10) + padString(g, 10) + padString(gv, 10) + padString(sa, 10) + s.getLine());
			System.out.println("");
		}
		
		System.out.println("\nParse tree:\n" + s.getTreeDoc() + "\n");
	}
	
	// calling Main function
	static public void main(String args[])
	{
		try {
                                System.out.println("Please enter the word to parse");
                                BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));                                
                                String parserStr = bufferRead.readLine();                                
				parser p = new parser();
				p.nodeText = parserStr;
				p.parse();
		} catch (Exception e) {
			System.out.println("Caught an Exception: " + e.getMessage());
		}
	}
}
