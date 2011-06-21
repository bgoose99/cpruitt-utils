package javautils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/*******************************************************************************
 * Contains some useful XML functions.
 ******************************************************************************/
public final class XMLUtils
{
    /***************************************************************************
     * Returns the value of the first child under the give element with the
     * given name.
     * 
     * @param e
     * @param name
     * @return
     * @throws Exception
     **************************************************************************/
    public static String getChildValueByName( Element e, String name )
            throws Exception
    {
        String s = "Not found";

        /*
         * The getElementsByTagName() function returns ANY children under the
         * given element with the given tag name. This function is intended to
         * return the value of only an immediate child with a given name.
         */
        NodeList childNodes = e.getChildNodes();
        for( int i = 0; i < childNodes.getLength(); i++ )
        {
            Node node = childNodes.item( i );
            if( node.getNodeType() != Node.ELEMENT_NODE )
                continue;

            if( node.getNodeName().equals( name ) )
            {
                if( node.getFirstChild() != null )
                    s = node.getFirstChild().getNodeValue();
                else
                    s = "";
                break;
            }
        }

        return s;
    }

    /***************************************************************************
     * Sets the value of the first child under the given element with the given
     * name. If the child has no nodes, one is created.
     * 
     * @param doc
     * @param e
     * @param name
     * @param value
     * @return
     * @throws Exception
     **************************************************************************/
    public static boolean setChildValueByName( Document doc, Element e,
            String name, String value ) throws Exception
    {
        NodeList childNodes = e.getChildNodes();
        for( int i = 0; i < childNodes.getLength(); i++ )
        {
            Node node = childNodes.item( i );
            if( node.getNodeType() != Node.ELEMENT_NODE )
                continue;

            if( node.getNodeName().equals( name ) )
            {
                Node child = node.getFirstChild();
                if( child == null )
                    ( (Element)node ).appendChild( doc.createTextNode( value ) );
                else
                    child.setNodeValue( value );

                return true;
            }
        }

        return false;
    }

    /***************************************************************************
     * Searches nodes in the given list for a child with a given name and value.
     * 
     * @param list
     * @param childName
     * @param value
     * @return
     * @throws Exception
     **************************************************************************/
    public static Node searchNodes( NodeList list, String childName,
            String value ) throws Exception
    {
        for( int i = 0; i < list.getLength(); i++ )
        {
            Node node = list.item( i );
            if( node.getNodeType() != Node.ELEMENT_NODE )
                continue;

            if( value.equals( getChildValueByName( (Element)node, childName ) ) )
                return node;
        }

        return null;
    }
}
