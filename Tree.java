package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	private static boolean isPrevTag = true;
	private static boolean negatedValue = false;
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	private static ArrayList<String> tagsList = new ArrayList<String>();
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
		tagsList.add("html");
		tagsList.add("body");
		tagsList.add("p");
		tagsList.add("em");
		tagsList.add("b");
		tagsList.add("table");
		tagsList.add("tr");
		tagsList.add("td");
		tagsList.add("ol");
		tagsList.add("ul");
		tagsList.add("li");
	}
	
	/**
	 * Builds the DOM tree from input HTML file, through scanner passed
	 * in to the constructor and stored in the sc field of this object. 
	 * 
	 * The root of the tree that is built is referenced by the root field of this object.
	 */
	public void build() {
		/** COMPLETE THIS METHOD **/
		Stack S = new Stack();
		int tableCount = 0;
		Stack S2 = new Stack();
		String firstTag = sc.nextLine();
		if(firstTag.equals("<html>"))
		{
			firstTag = getTagString(firstTag);
			root = new TagNode(firstTag,null,null);
			S.push(root);
			S2.push(root);
		}
		else
		{
			throw new IllegalArgumentException();
		}
		String secondTag = sc.nextLine();
		{
			secondTag = getTagString(secondTag);
			root.firstChild = new TagNode(secondTag,null,null);
			S.push(root.firstChild);
			S2.push(root.firstChild);
		}
		while(S.isEmpty()!=true)
		{
			String word = sc.nextLine();
			String tag = null;
			boolean hasTag = false;
			if(word.charAt(0)=='<')
			{
				tag = getTagString(word);
				hasTag = true;
			}
			if(negatedValue == true)
			{
				TagNode temp = (TagNode) S.pop();
				if(!temp.tag.equals(tag))
				{
					throw new IllegalArgumentException();
				}
				while(true)
				{
					TagNode temp2 = (TagNode)S2.pop();
					if(temp2.equals(temp))
					{
						isPrevTag = true;
						S2.push(temp2);
						break;
					}
				}
				negatedValue = false;
			}
			else if(isPrevTag ==true && hasTag == true)
			{
				if(tag.equals("table"))
				{
					tableCount++;
				}
				if(tableCount>1)
				{
					throw new IllegalArgumentException();
				}

				TagNode tagOne = (TagNode) S2.pop();
				TagNode tagThree = (TagNode) S.pop();
				S.push(tagThree);
				TagNode tagTwo = new TagNode(tag,null,null);
				if(tagTwo.tag.equals(tagThree.tag)&&(!tagTwo.tag.equals("ol")||!tagTwo.tag.equals("ul")))
				{
					throw new IllegalArgumentException();
				}
				if(tagOne.equals(tagThree))
				{
					tagOne.firstChild = tagTwo;
					S2.push(tagOne);
					S2.push(tagOne.firstChild);
					S.push(tagOne.firstChild);
				}
				else
				{
					tagOne.sibling = tagTwo;
					S2.push(tagOne);
					S2.push(tagOne.sibling);
					S.push(tagOne.sibling);
				}
				isPrevTag = true;
			}
			else if(isPrevTag == true && hasTag == false)
			{

				TagNode tagOne = (TagNode) S2.pop();
				TagNode tagThree = (TagNode) S.pop();
				S.push(tagThree);
				TagNode tagTwo = new TagNode(word,null,null);
				if(tagOne.equals(tagThree))
				{
					tagOne.firstChild = tagTwo;
					S2.push(tagOne);
					S2.push(tagOne.firstChild);
				}
				else
				{
					tagOne.sibling = tagTwo;
					S2.push(tagOne);
					S2.push(tagOne.sibling);
				}
				isPrevTag = false;
			}
			else if(isPrevTag == false && hasTag == true)
			{
				if(tag.equals("table"))
				{
					tableCount++;
				}
				if(tableCount>1)
				{
					throw new IllegalArgumentException();
				}
				TagNode tagOne = (TagNode) S2.pop();
				TagNode tagTwo = new TagNode(tag,null,null);
				tagOne.sibling = tagTwo;
				S2.push(tagOne);
				S2.push(tagOne.sibling);
				S.push(tagOne.sibling);
				isPrevTag = true;
			}
			else if(isPrevTag == false &&hasTag == false)
			{
				TagNode tagOne = (TagNode) S2.pop();
				TagNode tagTwo = new TagNode(word,null,null);
				System.out.println("4 Tag: "+ word);
				tagOne.sibling = tagTwo;
				S2.push(tagOne);
				S2.push(tagOne.sibling);
				isPrevTag = false;
			}
		}
	}

	
	
	
	private static Stack removeElement(Stack S2, TagNode element)
	{
		Stack tempStack = new Stack();
		int count = 0;
		while(S2.isEmpty()!=true)
		{
			TagNode temp = (TagNode)S2.pop();
			if(temp.equals(element))
			{
				count++;
			}
			if(count!=1)
			{
				tempStack.push(temp);
			}
		}
		while(tempStack.isEmpty()!=true)
		{
			S2.push(tempStack.pop());
		}
		return S2;
	}

	
	private static boolean hasString(String word,String check)
	{
		check = check.toLowerCase();
		word = word.toLowerCase();

		String temp = "";
		int count = 0;
		for(int x =0;x<word.length();x++)
		{
			temp+=word.charAt(x);	
			if(x == word.length()-1)
			{

				if(temp.charAt(temp.length()-1)=='?'||temp.charAt(temp.length()-1)=='.'||temp.charAt(temp.length()-1)=='!'
				 ||temp.charAt(temp.length()-1)==':'||temp.charAt(temp.length()-1)==','||temp.charAt(temp.length()-1)==';')
				{
					temp.toLowerCase();
					if(temp.substring(0,temp.length()-1).equals(check))
					{
						return true;
					}
					else
					{
						temp = "";
						count = 0;
					}
				}
				
				else
				{
					temp.toLowerCase();
					if(temp.equals(check))
					{
						return true;
					}
					else
					{
						temp = "";
						count = 0;
					}
				}
			}
			else if(temp.charAt(count)==' '&&!temp.equals(" "))
			{
				if((temp.charAt(temp.length()-2)=='?'||temp.charAt(temp.length()-2)=='.'||temp.charAt(temp.length()-2)=='!'
				 ||temp.charAt(temp.length()-2)==':'||temp.charAt(temp.length()-2)==','||temp.charAt(temp.length()-2)==';'))
				{
					temp.toLowerCase();
					if(temp.substring(0,temp.length()-2).equals(check))
					{
						return true;
					}
					else
					{
						temp = "";
						count = 0;
					}
				}
				
				else
				{
					temp.toLowerCase();
					if(temp.substring(0, temp.length()-1).equals(check))
					{
						return true;
					}
					else
					{
						temp = "";
						count = 0;
					}
				}
			}
			else
			{
				count++;
			}
		}
		return false;
	}
	
	
	
	private static int findStartingIndex(String word, String check){
		check = check.toLowerCase();
		word = word.toLowerCase();
		String temp = "";
		int count = 0;
		for(int x =0;x<word.length();x++)
		{
			temp+=word.charAt(x);	
			if(x == word.length()-1)
			{
				if(temp.charAt(temp.length()-1)=='?'||temp.charAt(temp.length()-1)=='.'||temp.charAt(temp.length()-1)=='!'
				 ||temp.charAt(temp.length()-1)==':'||temp.charAt(temp.length()-1)==','||temp.charAt(temp.length()-1)==';')
				{
					temp.toLowerCase();
					if(temp.substring(0,temp.length()-1).equals(check))
					{
						return x-temp.length()+1;
					}
					else
					{
						temp = "";
						count = 0;
					}
				}
				
				else
				{
					temp.toLowerCase();
					if(temp.equals(check))
					{
						return x-temp.length()+1;
					}
					else
					{
						temp = "";
						count = 0;
					}
				}
			}
			else if(temp.charAt(count)==' '&&!temp.equals(" "))
			{
				if((temp.charAt(temp.length()-2)=='?'||temp.charAt(temp.length()-2)=='.'||temp.charAt(temp.length()-2)=='!'
						 ||temp.charAt(temp.length()-2)==':'||temp.charAt(temp.length()-2)==','||temp.charAt(temp.length()-2)==';')&&!temp.equals(""))
				{
					temp.toLowerCase();
					if(temp.substring(0,temp.length()-2).equals(check))
					{
						return x-temp.length()+1;
					}
					else
					{
						temp = "";
						count = 0;
					}
				}
				
				else
				{
					temp.toLowerCase();
					if(temp.substring(0,temp.length()-1).equals(check))
					{
						return x-temp.length()+1;
					}
					else
					{
						temp = "";
						count = 0;
					}
				}
			}
			else
			{
				count++;
			}
		}
		return 0;
	}
	private static int findEndingIndex(String word,String check)
	{
		check = check.toLowerCase();
		word = word.toLowerCase();
		String temp = "";
		int count = 0;
		for(int x =0;x<word.length();x++)
		{
			temp+=word.charAt(x);	
			if(x == word.length()-1)
			{
				if((temp.charAt(temp.length()-1)=='?'||temp.charAt(temp.length()-1)=='.'||temp.charAt(temp.length()-1)=='!'
						 ||temp.charAt(temp.length()-1)==':'||temp.charAt(temp.length()-1)==','||temp.charAt(temp.length()-1)==';')&&!temp.equals(""))
				{
					temp.toLowerCase();
					if(temp.substring(0,temp.length()-1).equals(check))
					{
						return x+1;
					}
					else
					{
						temp = "";
						count = 0;
					}
				}
				
				else
				{
					temp.toLowerCase();
					if(temp.equals(check))
					{
						return x+1;
					}
					else
					{
						temp = "";
						count = 0;
					}
				}
			}
			else if(temp.charAt(count)==' '&&!temp.equals(" "))
			{
				if(temp.charAt(temp.length()-2)=='?'||temp.charAt(temp.length()-2)=='.'||temp.charAt(temp.length()-2)=='!'
				 ||temp.charAt(temp.length()-2)==':'||temp.charAt(temp.length()-2)==','||temp.charAt(temp.length()-2)==';')
				{
					temp.toLowerCase();
					if(temp.substring(0,temp.length()-2).equals(check))
					{
						return x;
					}
					else
					{
						temp = "";
						count = 0;
					}
				}
				
				else
				{
					temp.toLowerCase();
					if(temp.substring(0,temp.length()-1).equals(check))
					{
						return x;
					}
					else
					{
						temp = "";
						count = 0;
					}
				}
			}
			else
			{
				count++;
			}
		}
		return 0;
	}
	
	private static String findString(String word)
	{
		String temp = "";
	
		for(int x =0;x<word.length();x++)
		{
			if(temp.charAt(x)==' '||x == word.length()-1)
			{
				
			}
			temp+=word.charAt(x);
		}
		return temp;
	}
	
	private static String getTagString(String word)
	{
		String temp = "";
		boolean isValid = true;
		if(word.charAt(0)!='<'||word.charAt(word.length()-1)!='>')
		{
			return temp;
		}
		if(word.charAt(1)=='/')
		{
			negatedValue = true;
			for(int x = 2;x<word.length()-1;x++)
			{
				if((word.charAt(x)<'a'||word.charAt(x)>'z')&&(word.charAt(x)<'A'||word.charAt(x)>'Z'))
				{
					isValid = false;
				}
				temp+=word.charAt(x);
			}
		}
		else
		{
			for(int x = 1;x<word.length()-1;x++)
			{
				if((word.charAt(x)<'a'||word.charAt(x)>'z')&&(word.charAt(x)<'A'||word.charAt(x)>'Z'))
				{
					isValid = false;
				}
				temp+=word.charAt(x);
			}
		}
		if(isValid==false)
		{
			negatedValue = false;
			throw new IllegalArgumentException();
		}
		return temp;
	}
	
	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		/** COMPLETE THIS METHOD **/
		Stack S = new Stack();
		Stack S2 = new Stack();
		TagNode first = root;
		S.push(first);
		while(S.isEmpty()!=true)
		{
			while(S.isEmpty()!=true)
			{
				TagNode current = (TagNode)S.pop();
				if(current.sibling!=null)
				{
					if(current.sibling.tag.equals(oldTag))
					{
						current.sibling.tag = newTag;
						System.out.println(current.sibling);
					}
					S2.push(current.sibling);
				}
				if(current.firstChild!=null)
				{
					if(current.firstChild.tag.equals(oldTag))
					{
						current.firstChild.tag = newTag;
						System.out.println(current.firstChild);
					}
					S2.push(current.firstChild);
				}
			}
			while(S2.isEmpty()!=true)
			{
				S.push(S2.pop());
			}
		}
	}
	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		/** COMPLETE THIS METHOD **/
		Stack S = new Stack();
		Stack S2 = new Stack();
		TagNode first = root;
		S.push(first);
		int numberRows = 0;
		while(S.isEmpty()!=true)
		{
			while(S.isEmpty()!=true)
			{
				TagNode node = (TagNode) S.pop();
				TagNode siblingNode = node.sibling;
				TagNode childNode = node.firstChild;
				if(childNode!=null)
				{
					if(childNode.tag.equals("tr"))
					{
						numberRows++;
						if(numberRows==row)
						{
							if(childNode.firstChild.tag.equals("td"))
							{
								TagNode tdNode = childNode.firstChild;
								while(tdNode!=null)
								{
									if(tdNode.firstChild.tag.equals("em"))
									{
										TagNode text = tdNode.firstChild.firstChild;
										tdNode.firstChild.firstChild = new TagNode("b",null,null);
										tdNode.firstChild.firstChild.firstChild = text;
									}
									else
									{
										TagNode text = tdNode.firstChild;
										tdNode.firstChild = new TagNode("b",null,null);
										tdNode.firstChild.firstChild = text;
									}
									tdNode = tdNode.sibling;
								}
								return;
							}
						}
					}
					if(siblingNode!=null)
					{
					if(siblingNode.tag.equals("tr"))
					{
						numberRows++;
						if(numberRows==row)
						{
							if(siblingNode.firstChild.tag.equals("td"))
							{
								TagNode tdNode = siblingNode.firstChild;
								while(tdNode!=null)
								{
									if(tdNode.firstChild.tag.equals("em"))
									{
										TagNode text = tdNode.firstChild.firstChild;
										tdNode.firstChild.firstChild = new TagNode("b",null,null);
										tdNode.firstChild.firstChild.firstChild = text;
									}
									else
									{
										TagNode text = tdNode.firstChild;
										tdNode.firstChild = new TagNode("b",null,null);
										tdNode.firstChild.firstChild = text;
									}
									tdNode = tdNode.sibling;
								}
								return;
							}
						}
					}
					}
				}
				if(siblingNode!=null)
				{
				S2.push(siblingNode);
				}
				if(childNode!=null)
				{
				S2.push(childNode);
				}
			}
			while(S2.isEmpty()!=true)
			{
				S.push(S2.pop());
			}
		}
	}
	
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		/** COMPLETE THIS METHOD **/
		Stack S = new Stack();
		Stack S2 = new Stack();
		TagNode temp = root;
		if(root.tag.equals(tag))
		{
			root = root.firstChild;
			return;
		}
		S.push(temp);
		while(S.isEmpty()!=true)
		{
			while(S.isEmpty()!=true)
			{
				TagNode node = (TagNode) S.pop();
				if(node.firstChild!=null) {
				if(node.firstChild.tag.equals(tag))
				{
					if(node.firstChild.tag.equals("ol")||node.firstChild.tag.equals("ul"))
					{
						if(node.firstChild.firstChild.tag.equals("li")) 
						{
							TagNode siblingNode = node.firstChild.sibling;
							node.firstChild = node.firstChild.firstChild;
							node.firstChild.tag = "p";
							TagNode temp2 = node.firstChild;
							while(true)
							{
								if(temp2.sibling==null)
								{
									break;
								}
								temp2.sibling.tag="p";
								temp2 = temp2.sibling;							
							}
							temp2.sibling = siblingNode;
						}
						else
						{
							node.firstChild = node.firstChild.firstChild;
						}
					}
					else
					{
						TagNode deleteNode = node.firstChild;
						TagNode newNode = node.firstChild.firstChild;
						while(true)
						{
							if(newNode.sibling==null)
							{
								break;
							}
							newNode=newNode.sibling;
						}
						newNode.sibling=deleteNode.sibling;
						node.firstChild=node.firstChild.firstChild;
					}
				}
				}
				if(node.sibling!=null) {
				if(node.sibling.tag.equals(tag))
				{
					if(node.sibling.tag.equals("ol")||node.sibling.tag.equals("ul"))
					{
						if(node.sibling.firstChild.tag.equals("li")) 
						{
							TagNode siblingNode = node.sibling.sibling;
							node.sibling = node.sibling.firstChild;
							node.sibling.tag = "p";
							TagNode temp2 = node.sibling;
							while(true)
							{
								if(temp2.sibling==null)
								{
									break;
								}
								temp2.sibling.tag="p";
								temp2 = temp2.sibling;							
							}
							temp2.sibling = siblingNode;
						}
						else
						{
							node.firstChild = node.firstChild.firstChild;
						}
					}
					else
					{
						TagNode deleteNode = node.sibling;
						TagNode newNode = node.sibling.firstChild;
						while(true)
						{
							if(newNode.sibling==null)
							{
								break;
							}
							newNode=newNode.sibling;
						}
						newNode.sibling=deleteNode.sibling;
						node.sibling=node.sibling.firstChild;
					}
				}
				}
				if(node.sibling!=null)
				{
				S2.push(node.sibling);
				}
				if(node.firstChild!=null)
				{
				S2.push(node.firstChild);
				}
			}
			while(S2.isEmpty()!=true)
			{
				S.push(S2.pop());
			}
		
		}
	}
	
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */

	public void addTag(String word, String tag) {
		/** COMPLETE THIS METHOD **/
		Stack S = new Stack();
		Stack S2 = new Stack();
		TagNode first = root;
		S.push(first);
		while(S.isEmpty()!=true)
		{
			while(S.isEmpty()!=true)
			{
				TagNode current = (TagNode) S.pop();
				if(current.firstChild!=null) {
				if(hasString(current.firstChild.tag,word)==true)
				{
					String text = current.firstChild.tag;
					TagNode childTag = current.firstChild;
					TagNode childSibTag = current.firstChild.sibling;
					int startingIndex = findStartingIndex(text, word);
					int endingIndex = findEndingIndex(text,word);
					System.out.println(text.substring(startingIndex,endingIndex));
					if(startingIndex==0)
					{
						if(text.length()==word.length()||(text.length()==word.length()+1&&word.length()+1 == endingIndex-startingIndex))
						{
						 current.firstChild = new TagNode(tag,new TagNode(text,null,null),childSibTag);
						 if(childSibTag!=null)
						 {
							S2.push(childSibTag);
						 }
						}
						else
						{
							current.firstChild = new TagNode(tag,new TagNode(text.substring(0,endingIndex),null,null),new TagNode(text.substring(endingIndex),null,childSibTag));
							S2.push(current.firstChild.sibling);
						}
					}
					else if(text.length()==endingIndex)
					{
						current.firstChild = new TagNode(text.substring(0,startingIndex),null,new TagNode(tag,new TagNode(text.substring(startingIndex),null,null),childSibTag));
						if(childSibTag!=null)
						{
							S2.push(childSibTag);
						}
					}
					else
					{
						current.firstChild = new TagNode(text.substring(0,startingIndex),null,new TagNode(tag,new TagNode(text.substring(startingIndex,endingIndex),null,null),new TagNode(text.substring(endingIndex),null,childSibTag)));
						S2.push(current.firstChild.sibling.sibling);
					}
				}
				else
				{
					S2.push(current.firstChild);
				}
				}
				
				if(current.sibling!=null)
				{
					if(hasString(current.sibling.tag,word)==true)
					{
						String text = current.sibling.tag;
						TagNode sibTag = current.sibling;
						TagNode SibSibTag = current.sibling.sibling;
						int startingIndex = findStartingIndex(text, word);
						int endingIndex = findEndingIndex(text,word);
						if(startingIndex==0)
						{
							if(text.length()==word.length()||(text.length()==word.length()+1&&word.length()+1 == endingIndex-startingIndex))
							{
							 current.sibling = new TagNode(tag,new TagNode(text,null,null),SibSibTag);
							 if(SibSibTag!=null)
							 {
								S2.push(SibSibTag);
							 }
							}
							else
							{
								current.sibling = new TagNode(tag,new TagNode(text.substring(0,endingIndex),null,null),new TagNode(text.substring(endingIndex),null,SibSibTag));
								S2.push(current.sibling.sibling);
							}
						}
						else if(text.length()==endingIndex)
						{
							current.sibling = new TagNode(text.substring(0,startingIndex),null,new TagNode(tag,new TagNode(text.substring(startingIndex),null,null),SibSibTag));
							if(SibSibTag!=null)
							{
								S2.push(SibSibTag);
							}
						}
						else
						{
							current.sibling = new TagNode(text.substring(0,startingIndex),null,new TagNode(tag,new TagNode(text.substring(startingIndex,endingIndex),null,null),new TagNode(text.substring(endingIndex),null,SibSibTag)));
							S2.push(current.sibling.sibling.sibling);
						}
					}
					else
					{
						S2.push(current.sibling);
					}
				}
			}
			while(S2.isEmpty()!=true)
			{
				S.push(S2.pop());
			}
		}
		
	}
	
	
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
	/**
	 * Prints the DOM tree. 
	 *
	 */
	public void print() {
		print(root, 1);
	}
	
	private void print(TagNode root, int level) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			for (int i=0; i < level-1; i++) {
				System.out.print("      ");
			};
			if (root != this.root) {
				System.out.print("|---- ");
			} else {
				System.out.print("      ");
			}
			System.out.println(ptr.tag);
			if (ptr.firstChild != null) {
				print(ptr.firstChild, level+1);
			}
		}
	}
}
