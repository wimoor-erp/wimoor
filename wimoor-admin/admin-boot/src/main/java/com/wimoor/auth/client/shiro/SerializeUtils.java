package com.wimoor.auth.client.shiro;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SerializeUtils {
    private static Logger logger = LoggerFactory.getLogger(SerializeUtils.class);

    public SerializeUtils() {
    }

    public static Object deserialize(byte[] bytes) {
        Object result = null;
        if (isEmpty(bytes)) {
            return null;
        } else {
            try {
                ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);

                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(byteStream);

                    try {
                        result = objectInputStream.readObject();
                    } catch (ClassNotFoundException var5) {
                        throw new Exception("Failed to deserialize object type", var5);
                    }
                } catch (Throwable var6) {
                    throw new Exception("Failed to deserialize", var6);
                }
            } catch (Exception var7) {
                logger.error("Failed to deserialize", var7);
            }
            if(result!=null) {
            	if(result instanceof LinkedList&&((LinkedList<?>)result).size()==0) {
            		return null;
            	}else if(result instanceof AtomicInteger && ((AtomicInteger)result).intValue()==0) {
            		return null;
            	}
            }
            return result;
        }
    }

    public static boolean isEmpty(byte[] data) {
        return data == null || data.length == 0;
    }

    public static byte[] serialize(Object object) {
        byte[] result = null;
        if (object == null) {
            return new byte[0];
        } else {
            try {
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream(2048);

                try {
                    if (!(object instanceof Serializable)) {
                        throw new IllegalArgumentException(SerializeUtils.class.getSimpleName() + " requires a Serializable payload " + "but received an object of type [" + object.getClass().getName() + "]");
                    }

                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteStream);
                    objectOutputStream.writeObject(object);
                    objectOutputStream.flush();
                    result = byteStream.toByteArray();
                } catch (Throwable var4) {
                    throw new Exception("Failed to serialize", var4);
                }
            } catch (Exception var5) {
                logger.error("Failed to serialize", var5);
            }

            return result;
        }
    }
}