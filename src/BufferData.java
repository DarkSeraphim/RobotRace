/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.nio.Buffer;

/**
 *
 * @author Mark Hendriks & Gabriel Garcia
 */
public class BufferData
{
    public static class Key
    {
        private final float r;
        
        private final int scalex;
        
        private final int scaley;
        
        private final int hash;
        
        public Key(float radius, int scalex, int scaley)
        {
            this.r = radius;
            this.scalex = scalex;
            this.scaley = scaley;
            
            int hash = 3;
            hash = 37*hash + Float.floatToIntBits(this.r);
            hash = 37*hash + this.scalex;
            hash = 37*hash + this.scaley;
            this.hash = hash;
        }
        
        @Override
        public int hashCode()
        {
            return this.hash;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj == null)
            {
                return false;
            }
            if (getClass() != obj.getClass())
            {
                return false;
            }
            final BufferData.Key other = (BufferData.Key) obj;
            if (Float.floatToIntBits(this.r) != Float.floatToIntBits(other.r))
            {
                return false;
            }
            if (this.scalex != other.scalex)
            {
                return false;
            }
            if (this.scaley != other.scaley)
            {
                return false;
            }
            return true;
        }
    }
    
    private final int size;
    private final int esize;
    private final int nsize;
    private final int id;
    private final int eid;
    private final int nid;

    public BufferData(int id, Buffer buf, int nid, Buffer nbuf, int eid, Buffer ebuf)
    {
        this.id = id;
        this.size = buf.capacity();
        this.eid = eid;
        this.esize = ebuf.capacity();
        this.nid = nid;
        this.nsize = nbuf.capacity();
    }

    public int getSize()
    {
        return this.size;
    }
    public int getElementSize()
    {
        return this.esize;
    }
    
    public int getNormalSize()
    {
        return this.nsize;
    }

    public int getVertexId()
    {
        return this.id;
    }

    public int getElementId()
    {
        return this.eid;
    }
    
    public int getNormalId()
    {
        return this.nid;
    }
    
    public static BufferData.Key createKey(float radius, int scalex, int scaley)
    {
        return new BufferData.Key(radius, scalex, scaley);
    }
}

