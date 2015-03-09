/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucel_updater.utils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * PropertiesUtil is used to load and save properties to files.
 * <p>
 * @author Robin Sharp
 */

public class PropertiesUtil
{

    public PropertiesUtil()
    {
    }
    
    public static String getByKey( Properties properties, String value )
    {
        Map map = new HashMap();
        Iterator iter = properties.entrySet().iterator();
        do
        {
            if(!iter.hasNext() )
                break;
            java.util.Map.Entry entry = (java.util.Map.Entry)iter.next();
            if(entry.getKey().equals(value ) )
                return (String)entry.getValue();
        } while(true );
        return "missing value";
    }

    public static Map getByValue( Properties properties, String value )
    {
        Map map = new HashMap();
        Iterator iter = properties.entrySet().iterator();
        do
        {
            if(!iter.hasNext() )
                break;
            java.util.Map.Entry entry = (java.util.Map.Entry)iter.next();
            if(entry.getValue().equals(value ) )
                map.put(entry.getKey(), entry.getValue() );
        } while(true );
        return map;
    }

    public static Properties load( Properties properties, String fileName ) throws IOException
    {
        if(fileName == null )
        {
            throw new IllegalArgumentException( "FileName is not set." );
        }
        else
        {
            return load( properties, new File(fileName ) );
        }
    }

    public static Properties load( Properties properties, File file ) throws IOException
    {
        if(file == null )
        {
            throw new IllegalArgumentException( "File is not set." );
        }
        else
        {
            return load( properties, ((InputStream ) ( new FileInputStream(file ) )) );
        }
    }

    public static Properties load( Properties properties, URL url ) throws IOException
    {
        if( url == null )
        {
            throw new IllegalArgumentException( "Url is not set." );
        } 
        else
        {
            URLConnection connection = url.openConnection();
            return load( properties, connection.getInputStream() );
        }
    }

    public static Properties load( Properties properties, Class relativeClass, String relativeFileName ) throws IOException
    {
        if(relativeClass == null )
        {
            throw new IllegalArgumentException( "Relative Class is not set." );
        }
        if(relativeFileName == null )
        {
            throw new IllegalArgumentException( "Relative File Name is not set." );
        }
        else
        {
            return load( properties, relativeClass.getResourceAsStream(relativeFileName ) );
        }
    }

    public static Properties load( Properties properties, InputStream inputStream ) throws IOException
    {
        try
        {
            if( properties == null )
            {
                throw new IllegalArgumentException( "Properties is not set." );
            }
            
            if( inputStream == null )
            {
                throw new IllegalArgumentException( "InputStream is not set." );
            }
            
            properties.load( new BufferedInputStream(inputStream ) );
        }
        finally
        {
            if(inputStream != null )
                inputStream.close();
        }
        return properties;
    }

    public static void store( Properties properties, String fileName ) throws IOException
    {
        if(fileName == null )
        {
            throw new IllegalArgumentException( "FileName is not set." );
        } 
        else
        {
            store( properties, new File(fileName ) );
            return;
        }
    }

    public static void store( Properties properties, File file )
        throws IOException
    {
        if(file == null )
        {
            throw new IllegalArgumentException( "File is not set." );
        } else
        {
            store( properties, ((OutputStream ) ( new FileOutputStream(file ) )) );
            return;
        }
    }

    public static void store( Properties properties, URL url ) throws IOException
    {
        if( url == null )
        {
            throw new IllegalArgumentException( "Url is not set." );
        } else
        {
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true );
            store( properties, connection.getOutputStream() );
            return;
        }
    }

    public static void store( Properties properties, Class relativeClass, String relativeFileName )
        throws IOException
    {
        if(relativeClass == null )
            throw new IllegalArgumentException( "Relative Class is not set." );
        if(relativeFileName == null )
        {
            throw new IllegalArgumentException( "Relative File Name is not set." );
        } 
        else
        {
            String className = relativeClass.getName().substring(relativeClass.getName().lastIndexOf( "." ) + 1);
            URL url = relativeClass.getResource(String.valueOf(String.valueOf( className ) ).concat( ".class" ) );
            String fileName = url.getFile();
            fileName = String.valueOf(fileName.substring(1, fileName.length() - className.length() - 6) ) + String.valueOf(relativeFileName );
            store( properties, fileName );
            return;
        }
    }

    public static void store( Properties properties, OutputStream outputStream ) throws IOException
    {
        try
        {
            if( properties == null )
            {
                throw new IllegalArgumentException( "Properties is not set." );
            }
            if( outputStream == null )
            {
                throw new IllegalArgumentException( "OutputStream is not set." );
            }
            properties.store( new BufferedOutputStream( outputStream ), null );
        }
        finally
        {
            if( outputStream != null )
            {
                outputStream.close();
            }
        }
    }

    public static void main(String args[])
    {
        try
        {
            /*Properties p = new Properties();
            p.put("altisfr.ftp.host","http://altisfr.com/lucel");
            p.put("altisfr.ftp.username","lucel");
            p.put("altisfr.ftp.pass","caca");
            store(p, "config.properties");*/
            
           /* System.out.println(load( new Properties(), "C:/data/ccm_wa/dordev/Countrywide~csdrzs/Countrywide/src/com/javelin/util/test.properties" ) );
            System.out.println(load( new Properties(), new File( "C:/data/ccm_wa/dordev/Countrywide~csdrzs/Countrywide/src/com/javelin/util/test.properties" ) ));
            System.out.println(load( new Properties(), new URL( "file:///C:/data/ccm_wa/dordev/Countrywide~csdrzs/Countrywide/src/com/javelin/util/test.properties" ) ));
            System.out.println(load( new Properties(), PropertiesUtil.class, "test.properties" ) );
            Properties properties = new Properties();
            properties.put( "1", "2" );
            properties.put( "2", "2" );
            properties.put( "3", "3" );
            store( properties, "C:/data/ccm_wa/dordev/Countrywide~csdrzs/Countrywide/src/com/javelin/util/test1.properties" );
            store( properties, new File( "C:/data/ccm_wa/dordev/Countrywide~csdrzs/Countrywide/src/com/javelin/util/test2.properties" ) );
            store( properties, PropertiesUtil.class, "test4.properties" );
            System.out.println(getByValue( properties, "2" ) );*/
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
    }

}