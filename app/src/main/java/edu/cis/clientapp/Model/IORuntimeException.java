package edu.cis.clientapp.Model;

public class IORuntimeException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public IORuntimeException(String s)
    {
        super(s);
    }

    public IORuntimeException(String s, Throwable t)
    {
        super(s, t);
    }

    public IORuntimeException(Throwable t)
    {
        super(t);
    }
}
